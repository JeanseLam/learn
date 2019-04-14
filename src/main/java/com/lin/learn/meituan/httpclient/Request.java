package com.lin.learn.meituan.httpclient;

import org.apache.http.cookie.Cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {
    private HttpMethod method;
    private String url;
    private Map<String, String> params;
    private List<Cookie> cookies;
    private Map<String, String> headers;

    @java.beans.ConstructorProperties({"method", "url", "params", "cookies", "headers"})
    private Request(HttpMethod method, String url, Map<String, String> params, List<Cookie> cookies, Map<String, String> headers) {
        this.method = method;
        this.url = url;
        this.params = params;
        this.cookies = cookies;
        this.headers = headers;
    }

    public static RequestBuilder builder() {
        return new RequestBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Request request = (Request) o;

        if (!method.equals(request.method)) {
            return false;
        }
        if (!url.equals(request.url)) {
            return false;
        }
        return (params != null) ? params.equals(request.params) : request.params == null;

    }

    @Override
    public int hashCode() {
        int result = method.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + (params != null ? params.hashCode() : 0);
        return result;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public String getUrl() {
        return this.url;
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public List<Cookie> getCookies() {
        return this.cookies;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public static class RequestBuilder {
        private HttpMethod method;
        private String url;
        private Map<String, String> params;
        private List<Cookie> cookies;
        private Map<String, String> headers;

        RequestBuilder() {
        }

        public RequestBuilder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        public RequestBuilder url(String url) {
            this.url = url;
            return this;
        }

        public RequestBuilder params(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public RequestBuilder cookies(List<Cookie> cookies) {
            this.cookies = cookies;
            return this;
        }

        public RequestBuilder addCookie(Cookie cookie) {
            if (this.cookies == null) {
                cookies = new ArrayList<>();
            }
            cookies.add(cookie);
            return this;
        }

        public RequestBuilder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }
        public RequestBuilder addHeader(String key, String value) {
            if (this.headers == null) {
                headers = new HashMap<>();
            }
            headers.put(key, value);
            return this;
        }
        public RequestBuilder addParameter(String key, String value) {
            if (params == null) {
                params = new HashMap<>();
            }
            params.put(key, value);
            return this;
        }

        public Request build() {
            return new Request(method, url, params, cookies, headers);
        }

    }
}