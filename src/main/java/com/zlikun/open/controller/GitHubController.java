package com.zlikun.open.controller;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author zlikun
 * @date 2018-09-20 18:22
 */
@Slf4j
@RestController
@RequestMapping("/github")
public class GitHubController {

    @Value("${client_id}")
    private String clientId;
    @Value("${client_secret}")
    private String clientSecret;
    @Value("${redirect_uri}")
    private String redirectUri;
    @Value("${access_token_uri}")
    private String accessTokenUri;

    private String grantType = "authorization_code";

    /**
     * 授权回调URL
     *
     * @param code  用于调用API查询access_token信息
     * @param state 应作校验，这里忽略
     * @return
     */
    @GetMapping("/v3/callback")
    public Object callbackV3(String code, String state) {
        log.info("code = {} ,state = {}", code, state);

        // https://developer.github.com/apps/building-oauth-apps/authorizing-oauth-apps/#2-users-are-redirected-back-to-your-site-by-github
        AuthorizationCodeTokenRequest requestExecutor = new AuthorizationCodeTokenRequest(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                new GenericUrl(accessTokenUri),
                code)
                .setRedirectUri(redirectUri)
                .setClientAuthentication(new ClientParametersAuthentication(clientId, clientSecret));


        TokenResponse response = null;
        try {
            // 指定 Accept 消息头，GitHub会根据该H消息头返回所需数据结构
            requestExecutor.setRequestInitializer(request -> request.getHeaders().setAccept("application/json"));

            // 执行请求
            response = requestExecutor.execute();

            // {"access_token":"0bdfc544cb2fe879c4e6d2e2f26a8b5addd55ceb","scope":"","token_type":"bearer"}
            System.out.println(response);
        } catch (IOException e) {
            log.error("调用查询access_token信息API出错!", e);
        }

        // 这里直接返回Token信息，仅供调试使用
        return response;
    }

}
