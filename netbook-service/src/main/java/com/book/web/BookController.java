package com.book.web;

import com.book.model.Booktype;
import com.book.model.Netbook;
import com.book.model.ResponseResult;
import com.book.service.BookService;
import com.book.web.util.AcceptParams;
import com.framework.exception.BusinessException;
import com.framework.mybatis.model.QueryModel;
import com.framework.mybatis.util.PageResult;
import com.framework.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController extends BaseController {

    @Autowired
    private BookService bookServiceImpl;

    @ResponseBody
    @RequestMapping("/index")
    public ModelAndView mainIndex(String bookId) {
        ModelAndView mav = new ModelAndView("index");
        return mav;
    }

    /**
     * 此方法显示图片失败
     *
     * @param bookId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/testimg/{bookId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable String bookId) throws IOException {

        byte[] bytes = this.bookServiceImpl.getBookImageByBook(bookId);
        if (bytes == null || bytes.length <= 0) {
            return new ResponseEntity<byte[]>(HttpStatus.NO_CONTENT);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/testimg1/{bookId}", method = RequestMethod.GET)
    public void getBookImage(@PathVariable String bookId, HttpServletResponse response) throws IOException {
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream("d://10058s.jpg");
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = this.bookServiceImpl.getBookImageByBook(bookId);
        response.setContentType("image/png");
        OutputStream stream;
        stream = response.getOutputStream();
        stream.write(bytes);
        stream.flush();
        stream.close();
    }

    @RequestMapping("/img")
    public void getBookImage(String bookId, HttpServletRequest request,
                             HttpServletResponse response) {

        byte[] bytes = this.bookServiceImpl.getBookImageByBook(bookId);
        OutputStream os = null;// 将图像输出到Servlet输出流中。
        try {
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            os = response.getOutputStream();// 将图像输出到Servlet输出流中。
            os.write(bytes);
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
    public ModelMap getCatelogy() throws Exception {
        ModelMap mm = new ModelMap();
        List<Booktype> categories = this.bookServiceImpl.getBookCategory();
        ResponseResult resResult = new ResponseResult("1", "类型获取成功！", categories);
        mm.put("result", resResult);
        return mm;
    }

    @ResponseBody
    @RequestMapping(value = "/category")
    public ResponseResult getCatelogy1() throws Exception {
        List<Booktype> categories = this.bookServiceImpl.getBookCategory();
        ResponseResult resResult = new ResponseResult("1", "类型获取成功！", categories);
        return resResult;
    }

    @CrossOrigin(origins = "*")
    @ResponseBody
    @RequestMapping(value = "/books", method = RequestMethod.POST, consumes = "application/json")
    public ResponseResult getBooks(@RequestBody AcceptParams queryParams) {

        ResponseResult dataResult = new ResponseResult();
        String bookType = queryParams.getOtherParams().get("category").toString();
        PageResult<Netbook> books = (PageResult<Netbook>) queryParams.getPageInfo();
        QueryModel queryModel = queryParams.paramsToQueryModel();
        try {
            this.bookServiceImpl.getNetBookPageByType(bookType, books, queryModel);

        } catch (Exception e) {
            e.printStackTrace();
        }
        dataResult.setResultData(books);
        dataResult.setCode("0");
        dataResult.setMessage("获取数据成功");
        return dataResult;
    }

    @CrossOrigin(origins = "*")
    @ResponseBody
    @RequestMapping(value = "/shareBook", method = RequestMethod.POST, consumes = "application/json")
    public ResponseResult shareBook2WX(@RequestBody AcceptParams queryParams) {

        ResponseResult dataResult = new ResponseResult();
        String bookId = queryParams.getOtherParams().get("bookId").toString();

        dataResult.setResultData("tst");
        dataResult.setCode("0");
        dataResult.setMessage("获取数据成功");
        return dataResult;
    }
}
