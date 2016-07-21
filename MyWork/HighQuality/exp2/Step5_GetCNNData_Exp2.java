package exp2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import base.DirFile;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import theano.RandomData;
import util.FileUtil;

public class Step5_GetCNNData_Exp2 {

	private static String[] tagZ = { "algorithm", "application","definition", "example",
		"history", "implementation", "method", "operation", "feature", "type" };// 以前的标签
	private static String[] tag10 = { "algorithm", "application","definition", "example",
		"feature", "history", "implementation", "method", "operation", "type" };// 现在的标签
	
	public static void mikmydir(String path){
		if(!(new File(path).exists())){
			new File(path).mkdir();
		}
	}
	
	public static void readTxtFile(String filePath){
		try{
			String encoding = "UTF-8";
			File file = new File(filePath);
			if(file.isFile() && file.exists()){ //判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while((lineTxt = bufferedReader.readLine()) != null){
					System.out.println(lineTxt);
				}
				read.close();
			}else{
				System.out.println("找不到指定文件");
			}
		}catch(Exception e){
			System.out.println("读取文件出错");
			e.printStackTrace();
		}
	}
	
	/**
	 * 实现功能：建立工作表，输入是表格存储路径filepath，输出是表格对象WritableSheet
	 * @param filepath
	 */
	public static void getLabelOnlyQuora(String filepath, String dir, String targetDir) throws Exception {
		
		// 匹配所有目标文本名是否与源文件名相似
		File[] fs = new File(filepath).listFiles();
		for(int i = 0; i < fs.length; i++){
			String name = fs[i].getName();
			String tagtxtPath = filepath + "\\" + name;
			String tag = DirFile.getStringFromPathFile(tagtxtPath);// 得到标签内容
			File[] dirf = new File(dir).listFiles();
			for(int j = 0; j < dirf.length; j++){
				String fileName = dirf[j].getName();
				String targetPath = targetDir + "\\" + fileName;
				if(fileName.contains(name.substring(0, name.indexOf(".txt")))){// 目标文件名包含标签文件名
					File targetFile = new File(targetPath);
					FileWriter output = new FileWriter(targetFile);
					output.write(tag);
					output.close();
				}
			}
		}
		
		
		
//		Workbook workbook = Workbook.getWorkbook(new File(filepath));
//		Sheet sheet = workbook.getSheet(0);
//		int rows = sheet.getRows();
//		
////		String dir = "F:\\word2vec\\array_new_matrix_Last";// 每个文本所在文件夹的路径
//		File[] f = new File(dir).listFiles();
//		for(int i = 0; i < f.length; i++){
//			String fileName = f[i].getName();
//			// 得到每个文件的标签
//			String txtName = fileName.substring(0, fileName.indexOf(".txt"));
//			int tag = 0;
//			for(int j = 0; j < rows; j++){
//				String txt = sheet.getCell(0, j).getContents();
//				if(txtName.equals(txt)){
//					tag = Integer.parseInt(sheet.getCell(13, j).getContents());
//				}
//			}
//			// 生成与文件行数相同的标签数
//			mikmydir(targetDir);
//			String targetFileName = targetDir + "\\" + fileName;
//			File targetFile = new File(targetFileName);
//			FileWriter output = new FileWriter(targetFile);
//			output.write(tag + "");
//			output.close();
//		}
	}
	
	// 将源数据中的456*112的矩阵数据存成一行数据
	public static void getSourceOneLine(String inputPath, String targetPath) throws Exception {
//		String inputPath = "F:\\word2vec\\array_new_matrix_similarityMatrix_456×112_tile";
//		String targetPath = "F:\\word2vec\\array_new_matrix_similarityMatrix_456×112_tile_one";
		mikmydir(targetPath);
		File[] labelfiles = new File(inputPath).listFiles();
		for(int i = 0; i < labelfiles.length; i++){
			StringBuffer strNew = new StringBuffer();
			String str = new String();
			String labelName = labelfiles[i].getName();
			File sourceFile = new File(inputPath + "\\" + labelName);
			File targetFile = new File(targetPath + "\\" + labelName);
			if(!targetFile.exists()){
				targetFile.createNewFile();
			}

			// 读取原来内容
			System.out.println("--------开始读文件----------");
			System.out.println("读取文件：" + inputPath + "\\" + labelName);
			BufferedReader input = new BufferedReader(new FileReader(sourceFile));
			while ((str = input.readLine()) != null) {
				strNew.append(str);
			}
			input.close();
			System.out.println("--------读取完毕-----------");

			// 写文件
			System.out.println("--------开始写文件----------");
			System.out.println("写文件：" + targetPath + "\\" + labelName);
			FileWriter output = new FileWriter(targetFile);// 在原有基础上写
			output.write(strNew.toString());
			output.close();
			System.out.println("--------更新完毕-----------");
		}
	}
	
	// 在Text原有内容中写入新的内容，不覆盖原来内容（将所有数据写到一个文件中）
	public static void writeFile(String inputLabelPath, String target) throws Exception {
		File targetFile = new File(target);
		if(!targetFile.exists()){
			targetFile.createNewFile();
		}
		File[] labelfiles = new File(inputLabelPath).listFiles();
		for(int i = 0; i < labelfiles.length; i++){
			StringBuffer strNew = new StringBuffer();
			String str = new String();
			String labelName = labelfiles[i].getName();
			File sourceFile = new File(inputLabelPath + "\\" + labelName);
			System.out.println("读取文件：" + labelName);

			// 读取原来内容
			System.out.println("--------开始读文件----------");
			BufferedReader input = new BufferedReader(new FileReader(sourceFile));
			while ((str = input.readLine()) != null) {
				strNew.append(str + "\n");
			}

			input.close();
			System.out.println("--------读取完毕-----------");

			// 写文件
			System.out.println("--------开始写文件----------");
			FileWriter output = new FileWriter(targetFile, true);// 在原有基础上写
			output.write(strNew.toString());
			output.close();
			System.out.println("--------更新完毕-----------");
		}
	}
	
	// 合并标签和数据到一个文件夹中
	public static void getData(String dataPath, String tagPath, String targetPath){
//		String dataPath = "F:\\word2vec\\data_structure_tag_txt\\file5_data_step2(tile)";
//		String tagPath = "F:\\word2vec\\data_structure_tag_txt\\file5_tag_step2(tile)";
//		String targetPath = "F:\\word2vec\\data_structure_tag_txt\\file6_dataCNN(tile)";
		new File(targetPath).mkdir();
		File[] files = new File(dataPath).listFiles();
		for(int i = 0; i < files.length; i++){
			String keyword = files[i].getName();
			String inputDataPath = dataPath + "\\" + keyword + "\\" + keyword + "_train_100_100.txt";
			String inputTagPath = tagPath + "\\" + keyword + "\\" + keyword + "_tag_100_100.txt";
			new File(targetPath + "\\" + keyword).mkdir();
			//拷贝到多个文件夹（按照主题不同）
			String outputDataPath = targetPath + "\\" + keyword + "\\" + keyword + "_train_100_100.txt";
			String outputTagPath = targetPath + "\\" + keyword + "\\" + keyword + "_tag_100_100.txt";
			FileUtil.copyFile(inputDataPath, outputDataPath);
			FileUtil.copyFile(inputTagPath, outputTagPath);
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
		int[] index = Step5_GetCNNData_Exp2.genRandomData(labelfiles.length);
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
		//			System.out.println(index.length);
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
	
	// 合并标签和数据到一个文件夹中
	public static void getDataAll(String dataPath, String tagPath, String targetPath){
//		String dataPath = "F:\\word2vec\\data_structure_tag_txt\\file5_data_step2(tile)";
//		String tagPath = "F:\\word2vec\\data_structure_tag_txt\\file5_tag_step2(tile)";
//		String targetPath = "F:\\word2vec\\data_structure_tag_txt\\file6_dataCNN(tile)";
		new File(targetPath).mkdir();
		File[] files = new File(dataPath).listFiles();
		for(int i = 0; i < files.length; i++){
			String keyword = files[i].getName();
			String inputDataPath = dataPath + "\\" + keyword + "\\" + keyword + "_train_100_50.txt";
			String inputTagPath = tagPath + "\\" + keyword + "\\" + keyword + "_tag_100_50.txt";
			//拷贝到一个文件夹下
			new File(targetPath + "\\data").mkdir();
			new File(targetPath + "\\tag").mkdir();
			String outputDataPath = targetPath + "\\data\\" + keyword + "_train_100_50.txt";
			String outputTagPath = targetPath + "\\tag\\" + keyword + "_tag_100_50.txt";
			FileUtil.copyFile(inputDataPath, outputDataPath);
			FileUtil.copyFile(inputTagPath, outputTagPath);
		}
	}
	
	public static void getDataRandom() throws Exception{
		// 随机将数据写到一个文件中
		String path = "F:\\qualityExtraction";
		String inTagPath = path + "\\file5_tag_step1(tile)";
		String inTxtPath = path + "\\file5_data_step1(tile)";
		String outTagPath = path + "\\file6_dataCNN(tile)";
		new File(outTagPath).mkdir();
		File[] inTagFiles = new File(inTagPath).listFiles();
		for(int i = 0; i < inTagFiles.length; i++){
			String keyword = inTagFiles[i].getName();
			String sourcePathTag = inTagPath + "\\" + keyword;//所有标签文本路径
			String sourcePathTxt = inTxtPath + "\\" + keyword;//所有标签文本路径
			String targetPathTag = outTagPath + "\\" + keyword + "\\" + keyword + "_tag_100_100_random.txt";
			String targetPathTxt = outTagPath + "\\" + keyword + "\\" + keyword + "_train_100_100_random.txt";
			new File(outTagPath + "\\" + keyword).mkdir();
			writeFileRandom(targetPathTxt, targetPathTag, sourcePathTxt, sourcePathTag);
		}
	}
	
	private static String cata = "02-CQA网站中问题答案质量评估";
	public static void dealDataOnlyQuora(){
//		readTxtFile();
		try {
			String path = "F:\\" + cata + "qualityExtraction";
			
			// step1：将所有数据写到一行中并保存到文件夹
			String inputpath = path + "\\file4_matrixdeal_step2(tile)";
			String outputpath = path + "\\file5_data_step1(tile)";
			new File(outputpath).mkdir();
			File[] files = new File(inputpath).listFiles();
			for(int i = 0; i < files.length; i++){
				String keyword = files[i].getName();
				String sourcePath = inputpath + "\\" + keyword;
				String targetPath = outputpath + "\\" + keyword;
				new File(targetPath).mkdir();
				getSourceOneLine(sourcePath, targetPath);
			}
			
			// step2：将所有数据写到一个文件中（每个关键词 一个文件总汇）
			String inPath = path + "\\file5_data_step1(tile)";
			String outPath = path + "\\file5_data_step2(tile)";
			new File(outPath).mkdir();
			File[] inFiles = new File(inPath).listFiles();
			for(int i = 0; i < inFiles.length; i++){
				String keyword = inFiles[i].getName();
				String sourcePath = inPath + "\\" + keyword;
				String targetPath = outPath + "\\" + keyword + "\\" + keyword + "_train_100_100.txt";
				new File(outPath + "\\" + keyword).mkdir();
				writeFile(sourcePath, targetPath);
			}
			
			// step3：生成每个文本的标签
			String excelPath = "F:\\qualityExtraction\\quora\\selectQATag";
			String dataPath = path + "\\file4_matrixdeal_step2(tile)";
			String tagPath = path + "\\file5_tag_step1(tile)";
			new File(tagPath).mkdir();
			String dataFile = dataPath + "\\simMatrix(tile)";
			String tagFile = tagPath + "\\simMatrix(tile)";
			new File(tagFile).mkdir();
			getLabelOnlyQuora(excelPath, dataFile, tagFile);
			
			// step4：将所有数据写到一个文件中（每个关键词 一个文件总汇）（标签）
			String inTagPath = path + "\\file5_tag_step1(tile)";
			String outTagPath = path + "\\file5_tag_step2(tile)";
			new File(outTagPath).mkdir();
			File[] inTagFiles = new File(inTagPath).listFiles();
			for(int i = 0; i < inTagFiles.length; i++){
				String keyword = inTagFiles[i].getName();
				String sourcePath = inTagPath + "\\" + keyword;
				String targetPath = outTagPath + "\\" + keyword + "\\" + keyword + "_tag_100_100.txt";
				new File(outTagPath + "\\" + keyword).mkdir();
				writeFile(sourcePath, targetPath);
			}
			
			// step5：合并标签和数据到一个文件中，按随机的方式存储数据
			getDataRandom();
			
			// step5：合并标签和数据到一个文件夹中(按主题和按数据)
			String dataPath1 = path + "\\file5_data_step2(tile)";
			String tagPath1 = path + "\\file5_tag_step2(tile)";
			String targetPath = path + "\\file6_dataCNN(tile)";
			getData(dataPath1, tagPath1, targetPath);
//			String targetPath2 = path + "\\file6_dataCNN2(tile)";
//			getDataAll(dataPath1, tagPath1, targetPath2);
			
			// step6：合并所有主题的数据和标签
//			String sourcePathData = path + "\\file6_dataCNN2(tile)\\data";
//			String targetPathData = path + "\\file6_dataCNN2(tile)\\All_train_100_100.txt";
//			writeFile(sourcePathData, targetPathData);
//			String sourcePathTag = path + "\\file6_dataCNN2(tile)\\tag";
//			String targetPathTag = path + "\\file6_dataCNN2(tile)\\All_tag_100_100.txt";
//			writeFile(sourcePathTag, targetPathTag);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	public static void main(String[] args) {
		dealDataOnlyQuora();
	}

}
