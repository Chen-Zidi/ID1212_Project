package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.model.Room;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RoomController {

    String url = "http://localhost:10086/";

    @RequestMapping("/requestEnterRoom")
    public String verifyUserRoomPassword(HttpServletRequest request, HttpSession session, Model model){
        String correctPw = request.getParameter("requestRoomPassword");
        String inputPw = request.getParameter("inputRoomPassword");
        String roomId = request.getParameter("requestRoomId");

        List<Message> msgList = new ArrayList<Message>();
        List<Room> roomList = requestAllRoomAPI();

        if(!correctPw.equals(inputPw)){
            model.addAttribute("verifyResult","Wrong Room Password!");
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("MessageList", msgList);
            model.addAttribute("RoomList",roomList);
            return "index";
        }

        System.out.println("correct");
        return "redirect:/";
    }

    //get user inputs in creating a new room form
    @RequestMapping("/requestAddRoom")
    public String getNewRoomInput(HttpServletRequest request, HttpSession session, Model model){
        String topic = request.getParameter("inputTopic");
        String password = request.getParameter("inputPassword");
        String type = request.getParameter("inputType");

        System.out.println(topic+password+type);

        String result = requestAddRoomAPI(topic, password, type);
        System.out.println(result);
        return "redirect:/";
    }

    //call backend api to add a new room
    public String requestAddRoomAPI(String topic, String password, String type) {
        RestTemplate restTemplate = new RestTemplate();

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

//    //get one specific room from backend
//    public Room requestRoomAPI(){
//
//    }

}
