package com.tky.helloworld.controller;

import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@RestSchema(schemaId = "consumerController")
@RequestMapping("/")
public class ConsumerController {

    private final RestTemplate restTemplate = RestTemplateBuilder.create();


    @GetMapping("request")
    public  String sayHello(String name){
        String result = restTemplate.getForObject("cse://demo-provider/sayHello?name=" + name, String.class);

        System.out.println(name);
        return result;
    }
}
