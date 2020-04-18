package com.book.web;

import com.book.model.Booktype;
import com.book.model.Netbook;
import com.book.model.ResponseResult;
import com.book.service.BookService;
import com.framework.exception.BusinessException;
import com.framework.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class BookController extends BaseController {

    @Autowired
    private BookService bookServiceImpl;

    @ResponseBody
    @RequestMapping("/index")
    public ModelAndView mainIndex(String bookId) {
        System.out.println("----------------9999999999uuuuuuuuuuuuu999999---------");

        ModelAndView mav = new ModelAndView("index");
        return mav;
    }

    @RequestMapping("/img")
    public void getBookImage(String bookId, HttpServletRequest request,
                             HttpServletResponse response) {

        byte[] bytes = this.bookServiceImpl.getBookImageByBook(bookId);
        OutputStream os = null;// 将图像输出到Servlet输出流中。
        try {
            //ByteArrayInputStream in = new ByteArrayInputStream(bytes);    //将b作为输入流；
            //BufferedImage image = ImageIO.read(in);
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            os = response.getOutputStream();// 将图像输出到Servlet输出流中。
            os.write(bytes);
            //ImageIO.write(image, "jpeg", sos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/image64")
    public String imageBase64(String bookId) throws Exception {
        if (bookId == null) {
            throw new BusinessException("业务信息处理失败！！！");
        }
        byte[] bytes = this.bookServiceImpl.getBookImageByBook(bookId);
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        //返回Base64编码过的字节数组字符串
        return encoder.encode(bytes);
    }

    @ResponseBody
    @RequestMapping(value = "/category1")
    public ModelMap getCatelogy() throws Exception{
        ModelMap mm = new ModelMap();
        List<Booktype> categories =  this.bookServiceImpl.getBookCategory();
        ResponseResult resResult = new ResponseResult("1","类型获取成功！",categories);
        mm.put("result",resResult);
        return mm;
    }

    @ResponseBody
    @RequestMapping(value = "/category")
    public ResponseResult getCatelogy1() throws Exception{
        List<Booktype> categories =  this.bookServiceImpl.getBookCategory();
        ResponseResult resResult = new ResponseResult("1","类型获取成功！",categories);
        return resResult;
    }

    @ResponseBody
    @RequestMapping(value = "/books")
    public ResponseResult getBooks(String categoryKey){
        List<Netbook> books = this.bookServiceImpl.getNetBookByType(categoryKey);
        ResponseResult resResult = new ResponseResult("1","类型获取成功！",books);
        return resResult;
    }
}
