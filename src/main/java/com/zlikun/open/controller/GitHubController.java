package com.zlikun.open.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * @author zlikun
 * @date 2018-09-20 18:22
 */
@Slf4j
@RestController
@RequestMapping("/github")
public class GitHubController {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${client_id}")
    private String clientId;
    @Value("${client_secret}")
    private String clientSecret;
    @Value("${redirect_uri}")
    private String redirectUri;
    @Value("${access_token_uri}")
    private String accessTokenUri;
    @Value("${user_uri}")
    private String userUri;

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


        TokenResponse response;
        try {
            // 指定 Accept 消息头，GitHub会根据该H消息头返回所需数据结构
            requestExecutor.setRequestInitializer(request -> request.getHeaders().setAccept("application/json"));

            // 执行请求
            response = requestExecutor.execute();

            // {"access_token":"0bdfc544cb2fe879c4e6d2e2f26a8b5addd55ceb","scope":"","token_type":"bearer"}
            System.out.println(response);

            // 查询用户
            GenericJson json = queryUser(response.getAccessToken());
            // 仅用于测试（把access_token放用户信息中一并在浏览器显示出来）
            json.set("access_token", response.getAccessToken());

            // 打印用户信息
            System.out.println(json.toPrettyString());

            // 转换为Map（返回客户端时又重新序列化为JSON？！）
            return objectMapper.readValue(json.toString(), Map.class);
        } catch (IOException e) {
            log.error("调用查询access_token信息API出错!", e);
        }

        return "None";
    }

    /**
     * 查询用户
     *
     * @param accessToken
     * @return
     * @throws IOException
     */
    private GenericJson queryUser(String accessToken) throws IOException {
        HttpRequest request = new NetHttpTransport()
                .createRequestFactory()
                .buildGetRequest(new GenericUrl(String.format("%s?access_token=%s", userUri, accessToken)))
                .setParser(new JsonObjectParser(JacksonFactory.getDefaultInstance()));
        HttpResponse response = request.execute();
        if (response.isSuccessStatusCode()) {
            return response.parseAs(GenericJson.class);
        }
        return null;
    }

}
