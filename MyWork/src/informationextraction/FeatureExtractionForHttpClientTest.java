package informationextraction;

import java.io.File;

import org.jsoup.nodes.Document;

import Jsoup.JsoupParse;

public class FeatureExtractionForHttpClientTest {
	
	private static String path = "file\\datacollection\\test\\Ordinal+data+type";
	private static FeatureExtractionForHttpClient extract = new FeatureExtractionForHttpClient();

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		test();
		test1();
	}
	
	public static void test() throws Exception{
		String filePath = path + "\\Ordinal+data+type26.html";
		Document doc = JsoupParse.parsePathText(filePath);
//		String subjectpagecrawlertime = extract.getCrawlerTime(filePath);
//		System.out.println("time:"+subjectpagecrawlertime);
		int n = extract.countRealAnswerNumber(doc);
//		extract.questionContent(doc);
//		extract.questionExpandInfo(doc);
//		extract.questionContentWordLength(doc);
		for(int j = 0; j < n; j++){
//			extract.answerContent(doc, j);
//			extract.answerContentWordLength(doc, j);
//			extract.answerURLs(doc, j);
//			extract.authorName(doc, j);
			
			// 作者信息
			String authorPath = path + "\\Ordinal+data+type26_author_" + j + ".html";
			System.out.println("processing file: "+authorPath);
			// 有些作者页面没有爬取到
			if(new File(authorPath).exists()){
				Document authorDoc = JsoupParse.parsePathText(authorPath);
				extract.authorAnswers(authorDoc, j);
				extract.authorQuestions(authorDoc, j);
				extract.authorFollowers(authorDoc, j);
				extract.authorFollowing(authorDoc, j);
				extract.authorKnowAbout(authorDoc, j);
			}
			
		}
	}
	
	public static void test1() throws Exception{
		for(int i = 0; i < 50; i++){
			String filePath = path + "\\Ordinal+data+type" + i +".html";
			System.out.println("\nprocessing file: "+filePath);
			Document doc = JsoupParse.parsePathText(filePath);
//			String subjectpagecrawlertime = extract.getCrawlerTime(filePath);
//			System.out.println("time:"+subjectpagecrawlertime);
			int n = extract.countRealAnswerNumber(doc);
			extract.questionContent(doc);
			extract.questionExpandInfo(doc);
			extract.questionContentWordLength(doc);
			for(int j = 0; j < n; j++){
				extract.answerContent(doc, j);
				extract.answerContentWordLength(doc, j);
				extract.answerURLs(doc, j);
				extract.authorName(doc, j);
				
				// 作者信息
				String authorPath = path + "\\Ordinal+data+type" + i +"_author_" + j + ".html";
//				System.out.println("processing file: "+authorPath);
				// 有些作者页面没有爬取到
				if(new File(authorPath).exists()){
					Document authorDoc = JsoupParse.parsePathText(authorPath);
					extract.authorAnswers(authorDoc, j);
					extract.authorQuestions(authorDoc, j);
					extract.authorFollowers(authorDoc, j);
					extract.authorFollowing(authorDoc, j);
					extract.authorKnowAbout(authorDoc, j);
				}
			}
		}
	}

}
