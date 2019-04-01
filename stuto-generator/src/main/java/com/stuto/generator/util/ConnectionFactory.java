package com.stuto.generator.util;

import static com.stuto.generator.message.Messages.getString;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import com.alibaba.fastjson.JSONObject;
import com.stuto.generator.config.GeneratorContext;



/**
* 类说明
* @author 作者 : zyq
* 创建时间：2016年12月13日 下午8:56:45
* @version
*/
public class ConnectionFactory {

    private JSONObject dbCfg =GeneratorContext.cfgJson.getJSONObject("db");
    private String dbType = dbCfg.getString("dbType");
    private JSONObject connectionCfg = dbCfg.getJSONObject(dbType).getJSONObject("connection");

    private static ConnectionFactory instance = new ConnectionFactory();

    public static ConnectionFactory getInstance() {
        return instance;
    }

    /**
	 *
	 */
    private ConnectionFactory() {
        super();
    }

    public Connection getConnection()
            throws SQLException {
        Driver driver = getDriver();

        Properties props = new Properties();
        props.setProperty("user", connectionCfg.getString("username"));
        props.setProperty("password", connectionCfg.getString("password"));
        Connection conn = driver.connect(connectionCfg.getString("url"), props);
        if (conn == null) {
            throw new SQLException(getString("RuntimeError.7")); //$NON-NLS-1$
        }
        return conn;
    }

    private Driver getDriver() {
        String driverClass = connectionCfg.getString("driverClass");
        Driver driver;
        try {
            Class<?> clazz = Class.forName(driverClass);
            driver = (Driver) clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(getString("RuntimeError.8"), e);
        }

        return driver;
    }

    public static void main(String[] args) throws SQLException {

		Connection cc=new ConnectionFactory().getConnection();
		System.out.println(cc);
	}
}
