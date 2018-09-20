package com.zlikun.open;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author zlikun
 * @date 2018-09-20 19:11
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class AccessTokenApiTest {

    @Value("${client_id}")
    private String clientId;
    @Value("${client_secret}")
    private String clientSecret;
    @Value("${redirect_uri}")
    private String redirectUri;
    @Value("${access_token_uri}")
    private String accessTokenUri;

    String code = "4a32dff6bf69b05a9c7e";

    @Test
    @Ignore
    public void test() {

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
//            // GitHub AccessToken API
//            HttpResponse httpResponse = requestExecutor.executeUnparsed();
//            // access_token=58845a7bccc34e181b09da708285949067178b38&scope=&token_type=bearer
//            log.info(httpResponse.parseAsString());

            // 指定 Accept 消息头，GitHub会根据该H消息头返回所需数据结构
            requestExecutor.setRequestInitializer(request -> request.getHeaders().setAccept("application/json"));

            response = requestExecutor.execute();

            // {"access_token":"0bdfc544cb2fe879c4e6d2e2f26a8b5addd55ceb","scope":"","token_type":"bearer"}
            System.out.println(response);

            // access_token = 0bdfc544cb2fe879c4e6d2e2f26a8b5addd55ceb
            log.info("access_token = {}", response.getAccessToken());
            // token_type = bearer
            log.info("token_type = {}", response.getTokenType());
            // scope =
            log.info("scope = {}", response.getScope());
        } catch (IOException e) {
            log.error("调用查询access_token信息API出错!", e);
        }

    }

}
