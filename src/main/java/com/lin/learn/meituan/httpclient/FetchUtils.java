package com.lin.learn.meituan.httpclient;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.lin.learn.meituan.httpclient.HttpMethod.GET;
import static com.lin.learn.meituan.httpclient.HttpMethod.POST;

public final class FetchUtils {

    private static final String LOCATION = "Location";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36";
    public static final String[] PC_USER_AGENTS = {"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
            "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0)",
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:2.0.1) Gecko/20100101 Firefox/4.0.1",
            "Mozilla/5.0 (Windows NT 6.1; rv:2.0.1) Gecko/20100101 Firefox/4.0.1", "Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; en) Presto/2.8.131 Version/11.11",
            "Opera/9.80 (Windows NT 6.1; U; en) Presto/2.8.131 Version/11.11",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11",
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Maxthon 2.0)", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)",
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; The World)",
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SE 2.X MetaSr 1.0; SE 2.X MetaSr 1.0; .NET CLR 2.0.50727; SE 2.X MetaSr 1.0)",
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Avant Browser)",
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)"};

    private static final String[] MOBILE_USER_AGENS = {
            "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5",
            "Mozilla/5.0 (iPod; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5",
            "Mozilla/5.0 (iPad; U; CPU OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5",
            "Mozilla/5.0 (Linux; U; Android 2.3.7; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            "MQQBrowser/26 Mozilla/5.0 (Linux; U; Android 2.3.7; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            "Opera/9.80 (Android 2.3.4; Linux; Opera Mobi/build-1107180945; U; en-GB) Presto/2.8.149 Version/11.10",
            "Mozilla/5.0 (Linux; U; Android 3.0; en-us; Xoom Build/HRI39) AppleWebKit/534.13 (KHTML, like Gecko) Version/4.0 Safari/534.13",
            "Mozilla/5.0 (SymbianOS/9.4; Series60/5.0 NokiaN97-1/20.0.019; Profile/MIDP-2.1 Configuration/CLDC-1.1) AppleWebKit/525 (KHTML, like Gecko) BrowserNG/7.1.18124",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows Phone OS 7.5; Trident/5.0; IEMobile/9.0; HTC; Titan)", "UCWEB7.0.2.37/28/999", "Openwave/ UCWEB7.0.2.37/28/999",
            "Mozilla/4.0 (compatible; MSIE 6.0; ) Opera/UCWEB7.0.2.37/28/999",
            "Mozilla/5.0 (Linux; Android 4.4.2; GT-I9505 Build/JDQ39) AppleWebKit/537.36 (KHTML, like Gecko) Version/1.5 Chrome/28.0.1500.94 Mobile Safari/537.36",
            "Mozilla/5.0 (Linux; U; Android 4.0; en-us; GT-I9300 Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
            "Mozilla/5.0 (Linux; U; Android 4.3; en-us; SM-N900T Build/JSS15J) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
            "Mozilla/5.0 (Linux; U; Android 4.1; en-us; GT-N7100 Build/JRO03C) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
            "Mozilla/5.0 (Linux; U; Android 4.3; en-us; SM-N900T Build/JSS15J) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
            "Mozilla/5.0 (Linux; Android 4.4.4; en-us; Nexus 4 Build/JOP40D) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2307.2 Mobile Safari/537.36",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 8_0 like Mac OS X) AppleWebKit/600.1.3 (KHTML, like Gecko) Version/8.0 Mobile/12A4345d Safari/600.1.4",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0 like Mac OS X; en-us) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A465 Safari/9537.53",
            "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_2_1 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8C148 Safari/6533.18.5",
            "Mozilla/5.0 (iPad; CPU OS 4_3_5 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8L1 Safari/6533.18.5",
            "Mozilla/5.0 (iPad; CPU OS 7_0 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A465 Safari/9537.53"};
    public static final String USER_AGENT_KEY = "User-Agent";
    private static String setCookie = "Set-COOKIE";

    private static Logger logger = LoggerFactory.getLogger(FetchUtils.class);
    private static int size = Integer.MAX_VALUE;

    static {
        Protocol ignoreSecureHttps = new Protocol("https", new IgnoreSecureProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", ignoreSecureHttps);
    }

    private FetchUtils() {
    }

    /**
     * 处理url带的特殊字符
     *
     * @param url
     * @return
     */
    private static String replaceUrl(String url) {
        return url.replaceAll("&amp;", "&");
    }

    /**
     * 初始化httpMethod
     */
    private static void initHttpMethod(HttpRequestData data, org.apache.commons.httpclient.HttpMethod method) throws URIException {
        if (!data.getHeaders().containsKey(USER_AGENT_KEY) || data.getHeaders().get(USER_AGENT_KEY) == null) {
            data.setHeaders(USER_AGENT_KEY, USER_AGENT);
        }
        //method.addRequestHeader(USER_AGENT_KEY, data.getHeaders().get(USER_AGENT_KEY));
        // 添加请求头参数
        for (Entry<String, String> entry : data.getHeaders().entrySet()) {
            method.addRequestHeader(entry.getKey(), entry.getValue());
        }
        method.getParams().setVersion(HttpVersion.HTTP_1_1);
        method.getParams().setBooleanParameter(HttpMethodParams.SINGLE_COOKIE_HEADER, true);
        method.getParams().setSoTimeout(5 * 10000);// 5秒超时response
        method.getParams().setParameter("http.protocol.allow-circular-redirects", false);
    }

    /**
     * http get 字符数组
     */
    public static byte[] getBytes(HttpRequestData data, String url) throws IOException {
        GetMethod get = new GetMethod(replaceUrl(url));
        HttpClient httpClient = data.getHttpClient();
        try {
            initHttpMethod(data, get);
            execute(httpClient, get);
            trace(httpClient, url, null);

            return get.getResponseBody();
        } finally {
            get.releaseConnection();
            httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }
    }

    public static Response request(HttpRequestData data, Request req) throws IOException {
        if (req.getMethod() == GET) {
            return get(data, req);
        } else if (req.getMethod() == POST) {
            return post(data, req);
        }
        logger.warn("请求方法名错误：{}", req.getMethod());

        throw new IllegalArgumentException();
    }

    private static Response post(HttpRequestData data, Request req) throws IOException {
        PostMethod post = new PostMethod(replaceUrl(req.getUrl()));

        HttpClient httpClient = data.getHttpClient();

        if (req.getCookies() != null)
            httpClient.getState().addCookies(req.getCookies().toArray(new Cookie[req.getCookies().size()]));

        Response response;
        try {
            initHttpMethod(data, post);
            if (req.getHeaders() != null) {
                for (Entry<String, String> header : req.getHeaders().entrySet()) {
                    post.setRequestHeader(header.getKey(), header.getValue());
                }
            }

            if (req.getParams() != null) {

                for (Entry<String, String> param : req.getParams().entrySet()) {
                    post.addParameter(param.getKey(), param.getValue());
                }
            }

            execute(httpClient, post);

            String charsetStr = post.getResponseCharSet();
            Charset charset = null;

            if (StringUtils.isNotEmpty(charsetStr)) {
                charset = Charset.forName(charsetStr);
            }

            response = Response.builder().request(req).headers(post.getResponseHeaders()).encoding(charset).cookies(post.getResponseHeaders(setCookie))
                    .content(post.getResponseBody()).raw(post.getResponseBodyAsStream()).text(post.getResponseBodyAsString()).statusCode(post.getStatusCode()).build();

        } finally {
            post.releaseConnection();
            httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }

        return response;
    }

    public static Response postAndRedirect(HttpRequestData data, Request request) throws IOException {
        String redirectUrl;
        try (Response response = post(data, request)) {
            redirectUrl = getRedirectUrlFromResponseHeader(response.getHeaders());
        }

        if (StringUtils.isNotBlank(redirectUrl)) {
            Request redirectRequest = Request.builder().method(GET).url(redirectUrl).build();

            return get(data, redirectRequest);
        }
        logger.error("重定向跳转页面为空");
        return null;
    }

    public static String getRedirectUrlFromResponseHeader(Header[] headers) {
        if (headers != null) {
            for (Header h : headers) {
                if (LOCATION.equals(h.getName()))
                    return h.getValue();
            }
        }
        return "";
    }

    private static Response get(HttpRequestData data, Request req) throws IOException {
        GetMethod get = new GetMethod(replaceUrl(req.getUrl()));
        HttpClient httpClient = data.getHttpClient();

        if (req.getCookies() != null)
            httpClient.getState().addCookies(req.getCookies().toArray(new Cookie[req.getCookies().size()]));

        Response response;
        try {
            initHttpMethod(data, get);

            if (req.getHeaders() != null) {
                for (Entry<String, String> header : req.getHeaders().entrySet()) {
                    get.setRequestHeader(header.getKey(), header.getValue());
                }
            }

            if (req.getParams() != null && !req.getParams().isEmpty()) {
                int i = 0;
                NameValuePair[] paramPairs = new NameValuePair[req.getParams().size()];
                for (Entry<String, String> param : req.getParams().entrySet()) {
                    paramPairs[i++] = new NameValuePair(param.getKey(), param.getValue());
                }
                get.setQueryString(paramPairs);
            }
            execute(httpClient, get);
            String text = get.getResponseBodyAsString();
            String charsetStr = get.getResponseCharSet();

            Charset charset = null;
            if (StringUtils.isNotEmpty(charsetStr)) {
                charset = Charset.forName(charsetStr);
            }
            response = Response.builder().request(req).statusCode(get.getStatusCode()).content(get.getResponseBody()).text(text).raw(get.getResponseBodyAsStream())
                    .cookies(get.getResponseHeaders(setCookie)).encoding(charset).headers(get.getResponseHeaders()).build();

        } finally {
            get.releaseConnection();
            httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }

        return response;
    }

    /**
     * http get调用
     */
    public static String get(HttpRequestData data, String url) throws IOException {
        GetMethod get = new GetMethod(replaceUrl(url));
        HttpClient httpClient = data.getHttpClient();
        try {
            initHttpMethod(data, get);
            execute(httpClient, get);
            String html = get.getResponseBodyAsString();
            trace(httpClient, url, html);
            return html;
        } finally {
            get.releaseConnection();
            httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }
    }


    /**
     * http post调用
     */
    public static String post(HttpRequestData data, String url, Map<String, String> params) throws IOException {
        return post(data, url, params, false);
    }

    public static String post(HttpRequestData data, String url, Map<String, String> params, boolean setRespCookie) throws IOException {
        PostMethod post = new PostMethod(replaceUrl(url));
        HttpClient httpClient = data.getHttpClient();
        try {
            initHttpMethod(data, post);
            for (Entry<String, String> entry : params.entrySet()) {
                post.addParameter(entry.getKey(), entry.getValue() == null ? "" : entry.getValue());
            }
            execute(httpClient, post, setRespCookie);
            String html = post.getResponseBodyAsString();

            trace(httpClient, url, html);

            return html;
        } finally {
            post.releaseConnection();
            httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }
    }

    /**
     * 如果是302返回，Location. 如果其他直接返回
     * @param data
     * @param url
     * @param params
     * @param setRespCookie
     * @return
     * @throws IOException
     */
    public static String postReturnLocation(HttpRequestData data, String url, Map<String, String> params, boolean setRespCookie) throws IOException {
        PostMethod post = new PostMethod(replaceUrl(url));
        HttpClient httpClient = data.getHttpClient();
        try {
            initHttpMethod(data, post);
            for (Entry<String, String> entry : params.entrySet()) {
                post.addParameter(entry.getKey(), entry.getValue() == null ? "" : entry.getValue());
            }
            execute(httpClient, post, setRespCookie);
            String html = post.getResponseBodyAsString();
            if(StringUtils.isEmpty(html) && post.getStatusCode()==302 && post.getResponseHeader(LOCATION)!=null){
                return post.getResponseHeader(LOCATION).getValue();
            }

            trace(httpClient, url, html);

            return html;
        } finally {
            post.releaseConnection();
            httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }
    }

    public static String postAndRedirect(HttpRequestData data, String url, Map<String, String> params, boolean setResponseCookie) throws IOException {
        PostMethod post = new PostMethod(replaceUrl(url));
        HttpClient httpClient = data.getHttpClient();
        String redirectUrl;
        try {
            initHttpMethod(data, post);
            for (Entry<String, String> entry : params.entrySet()) {
                post.addParameter(entry.getKey(), entry.getValue());
            }
            execute(httpClient, post, setResponseCookie);
            Header setCookieHeader = post.getResponseHeader(setCookie);
            if (setCookieHeader != null) {
                data.getHttpClient().getState().addCookies(getRespCookies(setCookieHeader.getValue()));
            }
            String html = post.getResponseBodyAsString();
            if (post.getResponseHeader(LOCATION) != null) {
                redirectUrl = post.getResponseHeader(LOCATION).getValue();
                html = get(data, redirectUrl);
            }

            trace(httpClient, url, html);
            return html;
        } finally {
            post.releaseConnection();
            httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }
    }


    public static String postAndRedirect(HttpRequestData data, String url, Map<String, String> params) throws IOException {
        return postAndRedirect(data, url, params, false);
    }

    public static Map<String,String> postResponse(HttpRequestData data, String url, Map<String, String> params)throws IOException  {
        PostMethod post = new PostMethod(replaceUrl(url));
        HttpClient httpClient = data.getHttpClient();
        try {
            initHttpMethod(data, post);
            for (Entry<String, String> entry : params.entrySet()) {
                post.addParameter(entry.getKey(), entry.getValue());
            }
            execute(httpClient, post, true);
            Header setCookieHeader = post.getResponseHeader(setCookie);
            if (setCookieHeader != null) {
                data.getHttpClient().getState().addCookies(getRespCookies(setCookieHeader.getValue()));
            }
            Map<String,String>map=new HashMap<>();
            map.put(LOCATION,post.getResponseHeader(LOCATION)!=null?post.getResponseHeader(LOCATION).getValue():null);
            map.put("body",post.getResponseBodyAsString());
            return map;
        } finally {
            post.releaseConnection();
            httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }
    }

    /**
     * http post调用
     */
    public static String post(HttpRequestData data, String url, Map<String, String> params, String encode) throws IOException {
        PostMethod post = new PostMethod(replaceUrl(url));
        HttpClient httpClient = data.getHttpClient();
        try {
            initHttpMethod(data, post);
            post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encode);
            for (Entry<String, String> entry : params.entrySet()) {
                post.addParameter(entry.getKey(), entry.getValue());
            }
            execute(httpClient, post);
            String html = post.getResponseBodyAsString();

            Header setCookieHeader = post.getResponseHeader(setCookie);
            if (setCookieHeader != null) {
                data.getHttpClient().getState().addCookies(getRespCookies(setCookieHeader.getValue()));
            }

            trace(httpClient, url, html);

            return html;
        } finally {
            post.releaseConnection();
            httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }
    }

    /**
     * http post流
     * <p>
     * 即不是post key-value模型的数据，而是post一段文字 参数JsonObject param会转化为一段json文本post过去
     */
    public static String postJson(HttpRequestData data, String url, JSONObject param) throws IOException {
        return postText(data, url, param.toString(), "application/json", "utf-8");
    }

    /**
     * http post流
     * <p>
     * 即不是post key-value模型的数据，而是post一段文字
     */
    public static String postText(HttpRequestData data, String url, String text, String contentType, String encoding) throws IOException {
        PostMethod post = new PostMethod(replaceUrl(url));
        HttpClient httpClient = data.getHttpClient();
        try {
            data.setHeaders("Content-Type", contentType);
            initHttpMethod(data, post);
            RequestEntity entity = new StringRequestEntity(text, contentType, encoding);

            post.setRequestEntity(entity);
            execute(httpClient, post);
            String html = post.getResponseBodyAsString();

            trace(httpClient, url, html);

            return html;
        } finally {
            post.releaseConnection();
            httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }
    }


    public static String postStream(HttpRequestData data, String url, byte[] text) throws IOException {
        PostMethod post = new PostMethod(replaceUrl(url));
        HttpClient httpClient = data.getHttpClient();

        try {
            initHttpMethod(data, post);
            RequestEntity entity = new ByteArrayRequestEntity(text,"application/json");

            post.setRequestEntity(entity);
            execute(httpClient, post);
            String html = post.getResponseBodyAsString();

            trace(httpClient, url, html);

            return html;
        } finally {
            post.releaseConnection();
            httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }
    }



    public static InputStream getPostBytes(HttpRequestData data, String url, Map<String, String> params) throws IOException {
        PostMethod post = new PostMethod(replaceUrl(url));
        HttpClient httpClient = data.getHttpClient();
        try {
            initHttpMethod(data, post);
            for (Entry<String, String> entry : params.entrySet()) {
                post.addParameter(entry.getKey(), entry.getValue());
            }
            execute(httpClient, post);
            return post.getResponseBodyAsStream();
        } finally {
            post.releaseConnection();
            httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }
    }

    public static byte[] getPostByte(HttpRequestData data, String url, Map<String, String> params) throws IOException {
        PostMethod post = new PostMethod(replaceUrl(url));
        HttpClient httpClient = data.getHttpClient();
        try {
            initHttpMethod(data, post);
            for (Entry<String, String> entry : params.entrySet()) {
                post.addParameter(entry.getKey(), entry.getValue());
            }
            execute(httpClient, post);
            return post.getResponseBody();
        } finally {
            post.releaseConnection();
            httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }
    }

    /**
     * 日志
     */
    private static void trace(HttpClient httpClient, String url, String html) {
        if (logger.isDebugEnabled()) {
            logger.info("==========printing http request information===" + url + "===========================================");
            for (Cookie e : httpClient.getState().getCookies()) {
                logger.info(e.getName() + "===" + e.getValue());
            }
            if (!StringUtils.isEmpty(html)) {
                logger.info(html);
            } else {
                logger.info("html is empty");
            }
            logger.info("==========printing http request information===" + url + "===========================================");
        }
    }

    private static void execute(HttpClient client, HttpMethodBase method) throws IOException {
        execute(client, method, false);
    }

    private static void execute(HttpClient client, HttpMethodBase method, boolean setRespCookie) throws IOException {
        long start = System.currentTimeMillis();
        method.getParams().setSoTimeout(client.getParams().getSoTimeout());
        client.executeMethod(method);
        if (setRespCookie) {
            setRespCookies(method, client); //这个方法设置 cookie 有 bug
        }
        long cost = System.currentTimeMillis() - start;
        if (cost > 2000 && logger.isWarnEnabled()) {
            logger.warn("http request : " + method.getURI().toString() + " cost " + (System.currentTimeMillis() - start) + " milliseconds with result : " + method.getStatusCode()
                    + " " + method.getStatusText());
        }

    }

    private static void setRespCookies(HttpMethodBase post, HttpClient httpClient) {
        Header setCookieHeader = post.getResponseHeader(setCookie);
        if (setCookieHeader != null) {
            httpClient.getState().addCookies(getRespCookies(setCookieHeader.getValue()));
        }

    }

    private static Cookie[] getRespCookies(String respCookie) {
        Cookie[] cookies = null;
        List<Cookie> list = getRespCookieList(respCookie);
        if (!list.isEmpty()) {
            cookies = new Cookie[list.size()];
            list.toArray(cookies);
        }
        return cookies;
    }

    private static List<Cookie> getRespCookieList(String respCookie) {
        List<Cookie> setCookies = new ArrayList<>();
        if (StringUtils.isNotBlank(respCookie)) {
            String[] perCookies = respCookie.split(", ");
            for (String perCookie : perCookies) {
                parseCookie(perCookie, setCookies);
            }
        }
        return setCookies;
    }

    private static void parseCookie(String perCookie, List<Cookie> setCookies) {
        String[] infos = perCookie.split("; ");
        String name = "";
        String value = "";
        String path = "";
        String domain = "";
        for (String info : infos) {
            String[] ss = info.split("=");
            String s1 = ss[0];
            String s2 = "";
            if (ss.length > 1) {
                s2 = ss[1];
            }
            String lower = s1.toLowerCase();
            if ("path".equals(lower)) {
                path = s2;
            } else if ("domain".equals(lower)) {
                domain = s2;
            } else {
                name = s1;
                value = s2;
            }
        }
        setCookies.add(new Cookie(domain, name, value, path, null, false));
    }

