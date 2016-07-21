package exp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import util.FileUtil;

public class Step5_GetCNNData {

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
		Workbook workbook = Workbook.getWorkbook(new File(filepath));
		Sheet sheet = workbook.getSheet(0);
		int rows = sheet.getRows();
		
//		String dir = "F:\\word2vec\\array_new_matrix_Last";// 每个文本所在文件夹的路径
		File[] f = new File(dir).listFiles();
		for(int i = 0; i < f.length; i++){
			String fileName = f[i].getName();
			// 得到每个文件的标签
			String txtName = fileName.substring(0, fileName.indexOf(".txt"));
			int tag = 0;
			for(int j = 0; j < rows; j++){
				String txt = sheet.getCell(0, j).getContents();
				if(txtName.equals(txt)){
					tag = Integer.parseInt(sheet.getCell(13, j).getContents());
				}
			}
			// 生成与文件行数相同的标签数
			mikmydir(targetDir);
			String targetFileName = targetDir + "\\" + fileName;
			File targetFile = new File(targetFileName);
			FileWriter output = new FileWriter(targetFile);
			output.write(tag + "");
			output.close();
		}
	}
	
	/**
	 * 实现功能：建立工作表，输入是表格存储路径filepath，输出是表格对象WritableSheet
	 * @param filepath
	 */
	public static void getLabel(String filepath) throws Exception {
		Workbook workbook = Workbook.getWorkbook(new File(filepath));
		Sheet sheet = workbook.getSheet(0);
		int rows = sheet.getRows();
//		int columns = sheet.getColumns();
		
		String dir = "F:\\word2vec\\array_new_matrix_Last";
		File[] f = new File(dir).listFiles();
		for(int i = 0; i < f.length; i++){
			String fileName = f[i].getName();
			// 得到每个文件的标签
			String txtName = fileName.substring(0, fileName.indexOf(".txt"));
			int tag = 0;
			for(int j = 0; j < rows; j++){
				Cell cell = sheet.getCell(0, j);
				String txt = cell.getContents();
				if(txtName.equals(txt)){
					for(int z = 0; z < tagZ.length; z++){
						if(sheet.getCell(1, j).getContents().equals(tagZ[z])){
							tag = z;
							System.out.println(tag);
						}
					}
				}
			}
			String filePath = dir + "\\" + fileName;
			StringBuffer writeTxt = new StringBuffer();
			try{
				String encoding = "UTF-8";
				File file = new File(filePath);
				if(file.isFile() && file.exists()){ //判断文件是否存在
					InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//考虑到编码格式
					BufferedReader bufferedReader = new BufferedReader(read);
					@SuppressWarnings("unused")
					String lineTxt = null;
					while((lineTxt = bufferedReader.readLine()) != null){
						writeTxt.append(tag+"\n");
						System.out.println(tag);
//						System.out.println(lineTxt);
					}
					read.close();
				}else{
					System.out.println("找不到指定文件");
				}
			}catch(Exception e){
				System.out.println("读取文件出错");
				e.printStackTrace();
			}
//			System.out.println("txtName:"+txtName);
			// 生成与文件行数相同的标签数
			mikmydir("F:\\word2vec\\array_new_matrix_Last_Tag");
			String targetFileName = "F:\\word2vec\\array_new_matrix_Last_Tag\\"+fileName;
			File targetFile = new File(targetFileName);
			FileWriter output = new FileWriter(targetFile);
			output.write(writeTxt.toString());
			output.close();
		}
	}
	
	/**
	 * 实现功能：建立工作表，输入是表格存储路径filepath，输出是表格对象WritableSheet
	 * @param filepath
	 */
	public static void getLabel1550(String filepath, String targetPath) throws Exception {
		Workbook workbook = Workbook.getWorkbook(new File(filepath));
		Sheet sheet = workbook.getSheet(0);
		int rows = sheet.getRows();
//		System.out.println("rows："+rows);
		// 生成与文件行数相同的标签数
		mikmydir(targetPath);
//		mikmydir("F:\\word2vec\\array_new_matrix__Label_1550");
		
		for(int i = 0; i < rows; i++){
			String keyword = sheet.getCell(0, i).getContents();
			String pTag1 = sheet.getCell(1, i).getContents();
			String pTag2 = sheet.getCell(2, i).getContents();
			String pTag3 = sheet.getCell(3, i).getContents();
//			System.out.println(keyword);
			for(int j = 0; j < 10; j++){
//				System.out.println("tagZ:"+tag10[j]);
				String fileName = keyword + tag10[j] + ".txt";
				String targetFileName = targetPath+"\\"+fileName;
				System.out.println("filename:"+targetFileName);
				File targetFile = new File(targetFileName);
				FileWriter output = new FileWriter(targetFile);
				// 判断标签和人工标注的标签是否相同
				int flag = 0;
				if(tag10[j].equals(pTag1)||tag10[j].equals(pTag2)||tag10[j].equals(pTag3)){
					flag = 1;
				}
				System.out.println("flag: "+flag);
				output.write(flag+"");
				output.close();
			}
		}
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
			String inputDataPath = dataPath + "\\" + keyword + "\\" + keyword + "_train_100_50.txt";
			String inputTagPath = tagPath + "\\" + keyword + "\\" + keyword + "_tag_100_50.txt";
			new File(targetPath + "\\" + keyword).mkdir();
			//拷贝到多个文件夹（按照主题不同）
			String outputDataPath = targetPath + "\\" + keyword + "\\" + keyword + "_train_100_50.txt";
			String outputTagPath = targetPath + "\\" + keyword + "\\" + keyword + "_tag_100_50.txt";
			FileUtil.copyFile(inputDataPath, outputDataPath);
			FileUtil.copyFile(inputTagPath, outputTagPath);
		}
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
	
	private static String cata = "02-CQA网站中问题答案质量评估";
	
	public static void dealDataOnlyQuora(){
//		readTxtFile();
		try {
			
//			String path = "F:\\"+cata+"\\数据结构标注20主题_Step2\\txt1Tokenizer";
//			String path = "F:\\"+cata+"\\数据结构标注20主题_Step2\\txt2Tokenizer";
//			String path = "F:\\"+cata+"\\数据结构标注20主题_Step2\\txt3Tokenizer";
			String path = "F:\\"+cata+"\\数据结构标注20主题_Step3\\txt1Tokenizer";
//			String path = "F:\\"+cata+"\\数据结构标注20主题_Step3\\txt2Tokenizer";
//			String path = "F:\\"+cata+"\\数据结构标注20主题_Step3\\txt3Tokenizer";
			
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
				String targetPath = outPath + "\\" + keyword + "\\" + keyword + "_train_100_50.txt";
				new File(outPath + "\\" + keyword).mkdir();
				writeFile(sourcePath, targetPath);
			}
			
			// step3：生成每个文本的标签
			String excelPath = "F:\\"+cata+"\\数据结构标注20主题";
			String dataPath = path + "\\file4_matrixdeal_step2(tile)";
			String tagPath = path + "\\file5_tag_step1(tile)";
			new File(tagPath).mkdir();
			File[] excelFiles = new File(excelPath).listFiles();
			for(int i = 0; i < excelFiles.length; i++){
				String keyword = excelFiles[i].getName();
				String excelFile = excelPath + "\\" + keyword + "\\" + keyword + "-tag_filtering.xls";
				String dataFile = dataPath + "\\" + keyword;
				String tagFile = tagPath + "\\" + keyword;
				new File(tagFile).listFiles();
				getLabelOnlyQuora(excelFile, dataFile, tagFile);
			}
			
			// step4：将所有数据写到一个文件中（每个关键词 一个文件总汇）（标签）
			String inTagPath = path + "\\file5_tag_step1(tile)";
			String outTagPath = path + "\\file5_tag_step2(tile)";
			new File(outTagPath).mkdir();
			File[] inTagFiles = new File(inTagPath).listFiles();
			for(int i = 0; i < inTagFiles.length; i++){
				String keyword = inTagFiles[i].getName();
				String sourcePath = inTagPath + "\\" + keyword;
				String targetPath = outTagPath + "\\" + keyword + "\\" + keyword + "_tag_100_50.txt";
				new File(outTagPath + "\\" + keyword).mkdir();
				writeFile(sourcePath, targetPath);
			}

			// step5：合并标签和数据到一个文件夹中(按主题和按数据)
			String dataPath1 = path + "\\file5_data_step2(tile)";
			String tagPath1 = path + "\\file5_tag_step2(tile)";
			String targetPath = path + "\\file6_dataCNN(tile)";
			String targetPath2 = path + "\\file6_dataCNN2(tile)";
			getData(dataPath1, tagPath1, targetPath);
			getDataAll(dataPath1, tagPath1, targetPath2);
			
			// step6：合并所有主题的数据和标签
			String sourcePathData = path + "\\file6_dataCNN2(tile)\\data";
			String targetPathData = path + "\\file6_dataCNN2(tile)\\All_train_100_50.txt";
			writeFile(sourcePathData, targetPathData);
			String sourcePathTag = path + "\\file6_dataCNN2(tile)\\tag";
			String targetPathTag = path + "\\file6_dataCNN2(tile)\\All_tag_100_50.txt";
			writeFile(sourcePathTag, targetPathTag);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	public static void main(String[] args) {
		dealDataOnlyQuora();
	}

}
