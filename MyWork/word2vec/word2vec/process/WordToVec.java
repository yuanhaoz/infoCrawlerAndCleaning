package word2vec.process;

/**
 * 利用已经训练好的词向量模型："D:\\wiki_Data\\wiki.en.text.vector"（英文）
 * 匹配新的文本数据，生成对应的词向量文件
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class WordToVec {

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
		System.out.println("耗时1："+(end-start)/1000+"s");
		long start1 = System.currentTimeMillis();
		for (int i = 0; i < files.length; i++) {
			filename = files[i].getName();
			inputFilepath = inputCatalog + "\\" + filename;
			outputFilepath = outputCatalog + "\\" + filename;
//			System.out.println(outputFilepath);
//			if(!new File(outputFilepath).exists()){
//				new File(outputFilepath).createNewFile();
//			}
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
		}
		long end1 = System.currentTimeMillis();
		System.out.println("耗时1："+(end1-start1)/1000+"s");
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

	public static void main(String argv[]) throws Exception {
		String filePath1 = "D:\\wiki_Data\\wiki.en.text.vector";
//		String t = "Tree_traversal";
//		String inputCatalog = "D:\\wiki_Data_labels\\DataStructure\\Data_structure_5\\"+t+"\\";
//		String outputCatalog = "D:\\wiki_Data_labels\\DataStructure\\Data_structure_6\\"+t+"\\";
		String inputCatalog = "D:\\quora_Data\\data_structure_tag_txt\\dataStructureAll_exceptarray_PreDeal\\";
		String outputCatalog = "D:\\quora_Data\\data_structure_tag_txt\\dataStructureAll_exceptarray_Word2Vec\\";
		Match(filePath1, inputCatalog, outputCatalog);
		// MatchSingle(filePath1, inputCatalog, outputCatalog);
	}

}
