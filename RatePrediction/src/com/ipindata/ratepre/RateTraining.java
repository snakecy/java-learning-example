package com.ipindata.winratepre;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;

public class WinRateTraining {

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


//
//	Problem problem = new Problem();
//	problem.l = ... // number of training examples
//	problem.n = ... // number of features
//	problem.x = ... // feature nodes
//	problem.y = ... // target values
//
//	SolverType solver = SolverType.L2R_LR; // -s 0
//	double C = 1.0;    // cost of constraints violation
//	double eps = 0.01; // stopping criteria
//
//	Parameter parameter = new Parameter(solver, C, eps);
//	Model model = Linear.train(problem, parameter);
//	File modelFile = new File("model");
//	model.save(modelFile);


	public static void main(String[] args) {
		String trainPath = args[0]; // the pathway of the training file

		System.out.println("Start Training...");
		train(trainPath);
		System.out.println("Training finished!");

	}

	public static void train(String trainPath) {
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
				br = new BufferedReader(new InputStreamReader(new FileInputStream(trainPath)));
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
}
