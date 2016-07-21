package theano;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomData {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
//			getRandom();
//			genRandomData(20);
			
			String target = "F:\\word2vec\\array_new_matrix_similarityMatrix_CNN\\"
					+ "Array_train_456_112_random.txt";
			String target_label = "F:\\word2vec\\array_new_matrix_similarityMatrix_CNN\\"
					+ "Array_label_456_112_random.txt";
			String inputLabelPath = "F:\\word2vec\\array_new_matrix_similarityMatrix_456×112_tile_one";
			String inputLabelPath_label = "F:\\word2vec\\array_new_matrix_similarityMatrix_Label_1550";
			writeFileRandom(target, target_label, inputLabelPath, inputLabelPath_label);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 在Text原有内容中写入新的内容，不覆盖原来内容，随机选择文件写
	public static void writeFileRandom(String target, String target_label, 
			String inputLabelPath, String inputLabelPath_label) throws Exception {
		File targetFile = new File(target);
		File targetFile_label = new File(target_label);
		if(!targetFile.exists() && !targetFile_label.exists()){
			targetFile.createNewFile();
			targetFile_label.createNewFile();
		}
		File[] labelfiles = new File(inputLabelPath).listFiles();
		// 将0-labelfiles.length-1 范围内的数字随机化    得到随机下标
		int[] index = RandomData.genRandomData(labelfiles.length);
		for(int i = 0; i < index.length; i++){
			int idx = index[i];
			StringBuffer strNew = new StringBuffer();
			StringBuffer strNew_label = new StringBuffer();
			String str = new String();
			String labelName = labelfiles[idx].getName();
			File sourceFile = new File(inputLabelPath + "\\" + labelName);
			File sourceFile_label = new File(inputLabelPath_label + "\\" + labelName);

			// 读取原来内容
			System.out.println("--------开始读文件----------");
			System.out.println("读取文件：" + labelName);
			BufferedReader input = new BufferedReader(new FileReader(sourceFile));// 读取数据
			while ((str = input.readLine()) != null) {
				strNew.append(str + "\n");
			}
			input.close();
			
			BufferedReader input_label = new BufferedReader(new FileReader(sourceFile_label));// 读取标签
			while ((str = input_label.readLine()) != null) {
				strNew_label.append(str + "\n");
			}
			input_label.close();
			System.out.println("--------读取完毕-----------");

			// 写文件
			System.out.println("--------开始写文件----------");
			
			FileWriter output = new FileWriter(targetFile, true);// 写数据
			output.write(strNew.toString());
			output.close();
			
			FileWriter output_label = new FileWriter(targetFile_label, true);// 写标签
			output_label.write(strNew_label.toString());
			output_label.close();
			
			System.out.println("--------更新完毕-----------");
		}
//		System.out.println(index.length);
	}
	
	private static int[] genRandomData(int n) {
		int data[] = new int[n];
		for(int i = 0; i < n; i++){
			data[i] = i;
		}
		int resultdata[] = new int[n];// 用于存放随机结果
		List<Integer> dataArray = new ArrayList<Integer>();// 暂存中间结果
		for (int i = 0; i < data.length; i++) {
			dataArray.add(data[i]);
		}
		int i = 0;
		// 从中间结果中取数据，并赋值随机数
		while (dataArray.size() > 0) {
			int index = (int) (Math.random() * dataArray.size());
			resultdata[i] = dataArray.get(index);
			i++;
			dataArray.remove(index);
		}
		// 输出随机排序结果
		for (int m = 0; m < resultdata.length; m++) {
			System.out.println("resultdata"+m+": "+resultdata[m]);
		}
		return resultdata;
	}
	
	private static void genRandomDataTest() {
		int data[] = { 1, 2, 3, 4, 5, 6, 7, 8, 0, 10, 11, 12, 9};
		int resultdata[] = new int[13];// 用于存放随机结果
		List<Integer> dataArray = new ArrayList<Integer>();// 暂存中间结果
		for (int i = 0; i < data.length; i++) {
			dataArray.add(data[i]);
		}
		int i = 0;
		// 从中间结果中取数据，并赋值随机数
		while (dataArray.size() > 0) {
			int index = (int) (Math.random() * dataArray.size());
			resultdata[i] = dataArray.get(index);
			i++;
			dataArray.remove(index);
		}
		// 输出随机排序结果
		for (int m = 0; m < resultdata.length; m++) {
//			System.out.println("resultdata"+m+": "+resultdata[m]);
		}
		
		// 取某个范围的任意数[10,20]
		int max = 20;
		int min = 10;
		Random random = new Random();
		while(true){
			int s = random.nextInt(max) % (max - min + 1) + min;
			System.out.println(s);
		}
		
	}

}
