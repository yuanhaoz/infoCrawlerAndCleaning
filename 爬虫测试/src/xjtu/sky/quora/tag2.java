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
//		analysis("Data_mining");       //��ȡ�ӽ���
		analysis2("Computer_network");      //��������
	}
	
	/**
	 * ������ȡData_structure�������������
	 */
	public static void test() throws Exception {
		String keyword = "Array";
		tag2 t = new tag2();
		//t.GetChildPages(keyword);
		int pagelength = t.PageLength(keyword);
		t.Down2Excel(keyword, pagelength);
	}

	/**
	 * ��ȡ6�ſε��������⣬������һ������
	 * @author zhengtaishuai
	 */
	public static void test0() throws Exception {
		String[] course = { "Data_structure"};  //, "Data_mining","Computer_network", "Classical_mechanics","Euclidean_geometry", "Microbiology" 
		for (int i = 0; i < course.length; i++) {
			analysis(course[i]);
		}
	}

	/**
	 * ������ȡData_structure������������� �����ǿγ����ƣ�course�� �ȶ�ȡ�ļ����йؼ������ƣ�����ȡ
	 * @author zhengtaishuai
	 */
	public static void analysis(String course) throws Exception {
		tag2 t = new tag2();
		KeyWordMatch k = new KeyWordMatch();
		File file0 = new File("F:/����/�γ�����/" + course);
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
					a.pagedown(keyword, url);                         //��ȡ�õ���������ĵ�һ��
					t.GetChildPages(keyword);                         //��ȡ�õ��������������ҳ�棨�ڶ��㣩
					int pagelength = t.PageLength(keyword);
					t.Down2Excel(keyword, pagelength);
					k.match3(keyword);    //����-changed�ļ��������ؼ���ƥ�����͵��ʳ�����Ϣ
					k.match(keyword);     //����-changed1�ļ���ֻ�����ؼ���ƥ����������ʳ���δ����
					k.match1(keyword);    //����-changed2�ļ���ֻ�����ؼ���ƥ��������ҵ��ʳ�����0-500֮���
					long end = System.currentTimeMillis();
					System.out.println("��ȡ" + keyword + "��������Ϣ��ʱ��" + (end - start)/1000 + "��...");
				}
				catch (Exception e)
				{
					long start = System.currentTimeMillis();
					System.out.println("��ȡʧ�ܣ�������ȡ...");
					a.pagedown(keyword, url);                         //��ȡ�õ���������ĵ�һ��
					t.GetChildPages(keyword);                         //��ȡ�õ��������������ҳ�棨�ڶ��㣩
					int pagelength = t.PageLength(keyword);
					t.Down2Excel(keyword, pagelength);
					k.match3(keyword);    //����-changed�ļ��������ؼ���ƥ�����͵��ʳ�����Ϣ
					k.match(keyword);     //����-changed1�ļ���ֻ�����ؼ���ƥ����������ʳ���δ����
					k.match1(keyword);    //����-changed2�ļ���ֻ�����ؼ���ƥ��������ҵ��ʳ�����0-500֮���
					long end = System.currentTimeMillis();
					System.out.println("��ȡ" + keyword + "��������Ϣ��ʱ��" + (end - start)/1000 + "��...");
				}
							
			}
		}
	}
	
	/**
	 * ���ڽ����Ѿ���ȡ����������
	 * @author zhengtaishuai
	 */
	public static void analysis2(String course) throws Exception {
		tag2 t = new tag2();
		KeyWordMatch k = new KeyWordMatch();
		File file0 = new File("F:/����/�γ�����/" + course);
		File[] files = file0.listFiles();
		long start = System.currentTimeMillis();
		for (int i = 0; i < files.length; i++) {
			String keyword = files[i].getName();
			if(!keyword.contains(".html")){
				int pagelength = t.PageLength(keyword);
				t.Down2Excel(keyword, pagelength);
				k.match3(keyword);    //����-changed�ļ��������ؼ���ƥ�����͵��ʳ�����Ϣ�����ͳ����йصı�Ϊ1�������Ϊ0��
				k.match(keyword);     //����-changed1�ļ���ֻ�����ؼ���ƥ����������ʳ���δ����
				k.match1(keyword);    //����-changed2�ļ���ֻ�����ؼ���ƥ��������ҵ��ʳ�����0-500֮���
				System.out.println(keyword + "  �������...");
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("�ܹ���ʱ�� " + (end - start) / 1000 + " ��...");
	}
	
	/**
	 * ������ȡ����ҳ��
	 * @author zhengtaishuai
	 */
	public static void analysis3(String course) throws Exception {
		tag2 t = new tag2();
		File file0 = new File("F:/����/�γ�����/" + course);
		File[] files = file0.listFiles();
		for (int i = 0; i < files.length; i++) {
			String keyword = files[i].getName();
			t.GetChildPages(keyword);
			int pagelength = t.PageLength(keyword);
			t.Down2Excel(keyword, pagelength);
		}
	}

	/**
	 * ���ݹؼ��ʣ������ļ���
	 */
	public String GetCatalog(String keyword) {
		String catalog = null;
		File file0 = new File("f:/����/�γ�����/");
		File[] files = file0.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file1 = new File("f:/����/�γ�����/" + files[i].getName() + "/");
			File[] files1 = file1.listFiles();
			for (int j = 0; j < files1.length; j++) {
				if (files1[j].getName().equals(keyword + ".html")) {
					catalog = "f:/����/�γ�����/" + files[i].getName() + "/"
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
	 * �õ��ؼ���Ŀ¼����������ҳ�����ɴ��
	 */
	public String GetCatalog2(String keyword) {
		String catalog = null;
		File file0 = new File("f:/����/�γ�����/");
		File[] files = file0.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file1 = new File("f:/����/�γ�����/" + files[i].getName() + "/");
			File[] files1 = file1.listFiles();
			for (int j = 0; j < files1.length; j++) {
				if (files1[j].getName().equals(keyword)) {
					catalog = "f:/����/�γ�����/" + files[i].getName() + "/" + keyword + "/";
				}
			}
		}
		return catalog;
	}
	
	/**
	 * ���ڵõ���ȡ���ݵ�ʱ��
	 */
	public static String GetCrawlerTime() throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//�������ڸ�ʽ 
		String CrawlerTime = df.format(new Date());   // new Date()Ϊ��ȡ��ǰϵͳʱ��
		System.out.println("CrawlerTime1 is ��" + CrawlerTime);
		return CrawlerTime;
	}

	/**
	 * �����ؼ�����ҳ��Array.html�����õ���ҳ�����������ҳ�������
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
			System.out.println(path + "  �����ڣ��ò���������ҳ�����ӣ�����");
			return testresult;
		} else {
			Document doc = parsePathText(path);
			Elements links = doc.select("div.pagedlist_item")
					.select("div.title ").select("a.question_link")
					.select("a[href]");
			// print("\n" + path + " �������������  Links: (%d)", links.size());

			String urls[] = new String[links.size()];
			for (int i = 0; i < links.size(); i++) {
				Element link = links.get(i);
				urls[i] = "http://www.quora.com" + link.attr("href"); // ���������Ӵ浽urls��������
				print(" * " + (i + 1) + ": <%s>  (%s)", urls[i],
						trim(link.text(), 50));
			}
			return urls;
		}
	}

	/**
	 * ��ȡ���йؼ��ʵ�������ҳ��Array10.html����pagelength������ҳ������
	 * 
	 * @throws Exception
	 */
	public void GetChildPages(String keyword) throws Exception {
		String[] urls = GetChildUrls(keyword);
//		System.out.println("test line...");
		if (urls.equals(testresult)) {
			System.out.println("�����ڵ�һ�����ӣ�����");
		} else {
			for (int j = 0; j < urls.length; j++) {
				PageDown a = new PageDown();
				a.pagedown2(keyword, j, urls[j]);   // ��ȡ�����й����������ҳ
				GetAuthorPages(keyword, j);         // ��ȡ��������ҳ��
			}
		}
	}
	
	/**
	 * ��ȡ����ҳ��
	 * @throws Exception
	 */
	public void GetAuthorPages(String keyword, int n) throws Exception {
		// ��ȡ������Ϣ
		tag2 t = new tag2();
		String path = t.GetCatalog2(keyword);
		String filepath = path + keyword + n + ".html";
		Document doc = parsePathText(filepath);
		Elements authors = doc.select("div.grid_page_center_col").select("div.pagedlist_item").select("div.author_info");
		for (int m = 0; m < authors.size(); m++) {
			Element a = authors.get(m);
			Elements b = a.select("a.user");
			if (b.size() == 0) {
				System.out.println("������Ϣ�����ڣ�����");
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
					p.pagedown4(keyword, filename, url); // ��ȡ����ҳ��
				}else{
					System.out.println(filepath1 + "���ڣ�����Ҫ��ȡ");
				}
				
			}
		}
	}
	
	/**
	 * ��λ��������ҳ���ַ
	 * @throws Exception
	 */
	public String GetAuthorPagesName(String keyword, int n) throws Exception {
		// ��ȡ������Ϣ
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
				System.out.println("������Ϣ�����ڣ�����");
			} else {
				String filename = keyword + n + "_author_" + m + ".html";
				String filepath1 = path + filename;
				File file1 = new File(filepath1);
				if(!file1.exists()){
					auth = filepath1;
				}else{
					auth = filepath1 + "���ڣ�����Ҫ��ȡ";
				}
			}
		}
		return auth;
	}

	/**
	 * �õ�pagelength������ҳ������
	 * @throws Exception
	 */
	public int PageLength(String keyword) throws Exception {
		int pagelength = 0;
		String[] urls = GetChildUrls(keyword);
		if (urls.equals(testresult)) {
			System.out.println("�����ڵ�һ�����ӣ�����");
		} else {
			pagelength = urls.length;
		}
		System.out.println(keyword + "��������Ϊ��" + pagelength);
		return pagelength;
	}

	/**
	 * ������ҳ��������,������������string
	 * 
	 * @param doc
	 */
	public static String QuestionName(Document doc) {
		Element a = doc.select("div.grid_page_center_col").get(0);
		Elements name = a.select("div.header").select("div.question_text_edit")
				.select("h1");
		if (name.size() == 0) {
			return "����ҳ���������⡣";
		} else {
			String name_s = name.get(0).text();
//			System.out.println("��������Ϊ��" + name_s);
			return name_s;
		}
	}
	
	/**
	 * ������ҳ���⸽����Ϣ,�������⸽����Ϣstring
	 * @param doc
	 */
	public static String QuestionExpand(Document doc) {
		Element a = doc.select("div.grid_page_center_col").get(0);
		Elements expandinfo = a.select("div.header").select("div.question_details").select("div.expanded_q_text");
		if (expandinfo.size() == 0) {
			return "����ҳ�����ڸ�����Ϣ...";
		} else {
			String expandinfo_s = expandinfo.get(0).text();
			
//			System.out.println("��������Ϊ��" + expandinfo_s);
			
			return expandinfo_s;
		}
	}

	/**
	 * ������ҳ�� ����� ���� ����
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
			// ʹ��split�õ��ı��ĵ�����Ŀ
			String[] answers = name_s.split(" ");
			int length = answers.length;
			System.out.println("���ⵥ�ʳ���Ϊ��" + length);
			return length;
		}
	}

	/**
	 * ������ҳ�� ����� �ַ� ����
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
			System.out.println("�����ַ�����Ϊ��" + length);
			return length;
		}
	}

	/**
	 * ����������ҳ�����䱣�浽���ص�Excel����
	 */
	public void Down2Excel(String keyword, int pagelength) throws Exception {
		String catalog = GetCatalog2(keyword);
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog + keyword + "-tag.xls"));
		WritableSheet sheet = workbook.createSheet("��ǩ", 0);
		/** ************���õ�Ԫ������************** */
		// ����
		WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
		/** ************�������ü��ָ�ʽ�ĵ�Ԫ��************ */
		WritableCellFormat wcf_center = new WritableCellFormat(NormalFont);
		wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN);
		wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);
		wcf_center.setAlignment(Alignment.CENTRE);
