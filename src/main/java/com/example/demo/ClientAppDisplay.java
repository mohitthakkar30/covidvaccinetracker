package com.example.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;


/**
 * Servlet implementation class ClientAppDisplay
 */
public class ClientAppDisplay extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	static 
	{
	    System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
//		String path="/home/mohit/eclipse-workspace";
		
		String server= "../fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/connection-org1.yaml";
		
		String walletPath = "./wallet";
		
		System.out.println("Display Called.....");
		
		PrintWriter out=response.getWriter();
		response.setContentType("text/html");
//		response.setContentType("application/json; charset=UTF-8");
		Path fullWalletPath = Paths.get(walletPath);
		Wallet wallet = Wallets.newFileSystemWallet(fullWalletPath);
		
		Path networkConfigPath = Paths.get(server);
        
        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);
        
     // create a gateway connection
        try (Gateway gateway = builder.connect()) 
        {
        	
 
            // get the network and contract
        	
        	Network network = gateway.getNetwork("samplechannel");
            Contract contract = network.getContract("CovidVT");
	
            String key=request.getParameter("key");
            System.out.println("key:- "+key);
            
            byte[] result;
            
            result = contract.evaluateTransaction("queryPerson", key);
           
            System.out.println(new String(result));
            
            
         //-------------------------------------------------------------------------------------------------------------------------
           
           String str =new String(result);

         System.out.println("====>"+str);
         out.print(str);
            
        //------------------------------------------------------------------------------------------------------------------------    
            
        } catch (ContractException e) {
			// TODO Auto-generated catch block
        	
        	out.print("Data Not Exist....");
        	out.print(" <a href='../FebDemoNew/index.html'>Click Here to Insert</a> ");
        	
			e.printStackTrace();
		}
	
	
	}

}
