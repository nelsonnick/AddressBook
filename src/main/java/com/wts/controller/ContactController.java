package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.model.Contact;
import com.wts.interceptor.LoginInterceptor;
import org.apache.log4j.Logger;

import java.util.Date;

public class ContactController extends Controller {
    private static Logger logger = Logger.getLogger(Contact.class);

    public void index() {
        render("/contact.html");
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
                "  contact.*";
        String sqlExceptSelect = "FROM" +
                " contact" +
                " LEFT JOIN department ON department.id = contact.did" +
                " LEFT JOIN bureau ON bureau.id = department.bid" +
                " LEFT JOIN county ON county.id = bureau.cid" +
                " WHERE county.name LIKE '%" + getPara("keyword") + "%' " +
                " OR bureau.name LIKE '%" + getPara("keyword") + "%' " +
                " OR department.name LIKE '%" + getPara("keyword") + "%' " +
                " OR contact.phone LIKE '%" + getPara("keyword") + "%' " +
                " ORDER BY" +
                " bureau.id," +
                " department.id";
        renderJson(Db.paginate(
                getParaToInt("pageCurrent"),
                getParaToInt("pageSize"),
                select,
                sqlExceptSelect).getList());
    }

    @Before(LoginInterceptor.class)
    public void Total() {
        Long count = Db.queryLong("SELECT COUNT(*) FROM contact " +
                " LEFT JOIN department ON department.id = contact.did" +
                " LEFT JOIN bureau ON bureau.id = department.bid" +
                " LEFT JOIN county ON county.id = bureau.cid" +
                " WHERE county.name LIKE '%" + getPara("keyword") + "%' " +
                " OR bureau.name LIKE '%" + getPara("keyword") + "%' " +
                " OR department.name LIKE '%" + getPara("keyword") + "%' " +
                " OR contact.name LIKE '%" + getPara("keyword") + "%' " +
                " OR contact.phone LIKE '%" + getPara("keyword") + "%' ");
        renderText(count.toString());
    }

    @Before(LoginInterceptor.class)
    public void Get() {
        renderJson(Contact.dao.findById(getPara("id")));
    }

    @Before({Tx.class, LoginInterceptor.class})
    public void Add() {
        Contact contact = new Contact();
        contact.set("name", getPara("name"))
                .set("address", getPara("address"))
                .set("phone", getPara("phone"))
                .set("latitude", getPara("latitude"))
                .set("longitude", getPara("longitude"))
                .set("duty", getPara("duty"))
                .set("remark", getPara("remark"))
                .set("did", getPara("departmentId"))
                .save();
        logger.warn("function:" + this.getClass().getSimpleName() + "/Add;" + ";time:" + new Date() + ";");
        renderText("OK");
    }

    @Before({Tx.class,LoginInterceptor.class})
    public void Edit() {
        Contact contact = Contact.dao.findById(getPara("id"));
        if (contact == null) {
            renderText("要修改的信息不存在，请刷新页面后再试！");
        }else{
            contact.set("name", getPara("name"))
                    .set("phone", getPara("phone"))
                    .set("office", getPara("office"))
                    .set("address", getPara("address"))
                    .set("latitude", getPara("latitude"))
                    .set("longitude", getPara("longitude"))
                    .set("duty", getPara("duty"))
                    .set("remark", getPara("remark"))
                    .set("did", getPara("departmentId"))
                    .update();
            logger.warn("function:" + this.getClass().getSimpleName() + "/Edit;" + ";time:" + new Date() + ";");
            renderText("OK");
        }
    }
}
