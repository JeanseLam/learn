package com.lin.learn.meituan.httpclient;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 在需要换ip的时候，不建议重新new一个该对象，建议使用以下方法
 *
 * @author kafka
 */
public class HttpRequestData implements Serializable {
	private static final int TIMEOUT = 5 * 10000;

	/**
	 *
	 */
	private static final long serialVersionUID = -3942064259653141609L;

	private transient Logger logger = LoggerFactory.getLogger(HttpRequestData.class);

	private Map<String, String> headers;
	private transient HttpClient httpClient;

	// 是否使用代理
	private boolean useProxy;

	private String proxyIp;
	private int proxyPort;

	/**
	 * 默认使用代理
	 */
	public HttpRequestData() {
		this(false);
	}

	public HttpRequestData(boolean useProxy) {
		this.useProxy = useProxy;
	}

	public HttpRequestData(boolean useProxy, String proxyIp, int proxyPort) {
		this.useProxy = useProxy;
		this.proxyIp = proxyIp;
		this.proxyPort = proxyPort;
	}

	public Map<String, String> getHeaders() {
		if (null == headers)
			headers = new HashMap<>();
		return headers;
	}

	public void setHeaders(String name, String value) {
		headers = getHeaders();
		headers.put(name, value);
	}

	public void removeHeader(String name) {
		getHeaders().remove(name);
	}

	public void clearHeaders() {
		getHeaders().clear();
	}

	/**
	 * 获取httpClient
	 * <p>
	 * 如果需要并发使用httpClient，务必记得使用MultiThreadedHttpConnectionManager
	 * 如果需要设置cookie，调用方法 initHttpClientCookie
	 */
	public HttpClient getHttpClient(boolean useProxy, String proxyIp, int proxyPort) {
		if (null != this.httpClient) {
			return this.httpClient;
		}

		this.httpClient = new HttpClient(new HttpClientParams(), new MultiThreadedHttpConnectionManager());// 连接在releaseConnection后总是被关闭
		// 设置代理IP
		if (useProxy && !StringUtils.isEmpty(proxyIp)) {
			httpClient.getHostConfiguration().setProxy(proxyIp, proxyPort);
		}
		try {
//			httpClient.getHostConfiguration().setProxy("127.0.0.1", 8888);
			httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			httpClient.getParams().setSoTimeout(TIMEOUT);// 20秒超时response
			httpClient.getParams().setConnectionManagerTimeout(TIMEOUT);// 20秒超时connect
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(TIMEOUT);
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return this.httpClient;
	}

	public HttpClient getHttpClient() {
		return getHttpClient(this.useProxy, this.proxyIp, this.proxyPort);
	}

	/**
	 * 添加请求所需的cookies
	 *
	 * @param domain
	 *            域
	 * @param cookies
	 *            值
	 */
	public void initHttpClientCookie(String domain, Map<String, String> cookies) {
		if (null == cookies || cookies.isEmpty()) {
			return;
		}
		for (Map.Entry<String, String> entry : cookies.entrySet()) {
			getHttpClient().getState().addCookie(new Cookie(domain, entry.getKey(), entry.getValue(), "/", null, false));
		}
	}

	/**
	 * 添加请求所需的cookies
	 *
	 * @param domain
	 *            域
	 * @param cookies
	 *            值
	 */
	public void initHttpClientCookieNew(String domain, String cookies) {
		if (StringUtils.isBlank(cookies)) {
			return;
		}
		for (String x : cookies.split(";")) {
			String[] xx = x.trim().split("=");
			if (xx.length > 1) {
				String val = StringUtils.join(Arrays.asList(xx).subList(1, xx.length), "=");
				getHttpClient().getState().addCookie(new Cookie(domain, xx[0], val, "/", null, false));
			}
		}
	}

	/**
	 * 添加请求所需的cookies
	 *
	 * @param domain
	 *            域
	 * @param cookies
	 *            值
	 */
	public void initHttpClientCookie(String domain, String cookies) {
		if (StringUtils.isBlank(cookies)) {
			return;
		}
		for (String x : cookies.split(";")) {
			String[] xx = x.trim().split("=");
			String val = StringUtils.join(Arrays.asList(xx).subList(1, xx.length), "=");
			getHttpClient().getState().addCookie(new Cookie(domain, xx[0], val, "/", null, false));
		}
	}

	/**
	 * 添加请求所需的cookies
	 *
	 * @param domain
	 *            域
	 * @param name
	 *            cookie名称
	 * @param value
	 *            值
	 */
	public void initHttpClientCookie(String domain, String name, String value) {
		getHttpClient().getState().addCookie(new Cookie(domain, name, value, "/", null, false));
	}

	public String getCookieValue(HttpRequestData data, String name) {
		Cookie[] cookies = data.getHttpClient().getState().getCookies();
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals(name)) {
				return cookies[i].getValue();
			}

		}
		return "";
	}

	public void printCookie() {
		logger.info("=====================cookie=====================");
		for (Cookie e : getHttpClient().getState().getCookies()) {
			logger.error(e.getDomain() + ":\t" + e.getName() + "=" + e.getValue());
		}
		logger.info("=====================cookie end=====================");
	}
}