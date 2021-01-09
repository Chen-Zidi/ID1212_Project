package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.net.ssl.HttpsURLConnection;

@SpringBootApplication
public class ProjectClientSideAppApplication {

    public static void main(String[] args) {
        HttpsURLConnection.setDefaultHostnameVerifier ((hostname, session) -> true);
        SpringApplication.run(ProjectClientSideAppApplication.class, args);
    }


}
