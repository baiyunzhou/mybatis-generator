package com.zby.comment;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.PropertyRegistry;

public class MyCommentGenerator extends EmptyCommentGenerator {
    private Properties properties;


    private boolean suppressAllComments;

    private boolean addRemarkComments;

    public MyCommentGenerator() {
        super();
        properties = new Properties();
        suppressAllComments = false;
        addRemarkComments = false;
    }

    @Override
    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);

        suppressAllComments = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));

        addRemarkComments = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_ADD_REMARK_COMMENTS));

    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (suppressAllComments || !addRemarkComments) {
            return;
        }
        String author = properties.getProperty("author");
        String dateFormat = properties.getProperty("dateFormat", "yyyy-MM-dd");
        SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);

        String remarks = introspectedTable.getRemarks();

        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * " + remarks);
        topLevelClass.addJavaDocLine(" *");
        topLevelClass.addJavaDocLine(" * @author " + author);
        topLevelClass.addJavaDocLine(" * @date " + dateFormatter.format(new Date()));
        topLevelClass.addJavaDocLine(" */");
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable,
            IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }
        String remarks = introspectedColumn.getRemarks();
        field.addJavaDocLine("/**");
        field.addJavaDocLine(" * " + remarks);
        field.addJavaDocLine(" */");
    }

    @Override
    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }
        if (METHOD_JAVADOC_MAP.get(method.getName()) != null) {
            method.addJavaDocLine("/**");
            method.addJavaDocLine(" * " + METHOD_JAVADOC_MAP.get(method.getName()) + "【请勿手动修改】");
            method.addJavaDocLine(" */");
        }

    }

    private static final Map<String, String> METHOD_JAVADOC_MAP = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put("deleteByPrimaryKey", "根据主键删除数据");
            put("insert", "保存数据");
            put("selectByPrimaryKey", "根据主键查询数据");
            put("selectAll", "查询所有数据");
            put("updateByPrimaryKey", "根据主键更新数据");
        }
    };

}
