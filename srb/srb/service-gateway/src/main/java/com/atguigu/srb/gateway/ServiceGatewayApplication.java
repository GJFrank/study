package com.atguigu.srb.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * projectName: gitclone
 *
 * @author: GOD伟
 * time: 2022/6/28 11:03 周二
 * description:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServiceGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceGatewayApplication.class, args);
    }
}
