package exp1;
/**
 * word2vec + glove 生成词向量
 */
import java.io.File;
import java.util.ArrayList;

import org.junit.Test;

import base.DirFile;

public class Step3_Word2Vec {
	
	public static void main(String argv[]) throws Exception {
//		HighQuality();
		copyFile2(2);
		copyFile3(2);
		copyFile2(3);
		copyFile3(3);
	}
	
	
	private static String model = "F:\\zheng\\Glove训练的词向量模型\\vectors.txt";//glove词向量模型
	private static String gloveWikiPath = "F:\\zheng\\高质量\\gloveNew\\wiki";//wiki
	private static String path1 = "F:\\zheng\\高质量\\数据结构标注20主题_Step2\\txt1Tokenizer";//quora
	private static String path2 = "F:\\zheng\\高质量\\数据结构标注20主题_Step2\\txt2Tokenizer";//quora
	private static String path3 = "F:\\zheng\\高质量\\数据结构标注20主题_Step2\\txt3Tokenizer";//quora

	public static void HighQuality() throws Exception{
		QuoraForTag(path1);
		QuoraForTag(path2);
		QuoraForTag(path3);
	}
	
	public static void QuoraForTag(String filePath) throws Exception{
		
		// 设置路径
//		String source = filePath + "\\file0_source";
		String step1 = filePath + "\\file1_predeal";
		String step2 = filePath + "\\file2_wordtovec";
		String step3 = filePath + "\\file3_postdeal_step1";
//		String step4 = filePath+ "\\file3_postdeal_step2";
		
		// 1.预处理程序：去除文本中的标点符号，停用词，无用的行和空格
//		String inFilePath = source;
//		String outFilePath = step1;
//		new File(outFilePath).mkdir();
//		File[] sourceFile = new File(inFilePath).listFiles();
//		for(int i = 0; i < sourceFile.length; i++){
//			String keyword = sourceFile[i].getName();
//			String sourcePath = inFilePath + "\\" + keyword;
//			String targetPath = outFilePath + "\\" + keyword;
//			new File(targetPath).mkdir();
//			PreDeal.readAndProcessList(sourcePath,targetPath);
//			System.out.println(keyword + " is finished...");
//		}
		
		// 2.词向量程序：利用训练好的词向量模型，生成每段文本对应的词向量
		String modelPath = model;
		String inputCatalog = step1;
		String outputCatalog = step2;
		new File(outputCatalog).mkdir();
		File[] wordvecFile = new File(inputCatalog).listFiles();
//		ArrayList<String> stringModel = WordToVecZ.getWord(modelPath);
//		BufferedReader bufferedReaderModelAgain = WordToVecZ.getBuffer(modelPath);
		for(int i = 0; i < wordvecFile.length; i++){
			String keyword = wordvecFile[i].getName();
			String sourcePath = inputCatalog + "\\" + keyword;
			String targetPath = outputCatalog + "\\" + keyword;
			new File(targetPath).mkdir();
//			WordToVec.Match(modelPath, sourcePath, targetPath);
//			WordToVecZ.Match(stringModel, bufferedReaderModelAgain, modelPath, sourcePath, targetPath);
			System.out.println(keyword + " is finished...");
		}
		
		// 3.后期处理程序：去除步骤2中词向量文件中第一个英文单词，删除空行，删除最后一行不完整数据
		String inputCatalog2 = step2;
		String outputCatalog2 = step3;
//		String outputCatalog3 = step4;
		new File(outputCatalog2).mkdir();
//		new File(outputCatalog3).mkdir();
		File[] postFile = new File(inputCatalog2).listFiles();
		for(int i = 0; i < postFile.length; i++){
			String keyword = postFile[i].getName();
			String sourcePath = inputCatalog2 + "\\" + keyword;
			String targetPath = outputCatalog2 + "\\" + keyword;
//			String targetPath3 = outputCatalog3 + "\\" + keyword;
			new File(targetPath).mkdir();
//			new File(targetPath3).mkdir();
//			PostDeal.deleteWordandLine(sourcePath, targetPath);
//			PostDeal.deleteLastline(targetPath, targetPath3);
			System.out.println(keyword + " is finished...");
		}
	}
	
