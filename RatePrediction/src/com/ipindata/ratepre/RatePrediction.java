package com.ipindata.winratepre;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;

public class WinRatePrediction {

	// model parameter
	static Double alpha = 0.1;  // learning rate
	static Double beta = 1.0; 	// the parameters of self-learning rate
	static Double L1 = 0.1;   //
	static Double L2 = 0.1;   // L2_lamada

	static int epoch = 1;

	// features, discretization
	static Integer D = 1000000;
	static Double N[] = new Double[D];
	static Double Z[] = new Double[D];
	static Double W[] = new Double[D];  //weight

	static {
		for (int i = 0; i < D; i++) {
			N[i] = 0.0;
			Z[i] = 0.0;
			W[i] = 0.0;  // initial weight
		}
	}

//	model = Model.load(modelFile);
//
//	Feature[] instance = { new FeatureNode(1, 4), new FeatureNode(2, 2) };
//	double prediction = Linear.predict(model, instance);

	public static void main(String[] args) {
		String trainPath = args[0]; // "c:/Users/Andy/Desktop/train.normalized.fea.txt"; // the pathway of the training file
		String testPath = args[1]; //"c:/Users/Andy/Desktop/test.normalized.fea.txt";			 // the pathway of testing file
		String submissionPath = args[2]; //"c:/Users/Andy/Desktop/submission.txt";	 // the pathway of result file


		System.out.println("Start Training");
		train(trainPath);
		System.out.println("Training finished, start to predict:");

		long startTime = System.currentTimeMillis();
		test(testPath,submissionPath);
		long endTime = System.currentTimeMillis();
		long trainTime = endTime - startTime;
		long hours = (trainTime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (trainTime % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (trainTime % (1000 * 60)) / 1000;
		System.out.println(trainTime +"ms\t"+ hours+ " hour " + minutes + " munites "+ seconds + " seconds ");
		System.out.println("Prediction finished");
	}

	public static void train(String trainPath) {
//		String trainPath = "c:/Users/Andy/Desktop/train.normalized.fea.txt"; // the pathway of the training file
		BufferedReader br;
		String string = null;

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(trainPath)));
			string = br.readLine();
			String name[] = string.split("\t\t");

			String value[] = null;
			HashSet<Integer> set = new HashSet<Integer>();
			for (int c = 0; c < epoch; c++) {
				while ((string = br.readLine()) != null) {
					value = string.split("\t\t");
//					System.out.println(value.length);
					//for (int i = 2; i < value.length; i++) {  // when the format is "label click id...."
					for (int i =1 ; i < value.length; i++){   // when the format is "click id ..."
						Integer hashValue = Math.abs((name[i] + "_" + value[i]).hashCode()) % D;
						set.add(hashValue);
					}
					Double p = 0.0;
					for (Integer i : set) {
						int sign = Z[i] < 0 ? -1 : 1;
						if (Math.abs(Z[i]) <= L1) {
							W[i] = 0.0; // initial weight
						} else {
							W[i] = (sign * L1 - Z[i])/ ((beta + Math.sqrt(N[i])) / alpha + L2);
						}
						p += W[i];
					}

					// predict
					p = 1 / (1 + Math.exp(-p));
//					System.out.println(p);

					// update
					Double g = p - Double.parseDouble(value[0]);
					for (Integer i : set) {
						Double sigma = (Math.sqrt(N[i] + g * g) - Math.sqrt(N[i])) / alpha;
						Z[i] += g - sigma * W[i];
						N[i] += g * g;
					}
					set.clear();
				}
				br = new BufferedReader(new InputStreamReader(new FileInputStream(trainPath), "UTF-8"));
				br.readLine();
			}
			br.close();

		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	@SuppressWarnings("unused")
	public static void test(String testPath, String submissionPath) {
//		String testPath = "c:/Users/Andy/Desktop/test.normalized.fea.txt";			 // the pathway of testing file
//		String submissionPath = "c:/Users/Andy/Desktop/submission.txt";	 // the pathway of result file
		BufferedReader br;
		BufferedOutputStream bos;
		String string = null;
		byte[] newLine = "\r\n".getBytes();
		int count = 0;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(submissionPath));
			bos.write(("auctionid\t\tWinRate").getBytes());
			//bos.write(newLine);

			br = new BufferedReader(new InputStreamReader(new FileInputStream(testPath))); // in other : "UTF-8"
			string = br.readLine();

			String name[] = string.split("\t\t");
			String value[] = null;


			//System.out.println(string + "\n" + string.subSequence((name[0]+"\t").length(), string.length()));
			//System.out.println(name.length);
//			bos.write(("\t" + string.subSequence((name[0]+"\t").length(), string.length())).getBytes());
			//bos.write((string.subSequence((name[0]+"\t").length(), string.length()).toString()).getBytes()); // one length for "\t"
			bos.write(newLine);
			HashSet<Integer> set = new HashSet<Integer>();

			while ((string = br.readLine()) != null)
			{
				count++;
				value = string.split("\t\t");
				//for (int i = 1; i < value.length; i++)  // click column
				for(int i = 1; i< value.length; i++)
				{
					Integer hashValue = Math.abs((name[i] + "_" + value[i]).hashCode()) % D;
					set.add(hashValue);
				}

				Double p = 0.0;
				for (Integer i : set) {
					p += W[i];
				}
//				System.out.println(p);
				// predict
				p = 1 / (1 + Math.exp(-p));
				String result = value[3] + "\t\t" + p + "\t\t"+ value[0];
//				String result = p.toString() ;
				bos.write(result.getBytes());
				//bos.write((string.subSequence((value[0]+"\t").length(), string.length()).toString()).getBytes());//additional of mine
//				bos.write(("\t" + string.subSequence((value[0]+"\t").length(), string.length())).getBytes());//additional of mine
				bos.write(newLine);
				set.clear();
			}
			bos.flush();
			bos.close();
//			System.out.println(count);

		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
