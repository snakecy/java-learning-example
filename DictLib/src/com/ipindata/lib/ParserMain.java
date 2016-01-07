package com.ipindata.lib;

import java.io.IOException;

public class ParserMain {
	public static void main(String[] args) throws IOException{

		String fileInput = "input.txt";  //trainResp.split.txt
		String fileOutput = "output.txt"; ; //trainResp.log.txt

		LibFirstStep.dataFormat(fileInput, fileOutput);
	}

}
