package com.booker.lsp;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

/**
 * @Author BookerLiu
 * @Date 2022/9/26 15:21
 * @Description 自动生成实体
 **/
public class MybatisPlusGenerator {

//    private static final String TABLE_NAMES = "a_menu,a_resource,a_role,a_role_menu,a_role_resource,a_user,a_user_role,f_custom_type,f_file_info,f_cut_file_info";
//    private static final String TABLE_NAMES = "f_chunk_file,f_chunk_file_info,f_custom_type,f_directory,f_file";
    private static final String TABLE_NAMES = "";

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        /**
         * 是否覆盖之前的文件 important
         */
        gc.setFileOverride(true);

        gc.setBaseColumnList(true);
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("BookerLiu");
        gc.setOpen(false);
        gc.setSwagger2(true); //实体属性 Swagger2 注解
        gc.setDateType(DateType.ONLY_DATE); //使用java.util.date
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/lsp_pan?characterEncoding=utf-8");
        dsc.setSchemaName("lsp_pan");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.booker.lsp");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        mpg.setCfg(cfg);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setEntityBuilderModel(true);
        strategy.setInclude(TABLE_NAMES.split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix("a_", "f_" , "_");//需要截取不用的，
        // 生成实体类字段注解
        strategy.setEntityTableFieldAnnotationEnable(true);
        mpg.setStrategy(strategy);

        TemplateConfig config = new TemplateConfig();
        config.setController("");
        config.setService("");
        config.setServiceImpl("");

        mpg.setTemplate(config);

        mpg.setTemplateEngine(new VelocityTemplateEngine());
        mpg.execute();
    }
}
