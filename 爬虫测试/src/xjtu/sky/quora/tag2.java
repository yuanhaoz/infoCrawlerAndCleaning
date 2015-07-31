package xjtu.sky.quora;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
//import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Border;
import jxl.write.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//import org.openqa.selenium.ie.InternetExplorerDriver;

@SuppressWarnings("deprecation")
public class tag2 {

	public static void main(String[] args) throws Exception {
//		analysis("Data_mining");       //爬取加解析
		analysis2("Computer_network");      //解析数据
	}
	
	/**
	 * 用于爬取Data_structure下面的所有问题
	 */
	public static void test() throws Exception {
		String keyword = "Array";
		tag2 t = new tag2();
		//t.GetChildPages(keyword);
		int pagelength = t.PageLength(keyword);
		t.Down2Excel(keyword, pagelength);
	}

	/**
	 * 爬取6门课的所有问题，调用下一个程序
	 * @author zhengtaishuai
	 */
	public static void test0() throws Exception {
		String[] course = { "Data_structure"};  //, "Data_mining","Computer_network", "Classical_mechanics","Euclidean_geometry", "Microbiology" 
		for (int i = 0; i < course.length; i++) {
			analysis(course[i]);
		}
	}

	/**
	 * 用于爬取Data_structure下面的所有问题 输入是课程名称（course） 先读取文件夹中关键词名称，在爬取
	 * @author zhengtaishuai
	 */
	public static void analysis(String course) throws Exception {
		tag2 t = new tag2();
		KeyWordMatch k = new KeyWordMatch();
		File file0 = new File("F:/术语/课程术语/" + course);
		File[] files = file0.listFiles();
		for (int i = 0; i < files.length; i++) {
			String filename = files[i].getName();
			if(filename.contains(".html")){
				String keyword = filename.substring(0, filename.length() - 5);
				System.out.println(keyword);
				String url = "http://www.quora.com/search?q=" + keyword;
				PageDown a = new PageDown();
				try{
					long start = System.currentTimeMillis();
					a.pagedown(keyword, url);                         //爬取得到搜索主题的第一层
					t.GetChildPages(keyword);                         //爬取得到所有问题和作者页面（第二层）
					int pagelength = t.PageLength(keyword);
					t.Down2Excel(keyword, pagelength);
					k.match3(keyword);    //生成-changed文件，保留关键词匹配结果和单词长度信息
					k.match(keyword);     //生成-changed1文件，只保留关键词匹配成立，单词长度未处理
					k.match1(keyword);    //生成-changed2文件，只保留关键词匹配成立，且单词长度在0-500之间的
					long end = System.currentTimeMillis();
					System.out.println("爬取" + keyword + "的所有信息用时：" + (end - start)/1000 + "秒...");
				}
				catch (Exception e)
				{
					long start = System.currentTimeMillis();
					System.out.println("爬取失败，继续爬取...");
					a.pagedown(keyword, url);                         //爬取得到搜索主题的第一层
					t.GetChildPages(keyword);                         //爬取得到所有问题和作者页面（第二层）
					int pagelength = t.PageLength(keyword);
					t.Down2Excel(keyword, pagelength);
					k.match3(keyword);    //生成-changed文件，保留关键词匹配结果和单词长度信息
					k.match(keyword);     //生成-changed1文件，只保留关键词匹配成立，单词长度未处理
					k.match1(keyword);    //生成-changed2文件，只保留关键词匹配成立，且单词长度在0-500之间的
					long end = System.currentTimeMillis();
					System.out.println("爬取" + keyword + "的所有信息用时：" + (end - start)/1000 + "秒...");
				}
							
			}
		}
	}
	
