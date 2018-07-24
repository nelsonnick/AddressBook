package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.model.Bureau;
import com.wts.interceptor.LoginInterceptor;
import org.apache.log4j.Logger;

import java.util.Date;

public class BureauController extends Controller {
    private static Logger logger = Logger.getLogger(Bureau.class);
    public void index() {
        render("/bureau.html");
    }

    @Before(LoginInterceptor.class)
    public void Query() {
        renderJson(Db.paginate(
                getParaToInt("pageCurrent"),
                getParaToInt("pageSize"),
                "SELECT bureau.*, county.id AS countyId, county.name AS countyName",
                "FROM bureau LEFT JOIN county ON bureau.cid = county.id " +
                        "WHERE bureau.name LIKE '%" + getPara("keyword") + "%' " +
                        "OR bureau.url LIKE '%" + getPara("keyword") + "%' " +
                        "OR county.name LIKE '%" + getPara("keyword") + "%' " +
                        "ORDER BY bureau.id DESC").getList());
    }

    @Before(LoginInterceptor.class)
    public void Total() {
        Long count = Db.queryLong("SELECT COUNT(*) FROM bureau LEFT JOIN county ON bureau.cid = county.id " +
                "WHERE bureau.name LIKE '%" + getPara("keyword") + "%' " +
                "OR bureau.url LIKE '%" + getPara("keyword") + "%' " +
                "OR county.name LIKE '%" + getPara("keyword") + "%' ");
        renderText(count.toString());
    }

    @Before(LoginInterceptor.class)
    public void Get() {
        renderJson(Bureau.dao.findById(getPara("id")));
    }

    @Before({Tx.class, LoginInterceptor.class})
    public void Del() {
        Bureau b = Bureau.dao.findById(getPara("id"));
        renderText("OK");
    }

    @Before({Tx.class, LoginInterceptor.class})
    public void Add() {
        Bureau bureau = new Bureau();
        bureau.set("name", getPara("name"))
                .set("url", getPara("url"))
                .set("duty", getPara("duty"))
                .set("remark", getPara("remark"))
                .set("cid", getPara("countyId"))
                .save();
        logger.warn("function:" + this.getClass().getSimpleName() + "/Add;" + ";time:" + new Date() + ";");
        renderText("OK");
    }

    @Before({Tx.class, LoginInterceptor.class})
    public void Edit() {
        Bureau bureau = Bureau.dao.findById(getPara("id"));
        if (bureau == null) {
            renderText("要修改的信息不存在！");
        } else {
            bureau.set("name", getPara("name"))
                    .set("url", getPara("url"))
                    .set("duty", getPara("duty"))
                    .set("remark", getPara("remark"))
                    .set("cid", getPara("countyId"))
                    .update();
            logger.warn("function:" + this.getClass().getSimpleName() + "/Edit;" + ";time:" + new Date() + ";");
            renderText("OK");
        }
    }

}
