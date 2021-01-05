package com.example.demo.controller;

import com.example.demo.model.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {

    @PostMapping("/SendMessage")
    public String userSendMessage(@RequestParam("myfile") MultipartFile file, HttpServletRequest request, HttpSession session, Model model){
        List<Message> msgList = new ArrayList<Message>();
        String msgContent = request.getParameter("message");

        //deal with file
        if(file.isEmpty()){
            System.out.println("empty file");
//            return "index";
        }else {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                String path = "./FileStorage/";
                inputStream = file.getInputStream();
                String fileName = file.getOriginalFilename();
                File targetFile = new File(path + fileName);
                if (!targetFile.getParentFile().exists()) {
                    targetFile.getParentFile().mkdir();
                }
                outputStream = new FileOutputStream(targetFile);
                FileCopyUtils.copy(inputStream, outputStream);
                System.out.println("file upload success");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("file upload fail");
            } finally {//close input and output stream
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        //deal with messages
        Message msg = new Message("Paige","receiver",msgContent,new Date());
        msgList.add(msg);
        msgList.add(new Message("nobody","Paige","ok",new Date()));
//        System.out.println(msgContent);

        model.addAttribute("username", "Paige");
        model.addAttribute("MessageList",msgList);

        return "redirect:/";
    }



}
