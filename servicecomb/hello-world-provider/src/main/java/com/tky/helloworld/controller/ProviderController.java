package com.tky.helloworld.controller;

import com.tky.helloworld.api.HelloWorldInterfaces;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestSchema(schemaId = "providerController")
@RequestMapping("/")
public class ProviderController implements HelloWorldInterfaces{


    @Override
    @GetMapping("sayHello")
    public String sayHello(String name) {

        System.out.println("欢迎外宾："+name+"的到来！！！");
        return "已经接待《"+name+"》成功！！！";
    }
}
