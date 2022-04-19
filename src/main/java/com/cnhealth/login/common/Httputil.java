package com.cnhealth.login.common;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class Httputil {
    public static String doGet(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        return execute(httpGet);
    }

    public static String doPost(String url, Map<String, String> param) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        ArrayList<BasicNameValuePair> arrayList = new ArrayList<BasicNameValuePair>();
        Set<String> keySet = param.keySet();
        for (String key : keySet) {
            arrayList.add(new BasicNameValuePair(key, param.get(key)));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
        return execute(httpPost);
    }

    public static String doPostForRaw(String url, Map<String, Object> param) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        System.out.println("------------json==="+JSON.toJSONString(param));
        StringEntity postingString = new StringEntity(JSON.toJSONString(param));
        httpPost.setEntity(postingString);
        httpPost.setHeader("Content-type", "application/json");
        return execute(httpPost);
    }

    private static String execute(HttpRequestBase request) throws IOException, ClientProtocolException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(request);
        if (200 == response.getStatusLine().getStatusCode()) {
            return EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
        } else {
            System.out.println(EntityUtils.toString(response.getEntity(), Charset.forName("utf-8")));
        }
        return "";
    }

    public static String ReadAsChars(HttpServletRequest request)
    {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try
        {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null)
            {
                sb.append(str);
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != br)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
