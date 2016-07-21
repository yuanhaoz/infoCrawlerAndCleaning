package word2vec.utils;
/*
 * 输入：利用wiki训练得到的word2vec词向量模型(wiki.en.text.vector)
 * 		需要生成词向量的文本(txt)（Quora标注的数据或Wiki的标签数据）
 * 方法：读取word2vec词向量模型，将其存到数组里面。
 * 		读取新文件的每个单词，匹配词向量模型中的词向量，得到每个文本中每个词的词向量。
 * 输出：每个文本对应的词向量矩阵（txt文件形式）
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import base.DirFile;

public class TextMatrices {

	// 读取txt具体某一行
	private static String readLine(int lineNumber, BufferedReader reader) throws Exception {
		lineNumber = lineNumber + 1;
		String line = "";
		int i = 0;
		while (i < lineNumber) {
			line = reader.readLine();
			i++;
		}
		return line;
	}

	public static void Match(String filePath, String inputCatalog, String outputCatalog) throws Exception {
		String encoding = "UTF-8";
		File file = new File(filePath);
		File fileCatalog = new File(inputCatalog);
		File[] files = fileCatalog.listFiles();
		String filename = null;
		String inputFilepath = null;
		String outputFilepath = null;
		// String str = "";
		// int rowNum = 0;

		// String[] string1 = new String[3000000];
		ArrayList<String> string1 = new ArrayList<String>();
		// BufferedReader bufferedReader = new BufferedReader(new
		// FileReader(filePath));
		long start = System.currentTimeMillis();
		InputStreamReader read1 = new InputStreamReader(new FileInputStream(file), encoding);
		BufferedReader bufferedReader1 = new BufferedReader(read1);
		String lineTxt1 = null;
		while ((lineTxt1 = bufferedReader1.readLine()) != null) {

			String[] split1 = lineTxt1.split(" ");
			// for(String string:split1){
			string1.add(split1[0]);
			// System.out.println(split1[0]);
			// }

		}
		long end = System.currentTimeMillis();
		System.out.println("读取word2vec模型耗时："+(end-start)/1000+"s");
		long start1 = System.currentTimeMillis();
		for (int i = 0; i < files.length; i++) {
			long start2 = System.currentTimeMillis();
			filename = files[i].getName();
			inputFilepath = inputCatalog + filename;
			outputFilepath = outputCatalog + filename;
			@SuppressWarnings("resource")
			FileWriter outfile = new FileWriter(outputFilepath);
			try {

				if (file.isFile() && file.exists()) { // 判断文件是否存在
					InputStreamReader read2 = new InputStreamReader(new FileInputStream(inputFilepath), encoding);
					BufferedReader bufferedReader2 = new BufferedReader(read2);
					// while ((bufferedReader1.readLine()) != null) {
					// rowNum++;
					// }

					String lineTxt2 = null;
					// int rowNum=0;

					while ((lineTxt2 = bufferedReader2.readLine()) != null) {
						String[] split2 = lineTxt2.split(" ");
						for (String string : split2) {
							int index = string1.indexOf(string);
							// System.out.println(index+1);
							InputStreamReader read3 = new InputStreamReader(new FileInputStream(file), encoding);
							BufferedReader bufferedReader11 = new BufferedReader(read3);
							String lineVec = readLine(index, bufferedReader11);
							// System.out.println("line:" + lineVec);
							outfile.write(lineVec + "\n");
							// System.out.println(index);
						}

					}

					// System.out.println(string1.size());
					read1.close();
					read2.close();
				} else {
					System.out.println("找不到指定文件");
				}
			} catch (Exception e) {
				System.out.println("读取文件出错");
				e.printStackTrace();
			}
			long end2 = System.currentTimeMillis();
			System.out.println(filename+"文件生成词向量耗时："+(end2-start2)/1000+"s");
		}
		long end1 = System.currentTimeMillis();
		System.out.println("所有文件生成词向量耗时："+(end1-start1)/1000+"s");
	}

	@SuppressWarnings("resource")
	public static void MatchSingle(String filePath, String inputCatalog, String outputCatalog) throws Exception {
		String encoding = "UTF-8";
		File file = new File(filePath);
		// File fileCatalog = new File(inputCatalog);
		// File[] files = fileCatalog.listFiles();
		String filename = null;
		String inputFilepath = null;
		String outputFilepath = null;
		// String str = "";
		// int rowNum = 0;

		// String[] string1 = new String[3000000];
		ArrayList<String> string1 = new ArrayList<String>();
		// BufferedReader bufferedReader = new BufferedReader(new
		// FileReader(filePath));
		// for (int i = 0; i < files.length; i++) {
		// filename = files[i].getName();
		filename = "Absolute+deviation0.txt";
		inputFilepath = inputCatalog + filename;
		outputFilepath = outputCatalog + filename;
		FileWriter outfile = new FileWriter(outputFilepath);
		try {

			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read1 = new InputStreamReader(new FileInputStream(file), encoding);
				InputStreamReader read2 = new InputStreamReader(new FileInputStream(inputFilepath), encoding);
				BufferedReader bufferedReader1 = new BufferedReader(read1);
				BufferedReader bufferedReader2 = new BufferedReader(read2);
				// while ((bufferedReader1.readLine()) != null) {
				// rowNum++;
				// }
				String lineTxt1 = null;
				String lineTxt2 = null;
				// int rowNum=0;
				while ((lineTxt1 = bufferedReader1.readLine()) != null) {

					String[] split1 = lineTxt1.split(" ");
					// for(String string:split1){
					string1.add(split1[0]);
					// System.out.println(split1[0]);
					// }

				}
				while ((lineTxt2 = bufferedReader2.readLine()) != null) {
					String[] split2 = lineTxt2.split(" ");
					for (String string : split2) {
						int index = string1.indexOf(string);
						// System.out.println(index+1);
						InputStreamReader read3 = new InputStreamReader(new FileInputStream(file), encoding);
						BufferedReader bufferedReader11 = new BufferedReader(read3);
						String lineVec = readLine(index, bufferedReader11);
						// System.out.println("line:" + lineVec);
						outfile.write(lineVec + "\n");
						// System.out.println(index);
					}

				}

				// System.out.println(string1.size());
				read1.close();
				read2.close();
			} else {	
				System.out.println("找不到指定文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件出错");
			e.printStackTrace();
		}
		// }
	}
	
	public static void MatchLastLineSingle(String filePath, String inputCatalog, String outputCatalog) throws Exception {
		String encoding = "UTF-8";
		File file = new File(filePath);
		// File fileCatalog = new File(inputCatalog);
		// File[] files = fileCatalog.listFiles();
		String filename = null;
		String inputFilepath = null;
		String outputFilepath = null;
		// String str = "";
		// int rowNum = 0;

		// String[] string1 = new String[3000000];
		ArrayList<String> string1 = new ArrayList<String>();
		// BufferedReader bufferedReader = new BufferedReader(new
		// FileReader(filePath));
		// for (int i = 0; i < files.length; i++) {
		// filename = files[i].getName();
		filename = "Absolute+deviation0.txt";
		inputFilepath = inputCatalog + filename;
		outputFilepath = outputCatalog + filename;
		FileWriter outfile = new FileWriter(outputFilepath);
		try {

			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read1 = new InputStreamReader(new FileInputStream(file), encoding);
				InputStreamReader read2 = new InputStreamReader(new FileInputStream(inputFilepath), encoding);
				BufferedReader bufferedReader1 = new BufferedReader(read1);
				BufferedReader bufferedReader2 = new BufferedReader(read2);
				// while ((bufferedReader1.readLine()) != null) {
				// rowNum++;
				// }
				String lineTxt1 = null;
				String lineTxt2 = null;
				// int rowNum=0;
				while ((lineTxt1 = bufferedReader1.readLine()) != null) {

					String[] split1 = lineTxt1.split(" ");
					// for(String string:split1){
					string1.add(split1[0]);
					// System.out.println(split1[0]);
					// }

				}
				while ((lineTxt2 = bufferedReader2.readLine()) != null) {
					String[] split2 = lineTxt2.split(" ");
					for (String string : split2) {
						int index = string1.indexOf(string);
						// System.out.println(index+1);
						InputStreamReader read3 = new InputStreamReader(new FileInputStream(file), encoding);
						BufferedReader bufferedReader11 = new BufferedReader(read3);
						String lineVec = readLine(index, bufferedReader11);
						// System.out.println("line:" + lineVec);
						outfile.write(lineVec + "\n");
						// System.out.println(index);
					}

				}

				// System.out.println(string1.size());
				read1.close();
				read2.close();
			} else {
				System.out.println("找不到指定文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件出错");
			e.printStackTrace();
		}
		// }
	}
	
	// 将词向量文件按照标签分类存储（单个关键词）
	// 是在词向量生成以后做的处理
	// 源文件路径是：F:/word2vec/quora_Data/word2vec只包含向量_quora 
	// 目标文件路径是：F:/word2vec/quora_Data/word2vec按标签存储_quora
//	private static String sourceWordFile = "F:/word2vec/quora_Data/word2vec只包含向量_quora";
	private static String sourceWordFile = "F:/word2vec/quora_Data/word2vec只包含向量且去除空行_quora";
	private static String targetWordFile = "F:/word2vec/quora_Data/word2vec按标签存储_quora";
	
	public static void storeByLabel(String keyword){
		 String targetfilepath = targetWordFile + "/" + keyword;
		// 得到关键词的标签名
		ArrayList<String> label = DirFile.getFolderFileNamesFromDirectorybyArraylist(targetfilepath);
		// 替换每个标签文件夹下的文件为词向量文件
		for(int i = 0; i < label.size(); i++){
			// 得到每个标签路径
			String filepath = targetfilepath + "/" + label.get(i);
//			System.out.println("filepath" + filepath);
			File[] targetFiles = (new File(filepath)).listFiles();
			System.out.println("targetFiles.length:" + targetFiles.length);
			// 替代targetFiles中和sourceFiles中相同的文件
			for(int j = 0; j < targetFiles.length; j++){
				String fileName = targetFiles[j].getName();
				String oldPath = sourceWordFile + "/" + fileName;
				String newPath = filepath + "/" + fileName;
				DirFile.copyFile(oldPath, newPath);
			}
		}
	}
	
	// 批量处理
	public static void storeByLabel(){
		ArrayList<String> keywords = DirFile.getFolderFileNamesFromDirectorybyArraylist(targetWordFile);
		for(int i = 0; i < keywords.size(); i++){
			String keyword = keywords.get(i);
			storeByLabel(keyword);
		}
	}
	
	public static void test() {
//		String filePath1 = "D:\\wiki_Data\\wiki.en.text.vector";
//		String inputCatalog = "D:\\wiki_Data_labels\\copy5\\Data_structure\\";
//		String outputCatalog = "D:\\wiki_Data_labels\\copy6\\Data_structure\\";
//		Match(filePath1, inputCatalog, outputCatalog);
//		MatchSingle(filePath1, inputCatalog, outputCatalog);
//		String keyword = "Absolute+deviation";
//		storeByLabel(keyword);
		storeByLabel();
	}

	public static void main(String argv[]) throws Exception {
//		test();
	}

}
