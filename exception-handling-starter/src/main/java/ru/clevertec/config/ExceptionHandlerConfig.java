package ru.clevertec.config;


import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.exception.RestExceptionHandler;

@Configuration
@ConditionalOnMissingBean(RestExceptionHandler.class)
@ConditionalOnProperty(
        prefix = "exception.handler",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class ExceptionHandlerConfig {

    @Bean
    public RestExceptionHandler exceptionHandler() {
        return new RestExceptionHandler();
    }
}
