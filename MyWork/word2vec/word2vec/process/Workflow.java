package word2vec.process;

import java.io.File;

import org.junit.Test;

public class Workflow {
	
	public static void main(String argv[]) throws Exception {
//		testWiki();
		testQuora();
		
	}

	@Test
	public static void testWiki() throws Exception{
		
		// 设置路径
		String model = "D:\\wiki_Data\\wiki.en.text.vector";
		String source = "D:\\word2vec\\data_structure_wiki\\file0_source";
		String step1 = "D:\\word2vec\\data_structure_wiki\\file1_predeal";
		String step2 = "F:\\word2vec\\data_structure_wiki\\file2_wordtovec";
		String step3 = "F:\\word2vec\\data_structure_wiki\\file3_postdeal_step1";
		String step4 = "F:\\word2vec\\data_structure_wiki\\file3_postdeal_step2";
		
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
//		String modelPath = model;
//		String inputCatalog = step1;
//		String outputCatalog = step2;
//		new File(outputCatalog).mkdir();
//		File[] wordvecFile = new File(inputCatalog).listFiles();
//		for(int i = 0; i < wordvecFile.length; i++){
//			String keyword = wordvecFile[i].getName();
//			String sourcePath = inputCatalog + "\\" + keyword;
//			String targetPath = outputCatalog + "\\" + keyword;
//			new File(targetPath).mkdir();
//			WordToVec.Match(modelPath, sourcePath, targetPath);
//			System.out.println(keyword + " is finished...");
//		}
		
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
			PostDeal.deleteWordandLine(sourcePath, targetPath);
			PostDeal.deleteLastline(targetPath, targetPath3);
			System.out.println(keyword + " is finished...");
		}
		
	}
	
	@Test
	public static void testQuora() throws Exception{
		
		// 设置路径
		String model = "D:\\wiki_Data\\wiki.en.text.vector";
		String source = "D:\\word2vec\\data_structure_tag_txt\\file0_source";
		String step1 = "D:\\word2vec\\data_structure_tag_txt\\file1_predeal";
		String step2 = "F:\\word2vec\\data_structure_tag_txt\\file2_wordtovec";
		String step3 = "F:\\word2vec\\data_structure_tag_txt\\file3_postdeal_step1";
		String step4 = "F:\\word2vec\\data_structure_tag_txt\\file3_postdeal_step2";
		
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
//		String modelPath = model;
//		String inputCatalog = step1;
//		String outputCatalog = step2;
//		new File(outputCatalog).mkdir();
//		File[] wordvecFile = new File(inputCatalog).listFiles();
//		for(int i = 0; i < wordvecFile.length; i++){
//			String keyword = wordvecFile[i].getName();
//			String sourcePath = inputCatalog + "\\" + keyword;
//			String targetPath = outputCatalog + "\\" + keyword;
//			new File(targetPath).mkdir();
//			WordToVec.Match(modelPath, sourcePath, targetPath);
//			System.out.println(keyword + " is finished...");
//		}
		
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
			PostDeal.deleteWordandLine(sourcePath, targetPath);
			PostDeal.deleteLastline(targetPath, targetPath3);
			System.out.println(keyword + " is finished...");
		}
	}
	
}
