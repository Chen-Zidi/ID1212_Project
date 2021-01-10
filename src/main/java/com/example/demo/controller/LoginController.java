package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.model.Room;
import com.example.demo.model.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class LoginController {

    String url = "https://localhost:10086/";
    RoomController rc = new RoomController();

    @RequestMapping("/test")
    public String test(Model model) {
        return "test";
    }

    //logout
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        System.out.println("logout");
//        session.invalidate();
//        session.setAttribute("username", username);
//        session.setAttribute("userId",userId);
        return "redirect:/login";
    }

    //login html page
    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("errorInfo", "");
        return "login";
    }

    //deal with user input for login
    @RequestMapping("/loginRequest")
    public String getLoginInput(HttpServletRequest request, HttpSession session, Model model) {
        //get param from page
        String username = request.getParameter("inputName");
        String password = request.getParameter("inputPassword");
        String result = requestLoginAPI(username,password);

        //if user not exist
        if(result.equals("UserNotExist")){
            model.addAttribute("errorInfo", "User not exist");
            return "login";
        }

        //if password is not right
        if(result.equals("PasswordNotRight")){
            model.addAttribute("errorInfo", "Wrong password");
            return "login";
        }


        int userId = Integer.parseInt(result.split("=|,")[1]);

        //set session
        session.setAttribute("username", username);
        session.setAttribute("userId",userId);

        return "redirect:/";
    }

    //call backend api for login
    public String requestLoginAPI(String name, String password) {
        RestTemplate restTemplate = new RestTemplate();

        //set param
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("username",name);
        paramMap.add("password",password);

        //call backend and return a string
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url+"login", paramMap, String.class);
        String body = responseEntity.getBody();
        return body;
    }


    //register html page
    @RequestMapping("/register")
    public String register() {
        return "register";

    }

    //get user inputs in register page
    @PostMapping("/registerRequest")
    public String getRegisterInput(HttpServletRequest request, HttpSession session, Model model){
        String username = request.getParameter("inputName");
        String password = request.getParameter("inputPassword");
        String email = request.getParameter("inputEmail");
        String phone = request.getParameter("inputPhone");

        String result = requestRegisterAPI(username, password, email, phone);

        return "redirect:/login";
    }

    //call backend api for register
    public String requestRegisterAPI(String name, String password, String email, String phone) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("username",name);
        paramMap.add("password",password);
        paramMap.add("email",email);
        paramMap.add("phone_number",phone);



        //call backend and return a string
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url+"register", paramMap, String.class);
        String body = responseEntity.getBody();
        return body;
    }

    //go to main chat room page
    @RequestMapping("/")
    public String index(HttpServletRequest request, HttpSession session,Model model) {


        List<Room> roomList = rc.requestAllRoomAPI();

        if(session.getAttribute("username") == null){
            return "redirect:/login";
        }

        //set model for html
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("RoomList",roomList);
        model.addAttribute("displayRoom", false);
        model.addAttribute("displayRoomId", -1);


        return "index";
    }




}
