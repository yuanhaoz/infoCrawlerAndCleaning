package xjtu.sky.quoraStudy;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Border;
import jxl.write.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import base.DirFile;
import xjtu.sky.excel.ExcelSet;

@SuppressWarnings("deprecation")
public class tag2 {
	
	public static void main(String[] args) throws Exception {
		realize();
	}
	
	
	/**
	 * 实现功能：实现爬虫与清洗
	 * @param course
	 */
	public static void realize() throws Exception {
		Crawler("Computer_network");
//		Analysis("Computer_network");
	}
	
	/**
	 * 实现功能：用于爬取某门课程所有主题下的所有网页
	 *          输入是课程名，输出是所有网页
	 * @param course
	 */
	public static void Crawler(String course) throws Exception {
		String catalog = "F:/术语/课程术语/" + course;
		ArrayList<String> a = DirFile.getFileNamesFromDirectorybyArraylist(catalog);  //读取所有文件名
		setKeywordCatalog(course);           //为关键词建立目录
		Iterator<String> it = a.iterator();   //设置迭代器
		while(it.hasNext()){                  //判断是否有下一个
			long start = System.currentTimeMillis();
			String keyword = it.next();       //得到关键词
			CrawlerSubjectPages(keyword);     //爬取主题页面
			CrawlerQuestionAndAuthorPages(keyword);            //爬取问题页面和作者页面
			long end = System.currentTimeMillis();
			System.out.println("爬取" + keyword + "的所有信息用时：" + (end - start)/1000 + "秒...");
		}
	}
	
	/**
	 * 实现功能：用于解析某门课程所有主题下的所有网页
	 *          输入是课程名，输出是Excel
	 * @param course
	 */
	public static void Analysis(String course) throws Exception {
		KeyWordMatch k = new KeyWordMatch();
		String catalog = "F:/术语/课程术语/" + course;
		ArrayList<String> a = DirFile.getFileNamesFromDirectorybyArraylist(catalog);  //读取所有文件名
		Iterator<String> it = a.iterator();   //设置迭代器
		while(it.hasNext()){                  //判断是否有下一个
			long start = System.currentTimeMillis();
			String keyword = it.next();       //得到关键词
			int pagelength = QuestionPageNumber(keyword);      
			Down2Excel(keyword, pagelength);                   //解析问题页面和作者页面
//			k.match3(keyword);    //生成-changed文件，保留关键词匹配结果和单词长度信息
//			k.match(keyword);     //生成-changed1文件，只保留关键词匹配成立，单词长度未处理
//			k.match1(keyword);    //生成-changed2文件，只保留关键词匹配成立，且单词长度在0-500之间的
			long end = System.currentTimeMillis();
			System.out.println("解析" + keyword + "的所有信息用时：" + (end - start)/1000 + "秒...");
		}
	}

	/**
	 * 实现功能：根据课程名，为该课程目录下的所有关键词页面生成对应文件夹
	 *          输入是课程名，输出是生成相应文件夹
	 * @param course
	 */
	public static void setKeywordCatalog(String course) {
		String catalog = "F:/术语/课程术语/" + course + "/";
		ArrayList<String> a = DirFile.getFileNamesFromDirectorybyArraylist(catalog);  //读取所有文件名
		Iterator<String> it = a.iterator();
		while(it.hasNext()){                  //判断是否有下一个
			String keyword = it.next();
			System.out.println(course + "课程目录下有如下关键词：" + keyword); 
			String keywordcatalog = catalog + keyword + "/";
			File file = new File(keywordcatalog);
			if (!file.exists()) {
				file.mkdir();
			}
		}
	}
	
	/**
	 * 实现功能：根据关键词，定位到相应关键词目录
	 *  	        用于存放“主题”、“问题”和“作者”页面
	 *          输入是关键词，输出是生成相应文件夹
	 * @param keyword
	 */
	public static String GetKeywordCatalog(String keyword) {
		String keywordcatalog = "";
		String[] course = {"Computer_network", "Data_mining", "Data_structure"};
		for(int i = 0; i < course.length; i++){
			String catalog = "F:/术语/课程术语/" + course[i] + "/";
			ArrayList<String> a = DirFile.getFileNamesFromDirectorybyArraylist(catalog);  //读取所有文件名
			Iterator<String> it = a.iterator();
			while(it.hasNext()){                  //判断是否有下一个
				String testkeyword = it.next();
//				System.out.println(course + "课程目录下有如下关键词：" + keyword);
				if(keyword.equals(testkeyword)){
					keywordcatalog = catalog + keyword + "/";
				}
//				else{
//					keywordcatalog = catalog + "other" + "/";
//					File file = new File(keywordcatalog);
//					if (!file.exists()) {
//						file.mkdir();
//					}
//				}
			}
		}
		return keywordcatalog;
	}
	
