package theano;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class DealData {

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
	public static void getSourceOneLine(String targetPath, String inputPath) throws Exception {
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
	
	// 在Text原有内容中写入新的内容，不覆盖原来内容
	public static void writeFile(String target, String inputLabelPath) throws Exception {
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
	
	public static void dealData(){
//		readTxtFile();
		try {
			// 旧数据
//			getLabel("F:\\word2vec\\Array-tag2-new.xls");
			
//			String target = "F:\\word2vec\\Array_train.txt";
//			String inputLabelPath = "F:\\word2vec\\array_new_matrix_Last";
//			writeFile(target, inputLabelPath);
//			
//			String target1 = "F:\\word2vec\\Array_tag.txt";
//			String inputLabelPath1 = "F:\\word2vec\\array_new_matrix_Last_Tag";
//			writeFile(target1, inputLabelPath1);
			
			// 新数据
//			String filepath = "F:\\word2vec\\Array-tag2-new.xls";
////			String sourcePath = "F:\\word2vec\\array_new_matrix_similarityMatrix_456×112_tile";
//			String targetPath = "F:\\word2vec\\array_new_matrix_similarityMatrix_Label_1550";
//			getLabel1550(filepath, targetPath);
			
//			String targetpath = "F:\\word2vec\\array_new_matrix_similarityMatrix_456×112_tile_one";
//			String inputpath = "F:\\word2vec\\array_new_matrix_similarityMatrix_456×112_tile";
//			getSourceOneLine(targetpath, inputpath);
			
			String target = "F:\\word2vec\\array_new_matrix_similarityMatrix_CNN\\Array_train_456_112.txt";
			String inputLabelPath = "F:\\word2vec\\array_new_matrix_similarityMatrix_456×112_tile_one";
			writeFile(target, inputLabelPath);
			
			String target1 = "F:\\word2vec\\array_new_matrix_similarityMatrix_CNN\\Array_tag_456_112.txt";
			String inputLabelPath1 = "F:\\word2vec\\array_new_matrix_similarityMatrix_Label_1550";
			writeFile(target1, inputLabelPath1);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 测试一行有多少数据
	public static void test(){
		try{
			String path = "F:\\word2vec\\array_new_matrix_similarityMatrix_456×112_tile_one\\Array0_1algorithm.txt";
			File file = new File(path);
			BufferedReader input = new BufferedReader(new FileReader(file));
			String str = input.readLine();
			String[] strA = str.split(" ");
			System.out.println("strA: "+strA.length);
		} catch(Exception e){
			System.err.println(".....");
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		dealData();
//		test();
	}

}
