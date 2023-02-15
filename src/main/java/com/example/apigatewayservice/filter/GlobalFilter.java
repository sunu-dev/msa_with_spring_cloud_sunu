package com.example.apigatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    public GlobalFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request =exchange.getRequest();
            ServerHttpResponse response=exchange.getResponse();

            log.info("GlobalFilter baseMessage, {}", config.getBaseMessage());
            if(config.isPreLogger()){
                log.info("GlobalFilter start:request id, {}", request.getId());
            }

            //Custom POst filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> { //mono spring 5 추가
                if(config.isPostLogger()){
                    log.info("GlobalFilter End:request id, {}", response.getStatusCode());
                }
            }));

        };
    }

    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;

    }


}
