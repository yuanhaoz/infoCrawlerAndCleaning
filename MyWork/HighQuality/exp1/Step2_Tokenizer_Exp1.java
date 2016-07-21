package exp1;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermToBytesRefAttribute;

public class Step2_Tokenizer_Exp1 {

	private static String cata = "02-CQA网站中问题答案质量评估";
	private static String pathStep1 = "F:\\"+cata+"\\数据结构标注20主题_Step1";
	private static String pathStep2 = "F:\\"+cata+"\\数据结构标注20主题_Step2";

	public static void main(String[] args) throws Exception {
		tokenQuora(1);
		tokenQuora(2);
		tokenQuora(3);
	}

	/**
	 * 批处理：Quora分词预处理
	 */
	public static void tokenQuora(int type) throws Exception {
		new File(pathStep2).mkdir();
		String step1 = pathStep1 + "\\txt" + type + "FromExcel";
		String s2 = pathStep2 + "\\txt" + type + "Tokenizer";
		String step2 = s2 + "\\file1_predeal";
		new File(s2).mkdir();
		new File(step2).mkdir();
		ArrayList<String> keyword = Step1_Excel2txt_Exp1.getKeyword(step1);
		for (int i = 0; i < keyword.size(); i++) {
			String word = keyword.get(i);// 得到关键词
			String filePath = step1 + "\\" + word;// 得到txt1文件夹路径
			String filePathOut = step2 + "\\" + word;// 新文件夹路径
			new File(step2 + "\\" + word).mkdir();
			new File(filePathOut).mkdir();
			File[] txt = new File(filePath).listFiles();
			for (int j = 0; j < txt.length; j++) {// 对子文件夹下的所有文件进行预处理，用于词向量训练
				String txtName = txt[j].getName();
				String txtPathIn = filePath + "\\" + txtName;
				String txtPathOut = filePathOut + "\\" + txtName;// 新文件保存路径
				tokenizerForGlove(txtPathIn, txtPathOut);// 英文分词预处理
			}
			System.out.println(word + " is finished...");
		}
	}
	
	/**
	 * 批处理：Quora分词预处理
	 */
	public static void tokenQuoraOld() throws Exception {
		ArrayList<String> keyword = Step1_Excel2txt_Exp1.getKeyword(pathStep1);
		for (int i = 0; i < keyword.size(); i++) {
			String word = keyword.get(i);// 得到关键词
			for (int step = 1; step < 4; step++) {// 对txt1，txt2，txt3三个文件夹批量分词
				String filePath = pathStep1 + "\\" + word + "\\txt" + step;// 得到txt1文件夹路径
				new File(pathStep1 + "\\" + word + "\\file1_predeal").mkdir();
				String filePathOut = pathStep1 + "\\" + word
						+ "\\file1_predeal\\txt" + step + "_tokenizer";// 新文件夹路径
				new File(filePathOut).mkdir();
				File[] txt = new File(filePath).listFiles();
				for (int j = 0; j < txt.length; j++) {// 对子文件夹下的所有文件进行预处理，用于词向量训练
					String txtName = txt[j].getName();
					String txtPathIn = filePath + "\\" + txtName;
					String txtPathOut = filePathOut + "\\" + txtName;// 新文件保存路径
					tokenizerForGlove(txtPathIn, txtPathOut);// 英文分词预处理
				}
			}
			System.out.println(word + " is finished...");
		}
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
			@SuppressWarnings("resource")
			Analyzer a = new SimpleAnalyzer();// 空格及各种符号分割（对于Glove的输入，使用这哥分词器进行分词处理）
			// Analyzer a = new StopAnalyzer();//空格及各种符号分割,去掉停止词，停止词包括
			// is,are,in,on,the等无实际意义的词
			// Analyzer a = new CJKAnalyzer();
			// Analyzer a = new ChineseAnalyzer();
			// Analyzer a = new WhitespaceAnalyzer();
			TokenStream ts = a.tokenStream("", reader);// 读取文件六内容到字符流中
			ts.reset();// Resets this stream to the beginning. (Required)
			boolean hasnext = ts.incrementToken();// 判断是否存在分好的字符
			String data = "";// 用于保存分词的结果
			FileWriter fw = new FileWriter(fileOutPath, true);// 可以在文件末尾写
			while (hasnext) {
				TermToBytesRefAttribute ta = ts
						.getAttribute(TermToBytesRefAttribute.class);
				// System.out.println(ta.toString());
				data = data + ta.toString() + " ";
				hasnext = ts.incrementToken();
			}
			fw.write(data.toString());
			// System.out.println(data);
			fw.close();
			ts.end();
			ts.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}