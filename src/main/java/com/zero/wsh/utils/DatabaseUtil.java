package com.zero.wsh.utils;

import com.zero.wsh.enums.TableEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseUtil {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);
    private String driver;
    private String url;
    private String username;
    private String password;
    private DatabaseMetaData dbMetaData = null;
    private Connection conn = null;

    public DatabaseUtil() {
        this.driver = "com.mysql.cj.jdbc.Driver";
        this.url = "jdbc:mysql://192.168.200.118:3306?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8&nullNamePatternMatchesAll=true";
        this.username = "root";
        this.password = "123456";
        this.getDatabaseMetaData();
    }

    public DatabaseUtil(String driver, String url, String username, String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.getDatabaseMetaData();
    }

    private void getDatabaseMetaData() {
        try {
            if (null == dbMetaData || null == conn) {
                Class.forName(this.driver);
                conn = DriverManager.getConnection(this.url, this.username, this.password);
                dbMetaData = conn.getMetaData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭数据库连接
     *
     * @param conn
     */
    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("close connection failure", e);
            }
        }
    }

    /**
     * 获取表中所有字段名称
     *
     * @param tableName 表名
     * @return List
     */
    public List<Map<String, String>> getColumnNames(String tableName) {
        List<Map<String, String>> columnNames = new ArrayList<>();
        String tableSql = "select column_name , data_type, column_comment, column_key, extra from information_schema.columns" +
                " where table_name = ? and table_schema = (select database()) order by ordinal_position";
        try (PreparedStatement pStemt = conn.prepareStatement(tableSql)) {
            pStemt.setString(1, tableName);
            ResultSet resultSet = pStemt.executeQuery();
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            while (resultSet.next()) {
                Map<String, String> map = new HashMap<>();
                for (int i = 1; i <= size; i++) {
                    map.put(rsmd.getColumnName(i), resultSet.getString(i));
                }
                columnNames.add(map);
            }
        } catch (SQLException e) {
            logger.error("getColumnNames failure", e);
        } finally {
            this.closeConnection(conn);
        }
        return columnNames;
    }

    /**
     * 获取所有数据库
     *
     * @return List
     */
    public List<String> getCatalogs() {
        List<String> list = new ArrayList<>();
        try {
            ResultSet rs = dbMetaData.getCatalogs();
            while (rs.next()) {
                list.add(rs.getString("TABLE_CAT"));
            }
        } catch (SQLException e) {
            logger.error("获取所有数据库", e);
        } finally {
            this.closeConnection(conn);
        }
        return list;
    }

    /**
     * 获取所有表
     *
     * @param catalog 数据库
     * @return List
     */
    public List<String> getTableInfoByCatalog(String catalog) {
        List<String> list = new ArrayList<>();
        try {
            ResultSet rs = dbMetaData.getTables(catalog, null, null, new String[]{"TABLE"});
            while (rs.next()) {
                list.add(rs.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            logger.error("获取所有表", e);
        } finally {
            this.closeConnection(conn);
        }
        return list;
    }

    /**
     * 获取列信息
     *
     * @param catalog   数据库
     * @param tableName 表
     * @return List
     */
    public List<TableInfo> getTableColumns(String catalog, String tableName) {
        List<TableInfo> list = new ArrayList<>();
        try {
            ResultSet rs = dbMetaData.getColumns(catalog, null, tableName, "%");
            List<String> tablePrimaryKeys = this.getTablePrimaryKeys(catalog, tableName);
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                String isPrimaryKey = TableEnums.NO.getKey();
                if (tablePrimaryKeys.contains(columnName)) {
                    isPrimaryKey = TableEnums.YES.getKey();
                }
                TableInfo build = TableInfo.builder()
                        .columnName(columnName)
                        .typeName(rs.getString("TYPE_NAME"))
                        .columnSize(rs.getInt("COLUMN_SIZE"))
                        .decimalDigits(rs.getInt("DECIMAL_DIGITS"))
                        .isNullAble(rs.getString("IS_NULLABLE"))
                        .remarks(rs.getString("REMARKS"))
                        .columnDef(rs.getString("COLUMN_DEF"))
                        .isAutoincrement(rs.getString("IS_AUTOINCREMENT"))
                        .isPrimaryKey(isPrimaryKey)
                        .build();
                list.add(build);
            }
        } catch (SQLException e) {
            logger.error("获取列信息", e);
        } finally {
            this.closeConnection(conn);
        }
        return list;
    }

    /**
     * 获得表的主键信息
     *
     * @param catalog   数据库
     * @param tableName 表
     * @return
     */
    public List<String> getTablePrimaryKeys(String catalog, String tableName) {
        List<String> list = new ArrayList<>();
        try {
            ResultSet rs = dbMetaData.getPrimaryKeys(catalog, null, tableName);
            while (rs.next()) {
                list.add(rs.getString("COLUMN_NAME"));
            }
        } catch (SQLException e) {
            logger.error("获得表的主键信息", e);
        } finally {
            this.closeConnection(conn);
        }
        return list;
    }

    public static void main(String[] args) {
        DatabaseUtil databaseUtil = new DatabaseUtil();
        List<String> catalogs = databaseUtil.getCatalogs();

//        System.out.println(databaseUtil.getTableInfoByCatalog("zero"));
        System.out.println(databaseUtil.getTableColumns("zero", "t_table_info"));
    }
}
