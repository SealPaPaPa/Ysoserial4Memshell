package com.example.demo;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;

@SpringBootApplication
@RestController
public class Demo1Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo1Application.class, args);
    }

    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "myName", defaultValue = "World") String name) {
        //SpringMemshellTemplate mem = new SpringMemshellTemplate();
        return String.format("Hello %s!", name);
    }

    @PostMapping("/hello")
    public String getHelloPost(@RequestParam(value = "data", defaultValue = "") String request) {
        try {
            String temp = request.replace(" ", "+");
            System.out.println(temp);
            byte[] input = Base64.decodeBase64(temp);
            ByteArrayInputStream is = new ByteArrayInputStream(input);
            ObjectInputStream inputStream = new ObjectInputStream(is);
            inputStream.readObject();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return String.format("Hello !");
    }
}
