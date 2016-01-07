package com.ipindata.lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LibSecendStep {

	public static void main(String[] args) throws IOException{

		String fileInput = "input.txt";  //trainResp.split.txt
		String fileOutput = "dictput.txt"; ; //trainResp.log.txt

		LibSecendStep.dataFormat(fileInput, fileOutput);
	}

	private static void dataFormat(String fileInput, String fileOutput) {
		// TODO Auto-generated method stub
		FileInputStream inputfstream;
		BufferedReader bufferedReader;
		FileOutputStream fstream;
		BufferedWriter bufferedWriter;
		String line = null;

		try{
			inputfstream = new FileInputStream(fileInput);
			bufferedReader = new BufferedReader(new InputStreamReader(inputfstream));
			String readLine = null;

			fstream = new FileOutputStream(fileOutput);
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(fstream));

			Integer[] dict = new Integer[32];

			while((readLine = bufferedReader.readLine()) != null){
				line = readLine;
				String[] libstr = line.split("\t\t");
				for (int i =1; i < libstr.length; i++){
					dict[Integer.valueOf(libstr[i])] += 1;
				}
				System.out.println(dict);


			}
			bufferedWriter.close();
			bufferedReader.close();
		}catch(IOException ex){
			ex.getStackTrace();
		}
	}
}