	/**
	 * 用于解析已经爬取下来的数据
	 * @author zhengtaishuai
	 */
	public static void analysis2(String course) throws Exception {
		tag2 t = new tag2();
		KeyWordMatch k = new KeyWordMatch();
		File file0 = new File("F:/术语/课程术语/" + course);
		File[] files = file0.listFiles();
		long start = System.currentTimeMillis();
		for (int i = 0; i < files.length; i++) {
			String keyword = files[i].getName();
			if(!keyword.contains(".html")){
				int pagelength = t.PageLength(keyword);
				t.Down2Excel(keyword, pagelength);
				k.match3(keyword);    //生成-changed文件，保留关键词匹配结果和单词长度信息（将和长度有关的标为1，否则标为0）
				k.match(keyword);     //生成-changed1文件，只保留关键词匹配成立，单词长度未处理
				k.match1(keyword);    //生成-changed2文件，只保留关键词匹配成立，且单词长度在0-500之间的
				System.out.println(keyword + "  解析完成...");
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("总共用时： " + (end - start) / 1000 + " 秒...");
	}
	
	/**
	 * 用于爬取作者页面
	 * @author zhengtaishuai
	 */
	public static void analysis3(String course) throws Exception {
		tag2 t = new tag2();
		File file0 = new File("F:/术语/课程术语/" + course);
		File[] files = file0.listFiles();
		for (int i = 0; i < files.length; i++) {
			String keyword = files[i].getName();
			t.GetChildPages(keyword);
			int pagelength = t.PageLength(keyword);
			t.Down2Excel(keyword, pagelength);
		}
	}

	/**
	 * 根据关键词，生成文件夹
	 */
	public String GetCatalog(String keyword) {
		String catalog = null;
		File file0 = new File("f:/术语/课程术语/");
		File[] files = file0.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file1 = new File("f:/术语/课程术语/" + files[i].getName() + "/");
			File[] files1 = file1.listFiles();
			for (int j = 0; j < files1.length; j++) {
				if (files1[j].getName().equals(keyword + ".html")) {
					catalog = "f:/术语/课程术语/" + files[i].getName() + "/"
							+ keyword + "/";
					File file2 = new File(catalog);
					if (!file2.exists()) {
						file2.mkdir();
					}
				}
			}
		}
		return catalog;
	}

	/**
	 * 得到关键词目录，用于问题页面生成存放
	 */
	public String GetCatalog2(String keyword) {
		String catalog = null;
		File file0 = new File("f:/术语/课程术语/");
		File[] files = file0.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file1 = new File("f:/术语/课程术语/" + files[i].getName() + "/");
			File[] files1 = file1.listFiles();
			for (int j = 0; j < files1.length; j++) {
				if (files1[j].getName().equals(keyword)) {
					catalog = "f:/术语/课程术语/" + files[i].getName() + "/" + keyword + "/";
				}
			}
		}
		return catalog;
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
	 * 分析关键词网页（Array.html），得到该页面的所有问题页面的链接
	 * 
	 * @throws Exception
	 */
	static String[] testresult = { "aa", "bb" };

	public static String[] GetChildUrls(String keyword) throws Exception {
		tag2 a = new tag2();
		String path = a.GetCatalog2(keyword) + keyword + ".html";
		// System.out.println(path);
		File file = new File(path);
		if (!file.exists()) {
			System.out.println(path + "  不存在，得不到它的子页面链接！！！");
			return testresult;
		} else {
			Document doc = parsePathText(path);
			Elements links = doc.select("div.pagedlist_item")
					.select("div.title ").select("a.question_link")
					.select("a[href]");
			// print("\n" + path + " 所有问题的链接  Links: (%d)", links.size());

			String urls[] = new String[links.size()];
			for (int i = 0; i < links.size(); i++) {
				Element link = links.get(i);
				urls[i] = "http://www.quora.com" + link.attr("href"); // 将所有链接存到urls数组里面
				print(" * " + (i + 1) + ": <%s>  (%s)", urls[i],
						trim(link.text(), 50));
			}
			return urls;
		}
	}

	/**
	 * 爬取所有关键词的问题网页（Array10.html），pagelength是问题页面总数
	 * 
	 * @throws Exception
	 */
	public void GetChildPages(String keyword) throws Exception {
		String[] urls = GetChildUrls(keyword);
//		System.out.println("test line...");
		if (urls.equals(testresult)) {
			System.out.println("不存在第一层链接！！！");
		} else {
			for (int j = 0; j < urls.length; j++) {
				PageDown a = new PageDown();
				a.pagedown2(keyword, j, urls[j]);   // 爬取所有有关问题的子网页
				GetAuthorPages(keyword, j);         // 爬取所有作者页面
			}
		}
	}
	
	/**
	 * 爬取作者页面
	 * @throws Exception
	 */
	public void GetAuthorPages(String keyword, int n) throws Exception {
		// 爬取作者信息
		tag2 t = new tag2();
		String path = t.GetCatalog2(keyword);
		String filepath = path + keyword + n + ".html";
		Document doc = parsePathText(filepath);
		Elements authors = doc.select("div.grid_page_center_col").select("div.pagedlist_item").select("div.author_info");
		for (int m = 0; m < authors.size(); m++) {
			Element a = authors.get(m);
			Elements b = a.select("a.user");
			if (b.size() == 0) {
				System.out.println("作者信息不存在！！！");
			} else {
				Element author = b.get(0);
				String urls = author.attr("href");
				String url = new String();
				if (urls.startsWith("http://")) {
					url = urls;
				} else if (urls.startsWith("/")) {
					url = "http://www.quora.com/" + urls;
				}
				String filename = keyword + n + "_author_" + m + ".html";
				String filepath1 = path + filename;
				File file1 = new File(filepath1);
				if(!file1.exists()){
					PageDown p = new PageDown();
					p.pagedown4(keyword, filename, url); // 爬取作者页面
				}else{
					System.out.println(filepath1 + "存在，不需要爬取");
				}
				
			}
		}
	}
	
	/**
	 * 定位本地作者页面地址
	 * @throws Exception
	 */
	public String GetAuthorPagesName(String keyword, int n) throws Exception {
		// 爬取作者信息
		tag2 t = new tag2();
		String path = t.GetCatalog2(keyword);
		String filepath = path + keyword + n + ".html";
		Document doc = parsePathText(filepath);
		Elements authors = doc.select("div.grid_page_center_col").select("div.pagedlist_item").select("div.author_info");
		String auth = null;
		for (int m = 0; m < authors.size(); m++) {
			Element a = authors.get(m);
			Elements b = a.select("a.user");
			if (b.size() == 0) {
				System.out.println("作者信息不存在！！！");
			} else {
				String filename = keyword + n + "_author_" + m + ".html";
				String filepath1 = path + filename;
				File file1 = new File(filepath1);
				if(!file1.exists()){
					auth = filepath1;
				}else{
					auth = filepath1 + "存在，不需要爬取";
				}
			}
		}
		return auth;
	}

	/**
	 * 得到pagelength是问题页面总数
	 * @throws Exception
	 */
	public int PageLength(String keyword) throws Exception {
		int pagelength = 0;
		String[] urls = GetChildUrls(keyword);
		if (urls.equals(testresult)) {
			System.out.println("不存在第一层链接！！！");
		} else {
			pagelength = urls.length;
		}
		System.out.println(keyword + "问题总数为：" + pagelength);
		return pagelength;
	}

	/**
	 * 解析网页问题名字,返回问题名字string
	 * 
	 * @param doc
	 */
	public static String QuestionName(Document doc) {
		Element a = doc.select("div.grid_page_center_col").get(0);
		Elements name = a.select("div.header").select("div.question_text_edit")
				.select("h1");
		if (name.size() == 0) {
			return "该网页不存在问题。";
		} else {
			String name_s = name.get(0).text();
//			System.out.println("问题名是为：" + name_s);
			return name_s;
		}
	}
	
	/**
	 * 解析网页问题附加信息,返回问题附加信息string
	 * @param doc
	 */
	public static String QuestionExpand(Document doc) {
		Element a = doc.select("div.grid_page_center_col").get(0);
		Elements expandinfo = a.select("div.header").select("div.question_details").select("div.expanded_q_text");
		if (expandinfo.size() == 0) {
			return "该网页不存在附加信息...";
		} else {
			String expandinfo_s = expandinfo.get(0).text();
			
//			System.out.println("问题名是为：" + expandinfo_s);
			
			return expandinfo_s;
		}
	}

	/**
	 * 解析网页的 问题的 单词 长度
	 * @param doc
	 */
	public static int QuestionLength(Document doc) {
		Element a = doc.select("div.grid_page_center_col").get(0);
		Elements name = a.select("div.header").select("div.question_text_edit")
				.select("h1");
		if (name.size() == 0) {
			return 0;
		} else {
			String name_s = name.get(0).text();
			// 使用split得到文本的单词数目
			String[] answers = name_s.split(" ");
			int length = answers.length;
			System.out.println("问题单词长度为：" + length);
			return length;
		}
	}

	/**
	 * 解析网页的 问题的 字符 长度
	 * @param doc
	 */
	public static int QuestionLength2(Document doc) {
		Element a = doc.select("div.grid_page_center_col").get(0);
		Elements name = a.select("div.header").select("div.question_text_edit")
				.select("h1");
		if (name.size() == 0) {
			return 0;
		} else {
			String name_s = name.get(0).text();
			int length = name_s.length();
			System.out.println("问题字符长度为：" + length);
			return length;
		}
	}

	/**
	 * 解析问题网页，将其保存到本地的Excel表中
	 */
	public void Down2Excel(String keyword, int pagelength) throws Exception {
		String catalog = GetCatalog2(keyword);
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
		sheet.addCell(new Label(12, 0, "Sequence", wcf_center));
		sheet.setRowView(0, 700, false); // 设置行高
		int number = 1;
		for (int j = 0; j < pagelength; j++) {
			String path = catalog + keyword + j + ".html";
			File file = new File(path);
			if (!file.exists()) {
				System.out.println(path + "  不存在");
			} else {
				System.out.println("\n开始解析： " + path);
				Document doc = parsePathText(path);
				String questionname = QuestionName(doc);
				String questionexpand = QuestionExpand(doc);
//				int questionlength2 = QuestionLength2(doc);
				int questionlength = QuestionLength(doc);
				String want_answer = PageAnalysis.Want_Answers(doc);   //想知道问题答案的人数
				String question_comment = PageAnalysis.Comment_Numbers_question(doc); //答案评论数
				System.out.println(pagelength);
//				try{
					sheet.addCell(new Label(0, number, keyword + j, wcf_center));
					sheet.addCell(new Label(1, number, questionname + "\n" + "Expanded information：" + questionexpand, wcf_center));
//					sheet.addCell(new Label(2, number, questionlength2 + "", wcf_center));
					sheet.addCell(new Label(2, number, "1", wcf_center));
					sheet.addCell(new Label(3, number, want_answer, wcf_center));  
					sheet.addCell(new Label(4, number, "0", wcf_center));      //默认没有链接
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
					int realanswernumber = PageAnalysis.RealAnswerNumber(doc);
					for (int m = number; m < number + realanswernumber; m++) {
						sheet.setRowView(m + 1, 1300, false); // 设置行高
						String answercontent = PageAnalysis.AnswerContent(doc, m - number);
//						int contentlength2 = PageAnalysis.Content_Length2(doc, m - number); // 字符长度
						int contentlength = PageAnalysis.Content_Length(doc, m - number);   // 单词长度
						String upvote = PageAnalysis.Upvotes(doc, m - number);              // 支持票数 
						String url = PageAnalysis.URLs(doc, m - number);                    // 链接有无
						String comment = PageAnalysis.Comment_Numbers(doc, m - number);     // 评论数量
						
						String follower = null;
						String info = null;
						String know = null;
						String answer = null;
						String authorpath = catalog + keyword + j + "_author_" + (m - number) + ".html";
						File file1 = new File(authorpath);
						if(!file1.exists()){
							System.out.println(authorpath + "   不存在");
						}else{
							Document doc1 = parsePathText(authorpath);  //解析作者页面路径
							
							follower = PageAnalysis.Author_Followers(doc1, m - number);   // 作者粉丝
							info = PageAnalysis.Author_Info(doc, m - number);            // 作者简历
							know = PageAnalysis.Author_KnowAbout(doc1, m - number);       // 作者领域
							answer = PageAnalysis.Author_Answers(doc1, m - number);       // 回答总数
						}
						
						sheet.addCell(new Label(0, m + 1, keyword + j + "_" + (m - number + 1) + " ",wcf_center));
						sheet.addCell(new Label(1, m + 1, answercontent, wcf_center));
//						sheet.addCell(new Label(2, m + 1, contentlength2 + "",wcf_center));
						sheet.addCell(new Label(2, m + 1, "0", wcf_center));
						sheet.addCell(new Label(3, m + 1, upvote, wcf_center));
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
	public void match3(String keyword) throws Exception {
		tag2 entity = new tag2();
		String catalog = entity.GetCatalog2(keyword);
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
			WritableCellFormat wcf_center = set();
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
	public void match4(String keyword) throws Exception {
		tag2 entity = new tag2();
		String catalog = entity.GetCatalog2(keyword);
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
			WritableCellFormat wcf_center = set();
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
	 * 解析指定路径的html文件doc
	 */
	public static Document parsePathText(String path) {
		Document doc = null;
		try {
			File input = new File(path);
			doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}
	
	/**
	 * 设置Excel表格单元格格式
	 */
	public WritableCellFormat set() throws Exception{
		WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
		WritableCellFormat wcf_center = new WritableCellFormat(NormalFont);
		wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN);
		wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);
		wcf_center.setAlignment(Alignment.CENTRE);
		wcf_center.setWrap(true);
		return wcf_center;
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
