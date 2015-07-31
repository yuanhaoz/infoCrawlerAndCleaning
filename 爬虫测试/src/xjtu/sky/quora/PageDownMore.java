package xjtu.sky.quora;

/**
 * zhengyuanhao  2014/11/27
 * ��ʵ�֣�quora  
 * ��ȡ��ҳ�������������
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

	static int pagenumbers; // ��һ����ҳ��Ŀ
	static String[] testresult = { "aa", "bb" };

	/**
	 * ����һ����������õ���һ����ҳ ����keyword ��ʾ�����Ĺؼ��ʣ���ʽΪ��Binary-Trees����ͷ��ĸ��д���м��� - ���ӣ�
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
	 * ����һ����������õ���һ����ҳ���õ����������ص����⣨related topics�� ���Եõ������йص����⣬more��Ӱ�첻���ڣ�����
	 */
	public String[] GetRelatedTopics(String keyword) throws Exception {
		String path = "f:/test/" + keyword + "0.html";
		Document doc = parsePathText(path);
		// �õ�����related topics
		Elements relatedtopics = doc.select("div.grid_page_right_col")
				.select("li.related_topic_item")
				.select("div.related_topic_item_info").select("span.TopicName");
		String[] topicname = new String[relatedtopics.size()];
		System.out.println("related topics �У�");
		for (int i = 0; i < topicname.length; i++) {
			Element topic = relatedtopics.get(i);
			topicname[i] = topic.text();
			System.out.println(topicname[i]);
		}
		return topicname;
	}

	/**
	 * ������һ��ҳ�棬�õ�����������ҳ������ȡ�����أ����ص��ǵ�һ��ҳ������� ����keyword ��ʾ����binary
	 * trees������������ҳ�ĵ�һ��ҳ��
	 */
	public static void GetOtherPages(String keyword) throws Exception {
		String path = "f:/test/" + keyword + "0.html";
		File file = new File(path);
		if (!file.exists()) {
			System.out.println("f:/test/" + keyword + "0.html"
					+ "  �����ڣ����������У�����");
		} else {
			Document doc = parsePathText(path);
			// �õ����ڹؼ��ʵ�ʣ����й���ҳ
			Elements lastlink = doc.select("div.grid_page_center_col");
			Elements a = lastlink.select("a[title^=go to page]");
			// �ж��Ƿ���ڶ��ҳ�棬����ֻ��һҳ��Ԫ��a�Ĵ�СΪ0
			if (a.size() == 0) {
				System.out.println("ֻ��һҳ������");
				pagenumbers = 1;
			} else {
				Element b = a.last();
				int num = Integer.parseInt(b.text()); // ��һ����ҳ����
				String linkurls[] = new String[num - 1];
				for (int i = 2; i <= num; i++) {
					// Element link = linkpage2.get(i);
					linkurls[i - 2] = "http://www.quora.com/" + keyword
							+ "?page_id=" + i; // ���������Ӵ浽linkurls��������
					print(" * " + i + ": <%s>  (%s)", linkurls[i - 2], null, 60);
				}
				// ��ʣ��ҳ�����ص�����
				for (int i = 0; i < linkurls.length; i++) {
					int j = i + 1;
					String str = keyword + j; // ��ɹؼ��ּ����֣�Binary-Trees1������ʽ
					PageDown a1 = new PageDown();
					a1.pagedown(str, linkurls[i]);
				}
				pagenumbers = num;
			}
		}
	}

	/**
	 * ������ҳ���õ���ҳ���һ����ҳ�棨�����йأ�������
	 * 
	 * @throws Exception
	 */
	public static String[] GetChildUrls(String keyword, int n) throws Exception {
		// String[] testresult = {"aa","bb"};
		String path = "f:/test/" + keyword + n + ".html";
		File file = new File(path);
		if (!file.exists()) {
			System.out.println(path + "  �����ڣ��ò���������ҳ�棡����");
			return testresult;
		} else {
			Document doc = parsePathText(path);
			Elements links = doc.select("div.QuestionText").select("a[href]");
			print("\n" + path + " �������������  Links: (%d)", links.size());

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
	 * ����һ����ҳ���õ���ҳ����������⣬���䱣�浽Excel������з���
	 * 
	 * @throws Exception
	 */
	public static String[] GetQuestion(String keyword, int n) throws Exception {
		String path = "f:/test/" + keyword + n + ".html";
		File file = new File(path);
		if (!file.exists()) {
			System.out.println(path + "  �����ڣ��ò����������⣡����");
			return testresult;
		} else {
			Document doc = parsePathText(path);
			Elements links = doc.select("div.QuestionText").select("a[href]");

			String urls[] = new String[links.size()];
			for (int i = 0; i < links.size(); i++) {
				Element link = links.get(i);
				// ������������
				urls[i] = link.text();
				print(" * " + (i + 1) + ": <%s>  (%s)", urls[i],
						trim(link.text(), 100));
			}
			return urls;
		}
	}

	/**
	 * �õ�һ��ҳ����������⣬���䱣�浽Excel������з���
	 * 
	 * @throws Exception
	 */
	public void SaveQuestion2Excel(String keyword) throws Exception {
		WritableWorkbook workbook = Workbook.createWorkbook(new File("f:/test/"
				+ keyword + "_question_list.xls"));
		WritableSheet sheet = workbook.createSheet("�����ܻ�", 0);
		// ���ڱ���
		WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
		WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 12,
				WritableFont.BOLD);
		WritableCellFormat wcf_title = new WritableCellFormat(BoldFont);
		wcf_title.setBorder(Border.ALL, BorderLineStyle.THIN); // ����
		wcf_title.setVerticalAlignment(VerticalAlignment.CENTRE); // ��ֱ����
		wcf_title.setAlignment(Alignment.CENTRE); // ˮƽ����
		wcf_title.setBackground(Colour.GRAY_25);
		wcf_title.setWrap(true); // �Ƿ���
		// ����������
		WritableCellFormat wcf_center = new WritableCellFormat(NormalFont);
		wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN);
		wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);
		wcf_center.setAlignment(Alignment.CENTRE);
		wcf_center.setWrap(true);
		/** */
		/** ************��Ԫ���ʽ�������****************** */
		String path = "f:/test/" + keyword + "0.html";
		File file = new File(path);
		if (!file.exists()) {
			System.out.println("f:/test/" + keyword + "0.html"
					+ "  �����ڣ����������У�����");
		} else {
			Document doc = parsePathText(path);
			// �õ����ڹؼ��ʵ�ʣ����й���ҳ
			Elements lastlink = doc.select("div.grid_page_center_col");
			Elements a = lastlink.select("a[title^=go to page]");
			// �ж��Ƿ���ڶ��ҳ�棬����ֻ��һҳ��Ԫ��a�Ĵ�СΪ0
			if (a.size() == 0) {
				System.out.println("ֻ��һҳ������");
				pagenumbers = 1;
			} else {
				Element b = a.last();
				pagenumbers = Integer.parseInt(b.text()); // ��һ����ҳ����
			}
		}
		sheet.mergeCells(0, 0, pagenumbers - 1, 0);
		sheet.setRowView(0, 800, false);
		sheet.addCell(new Label(0, 0, keyword + "�����µ���������", wcf_title));
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
		/** *********�ر��ļ�************* */
		workbook.close();
	}

	/**
	 * ��ȡkeyword����������ҳ��������ҳ�� pagenumbers�ǵ�һ��ҳ����Ŀ��pagelength[i]��ÿ����һ��ҳ�����ҳ����Ŀ
	 * 
	 * @throws Exception
	 */
	public int[] GetChildPages(String keyword) throws Exception {
		GetOtherPages(keyword); // ��������ҳ�棬������ҳ������
		int[] pagelength = new int[pagenumbers];
		for (int i = 0; i < pagenumbers; i++) {
			String[] urls = GetChildUrls(keyword, i);
			// System.out.println("���ǲ����С�������������");
			if (urls.equals(testresult)) {
				System.out.println("�����ڵ�һ�����ӣ�����");
				pagelength[i] = 0;
			} else {
				// String[] urls = GetUrls(keyword,i); //�õ��ڶ���ҳ�������
				pagelength[i] = urls.length; // �õ���һ��ҳ�����������������Ŀ
				for (int j = 0; j < urls.length; j++) {
					String str = keyword + i + "_" + j; // ��ҳ����ڱ��صĸ�ʽ�ǡ�Binary-Trees1_1��
					PageDown a2 = new PageDown();
					a2.pagedown(str, urls[j]); // ��ȡ�����й����������ҳ

					// Thread.sleep(1000);
					// ��ȡ������Ϣ
					String path = "f:/test/" + str + ".html";
					Document doc = parsePathText(path);
					Elements authors = doc.select("div.pagedlist_item").select(
							"div.author_info");
					for (int m = 0; m < authors.size(); m++) {
						Element a = authors.get(m);
						Elements b = a.select("a.user");
						if (b.size() == 0) {
							System.out.println("������Ϣ�����ڣ�����");
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
							a3.pagedown(savestr, link); // ��ȡ����ҳ��
						}
					}
				}
			}
		}
		return pagelength;
	}

	/**
	 * ����������ҳ�����䱣�浽���ص�Excel����
	 */
	public void Down2Excel(String keyword, int[] pagelength) throws Exception {
		for (int i = 0; i < pagenumbers; i++) {
			if (pagelength[i] == 0) {
				System.out.println("��һ�����Ӳ����ڣ��ò����ڶ������ӵ���Ŀ");
			} else {
				for (int j = 0; j < pagelength[i]; j++) {
					PageAnalysis.Save2Excel(keyword, i, j);
				}
			}
		}
	}

	/**
	 * ����ָ��·����html�ļ�doc
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
