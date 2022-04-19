package com.cnhealth.login.controller;

import com.alibaba.fastjson.JSON;
import com.cnhealth.login.common.*;
import com.thoughtworks.xstream.XStream;
import org.apache.http.HttpRequest;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

/**
 * 微信登录信息接收
 */

@RestController
@RequestMapping("/accept")
public class AcceptController {




    @RequestMapping(value="/acceptMessage",method = RequestMethod.GET)
    public String authGet(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {

        //log.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature,
        //        timestamp, nonce, echostr);
        try{
            System.out.println("-----wx-message-----signature:"+signature+"----timestamp:"+timestamp+"----nonce:"+nonce+"----echostr:"+echostr);
            String bodyString = Httputil.ReadAsChars(request);
            System.out.println("-----request-message===="+ bodyString);
            Map<String,String> map = Dom4jUtil.readStringXmlOut(bodyString);
            String sessionid = String.valueOf(map.get("EventKey"));
            String openid = String.valueOf(map.get("FromUserName"));
            String userStr = Httputil.doGet(WeixinUtil.userinfo_url+"?access_token="+WeixinUtil.access_token+"&openid="+openid+"&lang=zh_CN");
            Map userMap = (Map) JSON.parse(userStr);
            userMap.put("openid",openid);
            MemoryUtil.memoryMap.put(sessionid,JSON.toJSONString(userMap));
            System.out.println("-----memoryMap check 1==="+JSON.toJSONString(MemoryUtil.memoryMap));

            String FromUserName = map.get("FromUserName");
            String ToUserName = map.get("ToUserName");
            String MsgType = map.get("MsgType");
            String responseMsg = "success";
            echostr = responseMsg;
            if("text".equals(MsgType)){
                ResponseMsg rmsg = new ResponseMsg();
                rmsg.setContent("欢迎关注东半球最好的公众号");
                rmsg.setCreateTime(System.currentTimeMillis());
                rmsg.setFromUserName(FromUserName);
                rmsg.setToUserName(ToUserName);
                rmsg.setMsgType("text");
                XStream xstream=new XStream();
                xstream.alias("xml", rmsg.getClass());
                responseMsg= xstream.toXML(rmsg);
                PrintWriter out = response.getWriter();
                out.print(responseMsg);
                out.close();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return echostr;
    }

    @RequestMapping(value="/acceptMessage",method = RequestMethod.POST)
    public String authPost(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {

        //log.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature,
        //        timestamp, nonce, echostr);
        try{
            System.out.println("-----wx-message-----signature:"+signature+"----timestamp:"+timestamp+"----nonce:"+nonce+"----echostr:"+echostr);
            String bodyString = Httputil.ReadAsChars(request);
            System.out.println("-----request-message===="+ bodyString);
            Map<String,String> map = Dom4jUtil.readStringXmlOut(bodyString);
            String sessionid = String.valueOf(map.get("EventKey"));
            String openid = String.valueOf(map.get("FromUserName"));
            String userStr = Httputil.doGet(WeixinUtil.userinfo_url+"?access_token="+WeixinUtil.access_token+"&openid="+openid+"&lang=zh_CN");
            Map userMap = (Map) JSON.parse(userStr);
            userMap.put("openid",openid);
            MemoryUtil.memoryMap.put(sessionid,JSON.toJSONString(userMap));
            System.out.println("-----memoryMap check 1==="+JSON.toJSONString(MemoryUtil.memoryMap));

            String FromUserName = map.get("FromUserName");
            String ToUserName = map.get("ToUserName");
            String MsgType = map.get("MsgType");
            String responseMsg = "success";
            echostr = responseMsg;
            if("text".equals(MsgType)){
                ResponseMsg rmsg = new ResponseMsg();
                rmsg.setContent("欢迎关注东半球最好的公众号");
                rmsg.setCreateTime(System.currentTimeMillis());
                rmsg.setFromUserName(FromUserName);
                rmsg.setToUserName(ToUserName);
                rmsg.setMsgType("text");
                XStream xstream=new XStream();
                xstream.alias("xml", rmsg.getClass());
                responseMsg= xstream.toXML(rmsg);
                PrintWriter out = response.getWriter();
                out.print(responseMsg);
                out.close();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return echostr;
    }


    @GetMapping(produces = "text/plain;charset=utf-8")
    @RequestMapping("/getopenid/{keyId}")
    public String getOpenid(@PathVariable String keyId){
        /*System.out.println("-----find-----keyId=="+keyId);
        System.out.println("-----find-----openid=="+MemoryUtil.memoryMap.get(keyId));
        System.out.println("-----memoryMap check 2==="+JSON.toJSONString(MemoryUtil.memoryMap));*/

        return MemoryUtil.memoryMap.get(keyId);
    }
}
