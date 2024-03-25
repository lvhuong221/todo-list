package com.lvhuong.TodoList.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = {"/", "/home", "/index"})
    public String homePage(){
        return "index";
    }

    @GetMapping(value = {"/profile"})
    public String homeProfilePage(){
        return "profile";
    }

    @GetMapping(value = {"/todo-list"})
    public String todoListPage(){
        return "todo-list";
    }


    @GetMapping(value = {"/login"})
    public String loginPage(){
        return "login";
    }
}
