package com.briup.server.impl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

//测试properties文件
@SuppressWarnings("all")
public class Test1 {

	public static void main(String[] args) throws Exception {

		// 创建空的properties对象
		Properties p = new Properties();
		// 加载db.properties文件中的K-V数据
		FileReader reader = new FileReader("src/main/java/db.properties");
		p.load(reader);

		System.out.println(p);

		Class.forName(p.getProperty("driver"));
		Connection conn = DriverManager.getConnection(p.getProperty("url"), p.getProperty("user"),
				p.getProperty("password"));
		System.out.println(conn);

	}
}
