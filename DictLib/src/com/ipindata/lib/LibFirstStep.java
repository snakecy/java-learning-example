package com.ipindata.lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LibFirstStep {

//	public static void main(String[] args) throws IOException{
//
//		String fileInput = "input.txt";  //trainResp.split.txt
//		String fileOutput = "output.txt"; ; //trainResp.log.txt
//
//		LibFirstStep.dataFormat(fileInput, fileOutput);
//	}

	public static void dataFormat(String fileInput, String fileOutput) {
		// TODO Auto-generated method stub
		FileInputStream inputfstream;
		BufferedReader bufferedReader;
		FileOutputStream fstream;
		BufferedWriter bufferedWriter;
		String line = null;

		Integer[] reserved_field = new Integer[32];
		String reserved_str = "0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31";
		String[] arraystr = reserved_str.split(",");
//		System.out.println(arraystr[1]);
		for (int i = 1; i< arraystr.length; i++){
			reserved_field[Integer.valueOf(arraystr[i])] = i;
		}
//		System.out.println(reserved_field[1]);

		try{
			inputfstream = new FileInputStream(fileInput);
			bufferedReader = new BufferedReader(new InputStreamReader(inputfstream));
			String readLine = null;

			fstream = new FileOutputStream(fileOutput);
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(fstream));

			while((readLine = bufferedReader.readLine()) != null){
				line = readLine;
				String[] libstr = line.split("\t\t");
				String content = libstr[0];
				for (int i = 1; i< libstr.length; i++){
					if (reserved_field[i] == i){
						if(i ==10 && libstr[i] != "null"){
							String[] col10 = libstr[i].split(",");
							for (int j = 0; j< col10.length; j++){
								content = content+"\t\t"+reserved_field[i]+"^A"+col10[j];
							}
						}else if(i ==17 && libstr[i] != "null"){
							String[] col17 = libstr[i].split(",");
							for (int j = 0; j< col17.length; j++){
								content = content+"\t\t"+reserved_field[i]+"^A"+col17[j];
							}
						}else if(i ==29 && libstr[i] != "null"){
							String[] col29 = libstr[i].split(",");
							for (int j = 0; j< col29.length; j++){
								content = content+"\t\t"+reserved_field[i]+"^A"+col29[j];
							}
						}else if(i ==30 && libstr[i] != "null"){
							String[] col30 = libstr[i].split(",");
							for (int j = 0; j< col30.length; j++){
								content = content+"\t\t"+reserved_field[i]+"^A"+col30[j];
							}
						}else{
							content = content+"\t\t"+reserved_field[i]+"^A"+libstr[i];
						}
					}
				}
				bufferedWriter.write(content+"\n");
				System.out.println(content);
			}
			bufferedWriter.close();
			bufferedReader.close();
		}catch(IOException ex){
			ex.getStackTrace();
		}
	}
}
