package com.example.demo.controller;

import com.example.demo.*;
import com.example.demo.model.Message;
import com.example.demo.model.Room;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class RoomController {

    String url = "https://localhost:10086/";

    //verify user password for entering the room
    @RequestMapping("/requestEnterRoom")
    public String verifyUserRoomPassword(HttpServletRequest request, HttpSession session, Model model){
       System.out.println("request Enter room function");

        //get inputs
        String correctPw = request.getParameter("requestRoomPassword");
        String inputPw = request.getParameter("inputRoomPassword");
        String roomId = request.getParameter("requestRoomId");

        //get all rooms from backend
        List<Room> roomList = requestAllRoomAPI();

        //if password is not correct, alert
        if(!correctPw.equals(inputPw)){
            model.addAttribute("verifyResult","Wrong Room Password!");
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("displayRoom", false);
            model.addAttribute("displayRoomId", -1);
            model.addAttribute("RoomList",roomList);
            return "index";
        }

        //if password is correct, then enter the room
        System.out.println("correct room password");

        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("displayRoom", true);
        model.addAttribute("displayRoomId", roomId);
        model.addAttribute("RoomList",roomList);
        return "index";
    }

    //get user inputs in creating a new room form
    @RequestMapping("/requestAddRoom")
    public String getNewRoomInput(HttpServletRequest request, HttpSession session, Model model){
       //get user inputs
        String topic = request.getParameter("inputTopic");
        String password = request.getParameter("inputPassword");
        String type = request.getParameter("inputType");


        //call backend api
        String result = requestAddRoomAPI(topic, password, type);

        return "redirect:/";
    }

    //call backend api to add a new room
    public String requestAddRoomAPI(String topic, String password, String type) {
        RestTemplate restTemplate = new RestTemplate();

        //set param
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("topic",topic);
        paramMap.add("password",password);
        paramMap.add("type",type);

        //call backend and return a string
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url+"room/add", paramMap, String.class);
        String body = responseEntity.getBody();
        return body;
    }

    //call backend api to have all rooms
    public List<Room> requestAllRoomAPI(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Room>> response = restTemplate.exchange(url+ "/room/get_all", HttpMethod.GET, null, new ParameterizedTypeReference<List<Room>>() {});
        List<Room> roomList = response.getBody();
        return roomList;

    }

    @RequestMapping("/RequestSendMessage")
    public void getUserMsg(@RequestParam("myfile") MultipartFile file, HttpServletRequest request, HttpSession session, Model model){

        System.out.println("request send message function");
        String msgContent = request.getParameter("message");

        //deal with file
        if(file.isEmpty()){
            System.out.println("no file");
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



//        //deal with messages
//        Message msg = new Message("Paige","receiver",msgContent,new Date());
//        msgList.add(msg);
//        msgList.add(new Message("nobody","Paige","ok",new Date()));
////        System.out.println(msgContent);


    }


}
