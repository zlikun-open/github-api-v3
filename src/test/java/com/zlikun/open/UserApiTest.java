package com.zlikun.open;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zlikun
 * @date 2018-09-20 19:48
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserApiTest {

    @Value("${user_uri}")
    private String userUri;

    private String accessToken = "4f99e64e7046965e6eab7271f69aa88075052b7e";

    @Test @Ignore
    public void test() throws IOException {
        HttpRequest request = new NetHttpTransport()
                .createRequestFactory()
                .buildGetRequest(new GenericUrl(String.format("%s?access_token=%s", userUri, accessToken)))
                .setParser(new JsonObjectParser(JacksonFactory.getDefaultInstance()));
        HttpResponse response = request.execute();
        if (response.isSuccessStatusCode()) {
            // 解析为字符串
            // {"login":"zlikun","id":10136686,"node_id":"MDQ6VXNlcjEwMTM2Njg2","avatar_url":"https://avatars0.githubusercontent.com/u/10136686?v=4","gravatar_id":"","url":"https://api.github.com/users/zlikun","html_url":"https://github.com/zlikun","followers_url":"https://api.github.com/users/zlikun/followers","following_url":"https://api.github.com/users/zlikun/following{/other_user}","gists_url":"https://api.github.com/users/zlikun/gists{/gist_id}","starred_url":"https://api.github.com/users/zlikun/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/zlikun/subscriptions","organizations_url":"https://api.github.com/users/zlikun/orgs","repos_url":"https://api.github.com/users/zlikun/repos","events_url":"https://api.github.com/users/zlikun/events{/privacy}","received_events_url":"https://api.github.com/users/zlikun/received_events","type":"User","site_admin":false,"name":"zlikun","company":null,"blog":"","location":null,"email":"zlikun-dev@hotmail.com","hireable":null,"bio":null,"public_repos":7,"public_gists":0,"followers":7,"following":0,"created_at":"2014-12-10T01:30:58Z","updated_at":"2018-09-09T09:05:41Z"}
            // log.info(response.parseAsString());
            // 解析为对象
            // {"login":"zlikun","id":10136686,"node_id":"MDQ6VXNlcjEwMTM2Njg2","avatar_url":"https://avatars0.githubusercontent.com/u/10136686?v=4","gravatar_id":"","url":"https://api.github.com/users/zlikun","html_url":"https://github.com/zlikun","followers_url":"https://api.github.com/users/zlikun/followers","following_url":"https://api.github.com/users/zlikun/following{/other_user}","gists_url":"https://api.github.com/users/zlikun/gists{/gist_id}","starred_url":"https://api.github.com/users/zlikun/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/zlikun/subscriptions","organizations_url":"https://api.github.com/users/zlikun/orgs","repos_url":"https://api.github.com/users/zlikun/repos","events_url":"https://api.github.com/users/zlikun/events{/privacy}","received_events_url":"https://api.github.com/users/zlikun/received_events","type":"User","site_admin":false,"name":"zlikun","company":null,"blog":"","location":null,"email":"zlikun-dev@hotmail.com","hireable":null,"bio":null,"public_repos":7,"public_gists":0,"followers":7,"following":0,"created_at":"2014-12-10T01:30:58Z","updated_at":"2018-09-09T09:05:41Z"}
            GenericJson json = response.parseAs(GenericJson.class);
            System.out.println(json);
        }

    }

}