	/**
	 * 实现功能：根据关键词，得到主题页面的链接（一个页面）
	 * @param keyword
	 */
	public static String GetSubjectURLs(String keyword){
		String url = "http://www.quora.com/search?q=" + keyword;
		System.out.println("主题页面链接为：" + url);
		return url;
	}
	
	/**
	 * 实现功能：解析主题网页，
	 *          得到主题页面中所有问题页面的链接
	 *          链接数量一般在49-59之间（爬取时拉动四次）
	 * @param keyword
	 */
	public static String[] GetQuestionURLs(String keyword) throws Exception {
		String path = tag2.GetKeywordCatalog(keyword) + keyword + ".html";
		// System.out.println(path);
		File file = new File(path);
		if (!file.exists()) {
			String[] testresult = { "aa", "bb" };
			System.out.println(path + "  不存在，得不到它的子页面链接！！！");
			return testresult;
		} else {
			Document doc = JsoupParse.parsePathText(path);
			Elements links = doc.select("div.pagedlist_item").select("div.title ")
					.select("a.question_link").select("a[href]");
			// print("\n" + path + " 所有问题的链接  Links: (%d)", links.size());
			String urls[] = new String[links.size()];
			for (int i = 0; i < links.size(); i++) {
				Element link = links.get(i);
				urls[i] = "http://www.quora.com" + link.attr("href"); // 将所有链接存到urls数组里面
				print("问题页面链接为：" + " * " + (i + 1) + ": <%s>  (%s)", urls[i], trim(link.text(), 50));
			}
			return urls;
		}
	}
	
	/**
	 * 实现功能：解析问题页面，
	 *          得到问题页面中所有作者页面的链接
	 *          keyword是主题词，n 是主题页面下的问题页面的序号
	 * @param keyword,n
	 */
	public static String[] GetAuthorURLs(String keyword, int n){
		String path = tag2.GetKeywordCatalog(keyword);
		String filepath = path + keyword + n + ".html";
		Document doc = JsoupParse.parsePathText(filepath);
		Elements authors = doc.select("div.grid_page_center_col").select("div.pagedlist_item").select("div.author_info");
		String[] url = new String[authors.size() - 3];    //最后有三个作者是例外
		for (int m = 0; m < authors.size() - 3; m++) {
			Element a = authors.get(m);
			Elements b = a.select("a.user");
			if (b.size() == 0) {
				System.out.println("作者信息不存在！！！");
			} else {
				Element author = b.get(0);
				String urls = author.attr("href");
				if (urls.startsWith("http://")) {
					url[m] = urls;
				} else if (urls.startsWith("/")) {
					url[m] = "http://www.quora.com/" + urls;
				}
			}
		}
		return url;
	}
	
	/**
	 * 实现功能：爬取单个主题页面（爬取阶段1）
	 * 			输入是一门学科下面的单个主题词keyword
	 * @param keyword
	 */
	public static void CrawlerSubjectPages(String keyword) throws Exception {
		PageDown p = new PageDown();
		String keywordcatalog = tag2.GetKeywordCatalog(keyword);   //得到主题文件夹
		String filepath = keywordcatalog + keyword + ".html";      //设置主题页面保存路径
		String subjectUrls = tag2.GetSubjectURLs(keyword);         //得到主题页面链接
		p.seleniumCrawlerSubject(filepath, subjectUrls);           //爬取主题页面
	}
	
/**
	 * 实现功能：爬取单个主题下的所有问题页面和作者页面（爬取阶段2）
	 * 			输入是主题词keyword
	 * @param keyword
	 */
	public static void CrawlerQuestionAndAuthorPages(String keyword) throws Exception {
		String catalog = tag2.GetKeywordCatalog(keyword);
		String[] urls = GetQuestionURLs(keyword);			   //得到问题页面链接
		String[] testresult = { "aa", "bb" };
		if (urls.equals(testresult)) {
			System.out.println("不存在第一层链接！！！");
		} else {
			for (int n = 0; n < urls.length; n++) {
				PageDown a = new PageDown();
				String filename = keyword + n + ".html";
				String filepath = catalog + filename;          //设置问题页面保存路径
				a.seleniumCrawlerQuestion(filepath, urls[n]);  //爬取问题网页
				CrawlerAuthorPages(keyword, n);                //爬取问题页面中的作者页面
			}
		}
	}

