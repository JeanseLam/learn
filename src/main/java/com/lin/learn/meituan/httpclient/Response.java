package com.lin.learn.meituan.httpclient;

import org.apache.commons.httpclient.Header;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class Response implements Closeable {

    private int statusCode;
    private String text;
    private byte[] content;
    private InputStream raw;
    private Charset encoding;
    private Header[] headers;
    private Header[] cookies;
    private Request request;


    public Response() {
        //nothing to do
    }

    public Response(int statusCode, String text, byte[] content, Charset encoding, Header[] headers, Header[] cookies, Request request, InputStream raw) {
        this.statusCode = statusCode;
        this.text = text;
        this.content = content;
        this.encoding = encoding;
        this.headers = headers;
        this.cookies = cookies;
        this.request = request;
        this.raw = raw;
    }

    public static ResponseBuilder builder() {
        return new ResponseBuilder();
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getText() {
        return this.text;
    }

    public byte[] getContent() {
        return this.content;
    }

    public Charset getEncoding() {
        return this.encoding;
    }

    public Header[] getHeaders() {
        return this.headers;
    }

    public Header[] getCookies() {
        return this.cookies;
    }


    public Request getRequest() {
        return this.request;
    }

    public InputStream getRaw() {
        return raw;
    }

    public void setRaw(InputStream raw) {
        this.raw = raw;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public void setEncoding(Charset encoding) {
        this.encoding = encoding;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    public void setCookies(Header[] cookies) {
        this.cookies = cookies;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public String getRedirectUrl() {
        if (headers != null) {
            for (Header header: headers) {
                if ("Location".equals(header.getName())) {
                    return header.getValue();
                }
            }
        }
        return "";
    }

    @Override
    public String toString() {
        return "com.kingdee.grab.entity.Response(statusCode=" + this.getStatusCode() + ", text=" + this.getText() +  ", encoding=" + this.getEncoding() + ", headers=" + java.util.Arrays.deepToString(this.getHeaders()) + ", cookies=" + java.util.Arrays.deepToString(this.getCookies()) + ")";
    }

    @Override
    public void close() throws IOException {
        if (raw != null)
            raw.close();
    }


    public static class ResponseBuilder {
        private int statusCode;
        private String text;
        private byte[] content;
        private Charset encoding;
        private Header[] headers;
        private Header[] cookies;
        private Request request;
        private InputStream raw;
        ResponseBuilder() {
        }

        public ResponseBuilder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public ResponseBuilder text(String text) {
            this.text = text;
            return this;
        }

        public ResponseBuilder content(byte[] content) {
            this.content = content;
            return this;
        }

        public ResponseBuilder encoding(Charset encoding) {
            this.encoding = encoding;
            return this;
        }

        public ResponseBuilder headers(Header[] headers) {
            this.headers = headers;
            return this;
        }

        public ResponseBuilder cookies(Header[] cookies) {
            this.cookies = cookies;
            return this;
        }

        public ResponseBuilder request(Request request) {
            this.request = request;
            return this;
        }

        public ResponseBuilder raw(InputStream raw) {
            this.raw = raw;
            return this;
        }

        public Response build() {
            return new Response(statusCode, text, content, encoding, headers, cookies, request, raw);
        }

        @Override
        public String toString() {
            return "com.kingdee.grab.entity.Response.ResponseBuilder(statusCode=" + this.statusCode + ", text=" + this.text + ", content=" + this.content + ", encoding=" + this.encoding + ", headers=" + java.util.Arrays.deepToString(this.headers) + ", cookies=" + java.util.Arrays.deepToString(this.cookies) + ", request=" + this.request + ")";
        }
    }
}
