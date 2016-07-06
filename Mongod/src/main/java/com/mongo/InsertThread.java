package com.mongo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.str.StringFactory;

public class InsertThread implements Runnable {
	private MongoClient mc = null;

	Logger log = LogManager.getLogger(InsertThread.class);
	private String db = null;
	private String tab = null;
	private int len;
	private int buf;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss:SSS");
	public static AtomicInteger b = new AtomicInteger(0);

	public InsertThread(MongoClient mc, String db, String tab, int len, int buf) {
		super();
		this.mc = mc;
		this.db = db;
		this.tab = tab;
		this.len = len;
		this.buf = buf;
	}

	public void run() {
		MongoDatabase mdb = mc.getDatabase(db);
		MongoCollection<Document> coll = mdb.getCollection(tab);
		long num = 0;// ���ݸ���
		long size = 0;// ���ݴ�С
		String post = "���ݿ⣺" + db + "|���" + tab + "|��Ϣ���С��" + len + "|һ�η������ݸ�����" + buf;
		int post_len = post.length() - 1;
		StringBuffer msg = new StringBuffer(post);
		int init = 0;
		int append = 0;
		long da = 0;
		while (buf > 0) {
			num++;
			size += len;

			da = System.currentTimeMillis();
			msg.append("|���ڣ�");
			msg.append(sdf.format(new Date(da)));
			msg.append("|���룺");
			msg.append(da);
			msg.append("|��¼��");
			msg.append(num);
			msg.append("|��ǰ���ݴ�С��");
			msg.append(size);

			init = msg.length();
			append = len - init;

			String key = StringFactory.uniqueStr(append);
			String value = StringFactory.uniqueStr(append);
			String tem = msg.toString();
			Document doc = new Document(tem + key, tem + value);
			try {
				coll.insertOne(doc);
				buf--;
				b.addAndGet(-1);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(tem);
			}finally {
				
			}
			log.info(new String(tem));
			msg.delete(post_len, tem.length());
		}
//		coll=null;
//		mdb=null;
//		mc.close();
//		msg=null;
//		sdf=null;
	}

}