	/**
	 * 实现功能：爬取“某个”问题页面中的作者页面（爬取阶段2.1）
	 * @param keyword,n
	 */
	public static void CrawlerAuthorPages(String keyword, int n) throws Exception {
		String catalog = tag2.GetKeywordCatalog(keyword);
		String[] authorUrls = tag2.GetAuthorURLs(keyword, n);         //得到作者页面链接
		for (int m = 0; m < authorUrls.length; m++) {
			String filename = keyword + n + "_author_" + m + ".html";
			String filepath = catalog + filename;                     //设置作者页面保存路径
			File file1 = new File(filepath);						  //判断作者页面是否存在
			if(!file1.exists()){
				PageDown a = new PageDown();
				a.seleniumCrawlerAuthor(filepath, authorUrls[m]);     //爬取作者页面
			}else{
				System.out.println(filepath + "存在，不需要爬取...");
			}
		}
	}

	/**
	 * 得到pagelength是问题页面总数
	 * @throws Exception
	 */
	public static int QuestionPageNumber(String keyword) throws Exception {
		int pagelength = 0;
		String[] urls = GetQuestionURLs(keyword);
		String[] testresult = { "aa", "bb" };
		if (urls.equals(testresult)) {
			System.out.println("不存在第一层链接！！！");
		} else {
			pagelength = urls.length;
		}
		System.out.println(keyword + "问题总数为：" + pagelength);
		return pagelength;
	}

	/**
	 * 解析问题网页，将其保存到本地的Excel表中
	 */
	public static void Down2Excel(String keyword, int pagelength) throws Exception {
		String catalog = GetKeywordCatalog(keyword);
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog + keyword + "-tag.xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0);
		/** ************设置单元格字体************** */
		// 字体
		WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
		/** ************以下设置几种格式的单元格************ */
		WritableCellFormat wcf_center = new WritableCellFormat(NormalFont);
		wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN);
		wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);
		wcf_center.setAlignment(Alignment.CENTRE);
//		wcf_center.setBackground(Colour.YELLOW);
		wcf_center.setWrap(true);
		/** ************单元格格式设置完成****************** */
		sheet.addCell(new Label(0, 0, "QA", wcf_center));
		sheet.addCell(new Label(1, 0, "Content", wcf_center));
