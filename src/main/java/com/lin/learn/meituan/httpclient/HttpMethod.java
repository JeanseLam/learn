package com.lin.learn.meituan.httpclient;

public enum HttpMethod {
    GET,
    POST,
    ILLEGAL_METHOD;


    public static HttpMethod getMethod(String method) {
        for (HttpMethod httpMethod : HttpMethod.values()) {
            if (httpMethod.name().equalsIgnoreCase(method))
                return httpMethod;
        }
        return ILLEGAL_METHOD;
    }
}