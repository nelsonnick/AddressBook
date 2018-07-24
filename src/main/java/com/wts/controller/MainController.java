package com.wts.controller;

import com.jfinal.core.Controller;

public class MainController extends Controller {

    public void index() {
        render("/index.html");
    }

    public void login() {
        if (getPara("user").equals("a") && getPara("password").equals("a")){
            setSessionAttr("user", "a");
            renderText("OK");
        } else {
            renderText("Error");
        }
    }
}