//		sheet.addCell(new Label(2, 0, "Char", wcf_center));
		sheet.addCell(new Label(2, 0, "QT(1) or AS(0)", wcf_center));
		sheet.addCell(new Label(3, 0, "Upvote", wcf_center));
		sheet.addCell(new Label(4, 0, "Link", wcf_center));
		sheet.addCell(new Label(5, 0, "Comment", wcf_center));
		sheet.addCell(new Label(6, 0, "Follower", wcf_center));
		sheet.addCell(new Label(7, 0, "Profession", wcf_center));
		sheet.addCell(new Label(8, 0, "KnowAbout", wcf_center));
		sheet.addCell(new Label(9, 0, "AnswerSum", wcf_center));
		sheet.addCell(new Label(10, 0, "Word", wcf_center));
		sheet.addCell(new Label(11, 0, "Exist(1)", wcf_center));
		sheet.addCell(new Label(12, 0, "0", wcf_center));
		sheet.setRowView(0, 700, false); // 设置行高
		int number = 1;
		for (int j = 0; j < pagelength; j++) {
			String path = catalog + keyword + j + ".html";
			File file = new File(path);
			if (!file.exists()) {
				System.out.println(path + "  不存在");
			} else {
				System.out.println("\n开始解析： " + path);
				Document doc = JsoupParse.parsePathText(path);
				String questionname = PageAnalysis.QuestionContent(doc);
				String questionexpand = PageAnalysis.QuestionExpandInfo(doc);
//				int questionlength2 = QuestionLength2(doc);
				int questionlength = PageAnalysis.QuestionContentWordLength(doc);
				String want_answer = PageAnalysis.QuestionWantAnswers(doc);         //问题关注人数
				String question_comment = PageAnalysis.QuestionCommentNumbers(doc); //答案评论数
				System.out.println(pagelength);
//				try{
					sheet.addCell(new Label(0, number, keyword + j, wcf_center));
					sheet.addCell(new Label(1, number, questionname + "\n" + "Expanded information：" + questionexpand, wcf_center));
//					sheet.addCell(new Label(2, number, questionlength2 + "", wcf_center));
					sheet.addCell(new Label(2, number, "1", wcf_center));
					sheet.addCell(new Label(3, number, want_answer, wcf_center));  
					sheet.addCell(new Label(4, number, "0", wcf_center));         //默认没有链接
					sheet.addCell(new Label(5, number, question_comment, wcf_center));
					sheet.addCell(new Label(6, number, "0", wcf_center));
					sheet.addCell(new Label(7, number, "0", wcf_center));
					sheet.addCell(new Label(8, number, "0", wcf_center));
					sheet.addCell(new Label(9, number, "0", wcf_center));
					sheet.addCell(new Label(10, number, questionlength + "", wcf_center));
					sheet.addCell(new Label(11, number, "1", wcf_center));
					sheet.addCell(new Label(12, number, j + "", wcf_center));
					
					sheet.setRowView(number, 1300, false); // 设置行高
					sheet.setColumnView(0, 20);
					sheet.setColumnView(1, 60);
					int realanswernumber = PageAnalysis.CountRealAnswerNumber(doc);
					for (int m = number; m < number + realanswernumber; m++) {
						sheet.setRowView(m + 1, 1300, false); // 设置行高
						String answercontent = PageAnalysis.AnswerContent(doc, m - number);
//						int contentlength2 = PageAnalysis.Content_Length2(doc, m - number); // 字符长度
						int contentlength = PageAnalysis.AnswerContentWordLength(doc, m - number);   // 单词长度
						int upvote = PageAnalysis.AnswerUpvotes(doc, m - number);              // 支持票数 
						String url = PageAnalysis.AnswerURLs(doc, m - number);                    // 链接有无
						String comment = PageAnalysis.AnswerCommentNumbers(doc, m - number);     // 评论数量
						
						String follower = null;
						String info = null;
						String know = null;
						String answer = null;
						String authorpath = catalog + keyword + j + "_author_" + (m - number) + ".html";
						File file1 = new File(authorpath);
						if(!file1.exists()){
							System.out.println(authorpath + "   不存在");
						}else{
							Document doc1 = JsoupParse.parsePathText(authorpath);  //解析作者页面路径
							
							follower = PageAnalysis.AuthorFollowers(doc1, m - number);   // 作者粉丝
							info = PageAnalysis.AuthorInfo(doc, m - number);            // 作者简历
							know = PageAnalysis.AuthorKnowAbout(doc1, m - number);       // 作者领域
							answer = PageAnalysis.AuthorAnswers(doc1, m - number);       // 回答总数
						}
						
						sheet.addCell(new Label(0, m + 1, keyword + j + "_" + (m - number + 1) + " ",wcf_center));
						sheet.addCell(new Label(1, m + 1, answercontent, wcf_center));
//						sheet.addCell(new Label(2, m + 1, contentlength2 + "",wcf_center));
						sheet.addCell(new Label(2, m + 1, "0", wcf_center));
						sheet.addCell(new Label(3, m + 1, upvote + "", wcf_center));
						sheet.addCell(new Label(4, m + 1, url, wcf_center));
						sheet.addCell(new Label(5, m + 1, comment, wcf_center));
						sheet.addCell(new Label(6, m + 1, follower, wcf_center));
						sheet.addCell(new Label(7, m + 1, info, wcf_center));
						sheet.addCell(new Label(8, m + 1, know, wcf_center));
						sheet.addCell(new Label(9, m + 1, answer, wcf_center));
						sheet.addCell(new Label(10, m + 1, contentlength + "",wcf_center));
						sheet.addCell(new Label(11, m + 1, "1", wcf_center));
						sheet.addCell(new Label(12, m + 1, j + "", wcf_center));
					}
					number = number + realanswernumber + 1;
					//System.out.println(number);
//				}catch(Exception e){
//					System.out.println("Quora User,No Name");
//					sheet.addCell(new Label(0, number, "Quora User,No Name", wcf_center));
//				}
			}
//			System.out.println(path + " 已经成功解析到 " + catalog + keyword + "-tag.xls");
		}
		workbook.write(); // 写表格而不是写单元格，所以不能放在循环里面，必须放在最后
		/** *********关闭文件************* */
		workbook.close();
	}

	/**
	 * 处理excel表，问题和回答中只要有出现关键词，就将原Excel表中的第五列中的值 1 改为 0，写到一张新的Excel表中
	 */
	public static void match3(String keyword) throws Exception {
		String catalog = tag2.GetKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0);              //得到excel表格
		int rsRows = rs.getRows();
		String[] keywordarray = keyword.split("\\+");      //将关键词以+分开存到数组里面，用于匹配文本
		System.out.println("关键词的组成有： ");
		for(int i = 0; i < keywordarray.length; i++){
			System.out.println(keywordarray[i]);
		}
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog + keyword + "-tag_changed.xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0);     //准备新建的写的excel
		for (int i = 0; i < rsRows; i++) {
			//将原来的一到三列写到新Excel中，内容不变
			sheet.setColumnView(0, 20);
			sheet.setColumnView(1, 60);
			WritableCellFormat wcf_center = ExcelSet.setCenterText();
			for (int col = 0; col < 11; col++) {
				sheet.setRowView(i, 1300, false);
				sheet.addCell(new Label(col, i, rs.getCell(col, i).getContents(), wcf_center));
			}
			//处理第四列，检查文本片段是否与关键词相关
			Cell cell = rs.getCell(1, i);
			String content = cell.getContents();
			int count = 0;
			for (int j = 0; j < keywordarray.length; j++) {
				// 在字符串中找关键词出现的次数
				Pattern p = Pattern.compile("(?i)" + keywordarray[j]);    //忽略大小写
				Matcher m = p.matcher(content);
				while (m.find()) {
					count++;
				}
			}
			System.out.println("关键词出现次数: " + count);
			if (count == 0) {
				if(i == 0){
					sheet.addCell(new Label(11, i, rs.getCell(11, 0).getContents(), wcf_center));
				}else{
					sheet.addCell(new Label(11, i, "0", wcf_center));
					System.out.println("第 " + i + " 个单元中没有出现关键词，与主题无关，将第五列标0...");
				}
			} else {
				sheet.addCell(new Label(11, i, "1", wcf_center));
				System.out.println("第 " + i + " 个单元中出现关键词，第五列值不变...");
			}
		}
		workbook.write();
		workbook.close();
		System.out.println(keyword + "处理完毕...\n");
	}
	
	/**
	 * 经过预处理后的数据，只保留匹配后值为1的还有单词长度在0-500之间的数据
	 */
	public static void match4(String keyword) throws Exception {
		String catalog = tag2.GetKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0);              //得到excel表格
		int rsRows = rs.getRows();
		String[] keywordarray = keyword.split("\\+");      //将关键词以+分开存到数组里面，用于匹配文本
		System.out.println("关键词的组成有： ");
		for(int i = 0; i < keywordarray.length; i++){
			System.out.println(keywordarray[i]);
		}
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog + keyword + "-tag_changed2.xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0);     //准备新建的写的excel
		for (int i = 0; i < rsRows; i++) {
			//将原来的一到三列写到新Excel中，内容不变
			sheet.setColumnView(0, 20);
			sheet.setColumnView(1, 60);
			WritableCellFormat wcf_center = ExcelSet.setCenterText();
			for (int col = 0; col < 2; col++) {
				sheet.setRowView(i, 1300, false);
				sheet.addCell(new Label(col, i, rs.getCell(col, i).getContents(), wcf_center));
			}
			for (int col = 5; col < 13; col++) {
				sheet.setRowView(i, 1300, false);
				sheet.addCell(new Label(col - 3, i, rs.getCell(col, i).getContents(), wcf_center));
			}
			//处理第四列，检查文本片段是否与关键词相关
			Cell cell = rs.getCell(1, i);
			String content = cell.getContents();
			int count = 0;
			for (int j = 0; j < keywordarray.length; j++) {
				// 在字符串中找关键词出现的次数
				Pattern p = Pattern.compile("(?i)" + keywordarray[j]);    //忽略大小写
				Matcher m = p.matcher(content);
				while (m.find()) {
					count++;
				}
			}
			System.out.println("关键词出现次数: " + count);
			if (count == 0) {
				if(i == 0){
					sheet.addCell(new Label(10, i, rs.getCell(4, 0).getContents(), wcf_center));
				}else{
					sheet.addCell(new Label(10, i, "0", wcf_center));
					System.out.println("第 " + i + " 个单元中没有出现关键词，与主题无关，将第五列标0...");
				}
			} else {
				sheet.addCell(new Label(10, i, "1", wcf_center));
				System.out.println("第 " + i + " 个单元中出现关键词，第五列值不变...");
			}
		}
		workbook.write();
		workbook.close();
		System.out.println(keyword + "处理完毕...\n");
	}
	
	/**
	 * 用于得到爬取数据的时间
	 */
	public static String GetCrawlerTime() throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//设置日期格式 
		String CrawlerTime = df.format(new Date());   // new Date()为获取当前系统时间
		System.out.println("CrawlerTime1 is ：" + CrawlerTime);
		return CrawlerTime;
	}
	
	/**
	 * 设置输出格式
	 */
	private static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

	/**
	 * 输出指定长度的字符串
	 */
	private static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1) + ".";
		else
			return s;
	}

}
