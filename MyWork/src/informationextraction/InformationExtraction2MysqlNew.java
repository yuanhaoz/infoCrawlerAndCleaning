package informationextraction;

/**
 * 填写fragment表：用于存储从Quora网站爬取知识碎片
 * 填写fragment_term表：用于存储知识碎片id与主题词id之间的关系
 * 填写assemble表：用于存储知识碎片id与term_id、facet_id、domain_id之间的关系
 * @author zhengyuanhao
 */

import java.io.File;
import java.net.URLDecoder;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import mysql.MysqlConnection;

import org.jsoup.nodes.Document;

import dataCollection.DataCollection;
import Jsoup.JsoupParse;
import basic.KeywordCatalogDesign;

public class InformationExtraction2MysqlNew {

	// 导入mysqloperation类
	MysqlConnection mysqlCon = new MysqlConnection();

	// 准备sql语句
	private String sql;

	// 影响行数（数据变更后，影响行数都是大于0，等于0时没变更，所以说如果变更失败，那么影响行数必定为负）
	// private int i = -1;

	// 结果集
	// private ResultSet rs;

	public static void main(String[] args) throws Exception {
//		analysis("Data_structure");
//		facetAnalysis("Data_structure");
	}
	
	/**
	 * 数据过滤
	 */
	public String keywordAndLength(String content, String[] keywordarray) {
		int count = 0;
		for (int j = 0; j < keywordarray.length; j++) {
			// 在字符串中找关键词出现的次数
			Pattern p = Pattern.compile("(?i)" + keywordarray[j]); // 忽略大小写
			Matcher m = p.matcher(content);
			while (m.find()) {
				count++;
			}
		}
		// System.out.println("关键词出现次数: " + count);
		int length = content.length();
		if (count != 0 && length < 500 && length > 10) {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * 保存从Quora网站上面爬取的数据：数据结构、数据挖掘、计算机网络
	 * 便于导出成Excel，而且节省存储空间
	 * 相对于存放在Excel，方便操作
	 * 可以导出成Excel进行分析
	 */
	public void storeAllFragmentForHttpClient(String keyword, int pagelength, String course) throws Exception {
		try {
			// 解析数据，数据加到数据库里面
			String catalog = KeywordCatalogDesign.GetKeywordCatalog(keyword);

			// 将关键词以+分开存到数组里面，用于匹配文本
			String[] keywordarray = keyword.split("\\+");

			// 解析每个问题网页的问题和回答
			for (int j = 0; j < pagelength; j++) {
				String path = catalog + keyword + j + ".html";
				File file = new File(path);
				if (!file.exists()) {
//					System.out.println(path + "  不存在，请重新爬取数据...");
				} else {
//					System.out.println("\n开始解析： " + path);
					Document doc = JsoupParse.parsePathText(path);
					
					// 得到问题的各字段信息，没有作者ID，爬取时间为当前解析数据的时间
					// 得到问题ID
					String keywordstore = keyword.replaceAll("\\+", "\\_");
					// System.out.println("转义以后：" + keywordstore);
					String QuestionId = keywordstore + j + ""; 
					// 得到碎片内容
					String QuestionContent = FeatureExtractionForHttpClient.questionContent(doc) + "\n"
					+ FeatureExtractionForHttpClient.questionExpandInfo(doc);
					// 文本是问题还是答案
					String QuestionType = "1";
					// 问题的关注人数
					String QuestionUpvote = "";
					// 问题是否含有链接
					String QuestionLink = "";
					// 问题是否含有评论
					String QuestionComment = "";
					// 问题粉丝数
					String QuestionAuthorFollower = "";
					// 提问者职业
					String QuestionAuthorProfession = "";
					// 提问者擅长领域
					String QuestionAuthorKnowAbout = "";
					// 提问者回答总数
					String QuestionAuthorAnswerSum = "";
					// 碎片单词长度
					int QuestionWordLength = FeatureExtractionForHttpClient.questionContentWordLength(doc);
					// 是否存在关键词并且长度在[10~500之间]
					InformationExtraction2MysqlNew ms = new InformationExtraction2MysqlNew();
					String QuestionExist = ms.keywordAndLength(QuestionContent, keywordarray);
					// 问题序号
					String QuestionSequence = j + "";
					String AuthorFollowing = "";
					String AuthorQuestion = "";
					String crawlerTime = FeatureExtractionForHttpClient.getCrawlerTime(path);
					
					// 创建sql语句
					if(course.equals("Data_structure")){
//						sql = "replace into fragment_filtering_datastructurenew values (?, ?, ?, ?, ?, ?, ?, ?,"
//								+ "?, ?, ?, ?, ?)";
						sql = "replace into fragment_filtering_datastructure values (?, ?, ?, ?, ?, ?, ?, ?,"
								+ "?, ?, ?, ?, ?)";
					} else if (course.equals("Data_mining")) {
						sql = "replace into fragment_filtering_datamining values (?, ?, ?, ?, ?, ?, ?, ?,"
								+ "?, ?, ?, ?, ?)";
					} else if (course.equals("Computer_network")) {
						sql = "replace into fragment_filtering_computernetwork values (?, ?, ?, ?, ?, ?, ?, ?,"
								+ "?, ?, ?, ?, ?)";
					} else if (course.equals("Data_structure_excel59")) {
						sql = "replace into fragment_filtering_datastructure_193 values (?, ?, ?, ?, ?, ?, ?, ?,"
								+ "?, ?, ?, ?, ?, ?, ?, ?)";
					} else if (course.equals("Data_structure_excel112")) {
						sql = "replace into fragment_filtering_datastructure_193 values (?, ?, ?, ?, ?, ?, ?, ?,"
								+ "?, ?, ?, ?, ?, ?, ?, ?)";
					}
					
					
					
					Object[] QuestionObject = new Object[] { QuestionId,QuestionContent,
							QuestionType, QuestionUpvote, QuestionLink,
							QuestionComment, QuestionAuthorFollower, QuestionAuthorProfession,
							QuestionAuthorKnowAbout, QuestionAuthorAnswerSum, 
							QuestionWordLength, QuestionExist, QuestionSequence,
							AuthorFollowing, AuthorQuestion, crawlerTime};
					
					//执行sql语句
					mysqlCon.doSql(sql, QuestionObject);
					
					//判断是否插入成功
					int i = mysqlCon.getUpdateCount();
					if (i != -1) {
//						System.out.println("数据插入成功！");
					} else {
						System.out.println("数据插入失败！");
					}
					
					// 关闭链接
					mysqlCon.getClose();

					// 得到答案数据
					int realanswernumber = FeatureExtractionForHttpClient.countRealAnswerNumber(doc);
					for (int m = 0; m < realanswernumber; m++) {

						// 得到答案的各字段信息
						// 得到答案ID
						String AnswerId = QuestionId + "_" + (m+1);
//						String AnswerId = QuestionId;
						// 得到碎片内容
						String AnswerContent = FeatureExtractionForHttpClient.answerContent(doc, m);
						// 文本是问题还是答案
						String AnswerType = "0";
						// 答案关注人数
						String AnswerUpvote = "";
						// 答案是否含有链接
						String AnswerLink = FeatureExtractionForHttpClient.answerURLs(doc, m);
						// 答案是否含有评论
						String AnswerComment = "";
						// 碎片单词长度
						int AnswerWordLength = FeatureExtractionForHttpClient.answerContentWordLength(doc, m);
						// 是否存在关键词并且长度在[10~500之间]
						String AswerExist = ms.keywordAndLength(AnswerContent, keywordarray);
						// 问题序号
						String AnswerSequence = j + "";
						// 得到作者信息
						String AuthorPath = catalog + keyword + j + "_author_" + m + ".html";
						File file1 = new File(AuthorPath);
						String AnswerAuthorFollower = null;
						String AnswerAuthorProfession = null;
						String AnswerAuthorKnowAbout = null;
						String AnswerAuthorAnswerSum = null;
						String AnswerAuthorQuestionSum = null;
						String AnswerAuthorFollowingSum = null;
						String AuthorCrawlerTime = null;
						
						if(!file1.exists()){
//							System.out.println(AuthorPath + "   不存在");
						}else{
							Document doc1 = JsoupParse.parsePathText(AuthorPath);  //解析作者页面路径
							// 回答者粉丝数
							AnswerAuthorFollower = FeatureExtractionForHttpClient.authorFollowers(doc1, m);
							// 作者简历
							AnswerAuthorProfession = FeatureExtractionForHttpClient.authorName(doc, m);   
							// 作者领域
							AnswerAuthorKnowAbout = FeatureExtractionForHttpClient.authorKnowAbout(doc1, m);  
							// 回答总数
							AnswerAuthorAnswerSum = FeatureExtractionForHttpClient.authorAnswers(doc1, m);       
							// 偶像数目
							AnswerAuthorFollowingSum = FeatureExtractionForHttpClient.authorFollowing(doc1, m);
							// 提问总数
							AnswerAuthorQuestionSum = FeatureExtractionForHttpClient.authorQuestions(doc1, m);
							// 文件爬取时间
							AuthorCrawlerTime = FeatureExtractionForHttpClient.getCrawlerTime(AuthorPath);
						}
						
						// 创建object数组
						Object[] AnswerObject = new Object[] { AnswerId,AnswerContent,
								AnswerType, AnswerUpvote, AnswerLink,
								AnswerComment, AnswerAuthorFollower, AnswerAuthorProfession,
								AnswerAuthorKnowAbout, AnswerAuthorAnswerSum, 
								AnswerWordLength, AswerExist, AnswerSequence,
								AnswerAuthorFollowingSum, AnswerAuthorQuestionSum, AuthorCrawlerTime};
						
						//执行sql语句
						mysqlCon.doSql(sql, AnswerObject);
						
						//判断是否插入成功
						i = mysqlCon.getUpdateCount();
						if (i != -1) {
//							System.out.println("数据插入成功！");
						} else {
							System.out.println("数据插入失败！");
						}
						
						// 关闭链接
						mysqlCon.getClose();

					}
				}
			}
		} catch (Exception ex) {
			System.out.println("Error : " + ex.toString());
		}
	}
	
	/**
	 * 保存从Quora网站上面爬取的数据：数据结构、数据挖掘、计算机网络
	 * 便于导出成Excel，而且节省存储空间
	 * 相对于存放在Excel，方便操作
	 * 可以导出成Excel进行分析
	 */
	public void storeAllFragmentForSelenium(String keyword, int pagelength, String course) throws Exception {
		try {
			// 解析数据，数据加到数据库里面
			String catalog = KeywordCatalogDesign.GetKeywordCatalog(keyword);

			// 将关键词以+分开存到数组里面，用于匹配文本
			String[] keywordarray = keyword.split("\\+");

			// 解析每个问题网页的问题和回答
			for (int j = 0; j < pagelength; j++) {
				String path = catalog + keyword + j + ".html";
				File file = new File(path);
				if (!file.exists()) {
//					System.out.println(path + "  不存在，请重新爬取数据...");
				} else {
//					System.out.println("\n开始解析： " + path);
					Document doc = JsoupParse.parsePathText(path);
					
					// 得到问题的各字段信息，没有作者ID，爬取时间为当前解析数据的时间
					// 得到问题ID
					String keywordstore = keyword.replaceAll("\\+", "\\_");
					// System.out.println("转义以后：" + keywordstore);
					String QuestionId = keywordstore + j + ""; 
					// 得到碎片内容
					String QuestionContent = FeatureExtractionForSelenium.questionContent(doc) + "\n"
					+ FeatureExtractionForSelenium.questionExpandInfo(doc);
					// 文本是问题还是答案
					String QuestionType = "1";
					// 问题的关注人数
					String QuestionUpvote = FeatureExtractionForSelenium.questionWantAnswers(doc);
					// 问题是否含有链接
					String QuestionLink = "";
					// 问题是否含有评论
					String QuestionComment = FeatureExtractionForSelenium.questionCommentNumbers(doc);
					// 问题粉丝数
					String QuestionAuthorFollower = "";
					// 提问者职业
					String QuestionAuthorProfession = "";
					// 提问者擅长领域
					String QuestionAuthorKnowAbout = "";
					// 提问者回答总数
					String QuestionAuthorAnswerSum = "";
					// 碎片单词长度
					int QuestionWordLength = FeatureExtractionForSelenium.questionContentWordLength(doc);
					// 是否存在关键词并且长度在[10~500之间]
					InformationExtraction2MysqlNew ms = new InformationExtraction2MysqlNew();
					String QuestionExist = ms.keywordAndLength(QuestionContent, keywordarray);
					// 问题序号
					String QuestionSequence = j + "";
					String AuthorFollowing = "";
					String AuthorQuestion = "";
					String crawlerTime = FeatureExtractionForSelenium.getCrawlerTime(path);
					
					// 创建sql语句
					if(course.equals("Data_structure")){
//						sql = "replace into fragment_filtering_datastructurenew values (?, ?, ?, ?, ?, ?, ?, ?,"
//								+ "?, ?, ?, ?, ?)";
						sql = "replace into fragment_filtering_datastructure values (?, ?, ?, ?, ?, ?, ?, ?,"
								+ "?, ?, ?, ?, ?)";
					} else if (course.equals("Data_mining")) {
						sql = "replace into fragment_filtering_datamining values (?, ?, ?, ?, ?, ?, ?, ?,"
								+ "?, ?, ?, ?, ?)";
					} else if (course.equals("Computer_network")) {
						sql = "replace into fragment_filtering_computernetwork values (?, ?, ?, ?, ?, ?, ?, ?,"
								+ "?, ?, ?, ?, ?)";
					} else if (course.equals("Data_structure_excel59")) {
						sql = "replace into fragment_filtering_datastructure_193 values (?, ?, ?, ?, ?, ?, ?, ?,"
								+ "?, ?, ?, ?, ?, ?, ?, ?)";
					} else if (course.equals("Data_structure_excel112")) {
						sql = "replace into fragment_filtering_datastructure_193 values (?, ?, ?, ?, ?, ?, ?, ?,"
								+ "?, ?, ?, ?, ?, ?, ?, ?)";
					}
					
					
					
					Object[] QuestionObject = new Object[] { QuestionId,QuestionContent,
							QuestionType, QuestionUpvote, QuestionLink,
							QuestionComment, QuestionAuthorFollower, QuestionAuthorProfession,
							QuestionAuthorKnowAbout, QuestionAuthorAnswerSum, 
							QuestionWordLength, QuestionExist, QuestionSequence,
							AuthorFollowing, AuthorQuestion, crawlerTime};
					
					//执行sql语句
					mysqlCon.doSql(sql, QuestionObject);
					
					//判断是否插入成功
					int i = mysqlCon.getUpdateCount();
					if (i != -1) {
//						System.out.println("数据插入成功！");
					} else {
						System.out.println("数据插入失败！");
					}
					
					// 关闭链接
					mysqlCon.getClose();

					// 得到答案数据
					int realanswernumber = FeatureExtractionForSelenium.countRealAnswerNumber(doc);
					for (int m = 0; m < realanswernumber; m++) {

						// 得到答案的各字段信息
						// 得到答案ID
						String AnswerId = QuestionId + "_" + (m+1);
//						String AnswerId = QuestionId;
						// 得到碎片内容
						String AnswerContent = FeatureExtractionForSelenium.answerContent(doc, m);
						// 文本是问题还是答案
						String AnswerType = "0";
						// 答案关注人数
						String AnswerUpvote = FeatureExtractionForSelenium.answerUpvotes(doc, m);
						// 答案是否含有链接
						String AnswerLink = FeatureExtractionForSelenium.answerURLs(doc, m);
						// 答案是否含有评论
						String AnswerComment = FeatureExtractionForSelenium.answerCommentNumbers(doc, m);
						// 碎片单词长度
						int AnswerWordLength = FeatureExtractionForSelenium.answerContentWordLength(doc, m);
						// 是否存在关键词并且长度在[10~500之间]
						String AswerExist = ms.keywordAndLength(AnswerContent, keywordarray);
						// 问题序号
						String AnswerSequence = j + "";
						// 得到作者信息
						String AuthorPath = catalog + keyword + j + "_author_" + m + ".html";
						File file1 = new File(AuthorPath);
						String AnswerAuthorFollower = null;
						String AnswerAuthorProfession = null;
						String AnswerAuthorKnowAbout = null;
						String AnswerAuthorAnswerSum = null;
						String AnswerAuthorQuestionSum = null;
						String AnswerAuthorFollowingSum = null;
						String AuthorCrawlerTime = null;
						
						if(!file1.exists()){
//							System.out.println(AuthorPath + "   不存在");
						}else{
							Document doc1 = JsoupParse.parsePathText(AuthorPath);  //解析作者页面路径
							// 回答者粉丝数
							AnswerAuthorFollower = FeatureExtractionForSelenium.authorFollowers(doc1, m);
							// 作者简历
							AnswerAuthorProfession = FeatureExtractionForSelenium.authorName(doc, m);   
							// 作者领域
							AnswerAuthorKnowAbout = FeatureExtractionForSelenium.authorKnowAbout(doc1, m);  
							// 回答总数
							AnswerAuthorAnswerSum = FeatureExtractionForSelenium.authorAnswers(doc1, m);       
							// 偶像数目
							AnswerAuthorFollowingSum = FeatureExtractionForSelenium.authorFollowing(doc1, m);
							// 提问总数
							AnswerAuthorQuestionSum = FeatureExtractionForSelenium.authorQuestions(doc1, m);
							// 文件爬取时间
							AuthorCrawlerTime = FeatureExtractionForSelenium.getCrawlerTime(AuthorPath);
						}
						
						// 创建object数组
						Object[] AnswerObject = new Object[] { AnswerId,AnswerContent,
								AnswerType, AnswerUpvote, AnswerLink,
								AnswerComment, AnswerAuthorFollower, AnswerAuthorProfession,
								AnswerAuthorKnowAbout, AnswerAuthorAnswerSum, 
								AnswerWordLength, AswerExist, AnswerSequence,
								AnswerAuthorFollowingSum, AnswerAuthorQuestionSum, AuthorCrawlerTime};
						
						//执行sql语句
						mysqlCon.doSql(sql, AnswerObject);
						
						//判断是否插入成功
						i = mysqlCon.getUpdateCount();
						if (i != -1) {
//							System.out.println("数据插入成功！");
						} else {
							System.out.println("数据插入失败！");
						}
						
						// 关闭链接
						mysqlCon.getClose();

					}
				}
			}
		} catch (Exception ex) {
			System.out.println("Error : " + ex.toString());
		}
	}

}
