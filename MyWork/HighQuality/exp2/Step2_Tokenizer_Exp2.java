package exp2;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermToBytesRefAttribute;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.core.*;
import org.apache.lucene.util.Version;

public class Step2_Tokenizer_Exp2 {
	
	private static String cata = "02-CQA网站中问题答案质量评估";
	private static String path = "F:\\"+cata+"\\二叉树\\quora\\标注_CNN\\data";
	private static String pathNew = "F:\\"+cata+"\\二叉树\\quora\\标注_CNN\\dataForGlove";
	private static String pathNewWikiBefore = "F:\\"+cata+"\\二叉树\\wiki\\分词处理\\wiki_tokenizer_before";
	private static String pathNewWikiAfter = "F:\\"+cata+"\\二叉树\\wiki\\分词处理\\wiki_tokenizer_after";

	public static void main(String[] args) throws Exception {
//		example();
//		tokenQuora();
		tokenWiki();
	}

	/**
	 * 批处理：Quora分词预处理
	 */
	public static void tokenQuora() {
		new File(pathNew).mkdir();
		File[] files = new File(path).listFiles();// 得到数据保存路径
		for(int i = 0; i < files.length; i++){
			String cata = files[i].getName();
			String filePath = path + "\\" + cata;// 得到子文件夹路径
			String filePathOut = pathNew + "\\" + cata;// 新文件夹路径
			new File(filePathOut).mkdir();
			File[] txt = new File(filePath).listFiles();
			for(int j = 0; j < txt.length; j++){// 对子文件夹下的所有文件进行预处理，用于词向量训练
				String txtName = txt[j].getName();
				String txtPathIn = filePath + "\\" + txtName;
				String txtPathOut = filePathOut + "\\" + txtName;// 新文件保存路径
				tokenizerForGlove(txtPathIn, txtPathOut);// 英文分词预处理
			}
		}
	}
	
	/**
	 * 批处理：Wiki分词预处理
	 */
	public static void tokenWiki() {
		new File(pathNewWikiAfter).mkdir();
		File[] files = new File(pathNewWikiBefore).listFiles();// 得到数据保存路径
		for(int i = 0; i < files.length; i++){
			String cata = files[i].getName();
			String filePath = pathNewWikiBefore + "\\" + cata;// 得到子文件夹路径
			String filePathOut = pathNewWikiAfter + "\\" + cata;// 新文件夹路径
			new File(filePathOut).mkdir();
			File[] txt = new File(filePath).listFiles();
			for(int j = 0; j < txt.length; j++){// 对子文件夹下的所有文件进行预处理，用于词向量训练
				String txtName = txt[j].getName();
				String txtPathIn = filePath + "\\" + txtName;
				String txtPathOut = filePathOut + "\\" + txtName;// 新文件保存路径
				tokenizerForGlove(txtPathIn, txtPathOut);// 英文分词预处理
			}
		}
	}
	
	/**
	 * 预处理Glove词向量模型的输入文件: 测试程序
	 */
	public static void example() {
		String filePath = "F:\\sample.txt";// 输入文件
		String fileOutPath = "F:\\sample(Tokenizer).txt";// 输出文件
		tokenizerForGlove(filePath, fileOutPath);
	}

	/**
	 * 对原始的英文文件进行分词处理，按照空格及各种符号分割（对于Glove的输入，使用这哥分词器进行分词处理）
	 * @param filePath: 输入文件
	 * @param fileOutPath: 分词处理后的输出文件
	 */
	public static void tokenizerForGlove(String filePath, String fileOutPath) {
		try {
			File file = new File(filePath);
			Reader reader = new FileReader(file);
			// Analyzer a = new StandardAnalyzer();//混合分割,包括了去掉停止词，支持汉语
			// Analyzer a = new WhitespaceAnalyzer();//空格分割
			Analyzer a = new SimpleAnalyzer();// 空格及各种符号分割（对于Glove的输入，使用这哥分词器进行分词处理）
			// Analyzer a = new StopAnalyzer();//空格及各种符号分割,去掉停止词，停止词包括 is,are,in,on,the等无实际意义的词
			// Analyzer a = new CJKAnalyzer();
			// Analyzer a = new ChineseAnalyzer();
			// Analyzer a = new WhitespaceAnalyzer();
			TokenStream ts = a.tokenStream("", reader);// 读取文件六内容到字符流中
			ts.reset();// Resets this stream to the beginning. (Required)
			boolean hasnext = ts.incrementToken();// 判断是否存在分好的字符
			String data = "";// 用于保存分词的结果
			FileWriter fw = new FileWriter(fileOutPath, true);// 可以在文件末尾写
			while (hasnext) {
				TermToBytesRefAttribute ta = ts.getAttribute(TermToBytesRefAttribute.class);
//				System.out.println(ta.toString());
				data = data + ta.toString() + " ";
				hasnext = ts.incrementToken();
			}
			fw.write(data.toString());
			System.out.println(data);
			fw.close();
			ts.end();
			ts.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}