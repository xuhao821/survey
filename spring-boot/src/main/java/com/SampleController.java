package com;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan
public class SampleController {

    public static void main(String[] args){
        SpringApplication.run(SampleController.class, args);
    }
}