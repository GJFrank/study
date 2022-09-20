package com.atguigu.srb.gateway.filters;

import com.atguigu.srb.common.exception.Assert;
import com.atguigu.srb.common.result.ResponseEnum;
import com.atguigu.srb.common.utils.JwtUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

/**
 * projectName: gitclone
 *
 * @author: GOD伟
 * time: 2022/6/28 11:12 周二
 * description: 我的鉴权过滤器
 */
@Component
public class MyAuthFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获得断言信息
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        URI uri = request.getURI();
        RequestPath path = request.getPath();

        //鉴权
        Long userId = null;
        String token = "";
        List<String> tokens = request.getHeaders().get("token");
        if (null != tokens && tokens.size() > 0) {
            token = tokens.get(0);
            // 将token断言成功后，将token中的用户信息传递给后端微服务
            Assert.notNull(token, ResponseEnum.WEIXIN_FETCH_USERINFO_ERROR);
            userId = JwtUtils.getUserId(token);
            request.mutate().header("userId", userId + "");
        }
        System.out.println("解析得到的token" + tokens);
        System.out.println("全网过滤器,过滤所有请求!");
        return chain.filter(exchange);
    }
}
