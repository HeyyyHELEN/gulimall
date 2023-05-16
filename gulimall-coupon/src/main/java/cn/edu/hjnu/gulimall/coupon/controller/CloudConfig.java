package cn.edu.hjnu.gulimall.coupon.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
public class CloudConfig {

    @Value("${coupon.user.name}")
    private String name;

    @Value("${coupon.user.age}")
    private int age;

    public String getName(){
        return name;
    }

    public Integer getAge(){
        return age;
    }

}
