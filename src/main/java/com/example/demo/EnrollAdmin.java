package com.example.demo;

import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.Properties;

@Component
public class EnrollAdmin {

   @Value("${pem.path}")
    private String pemPath;

    @Value("${wallet.path}")
    private String walletPath;

    @Value("${HFCAClient.server}")
    private String hostName;

    public void enrollAdmin() {
        try {
            System.out.println("PemPath ===> "+pemPath);
            Properties props = new Properties();
            props.put("pemFile", pemPath);
            props.put("allowAllHostNames", "true");
            HFCAClient caClient = HFCAClient.createNewInstance(hostName, props);
            CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
            caClient.setCryptoSuite(cryptoSuite);

            // Create a wallet for managing identities+
            Wallet wallet = Wallets.newFileSystemWallet(Paths.get(walletPath));

            // Check to see if we've already enrolled the admin user.
            if (wallet.get("admin") != null) {
                System.out.println("An identity for the admin user \"admin\" already exists in the wallet");
                return;
            }

            // Enroll the admin user, and import the new identity into the wallet.
            final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
            enrollmentRequestTLS.addHost("localhost");
            enrollmentRequestTLS.setProfile("tls");
            Enrollment enrollment = caClient.enroll("admin", "adminpw", enrollmentRequestTLS);
            Identity user = Identities.newX509Identity("Org1MSP", enrollment);
            wallet.put("admin", user);
            System.out.println("Successfully enrolled user \"admin\" and imported it into the wallet");

        } catch (Exception e) {
            System.out.println("Something went wrong !!" + e.getMessage());
        }
    }
}



