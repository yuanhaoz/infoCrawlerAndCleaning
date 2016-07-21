package data;

import java.io.File;
import java.util.ArrayList;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import informationextraction.FeatureExtractionForSelenium;
import informationextraction.InformationExtraction2MysqlNew;

import org.jsoup.nodes.Document;

import basic.MysqlConnection_binarytree;
import excel.ExcelSet;
import Jsoup.JsoupParse;

public class ExtractionQuora2 {

	// 导入mysqloperation类
	static MysqlConnection_binarytree mysqlCon = new MysqlConnection_binarytree();
	// 准备sql语句
	private static String sql;
	// 影响行数（数据变更后，影响行数都是大于0，等于0时没变更，所以说如果变更失败，那么影响行数必定为负）
	// private int i = -1;
	// 结果集
	// private ResultSet rs;
	private static String cata = "02-CQA网站中问题答案质量评估";
	private static String path = "F:\\"+cata+"\\二叉树\\quora\\question";
	private static String pathExcel = "F:\\"+cata+"\\二叉树\\quora\\question\\excel";
	private static String pathAuthor = "F:\\"+cata+"\\二叉树\\quora\\question\\authorPage";
	private static FeatureExtractionForSelenium extract = new FeatureExtractionForSelenium();
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		test_single();
//		test1();
		extract2Excel("Binary_tree",400);
//		extract2mysql("Binary_tree", 400, "Binary_tree");
	}
	
	public static void test_single() throws Exception{
		String filePath = path + "\\Binary_tree0.html";
		Document doc = JsoupParse.parsePathText(filePath);
		int n = extract.countRealAnswerNumber2(doc);
		extract.questionContent(doc);
		extract.questionExpandInfo(doc);
		extract.questionWantAnswers(doc);
		extract.questionCommentNumbers(doc);
		for(int j = 0; j < n; j++){
			
			extract.answerContent(doc, j);
			extract.answerUpvotes(doc, j);
			extract.answerCommentNumbers(doc, j);
			
			
		}
		String authorpath = pathAuthor + "\\Binary_tree0_author_0.html";
//		String authorpath = pathAuthor + "\\Binary_tree0_author_1.html";
//		String authorpath = pathAuthor + "\\Binary_tree0_author_2.html";
//		String authorpath = pathAuthor + "\\Binary_tree0_author_3.html";
		Document authorDoc = JsoupParse.parsePathText(authorpath);
		FeatureExtractionForSelenium.authorPosts(authorDoc, 0);
		FeatureExtractionForSelenium.authorEdits(authorDoc, 0);
	}
	
	public static void test_all() throws Exception{
		for(int i = 0; i < 400; i++){
			String filePath = path + "\\Binary_tree" + i +".html";
			System.out.println("\nprocessing file: "+filePath);
			Document doc = JsoupParse.parsePathText(filePath);
			int n = extract.countRealAnswerNumber2(doc);
			extract.questionContent(doc);
			extract.questionExpandInfo(doc);
			extract.questionWantAnswers(doc);
			extract.questionCommentNumbers(doc);
			for(int j = 0; j < n; j++){
				extract.answerContent(doc, j);
				extract.answerUpvotes(doc, j);
				extract.answerCommentNumbers(doc, j);
			}
		}
	}
	
	/**
	 * 保存从Quora网站上面爬取的数据：数据结构、数据挖掘、计算机网络
	 * 便于导出成Excel，而且节省存储空间
	 * 相对于存放在Excel，方便操作
	 * 可以导出成Excel进行分析
	 */
	public static void extract2mysql(String keyword, int pagelength, String course) throws Exception {
		try {
			// 将关键词以+分开存到数组里面，用于匹配文本
			String[] keywordarray = keyword.split("\\_");
			// 解析每个问题网页的问题和回答
			for (int j = 0; j < pagelength; j++) {
				String filePath = path + "\\" + keyword + j + ".html";
				File file = new File(filePath);
				if (!file.exists()) {
//					System.out.println(path + "  不存在，请重新爬取数据...");
				} else {
//					System.out.println("\n开始解析： " + path);
					Document doc = JsoupParse.parsePathText(filePath);
					// 得到问题的各字段信息，没有作者ID，爬取时间为当前解析数据的时间
					// 得到问题ID
					String keywordstore = keyword;
					String QuestionId = keywordstore + j + ""; 
					// 得到碎片内容
					String QuestionContent = FeatureExtractionForSelenium.questionContent(doc) + "\n"
					+ FeatureExtractionForSelenium.questionExpandInfo(doc);
					QuestionContent = UTF8Code.unicodeReplace(QuestionContent).toString();
					// 文本是问题还是答案
					String QuestionType = "1";
					// 问题的关注人数
					String QuestionUpvote = FeatureExtractionForSelenium.questionWantAnswers(doc);
					// 问题是否含有链接
					String QuestionLink = "";
					// 问题是否含有评论
					String QuestionComment = FeatureExtractionForSelenium.questionCommentNumbers(doc);
					// 提问者粉丝数
//					String QuestionAuthorFollower = "";
					// 提问者职业
//					String QuestionAuthorProfession = "";
					// 提问者擅长领域
//					String QuestionAuthorKnowAbout = "";
					// 提问者回答总数
//					String QuestionAuthorAnswerSum = "";
					// 碎片单词长度
					int QuestionWordLength = FeatureExtractionForSelenium.questionContentWordLength(doc);
					// 是否存在关键词并且长度在[10~500之间]
					InformationExtraction2MysqlNew ms = new InformationExtraction2MysqlNew();
					String QuestionExist = ms.keywordAndLength(QuestionContent, keywordarray);
					// 问题序号
					String QuestionSequence = j + "";
//					String AuthorFollowing = "";
//					String AuthorQuestion = "";
					String crawlerTime = FeatureExtractionForSelenium.getCrawlerTime(filePath);
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
					} else if (course.equals("Binary_tree")) {
						sql = "replace into Binary_tree values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
					}
					Object[] QuestionObject = new Object[] { QuestionId, QuestionContent,
							QuestionType, QuestionUpvote, QuestionLink,	QuestionComment, 
							QuestionWordLength, QuestionExist, QuestionSequence, crawlerTime};
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
					int realanswernumber = FeatureExtractionForSelenium.countRealAnswerNumber2(doc);
					for (int m = 0; m < realanswernumber; m++) {
						// 得到答案的各字段信息
						// 得到答案ID
						String AnswerId = QuestionId + "_" + m;
//						String AnswerId = QuestionId;
						// 得到碎片内容
						String AnswerContent = FeatureExtractionForSelenium.answerContent(doc, m);
						AnswerContent = UTF8Code.unicodeReplace(AnswerContent).toString();
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
//						String AuthorPath = catalog + keyword + j + "_author_" + m + ".html";
//						File file1 = new File(AuthorPath);
//						String AnswerAuthorFollower = null;
//						String AnswerAuthorProfession = null;
//						String AnswerAuthorKnowAbout = null;
//						String AnswerAuthorAnswerSum = null;
//						String AnswerAuthorQuestionSum = null;
//						String AnswerAuthorFollowingSum = null;
//						String AuthorCrawlerTime = null;
//						if(!file1.exists()){
////							System.out.println(AuthorPath + "   不存在");
//						}else{
//							Document doc1 = JsoupParse.parsePathText(AuthorPath);  //解析作者页面路径
//							// 回答者粉丝数
//							AnswerAuthorFollower = FeatureExtractionForSelenium.authorFollowers(doc1, m);
//							// 作者简历
//							AnswerAuthorProfession = FeatureExtractionForSelenium.authorName(doc, m);   
//							// 作者领域
//							AnswerAuthorKnowAbout = FeatureExtractionForSelenium.authorKnowAbout(doc1, m);  
//							// 回答总数
//							AnswerAuthorAnswerSum = FeatureExtractionForSelenium.authorAnswers(doc1, m);       
//							// 偶像数目
//							AnswerAuthorFollowingSum = FeatureExtractionForSelenium.authorFollowing(doc1, m);
//							// 提问总数
//							AnswerAuthorQuestionSum = FeatureExtractionForSelenium.authorQuestions(doc1, m);
//							// 文件爬取时间
//							AuthorCrawlerTime = FeatureExtractionForSelenium.getCrawlerTime(AuthorPath);
//						}
						// 创建object数组
						Object[] AnswerObject = new Object[] { AnswerId, AnswerContent,
								AnswerType, AnswerUpvote, AnswerLink, AnswerComment, 
								AnswerWordLength, AswerExist, AnswerSequence, crawlerTime};
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
	 * 实现功能：解析问题网页和作者网页，实现信息的抽取，将其保存到本地的Excel表中
	 *          输入是关键词和问题页面数量，输出是保存信息抽取结果的Excel
	 * @param course
	 */
	public static void extract2Excel(String keyword, int pageLength) throws Exception {
		//建立保存目录
		new File(pathExcel).mkdir();
		String filepath = pathExcel + "\\" + keyword + ".xls";
		WritableWorkbook workbook = Workbook.createWorkbook(new File(filepath));
		WritableSheet sheet = workbook.createSheet("信息抽取", 0);
		initalTitle(sheet);
		WritableCellFormat wcf_center = ExcelSet.setCenterText();   //设置单元格正文格式
		
		//存放信息
		int number = 1;
		for (int i = 0; i < pageLength; i++) {
			String filePath = path + "\\" + keyword + i + ".html";
			File file = new File(filePath);
			if (!file.exists()) {
//				System.out.println(path + "  不存在");
			} else {
				
				//开始解析问题页面，将问题的有关信息填入表格之中
//				System.out.println("开始解析： " + path);
				Document doc = JsoupParse.parsePathText(filePath);
				
				//得到主题网页爬取时间
				String subjectpagecrawlertime = FeatureExtractionForSelenium.getCrawlerTime(filePath);
				
				ArrayList<String> titlelist = new ArrayList<String>();
//				ArrayList<String> infolist = new ArrayList<String>();
				titlelist.add(keyword + i);
				String textDeal = UTF8Code.unicodeReplace(FeatureExtractionForSelenium.questionContent(doc) 
						+ "\n" 
						+ FeatureExtractionForSelenium.questionExpandInfo(doc)).toString();
				titlelist.add(textDeal);
				titlelist.add("1");
				titlelist.add(FeatureExtractionForSelenium.questionWantAnswers(doc));
				titlelist.add("");
				titlelist.add(FeatureExtractionForSelenium.questionCommentNumbers(doc));
				titlelist.add("");
				titlelist.add("");
				titlelist.add("");
				titlelist.add("");
				titlelist.add(FeatureExtractionForSelenium.questionContentWordLength(doc) + "");
				titlelist.add("1");
				titlelist.add(i + "");
				for(int j = 0; j < 13; j++){
					sheet.addCell(new Label(j, number, titlelist.get(j), wcf_center));
				}
				// 作者偶像、作者提问数、爬取时间
				sheet.addCell(new Label(18, number, "", wcf_center));
				sheet.addCell(new Label(19, number, "", wcf_center));
				sheet.addCell(new Label(20, number, subjectpagecrawlertime, wcf_center));
				sheet.addCell(new Label(21, number, "", wcf_center));
				sheet.addCell(new Label(22, number, "", wcf_center));
				
				sheet.setRowView(number, 1300, false); // 设置行高
				sheet.setColumnView(0, 20);
				sheet.setColumnView(1, 60);
				
				//将问题下的回答和作者的信息填入表中
				int realanswernumber = FeatureExtractionForSelenium.countRealAnswerNumber2(doc);
				for (int m = number; m < number + realanswernumber; m++) {
					
					sheet.setRowView(m + 1, 1300, false);// 设置行高
					String answercontent = FeatureExtractionForSelenium.answerContent(doc, m - number);
					answercontent = UTF8Code.unicodeReplace(answercontent).toString();
					int contentlength = FeatureExtractionForSelenium.answerContentWordLength(doc, m - number);// 单词长度
					String upvote = FeatureExtractionForSelenium.answerUpvotes(doc, m - number);              // 支持票数 
					String url = FeatureExtractionForSelenium.answerURLs(doc, m - number);// 链接有无
					String comment = FeatureExtractionForSelenium.answerCommentNumbers(doc, m - number);// 评论数量
					String authorName = FeatureExtractionForSelenium.authorName(doc, m - number);            // 作者姓名
					
					String follower = null;
					String know = null;
					String answer = null;
					String following = null;
					String questions = null;
					String posts = null;
					String edits = null;
					
					String authorpath = pathAuthor + "\\" + keyword + i + "_author_" + (m - number) + ".html";
					
					File file1 = new File(authorpath);
					if(!file1.exists()){
						System.out.println(authorpath + "   不存在");
					}else{
						Document authorDoc = JsoupParse.parsePathText(authorpath);  //解析作者页面路径
						
						follower = FeatureExtractionForSelenium.authorFollowers(authorDoc, m - number);   // 作者粉丝
						know = FeatureExtractionForSelenium.authorKnowAbout(authorDoc, m - number);       // 作者领域
						answer = FeatureExtractionForSelenium.authorAnswers(authorDoc, m - number);       // 回答总数
						following = FeatureExtractionForSelenium.authorFollowing(authorDoc, m - number);  // 作者偶像
						questions = FeatureExtractionForSelenium.authorQuestions(authorDoc, m - number);  // 提问总数
						posts = FeatureExtractionForSelenium.authorPosts(authorDoc, m - number);  // 提问总数
						edits = FeatureExtractionForSelenium.authorEdits(authorDoc, m - number);  // 提问总数
					}
					
					sheet.addCell(new Label(0, m + 1, keyword + i + "_" + (m - number) + " ",wcf_center));
					sheet.addCell(new Label(1, m + 1, answercontent, wcf_center));
					sheet.addCell(new Label(2, m + 1, "0", wcf_center));
					sheet.addCell(new Label(3, m + 1, upvote, wcf_center));
					sheet.addCell(new Label(4, m + 1, url, wcf_center));
					sheet.addCell(new Label(5, m + 1, comment, wcf_center));
					sheet.addCell(new Label(6, m + 1, follower, wcf_center));
					sheet.addCell(new Label(7, m + 1, authorName, wcf_center));
					sheet.addCell(new Label(8, m + 1, know, wcf_center));
					sheet.addCell(new Label(9, m + 1, answer, wcf_center));
					sheet.addCell(new Label(10, m + 1, contentlength + "",wcf_center));
					sheet.addCell(new Label(11, m + 1, "1", wcf_center));
					sheet.addCell(new Label(12, m + 1, i + "", wcf_center));
					// 作者偶像、作者提问数、爬取时间
					sheet.addCell(new Label(18, m + 1, following, wcf_center));
					sheet.addCell(new Label(19, m + 1, questions, wcf_center));
					sheet.addCell(new Label(20, m + 1, subjectpagecrawlertime, wcf_center));
					sheet.addCell(new Label(21, m + 1, posts, wcf_center));
					sheet.addCell(new Label(22, m + 1, edits, wcf_center));
					
				}
				number = number + realanswernumber + 1;
			}
//			System.out.println(path + " 已经成功解析到 " + catalog + keyword + "-tag.xls");
		}
		ExcelSet.close(workbook);  //关闭工作空间
	}

	/**
	 * 实现功能：建立工作表，输入是表格存储路径filepath，输出是表格对象WritableSheet
	 * @param filepath
	 */
	public static WritableSheet createSheet(String filepath) throws Exception {
		WritableWorkbook workbook = Workbook.createWorkbook(new File(filepath));
		WritableSheet sheet = workbook.createSheet("信息抽取", 0);
		return sheet;
	}
	
	/**
	 * 实现功能：设置表格第一行
	 * @param filepath
	 */
	public static void initalTitle(WritableSheet sheet) throws Exception {
		WritableCellFormat wcf_title = ExcelSet.setTitleText();   //设置单元格正文格式
		sheet.addCell(new Label(0, 0, "QA", wcf_title));
		sheet.addCell(new Label(1, 0, "Content", wcf_title));
		sheet.addCell(new Label(2, 0, "QT(1) or AS(0)", wcf_title));
		sheet.addCell(new Label(3, 0, "Upvote", wcf_title));
		sheet.addCell(new Label(4, 0, "Link", wcf_title));
		sheet.addCell(new Label(5, 0, "Comment", wcf_title));
		sheet.addCell(new Label(6, 0, "Follower", wcf_title));
		sheet.addCell(new Label(7, 0, "AuthorName", wcf_title));
		sheet.addCell(new Label(8, 0, "KnowAbout", wcf_title));
		sheet.addCell(new Label(9, 0, "AnswerSum", wcf_title));
		sheet.addCell(new Label(10, 0, "Word", wcf_title));
		sheet.addCell(new Label(11, 0, "Exist(1)", wcf_title));
		sheet.addCell(new Label(12, 0, "Sequence", wcf_title));
//		sheet.addCell(new Label(13, 0, "有用（1）", wcf_title));
		
		sheet.addCell(new Label(18, 0, "AuthorFollowing", wcf_title));
		sheet.addCell(new Label(19, 0, "AuthorQuestion", wcf_title));
		sheet.addCell(new Label(20, 0, "crawlerTime", wcf_title));
		sheet.addCell(new Label(21, 0, "AuthorPosts", wcf_title));
		sheet.addCell(new Label(22, 0, "AuthorEdits", wcf_title));
		
		sheet.setRowView(0, 700, false);                         // 设置行高
	}

}
