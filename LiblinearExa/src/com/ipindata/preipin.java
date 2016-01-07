package com.ipindata.testdatamaking;

import de.bwaldvogel.liblinear.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
//import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class TestDataGen2 {

	public static void main(String[] args) throws IOException{

		//		String fileInputReq = args[0];
		//		float bid_price = Float.valueOf(args[1]);
		//		String model_para = args[2];
		//		String fileOutReq = args[3];

		String fileInputReq = "test.txt";
		String str = "0.150000000";
		float bid_price = Float.parseFloat(str);
		System.out.println(bid_price);
		String dict = "dict";
		String model_para = "train.model";
		String resultsOut = "results.txt";

		TestDataGen2 saj = new TestDataGen2();
		long startTime = System.currentTimeMillis();
		String ReqLine = saj.dataMergeReq(fileInputReq,bid_price);
//						System.out.println(ReqLine);
		String cutdic = saj.MakeDict(ReqLine);
//						System.out.println(cutdic);
//		String ReqLineFormat = saj.dictGet(cutdic,dict);
		String outputs = "result.txt";
		saj.dictGet(cutdic, dict, outputs);
//		System.out.println(ReqLineFormat);

		String[] arg = {"-b","1",outputs,model_para,resultsOut};
		Predict.main(arg);

		long endTime = System.currentTimeMillis();
		long trainTime = endTime - startTime;
		System.out.println(trainTime +"ms\n");
	}

	private void dictGet(String cutdic, String dict, String output) {

	}

	private  String MakeDict(String reqLine) {

		return content;
	}

	private String dataMergeReq(String fileInputReq,float bid_price) {


	}

	public static boolean isJSONValid(String string) {
		// TODO Auto-generated method stub
		try{
			JSONObject.fromObject(string);
			return true;
		}catch(JSONException ex){
			return false;
		}
	}

}
