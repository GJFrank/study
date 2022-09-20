package com.atguigu.srb.sms.client;

import com.atguigu.srb.sms.client.impl.CoreUserInfoClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * projectName: srb
 *
 * @author: GOD伟
 * time: 2022/6/27 20:54 周一
 * description:
 */
@FeignClient(value = "service-core", fallback = CoreUserInfoClientFallback.class)
public interface CoreUserInfoClient {
    @GetMapping("/api/core/userInfo/checkMobile/{mobile}")
    boolean checkMobile(@PathVariable("mobile") String mobile);
}