//    public static void main(String[] args) {
//        HttpRequestData data = new HttpRequestData();
//
//        // test get method
//        Map<String, String> params = new HashMap<>();
//        params.put("q", "Java+Autocloseable");
//        params.put("FORM", "QSRE1");
//
//        Request request = Request.builder().url("http://cn.bing.com/search").method(GET).params(params).build();
//
//        try (Response response = FetchUtils.request(data, request)) {
//            logger.info(response.getText());
//        } catch (IOException e) {
//            logger.error("error in main:{}", e);
//        }
//
//        // test post method
//
//        Map<String, String> postParams = new HashMap<>();
//        postParams.put("m", "checkphone");
//        postParams.put("phone", "17769314905");
//
//        Request postRequest = Request.builder().url("http://login.189.cn/login/ajax").method(POST).params(postParams).build();
//
//        try (Response response = FetchUtils.request(data, postRequest)) {
//            logger.info(response.getText());
//        } catch (IOException e) {
//            logger.error("error in main:{}", e);
//        }
//    }

    public static String postXml(HttpRequestData data, String url, String xml, String contentType, String charSet) throws IOException {
        PostMethod post = new PostMethod(url);
        HttpClient httpClient = data.getHttpClient();
        try {
            FetchUtils.initHttpMethod(data, post);
            StringRequestEntity requestEntity = new StringRequestEntity(xml, contentType, charSet);
            post.setRequestEntity(requestEntity);
            FetchUtils.execute(httpClient, post);
            return post.getResponseBodyAsString();
        } finally {
            post.releaseConnection();
            httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }
    }

}
