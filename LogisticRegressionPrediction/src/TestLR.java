import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

class TestSample {
	int pv;
	int click;
	int categorical_list[];
	public TestSample(int []categorical_list, int pv, int click) {
		super();
		this.categorical_list = categorical_list;
		this.pv = pv;
		this.click = click;
	}
}
public class TestLR {
	static int iteration_times = 500;

	//current time and last time loss gap 
	static float stop_loss_gap_threshold = 0.01f;
	static float learn_rate = 0.01f;
	static float L2_lambda = 0.001f;
	static int cat_size = 32;

	static List<TestSample> TestSampleList = new ArrayList<TestSample>();
	static Map<String,Integer> categoricalMap = new HashMap<String,Integer>();
	static List<Float> categoricalParList = new ArrayList<Float>();

	static float weight_upper = 0.1f;
	static float weight_lower = -0.1f;
	static Random random = new Random();
	public static void main(String[] args) throws IOException {
		String fileName = "c:/Users/Andy/Desktop/train.formalized.txt";

		System.out.println(System.currentTimeMillis());
		// output the system starting time
		long startTime = System.currentTimeMillis();
		readData(fileName);
		System.out.println(categoricalMap.size());
		init_parameter();
		linear_normalization();
		discretization();
		sgd();
		Test();
		save_model();

		// Caculate the running time in the whole life
		long endTime = System.currentTimeMillis();
		System.out.println("runtime :"+(endTime-startTime)+"ms");
	}

	private static void init_parameter() {
		for(int i = 0;i < categoricalMap.size();i++){
			categoricalParList.add(0f);
		}
		for(int i = 0;i < categoricalParList.size();i++){
			categoricalParList.set(i,getRandomValue(weight_lower,weight_upper));
		}

	}

	private static Float getRandomValue(float weight_lower_value, float weight_upper_value) {
		return random.nextInt(10000)/10000.0f*(weight_upper_value-weight_lower_value)+weight_lower_value;
	}

	private static void readData(String fileName) throws IOException {
		System.out.println("read data");
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
		String line = null;
		int index = 0;
		String feature_temp = null;
		int cat_index = 0;
		while((line = reader.readLine()) != null){
			String [] arr = line.split("\t\t");
			if(index == 0) {index++;continue;}
			int [] categorical_list = new int[cat_size];
			int click = Integer.parseInt(arr[0]);
			for(int i = 1; i < arr.length; i++){
				feature_temp = arr[i];
				if(feature_temp.length() >= 1){
					if(categoricalMap.containsKey(feature_temp)){
						categorical_list[i-1] = categoricalMap.get(feature_temp);
					}else{
						categorical_list[i-1] = cat_index;
						categoricalMap.put(feature_temp, cat_index);
						cat_index++;
					}

				}else{
					//missing value
					categorical_list[i-1] = -1;
				}
			}
			TestSample TestSample = new TestSample(categorical_list,1,click);
			TestSampleList.add(TestSample);
			index++;
		}
		reader.close();
	}
	private static void linear_normalization() {

		@SuppressWarnings("unused")
		TestSample TestSample = null;
		for(int j = 0;j < TestSampleList.size();j++){
			TestSample = TestSampleList.get(j);
		}
	}
	private static void sgd() {

		Collections.shuffle(TestSampleList);

		float prev_loss_sum = Float.MIN_VALUE;
		float current_loss_average = 0.0f;
		float output_value = 0;
		TestSample TestSample = null;
		float gradient = 0.0f; 
		float temp_weight = 0.0f;
		int temp_cat_index = 0;
		for (int i = 0; i < iteration_times; i++){
			for(int j = 0;j < TestSampleList.size();j++){
				output_value = activation_value(j);
				TestSample = TestSampleList.get(j);
				gradient = -learn_rate*(TestSample.click - TestSample.pv*output_value);
				for(int k = 0;k < cat_size; k++){
					temp_cat_index = TestSample.categorical_list[k];
					if(temp_cat_index >= 0){
						temp_weight = categoricalParList.get(temp_cat_index);
						categoricalParList.set(temp_cat_index,temp_weight-gradient-L2_lambda*temp_weight);
					}
				}
			}
			current_loss_average = do_calculate_loss_average();
			System.out.println("iterate "+i+" average loss:"+current_loss_average);
			if(check_stop_threshold(prev_loss_sum,current_loss_average)) return;
			prev_loss_sum = current_loss_average;
		}
	}

	private static boolean check_stop_threshold(float prev_average_loss,float current_average_loss) {
		if(Math.abs(current_average_loss-prev_average_loss)/Math.abs(current_average_loss) < stop_loss_gap_threshold ){
			return true;
		}
		return false;
	}
	private static float do_calculate_loss_average() {
		float output_value = 0;
		TestSample TestSample = null;
		float loss_sum = 0.0f;
		for(int j = 0;j < TestSampleList.size();j++){
			output_value = activation_value(j);
			TestSample = TestSampleList.get(j);
			loss_sum += TestSample.click*Math.log(output_value)+(TestSample.pv-TestSample.click)*Math.log(1-output_value);
		}
		return loss_sum/TestSampleList.size();
	}
	private static float activation_value(int TestSample_index) {
		float value = 0.0f;
		int cat_index = 0;
		for(int i = 0; i < cat_size;i++){
			cat_index = TestSampleList.get(TestSample_index).categorical_list[i];
			if(cat_index >= 0){
				try{
					value += categoricalParList.get(cat_index);
				}catch(Exception e){
					System.out.println(cat_index);
				}
			}
		}
		return activation(value);
	}
	private static float activation(float value) {
		if(value > 10){
			return 1.0f;
		}else if(value < -10){
			return 0f;
		}else{
			return (float) (1.0f/(1+Math.exp(-value)));
		}
	}

	private static void discretization() {
		// TODO Auto-generated method stub
		

	}

	private static void Test() {
		// TODO Auto-generated method stub

		
	}

	private static void save_model() {

		
	}
}