	@Test
	public static void testWiki() throws Exception{
		
		// 设置路径
		String source = gloveWikiPath + "\\file0_source";
		String step1 = gloveWikiPath + "\\file1_predeal";
		String step2 = gloveWikiPath + "\\file2_wordtovec";
		String step3 = gloveWikiPath + "\\file3_postdeal_step1";
		String step4 = gloveWikiPath + "\\file3_postdeal_step2";
		
		// 1.预处理程序：去除文本中的标点符号，停用词，无用的行和空格
//		String inFilePath = source;
//		String outFilePath = step1;
//		new File(outFilePath).mkdir();
//		File[] sourceFile = new File(inFilePath).listFiles();
//		for(int i = 0; i < sourceFile.length; i++){
//			String keyword = sourceFile[i].getName();
//			String sourcePath = inFilePath + "\\" + keyword;
//			String targetPath = outFilePath + "\\" + keyword;
//			new File(targetPath).mkdir();
//			PreDeal.readAndProcessList(sourcePath, targetPath);
//			System.out.println(keyword + " is finished...");
//		}
		
		// 2.词向量程序：利用训练好的词向量模型，生成每段文本对应的词向量
		String modelPath = model;
		String inputCatalog = step1;
		String outputCatalog = step2;
		new File(outputCatalog).mkdir();
		File[] wordvecFile = new File(inputCatalog).listFiles();
		for(int i = 0; i < wordvecFile.length; i++){
			String keyword = wordvecFile[i].getName();
			String sourcePath = inputCatalog + "\\" + keyword;
			String targetPath = outputCatalog + "\\" + keyword;
			new File(targetPath).mkdir();
//			WordToVec.Match(modelPath, sourcePath, targetPath);
			System.out.println(keyword + " is finished...");
		}
		
		// 3.后期处理程序：去除步骤2中词向量文件中第一个英文单词，删除空行，删除最后一行不完整数据
		String inputCatalog2 = step2;
		String outputCatalog2 = step3;
		String outputCatalog3 = step4;
		new File(outputCatalog2).mkdir();
		new File(outputCatalog3).mkdir();
		File[] postFile = new File(inputCatalog2).listFiles();
		for(int i = 0; i < postFile.length; i++){
			String keyword = postFile[i].getName();
			String sourcePath = inputCatalog2 + "\\" + keyword;
			String targetPath = outputCatalog2 + "\\" + keyword;
			String targetPath3 = outputCatalog3 + "\\" + keyword;
			new File(targetPath).mkdir();
			new File(targetPath3).mkdir();
//			PostDeal.deleteWordandLine(sourcePath, targetPath);
//			PostDeal.deleteLastline(targetPath, targetPath3);
			System.out.println(keyword + " is finished...");
		}
		
	}
	
	public static void copyFile2(int type) throws Exception{
		String pathIn = "F:\\高质量\\数据结构标注20主题";
		String pathOut = "F:\\高质量\\数据结构标注20主题_Step2";
		ArrayList<String> keywords = Step1_Excel2txt_Exp1.getKeyword(pathIn);
		new File(pathOut + "\\txt" + type + "Tokenizer\\file2_wordtovec").mkdir();
		for(int i = 0; i < keywords.size(); i++){
			String keyword = keywords.get(i);
			System.out.println(keyword + " is processing...");
			String pathInfile2txt1 = pathIn + "\\" + keyword + "\\file2_wordtovec\\txt" + type + "_tokenizer";
			String pathOutfile2txt1 = pathOut + "\\txt" + type + "Tokenizer\\file2_wordtovec\\" + keyword;
			new File(pathOutfile2txt1).mkdir();
			File[] files = new File(pathInfile2txt1).listFiles();
			for(int j = 0; j < files.length; j++){
				String fileName = files[j].getName();
				String pathIntxt = pathInfile2txt1 + "\\" + fileName;
				String pathOuttxt = pathOutfile2txt1 + "\\" + fileName;
				DirFile.copyFile(pathIntxt, pathOuttxt);
			}
			System.out.println(keyword + " is finished...");
		}
		
	}
	
	public static void copyFile3(int type) throws Exception{
		String pathIn = "F:\\高质量\\数据结构标注20主题";
		String pathOut = "F:\\高质量\\数据结构标注20主题_Step2";
		ArrayList<String> keywords = Step1_Excel2txt_Exp1.getKeyword(pathIn);
		new File(pathOut + "\\txt" + type + "Tokenizer\\file3_postdeal_step1").mkdir();
		for(int i = 0; i < keywords.size(); i++){
			String keyword = keywords.get(i);
			System.out.println(keyword + " is processing...");
			String pathInfile2txt1 = pathIn + "\\" + keyword + "\\file3_postdeal_step1\\txt" + type + "_tokenizer";
			String pathOutfile2txt1 = pathOut + "\\txt" + type + "Tokenizer\\file3_postdeal_step1\\" + keyword;
			new File(pathOutfile2txt1).mkdir();
			File[] files = new File(pathInfile2txt1).listFiles();
			for(int j = 0; j < files.length; j++){
				String fileName = files[j].getName();
				String pathIntxt = pathInfile2txt1 + "\\" + fileName;
				String pathOuttxt = pathOutfile2txt1 + "\\" + fileName;
				DirFile.copyFile(pathIntxt, pathOuttxt);
			}
			System.out.println(keyword + " is finished...");
		}
		
	}
	
	
}
