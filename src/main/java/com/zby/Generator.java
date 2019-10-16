package com.zby;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    }

}
