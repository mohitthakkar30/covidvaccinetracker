package com.example.demo;

import org.hyperledger.fabric.gateway.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;


@Controller
public class ClientAppController {

    @Value("${connection.pem.path}")
    private String connectionPemPath;

    @Value("${wallet.path}")
    private String walletPath;

    @Value("${HFCAClient.server}")
    private String hostName;

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    @RequestMapping("/index")
    public String loadUserForm() throws InterruptedException {

        return "index.html";

    }

    @PostMapping("/ClientApp")
    public String postUserForm(ModelMap modelMap, @RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "age", required = false) String age,
                               @RequestParam(value = "id", required = false) String id,
                               @RequestParam(value = "key", required = false) String key,
                               @RequestParam(value = "gender", required = false) String gender,
                               @RequestParam(value = "vaccineName", required = false) String vaccineName,
                               @RequestParam(value = "dose", required = false) String dose,
                               @RequestParam(value = "vaccinationDate", required = false) String vaccinationDate,
                               HttpServletRequest request, HttpServletResponse response) {
        try {
            System.out.println("Service called...");

            Path fullWalletPath = Paths.get(walletPath);

            Wallet wallet = Wallets.newFileSystemWallet(fullWalletPath);

            // load a CCP
            Path networkConfigPath = Paths.get(connectionPemPath);

            Gateway.Builder builder = Gateway.createBuilder();
            builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);

            // create a gateway connection
            try (Gateway gateway = builder.connect()) {

                // get the network and contract
                Network network = gateway.getNetwork("samplechannel");
                Contract contract = network.getContract("CovidVT");

                System.out.println("Name:- " + name);
                System.out.println("Age:- " + age);
                System.out.println("id:- " + id);
                System.out.println("key:- " + key);
                System.out.println("gender:- " + gender);
                System.out.println("vaccineName:- " + vaccineName);
                System.out.println("dose:- " + dose);
                System.out.println("date:- " + vaccinationDate);

                //, "1", "Mohit","21","male","123123123","Covaxin","30-08-2000",""
                byte[] result;

                contract.submitTransaction("addPerson", key, name, age, gender, id, vaccineName, vaccinationDate, dose);
                return "thankyou.html";

            } catch (Exception e1) {
                System.out.println("Contract Exception Called...");
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exist.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "index.html";
        }
    }

    @RequestMapping("/update")
    public String loadUpdateUserForm() {
        return "update.html";
    }

    @PostMapping("/updateUser")
    public String updateUser(ModelMap modelMap,
                             @RequestParam(value = "key", required = false) String key,
                             @RequestParam(value = "dose", required = false) String dose,
                             HttpServletRequest request, HttpServletResponse response) {
        try {
            System.out.println("Client Service Called.....");

            Path fullWalletPath = Paths.get(walletPath);
            Wallet wallet = Wallets.newFileSystemWallet(fullWalletPath);

            // load a CCP
            Path networkConfigPath = Paths.get(connectionPemPath);

            Gateway.Builder builder = Gateway.createBuilder();
            builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);

            // create a gateway connection
            try (Gateway gateway = builder.connect()) {

                // get the network and contract
                Network network = gateway.getNetwork("samplechannel");
                Contract contract = network.getContract("CovidVT");

                System.out.println("key:- " + key);
                System.out.println("dose:- " + dose);

                byte[] result;

                contract.submitTransaction("changePersonDose", key, dose);
                result = contract.evaluateTransaction("queryPerson", "1");
                System.out.println(new String(result));

                return "thankyou.html";

            } catch (ContractException | TimeoutException | InterruptedException e1) {
                e1.printStackTrace();
                return "notexist.html";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "update.html";
        }

    }

    @RequestMapping("/display")
    public String loadDisplayUser(ModelMap modelMap){
        return "display.html";
    }

    @GetMapping(value = "/clientAppDisplay",produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> displayClient(ModelMap modelMap,
                                        @RequestParam(value = "key", required = false) String key,
                                        HttpServletRequest request, HttpServletResponse response){
        Map<String,String> clientData = new HashMap<>();
       String str;
        System.out.println("Display Called.....");
        byte[] result={};

        try {
            Path fullWalletPath = Paths.get(walletPath);
            Wallet wallet = Wallets.newFileSystemWallet(fullWalletPath);

            Path networkConfigPath = Paths.get(connectionPemPath);

            Gateway.Builder builder = Gateway.createBuilder();
            builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);

            // create a gateway connection
            try (Gateway gateway = builder.connect())
            {
                // get the network and contract
                Network network = gateway.getNetwork("samplechannel");
                Contract contract = network.getContract("CovidVT");

                System.out.println("key:- "+key);

                result = contract.evaluateTransaction("queryPerson", key);
                str =new String(result);

                System.out.println("====>"+str);

                clientData.put("data",str);
                return new ResponseEntity<String>(str,HttpStatus.OK);

            } catch (ContractException e) {
                e.printStackTrace();
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
