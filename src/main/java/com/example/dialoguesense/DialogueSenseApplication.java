package com.example.dialoguesense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class DialogueSenseApplication {

    public static void main(String[] args) {
        SpringApplication.run(DialogueSenseApplication.class, args);
    }

}
