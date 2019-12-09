package com.zby;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Cleaner {

    public static final String mapper_target = "mapper.target";
    public static final String mapper_package = "mapper.package";
    public static final String model_target = "model.target";
    public static final String model_package = "model.package";
    public static final String sql_target = "sql.target";
    public static final String sql_package = "sql.package";


    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/generator.properties"));
        deleteFile(properties.get(mapper_target).toString(), properties.get(mapper_package).toString());
        deleteFile(properties.get(model_target).toString(), properties.get(model_package).toString());
        deleteFile(properties.get(sql_target).toString(), properties.get(sql_package).toString());
    }

    public static void deleteFile(String targetPath, String packagePath) {
        String fileLocation = targetPath + "/" + packagePath.replace(".", "/");
        System.out.println(fileLocation);
        File directory = new File(fileLocation);
        if (directory.exists()) {
            for (File file : directory.listFiles()) {
                file.delete();
            }
            directory.delete();
        }
    }
}
