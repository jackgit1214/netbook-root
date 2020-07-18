package com.book.web;

import com.book.web.util.SignatureUtil;
import com.framework.web.controller.BaseController;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("/wx")
public class WXController extends BaseController {

    @RequestMapping(value = "/checkSignature", method = {RequestMethod.GET, RequestMethod.POST})
    public void verification(HttpServletRequest request,
                             HttpServletResponse response) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");

        this.log.debug(signature);
        this.log.debug(timestamp);
        this.log.debug(timestamp);

        if (SignatureUtil.checkSignature(signature, timestamp, nonce)) {
            // 随机字符串
            String echostr = request.getParameter("echostr");
            this.log.debug("接入成功，echostr:" + echostr);
            try {
                response.getWriter().write(echostr);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/getMsg", method = RequestMethod.POST)
    public void acceptMessage(HttpServletRequest request,
                              HttpServletResponse response) {
        String message = request.getParameter("ToUserName");
        this.log.debug("post ..................................");
        // 从request中取得输入流
        InputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            // 读取输入流
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            // 得到xml根元素
            Element root = document.getRootElement();
            // 得到根元素的所有子节点
            List<Element> elementList = root.elements();

            // 遍历所有子节点
            for (Element e : elementList)
                this.log.debug(e.getName() + ":" + e.getText());
            //map.put(e.getName(), e.getText());

            // 释放资源
            inputStream.close();
            inputStream = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
