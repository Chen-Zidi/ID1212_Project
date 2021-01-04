package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/UserLogin")
    public String userLogin(HttpServletRequest request, HttpSession session, Model model){
        String email = request.getParameter("inputEmail");
        String password = request.getParameter("inputPassword");

        System.out.println(email);
        System.out.println(password);

        model.addAttribute("username", "Paige");




        return "redirect:/";
    }

    @RequestMapping("/")
    public String index(Model model){

        List<Message> msgList = new ArrayList<Message>();
        msgList.add(new Message("Bob","Paige","Could you please close the window?",new Date()));
        msgList.add(new Message("Paige","Bob","ok",new Date()));


        model.addAttribute("username", "Paige");
        model.addAttribute("MessageList",msgList);


        return "index";
    }

    @RequestMapping("/test")
    public String test(Model model){

        List<Message> msgList = new ArrayList<Message>();


        model.addAttribute("MessageList",msgList);
        return "test";
    }

    @RequestMapping("/register")
    public String register(){
        return "register";
    }



}
