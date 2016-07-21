package word2vec.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class DealTxt {
	
	public static void readTxtFile(String filePath){
		try{
			String encoding = "UTF-8";
			File file = new File(filePath);
			 if(file.isFile() && file.exists()){ //判断文件是否存在
                 InputStreamReader read = new InputStreamReader(
                 new FileInputStream(file),encoding);//考虑到编码格式
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
	
	// 读取txt文件第lineNumber行的数据
	// 必须从第一行开始读
	private static String readLine(int lineNumber,BufferedReader reader)throws Exception{
//		lineNumber=lineNumber+1;
		String line="";
		int i=0;
		while(i<lineNumber){
			line=reader.readLine();
			i++;
		}
		return line;
	}
	
	public static void test() {
		String path = "F:\\word2vec\\quora_Data\\word2vec_quora\\Absolute+deviation0.txt";
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(path));
			String line=readLine(7,reader);
			System.out.println(line);
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 处理一个文件的词向量
	// 去除该词向量文件每行的第一个词和空格，使得文件只包含向量，而没有单词。
	public static void removeFirstWord() throws Exception{
		String path = "F:\\word2vec\\quora_Data\\word2vec_quora\\Absolute+deviation0.txt";
		String path2 = "F:\\word2vec\\quora_Data\\word2vec只包含向量_quora\\Absolute+deviation0.txt";
		BufferedReader bufReader = new BufferedReader(new FileReader(path));
		BufferedWriter bufwrite = new BufferedWriter(new FileWriter(path2));
		String input = null;
        while((input = bufReader.readLine())!=null)
		{
        	String string=input;
        	string=string.substring(string.indexOf(" ")+1);
        	bufwrite.write(string);
        	bufwrite.newLine();
		}
		bufReader.close();
		bufwrite.close();
		System.out.println("done");
	}
	
	// 处理多个文件
	public static void removeFirstWordAll(String inputPath, String outputPath) throws Exception{
		File[] files = (new File(inputPath)).listFiles();
		for (int i = 0; i < files.length; i++) {
			String inputFilepath = inputPath + "\\" + files[i].getName();
			String outputFilepath = outputPath + "\\" + files[i].getName();
			BufferedReader bufReader = new BufferedReader(new FileReader(inputFilepath));
			BufferedWriter bufwrite = new BufferedWriter(new FileWriter(outputFilepath));
			String input = null;
			while ((input = bufReader.readLine()) != null) {
				String string = input;
				string = string.substring(string.indexOf(" ") + 1);
				bufwrite.write(string);
				bufwrite.newLine();
			}
			bufReader.close();
			bufwrite.close();
			System.out.println("i:" + i + "   " + files[i].getName());
			System.out.println("done");
		}
	}
	
	// 去掉txt文件的空行
	public static void deleteNullLine(String inputFile, String outFile){
		BufferedReader bre = null;
		BufferedWriter bufwrite = null;
		String str = null;
		try {
			bre = new BufferedReader(new FileReader(inputFile));
			bufwrite = new BufferedWriter(new FileWriter(outFile));
			while ((str = bre.readLine())!= null){// 判断最后一行不存在，为空结束循环
				if(!str.trim().equals("")){// 如果判断此行是空的，输出结果
					bufwrite.write(str);
					bufwrite.newLine();
				} else {
					System.out.println("此行是空行...");
				}
			}
			bre.close();
			bufwrite.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 删除所有文件的空行
	public static void deleteNullLine(){
		String inputPath = "F:\\word2vec\\quora_Data\\word2vec只包含向量_quora";
		String outPath = "F:\\word2vec\\quora_Data\\word2vec只包含向量且去除空行_quora";
		File[] inputFiles = (new File(inputPath)).listFiles();
		for(int i = 0; i < inputFiles.length; i++){
			String fileName = inputFiles[i].getName();
			String inputFile = inputPath + "/" + fileName;
			String outFile = outPath + "/" + fileName;
			deleteNullLine(inputFile, outFile);
		}
	}
	
	// 测试删除空行
	public static void testDeleteNullLine() {
//		String inputFile = "F:\\word2vec\\quora_Data\\word2vec只包含向量_quora\\Absolute+deviation1.txt";
//		String outFile = "F:\\word2vec\\quora_Data\\word2vec_quoraData\\Absolute+deviation1.txt";
//		deleteNullLine(inputFile, outFile);
		deleteNullLine();
	}
	
	// 测试去除首字母
	public static void testRemoveFirstWord() throws Exception {
		String intputPath = "F:\\word2vec\\quora_Data\\word2vec_quora";
		String outputPath = "F:\\word2vec\\quora_Data\\word2vec只包含向量_quora";
		removeFirstWordAll(intputPath,outputPath);
	}
	
	public static void main(String argv[]) throws Exception{
		testDeleteNullLine();
//		testRemoveFirstWord();
	}

}
