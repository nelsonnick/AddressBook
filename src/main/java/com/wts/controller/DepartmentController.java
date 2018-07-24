package com.wts.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.wts.entity.model.Contact;
import com.wts.entity.model.Department;
import com.wts.interceptor.LoginInterceptor;
import org.apache.log4j.Logger;

public class DepartmentController extends Controller {
    private static Logger logger = Logger.getLogger(Department.class);
    public void index() {
        render("/main.html");
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
                "  contact.id AS contactId," +
                "  contact.name AS contactName," +
                "  contact.phone AS contactPhone," +
                "  contact.office AS contactOffice ";
        String sqlExceptSelect = "FROM" +
                " county" +
                " LEFT JOIN bureau ON county.id = bureau.cid" +
                " LEFT JOIN department ON bureau.id = department.bid" +
                " LEFT JOIN contact ON department.id = contact.did" +
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
        Long count = Db.queryLong("SELECT COUNT(*) FROM county " +
                " LEFT JOIN bureau ON county.id = bureau.cid" +
                " LEFT JOIN department ON bureau.id = department.bid" +
                " LEFT JOIN contact ON department.id = contact.did" +
                " WHERE county.name LIKE '%" + getPara("keyword") + "%' " +
                " OR bureau.name LIKE '%" + getPara("keyword") + "%' " +
                " OR department.name LIKE '%" + getPara("keyword") + "%' " +
                " OR contact.phone LIKE '%" + getPara("keyword") + "%' ");
        renderText(count.toString());
    }

    @Before(LoginInterceptor.class)
    public void GetContact() {
        String sql = "SELECT" +
                "  county.id AS countyId," +
                " county.name AS countyName," +
                " bureau.id AS bureauId," +
                " bureau.name AS bureauName," +
                " bureau.duty AS bureauDuty," +
                " bureau.remark AS dbureauRemark," +
                " department.id AS departmentId," +
                " department.name AS departmentName," +
                " department.address AS departmentAddress," +
                " department.img AS departmentImg," +
                " department.latitude AS departmentLatitude," +
                " department.longitude AS departmentLongitude," +
                " department.duty AS departmentDuty," +
                " department.remark AS departmentRemark," +
                " contact.id AS contactId," +
                " contact.name AS contactName," +
                " contact.phone AS contactPhone," +
                " contact.office AS contactOffice" +
                " FROM" +
                " county" +
                " LEFT JOIN bureau ON county.id = bureau.cid" +
                " LEFT JOIN department ON bureau.id = department.bid" +
                " LEFT JOIN contact ON department.id = contact.did" +
                " WHERE contact.id = " + getPara("id") +
                " ORDER BY" +
                " bureau.id," +
                " department.id";
        renderJson(Db.findFirst(sql));
    }

    @Before({Tx.class,LoginInterceptor.class})
    public void EditContact() {
        Contact contact = Contact.dao.findById(getPara("id"));
        if (contact == null) {
            renderText("要修改的信息不存在，请刷新页面后再试！");
        }else{
            contact.set("name", getPara("contactName"))
                    .set("phone", getPara("contactPhone"))
                    .set("office", getPara("contactOffice"))
                    .update();
            renderText("OK");
        }
    }
}
