package com.wts.common;

import com.jfinal.config.*;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.tx.TxByMethods;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import com.wts.controller.*;
import com.wts.controller.MainController;
import com.wts.entity.model._MappingKit;

/**
 * API引导式配置
 */
public class Config extends JFinalConfig {

    /**
     * 配置常量
     */
    public void configConstant(Constants me) {
        // 加载少量必要配置，随后可用PropKit.get(...)获取值
        PropKit.use("a_little_config.txt");
        me.setDevMode(false);
    }

    /**
     * 配置路由
     */
    public void configRoute(Routes me) {
        me.add("/", MainController.class);
        me.add("/county", CountyController.class);
        me.add("/bureau", BureauController.class);
        me.add("/department", DepartmentController.class);
        me.add("/contact", ContactController.class);
        me.add("/wrong", WrongController.class);
    }

    /**
     * 配置插件
     */
    public void configPlugin(Plugins me) {
        // 配置C3p0数据库连接池插件
//        C3p0Plugin c3p0Plugin = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
//        c3p0Plugin.setDriverClass("com.mysql.cj.jdbc.Driver");
//        me.add(c3p0Plugin);
        DruidPlugin dp = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
        dp.setDriverClass("com.mysql.cj.jdbc.Driver");
        me.add(dp);

        // 配置ActiveRecord插件
//        ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
        arp.setShowSql(false);
        me.add(arp);
        _MappingKit.mapping(arp);

    }

    /**
     * 配置全局拦截器
     */
    public void configInterceptor(Interceptors me) {
        me.add(new TxByMethods("save","update"));
    }

    /**
     * 配置处理器
     */
    public void configHandler(Handlers me) {
        me.add(new ContextPathHandler("contextPath"));//设置上下文路径
    }

    /**
     * 配置模板引擎
     */
    public void configEngine(Engine me) {}
    /**
     * 建议使用 JFinal 手册推荐的方式启动项目
     * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
     */
    public static void main(String[] args) {
        JFinal.start("src/main/webapp", 80, "/");
    }
}
