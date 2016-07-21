package exp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import base.DirFile;
import excel.ExcelSet;

public class Base1_GetWekaData {
	private static String cata = "02-CQA网站中问题答案质量评估";
	private static String inputFile = "F:\\"+cata+"\\数据结构标注20主题_Step3\\txt1Tokenizer\\file3_postdeal_step2";
	private static String outFile = "F:\\"+cata+"\\数据结构标注20主题_Step3\\txt1Tokenizer\\file3_postdeal_step2_excel";
	private static String outFile2 = "F:\\"+cata+"\\数据结构标注20主题_Step3\\txt1Tokenizer\\file3_postdeal_step2_excel2";
	private static String tagFile = "F:\\"+cata+"\\数据结构标注20主题_Step3\\txt1Tokenizer\\file5_tag_step1(tile)";
	String encoding = "UTF-8";
	
	/**
	 *  删除文件
	 */
	public static void deleteFile() throws Exception {
		File[] files = new File(outFile2).listFiles();
		for(int i = 0; i < files.length; i++){
			String keyword = files[i].getName();
			String filePath = outFile2 + "\\" + keyword + "\\" + keyword + ".xls";
			new File(filePath).delete();
		}
		
	}
	
	/**
	 *  读取TXT文件第i行数据
	 */
	public static String readLine(int lineNumber, BufferedReader reader) throws Exception {
		lineNumber = lineNumber + 1;
		String line = "";
		int i = 0;
		while (i < lineNumber) {
			line = reader.readLine();
			i++;
		}
		return line;
	}
	
