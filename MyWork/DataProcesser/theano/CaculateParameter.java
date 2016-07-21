package theano;

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
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import excel.ExcelSet;

public class CaculateParameter {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		classfierAll();
		createExcel();
	}
	
	private static String Path = "D:\\Workspace\\Python\\CNN_Python\\Array1550_1200T_100V_200T";
	private static String rightLabelPath = "D:\\Workspace\\Python\\CNN_Python\\data\\Array_tag_456_112.txt";
	
	public static void test(){
		String labelPath = "D:\\Workspace\\Python\\CNN_Python\\Array1550_1200T_100V_200T\\predict11.txt";
		String labelPath1 = "D:\\Workspace\\Python\\CNN_Python\\Array1550_1200T_100V_200T\\validate5.txt";
		String labelPath2= "D:\\Workspace\\Python\\CNN_Python\\Array1550_1200T_100V_200T\\validate4.txt";
		String labelPath3 = "D:\\Workspace\\Python\\CNN_Python\\Array1550_1200T_100V_200T\\test1.txt";
//		getRightLabel();
//		getPredictLabel(labelPath);
		int[] confusionMatrix = confusionMatrix_Predict(labelPath);
		float precision = getPrecision(confusionMatrix);
		float recall = getRecall(confusionMatrix);
		float fscore = getFScore(precision, recall);
		float accuracy = getAccuracy(confusionMatrix);
		
		int[] confusionMatrixV = confusionMatrix_Validate(labelPath1, 1200);
		float precisionV = getPrecision(confusionMatrixV);
		float recallV = getRecall(confusionMatrixV);
		float fscoreV = getFScore(precisionV, recallV);
		float accuracyV = getAccuracy(confusionMatrixV);
		
		int[] confusionMatrixT = confusionMatrix_Test(labelPath3, 1300);
		float precisionT = getPrecision(confusionMatrixT);
		float recallT = getRecall(confusionMatrixT);
		float fscoreT = getFScore(precisionT, recallT);
		float accuracyT = getAccuracy(confusionMatrixT);
	}
	
	public static void createExcel() throws Exception{
		String excelPath = "D:\\Workspace\\Python\\CNN_Python\\Array1550_1200T_100V_200T\\分类器评价指标.xls";
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
				int[] confusionMatrix = confusionMatrix_Predict(filePath);
				float[] classfierAccess = getClassfierParameter(confusionMatrix);
				sheet.addCell(new Label(0, row, fileName, wcf_center));
				for(int j = 0; j < 4; j++){
					sheet.addCell(new Label(j+1, row, classfierAccess[j]+"", wcf_center));
				}
			}
			
			if(fileName.contains("validate")){
				rowV++;
				System.out.println("filePath is: "+filePath);
				int[] confusionMatrix = confusionMatrix_Validate(filePath, 1200);
				float[] classfierAccess = getClassfierParameter(confusionMatrix);
				sheetV.addCell(new Label(0, rowV, fileName, wcf_center));
				for(int j = 0; j < 4; j++){
					sheetV.addCell(new Label(j+1, rowV, classfierAccess[j]+"", wcf_center));
				}
			}
			
			if(fileName.contains("test")){
				rowT++;
				System.out.println("filePath is: "+filePath);
				int[] confusionMatrix = confusionMatrix_Test(filePath, 1300);
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
				int[] confusionMatrix = confusionMatrix_Predict(filePath);
				float[] classfierAccess = getClassfierParameter(confusionMatrix);
			}
			if(fileName.contains("validate")){
				System.out.println("filePath is: "+filePath);
				int[] confusionMatrix = confusionMatrix_Validate(filePath, 1200);
				float[] classfierAccess = getClassfierParameter(confusionMatrix);
			}
			if(fileName.contains("test")){
				System.out.println("filePath is: "+filePath);
				int[] confusionMatrix = confusionMatrix_Test(filePath, 1300);
				float[] classfierAccess = getClassfierParameter(confusionMatrix);
			}
		}
		
	}
	
	@SuppressWarnings("resource")
	public static ArrayList<Integer> getRightLabel(){
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
	
	public static int[] confusionMatrix_Predict(String labelPath){
		int[] confusionMatrix = {0, 0, 0, 0};// 0-TP,1-FP,2-FN,3-TN
		ArrayList<Integer> labelRight = getRightLabel();
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
		System.out.println("TP is: " + confusionMatrix[0]);
		System.out.println("FP is: " + confusionMatrix[1]);
		System.out.println("FN is: " + confusionMatrix[2]);
		System.out.println("TN is: " + confusionMatrix[3]);
		return confusionMatrix;
		
	}
	
	public static int[] confusionMatrix_Validate(String labelPath, int id){
		int[] confusionMatrix = {0, 0, 0, 0};// 0-TP,1-FP,2-FN,3-TN
		ArrayList<Integer> labelRight = getRightLabel();
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
		System.out.println("TP is: " + confusionMatrix[0]);
		System.out.println("FP is: " + confusionMatrix[1]);
		System.out.println("FN is: " + confusionMatrix[2]);
		System.out.println("TN is: " + confusionMatrix[3]);
		return confusionMatrix;
	}

	public static int[] confusionMatrix_Test(String labelPath, int id){
		int[] confusionMatrix = {0, 0, 0, 0};// 0-TP,1-FP,2-FN,3-TN
		ArrayList<Integer> labelRight = getRightLabel();
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
		System.out.println("TP is: " + confusionMatrix[0]);
		System.out.println("FP is: " + confusionMatrix[1]);
		System.out.println("FN is: " + confusionMatrix[2]);
		System.out.println("TN is: " + confusionMatrix[3]);
		return confusionMatrix;
	}
	
	public static float getPrecision(int[] confusionMatrix){
		int TP = confusionMatrix[0];
		int FP = confusionMatrix[1];
		int FN = confusionMatrix[2];
		int TN = confusionMatrix[3];
		float precision = (float)TP / (TP + FP);
		if(TP + FP != 0){
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
		float recall = (float)TP / (TP + FN);
		DecimalFormat df = new DecimalFormat("0.0000");//格式化小数，不足的补0
		recall = Float.parseFloat(df.format(recall));//返回的是String类型的
		System.out.println("Recall is: " + recall);
		return recall;
	}

	public static float getFScore(float precision, float recall){
		float fscore = (float)(2 * precision * recall) / (precision + recall);
		if(precision + recall != 0){
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
		DecimalFormat df = new DecimalFormat("0.0000");//格式化小数，不足的补0
		accuracy = Float.parseFloat(df.format(accuracy));//返回的是String类型的
		System.out.println("accuracy is: " + accuracy);
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
	
}
