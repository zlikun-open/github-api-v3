package com.zlikun.open.controller;

import com.google.api.client.auth.oauth2.AuthorizationRequestUrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.Random;

/**
 * @author zlikun
 * @date 2018-09-20 17:49
 */
@Slf4j
@Controller
public class DefaultController {

    @Value("${authorize_uri}")
    private String authorizeUri;
    @Value("${client_id}")
    private String clientId;
    @Value("${redirect_uri}")
    private String redirectUri;

    /**
     * http://tools.ietf.org/html/rfc6749#section-4.1.1
     */
    private String responseType = "code";

    @GetMapping("/")
    public ResponseEntity<String> index() {
        // 生成6位随机数
        String state = String.format("%04x", new Random().nextInt() & 0xffffff);

        // 生成授权链接
        AuthorizationRequestUrl aru = new AuthorizationRequestUrl(
                authorizeUri,
                clientId,
                Arrays.asList(responseType))
                .setRedirectUri(redirectUri)
                .setState(state);
        String targetUrl = aru.toURL().toString();
        // https://github.com/login/oauth/authorize?client_id=fe53ce2286f7104ef02d&redirect_uri=https://open.zlikun.com/github/v3/callback&response_type=code&state=d6395c
        log.info(targetUrl);

        return ResponseEntity.ok(String.format("<a target=\"blank\" href=\"%s\">使用GitHub登录</a>", targetUrl));
    }

}