	/**
	 *  将一个词向量文件中的词向量矩阵写到一个二维数组中
	 */
	public static double[][] getWord2VecMatrix(String inputfile) throws Exception{
		@SuppressWarnings("resource")
		BufferedReader rowbufferedReader = new BufferedReader(new FileReader(inputfile));
		BufferedReader columnbufferedReader = new BufferedReader(new FileReader(inputfile));
		// 词向量矩阵行数
		int rowNum = 0;
		while ((rowbufferedReader.readLine()) != null) {
			rowNum++;
		}
		System.out.println(rowNum);
		// 词向量矩阵列数（每行列数相同）
		String firstStr = readLine(0, columnbufferedReader);// 得去文件一行数据
		String[] firstSplit = firstStr.split(" ");
		int columnNum = firstSplit.length;
		// 生成词向量矩阵
		System.out.println(columnNum);
		double[][] Word2VecMatrix = new double[rowNum][columnNum];
		for (int i = 0; i < rowNum; i++) {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(inputfile));
			// 得到每行数据
			String rowData = readLine(i, bufferedReader);
			System.out.println("第"+(i+1)+"数据为："+rowData);
			if (rowData == null) {
				System.out.println("第"+(i+1)+"行无数据...");
			} else {
				String[] rowDataAll = rowData.split(" ");
				for (int j = 0; j < columnNum; j++) {
					if (rowDataAll[j] == ""){// 某个数据不存在
						continue;
					}
					// 数据写进词向量矩阵
					Word2VecMatrix[i][j] = Double.valueOf(rowDataAll[j]);
					System.out.println("++++" + Word2VecMatrix[i][j]);
				}
//				System.out.println("第"+(i+1)+"行数据写完...");
			}
		}
		System.out.println("----------------------------------------------------");
		System.out.println(inputfile+"词向量矩阵生成完毕...");
		return Word2VecMatrix;
	}
	
	/**
	 *  将一个词向量文件中的词向量矩阵写到一个二维数组中（字符串型）
	 */
	public static String[][] getWord2VecMatrixbyString(String inputfile) throws Exception{
		@SuppressWarnings("resource")
		BufferedReader rowbufferedReader = new BufferedReader(new FileReader(inputfile));
		BufferedReader columnbufferedReader = new BufferedReader(new FileReader(inputfile));
		// 词向量矩阵行数
		int rowNum = 0;
		while ((rowbufferedReader.readLine()) != null) {
			rowNum++;
		}
//		System.out.println("行数：" + rowNum);
		// 词向量矩阵列数（每行列数相同）
		String firstStr = readLine(0, columnbufferedReader);// 得去文件一行数据
		String[] firstSplit = firstStr.split(" ");
		int columnNum = firstSplit.length;
		// 生成词向量矩阵
//		System.out.println("列数：" + columnNum);
		String[][] Word2VecMatrix = new String[rowNum][columnNum];
		for (int i = 0; i < rowNum; i++) {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(inputfile));
			// 得到每行数据
			String rowData = readLine(i, bufferedReader);
//			System.out.println("第"+(i+1)+"数据为："+rowData);
			if (rowData == null) {
				System.out.println("第"+(i+1)+"行无数据...");
			} else {
				String[] rowDataAll = rowData.split(" ");
				for (int j = 0; j < columnNum; j++) {
					if (rowDataAll[j] == ""){// 某个数据不存在
						continue;
					}
					// 数据写进词向量矩阵
					Word2VecMatrix[i][j] = rowDataAll[j];
//					System.out.println("++++" + Word2VecMatrix[i][j]);
				}
//				System.out.println("第"+(i+1)+"行数据写完...");
			}
		}
		System.out.println("----------------------------------------------------");
		System.out.println(inputfile+"词向量矩阵生成完毕...");
		return Word2VecMatrix;
	}

	/**
	 * 求矩阵的平均值
	 */
	public static double[] matrixAverage(String inputFile) throws Exception{
		double[][] matrix = getWord2VecMatrix(inputFile);
		double[] average = new double[matrix[0].length];
		// 内容信息
		for(int j = 0; j < matrix[0].length; j++){
			double sum = 0;
			for(int i = 0; i < matrix.length; i++){
				BigDecimal b1 = new BigDecimal(Double.toString(sum));
				BigDecimal b2 = new BigDecimal(Double.toString(matrix[i][j]));
				sum = b1.add(b2).doubleValue();
				sum = sum + matrix[i][j];
			}
			average[j] = sum / matrix.length;
			System.out.println("第" + j + "列的平均值为：" + average[j]);
		}
		return average;
	}
	
	/**
	 * 求矩阵的平均值
	 */
	public static String[] matrixAveragebyString(String inputFile) throws Exception{
		String[][] matrix = getWord2VecMatrixbyString(inputFile);
		String[] average = new String[matrix[0].length];
		// 内容信息
		for(int j = 0; j < matrix[0].length; j++){
			String sum = "0.000000";
			for(int i = 0; i < matrix.length; i++){
				BigDecimal b1 = new BigDecimal(sum);
				BigDecimal b2 = new BigDecimal(matrix[i][j]);
				sum = b1.add(b2).toString(); // 使用math中的BigDecimal对象的加法可以保存精度信息
//				System.out.println("sum is : " + sum);
			}
			int scale = 6;// 最后数值的精度
			BigDecimal b1=new BigDecimal(sum);
			BigDecimal b2=new BigDecimal(Double.toString(matrix.length));
			average[j] = b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toString();
//			System.out.println("第" + j + "列的平均值为：" + average[j]);
		}
		return average;
	}

	/**
	 * TXT矩阵写到excel中：某主题下的所有问答词向量特征及其标签写到excel中
	 */
	public static void getWekaTrainData(String txtfilePath, String tagfilePath, String excelPath) throws Exception{
		WritableWorkbook workbook = Workbook.createWorkbook(new File(excelPath));
		WritableSheet sheet = workbook.createSheet("word2vec", 0);
		// 某主题下的所有问答词向量特征及其标签写到excel中
		File[] txts = new File(txtfilePath).listFiles();
		int row = 0;//excel行
		// 第一行标题
		for(int i = 0; i < 50; i++){
			Label feature = new Label(i, 0, "f" + (i+1));
			sheet.addCell(feature);
		}
		sheet.addCell(new Label(50, 0, "label"));
		for(int m = 0; m < txts.length; m++){
			String txtName = txts[m].getName();
			String txtPath = txtfilePath + "\\" + txtName;
			String tagPath = tagfilePath + "\\" + txtName;
			String[][] matrix = getWord2VecMatrixbyString(txtPath);
			String tag = DirFile.getStringFromPathFile(tagPath);
			// 内容信息
			for(int i = 0; i < matrix.length; i++){
				row++;
				for(int j = 0; j < matrix[0].length; j++){//写入50维词向量特征数据
					Label feature = new Label(j, row, matrix[i][j]);
					sheet.addCell(feature);
				}
				// 用于weka分类
				if(tag.equals("1")){
					Label tagData = new Label(matrix[0].length, row, "yes");//写入标签
					sheet.addCell(tagData);
				} else {
					Label tagData = new Label(matrix[0].length, row, "no");//写入标签
					sheet.addCell(tagData);
				}
			}
		}
		
		ExcelSet.close(workbook);  //关闭工作空间
	}
	
	/**
	 * TXT矩阵写到excel中：某主题下的所有问答词向量特征及其标签写到excel中
	 */
	public static void getWekaTestData(String txtfilePath, String tagfilePath, String excelPath) throws Exception{
		WritableWorkbook workbook = Workbook.createWorkbook(new File(excelPath));
		WritableSheet sheet = workbook.createSheet("word2vec", 0);
		// 某主题下的所有问答词向量特征及其标签写到excel中
		File[] txts = new File(txtfilePath).listFiles();
		int row = 0;//excel行
		// 第一行标题
		for(int i = 0; i < 50; i++){
			Label feature = new Label(i, 0, "f" + (i+1));
			sheet.addCell(feature);
		}
		sheet.addCell(new Label(50, 0, "label"));
		for(int m = 0; m < txts.length; m++){
			String txtName = txts[m].getName();
			String txtPath = txtfilePath + "\\" + txtName;
			String[][] matrix = getWord2VecMatrixbyString(txtPath);
			// 内容信息
			for(int i = 0; i < matrix.length; i++){
				row++;
				for(int j = 0; j < matrix[0].length; j++){//写入50维词向量特征数据
					Label feature = new Label(j, row, matrix[i][j]);
					sheet.addCell(feature);
				}
			}
		}
		
		ExcelSet.close(workbook);  //关闭工作空间
	}
	
	/**
	 * 所有矩阵数据到excel中
	 */
	public static void getWekaTrainDataAll(String txtfilePath, String tagfilePath, String excelPath) throws Exception{
		WritableWorkbook workbook = Workbook.createWorkbook(new File(excelPath));
		WritableSheet sheet = workbook.createSheet("word2vec", 0);
		// 某主题下的所有问答词向量特征及其标签写到excel中
		int row = 0;//excel行
		// 第一行标题
		for(int i = 0; i < 50; i++){
			Label feature = new Label(i, 0, "f" + (i+1));
			sheet.addCell(feature);
		}
		sheet.addCell(new Label(50, 0, "label"));
		
		File[] files = new File(txtfilePath).listFiles();
		for(int s = 0; s < 11; s++){
			String keyword = files[s].getName();
			String txtfilePathKeyword = txtfilePath + "\\" + keyword;
			String tagfilePathKeyword = tagfilePath + "\\" + keyword;
			File[] txts = new File(txtfilePathKeyword).listFiles();
			for(int m = 0; m < txts.length; m++){
				String txtName = txts[m].getName();
				String txtPath = txtfilePathKeyword + "\\" + txtName;
				String tagPath = tagfilePathKeyword + "\\" + txtName;
				String[][] matrix = getWord2VecMatrixbyString(txtPath);
				String tag = DirFile.getStringFromPathFile(tagPath);
				// 内容信息
				for(int i = 0; i < matrix.length; i++){
					row++;
					for(int j = 0; j < matrix[0].length; j++){//写入50维词向量特征数据
						Label feature = new Label(j, row, matrix[i][j]);
						sheet.addCell(feature);
					}
					// 用于weka分类
					if(tag.equals("1")){
						Label tagData = new Label(matrix[0].length, row, "yes");//写入标签
						sheet.addCell(tagData);
					} else {
						Label tagData = new Label(matrix[0].length, row, "no");//写入标签
						sheet.addCell(tagData);
					}
					
				}
			}
		}
		
		ExcelSet.close(workbook);  //关闭工作空间
	}
	
	/**
	 * 所有矩阵数据到excel中
	 */
	public static void getWekaTestDataAll(String txtfilePath, String tagfilePath, String excelPath) throws Exception{
		WritableWorkbook workbook = Workbook.createWorkbook(new File(excelPath));
		WritableSheet sheet = workbook.createSheet("word2vec", 0);
		// 某主题下的所有问答词向量特征及其标签写到excel中
		File[] files = new File(txtfilePath).listFiles();
		int row = 0;//excel行
		// 第一行标题
		for(int i = 0; i < 50; i++){
			Label feature = new Label(i, 0, "f" + (i+1));
			sheet.addCell(feature);
		}
		sheet.addCell(new Label(50, 0, "label"));
		for(int s = 0; s < files.length; s++){
			String keyword = files[s].getName();
			String txtfilePathKeyword = txtfilePath + "\\" + keyword;
			File[] txts = new File(txtfilePathKeyword).listFiles();
			for(int m = 0; m < txts.length; m++){
				String txtName = txts[m].getName();
				String txtPath = txtfilePathKeyword + "\\" + txtName;
				String[][] matrix = getWord2VecMatrixbyString(txtPath);
				// 内容信息
				for(int i = 0; i < matrix.length; i++){
					row++;
					for(int j = 0; j < matrix[0].length; j++){//写入50维词向量特征数据
						Label feature = new Label(j, row, matrix[i][j]);
						sheet.addCell(feature);
					}
				}
			}
		}
		
		ExcelSet.close(workbook);  //关闭工作空间
	}
	
	/**
	 * TXT矩阵写到excel中：某主题下的所有问答词向量特征及其标签写到excel中
	 */
	public static void getWekaTrainData(String filePath, String excelPath) throws Exception{
		WritableWorkbook workbook = Workbook.createWorkbook(new File(excelPath));
		WritableSheet sheet = workbook.createSheet("word2vec", 0);
		// 某主题下的所有问答词向量特征及其标签写到excel中
		int row = 0;//excel行
		// 第一行标题
		for(int i = 0; i < 100; i++){
			Label feature = new Label(i, 0, "f" + (i+1));
			sheet.addCell(feature);
		}
		sheet.addCell(new Label(100, 0, "label"));
		String txtPath = filePath + "\\simMatrix(tile)_train_100_100_random.txt";
		String tagPath = filePath + "\\simMatrix(tile)_tag_100_100_random.txt";
		String[][] txtMatrix = getWord2VecMatrixbyString(txtPath);
		String[][] tagMatrix = getWord2VecMatrixbyString(tagPath);
		// 内容信息
		for(int i = 0; i < txtMatrix.length; i++){
			row++;
			for(int j = 0; j < txtMatrix[0].length; j++){//写入50维词向量特征数据
				Label feature = new Label(j, row, txtMatrix[i][j]);
				sheet.addCell(feature);
			}
			// 用于weka分类
			if(tagMatrix[i].equals("1")){
				Label tagData = new Label(txtMatrix[0].length, row, "yes");//写入标签
				sheet.addCell(tagData);
			} else {
				Label tagData = new Label(txtMatrix[0].length, row, "no");//写入标签
				sheet.addCell(tagData);
			}
			
		}
		
		ExcelSet.close(workbook);  //关闭工作空间
	}
	
	/**
	 *  测试函数
	 */
	public static void test() {
		try {
			long start = System.currentTimeMillis();
			
			// step1：得到每个矩阵文本的行平均值
			new File(outFile).mkdir();
			File[] files = new File(inputFile).listFiles();
			for(int i = 0; i < files.length; i++){
				String keyword = files[i].getName();//主题
				String inputPath = inputFile + "\\" + keyword;
				String outPath = outFile + "\\" + keyword;
				new File(outPath).mkdir();
				File[] txts = new File(inputPath).listFiles();
				for(int j = 0; j < txts.length; j++){
					String txtName = txts[j].getName();//文件名
					String inputFile1 = inputPath + "\\" + txtName;
					String outFile1 = outPath + "\\" + txtName;
					String[] matrixAverage = matrixAveragebyString(inputFile1);
					for(int m = 0; m < matrixAverage.length; m++){
						DirFile.storeString2FileMore(matrixAverage[m] + " ", outFile1);
					}
				}
			}

			// step2：合并所有txt和标签到一个Excel表格中
			new File(outFile2).mkdir();
			File[] files2 = new File(outFile).listFiles();
			for(int i = 0; i < files2.length; i++){
				String keyword = files2[i].getName();
				String txtfilePath = outFile + "\\" + keyword;
				String tagfilePath = tagFile + "\\" + keyword;
				String excelPathTrain = outFile2 + "\\" + keyword + "\\" + keyword + "_train.xls";
				String excelPathTest = outFile2 + "\\" + keyword + "\\" + keyword + "_test.xls";
				new File(outFile2 + "\\" + keyword).mkdir();
				getWekaTrainData(txtfilePath, tagfilePath, excelPathTrain);
				getWekaTestData(txtfilePath, tagfilePath, excelPathTest);
			}
			
			// 所有主题数据存到一个excel中
			String excelPath = outFile2 + "\\" + "All_train_11.xls";
			getWekaTrainDataAll(outFile, tagFile, excelPath);
			
//			deleteFile();
			long end = System.currentTimeMillis();
			System.out.println("生成相似矩阵耗时："+(end-start)/1000+"s");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  测试函数
	 */
	public static void test2() {
		String filePath = "F:\\qualityExtraction\\file6_dataCNN(tile)\\simMatrix(tile)";
		String excelPath = filePath + "simMatrix(tile)_train_100_100_random.xls";
		try {
			getWekaTrainData(filePath, excelPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		test();
		test2();
	}

}
