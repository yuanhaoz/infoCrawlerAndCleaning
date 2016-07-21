package exp1;

/**
 * 分类器评价指标总结：
 * 1.混淆矩阵 
 * 2.精确度（Precision）
 * 3.召回率（Recall）
 * 4.Fscore（精确度和召回率的调和平均值）
 * 5.精确率（Accuracy）
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

import base.DirFile;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import excel.ExcelSet;

public class Step6_CaculateParameter {

	private static String rootPath = "F:\\word2vec\\NewData_HighQuality\\txt2Tokenizer\\file6_dataCNN(tile)";
	private static String keyword = "Absolute+deviation";
	private static String cata = "Absolute+deviation_tag_255_150T_50V_50T";
	private static String txt = keyword + "_tag_100_50";
	private static String Path = rootPath + "\\" + keyword + "\\" + cata;
	private static String rightLabelPath = rootPath + "\\"+ keyword + "\\" + txt + ".txt";
	private static String excelPath = rootPath + "\\"+ keyword + "\\" + cata + "\\分类器评价指标.xls";
	private static int validate = 150;
	private static int test = 200;
	
	public static void main(String[] args) throws Exception {
			// TODO Auto-generated method stub
//			createExcel();// 处理单个主题的数据
			dealAll();// 处理所有主题的数据
	}
	
	public static void dealAll() throws Exception{
		String rootPath = "F:\\word2vec\\NewData_HighQuality_word2vec\\txt2Tokenizer_new\\file6_dataCNN(tile)";
		ArrayList<String> words = DirFile.getFolderFileNamesFromDirectorybyArraylist(rootPath);
		for(int i = 0; i < words.size(); i++){
			String keyword = words.get(i);
			String childPath = rootPath + "\\" + keyword;
			String txt = keyword + "_tag_100_50";
			ArrayList<String> exp = DirFile.getFolderFileNamesFromDirectorybyArraylist(childPath);
			for(int j = 0; j < exp.size(); j++){
				String cata = exp.get(j);
				String Path = rootPath + "\\" + keyword + "\\" + cata;
				String rightLabelPath = rootPath + "\\"+ keyword + "\\" + txt + ".txt";//每个主题正确标签的路径
				String excelPath = rootPath + "\\"+ keyword + "\\" + cata + "\\分类器评价指标.xls";//每个主题Excel存储路径
				String[] arr = cata.split("_");
				int validate = Integer.parseInt(arr[3].substring(0, arr[3].length()-1));
				System.out.println("valiadte: " + validate);
				int test = validate + Integer.parseInt(arr[4].substring(0, arr[4].length()-1));
				System.out.println("test: " + test);
				createExcel(excelPath, Path, rightLabelPath, validate, test);
			}
		}
		getAllInfo(rootPath);
	}

	/**
	 * 得到单个关键词的信息
	 */
	public static void createExcel(String excelPath, String Path, String rightLabelPath, int validate, int test) throws Exception{
		WritableWorkbook workbook = Workbook.createWorkbook(new File(excelPath));
		WritableSheet sheet = workbook.createSheet("predict", 0);
		WritableSheet sheetV = workbook.createSheet("validate", 0);
		WritableSheet sheetT = workbook.createSheet("test", 0);
		WritableCellFormat wcf_title = ExcelSet.setTitleText();   //设置单元格正文格式
		WritableCellFormat wcf_center = ExcelSet.setCenterText();   //设置单元格正文格式
		String[] para = {"Precision", "Recall", "FScore", "Accuracy"};
		for(int i = 1; i < 5; i++){
			sheet.addCell(new Label(i, 0, para[i-1], wcf_title));
			sheetV.addCell(new Label(i, 0, para[i-1], wcf_title));
			sheetT.addCell(new Label(i, 0, para[i-1], wcf_title));
		}
		
		int row = 0;
		int rowV = 0;
		int rowT = 0;
		File[] files = new File(Path).listFiles();
		for(int i = 0; i < files.length; i++){
			String fileName = files[i].getName();
			String filePath = Path + "\\" + fileName;
			String txtPath = Path + "\\confusionMatrix";
			new File(txtPath).mkdir();
			if(fileName.contains("predict")){
				row++;
				System.out.println("\nfileName is: "+fileName);
				int[] confusionMatrix = confusionMatrix_Predict(rightLabelPath, filePath);
				confusionMatrix_Save(confusionMatrix, txtPath + "\\" + fileName);//将混淆矩阵保存到txt中
				float[] classfierAccess = getClassfierParameter(confusionMatrix);
				sheet.addCell(new Label(0, row, fileName, wcf_center));
				for(int j = 0; j < 4; j++){
					sheet.addCell(new Label(j+1, row, classfierAccess[j]+"", wcf_center));
				}
			}
			
			if(fileName.contains("validate")){
				rowV++;
				System.out.println("\nfileName is: "+fileName);
				int[] confusionMatrix = confusionMatrix_Validate(rightLabelPath, filePath, validate);
//				int[] confusionMatrix = confusionMatrix_Validate(rightLabelPath, filePath);
				confusionMatrix_Save(confusionMatrix, txtPath + "\\" + fileName);//将混淆矩阵保存到txt中
				float[] classfierAccess = getClassfierParameter(confusionMatrix);
				sheetV.addCell(new Label(0, rowV, fileName, wcf_center));
				for(int j = 0; j < 4; j++){
					sheetV.addCell(new Label(j+1, rowV, classfierAccess[j]+"", wcf_center));
				}
			}
			
			if(fileName.contains("test")){
				rowT++;
				System.out.println("\nfileName is: "+fileName);
				int[] confusionMatrix = confusionMatrix_Test(rightLabelPath, filePath, test);
//				int[] confusionMatrix = confusionMatrix_Test(rightLabelPath, filePath);
				confusionMatrix_Save(confusionMatrix, txtPath + "\\" + fileName);//将混淆矩阵保存到txt中
				float[] classfierAccess = getClassfierParameter(confusionMatrix);
				sheetT.addCell(new Label(0, rowT, fileName, wcf_center));
				for(int j = 0; j < 4; j++){
					sheetT.addCell(new Label(j+1, rowT, classfierAccess[j]+"", wcf_center));
				}
			}
		}
		
		ExcelSet.close(workbook);  //关闭工作空间
	}
	
	/**
	 * 得到单个关键词的信息
	 */
	public static void createExcel() throws Exception{
		WritableWorkbook workbook = Workbook.createWorkbook(new File(excelPath));
		WritableSheet sheet = workbook.createSheet("predict", 0);
		WritableSheet sheetV = workbook.createSheet("validate", 0);
		WritableSheet sheetT = workbook.createSheet("test", 0);
		WritableCellFormat wcf_title = ExcelSet.setTitleText();   //设置单元格正文格式
		WritableCellFormat wcf_center = ExcelSet.setCenterText();   //设置单元格正文格式
		String[] para = {"Precision", "Recall", "FScore", "Accuracy"};
		for(int i = 1; i < 5; i++){
			sheet.addCell(new Label(i, 0, para[i-1], wcf_title));
			sheetV.addCell(new Label(i, 0, para[i-1], wcf_title));
			sheetT.addCell(new Label(i, 0, para[i-1], wcf_title));
		}
		
		int row = 0;
		int rowV = 0;
		int rowT = 0;
		File[] files = new File(Path).listFiles();
		for(int i = 0; i < files.length; i++){
			String fileName = files[i].getName();
			String filePath = Path + "\\" + fileName;
			if(fileName.contains("predict")){
				row++;
				System.out.println("filePath is: "+filePath);
				int[] confusionMatrix = confusionMatrix_Predict(rightLabelPath, filePath);
				float[] classfierAccess = getClassfierParameter(confusionMatrix);
				sheet.addCell(new Label(0, row, fileName, wcf_center));
				for(int j = 0; j < 4; j++){
					sheet.addCell(new Label(j+1, row, classfierAccess[j]+"", wcf_center));
				}
			}
			
			if(fileName.contains("validate")){
				rowV++;
				System.out.println("filePath is: "+filePath);
				int[] confusionMatrix = confusionMatrix_Validate(rightLabelPath, filePath, validate);
				float[] classfierAccess = getClassfierParameter(confusionMatrix);
				sheetV.addCell(new Label(0, rowV, fileName, wcf_center));
				for(int j = 0; j < 4; j++){
					sheetV.addCell(new Label(j+1, rowV, classfierAccess[j]+"", wcf_center));
				}
			}
			
			if(fileName.contains("test")){
				rowT++;
				System.out.println("filePath is: "+filePath);
				int[] confusionMatrix = confusionMatrix_Test(rightLabelPath, filePath, test);
				float[] classfierAccess = getClassfierParameter(confusionMatrix);
				sheetT.addCell(new Label(0, rowT, fileName, wcf_center));
				for(int j = 0; j < 4; j++){
					sheetT.addCell(new Label(j+1, rowT, classfierAccess[j]+"", wcf_center));
				}
			}
		}
		
		ExcelSet.close(workbook);  //关闭工作空间
	}
	
	public static void classfierAll(){
		File[] files = new File(Path).listFiles();
		for(int i = 0; i < files.length; i++){
			String fileName = files[i].getName();
			String filePath = Path + "\\" + fileName;
			if(fileName.contains("predict")){
				System.out.println("filePath is: "+filePath);
				int[] confusionMatrix = confusionMatrix_Predict(rightLabelPath, filePath);
				float[] classfierAccess = getClassfierParameter(confusionMatrix);
			}
			if(fileName.contains("validate")){
				System.out.println("filePath is: "+filePath);
				int[] confusionMatrix = confusionMatrix_Validate(rightLabelPath, filePath, 1200);
				float[] classfierAccess = getClassfierParameter(confusionMatrix);
			}
			if(fileName.contains("test")){
				System.out.println("filePath is: "+filePath);
				int[] confusionMatrix = confusionMatrix_Test(rightLabelPath, filePath, 1300);
				float[] classfierAccess = getClassfierParameter(confusionMatrix);
			}
		}
		
	}
	
	@SuppressWarnings("resource")
	public static ArrayList<Integer> getRightLabel(String rightLabelPath){
		File rightLabel = new File(rightLabelPath);
		ArrayList<Integer> labelRight = new ArrayList<Integer>();
		String encoding = "UTF-8";
		InputStreamReader read;
		try {
			read = new InputStreamReader(new FileInputStream(rightLabel),encoding);
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while((lineTxt = bufferedReader.readLine()) != null){
//				System.out.println(lineTxt);
//				System.out.println(Integer.parseInt(lineTxt));
				labelRight.add(Integer.parseInt(lineTxt));
			}
			System.out.println("labelRight's size is: "+labelRight.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return labelRight;
	}
	
	@SuppressWarnings("resource")
	public static ArrayList<Integer> getPredictLabel(String labelPath){
		File predictLabel = new File(labelPath);
		ArrayList<Integer> labelPredict = new ArrayList<Integer>();
		String encoding = "UTF-8";
		InputStreamReader read;
		try {
			read = new InputStreamReader(new FileInputStream(predictLabel),encoding);
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while((lineTxt = bufferedReader.readLine()) != null){
//				System.out.println(lineTxt);
//				System.out.println(Integer.parseInt(lineTxt));
				labelPredict.add(Integer.parseInt(lineTxt));
			}
			System.out.println("labelPredict's size is: "+labelPredict.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return labelPredict;
	}
	
	/**
	 * 将混淆矩阵写到txt中保存
	 * @throws Exception 
	 */
	public static void confusionMatrix_Save(int[] confusionMatrix, String path) throws Exception{
//		int[] confusionMatrix = {0, 0, 0, 0};// 0-TP,1-FP,2-FN,3-TN
		String matrix = "TP: " + confusionMatrix[0] + "\nFP: " + confusionMatrix[1] + "\nFN: " + confusionMatrix[2]
				+ "\nTN: " + confusionMatrix[3];
		FileWriter output = new FileWriter(path);// 在原有基础上写
		output.write(matrix);
		output.close();
	}
	
	public static int[] confusionMatrix_Predict(String rightLabelPath, String labelPath){
		int[] confusionMatrix = {0, 0, 0, 0};// 0-TP,1-FP,2-FN,3-TN
		ArrayList<Integer> labelRight = getRightLabel(rightLabelPath);
		ArrayList<Integer> labelPredict = getPredictLabel(labelPath);
		
		for(int i = 0; i < labelPredict.size(); i++){
			int predict = labelPredict.get(i);
			int right = labelRight.get(i);
			if(right == 1 && predict == 1){
				confusionMatrix[0]++;
			}
			if(right == 0 && predict == 1){
				confusionMatrix[1]++;
			}
			if(right == 1 && predict == 0){
				confusionMatrix[2]++;
			}
			if(right == 0 && predict == 0){
				confusionMatrix[3]++;
			}
		}
//		System.out.println("TP is: " + confusionMatrix[0]);
//		System.out.println("FP is: " + confusionMatrix[1]);
//		System.out.println("FN is: " + confusionMatrix[2]);
//		System.out.println("TN is: " + confusionMatrix[3]);
		return confusionMatrix;
		
	}
	
	public static int[] confusionMatrix_Validate(String rightLabelPath, String labelPath, int id){
		int[] confusionMatrix = {0, 0, 0, 0};// 0-TP,1-FP,2-FN,3-TN
		ArrayList<Integer> labelRight = getRightLabel(rightLabelPath);
		ArrayList<Integer> labelPredict = getPredictLabel(labelPath);
		
		for(int i = 0; i < labelPredict.size(); i++){
			int predict = labelPredict.get(i);
			int right = labelRight.get(i + id);
			if(right == 1 && predict == 1){
				confusionMatrix[0]++;
			}
			if(right == 0 && predict == 1){
				confusionMatrix[1]++;
			}
			if(right == 1 && predict == 0){
				confusionMatrix[2]++;
			}
			if(right == 0 && predict == 0){
				confusionMatrix[3]++;
			}
		}
//		System.out.println("TP is: " + confusionMatrix[0]);
//		System.out.println("FP is: " + confusionMatrix[1]);
//		System.out.println("FN is: " + confusionMatrix[2]);
//		System.out.println("TN is: " + confusionMatrix[3]);
		return confusionMatrix;
	}
	
	/**
	 * 所有样本为测试集
	 */
	public static int[] confusionMatrix_Validate(String rightLabelPath, String labelPath){
		int[] confusionMatrix = {0, 0, 0, 0};// 0-TP,1-FP,2-FN,3-TN
		ArrayList<Integer> labelRight = getRightLabel(rightLabelPath);
		ArrayList<Integer> labelPredict = getPredictLabel(labelPath);
		
		for(int i = 0; i < labelPredict.size(); i++){
			int predict = labelPredict.get(i);
			int right = labelRight.get(i);
			if(right == 1 && predict == 1){
				confusionMatrix[0]++;
			}
			if(right == 0 && predict == 1){
				confusionMatrix[1]++;
			}
			if(right == 1 && predict == 0){
				confusionMatrix[2]++;
			}
			if(right == 0 && predict == 0){
				confusionMatrix[3]++;
			}
		}
//		System.out.println("TP is: " + confusionMatrix[0]);
//		System.out.println("FP is: " + confusionMatrix[1]);
//		System.out.println("FN is: " + confusionMatrix[2]);
//		System.out.println("TN is: " + confusionMatrix[3]);
		return confusionMatrix;
	}

	public static int[] confusionMatrix_Test(String rightLabelPath, String labelPath, int id){
		int[] confusionMatrix = {0, 0, 0, 0};// 0-TP,1-FP,2-FN,3-TN
		ArrayList<Integer> labelRight = getRightLabel(rightLabelPath);
		ArrayList<Integer> labelPredict = getPredictLabel(labelPath);
		
		for(int i = 0; i < labelPredict.size(); i++){
			int predict = labelPredict.get(i);
			int right = labelRight.get(i + id);
			if(right == 1 && predict == 1){
				confusionMatrix[0]++;
			}
			if(right == 0 && predict == 1){
				confusionMatrix[1]++;
			}
			if(right == 1 && predict == 0){
				confusionMatrix[2]++;
			}
			if(right == 0 && predict == 0){
				confusionMatrix[3]++;
			}
		}
//		System.out.println("TP is: " + confusionMatrix[0]);
//		System.out.println("FP is: " + confusionMatrix[1]);
//		System.out.println("FN is: " + confusionMatrix[2]);
//		System.out.println("TN is: " + confusionMatrix[3]);
		return confusionMatrix;
	}
	
	/**
	 * 所有样本为测试集
	 */
	public static int[] confusionMatrix_Test(String rightLabelPath, String labelPath){
		int[] confusionMatrix = {0, 0, 0, 0};// 0-TP,1-FP,2-FN,3-TN
		ArrayList<Integer> labelRight = getRightLabel(rightLabelPath);
		ArrayList<Integer> labelPredict = getPredictLabel(labelPath);
		
		for(int i = 0; i < labelPredict.size(); i++){
			int predict = labelPredict.get(i);
			int right = labelRight.get(i);
			if(right == 1 && predict == 1){
				confusionMatrix[0]++;
			}
			if(right == 0 && predict == 1){
				confusionMatrix[1]++;
			}
			if(right == 1 && predict == 0){
				confusionMatrix[2]++;
			}
			if(right == 0 && predict == 0){
				confusionMatrix[3]++;
			}
		}
//		System.out.println("TP is: " + confusionMatrix[0]);
//		System.out.println("FP is: " + confusionMatrix[1]);
//		System.out.println("FN is: " + confusionMatrix[2]);
//		System.out.println("TN is: " + confusionMatrix[3]);
		return confusionMatrix;
	}
	
	public static float getPrecision(int[] confusionMatrix){
		int TP = confusionMatrix[0];
		int FP = confusionMatrix[1];
		int FN = confusionMatrix[2];
		int TN = confusionMatrix[3];
		float precision = 0;
		if(TP + FP != 0){
			precision = (float)TP / (TP + FP);
			DecimalFormat df = new DecimalFormat("0.0000");//格式化小数，不足的补0
			precision = Float.parseFloat(df.format(precision));//返回的是String类型的
			System.out.println("precision is: " + precision);
		} else {
			precision = (float) 0.0000;
			DecimalFormat df = new DecimalFormat("0.0000");//格式化小数，不足的补0
			precision = Float.parseFloat(df.format(precision));//返回的是String类型的
			System.out.println("precision is: " + precision);
		}
		
		return precision;
	}
	
	public static float getRecall(int[] confusionMatrix){
		int TP = confusionMatrix[0];
		int FP = confusionMatrix[1];
		int FN = confusionMatrix[2];
		int TN = confusionMatrix[3];
		float recall = 0;
		if((TP + FN) != 0){
			recall = (float)TP / (TP + FN);
			DecimalFormat df = new DecimalFormat("0.0000");//格式化小数，不足的补0
			recall = Float.parseFloat(df.format(recall));//返回的是String类型的
			System.out.println("Recall is: " + recall);
		} else {
			recall = 0;
			DecimalFormat df = new DecimalFormat("0.0000");//格式化小数，不足的补0
			recall = Float.parseFloat(df.format(recall));//返回的是String类型的
			System.out.println("recall is: " + recall);
		}
		return recall;
	}

	public static float getFScore(float precision, float recall){
		float fscore = 0;
		if(precision + recall != 0){
			fscore = (float)(2 * precision * recall) / (precision + recall);
			DecimalFormat df = new DecimalFormat("0.0000");//格式化小数，不足的补0
			fscore = Float.parseFloat(df.format(fscore));//返回的是String类型的
			System.out.println("fscore is: " + fscore);
		} else {
			fscore = 0;
			DecimalFormat df = new DecimalFormat("0.0000");//格式化小数，不足的补0
			fscore = Float.parseFloat(df.format(fscore));//返回的是String类型的
			System.out.println("fscore is: " + fscore);
		}
		
		return fscore;
	}
	
	public static float getAccuracy(int[] confusionMatrix){
		int TP = confusionMatrix[0];
		int FP = confusionMatrix[1];
		int FN = confusionMatrix[2];
		int TN = confusionMatrix[3];
		float accuracy = (float)(TP + TN) / (TP + FN + FP + TN);
		if(TP + FN + FP + TN != 0){
			accuracy = (float)(TP + TN) / (TP + FN + FP + TN);
			DecimalFormat df = new DecimalFormat("0.0000");//格式化小数，不足的补0
			accuracy = Float.parseFloat(df.format(accuracy));//返回的是String类型的
			System.out.println("fscore is: " + accuracy);
		} else {
			accuracy = 0;
			DecimalFormat df = new DecimalFormat("0.0000");//格式化小数，不足的补0
			accuracy = Float.parseFloat(df.format(accuracy));//返回的是String类型的
			System.out.println("fscore is: " + accuracy);
		}
		
		return accuracy;
	}
	
	public static float[] getClassfierParameter(int[] confusionMatrix){
		float[] classfierAccess = new float[4];// 0-Precision,1-Recall,2-FScore,3-Accuracy
		classfierAccess[0] = getPrecision(confusionMatrix);
		classfierAccess[1] = getRecall(confusionMatrix);
		classfierAccess[2] = getFScore(classfierAccess[0], classfierAccess[1]);
		classfierAccess[3] = getAccuracy(confusionMatrix);
		return classfierAccess;
	}
	
//	public static void test(){
//		String labelPath = "D:\\Workspace\\Python\\CNN_Python\\Array1550_1200T_100V_200T\\predict11.txt";
//		String labelPath1 = "D:\\Workspace\\Python\\CNN_Python\\Array1550_1200T_100V_200T\\validate5.txt";
//		String labelPath2= "D:\\Workspace\\Python\\CNN_Python\\Array1550_1200T_100V_200T\\validate4.txt";
//		String labelPath3 = "D:\\Workspace\\Python\\CNN_Python\\Array1550_1200T_100V_200T\\test1.txt";
////		getRightLabel();
////		getPredictLabel(labelPath);
//		int[] confusionMatrix = confusionMatrix_Predict(labelPath);
//		float precision = getPrecision(confusionMatrix);
//		float recall = getRecall(confusionMatrix);
//		float fscore = getFScore(precision, recall);
//		float accuracy = getAccuracy(confusionMatrix);
//		
//		int[] confusionMatrixV = confusionMatrix_Validate(labelPath1, 1200);
//		float precisionV = getPrecision(confusionMatrixV);
//		float recallV = getRecall(confusionMatrixV);
//		float fscoreV = getFScore(precisionV, recallV);
//		float accuracyV = getAccuracy(confusionMatrixV);
//		
//		int[] confusionMatrixT = confusionMatrix_Test(labelPath3, 1300);
//		float precisionT = getPrecision(confusionMatrixT);
//		float recallT = getRecall(confusionMatrixT);
//		float fscoreT = getFScore(precisionT, recallT);
//		float accuracyT = getAccuracy(confusionMatrixT);
//	}
	
	/**
	 *统计所有表格的信息
	 * @throws Exception
	 */
	public static void getAllInfo(String rootPath) throws Exception{
		WritableWorkbook workbook = Workbook.createWorkbook(new File(rootPath + "\\分类器性能.xls"));
		WritableSheet sheet = workbook.createSheet("统计", 0);
		WritableCellFormat wcf_title = ExcelSet.setTitleText();   //设置单元格正文格式
		WritableCellFormat wcf_center = ExcelSet.setCenterText();   //设置单元格正文格式
		sheet.setColumnView(0, 50);
		// 标题信息
		String[] para = {"Topic", "Data", "Precision", "Recall", "FScore", "Accuracy"};
		for(int i = 0; i < 6; i++){
			sheet.addCell(new Label(i, 0, para[i], wcf_title));
		}
		// 内容信息
		int row = 0;
//		File[] files = new File(rootPath).listFiles();
		ArrayList<String> files = DirFile.getFolderFileNamesFromDirectorybyArraylist(rootPath);
		for(int i = 0; i < files.size(); i++){
			String topic = files.get(i);
			String filePath = rootPath + "\\" + topic;
			ArrayList<String> exps = DirFile.getFolderFileNamesFromDirectorybyArraylist(filePath);//三次实验的路径
			for(int j = 0; j < exps.size(); j++){
				String expId = exps.get(j);
				String expPath = filePath + "\\" + expId;
				String expExcelPath = expPath + "\\分类器评价指标.xls";
				System.out.println(expExcelPath);
				try{
					Workbook wb = Workbook.getWorkbook(new File(expExcelPath));
					// 处理训练数据train
					row++;
					Sheet train = wb.getSheet(2);
					int rowTrain = train.getRows();
					if (rowTrain == 1){
						ArrayList<String> trainInfo = new ArrayList<String>();
						String precisionTrain = "";
						String recallTrain = "";
						String fscoreTrain = "";
						String accuaryTrain = "";
						trainInfo.add(expId);
						trainInfo.add("train");
						trainInfo.add(precisionTrain);
						trainInfo.add(recallTrain);
						trainInfo.add(fscoreTrain);
						trainInfo.add(accuaryTrain);
						sheet.setRowView(row, 500);
						for(int m = 0; m < trainInfo.size(); m++){
							sheet.addCell(new Label(m, row, trainInfo.get(m), wcf_center));
						}
					} else {
						ArrayList<String> trainInfo = new ArrayList<String>();
						String precisionTrain = train.getCell(1, rowTrain-1).getContents();
						String recallTrain = train.getCell(2, rowTrain-1).getContents();
						String fscoreTrain = train.getCell(3, rowTrain-1).getContents();
						String accuaryTrain = train.getCell(4, rowTrain-1).getContents();
						trainInfo.add(expId);
						trainInfo.add("train");
						trainInfo.add(precisionTrain);
						trainInfo.add(recallTrain);
						trainInfo.add(fscoreTrain);
						trainInfo.add(accuaryTrain);
						sheet.setRowView(row, 500);
						for(int m = 0; m < trainInfo.size(); m++){
							sheet.addCell(new Label(m, row, trainInfo.get(m), wcf_center));
						}
					}
					
					// 处理验证数据validate
					row++;
					Sheet validate = wb.getSheet(1);
					int rowValidate = validate.getRows();
					if (rowValidate == 1){
						ArrayList<String> validateInfo = new ArrayList<String>();
						String precisionValidate = "";
						String recallValidate = "";
						String fscoreValidate = "";
						String accuaryValidate = "";
						validateInfo.add(expId);
						validateInfo.add("validate");
						validateInfo.add(precisionValidate);
						validateInfo.add(recallValidate);
						validateInfo.add(fscoreValidate);
						validateInfo.add(accuaryValidate);
						sheet.setRowView(row, 500);
						for(int m = 0; m < validateInfo.size(); m++){
							sheet.addCell(new Label(m, row, validateInfo.get(m), wcf_center));
						}
					} else {
						ArrayList<String> validateInfo = new ArrayList<String>();
						String precisionValidate = validate.getCell(1, rowValidate-1).getContents();
						String recallValidate = validate.getCell(2, rowValidate-1).getContents();
						String fscoreValidate = validate.getCell(3, rowValidate-1).getContents();
						String accuaryValidate = validate.getCell(4, rowValidate-1).getContents();
						validateInfo.add(expId);
						validateInfo.add("validate");
						validateInfo.add(precisionValidate);
						validateInfo.add(recallValidate);
						validateInfo.add(fscoreValidate);
						validateInfo.add(accuaryValidate);
						sheet.setRowView(row, 500);
						for(int m = 0; m < validateInfo.size(); m++){
							sheet.addCell(new Label(m, row, validateInfo.get(m), wcf_center));
						}
					}
					
					// 处理训练数据test
					row++;
					Sheet test = wb.getSheet(0);
					int rowTest = test.getRows();
					if (rowTest == 1){
						ArrayList<String> testInfo = new ArrayList<String>();
						String precisionTest = "";
						String recallTest = "";
						String fscoreTest = "";
						String accuaryTest = "";
						testInfo.add(expId);
						testInfo.add("test");
						testInfo.add(precisionTest);
						testInfo.add(recallTest);
						testInfo.add(fscoreTest);
						testInfo.add(accuaryTest);
						sheet.setRowView(row, 500);
						for(int m = 0; m < testInfo.size(); m++){
							sheet.addCell(new Label(m, row, testInfo.get(m), wcf_center));
						}
					} else {
						ArrayList<String> testInfo = new ArrayList<String>();
						String precisionTest = test.getCell(1, rowTest-1).getContents();
						String recallTest = test.getCell(2, rowTest-1).getContents();
						String fscoreTest = test.getCell(3, rowTest-1).getContents();
						String accuaryTest = test.getCell(4, rowTest-1).getContents();
						testInfo.add(expId);
						testInfo.add("test");
						testInfo.add(precisionTest);
						testInfo.add(recallTest);
						testInfo.add(fscoreTest);
						testInfo.add(accuaryTest);
						sheet.setRowView(row, 500);
						for(int m = 0; m < testInfo.size(); m++){
							sheet.addCell(new Label(m, row, testInfo.get(m), wcf_center));
						}
					}
				} catch (Exception e) {
					System.out.println("excel文件不存在...");
				}
				
			}
			
		}
		
		ExcelSet.close(workbook);  //关闭工作空间
	}
	
}
