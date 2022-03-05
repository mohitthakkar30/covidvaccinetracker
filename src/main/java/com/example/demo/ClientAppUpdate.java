package com.example.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;

/**
 * Servlet implementation class ClientAppUpdate
 */
public class ClientAppUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	static 
	{
	    System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		String path="/home/mohit/eclipse-workspace";
		
		String server= "../fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/connection-org1.yaml";
		
		String walletPath = "./wallet";
		
		PrintWriter out =response.getWriter();
		response.setContentType("text/html");
		
	System.out.println("Client Service Called.....");

	 	Path fullWalletPath = Paths.get(walletPath);
		//	  System.out.println(walletPath.toString());
		        Wallet wallet = Wallets.newFileSystemWallet(fullWalletPath);
		        // load a CCP
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
		    		String dose=request.getParameter("dose");
		    		
		            
		    		System.out.println("key:- "+key);
		    		System.out.println("dose:- "+dose);
		            
		            
		            
		      
		            //, "1", "Mohit","21","male","123123123","Covaxin","30-08-2000",""
		            byte[] result;
		           
		//            contract.submitTransaction("addPerson", key, name,age, gender ,id, vaccinename, date1,dose );
					/*
					 * try { result = contract.evaluateTransaction("queryPerson", "5001");
					 * System.out.println(new String(result)); } catch (ContractException e) { //
					 * TODO Auto-generated catch block e.printStackTrace(); }
					 */
		 
		            contract.submitTransaction("changePersonDose", key, dose);
		            result = contract.evaluateTransaction("queryPerson", "1");
		            System.out.println(new String(result));
		            
		            RequestDispatcher rd=request.getRequestDispatcher("/thankyou.html"); 
		    		
			        rd.forward(request, response);
			
		        } catch (ContractException | TimeoutException | InterruptedException e1) {
					// TODO Auto-generated catch block
		        	
		   /*     	out.print("Data Not Exist....");
		        	out.print(" <a href='../FebDemoNew/index.html'>Click Here to Insert</a> ");
		     */  
		        	RequestDispatcher rd=request.getRequestDispatcher("/notexist.html");
		        	rd.forward(request, response);
		        	
					e1.printStackTrace();
				}
		        
		        
	
	
	
	}


	
	

}