//		wcf_center.setBackground(Colour.YELLOW);
		wcf_center.setWrap(true);
		/** ************��Ԫ���ʽ�������****************** */
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
		sheet.setRowView(0, 700, false); // �����и�
		int number = 1;
		for (int j = 0; j < pagelength; j++) {
			String path = catalog + keyword + j + ".html";
			File file = new File(path);
			if (!file.exists()) {
				System.out.println(path + "  ������");
			} else {
				System.out.println("\n��ʼ������ " + path);
				Document doc = parsePathText(path);
				String questionname = QuestionName(doc);
				String questionexpand = QuestionExpand(doc);
//				int questionlength2 = QuestionLength2(doc);
				int questionlength = QuestionLength(doc);
				String want_answer = PageAnalysis.Want_Answers(doc);   //��֪������𰸵�����
				String question_comment = PageAnalysis.Comment_Numbers_question(doc); //��������
				System.out.println(pagelength);
//				try{
					sheet.addCell(new Label(0, number, keyword + j, wcf_center));
					sheet.addCell(new Label(1, number, questionname + "\n" + "Expanded information��" + questionexpand, wcf_center));
//					sheet.addCell(new Label(2, number, questionlength2 + "", wcf_center));
					sheet.addCell(new Label(2, number, "1", wcf_center));
					sheet.addCell(new Label(3, number, want_answer, wcf_center));  
					sheet.addCell(new Label(4, number, "0", wcf_center));      //Ĭ��û������
					sheet.addCell(new Label(5, number, question_comment, wcf_center));
					sheet.addCell(new Label(6, number, "0", wcf_center));
					sheet.addCell(new Label(7, number, "0", wcf_center));
					sheet.addCell(new Label(8, number, "0", wcf_center));
					sheet.addCell(new Label(9, number, "0", wcf_center));
					sheet.addCell(new Label(10, number, questionlength + "", wcf_center));
					sheet.addCell(new Label(11, number, "1", wcf_center));
					sheet.addCell(new Label(12, number, j + "", wcf_center));
					
					sheet.setRowView(number, 1300, false); // �����и�
					sheet.setColumnView(0, 20);
					sheet.setColumnView(1, 60);
					int realanswernumber = PageAnalysis.RealAnswerNumber(doc);
					for (int m = number; m < number + realanswernumber; m++) {
						sheet.setRowView(m + 1, 1300, false); // �����и�
						String answercontent = PageAnalysis.AnswerContent(doc, m - number);
//						int contentlength2 = PageAnalysis.Content_Length2(doc, m - number); // �ַ�����
						int contentlength = PageAnalysis.Content_Length(doc, m - number);   // ���ʳ���
						String upvote = PageAnalysis.Upvotes(doc, m - number);              // ֧��Ʊ�� 
						String url = PageAnalysis.URLs(doc, m - number);                    // ��������
						String comment = PageAnalysis.Comment_Numbers(doc, m - number);     // ��������
						
						String follower = null;
						String info = null;
						String know = null;
						String answer = null;
						String authorpath = catalog + keyword + j + "_author_" + (m - number) + ".html";
						File file1 = new File(authorpath);
						if(!file1.exists()){
							System.out.println(authorpath + "   ������");
						}else{
							Document doc1 = parsePathText(authorpath);  //��������ҳ��·��
							
							follower = PageAnalysis.Author_Followers(doc1, m - number);   // ���߷�˿
							info = PageAnalysis.Author_Info(doc, m - number);            // ���߼���
							know = PageAnalysis.Author_KnowAbout(doc1, m - number);       // ��������
							answer = PageAnalysis.Author_Answers(doc1, m - number);       // �ش�����
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
//			System.out.println(path + " �Ѿ��ɹ������� " + catalog + keyword + "-tag.xls");
		}
		workbook.write(); // д��������д��Ԫ�����Բ��ܷ���ѭ�����棬����������
		/** *********�ر��ļ�************* */
		workbook.close();
	}

	/**
	 * ����excel������ͻش���ֻҪ�г��ֹؼ��ʣ��ͽ�ԭExcel���еĵ������е�ֵ 1 ��Ϊ 0��д��һ���µ�Excel����
	 */
	public void match3(String keyword) throws Exception {
		tag2 entity = new tag2();
		String catalog = entity.GetCatalog2(keyword);
		String path = catalog + keyword + "-tag.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0);              //�õ�excel���
		int rsRows = rs.getRows();
		String[] keywordarray = keyword.split("\\+");      //���ؼ�����+�ֿ��浽�������棬����ƥ���ı�
		System.out.println("�ؼ��ʵ�����У� ");
		for(int i = 0; i < keywordarray.length; i++){
			System.out.println(keywordarray[i]);
		}
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog + keyword + "-tag_changed.xls"));
		WritableSheet sheet = workbook.createSheet("��ǩ", 0);     //׼���½���д��excel
		for (int i = 0; i < rsRows; i++) {
			//��ԭ����һ������д����Excel�У����ݲ���
			sheet.setColumnView(0, 20);
			sheet.setColumnView(1, 60);
			WritableCellFormat wcf_center = set();
			for (int col = 0; col < 11; col++) {
				sheet.setRowView(i, 1300, false);
				sheet.addCell(new Label(col, i, rs.getCell(col, i).getContents(), wcf_center));
			}
			//��������У�����ı�Ƭ���Ƿ���ؼ������
			Cell cell = rs.getCell(1, i);
			String content = cell.getContents();
			int count = 0;
			for (int j = 0; j < keywordarray.length; j++) {
				// ���ַ������ҹؼ��ʳ��ֵĴ���
				Pattern p = Pattern.compile("(?i)" + keywordarray[j]);    //���Դ�Сд
				Matcher m = p.matcher(content);
				while (m.find()) {
					count++;
				}
			}
			System.out.println("�ؼ��ʳ��ִ���: " + count);
			if (count == 0) {
				if(i == 0){
					sheet.addCell(new Label(11, i, rs.getCell(11, 0).getContents(), wcf_center));
				}else{
					sheet.addCell(new Label(11, i, "0", wcf_center));
					System.out.println("�� " + i + " ����Ԫ��û�г��ֹؼ��ʣ��������޹أ��������б�0...");
				}
			} else {
				sheet.addCell(new Label(11, i, "1", wcf_center));
				System.out.println("�� " + i + " ����Ԫ�г��ֹؼ��ʣ�������ֵ����...");
			}
		}
		workbook.write();
		workbook.close();
		System.out.println(keyword + "�������...\n");
	}
	
	/**
	 * ����Ԥ���������ݣ�ֻ����ƥ���ֵΪ1�Ļ��е��ʳ�����0-500֮�������
	 */
	public void match4(String keyword) throws Exception {
		tag2 entity = new tag2();
		String catalog = entity.GetCatalog2(keyword);
		String path = catalog + keyword + "-tag.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0);              //�õ�excel���
		int rsRows = rs.getRows();
		String[] keywordarray = keyword.split("\\+");      //���ؼ�����+�ֿ��浽�������棬����ƥ���ı�
		System.out.println("�ؼ��ʵ�����У� ");
		for(int i = 0; i < keywordarray.length; i++){
			System.out.println(keywordarray[i]);
		}
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog + keyword + "-tag_changed2.xls"));
		WritableSheet sheet = workbook.createSheet("��ǩ", 0);     //׼���½���д��excel
		for (int i = 0; i < rsRows; i++) {
			//��ԭ����һ������д����Excel�У����ݲ���
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
			//��������У�����ı�Ƭ���Ƿ���ؼ������
			Cell cell = rs.getCell(1, i);
			String content = cell.getContents();
			int count = 0;
			for (int j = 0; j < keywordarray.length; j++) {
				// ���ַ������ҹؼ��ʳ��ֵĴ���
				Pattern p = Pattern.compile("(?i)" + keywordarray[j]);    //���Դ�Сд
				Matcher m = p.matcher(content);
				while (m.find()) {
					count++;
				}
			}
			System.out.println("�ؼ��ʳ��ִ���: " + count);
			if (count == 0) {
				if(i == 0){
					sheet.addCell(new Label(10, i, rs.getCell(4, 0).getContents(), wcf_center));
				}else{
					sheet.addCell(new Label(10, i, "0", wcf_center));
					System.out.println("�� " + i + " ����Ԫ��û�г��ֹؼ��ʣ��������޹أ��������б�0...");
				}
			} else {
				sheet.addCell(new Label(10, i, "1", wcf_center));
				System.out.println("�� " + i + " ����Ԫ�г��ֹؼ��ʣ�������ֵ����...");
			}
		}
		workbook.write();
		workbook.close();
		System.out.println(keyword + "�������...\n");
	}
	
	/**
	 * ����ָ��·����html�ļ�doc
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
	 * ����Excel���Ԫ���ʽ
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
	 * ���������ʽ
	 */
	private static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

	/**
	 * ���ָ�����ȵ��ַ���
	 */
	private static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1) + ".";
		else
			return s;
	}

}
