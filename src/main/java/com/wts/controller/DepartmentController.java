package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.model.Contact;
import com.wts.entity.model.Department;
import com.wts.interceptor.LoginInterceptor;
import org.apache.log4j.Logger;

import java.util.Date;

public class DepartmentController extends Controller {
    private static Logger logger = Logger.getLogger(Department.class);
    public void index() {
        render("/department.html");
    }

    @Before(LoginInterceptor.class)
    public void Query() {
        String select = "SELECT" +
                "  county.name AS countyName," +
                "  bureau.name AS bureauName," +
                "  department.*";
        String sqlExceptSelect = "FROM" +
                " department" +
                " LEFT JOIN bureau ON bureau.id = department.bid" +
                " LEFT JOIN county ON county.id = bureau.cid" +
                " WHERE county.name LIKE '%" + getPara("keyword") + "%' " +
                " OR bureau.name LIKE '%" + getPara("keyword") + "%' " +
                " OR department.name LIKE '%" + getPara("keyword") + "%' " +
                " ORDER BY" +
                " department.id";
        renderJson(Db.paginate(
                getParaToInt("pageCurrent"),
                getParaToInt("pageSize"),
                select,
                sqlExceptSelect).getList());
    }

    @Before(LoginInterceptor.class)
    public void Total() {
        Long count = Db.queryLong("SELECT COUNT(*) FROM department " +
                " LEFT JOIN bureau ON bureau.id = department.bid" +
                " LEFT JOIN county ON county.id = bureau.cid" +
                " WHERE county.name LIKE '%" + getPara("keyword") + "%' " +
                " OR bureau.name LIKE '%" + getPara("keyword") + "%' " +
                " OR department.name LIKE '%" + getPara("keyword") + "%' ");
        renderText(count.toString());
    }

    @Before(LoginInterceptor.class)
    public void Get() {
        renderJson(Department.dao.findById(getPara("id")));
    }

    @Before({Tx.class, LoginInterceptor.class})
    public void Del() {
        Department d = Department.dao.findById(getPara("id"));
        renderText("OK");
    }

    @Before({Tx.class, LoginInterceptor.class})
    public void Add() {
        Department department = new Department();
        department.set("name", getPara("name"))
                .set("address", getPara("address"))
                .set("img", getPara("img"))
                .set("latitude", getPara("latitude"))
                .set("longitude", getPara("longitude"))
                .set("duty", getPara("duty"))
                .set("remark", getPara("remark"))
                .set("bid", getPara("bureauId"))
                .save();
        logger.warn("function:" + this.getClass().getSimpleName() + "/Add;" + ";time:" + new Date() + ";");
        renderText("OK");
    }

    @Before({Tx.class, LoginInterceptor.class})
    public void Edit() {
        Department department = Department.dao.findById(getPara("id"));
        if (department == null) {
            renderText("要修改的信息不存在！");
        } else {
            department.set("name", getPara("name"))
                    .set("address", getPara("address"))
                    .set("img", getPara("img"))
                    .set("latitude", getPara("latitude"))
                    .set("longitude", getPara("longitude"))
                    .set("duty", getPara("duty"))
                    .set("remark", getPara("remark"))
                    .set("bid", getPara("bureauId"))
                    .update();
            logger.warn("function:" + this.getClass().getSimpleName() + "/Edit;" + ";time:" + new Date() + ";");
            renderText("OK");
        }
    }

}
