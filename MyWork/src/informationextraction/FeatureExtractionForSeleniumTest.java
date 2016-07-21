package informationextraction;

import java.io.File;

import org.jsoup.nodes.Document;

import Jsoup.JsoupParse;

public class FeatureExtractionForSeleniumTest {
	
//	private static String path = "file\\datacollection\\test\\2-3+heap";
	private static String path = "F:\\高质量\\二叉树\\quora\\question";
	private static FeatureExtractionForSelenium extract = new FeatureExtractionForSelenium();

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		test();
		test1();
	}
	
	public static void test() throws Exception{
		String filePath = path + "\\Binary_tree0.html";
		Document doc = JsoupParse.parsePathText(filePath);
//		String subjectpagecrawlertime = extract.getCrawlerTime(filePath);
//		System.out.println("time:"+subjectpagecrawlertime);
//		extract.countAnswerNumber(doc);
		int n = extract.countRealAnswerNumber2(doc);
		extract.questionContent(doc);
		extract.questionExpandInfo(doc);
//		extract.questionContentWordLength(doc);
//		extract.questionWantAnswers(doc);
//		extract.questionCommentNumbers(doc);
//		extract.questionTopics(doc);
		for(int j = 0; j < n; j++){
			extract.answerContent(doc, j);
//			extract.answerContentWordLength(doc, j);
//			extract.answerUpvotes(doc, j);
//			extract.answerCommentNumbers(doc, j);
//			extract.answerURLs(doc, j);
//			extract.authorName(doc, j);
			
			// 作者信息
//			String authorPath = path + "\\2-3+heap1_author_" + j + ".html";
//			System.out.println("processing file: "+authorPath);
//			// 有些作者页面没有爬取到
//			if(new File(authorPath).exists()){
//				Document authorDoc = JsoupParse.parsePathText(authorPath);
//				extract.authorAnswers(authorDoc, j);
////				extract.authorQuestions(authorDoc, j);
//				extract.authorFollowers(authorDoc, j);
//				extract.authorFollowing(authorDoc, j);
//				extract.authorKnowAbout(authorDoc, j);
//			}
			
		}
	}
	
	public static void test1() throws Exception{
		for(int i = 0; i < 50; i++){
			String filePath = path + "\\Binary_tree" + i +".html";
			System.out.println("\nprocessing file: "+filePath);
			Document doc = JsoupParse.parsePathText(filePath);
//			String subjectpagecrawlertime = extract.getCrawlerTime(filePath);
//			System.out.println("time:"+subjectpagecrawlertime);
			int n = extract.countRealAnswerNumber2(doc);
			extract.questionContent(doc);
			extract.questionExpandInfo(doc);
//			extract.questionContentWordLength(doc);
//			extract.questionWantAnswers(doc);
//			extract.questionCommentNumbers(doc);
//			extract.questionTopics(doc);
			for(int j = 0; j < n; j++){
				extract.answerContent(doc, j);
//				extract.answerContentWordLength(doc, j);
//				extract.answerUpvotes(doc, j);
//				extract.answerCommentNumbers(doc, j);
//				extract.answerURLs(doc, j);
//				extract.authorName(doc, j);
				
//				// 作者信息
//				String authorPath = path + "\\Biconnected+graph" + i +"_author_" + j + ".html";
//				System.out.println("processing file: "+authorPath);
//				// 有些作者页面没有爬取到
//				if(new File(authorPath).exists()){
//					Document authorDoc = JsoupParse.parsePathText(authorPath);
//					extract.authorAnswers(authorDoc, j);
//					extract.authorQuestions(authorDoc, j);
//					extract.authorFollowers(authorDoc, j);
//					extract.authorFollowing(authorDoc, j);
//					extract.authorKnowAbout(authorDoc, j);
//				}
			}
		}
	}

}
