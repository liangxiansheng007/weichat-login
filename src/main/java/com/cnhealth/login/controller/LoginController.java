package com.cnhealth.login.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONPOJOBuilder;
import com.cnhealth.login.common.Httputil;
import com.cnhealth.login.common.WeixinUtil;
import org.apache.http.HttpRequest;
import org.apache.http.client.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录控制模块
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private HttpSession session;

    @RequestMapping("/getQr")
    public String getQr(){
        Map result = new HashMap();
        result.put("code","0");
        try{
            //获取token
            String token = Httputil.doGet(WeixinUtil.token_url+"?grant_type=client_credential&appid="+WeixinUtil.appid+"&secret="+WeixinUtil.appsecret);
            System.out.println("----token==="+token);
            Map tokenJson = (Map) JSON.parse(token);
            Object tokenObject = tokenJson.get("access_token");
            if(tokenObject==null){
                result.put("message","get access_token errror");
                JSON.toJSONString(result);
            }
            String access_token = String.valueOf(tokenObject);
            WeixinUtil.access_token=access_token;
            //获取ticket
            Map<String, Object> param = new HashMap<>();
            param.put("expire_seconds","300");//有效时间5分钟
            param.put("action_name","QR_STR_SCENE");

            Map scene_id = new HashMap();
            String keyId = session.getId();
            System.out.println("----sessionid==="+keyId);
            scene_id.put("scene_str",keyId);
            Map scene = new HashMap();
            scene.put("scene",scene_id);
            param.put("action_info",scene);

            String ticket = Httputil.doPostForRaw(WeixinUtil.ticket_url+"?access_token="+access_token,param);
            System.out.println("----ticket==="+ticket);
            Map ticketJson = (Map) JSON.parse(ticket);
            Object ticketObject = ticketJson.get("ticket");
            if(ticketObject==null){
                result.put("message","get ticket errror");
                JSON.toJSONString(result);
            }
            result.put("code","1");
            result.put("message","success");
            result.put("url", WeixinUtil.showqr_url+ticketObject);
            result.put("keyId", keyId);
        }catch(Exception e){
            e.printStackTrace();
            result.put("message",e);
        }
        return JSON.toJSONString(result);
    }

}
