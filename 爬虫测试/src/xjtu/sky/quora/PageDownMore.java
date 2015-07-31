package xjtu.sky.quora;

/**
 * zhengyuanhao  2014/11/27
 * 简单实现：quora  
 * 获取网页上面的所有链接
 * 
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.IOException;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Border;
import jxl.write.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@SuppressWarnings("deprecation")
public class PageDownMore {

	public static void main(String[] args) throws Exception {
		PageDownMore a = new PageDownMore();
		String keyword = "Decision-Trees";
		a.SaveQuestion2Excel(keyword);
	}

	static int pagenumbers; // 第一层网页数目
	static String[] testresult = { "aa", "bb" };

	/**
	 * 输入一个术语，搜索得到第一个网页 参数keyword 表示搜索的关键词，格式为：Binary-Trees（开头字母大写，中间用 - 连接）
	 * 
	 * @throws Exception
	 */
	public void GetFirstPage(String keyword) throws Exception {
		String path = "http://www.quora.com/" + keyword;
		String keyword1 = keyword + 0;
		PageDown a = new PageDown();
		a.pagedown(keyword1, path);
	}

	/**
	 * 输入一个术语，搜索得到第一个网页，得到与该术语相关的主题（related topics） 可以得到所有有关的主题，more的影响不存在！！！
	 */
	public String[] GetRelatedTopics(String keyword) throws Exception {
		String path = "f:/test/" + keyword + "0.html";
		Document doc = parsePathText(path);
		// 得到所有related topics
		Elements relatedtopics = doc.select("div.grid_page_right_col")
				.select("li.related_topic_item")
				.select("div.related_topic_item_info").select("span.TopicName");
		String[] topicname = new String[relatedtopics.size()];
		System.out.println("related topics 有：");
		for (int i = 0; i < topicname.length; i++) {
			Element topic = relatedtopics.get(i);
			topicname[i] = topic.text();
			System.out.println(topicname[i]);
		}
		return topicname;
	}

	/**
	 * 分析第一个页面，得到其他所有网页，将爬取到本地，返回的是第一层页面的数量 参数keyword 表示关于binary
	 * trees搜索的所有网页的第一个页面
	 */
	public static void GetOtherPages(String keyword) throws Exception {
		String path = "f:/test/" + keyword + "0.html";
		File file = new File(path);
		if (!file.exists()) {
			System.out.println("f:/test/" + keyword + "0.html"
					+ "  不存在，请重新运行！！！");
		} else {
			Document doc = parsePathText(path);
			// 得到关于关键词的剩余的有关网页
			Elements lastlink = doc.select("div.grid_page_center_col");
			Elements a = lastlink.select("a[title^=go to page]");
			// 判断是否存在多个页面，否则只有一页，元素a的大小为0
			if (a.size() == 0) {
				System.out.println("只有一页！！！");
				pagenumbers = 1;
			} else {
				Element b = a.last();
				int num = Integer.parseInt(b.text()); // 第一层总页面数
				String linkurls[] = new String[num - 1];
				for (int i = 2; i <= num; i++) {
					// Element link = linkpage2.get(i);
					linkurls[i - 2] = "http://www.quora.com/" + keyword
							+ "?page_id=" + i; // 将所有链接存到linkurls数组里面
					print(" * " + i + ": <%s>  (%s)", linkurls[i - 2], null, 60);
				}
				// 将剩余页面下载到本地
				for (int i = 0; i < linkurls.length; i++) {
					int j = i + 1;
					String str = keyword + j; // 存成关键字加数字（Binary-Trees1）的形式
					PageDown a1 = new PageDown();
					a1.pagedown(str, linkurls[i]);
				}
				pagenumbers = num;
			}
		}
	}

	/**
	 * 分析网页，得到该页面的一个子页面（问题有关）的链接
	 * 
	 * @throws Exception
	 */
	public static String[] GetChildUrls(String keyword, int n) throws Exception {
		// String[] testresult = {"aa","bb"};
		String path = "f:/test/" + keyword + n + ".html";
		File file = new File(path);
		if (!file.exists()) {
			System.out.println(path + "  不存在，得不到它的子页面！！！");
			return testresult;
		} else {
			Document doc = parsePathText(path);
			Elements links = doc.select("div.QuestionText").select("a[href]");
			print("\n" + path + " 所有问题的链接  Links: (%d)", links.size());

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
	 * 分析一层网页，得到该页面的所有问题，将其保存到Excel里面进行分析
	 * 
	 * @throws Exception
	 */
	public static String[] GetQuestion(String keyword, int n) throws Exception {
		String path = "f:/test/" + keyword + n + ".html";
		File file = new File(path);
		if (!file.exists()) {
			System.out.println(path + "  不存在，得不到它的问题！！！");
			return testresult;
		} else {
			Document doc = parsePathText(path);
			Elements links = doc.select("div.QuestionText").select("a[href]");

			String urls[] = new String[links.size()];
			for (int i = 0; i < links.size(); i++) {
				Element link = links.get(i);
				// 保存所有问题
				urls[i] = link.text();
				print(" * " + (i + 1) + ": <%s>  (%s)", urls[i],
						trim(link.text(), 100));
			}
			return urls;
		}
	}

	/**
	 * 得到一层页面的所有问题，将其保存到Excel里面进行分析
	 * 
	 * @throws Exception
	 */
	public void SaveQuestion2Excel(String keyword) throws Exception {
		WritableWorkbook workbook = Workbook.createWorkbook(new File("f:/test/"
				+ keyword + "_question_list.xls"));
		WritableSheet sheet = workbook.createSheet("问题总汇", 0);
		// 用于标题
		WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
		WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 12,
				WritableFont.BOLD);
		WritableCellFormat wcf_title = new WritableCellFormat(BoldFont);
		wcf_title.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
		wcf_title.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
		wcf_title.setAlignment(Alignment.CENTRE); // 水平对齐
		wcf_title.setBackground(Colour.GRAY_25);
		wcf_title.setWrap(true); // 是否换行
		// 用于正文左
		WritableCellFormat wcf_center = new WritableCellFormat(NormalFont);
		wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN);
		wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);
		wcf_center.setAlignment(Alignment.CENTRE);
		wcf_center.setWrap(true);
		/** */
		/** ************单元格格式设置完成****************** */
		String path = "f:/test/" + keyword + "0.html";
		File file = new File(path);
		if (!file.exists()) {
			System.out.println("f:/test/" + keyword + "0.html"
					+ "  不存在，请重新运行！！！");
		} else {
			Document doc = parsePathText(path);
			// 得到关于关键词的剩余的有关网页
			Elements lastlink = doc.select("div.grid_page_center_col");
			Elements a = lastlink.select("a[title^=go to page]");
			// 判断是否存在多个页面，否则只有一页，元素a的大小为0
			if (a.size() == 0) {
				System.out.println("只有一页！！！");
				pagenumbers = 1;
			} else {
				Element b = a.last();
				pagenumbers = Integer.parseInt(b.text()); // 第一层总页面数
			}
		}
		sheet.mergeCells(0, 0, pagenumbers - 1, 0);
		sheet.setRowView(0, 800, false);
		sheet.addCell(new Label(0, 0, keyword + "主题下的所有问题", wcf_title));
		for (int k = 0; k < pagenumbers; k++) {
			sheet.setColumnView(k, 30);
			String[] question = GetQuestion(keyword, k);
			for (int i = 0; i < question.length; i++) {
				sheet.setRowView(i + 1, 800, false);
				sheet.addCell(new Label(k, i + 1, question[i], wcf_center));
			}
		}
		workbook.write();
		/** */
		/** *********关闭文件************* */
		workbook.close();
	}

	/**
	 * 爬取keyword的所有子网页（问题网页） pagenumbers是第一层页面数目，pagelength[i]是每个第一层页面的子页面数目
	 * 
	 * @throws Exception
	 */
	public int[] GetChildPages(String keyword) throws Exception {
		GetOtherPages(keyword); // 下载所有页面，并返回页面总数
		int[] pagelength = new int[pagenumbers];
		for (int i = 0; i < pagenumbers; i++) {
			String[] urls = GetChildUrls(keyword, i);
			// System.out.println("这是测试行。。。。。。。");
			if (urls.equals(testresult)) {
				System.out.println("不存在第一层链接！！！");
				pagelength[i] = 0;
			} else {
				// String[] urls = GetUrls(keyword,i); //得到第二层页面的链接
				pagelength[i] = urls.length; // 得到第一层页面里面包含的链接数目
				for (int j = 0; j < urls.length; j++) {
					String str = keyword + i + "_" + j; // 子页面存在本地的格式是“Binary-Trees1_1”
					PageDown a2 = new PageDown();
					a2.pagedown(str, urls[j]); // 爬取所有有关问题的子网页

					// Thread.sleep(1000);
					// 爬取作者信息
					String path = "f:/test/" + str + ".html";
					Document doc = parsePathText(path);
					Elements authors = doc.select("div.pagedlist_item").select(
							"div.author_info");
					for (int m = 0; m < authors.size(); m++) {
						Element a = authors.get(m);
						Elements b = a.select("a.user");
						if (b.size() == 0) {
							System.out.println("作者信息不存在！！！");
						} else {
							Element author = b.get(0);
							String links = author.attr("href");
							String link = new String();
							if (links.startsWith("http://")) {
								link = links;
							} else if (links.startsWith("/")) {
								link = "http://www.quora.com/" + links;
							}
							String savestr = keyword + i + "_" + j + "_author_" + m;
							PageDown a3 = new PageDown();
							a3.pagedown(savestr, link); // 爬取作者页面
						}
					}
				}
			}
		}
		return pagelength;
	}

	/**
	 * 解析问题网页，将其保存到本地的Excel表中
	 */
	public void Down2Excel(String keyword, int[] pagelength) throws Exception {
		for (int i = 0; i < pagenumbers; i++) {
			if (pagelength[i] == 0) {
				System.out.println("第一层链接不存在，得不到第二层链接的数目");
			} else {
				for (int j = 0; j < pagelength[i]; j++) {
					PageAnalysis.Save2Excel(keyword, i, j);
				}
			}
		}
	}

	/**
	 * 解析指定路径的html文件doc
	 */
	public static Document parsePathText(String path) {
		Document doc = null;
		try {
			File input = new File(path);
			doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com/"); // http://www.quora.com/Binary-Trees
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
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
