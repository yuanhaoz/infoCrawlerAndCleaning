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
	 * ʵ�ֹ��ܣ�ʵ����������ϴ
	 * @param course
	 */
	public static void realize() throws Exception {
		Crawler("Computer_network");
//		Analysis("Computer_network");
	}
	
	/**
	 * ʵ�ֹ��ܣ�������ȡĳ�ſγ����������µ�������ҳ
	 *          �����ǿγ����������������ҳ
	 * @param course
	 */
	public static void Crawler(String course) throws Exception {
		String catalog = "F:/����/�γ�����/" + course;
		ArrayList<String> a = DirFile.getFileNamesFromDirectorybyArraylist(catalog);  //��ȡ�����ļ���
		setKeywordCatalog(course);           //Ϊ�ؼ��ʽ���Ŀ¼
		Iterator<String> it = a.iterator();   //���õ�����
		while(it.hasNext()){                  //�ж��Ƿ�����һ��
			long start = System.currentTimeMillis();
			String keyword = it.next();       //�õ��ؼ���
			CrawlerSubjectPages(keyword);     //��ȡ����ҳ��
			CrawlerQuestionAndAuthorPages(keyword);            //��ȡ����ҳ�������ҳ��
			long end = System.currentTimeMillis();
			System.out.println("��ȡ" + keyword + "��������Ϣ��ʱ��" + (end - start)/1000 + "��...");
		}
	}
	
	/**
	 * ʵ�ֹ��ܣ����ڽ���ĳ�ſγ����������µ�������ҳ
	 *          �����ǿγ����������Excel
	 * @param course
	 */
	public static void Analysis(String course) throws Exception {
		KeyWordMatch k = new KeyWordMatch();
		String catalog = "F:/����/�γ�����/" + course;
		ArrayList<String> a = DirFile.getFileNamesFromDirectorybyArraylist(catalog);  //��ȡ�����ļ���
		Iterator<String> it = a.iterator();   //���õ�����
		while(it.hasNext()){                  //�ж��Ƿ�����һ��
			long start = System.currentTimeMillis();
			String keyword = it.next();       //�õ��ؼ���
			int pagelength = QuestionPageNumber(keyword);      
			Down2Excel(keyword, pagelength);                   //��������ҳ�������ҳ��
//			k.match3(keyword);    //����-changed�ļ��������ؼ���ƥ�����͵��ʳ�����Ϣ
//			k.match(keyword);     //����-changed1�ļ���ֻ�����ؼ���ƥ����������ʳ���δ����
//			k.match1(keyword);    //����-changed2�ļ���ֻ�����ؼ���ƥ��������ҵ��ʳ�����0-500֮���
			long end = System.currentTimeMillis();
			System.out.println("����" + keyword + "��������Ϣ��ʱ��" + (end - start)/1000 + "��...");
		}
	}

	/**
	 * ʵ�ֹ��ܣ����ݿγ�����Ϊ�ÿγ�Ŀ¼�µ����йؼ���ҳ�����ɶ�Ӧ�ļ���
	 *          �����ǿγ����������������Ӧ�ļ���
	 * @param course
	 */
	public static void setKeywordCatalog(String course) {
		String catalog = "F:/����/�γ�����/" + course + "/";
		ArrayList<String> a = DirFile.getFileNamesFromDirectorybyArraylist(catalog);  //��ȡ�����ļ���
		Iterator<String> it = a.iterator();
		while(it.hasNext()){                  //�ж��Ƿ�����һ��
			String keyword = it.next();
			System.out.println(course + "�γ�Ŀ¼�������¹ؼ��ʣ�" + keyword); 
			String keywordcatalog = catalog + keyword + "/";
			File file = new File(keywordcatalog);
			if (!file.exists()) {
				file.mkdir();
			}
		}
	}
	
	/**
	 * ʵ�ֹ��ܣ����ݹؼ��ʣ���λ����Ӧ�ؼ���Ŀ¼
	 *  	        ���ڴ�š����⡱�������⡱�͡����ߡ�ҳ��
	 *          �����ǹؼ��ʣ������������Ӧ�ļ���
	 * @param keyword
	 */
	public static String GetKeywordCatalog(String keyword) {
		String keywordcatalog = "";
		String[] course = {"Computer_network", "Data_mining", "Data_structure"};
		for(int i = 0; i < course.length; i++){
			String catalog = "F:/����/�γ�����/" + course[i] + "/";
			ArrayList<String> a = DirFile.getFileNamesFromDirectorybyArraylist(catalog);  //��ȡ�����ļ���
			Iterator<String> it = a.iterator();
			while(it.hasNext()){                  //�ж��Ƿ�����һ��
				String testkeyword = it.next();
//				System.out.println(course + "�γ�Ŀ¼�������¹ؼ��ʣ�" + keyword);
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
	 * ʵ�ֹ��ܣ����ݹؼ��ʣ��õ�����ҳ������ӣ�һ��ҳ�棩
	 * @param keyword
	 */
	public static String GetSubjectURLs(String keyword){
		String url = "http://www.quora.com/search?q=" + keyword;
		System.out.println("����ҳ������Ϊ��" + url);
		return url;
	}
	
	/**
	 * ʵ�ֹ��ܣ�����������ҳ��
	 *          �õ�����ҳ������������ҳ�������
	 *          ��������һ����49-59֮�䣨��ȡʱ�����ĴΣ�
	 * @param keyword
	 */
	public static String[] GetQuestionURLs(String keyword) throws Exception {
		String path = tag2.GetKeywordCatalog(keyword) + keyword + ".html";
		// System.out.println(path);
		File file = new File(path);
		if (!file.exists()) {
			String[] testresult = { "aa", "bb" };
			System.out.println(path + "  �����ڣ��ò���������ҳ�����ӣ�����");
			return testresult;
		} else {
			Document doc = JsoupParse.parsePathText(path);
			Elements links = doc.select("div.pagedlist_item").select("div.title ")
					.select("a.question_link").select("a[href]");
			// print("\n" + path + " �������������  Links: (%d)", links.size());
			String urls[] = new String[links.size()];
			for (int i = 0; i < links.size(); i++) {
				Element link = links.get(i);
				urls[i] = "http://www.quora.com" + link.attr("href"); // ���������Ӵ浽urls��������
				print("����ҳ������Ϊ��" + " * " + (i + 1) + ": <%s>  (%s)", urls[i], trim(link.text(), 50));
			}
			return urls;
		}
	}
	
	/**
	 * ʵ�ֹ��ܣ���������ҳ�棬
	 *          �õ�����ҳ������������ҳ�������
	 *          keyword������ʣ�n ������ҳ���µ�����ҳ������
	 * @param keyword,n
	 */
	public static String[] GetAuthorURLs(String keyword, int n){
		String path = tag2.GetKeywordCatalog(keyword);
		String filepath = path + keyword + n + ".html";
		Document doc = JsoupParse.parsePathText(filepath);
		Elements authors = doc.select("div.grid_page_center_col").select("div.pagedlist_item").select("div.author_info");
		String[] url = new String[authors.size() - 3];    //�������������������
		for (int m = 0; m < authors.size() - 3; m++) {
			Element a = authors.get(m);
			Elements b = a.select("a.user");
			if (b.size() == 0) {
				System.out.println("������Ϣ�����ڣ�����");
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
	 * ʵ�ֹ��ܣ���ȡ��������ҳ�棨��ȡ�׶�1��
	 * 			������һ��ѧ������ĵ��������keyword
	 * @param keyword
	 */
	public static void CrawlerSubjectPages(String keyword) throws Exception {
		PageDown p = new PageDown();
		String keywordcatalog = tag2.GetKeywordCatalog(keyword);   //�õ������ļ���
		String filepath = keywordcatalog + keyword + ".html";      //��������ҳ�汣��·��
		String subjectUrls = tag2.GetSubjectURLs(keyword);         //�õ�����ҳ������
		p.seleniumCrawlerSubject(filepath, subjectUrls);           //��ȡ����ҳ��
	}
	
/**
	 * ʵ�ֹ��ܣ���ȡ���������µ���������ҳ�������ҳ�棨��ȡ�׶�2��
	 * 			�����������keyword
	 * @param keyword
	 */
	public static void CrawlerQuestionAndAuthorPages(String keyword) throws Exception {
		String catalog = tag2.GetKeywordCatalog(keyword);
		String[] urls = GetQuestionURLs(keyword);			   //�õ�����ҳ������
		String[] testresult = { "aa", "bb" };
		if (urls.equals(testresult)) {
			System.out.println("�����ڵ�һ�����ӣ�����");
		} else {
			for (int n = 0; n < urls.length; n++) {
				PageDown a = new PageDown();
				String filename = keyword + n + ".html";
				String filepath = catalog + filename;          //��������ҳ�汣��·��
				a.seleniumCrawlerQuestion(filepath, urls[n]);  //��ȡ������ҳ
				CrawlerAuthorPages(keyword, n);                //��ȡ����ҳ���е�����ҳ��
			}
		}
	}

	/**
	 * ʵ�ֹ��ܣ���ȡ��ĳ��������ҳ���е�����ҳ�棨��ȡ�׶�2.1��
	 * @param keyword,n
	 */
	public static void CrawlerAuthorPages(String keyword, int n) throws Exception {
		String catalog = tag2.GetKeywordCatalog(keyword);
		String[] authorUrls = tag2.GetAuthorURLs(keyword, n);         //�õ�����ҳ������
		for (int m = 0; m < authorUrls.length; m++) {
			String filename = keyword + n + "_author_" + m + ".html";
			String filepath = catalog + filename;                     //��������ҳ�汣��·��
			File file1 = new File(filepath);						  //�ж�����ҳ���Ƿ����
			if(!file1.exists()){
				PageDown a = new PageDown();
				a.seleniumCrawlerAuthor(filepath, authorUrls[m]);     //��ȡ����ҳ��
			}else{
				System.out.println(filepath + "���ڣ�����Ҫ��ȡ...");
			}
		}
	}

	/**
	 * �õ�pagelength������ҳ������
	 * @throws Exception
	 */
	public static int QuestionPageNumber(String keyword) throws Exception {
		int pagelength = 0;
		String[] urls = GetQuestionURLs(keyword);
		String[] testresult = { "aa", "bb" };
		if (urls.equals(testresult)) {
			System.out.println("�����ڵ�һ�����ӣ�����");
		} else {
			pagelength = urls.length;
		}
		System.out.println(keyword + "��������Ϊ��" + pagelength);
		return pagelength;
	}

	/**
	 * ����������ҳ�����䱣�浽���ص�Excel����
	 */
	public static void Down2Excel(String keyword, int pagelength) throws Exception {
		String catalog = GetKeywordCatalog(keyword);
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
		sheet.addCell(new Label(12, 0, "0", wcf_center));
		sheet.setRowView(0, 700, false); // �����и�
		int number = 1;
		for (int j = 0; j < pagelength; j++) {
			String path = catalog + keyword + j + ".html";
			File file = new File(path);
			if (!file.exists()) {
				System.out.println(path + "  ������");
			} else {
				System.out.println("\n��ʼ������ " + path);
				Document doc = JsoupParse.parsePathText(path);
				String questionname = PageAnalysis.QuestionContent(doc);
				String questionexpand = PageAnalysis.QuestionExpandInfo(doc);
//				int questionlength2 = QuestionLength2(doc);
				int questionlength = PageAnalysis.QuestionContentWordLength(doc);
				String want_answer = PageAnalysis.QuestionWantAnswers(doc);         //�����ע����
				String question_comment = PageAnalysis.QuestionCommentNumbers(doc); //��������
				System.out.println(pagelength);
//				try{
					sheet.addCell(new Label(0, number, keyword + j, wcf_center));
					sheet.addCell(new Label(1, number, questionname + "\n" + "Expanded information��" + questionexpand, wcf_center));
//					sheet.addCell(new Label(2, number, questionlength2 + "", wcf_center));
					sheet.addCell(new Label(2, number, "1", wcf_center));
					sheet.addCell(new Label(3, number, want_answer, wcf_center));  
					sheet.addCell(new Label(4, number, "0", wcf_center));         //Ĭ��û������
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
					int realanswernumber = PageAnalysis.CountRealAnswerNumber(doc);
					for (int m = number; m < number + realanswernumber; m++) {
						sheet.setRowView(m + 1, 1300, false); // �����и�
						String answercontent = PageAnalysis.AnswerContent(doc, m - number);
//						int contentlength2 = PageAnalysis.Content_Length2(doc, m - number); // �ַ�����
						int contentlength = PageAnalysis.AnswerContentWordLength(doc, m - number);   // ���ʳ���
						int upvote = PageAnalysis.AnswerUpvotes(doc, m - number);              // ֧��Ʊ�� 
						String url = PageAnalysis.AnswerURLs(doc, m - number);                    // ��������
						String comment = PageAnalysis.AnswerCommentNumbers(doc, m - number);     // ��������
						
						String follower = null;
						String info = null;
						String know = null;
						String answer = null;
						String authorpath = catalog + keyword + j + "_author_" + (m - number) + ".html";
						File file1 = new File(authorpath);
						if(!file1.exists()){
							System.out.println(authorpath + "   ������");
						}else{
							Document doc1 = JsoupParse.parsePathText(authorpath);  //��������ҳ��·��
							
							follower = PageAnalysis.AuthorFollowers(doc1, m - number);   // ���߷�˿
							info = PageAnalysis.AuthorInfo(doc, m - number);            // ���߼���
							know = PageAnalysis.AuthorKnowAbout(doc1, m - number);       // ��������
							answer = PageAnalysis.AuthorAnswers(doc1, m - number);       // �ش�����
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
//			System.out.println(path + " �Ѿ��ɹ������� " + catalog + keyword + "-tag.xls");
		}
		workbook.write(); // д��������д��Ԫ�����Բ��ܷ���ѭ�����棬����������
		/** *********�ر��ļ�************* */
		workbook.close();
	}

	/**
	 * ����excel������ͻش���ֻҪ�г��ֹؼ��ʣ��ͽ�ԭExcel���еĵ������е�ֵ 1 ��Ϊ 0��д��һ���µ�Excel����
	 */
	public static void match3(String keyword) throws Exception {
		String catalog = tag2.GetKeywordCatalog(keyword);
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
			WritableCellFormat wcf_center = ExcelSet.setCenterText();
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
	public static void match4(String keyword) throws Exception {
		String catalog = tag2.GetKeywordCatalog(keyword);
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
			WritableCellFormat wcf_center = ExcelSet.setCenterText();
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
	 * ���ڵõ���ȡ���ݵ�ʱ��
	 */
	public static String GetCrawlerTime() throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//�������ڸ�ʽ 
		String CrawlerTime = df.format(new Date());   // new Date()Ϊ��ȡ��ǰϵͳʱ��
		System.out.println("CrawlerTime1 is ��" + CrawlerTime);
		return CrawlerTime;
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
