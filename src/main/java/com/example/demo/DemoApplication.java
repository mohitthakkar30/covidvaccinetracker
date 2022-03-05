package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class DemoApplication extends SpringBootServletInitializer {

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }


    public static void main(String[] args) {

        ApplicationContext context=SpringApplication.run(DemoApplication.class);
        context.getBean(EnrollAdmin.class).enrollAdmin();
        context.getBean(EnrollRegisterClientUser.class).enrollRegisterClientUser();
        }
}
