package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.model.Bureau;
import com.wts.entity.model.Wrong;
import com.wts.interceptor.LoginInterceptor;
import org.apache.log4j.Logger;

import java.util.Date;

public class WrongController extends Controller {
    private static Logger logger = Logger.getLogger(Wrong.class);
    public void index() {
        render("/wrong.html");
    }

    @Before(LoginInterceptor.class)
    public void Query() {
        String select = "SELECT" +
                "  county.id AS countyId," +
                "  county.name AS countyName," +
                "  bureau.id AS bureauId," +
                "  bureau.name AS bureauName," +
                "  department.id AS departmentId," +
                "  department.name AS departmentName," +
                "  department.address AS departmentAddress," +
                "  department.img AS departmentImg," +
                "  department.latitude AS departmentLatitude," +
                "  department.longitude AS departmentLongitude," +
                "  department.duty AS departmentDuty," +
                "  department.remark AS departmentRemark," +
                "  wrong.*";
        String sqlExceptSelect = "FROM" +
                " wrong" +
                " LEFT JOIN department ON department.id = wrong.did" +
                " LEFT JOIN bureau ON bureau.id = department.bid" +
                " LEFT JOIN county ON county.id = bureau.cid" +
                " WHERE county.name LIKE '%" + getPara("keyword") + "%' " +
                " OR bureau.name LIKE '%" + getPara("keyword") + "%' " +
                " OR department.name LIKE '%" + getPara("keyword") + "%' " +
                " OR wrong.type LIKE '%" + getPara("keyword") + "%' " +
                " OR wrong.content LIKE '%" + getPara("keyword") + "%' " +
                " ORDER BY" +
                " wrong.id DESC";
        renderJson(Db.paginate(
                getParaToInt("pageCurrent"),
                getParaToInt("pageSize"),
                select,
                sqlExceptSelect).getList());
    }

    @Before(LoginInterceptor.class)
    public void Total() {
        Long count = Db.queryLong("SELECT COUNT(*) FROM wrong " +
                " LEFT JOIN department ON department.id = wrong.did" +
                " LEFT JOIN bureau ON bureau.id = department.bid" +
                " LEFT JOIN county ON county.id = bureau.cid" +
                " WHERE county.name LIKE '%" + getPara("keyword") + "%' " +
                " OR bureau.name LIKE '%" + getPara("keyword") + "%' " +
                " OR department.name LIKE '%" + getPara("keyword") + "%' " +
                " OR wrong.type LIKE '%" + getPara("keyword") + "%' " +
                " OR wrong.content LIKE '%" + getPara("keyword") + "%' ");
        renderText(count.toString());
    }

    @Before(LoginInterceptor.class)
    public void Get() {
        renderJson(Wrong.dao.findById(getPara("id")));
    }

    @Before({Tx.class, LoginInterceptor.class})
    public void Del() {
        Wrong w = Wrong.dao.findById(getPara("id"));
        renderText("OK");
    }

    @Before({Tx.class, LoginInterceptor.class})
    public void Add() {
        Wrong wrong = new Wrong();
        wrong.set("did", getPara("departmentId"))
                .set("type", getPara("type"))
                .set("content", getPara("content"))
                .set("time", new Date())
                .save();
        logger.warn("function:" + this.getClass().getSimpleName() + "/Add;" + ";time:" + new Date() + ";");
        renderText("OK");
    }

}
