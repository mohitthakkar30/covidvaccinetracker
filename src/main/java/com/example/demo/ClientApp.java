package com.example.demo;

import org.hyperledger.fabric.gateway.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Servlet implementation class ClientApp
 */
public class ClientApp extends HttpServlet {
    private static final long serialVersionUID = 1L;


    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    @Override
    public void init() throws ServletException {
        // TODO Auto-generated method stub
        System.out.println("init called...");
//		EnrollAdmin.enrollAdmin();
//		EnrollRegisterClientUser.enrollRegisterClientUser();
    }


    /**
     * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
     */
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//		String path="/home/mohit/eclipse-workspace";
        String server = "../fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/connection-org1.yaml";
        String walletPath = "./wallet";


        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        System.out.println("Service called...");

        Path fullWalletPath = Paths.get(walletPath);
//		  Path fullWalletPath = Paths.get("/tmp/wallet");

        //	  System.out.println(walletPath.toString());
        Wallet wallet = Wallets.newFileSystemWallet(fullWalletPath);
        // load a CCP
        Path networkConfigPath = Paths.get(server);

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);

        // create a gateway connection
        try (Gateway gateway = builder.connect()) {


            // get the network and contract

            Network network = gateway.getNetwork("samplechannel");
            Contract contract = network.getContract("CovidVT");

            String name = request.getParameter("name");
            String age = request.getParameter("age");
            String id = request.getParameter("id");
            String key = request.getParameter("key");
            String gender = request.getParameter("gender");
            String vaccinename = request.getParameter("vaccinename");
            String dose = request.getParameter("dose");
            String date1 = request.getParameter("date-of-vaccination");

            System.out.println("Name:- " + name);
            System.out.println("Age:- " + age);
            System.out.println("id:- " + id);
            System.out.println("key:- " + key);
            System.out.println("gender:- " + gender);
            System.out.println("vaccinedose:- " + vaccinename);
            System.out.println("dose:- " + dose);
            System.out.println("date:- " + date1);


            //, "1", "Mohit","21","male","123123123","Covaxin","30-08-2000",""
            byte[] result;

            contract.submitTransaction("addPerson", key, name, age, gender, id, vaccinename, date1, dose);



            /*
             * try { result = contract.evaluateTransaction("queryPerson", "5001");
             * System.out.println(new String(result)); } catch (ContractException e) { //
             * TODO Auto-generated catch block e.printStackTrace(); }
             */

            //           contract.submitTransaction("changePersonDose", "1", "3");
            //         result = contract.evaluateTransaction("queryPerson", "1");
            //       System.out.println(new String(result));

            RequestDispatcher rd = request.getRequestDispatcher("/thankyou.html");

            rd.forward(request, response);

        } catch (Exception e1) {
	        	
	 /*       	out.print("body { background:rgb(30,30,40);position:fixed;top:0;left:0;bottom:0;right:0;font-family: 'Montserrat', Arial, Helvetica, sans-serif;}");
	        	out.print("<html>");
	        	out.print("<body>");
	        	out.print("Data Already Exist....");
	        	out.print(" <a href='../FebDemoNew/update.html'>Click Here to Update</a> ");
	        	
	        	out.print("</body>");
	        	out.print("</html>");
	   */
            RequestDispatcher rd = request.getRequestDispatcher("/exist.html");
            rd.forward(request, response);


            System.out.println("Contract Exception Called...");
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


    } //Service End....


}
