package com.zby;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * @author zby
 * @title Generator
 * @date 2019年10月16日
 * @description
 */
public class Generator {

    public static final String MAPPER_TARGET = "mapper.target";
    public static final String MAPPER_PACKAGE = "mapper.package";
    public static final String SQL_TARGET = "sql.target";
    public static final String SQL_PACKAGE = "sql.package";
    public static final String AUTHOR_NAME = "author.name";

    public static void main(String[] args) throws Exception {
        // MBG 执行过程中的警告信息
        List<String> warnings = new ArrayList<String>();
        // 当 生成的代码重复时，覆盖原代码
        boolean overwrite = true;
        // 读取 MBG 配直文件
        File file = new File("src/main/resources/generatorConfig.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(file);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        // 创建 MBG
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        // 执行生成代码
        myBatisGenerator.generate(null);
        for (String warning : warnings) {
            System.out.println(warning);
        }

        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/generator.properties"));
        replaceDot(properties);
        generateExtXml(properties);
        generateExtMapper(properties);
    }

    public static void replaceDot(Properties properties) throws Exception {
        File sqlDirectory = new File(
                properties.getProperty(SQL_TARGET) + "/" + properties.getProperty(SQL_PACKAGE).replace(".", "/"));
        for (File file : sqlDirectory.listFiles()) {
            String absolutePath = file.getAbsolutePath();
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            bufferedReader.lines().forEach(s -> sb.append(s).append("\n"));
            bufferedReader.close();
            file.delete();
            BufferedWriter bufferedWriter =
                    new BufferedWriter(new OutputStreamWriter(new FileOutputStream(absolutePath)));
            bufferedWriter.write(sb.toString().replaceAll("\\.\\.", "\\."));
            bufferedWriter.close();
        }
    }

    public static void generateExtXml(Properties properties) throws Exception {
        File sqlDirectory = new File(
                properties.getProperty(SQL_TARGET) + "/" + properties.getProperty(SQL_PACKAGE).replace(".", "/"));
        for (File file : sqlDirectory.listFiles()) {
            String absolutePath = file.getAbsolutePath();
            BufferedWriter extWriter = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(absolutePath.replace("Mapper", "MapperExt"))));
            extWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    + "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n"
                    + "<mapper namespace=\"" + properties.getProperty(MAPPER_PACKAGE) + "."
                    + file.getName().substring(0, file.getName().length() - 4) + "Ext" + "\" >\n\n" + "</mapper>");
            extWriter.close();
        }
    }

    public static void generateExtMapper(Properties properties) throws Exception {
        File mapperDirectory = new File(
                properties.getProperty(MAPPER_TARGET) + "/" + properties.getProperty(MAPPER_PACKAGE).replace(".", "/"));
        for (File file : mapperDirectory.listFiles()) {
            String mapperName = file.getName().substring(0, file.getName().length() - 5);
            String extName = file.getAbsolutePath().replace("Mapper", "MapperExt");
            BufferedWriter extWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(extName)));
            extWriter.write("package " + properties.getProperty(MAPPER_PACKAGE) + ";\n\n");
            extWriter.write("/**\n");
            extWriter.write(" * " + mapperName + "拓展\n");
            extWriter.write(" *\n");
            extWriter.write(" * @author " + properties.get(AUTHOR_NAME) + "\n");
            extWriter.write(" * @date " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "\n");
            extWriter.write(" *\n");
            extWriter.write(" */\n");
            extWriter.write("public interface " + mapperName + "Ext extends " + mapperName + "{\n\n}");
            extWriter.close();
            System.out.println(extName);
        }

    }

}
