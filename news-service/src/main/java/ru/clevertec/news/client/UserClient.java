package ru.clevertec.news.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.clevertec.news.dto.UserDetailsDto;

@FeignClient(name = "UserClient", url = "${external.service.user-data.url}")
public interface UserClient {

    @GetMapping("/info")
    UserDetailsDto getUserDetails(@RequestHeader(HttpHeaders.AUTHORIZATION) String token);
}
