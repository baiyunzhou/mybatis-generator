package com.zby;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
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
 * @description 【生成完成后需要手动把每个Mapper.xml文件中..替换为.;使用replaceAll；遗留问题待解决】
 */
public class Generator {

    public static final String sql_target = "sql.target";
    public static final String sql_package = "sql.package";

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
        replace(properties.get(sql_target).toString(), properties.get(sql_package).toString());
    }

    public static void replace(String targetPath, String packagePath) throws Exception {
        String fileLocation = targetPath + "/" + packagePath.replace(".", "/");
        File directory = new File(fileLocation);
        for (File file : directory.listFiles()) {
            String absolutePath = file.getAbsolutePath();
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            bufferedReader.lines().forEach(s -> sb.append(s).append("\r\n"));
            bufferedReader.close();
            BufferedWriter bufferedWriter =
                    new BufferedWriter(new OutputStreamWriter(new FileOutputStream(absolutePath)));
            bufferedWriter.write(sb.toString().replaceAll("\\.\\.", "\\."));
            bufferedWriter.close();
        }
        directory.delete();
    }
}
