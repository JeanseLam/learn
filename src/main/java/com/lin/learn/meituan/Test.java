package com.lin.learn.meituan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lin.learn.meituan.httpclient.FetchUtils;
import com.lin.learn.meituan.httpclient.HttpRequestData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Test {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {

        String mobileNo = "17620304882";
        String loginName = "linjinzhi";
        String password = "zhi19940306";

        HttpRequestData httpRequestData = new HttpRequestData();
        Map<String, String> headers = new HashMap<>();
        Map<String, String> params = new HashMap<>();

        try {

            // 请求登陆页面
            httpRequestData.setHeaders("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            httpRequestData.setHeaders("Accept-Language", "zh-CN,zh;q=0.9");
            httpRequestData.setHeaders("Cache-Control", "max-age=0");
            httpRequestData.setHeaders("Connection", "keep-alive");
            httpRequestData.setHeaders("Host", "e.meituan.com");
            httpRequestData.setHeaders("Upgrade-Insecure-Requests", "1");
            httpRequestData.setHeaders("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36");
            String html = FetchUtils.get(httpRequestData,
                    "http://e.meituan.com/meishi/epassport/login?service=com.sankuai.meishi.fe.ecom&continue=" +
                            "http%3A%2F%2Fe.meituan.com%2Fmeishi%2Fepassport%2Ftoken%3Ftarget%3Dhttp%253A%252F%252Fe.meituan.com%252Fmeishi%252F");
            httpRequestData.setHeaders("Host", "epassport.meituan.com");
            html = FetchUtils.get(httpRequestData,
                    "https://epassport.meituan.com/account/unitivelogin/phone?service=com.sankuai.meishi.fe.ecom&bg_source=1&part_type=0&continue=" +
                            "http%3A%2F%2Fe.meituan.com%2Fmeishi%2Fepassport%2Ftoken%3Ftarget%3Dhttp%253A%252F%252Fe.meituan.com%252Fmeishi%252F&feconfig=bssoify");

            // 请求生成token
            System.setProperty("phantomjs.binary.path", "C:\\Users\\linsf\\Desktop\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");

            DesiredCapabilities cap = DesiredCapabilities.phantomjs();
            cap.setCapability("phantomjs.page.settings.loadImages" + "loadImages", false);
            cap.setCapability("phantomjs.page.settings.javascriptEnabled", true);
            cap.setBrowserName("chrome");

            // phantomjs设置头部
            cap.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Host", "e.meituan.com");
            cap.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Connection", "keep-alive");
            cap.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Cache-Control", "no-cache");
            cap.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Accept", "*/*");
            cap.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.67 Safari/537.36");

            PhantomJSDriver webDriver = new PhantomJSDriver(cap);

            String rohrJs = getRohrJsStr();
            String rohrInit = "window.rohrdata = \"\";\n" +
                    "window.Rohr_Opt = new Object;\n" +
                    "window.Rohr_Opt.Flag = 100043;\n" +
                    "window.Rohr_Opt.LogVal = \"rohrdata\";";

            webDriver.executeScript(rohrInit);
            webDriver.executeScript(rohrJs);

            String getTokenJs = "var data = \"com.sankuai.meishi.fe.ecom&bg_source=1&part_type=0&continue=http%3A%2F%2Fe.meituan.com%2Fmeishi%2Fepassport%2Ftoken%3Ftarget%3Dhttp%253A%252F%252Fe.meituan.com%252Fmeishi%252F&feconfig=bssoify\"\n" +
                    "window.Rohr_Opt.reload(data);\n" +
                    "var token = decodeURIComponent(window.rohrdata);\n" +
                    "return token;";
            String token = (String) webDriver.executeScript(getTokenJs);
            System.out.println(token);

            // 账号登陆
            // todo

            // 短信登陆
            httpRequestData.setHeaders("Accept", "application/json");
            httpRequestData.setHeaders("Origin", "https://epassport.meituan.com");
            httpRequestData.setHeaders("x-requested-with", "XMLHttpRequest");
            httpRequestData.setHeaders("Content-Type", "application/json;charset=UTF-8");
            httpRequestData.setHeaders("Host", "epassport.meituan.com");

            JSONObject smsParams = new JSONObject();
            smsParams.put("mobile", mobileNo);
            smsParams.put("sms_code", "");
            smsParams.put("intercode", "86");
            smsParams.put("isFetching", false);
            smsParams.put("success", "");
            smsParams.put("error", "");
            smsParams.put("loginType", "mobile");
            smsParams.put("search", "?service=com.sankuai.meishi.fe.ecom&bg_source=1&part_type=0&continue=http%3A%2F%2Fe.meituan.com%2Fmeishi%2Fepassport%2Ftoken%3Ftarget%3Dhttp%253A%252F%252Fe.meituan.com%252Fmeishi%252F&feconfig=bssoify");
            smsParams.put("verify_event", 2);
            html = FetchUtils.postJson(httpRequestData, "https://epassport.meituan.com/api/biz/auth/sms?service=com.sankuai.meishi.fe.ecom&bg_source=1&part_type=0", smsParams);
            System.out.println(html);

            String smsCode = new Scanner(System.in).nextLine();
            httpRequestData.setHeaders("Connection", "keep-alive");
            JSONObject loginParams = JSON.parseObject("{\"mobile\":\"" + mobileNo + "\",\"sms_code\":\"" + smsCode + "\",\"intercode\":\"86\",\"isFetching\":false,\"success\":\"\",\"error\":\"\",\"loginType\":\"mobile\",\"search\":\"?service=com.sankuai.meishi.fe.ecom&bg_source=1&part_type=0&continue=http%3A%2F%2Fe.meituan.com%2Fmeishi%2Fepassport%2Ftoken%3Ftarget%3Dhttp%253A%252F%252Fe.meituan.com%252Fmeishi%252F&feconfig=bssoify\",\"continue\":\"http://e.meituan.com/meishi/epassport/token?target=http%3A%2F%2Fe.meituan.com%2Fmeishi%2F\",\"rohrToken\":\"" + token + "\"}");
            html = FetchUtils.postJson(httpRequestData, "https://epassport.meituan.com/api/account/login?service=com.sankuai.meishi.fe.ecom&bg_source=1&part_type=0&loginContinue=http:%2F%2Fe.meituan.com%2Fmeishi%2Fepassport%2Ftoken%3Ftarget%3Dhttp%253A%252F%252Fe.meituan.com%252Fmeishi%252F&loginType=mobile", loginParams);
            System.out.println(html);

            String bsid = JSON.parseObject(html).getString("bsid");
            httpRequestData.setHeaders("Host", "e.meituan.com");
            httpRequestData.setHeaders("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            httpRequestData.setHeaders("Accept-Language", "zh-CN,zh;q=0.9");
            html = FetchUtils.get(httpRequestData, "http://e.meituan.com/meishi/epassport/token?target=http%3A%2F%2Fe.meituan.com%2Fmeishi%2F&BSID=" + bsid + "&source=TOKEN_SOURCE_LOGIN");
            System.out.println(html);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }

    private static String getRohrJsStr() {
        try {
            String rohrUrl = "https://static.meituan.net/mx/rohr/rohr.min.js";
            HttpRequestData httpRequestData = new HttpRequestData();
            return FetchUtils.get(httpRequestData, rohrUrl);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }
}
