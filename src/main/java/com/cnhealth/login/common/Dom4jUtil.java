package com.cnhealth.login.common;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Dom4jUtil {

    /**
           * @description 将xml字符串转换成map
           * @param xml
           * @return Map
           */
      public static Map readStringXmlOut(String xml) {
                 Map<String,String> map = new HashMap();
                 Document doc = null;
                 try {
                         // 将字符串转为XML
                         doc = DocumentHelper.parseText(xml);

                     Element root = doc.getRootElement();
                     List<Element> elementList = root.elements();

                     for (Element ele:elementList){
                         map.put(ele.getName(),ele.getTextTrim());
                     }
                     } catch (DocumentException e) {
                         e.printStackTrace();
                    } catch (Exception e) {
                         e.printStackTrace();
                     }
                return map;
             }

             public static void main(String[] args){
          String a = "<xml><ToUserName><![CDATA[gh_2f31f5c2de87]]></ToUserName><FromUserName><![CDATA[o57Rn5hAZxKqyLmC5Kw4eVZPqFPA]]></FromUserName><CreateTime>1609834876</CreateTime><MsgType><![CDATA[event]]></MsgType><Event><![CDATA[SCAN]]></Event><EventKey><![CDATA[0D8558449F2AA9E1C2DDCB2D9C40FB78]]></EventKey><Ticket><![CDATA[gQFP7zwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAybVlWekk4RFFkREMxT3d5UTF2Y2MAAgR0IfRfAwQsAQAA]]></Ticket></xml>";
                 Map map = Dom4jUtil.readStringXmlOut(a);

                 ResponseMsg rmsg = new ResponseMsg();
                 rmsg.setContent("欢迎关注东半球最好的公众号");
                 rmsg.setCreateTime(System.currentTimeMillis());
                 rmsg.setFromUserName(String.valueOf(map.get("FromUserName")));
                 rmsg.setToUserName(String.valueOf(map.get("ToUserName")));
                 rmsg.setMsgType("text");
                 XStream xstream=new XStream();
                 xstream.alias("xml", rmsg.getClass());
                System.out.println(xstream.toXML(rmsg));
             }

}
