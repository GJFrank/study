package com.atguigu.srb.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * projectName: srb
 *
 * @author: GOD伟
 * time: 2022/6/11 11:56 周六
 * description:
 */
@SpringBootApplication
@ComponentScan({"com.atguigu.srb"})
@EnableDiscoveryClient
@EnableFeignClients
public class ServiceCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCoreApplication.class, args);

    }
}
