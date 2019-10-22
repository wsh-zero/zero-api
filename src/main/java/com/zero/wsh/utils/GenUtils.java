package com.zero.wsh.utils;

import com.zero.wsh.components.entity.ColumnEntity;
import com.zero.wsh.components.entity.TableEntity;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 代码生成器
 */
public class GenUtils {
    /**
     * 需要生成的模板
     *
     * @return list<String>
     */
    private static List<String> getTemplates() {
        List<String> templates = new ArrayList<>();
        templates.add("template/Entity.java.vm");
        templates.add("template/Mapper.java.vm");
        templates.add("template/Mapper.xml.vm");
        templates.add("template/Service.java.vm");
        templates.add("template/ServiceImpl.java.vm");
        templates.add("template/Controller.java.vm");
        return templates;
    }

    /**
     * 封装模板中使用的数据
     */
    private static VelocityContext setVelocityContext(TableEntity tableEntity, Configuration config) {
        String mainPath = config.getString("mainPath");
        mainPath = StringUtils.isBlank(mainPath) ? "io.renren" : mainPath;
        //封装模板中使用的数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableEntity.getTableName());
        map.put("comments", tableEntity.getComments());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("classname", tableEntity.getClassname());
        map.put("pathName", tableEntity.getClassname().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("mainPath", mainPath);
        map.put("package", config.getString("package"));
        map.put("moduleName", config.getString("moduleName"));
        map.put("author", config.getString("author"));
        map.put("email", config.getString("email"));
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        return new VelocityContext(map);
    }

    /**
     * 生成代码
     */
    public static void generatorCode(List<Map<String, String>> columns) {
        //配置信息
        Configuration config = getConfig();
        //表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName("zero");
        tableEntity.setComments("");
        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), config.getStringArray("tablePrefix"));
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnEntity> columsList = new ArrayList<>();
        for (Map<String, String> column : columns) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.get("COLUMN_NAME"));
            columnEntity.setDataType(column.get("DATA_TYPE"));
            columnEntity.setComments(column.get("COLUMN_COMMENT"));
            columnEntity.setExtra(column.get("EXTRA"));

            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType(), "unknowType");
            columnEntity.setAttrType(attrType);
            //是否主键
            if ("PRI".equalsIgnoreCase(column.get("COLUMN_KEY")) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }

            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        VelocityContext context = setVelocityContext(tableEntity, config);
        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            String filepath = getCoverFileName(template, tableEntity.getClassname(), tableEntity.getClassName(), config.getString("package"), config.getString("moduleName"), config.getString("controller_permission_key"));
            if (StringUtils.isNotBlank(filepath)) {
                Template tpl = Velocity.getTemplate(template, "UTF-8");
                File file = new File(filepath);
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException("创建文件失败", e);
                    }
                }
                try (FileOutputStream outStream = new FileOutputStream(file);
                     OutputStreamWriter writer = new OutputStreamWriter(outStream, StandardCharsets.UTF_8);
                     BufferedWriter sw = new BufferedWriter(writer);) {
                    tpl.merge(context, sw);
                    sw.flush();
                    System.out.println("成功生成Java文件:" + filepath);
                } catch (IOException e) {
                    throw new RuntimeException("模板生成失败，表名：" + tableEntity.getTableName(), e);
                }
            }
        }
    }


    /**
     * 列名转换成Java属性名
     */
    private static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    private static String tableToJava(String tableName, String[] tablePrefixArray) {
        if (null != tablePrefixArray && tablePrefixArray.length > 0) {
            for (String tablePrefix : tablePrefixArray) {
                tableName = tableName.replace(tablePrefix, "");
            }
        }
        return columnToJava(tableName);
    }

    /**
     * 获取配置信息
     */
    private static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            throw new RuntimeException("获取配置文件失败，", e);
        }
    }

    private static String targetPath = System.getProperty("user.dir");

    /**
     * 获取覆盖路径
     */
    public static String getCoverFileName(String template, String classname, String className, String packageName, String moduleName, String controller) {
        String packagePath = targetPath + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
        String resourcesPath = targetPath + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator;
        }

        if (template.contains("Entity.java.vm")) {
            return packagePath + moduleName + File.separator + "auto" + File.separator + className + "Entity.java";
        }

        if (template.contains("Mapper.java.vm")) {
            return packagePath + "mapper" + File.separator + "auto" + File.separator + className + "Mapper.java";
        }
        if (template.contains("Mapper.xml.vm")) {
            return resourcesPath + "mybatis" + File.separator + "auto" + File.separator + className + "Mapper.xml";
        }

        if (template.contains("ServiceImpl.java.vm")) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }
        if (template.contains("Service.java.vm")) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }
        if (template.contains("Controller.java.vm")) {
            return packagePath + "controller" + File.separator + controller + File.separator + className + "Controller.java";
        }
        return null;
    }
}
