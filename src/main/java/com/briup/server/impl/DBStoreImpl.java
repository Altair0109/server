package com.briup.server.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import com.briup.bean.Environment;
import com.briup.server.DBStore;
import com.briup.server.impl.ServerImpl;

@SuppressWarnings("all")
public class DBStoreImpl implements DBStore {

	@Override
	public void init(Properties properties) {

	}

	@Override
	public void saveToDB(Collection<Environment> paramCollection) throws Exception {

		Properties p = new Properties();
		FileReader reader = new FileReader("src/main/java/db.properties");
		p.load(reader);

//		Environment envir = new Environment();
		// 注册驱动
		Class.forName(p.getProperty("driver"));
		// 获得连接
		Connection conn = DriverManager.getConnection(p.getProperty("url"), p.getProperty("user"),
				p.getProperty("password"));
		PreparedStatement ps = null;
		ArrayList<Environment> list = (ArrayList<Environment>) paramCollection;
		// 上一个Environment对象的时间
		int oldDa = 0;
		for (int i = 0; i < list.size(); i++) {
			Environment en = list.get(i);
			Timestamp date = en.getGather_date();
			// 本次Environment对象的时间
			int nowDa = date.getDate();
			// 1、ps
			if (nowDa != oldDa) {// 当前的日期和上一个Environment日期不同才需要更新ps对象，上一次的ps对象需要先提交批次中的数据
				// 关闭上一次的ps
				// values('name','srcId','devId','sersorAddress',count,'cmd',status,data,'gather_date');
				if (ps != null) {
					// 第一次ps为null
					// 提交并关闭老的ps
					ps.executeBatch();
					ps.close();
				}
				String sql = "insert into E_DETAIL_" + nowDa + " values(?,?,?,?,?,?,?,?,?)";
				ps = conn.prepareStatement(sql);
				// 记录本次时间
				// 下一次循环时，新的时间变为老时间
				oldDa = nowDa;
			}
			// 2、给？设置值
			ps.setString(1, en.getName());
			ps.setString(2, en.getSrcId());
			ps.setString(3, en.getDevId());
			ps.setString(4, en.getSersorAddress());
			ps.setInt(5, en.getCount());
			ps.setString(6, en.getCmd());
			ps.setInt(7, en.getStatus());
			ps.setFloat(8, en.getData());
			ps.setTimestamp(9, en.getGather_date());

			ps.addBatch();

			if (i % 3000 == 0) {
				ps.executeBatch();
			}
		}
		ps.executeBatch();
		ps.close();

	}

//		// 创建会话
//		Statement stat = conn.createStatement();
//		for (Environment envir : paramCollection) {
//
//			String name = envir.getName();
//			String srcId = envir.getSrcId();
//			String devId = envir.getDevId();
//			String sersorAddress = envir.getSersorAddress();
//			int count = envir.getCount();
//			String cmd = envir.getCmd();
//			int status = envir.getStatus();
//			float data = envir.getData();
//			Timestamp gather_date = envir.getGather_date();
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(gather_date);
//			int day = calendar.get(Calendar.DAY_OF_MONTH);
//			Date date2 = new Date(gather_date.getTime());
//			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			String date3 = simpleDateFormat.format(date2);
//			// 执行sql语句
//			// insert into E_DETAIL_1
//			// values('name','srcId','devId','sersorAddress',count,'cmd',status,data,'gather_date');
//			String sql = "insert into E_DETAIL_" + day + " values(" + "'" + name + "'," + "'" + srcId + "'," + "'"
//					+ devId + "'," + "'" + sersorAddress + "'," + count + ",'" + cmd + "'," + status + "," + data + ","
//					+ "to_date(" + "'" + date3 + "'" + ",'yyyy-MM-dd HH24:mi:ss')" + ")";
//			// 处理结果集
//			stat.executeQuery(sql);
//		}
//		// 关闭资源
//		stat.close();
//	}

}
