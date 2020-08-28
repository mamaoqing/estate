package com.estate.autocode;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

public class AutoCode {

    public static void main(String[] args) {

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        // 获取当前路径
        String projectPath = System.getProperty("user.dir");
        // 写入到目录
        gc.setOutputDir(projectPath + "/estate-autocode/src/main/java");
        // 设置作者
        gc.setAuthor("mq");
        // 是否打开资源管理器
        gc.setOpen(false);
        // 是否覆盖
        gc.setFileOverride(true);
        // 去掉service前面的i
        gc.setServiceName("%sService");
        // 设置时间格式
        gc.setDateType(DateType.ONLY_DATE);
        mpg.setGlobalConfig(gc);

        // 设置数据源
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDriverName("org.mariadb.jdbc.Driver");
        dsc.setUrl("jdbc:mysql://192.168.0.114:3306/wygl");
        dsc.setPassword("sdzy@123");
        dsc.setUsername("root");
        dsc.setDbType(DbType.MARIADB);

        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("asstes");
        pc.setParent("com.estate.sdzy");
        pc.setController("controller");
        pc.setService("service");
        pc.setMapper("mapper");
        pc.setEntity("entity");
        mpg.setPackageInfo(pc);

        // 策略，逻辑删除。。。
        StrategyConfig strategy = new StrategyConfig();
        // 要映射的表
//        strategy.setInclude("f_bill","f_bill_period","f_chargeoff_period","sale_info","pay_info");
//        strategy.setInclude("f_cost_item","f_cost_rule","f_cost_type","f_meter","f_meter_record");
        strategy.setInclude("f_cost_rule_room");
//        strategy.setInclude("r_unit","r_room","r_province","r_owner_property","r_owner_invoice_info","r_owner","r_district","r_community","r_comm_role_agreement","r_comm_area","r_city","r_building");
//        strategy.setInclude("s_user","s_role_menu","s_role","s_org","s_menu","s_dict_item","s_dict","s_company","s_comp_link","s_user_role","s_user_comm","s_bank");
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // 自动使用lombok
        strategy.setEntityLombokModel(true);

        // 逻辑删除
        strategy.setLogicDeleteFieldName("is_delete");
        // 自动填充
        TableFill createTime = new TableFill("created_at", FieldFill.INSERT);
        TableFill updateTime = new TableFill("modified_at", FieldFill.INSERT_UPDATE);
        List<TableFill> tableFills = new ArrayList<TableFill>(2);
        tableFills.add(createTime);
        tableFills.add(updateTime);
        strategy.setTableFillList(tableFills);

        mpg.setStrategy(strategy);

        mpg.execute();
    }
}
