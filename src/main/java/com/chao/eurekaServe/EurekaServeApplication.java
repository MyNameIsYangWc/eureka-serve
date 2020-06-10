package com.chao.eurekaServe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServeApplication.class,args);
        System.out.println("注册中心启动成功");
    }
}
