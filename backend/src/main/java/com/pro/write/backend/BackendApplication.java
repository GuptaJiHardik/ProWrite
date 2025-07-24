package com.pro.write.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
//@PropertySource(value = "classpath:.env", ignoreResourceNotFound = true)
public class BackendApplication {

    public static void main(String[] args) {

//        Dotenv dotenv = Dotenv.load();
//
//        System.setProperty("GEMINI_API_URL", dotenv.get("GEMINI_API_URL"));
//        System.setProperty("GEMINI_API_KEY", dotenv.get("GEMINI_API_KEY"));
//
//        System.setProperty("DB_URL", dotenv.get("DB_URL"));
//        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
//        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

        SpringApplication.run(BackendApplication.class, args);
    }
}
