package com.github.polaris.gateway.filter;

import cn.hutool.core.util.IdUtil;
import com.github.polaris.constants.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class TraceFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest mutatedRequest = request.mutate()
                .header(CommonConstants.HEADER_X_TRACE_ID, IdUtil.getSnowflakeNextIdStr())
                .build();
        return chain.filter(exchange.mutate()
                .request(mutatedRequest)
                .build());
    }
}
