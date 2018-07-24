package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.wts.entity.model.Bureau;
import com.wts.entity.model.County;
import com.wts.interceptor.LoginInterceptor;

import java.util.List;

public class MainController extends Controller {

    public void index() {
        render("/index.html");
    }

    public void login() {
        if (getPara("user").equals("1") && getPara("password").equals("1")){
            setSessionAttr("user", "a");
            renderText("OK");
        } else {
            renderText("Error");
        }
    }

    @Before(LoginInterceptor.class)
    public void getCounty() {
        List<County> counties = County.dao.find("SELECT * FROM county");
        String str = "";
        for (County county : counties) {
            str = str + "{value: '" + county.getId() + "',label:'" + county.getName() + "'},";
        }
        str = str.substring(0, str.length() - 1);
        renderJson("[" + str + "]");
    }

    @Before(LoginInterceptor.class)
    public void getBureau() {
        List<Bureau> Bureaues = Bureau.dao.find("SELECT * FROM bureau WHERE cid = " + getPara("countyId"));
        String str = "";
        for (Bureau bureau : Bureaues) {
            str = str + "{value: '" + bureau.getId() + "',label:'" + bureau.getName() + "'},";
        }
        str = str.substring(0, str.length() - 1);
        renderJson("[" + str + "]");
    }

}
