package com.codeL.data.ds.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @author
 * @date
 */
@SpringBootApplication
@ImportResource(value = {"classpath:datasource.xml"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
