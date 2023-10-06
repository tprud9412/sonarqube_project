package gu.common;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

// 추가한 import 문
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Upload4ckeditor {
    static final Logger LOGGER = LoggerFactory.getLogger(AdminInterceptor.class);

    /**
     * ckeditor의 이미지 업로드 처리.
     */
    @RequestMapping(value = "/upload4ckeditor", method = {RequestMethod.GET, RequestMethod.POST})
    public void upload(HttpServletResponse response, HttpServletRequest request, MultipartFile upload) {
        String callback = request.getParameter("CKEditorFuncNum");
        
        String filePath = LocaleMessage.getMessage("info.filePath");
        String newName = FileUtil.getNewName();
            
        FileUtil.saveFileOne(upload, filePath + "/" + newName.substring(0,4) + "/", newName);

        String url = request.getRequestURL().toString();
        Integer inx = url.lastIndexOf("/");
        url = url.substring(0, inx);
        url = url + "/fileDownload?downname=" + newName;
        
        url = "<script type='text/javascript'>window.parent.CKEDITOR.tools.callFunction(" + callback + ",'" + url + "','upload completed!')</script>";
        
        try { 
            response.getWriter().write(url);
        } catch (IOException ex) {
            LOGGER.error("Error: upload4ckeditor");
        }
    }
    
}
