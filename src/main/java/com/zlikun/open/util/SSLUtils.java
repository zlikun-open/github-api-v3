package com.zlikun.open.util;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport.Builder;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

/**
 * API统一封装
 *
 * @param <T>
 * @author zlikun
 * @date 2016年5月20日 下午2:01:47
 */
public abstract class SSLUtils<T> {

    protected String serverUri;
    protected String accessToken;

    private final Map<String, Object> parameters = new HashMap<>();

    public SSLUtils(String serverUri, String accessToken) {
        this.serverUri = serverUri;
        this.accessToken = accessToken;
    }

    public abstract T handleResponse(HttpResponse response);

    /**
     * 执行请求
     *
     * @return
     */
    public T execute() {
        return this.execute(null);
    }

    /**
     * 执行请求，使用附加请求参数
     *
     * @param params
     * @return
     */
    public T execute(Map<String, Object> params) {
        try {
            parameters.put("access_token", accessToken);
            if (params != null && !params.isEmpty()) {
                parameters.putAll(params);
            }
            HttpRequest req = createNetHttpTransport().createRequestFactory()
                    .buildPostRequest(new GenericUrl(serverUri), new UrlEncodedContent(parameters))
                    .setParser(new JsonObjectParser(JacksonFactory.getDefaultInstance()));
            HttpResponse resp = req.execute();
            if (resp.isSuccessStatusCode()) {
                return handleResponse(resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加请求参数
     *
     * @param key
     * @param value
     * @return
     */
    public SSLUtils<T> addParameter(String key, Object value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("添加参数时，取值不能为空!");
        }
        this.parameters.put(key, value);
        return this;
    }

    public static final NetHttpTransport createNetHttpTransport() {
        return new Builder()
                .setSslSocketFactory(createSSLSocketFactory())
                .setHostnameVerifier((hostname, session) -> {
                    System.out.println(String.format("hostname = %s ,creationTime = %d", hostname, session.getCreationTime()));
                    return true;
                }).build();
    }

    public static final SSLSocketFactory createSSLSocketFactory() {
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }
        };
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{tm}, null);
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

}