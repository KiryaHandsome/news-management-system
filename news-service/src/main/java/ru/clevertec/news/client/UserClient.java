package ru.clevertec.news.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "UserClient", url = "${external.service.user-data.url}")
public interface UserClient {

    @GetMapping("/info")
    UserDetails getUserDetails(@RequestHeader(HttpHeaders.AUTHORIZATION) String token);
}
