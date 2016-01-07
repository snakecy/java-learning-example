package com.testprocon.example;

import java.util.Date;
import java.util.Properties;
//import java.util.Random;


import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class ProducerDemo {
	public static void main(String[] args) {
//		long events = Long.parseLong(args[0]);
		//Random rnd = new Random();
		int events = 6;
		
		// ������������
		Properties props = new Properties();
		props.put("metadata.broker.list","gmb-kafka.cloudapp.net:9092,gmb-kafka.cloudapp.net:9093");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		// key.serializer.classĬ��Ϊserializer.class
		props.put("key.serializer.class", "kafka.serializer.StringEncoder");
		// ��ѡ���ã���������ã���ʹ��Ĭ�ϵ�partitioner
		props.put("partitioner.class", "com.testprocon.example.PartitionerDemo");
		// ����acknowledgement���ƣ�������fire and forget�����ܻ��������ݶ�ʧ
		// ֵΪ0,1,-1,���Բο�
		props.put("request.required.acks", "1");
		ProducerConfig config = new ProducerConfig(props);

		// ����producer
		Producer<String, String> producer = new Producer<String, String>(config);
		// ������������Ϣ
		long start=System.currentTimeMillis();
		for (long i = 0; i < events; i++) {
			long runtime = new Date().getTime();
			String ip = "192.168.2." + i;//rnd.nextInt(255);
			String msg = runtime + ",www.example.com," + ip;
			//���topic�����ڣ�����Զ�������Ĭ��replication-factorΪ1��partitionsΪ0
			KeyedMessage<String, String> data = new KeyedMessage<String, String>("rtb_log.win_notice", ip, msg);
			producer.send(data);
		}
		System.out.println("��ʱ:" + (System.currentTimeMillis() - start));
		// �ر�producer
		producer.close();
	}
}