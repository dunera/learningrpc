package com.dunera.rpc.demo;

public class HelloWorldImpl implements HelloWorld {
    @Override
    public String sayHelloTo(String name) {
        System.out.println(name + " 哈哈哈");
        return "i am fine and ni hao: " + name;
    }
}
