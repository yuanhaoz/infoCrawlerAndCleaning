package informationExtraction;

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
import mysql.mysqlconnection;

import org.jsoup.nodes.Document;

import dataCollection.datacollection;
import Jsoup.JsoupParse;
import basic.keywordcatalogdesign;

public class informationextractiontomysql {

	// 导入mysqloperation类
	mysqlconnection mysqlCon = new mysqlconnection();

	// 准备sql语句
	private String sql;

	// 影响行数（数据变更后，影响行数都是大于0，等于0时没变更，所以说如果变更失败，那么影响行数必定为负）
//	private int i = -1;

	// 结果集
	// private ResultSet rs;

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
//		analysis("Data_structure");
		facetanalysis("Data_structure");
		long end = System.currentTimeMillis();
		System.out.println("总共用时： " + (end - start) / 1000 + " 秒...");
	}

	/**
	 * 填写fragment表 和 fragment_term表
	 * 
	 * @author zhengyuanhao
	 */
	public static void analysis(String course) throws Exception {
		informationextractiontomysql m = new informationextractiontomysql();
		File file0 = new File("file/datacollection/" + course);
		// File file0 = new File("F:/术语/课程术语1/" + course);
		File[] files = file0.listFiles();
		for (int i = 0; i < files.length; i++) {
			String keyword = files[i].getName();
			if (!keyword.contains(".html")) {
				int pagelength = informationextractiontoexcel
						.QuestionPageNumber(keyword);
//				m.fragment(keyword, pagelength);             // 填写fragment表
//				m.fragment_filtering(keyword, pagelength);   // 填写fragment表（过滤处理后的）
				m.fragment_term(keyword, pagelength);        // 填写fragment_term表
				System.out.println(keyword + "的信息已经保存到数据库...");
			}
		}
	}

	/**
	 * 填写assemble装配表，将知识碎片与分面之间的关系写入表格
	 * 
	 * @author zhengyuanhao
	 */
	public static void facetanalysis(String course) throws Exception {
		informationextractiontomysql m = new informationextractiontomysql();

		// 填写Binary+tree的装配表
		m.assemble("Binary+tree");

		// 填写所有主题的装配表
		// File file0 = new File("file/datacollection/" + course);
		// File[] files = file0.listFiles();
		// for (int i = 0; i < files.length; i++) {
		// String keyword = files[i].getName();
		// if(!keyword.contains(".html")){
		// m.assemble(keyword);
		// System.out.println(keyword + "知识碎片分面对应关系填写完毕...");
		// }
		// }
	}

	/**
	 * 解析问题网页，将其保存到数据库，填写fragment表（过滤前的）
	 */
	public void fragment(String keyword, int pagelength) throws Exception {
		try {
			// 解析数据，数据加到数据库里面
			String catalog = keywordcatalogdesign.GetKeywordCatalog(keyword);

			// 得到问题网页的链接
			String[] QuestionUrls = datacollection.GetQuestionURLs(keyword);
			// System.out.println("链接数目为：" + pagelength);

			// 解析每个问题网页的问题和回答
			for (int j = 0; j < pagelength; j++) {
				String path = catalog + keyword + j + ".html";
				File file = new File(path);
				if (!file.exists()) {
					System.out.println(path + "  不存在，请重新爬取数据...");
				} else {
					System.out.println("\n开始解析： " + path);
					Document doc = JsoupParse.parsePathText(path);

					// 得到问题的各字段信息，没有作者ID，爬取时间为当前解析数据的时间
					String keywordstore = keyword.replaceAll("\\+", "\\_");
					// System.out.println("转义以后：" + keywordstore);
					String QuestionId = keywordstore + j + ""; // 得到问题ID
					String SourceType = "Quora"; // 得到问题来源
					String URL = QuestionUrls[j]; // 得到碎片链接URL
					// System.out.println("url为：" + URL);
					String QuestionContent = featureExtraction
							.QuestionContent(doc)
							+ "\n"
							+ "Expanded information："
							+ featureExtraction.QuestionExpandInfo(doc); // 得到碎片内容
					SimpleDateFormat df = new SimpleDateFormat(
							"yyyy/MM/dd HH:mm:ss");// 设置日期格式
					String CrawlerTime = df.format(new Date()); // new
																// Date()为获取当前系统时间
					System.out.println("CrawlerTime1 is ：" + CrawlerTime);
					String AuthorID = "0";
					String media_type = "text"; // 数据是文本类型的text
					String evaluation = "1"; // 数据是否可用，默认都是1

					// 创建sql语句
					sql = "replace into fragment values (?, ?, ?, ?, ?, ?, ?, ?)";
					// 创建object数组
					Object[] questionobject = new Object[] { QuestionId,
							SourceType, URL, QuestionContent, CrawlerTime,
							AuthorID, media_type, evaluation };
					// 执行sql语句
					mysqlCon.doSql(sql, questionobject);
					// 获取影响行数
//					i = mysqlCon.getUpdateCount();
//					// 判断是否插入成功
//					if (i != -1) {
//						System.out.println("数据插入成功！");
//					} else {
//						System.out.println("数据插入失败！");
//					}
					// 关闭链接
					mysqlCon.getClose();

					// 得到答案数据
					int realanswernumber = featureExtraction
							.CountRealAnswerNumber(doc);
					for (int m = 0; m < realanswernumber; m++) {

						// 得到答案的各字段信息，没有作者ID，爬取时间为当前解析数据的时间
						String AnswerId = QuestionId + "_" + m; // 得到答案ID
						String AnswerContent = featureExtraction.AnswerContent(
								doc, m);
						; // 得到碎片内容

						// 创建object数组
						Object[] answerobject = new Object[] { AnswerId,
								SourceType, URL, AnswerContent, CrawlerTime,
								AuthorID, media_type, evaluation };
						mysqlCon.doSql(sql, answerobject);
//						i = mysqlCon.getUpdateCount();
//						if (i != -1) {
//							System.out.println("数据插入成功！");
//						} else {
//							System.out.println("数据插入失败！");
//						}
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
	 * 数据过滤
	 */
	public String keywordandlength(String content, String[] keywordarray) {
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
		if (count != 0 && length < 500) {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * 填写fragment表（过滤后的）
	 */
	public void fragment_filtering(String keyword, int pagelength)
			throws Exception {
		try {
			// 解析数据，数据加到数据库里面
			String catalog = keywordcatalogdesign.GetKeywordCatalog(keyword);

			// 得到问题网页的链接
			String[] QuestionUrls = datacollection.GetQuestionURLs(keyword);
			// System.out.println("链接数目为：" + pagelength);

			// 将关键词以+分开存到数组里面，用于匹配文本
			String[] keywordarray = keyword.split("\\+");

			// 解析每个问题网页的问题和回答
			for (int j = 0; j < pagelength; j++) {
				String path = catalog + keyword + j + ".html";
				File file = new File(path);
				if (!file.exists()) {
					System.out.println(path + "  不存在，请重新爬取数据...");
				} else {
					System.out.println("\n开始解析： " + path);
					Document doc = JsoupParse.parsePathText(path);

					// 得到问题的各字段信息，没有作者ID，爬取时间为当前解析数据的时间
					String keywordstore = keyword.replaceAll("\\+", "\\_");
					// System.out.println("转义以后：" + keywordstore);
					String QuestionId = keywordstore + j + ""; // 得到问题ID
					String SourceType = "Quora"; // 得到问题来源
					String URL = QuestionUrls[j]; // 得到碎片链接URL
					// System.out.println("url为：" + URL);
					String QuestionContent = featureExtraction.QuestionContent(doc)
							+ "\n" + "Expanded information："
							+ featureExtraction.QuestionExpandInfo(doc); // 得到碎片内容
					SimpleDateFormat df = new SimpleDateFormat(
							"yyyy/MM/dd HH:mm:ss");// 设置日期格式
					String CrawlerTime = df.format(new Date()); // new
																// Date()为获取当前系统时间
					System.out.println("CrawlerTime1 is ：" + CrawlerTime);
					String AuthorID = "0";
					String media_type = "text"; // 数据是文本类型的text

					// 判断数据过滤，关键词匹配和长度判断
					informationextractiontomysql ms = new informationextractiontomysql();
					String questionevaluation = ms.keywordandlength(
							QuestionContent, keywordarray);

					// 创建sql语句
					sql = "replace into fragment values (?, ?, ?, ?, ?, ?, ?, ?)";
					Object[] questionobject = new Object[] { QuestionId,
							SourceType, URL, QuestionContent, CrawlerTime,
							AuthorID, media_type, questionevaluation };
					mysqlCon.doSql(sql, questionobject);
//					i = mysqlCon.getUpdateCount();
//					if (i != -1) {
//						System.out.println("数据插入成功！");
//					} else {
//						System.out.println("数据插入失败！");
//					}
					// 关闭链接
					mysqlCon.getClose();

					// 得到答案数据
					int realanswernumber = featureExtraction
							.CountRealAnswerNumber(doc);
					for (int m = 0; m < realanswernumber; m++) {

						// 得到答案的各字段信息，没有作者ID，爬取时间为当前解析数据的时间
						String AnswerId = QuestionId + "_" + m; // 得到答案ID
						String AnswerContent = featureExtraction.AnswerContent(doc, m);  // 得到碎片内容

						// 判断数据过滤，关键词匹配和长度判断
						String answerevaluation = ms.keywordandlength(
								AnswerContent, keywordarray);

						// 创建object数组
						Object[] answerobject = new Object[] { AnswerId,
								SourceType, URL, AnswerContent, CrawlerTime,
								AuthorID, media_type, answerevaluation };
						mysqlCon.doSql(sql, answerobject);
//						i = mysqlCon.getUpdateCount();
//						if (i != -1) {
//							System.out.println("数据插入成功！");
//						} else {
//							System.out.println("数据插入失败！");
//						}
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
	 * 解析碎片与分面之间的关系，填写assemble表（表格变化） （针对二叉树Binary_tree）
	 */
	public void assemble(String keyword) throws Exception {
		try {
			// 解析数据，数据加到数据库里面
			String catalog = keywordcatalogdesign.GetKeywordCatalog(keyword);
			String path = catalog + keyword + "-tag_changed.xls";
			File file = new File(path);
			if (!file.exists()) {
				System.out.println(path + "  不存在，请重新生成解析表格...");
			} else {
				System.out.println("\n开始对应： " + path);
				Workbook book = Workbook.getWorkbook(file);
				Sheet sheet = book.getSheet(0);
				int row = sheet.getRows();
				String[] facetname = { "definition", "feature",
						"implementation", "example", "operation",
						"application", "method", "type", "relevant", "history",
						"description", "purpose", "explanation", "storage",
						"simulator" };
				for (int i = 0; i < row; i++) {
					Cell cell0 = sheet.getCell(0, i);
					Cell cell12 = sheet.getCell(12, i);
					if (cell12.getContents().equals("1")) {
						String fragmentid = cell0.getContents().replaceAll("\\+", "\\_");
						int facetid;
						Cell cell13 = sheet.getCell(13, i);
						Cell cell14 = sheet.getCell(14, i);
						Cell cell15 = sheet.getCell(15, i);
						Cell cell16 = sheet.getCell(16, i);
						String[] tag = { cell14.getContents(),
								cell15.getContents(), cell16.getContents(),
								cell13.getContents() };
						for (int m = 0; m < tag.length; m++) {
							for (int n = 0; n < facetname.length; n++) {
								if (tag[m].equals(facetname[n])) {
									facetid = n + 1;

									sql = "replace into assemble values (?, ?, ?, ?, ?)";
									Object[] answerobject = new Object[] { 52,
											facetid, 1, fragmentid, " " };
									mysqlCon.doSql(sql, answerobject);
//									i = mysqlCon.getUpdateCount();
//									if (i != -1) {
//										System.out.println("数据插入成功！");
//									} else {
//										System.out.println("数据插入失败！");
//									}
									// 关闭链接
									mysqlCon.getClose();

									System.out.println(fragmentid + "   "
											+ facetid + "  52");
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("Error : " + ex.toString());
		}
	}

	/**
	 * 填写fragment_term表（填写fragment和以前的主题词的关系表）
	 * 读取term表
	 * @throws Exception 
	 */
	public void fragment_term(String keyword, int pagelength) throws Exception {
		Connection conn;
		PreparedStatement ps;
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		
		// 建立到MySQL的连接（注意数据库IP）
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/knowledgeforest", "root","199306");
//		conn = DriverManager.getConnection("jdbc:mysql://202.117.16.39:3306/knowledgeforest", "e-learning","knowledgeforest");
//		conn = DriverManager.getConnection("jdbc:mysql://202.117.16.39:3306/knowledgeforestlocal", "e-learning","knowledgeforest");
//		conn = DriverManager.getConnection("jdbc:mysql://202.117.54.43:3306/knowledgeforest", "e-learning","knowledgeforest");
		
		//查询term表，得到关键词表
		ps = conn.prepareStatement("select * from term");
		ResultSet rs = ps.executeQuery();
		int length = 0;
		while(rs.next()){
			length++;            //得到term总数
		}    
		System.out.println("共有term数目为：" + length);
		
		//得到term表的主题词名和对应id
		String[] topic = new String[length];
		int[] term = new int[length];
		ResultSet rs1 = ps.executeQuery();
		int index = 0;
		while(rs1.next()){
		        int term_id = rs1.getInt(1);
		        String domterm_name = rs1.getString(2);
		        topic[index] = domterm_name;
		        term[index] = term_id;
		        index++;
		}
		
		//比较关键词和主题词，判断关键词是否在term表中是否存在
		String keywordchange = keyword.replaceAll("\\+", "\\_");
		for (int i = 0; i < topic.length; i++) {
			if(keywordchange.equals(topic[i])){
				String catalog = keywordcatalogdesign.GetKeywordCatalog(keyword);
				for (int j = 0; j < pagelength; j++) {
					String path = catalog + keyword + j + ".html";
					File file = new File(path);
					Document doc = JsoupParse.parsePathText(path);
					if (!file.exists()) {
						System.out.println(path + "  不存在，请重新爬取数据...");
					} else {
						String QuestionId = keywordchange + j + "";  //得到问题ID
						int term_id = term[i];
//						System.out.println(QuestionId + "   " + term_id);
						// 创建sql语句
						sql = "replace into fragment_term values (?, ?)";
						Object[] answerobject = new Object[] { QuestionId, term_id };
						mysqlCon.doSql(sql, answerobject);
//						i = mysqlCon.getUpdateCount();
//						if (i != -1) {
//							System.out.println("数据插入成功！");
//						} else {
//							System.out.println("数据插入失败！");
//						}
						// 关闭链接
						mysqlCon.getClose();

					}
					int realanswernumber = featureExtraction.CountRealAnswerNumber(doc);
					for (int m = 0; m < realanswernumber; m++) {
						String AnswerId = keywordchange + j + "_" + m;  //得到答案ID
						int term_id = term[i];
//						System.out.println(AnswerId + "   " + term_id);
						// 创建sql语句
						sql = "replace into fragment_term values (?, ?)";
						Object[] answerobject = new Object[] { AnswerId, term_id };
						mysqlCon.doSql(sql, answerobject);
//						i = mysqlCon.getUpdateCount();
//						if (i != -1) {
//							System.out.println("数据插入成功！");
//						} else {
//							System.out.println("数据插入失败！");
//						}
						// 关闭链接
						mysqlCon.getClose();

					}
				}
			}
		}
	}

	/**
	 * 填写主题词表（自己爬取的数据结构的54个主题词）
	 * 
	 * （暂时没有用处）
	 * 
	 * @throws Exception
	 */
	public void topic(String course) throws Exception {
		// 填表
		File file0 = new File("file/datacollection/" + course);
		// File file0 = new File("f:/术语/课程术语/" + course);
		File[] files = file0.listFiles();
		for (int i = 0; i < files.length; i++) {
			int topic_id = i + 1;
			String topic_name = files[i].getName();
			topic_name = URLDecoder.decode(topic_name, "UTF-8");
			// 向MYSQL里面添加记录

			// 创建sql语句
			sql = "insert into domain_topic values (?, ?)";
			// 创建object数组
			Object[] answerobject = new Object[] { topic_id, topic_name };
			// 执行sql语句
			mysqlCon.doSql(sql, answerobject);
			// 获取影响行数
			i = mysqlCon.getUpdateCount();
			// 判断是否插入成功
			if (i != -1) {
				System.out.println("数据插入成功！");
			} else {
				System.out.println("数据插入失败！");
			}
			// 关闭链接
			mysqlCon.getClose();

			System.out.println(topic_id + "   " + topic_name);
		}
	}

}
