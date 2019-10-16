package com.briup.server.impl;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

import com.briup.bean.Environment;
import com.briup.server.Server;

@SuppressWarnings("all")
public class ServerImpl implements Server {

	@Override
	public void init(Properties properties) {

	}

	@Override
	public void revicer() throws Exception {
		ServerSocket server = new ServerSocket(8099);
		while (true) {
			// 客户端对象
			Socket socket = server.accept();
			InputStream in = socket.getInputStream();

			ObjectInputStream ois = new ObjectInputStream(in);
			Collection<Environment> paramCollection = (Collection<Environment>) ois.readObject();
			System.out.println(paramCollection.size());

			DBStoreImpl dbStoreImpl = new DBStoreImpl();
			dbStoreImpl.saveToDB(paramCollection);
			
			ois.close();
			in.close();
			socket.close();

		}
	}

	@Override
	public void shutdown() {

	}
}
