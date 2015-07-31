package xjtu.sky.quora;

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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@SuppressWarnings("deprecation")
public class tag {

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		// test0();
		test1();
		test2();
		test3();
		long end = System.currentTimeMillis();
		System.out.println("time used:" + (end - start) / 1000 + "s" + "\n"
				+ "finished");
	}

	public static void test0() throws Exception {
		String keyword = "Binary-Trees";
		tag t = new tag();
		int pagenumbers = t.PageNumbers(keyword);
		int[] pagelength = t.PageLength(keyword, pagenumbers);
		t.Down2Excel(keyword, pagenumbers, pagelength);
	}

	public static void test1() throws Exception {
		String[] data_structures = { "B-Trees", "Tries", "Linked-Lists",
				"Binary-Trees", "Strings-data-structure", "Skip-Lists",
				"Ontologies", "Quora-Topic-Ontology", "Dbpedia", "Freebase",
				"Hierarchy", "Trees-data-structures",
				"Graphs-computer-science", "Graph-Databases", "OrientDB",
				"Neo4j", "Gremlin", "Sparsity-Technologies",
				"Titan-graph-database", "Cassandra-database", "HBase",
				"Berkeley-DB", "Apache-Hadoopg", "Apache-Hive", "HDFS",
				"Apache-Hama-1", "Bulk-Synchronous-Parallel-Computing",
				"Pregel", "Qubole", "Apache-2-0-License",
				"Attributes-computer-sciences",
				"Extended-Attributes-Computer-Science", "Unstructured-Data",
				"Theory-of-Data-Structures", "Probabilistic-Data-Structures",
				"Bloom-Filters" };
		for (int i = 0; i < data_structures.length; i++) {
			tag t = new tag();
			int pagenumbers = t.PageNumbers(data_structures[i]);
			int[] pagelength = t.PageLength(data_structures[i], pagenumbers);
			t.Down2Excel(data_structures[i], pagenumbers, pagelength);
		}
	}

	public static void test2() throws Exception {
		String[] data_mining = { "Data-Mining-Startups", "Rapleaf",
				"Linked-Lists", "Captain-Dash", "Color-Labs-startup",
				"Color-for-Facebook-application",
				"Color-Sale-and-Shutdown-Rumors-October-2012",
				"Funding-of-Color-Labs", "Junyo", "Hotlist", "Anametrix",
				"Text-Analytics", "Sentiment-Analysis", "Tagging",
				"Semantic-Annotation", "People-Tagging",
				"Facebook-Photo-Tagging", "Collaborative-Tagging",
				"Semantic-Search", "Text-Processing",
				"Named-Entity-Recognition", "MAXQDA", "Data-Mining-Books",
				"Python-Data-Mining", "Educational-Data-Mining" };
		for (int i = 0; i < data_mining.length; i++) {
			tag t = new tag();
			int pagenumbers = t.PageNumbers(data_mining[i]);
			int[] pagelength = t.PageLength(data_mining[i], pagenumbers);
			t.Down2Excel(data_mining[i], pagenumbers, pagelength);
		}
	}

	public static void test3() throws Exception {
		String[] computer_networking = { "Network-Protocols",
				"Network-Security", "Local-Area-Networks",
				"Metropolitan-Area-Network", "Wide-Area-Networks",
				"Virtual-Private-Networks-VPNs", "Bandwidth-Optimization",
				"Network-DMZs", "Computer-Network-Performance",
				"Ubiquitous-Computing", "Cloud-Computing",
				"Fibre-to-the-Cabinet", "Self-Organizing-Networks", "Routing",
				"RJ-45", "Vision-Technology-Management-LLC",
				"Software-defined-Networking", "Distributed-Social-Networks",
				"The-Internet-2", "Wireless-Technology" };
		for (int i = 0; i < computer_networking.length; i++) {
			tag t = new tag();
			int pagenumbers = t.PageNumbers(computer_networking[i]);
			int[] pagelength = t
					.PageLength(computer_networking[i], pagenumbers);
			t.Down2Excel(computer_networking[i], pagenumbers, pagelength);
		}
	}

	/**
	 * 得到关键词根目录
	 */
	public String GetCatalog(String keyword) {
		String catalog = null;
		File file0 = new File("f:/test/");
		File[] files = file0.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().equals("Computer Networking（计算机网络）")
					|| files[i].getName().equals("Data Mining（数据挖掘）")
					|| files[i].getName().equals("Data Structures（数据结构）")) {
				File file1 = new File("f:/test/" + files[i].getName() + "/");
				File[] files1 = file1.listFiles();
				for (int j = 0; j < files1.length; j++) {
					if (files1[j].getName().equals(keyword)) {
						catalog = "f:/test/" + files[i].getName() + "/"
								+ keyword + "/";
					}
				}
			}
		}
		return catalog;
	}

	/**
	 * 分析第一个页面，得到页面总数
	 */
	public int PageNumbers(String keyword) throws Exception {
		int pagenumbers = 0;
		String catalog = GetCatalog(keyword);
		String path = catalog + keyword + "0.html";
		File file = new File(path);
		if (!file.exists()) {
			System.out.println("f:/test/" + keyword + "0.html"
					+ "  不存在，请重新运行！！！");
		} else {
			Document doc = parsePathText(path);
			Elements lastlink = doc.select("div.grid_page_center_col");
			Elements a = lastlink.select("a[title^=go to page]");
			if (a.size() == 0) {
				System.out.println("只有一页！！！");
				pagenumbers = 1;
			} else {
				Element b = a.last();
				pagenumbers = Integer.parseInt(b.text());
			}
		}
		System.out.println(pagenumbers);
		return pagenumbers;
	}

	/**
	 * pagenumbers是第一层页面数目，pagelength[i]是每个第一层页面的子页面数目
	 * 
	 * @throws Exception
	 */
	public int[] PageLength(String keyword, int pagenumbers) throws Exception {
		String[] testresult = { "aa", "bb" };
		int[] pagelength = new int[pagenumbers];
		for (int i = 0; i < pagenumbers; i++) {
			String[] urls = PageDownMore.GetChildUrls(keyword, i);
			if (urls.equals(testresult)) {
				System.out.println("不存在第一层链接！！！");
				pagelength[i] = 0;
			} else {
				pagelength[i] = urls.length;
			}
		}
		System.out.println(pagelength);
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
			System.out.println("问题名是为：" + name_s);
			return name_s;
		}
	}

	/**
	 * 解析网页的 问题的 单词 长度
	 * 
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
	 * 
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
			System.out.println("问题单词长度为：" + length);
			return length;
		}
	}

	/**
	 * 解析问题网页，将其保存到本地的Excel表中
	 */
	public void Down2Excel(String keyword, int pagenumbers, int[] pagelength)
			throws Exception {
		String catalog = GetCatalog(keyword);
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
				+ keyword + "-tag.xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0);
		/** */
		/** ************设置单元格字体************** */
		// 字体
		WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
		WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 12,
				WritableFont.BOLD);

		/** */
		/** ************以下设置几种格式的单元格************ */
		WritableCellFormat wcf_title = new WritableCellFormat(BoldFont);
		wcf_title.setBorder(Border.ALL, BorderLineStyle.THIN);
		wcf_title.setVerticalAlignment(VerticalAlignment.CENTRE);
		wcf_title.setAlignment(Alignment.CENTRE);
		wcf_title.setBackground(Colour.GRAY_25);
		wcf_title.setWrap(true);

		WritableCellFormat wcf_center = new WritableCellFormat(NormalFont);
		wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN);
		wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);
		wcf_center.setAlignment(Alignment.CENTRE);
		wcf_center.setWrap(true);
		/** */
		/** ************单元格格式设置完成****************** */

		int number = 0;
		for (int i = 0; i < pagenumbers; i++) {
			for (int j = 0; j < pagelength[i]; j++) {
				String path = catalog + keyword + i + "_" + j + ".html";
				File file = new File(path);
				if (!file.exists()) {
					System.out.println(path + "  不存在");
				} else {
					System.out.println("\n开始解析： " + path);
					Document doc = parsePathText(path);
					String questionname = QuestionName(doc);
					int questionlength2 = QuestionLength2(doc);
					int questionlength = QuestionLength(doc);
					sheet.addCell(new Label(0, number, keyword + i + "_" + j, wcf_center));
					sheet.addCell(new Label(1, number, questionname, wcf_center));
					sheet.addCell(new Label(2, number, questionlength2 + "", wcf_center));
					sheet.addCell(new Label(3, number, questionlength + "", wcf_center));
					sheet.addCell(new Label(4, number, "1", wcf_center));
					sheet.setRowView(number, 1300, false); // 设置行高
					sheet.setColumnView(0, 20);
					sheet.setColumnView(1, 60);
					int realanswernumber = PageAnalysis.RealAnswerNumber(doc);
					// number = number + realanswernumber;
					for (int m = number; m < number + realanswernumber; m++) {
						sheet.setRowView(m + 1, 1300, false); // 设置行高
						String answercontent = PageAnalysis.AnswerContent(doc, m - number);
						int contentlength2 = PageAnalysis.Content_Length2(doc, m - number); //字符长度
						int contentlength = PageAnalysis.Content_Length(doc, m - number); //单词长度
						sheet.addCell(new Label(0, m + 1, (m - number + 1) + " ", wcf_center));
						sheet.addCell(new Label(1, m + 1, answercontent, wcf_center));
						sheet.addCell(new Label(2, m + 1, contentlength2 + "", wcf_center));
						sheet.addCell(new Label(3, m + 1, contentlength + "", wcf_center));
						sheet.addCell(new Label(4, m + 1, "1", wcf_center));
					}
					number = number + realanswernumber + 1;
					System.out.println(number);
					System.out.println(path + " 已经成功解析到 " + "f:/test/" + keyword + "-tag.xls");
				}
			}
		}
		workbook.write(); // 写表格而不是写单元格，所以不能放在循环里面，必须放在最后
		/** *********关闭文件************* */
		workbook.close();
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

}
