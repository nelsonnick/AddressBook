package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.model.County;
import com.wts.interceptor.LoginInterceptor;
import org.apache.log4j.Logger;

import java.util.Date;

public class CountyController extends Controller {
    private static Logger logger = Logger.getLogger(County.class);

    public void index() {
        render("/county.html");
    }

    @Before(LoginInterceptor.class)
    public void Query() {
        renderJson(Db.paginate(
                getParaToInt("pageCurrent"),
                getParaToInt("pageSize"),
                "SELECT county.id, county.name, county.url",
                "FROM county " +
                        "WHERE county.name LIKE '%" + getPara("keyword") + "%' " +
                        "OR county.url LIKE '%" + getPara("keyword") + "%' ORDER BY county.id DESC").getList());
    }

    @Before(LoginInterceptor.class)
    public void Total() {
        Long count = Db.queryLong("SELECT COUNT(*) FROM county " +
                "WHERE name LIKE '%" + getPara("keyword") + "%' " +
                "OR url LIKE '%" + getPara("keyword") + "%' ");
        renderText(count.toString());
    }

    @Before(LoginInterceptor.class)
    public void Get() {
        renderJson(County.dao.findById(getPara("id")));
    }

    @Before({Tx.class, LoginInterceptor.class})
    public void Del() {
        County u = County.dao.findById(getPara("id"));
        renderText("OK");
    }

    @Before({Tx.class, LoginInterceptor.class})
    public void Add() {

        County county = new County();
        county.set("name", getPara("name"))
                .set("url", getPara("url"))
                .save();
        logger.warn("function:" + this.getClass().getSimpleName() + "/Add;" + ";time:" + new Date() + ";");
        renderText("OK");
    }

    @Before({Tx.class, LoginInterceptor.class})
    public void Edit() {
        County county = County.dao.findById(getPara("id"));
        if (county == null) {
            renderText("要修改的信息不存在！");
        } else {
            county.set("name", getPara("name"))
                    .set("url", getPara("url"))
                    .update();
            logger.warn("function:" + this.getClass().getSimpleName() + "/Edit;" + ";time:" + new Date() + ";");
            renderText("OK");
        }
    }

}
