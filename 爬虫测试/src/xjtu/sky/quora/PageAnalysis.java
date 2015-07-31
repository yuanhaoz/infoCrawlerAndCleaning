package xjtu.sky.quora;

/**
 * zhengyuanhao  2014/11/27
 * ��ʵ�֣�quora  
 * �������ص���ҳ������ҳ��������ݱ��浽Excel����
 * ������һ���Ѿ����ص����ص�quora�ش���ҳ������Ǳ����������ҳ�����Excel�ļ�
 * 
 */
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
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

@SuppressWarnings("deprecation")
public class PageAnalysis {

	/**
	 * ������
	 * 
	 * @param args
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public static void main(String[] args) throws Exception {
		String keyword = "Bipartite-Graphs"; // Binary-Trees Database-Systems
												// Binary-Search
		Save2Excel(keyword, 0, 0);
	}

	/**
	 * ����more��ǩ�õ����лش𣬣�ʧ���ˣ�ԭ����more�����ӻ���ָ���Լ������ҳ��
	 * 
	 * @param doc
	 */
	public static String MorePage(Document doc) {
		System.out.println("more������Ϊ��");
		Elements href = doc.select("a[href]");
		String morelink = new String();
		for (int i = 0; i < href.size(); i++) {
			String href_txt = href.get(i).text();
			// System.out.println(href_txt);
			if (href_txt.equals("More")) {
				morelink = href.get(i).attr("href");
			}
		}
		System.out.println(morelink);
		try {
			PageDown a = new PageDown();
			a.pagedown("more", morelink);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return morelink;
	}

	/**
	 * ��������Ŀ,���ش���Ŀstring
	 * 
	 * @param doc
	 */
	public static int AnswerNumber(Document doc) {
		Elements answer_count = doc.select("div.answer_count");
		String answer_count_s = answer_count.get(0).text();
		System.out.println("����ĿΪ��" + answer_count_s);
		int number = Integer.parseInt(answer_count_s.substring(0, answer_count_s.length()-8));
		System.out.println("����ĿΪ��" + number);
		return number;
	}

	/**
	 * �õ�������ҳ�����ʵ�ʴ���Ŀ
	 * 
	 * @param doc
	 */
	public static int RealAnswerNumber(Document doc) {
//		int real_answer_count_s = 0;
		Elements real_answer_count = doc.select("div.pagedlist_item");
		int number = real_answer_count.size() - 4;
		boolean more = doc.select("div.EmptyAnswerSuggestions").isEmpty();
		if (!more) {
			number = 0;
		}
//		if (real_answer_count.size() == 2 || real_answer_count.size() == 1) {
//			real_answer_count_s = real_answer_count.size() - 1;
//		} else {
//			real_answer_count_s = real_answer_count.size() - 2;
//		}
		
//		System.out.println("ʵ�ʴ���ĿΪ��" + number);
		
		return number;
	}

	/**
	 * ����֧��Ʊ�������ػش��ߴ𰸵�֧��Ʊ��string
	 * 
	 * @param doc,n
	 */
	public static String Upvotes(Document doc, int n) {
		Elements answer_voters = doc.select("div.Answer")
				.select("div.primary_item").select("a[class]").select("span");
		String answer_voters_s = answer_voters.get(n).text();
		System.out.println("֧��Ʊ��Ϊ��" + answer_voters_s);
		if (answer_voters_s.equals("0")) {
			System.out.println("֧��Ʊ��Ϊ0����ע�⣡����");
		}
		return answer_voters_s;
	}

	/**
	 * ������������Ŀ
	 * @param doc,n
	 */
	public static String Comment_Numbers(Document doc, int n) {
		Elements comment_numbers = doc.select("div.pagedlist_item")
				.select("div.Answer").select("div[class$=ActionBar]");
		Element per_answer = comment_numbers.get(n);
		Elements comment = per_answer.select("div.action_item")
				.select("span[action_click]").select("span[id]")
				.select("a[class]");
		if (comment.size() == 0) {
			System.out.println("��" + n + "���������ۣ�����");
			return "0";
		} else {
			Elements b = comment.select("span[class]");
			if (b.size() == 0) {
				System.out.println("��" + n + "���������ۣ�����");
				return "0";
			} else {
				String comment_number = b.text();
				System.out.println("��" + n + "��������ĿΪ��" + comment_number);
				return "1";
			}
		}
	}
	
	/**
	 * ��������������Ŀ
	 * @param doc,n
	 */
	public static String Comment_Numbers_question(Document doc) {
		Elements questions = doc.select("div.header").select("div[class^=ActionBar]");
		if(questions.size() == 0){
			questions = doc.select("div.reviews_header_wrapper").select("div[class^=ActionBar]");
		}
		Element question = questions.get(0);
		Elements comment = question.select("div.action_item")
				.select("span[action_click]").select("span[id]")
				.select("a[class]");
		if (comment.size() == 0) {
			System.out.println("���ⲻ�������ۣ�����");
			return "0";
		} else {
			Elements b = comment.select("span[class]");
			if (b.size() == 0) {
				System.out.println("���ⲻ�������ۣ�����");
				return "0";
			} else {
				String comment_number = b.text();
				System.out.println("����������ĿΪ��" + comment_number);
				return "1";
			}
		}
	}

	/**
	 * �����ش𳤶�,���Ͽո�,����һ�� ���� Ϊһ�������
	 * @param doc,n
	 */
	public static int Content_Length(Document doc, int n) {
		Elements contents = doc.select("div.Answer");
		Element content_i = contents.get(2 * n);
		String content = content_i.attr("id");
		Elements title = doc.select("div.header").select("div.question_text_edit");
		Elements content_j = doc.select("div.Answer").select("div#" + content + "_content");
		Elements content_k = content_j.select("div[class^=Expandable]").select("span.inline_editor_value").select("div[id$=outer]").select("div[id$=container]");
		Element per;
		if (content_k.size() == 0) {
			if(content_j.size() == 0){   //�������û�г��ֻش𣬾����Ϊ��
				per = title.first();
			}else{
				per = content_j.first();
			}
		} else {
			per = content_k.first();
		}

		String answer_context_s = null;
		try{
			answer_context_s = per.text(); // ���ݿ��ܸ�����ȡ���еĵ�һ������
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("�õ������ݳ���...");
		}
		// ʹ��split�õ��ı��ĵ�����Ŀ
		String[] answers = answer_context_s.split(" ");
		int content_length = answers.length;
		System.out.println("�ش𵥴ʳ���Ϊ��" + content_length);
		return content_length;
	}

	/**
	 * �����ش𳤶�,���Ͽո�,����һ�� �ַ� Ϊһ�������
	 * @param doc,n
	 */
	public static int Content_Length2(Document doc, int n) {
		Elements contents = doc.select("div.Answer");
		Element content_i = contents.get(2 * n);
		String content = content_i.attr("id");
		Elements title = doc.select("div.header").select("div.question_text_edit");
		Elements content_j = doc.select("div.Answer").select("div#" + content + "_content");
		Elements content_k = content_j.select("div[class^=Expandable]").select("span.inline_editor_value").select("div[id$=outer]").select("div[id$=container]");
		Element per;
		if (content_k.size() == 0) {
			if(content_j.size() == 0){   //�������û�г��ֻش𣬾����Ϊ��
				per = title.first();
			}else{
				per = content_j.first();
			}
		} else {
			per = content_k.first();
		}

		String answer_context_s = null;
		try{
			answer_context_s = per.text(); // ���ݿ��ܸ�����ȡ���еĵ�һ������
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("�õ������ݳ���...");
		}
		// ʹ��split�õ��ı��ĵ�����Ŀ
		// String[] answers = answer_context_s.split(" ");
		int content_length = answer_context_s.length();
		System.out.println("�ش��ַ�����Ϊ��" + content_length);
		return content_length;
	}

	/**
	 * �������ݵõ�����
	 * @param doc,n
	 */
	public static String URLs(Document doc, int n) {
		Elements contents = doc.select("div.Answer");
		Element content_i = contents.get(2 * n);
		String content = content_i.attr("id");
		Elements title = doc.select("div.header").select("div.question_text_edit");
		Elements content_j = doc.select("div.Answer").select("div#" + content + "_content");
		Elements content_k = content_j.select("div[class^=Expandable]").select("span.inline_editor_value").select("div[id$=outer]").select("div[id$=container]");
		Element per;
		if (content_k.size() == 0) {
			if(content_j.size() == 0){            //�������û�г��ֻش𣬾����Ϊ��Ŀ����
				per = title.first();
			}else{
				per = content_j.first();
			}
		} else {
			per = content_k.first();
		}
		
		Elements a = per.select("a.external_link"); // �õ������������������
		if (a.size() == 0) {
			return "0";
		} else {
			String urls = "1" + "\n" + "�����ǣ�"; // ���ڱ�������
			for (int i = 0; i < a.size(); i++) {
				Element b = a.get(i);
				urls = urls + "\n" + b.attr("href");
			}
			String exist = "1";
			return exist;
		}
	}

	/**
	 * ���������ݣ����ػش��ߵĴ����� string
	 * @param doc, n
	 */
	public static String AnswerContent(Document doc, int n) {
//		System.out.println("\n�ش�����Ϊ��");
		Elements contents = doc.select("div.Answer");
		Element content_i = contents.get(2 * n);
		String content = content_i.attr("id");
		Elements title = doc.select("div.header").select("div.question_text_edit");
		Elements content_j = doc.select("div.Answer").select("div#" + content + "_content");
		Elements content_k = content_j.select("div[class^=Expandable]").select("span.inline_editor_value").select("div[id$=outer]").select("div[id$=container]");
		Element per;
		if (content_k.size() == 0) {
			if(content_j.size() == 0){            //�������û�г��ֻش𣬾����Ϊ��Ŀ����
				per = title.first();
			}else{
				per = content_j.first();
			}
		} else {
			per = content_k.first();
		}

		String answer_context_s = null;
		try{
			answer_context_s = per.text(); // ���ݿ��ܸ�����ȡ���еĵ�һ������
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("�õ������ݳ���...");
		}
//		System.out.println(answer_context_s);
		
//		for (int j = 0; j < answer_context_s.length() / 100; j++) { // ÿ����ʾ100�ַ�
//			System.out.println(answer_context_s.substring(j * 100, j * 100 + 100));
//			System.out.println(answer_context_s.substring(j * 100, answer_context_s.length())); // ��ʾʣ��ģ�����12����Ϊȥ������ġ�Embed
//		}
		return answer_context_s;
	}

	/**
	 * �������ߵķ�˿��Ŀ�������Ǹ��ݱ�������ҳ�������doc������ǽ������������ߵ�follower��Ŀ
	 * @param doc,n
	 */
	public static String Author_Followers(Document doc, int n) {
		Elements author_followers = doc.select("div.grid_page_left_col")
				.select("div.secondary").select("span[class]");
		if(author_followers.size() == 0){
			System.out.println("û�еõ����߷�˿��Ϣ������������ȡ����ҳ��......");
			return null;
		}else{
			String author_followers_s = author_followers.get(0).text();
			System.out.println("��" + (n + 1) + "λ����follower��ĿΪ��"
					+ author_followers_s);
			return author_followers_s;
		}
	}

	/**
	 * �����ش�����Ϣ�����ػش�����ϢString������String�����Ա㽫�����ã���ͬ����Ҳ���Է��ؿ�ֵ��
	 * @param doc,n
	 */
	public static String Author_Info(Document doc, int n) {
		Elements author_name = doc.select("div.author_info").select(
				"span.feed_item_answer_user"); // ��������user //row AnswerListDiv
		Element a = author_name.get(n); // �����ش�
		// �û�����Quora User���������
		Elements c = a.select("a.user");
		String author_name_s = null;
		if (c.size() == 0) {
			author_name_s = "Quora User(�����û�)";
			System.out.println("�����û���Quora User");
		} else {
			author_name_s = c.text();
			System.out.println("��������Ϊ��" + author_name_s);
		}
		// �û�ְҵ����δ��ӵ����
		Elements b = a.select("span.hidden");
		String profession = null;
		if (b.size() == 0) {
			profession = "δ���ְҵ��Ϣ";
			System.out.println("������ְҵ������");
		} else {
			profession = b.text();
			System.out.println("ְҵ��ϢΪ��" + profession);
		}
		String author_info_s = author_name_s + "��" + "\n" + profession;
		System.out.println("��" + (n + 1) + "λ�ش�����ϢΪ��");
		System.out.println(author_info_s);
		return author_info_s;
	}

	/**
	 * ���������ó��ش����Ҫ�������������Ǹ��ݱ�������ҳ�������doc������ǽ������������ߵ�know about���
	 * @param doc,n
	 */
	public static String Author_KnowAbout(Document doc, int n) {
		Elements Author_Answers = doc.select("div.grid_page_right_col");
		if(Author_Answers.size() == 0){
			System.out.println("û�еõ����ߵ�������Ϣ������������ȡ����ҳ��...");
			return null;
		}else{
			Elements a = Author_Answers.select("div[class$=ProfileExperienceList]");
			if (a.size() == 0) {
				System.out.println("���������ó����򣡣���");
				return "null��author has not filled out their profile";
			} else {
				Elements TopicName = a.select("li[class]").select("span.TopicName");
				Elements answer_count = a.select("li[class]").select(
						"div.answers_link");
				// int num = TopicName.size();
				String Author_Answers_s = "Know About��";
				for (int i = 0; i < answer_count.size(); i++) {
					Author_Answers_s = Author_Answers_s + "\n"
							+ TopicName.get(i).text() + " �� "
							+ answer_count.get(i).text();
				}
				System.out.println("��" + (n + 1) + "λ���߻ش��ó���������ҪΪ��" + Author_Answers_s);
				return Author_Answers_s;
			}
		}
	}

	/**
	 * �������߻ش������ṩ�Ĵ���Ŀ�������Ǹ��ݱ�������ҳ�������doc������ǽ������������ߵ�answer��Ŀ
	 * @param doc,n
	 */
	public static String Author_Answers(Document doc, int n) {
		Elements Author_Answers = doc.select("div.grid_page_left_col")
				.select("div.primary").select("span[class]");
		if(Author_Answers.size() == 0){
			System.out.println("û�еõ����ߵĴ�������Ϣ������������ȡ����ҳ��...");
			return null;
		}else{
			String Author_Answers_s = Author_Answers.get(1).text();
			System.out.println("��" + (n + 1) + "λ���߻ش��������ĿΪ��" + Author_Answers_s);
			return Author_Answers_s;
		}
	}

	/**
	 * �������ߵ�ż����Ŀ�������Ǹ��ݱ�������ҳ�������doc������ǽ������������ߵ�following��Ŀ
	 * 
	 * @param doc
	 *            ,n
	 */
	public static String Author_Following(Document doc, int n) {
		Elements author_following = doc.select("div.grid_page_left_col")
				.select("div.secondary").select("span[class]");
		String author_following_s = author_following.get(1).text();
		System.out.println("��" + (n + 1) + "λ����following��ĿΪ��"
				+ author_following_s);
		return author_following_s;
	}

	/**
	 * ������������صĻ���
	 * 
	 * @param doc
	 *            ,n
	 */
	public static String Related_Topics(Document doc) {
		Elements related_topics = doc.select("div.grid_page_left_col")
				.select("div.BreadCrumbList").select("div[id]");
		String related_topics_s = "Related_Topics: ";
		for (int i = 0; i < related_topics.size(); i++) {
			Element a = related_topics.get(i);
			related_topics_s = related_topics_s + "\n" + a.text();
		}
		return related_topics_s;
	}

	/**
	 * ���������ע����
	 * 
	 * @param doc
	 *            ,n
	 */
	public static String Want_Answers(Document doc) {
		Elements want_answers = doc.select("div.header")
				.select("div.primary_item").select("span").select("a[class]");
		if(want_answers.size() == 0){
			want_answers = doc.select("div.reviews_header_wrapper")
					.select("div.primary_item").select("a[class]").select("span");
		}
		Element a = want_answers.get(0);
		Elements b = a.select("span[class]");
		if (b.size() == 0) {
			System.out.println("Want_Answers����Ϊ0������");
			return "null";
		} else {
			String want_answer = b.text();
			System.out.println("Want_Answers����Ϊ��" + want_answer);
			return want_answer;
		}
	}

	/**
	 * �������õ�������ת�浽Excel����
	 * 
	 * @param doc
	 *            ,n
	 * @throws WriteException
	 *             ,RowsExceededException,IOException
	 * 
	 */
	public static void Save2Excel(String keyword, int m, int n)
			throws Exception {
		String path = "f:/test/" + keyword + m + "_" + n + ".html";
		/** */
		/** **********����������************ */
		WritableWorkbook workbook = Workbook.createWorkbook(new File("f:/test/"
				+ keyword + m + "_" + n + ".xls"));
		/** */
		/** **********����������************ */
		WritableSheet sheet = workbook.createSheet("���ܻ�", 0);
		/** */
		/** *********�����п�**************** */
		sheet.setColumnView(0, 20);
		sheet.setColumnView(1, 20);
		sheet.setColumnView(2, 20);
		sheet.setColumnView(3, 20);
		sheet.setColumnView(4, 20);
		sheet.setColumnView(5, 40);
		sheet.setColumnView(6, 80);
		sheet.setColumnView(7, 20);
		sheet.setColumnView(8, 20);
		sheet.setColumnView(9, 40);
		sheet.setColumnView(10, 20);
		sheet.setColumnView(11, 20);
		sheet.setColumnView(12, 20);
		sheet.setColumnView(13, 20);

		// �����и�
		sheet.setRowView(0, 800, false);
		sheet.setRowView(1, 1000, false);
		Document doc0 = parsePathText(path);
		int realanswernumber0 = RealAnswerNumber(doc0);
		for (int i = 0; i < realanswernumber0; i++) {
			sheet.setRowView(i + 2, 2000, false); // �����и�
		}

		/** */
		/** ************���õ�Ԫ������************** */
		// ����
		WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
		WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 12,
				WritableFont.BOLD);

		/** */
		/** ************�������ü��ָ�ʽ�ĵ�Ԫ��************ */
		// ���ڱ���
		WritableCellFormat wcf_title = new WritableCellFormat(BoldFont);
		wcf_title.setBorder(Border.ALL, BorderLineStyle.THIN); // ����
		wcf_title.setVerticalAlignment(VerticalAlignment.CENTRE); // ��ֱ����
		wcf_title.setAlignment(Alignment.CENTRE); // ˮƽ����
		wcf_title.setBackground(Colour.GRAY_25);
		wcf_title.setWrap(true); // �Ƿ���

		// ����������
		WritableCellFormat wcf_center = new WritableCellFormat(NormalFont);
		wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // ����
		wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // ��ֱ����
		wcf_center.setAlignment(Alignment.CENTRE);
		// wcf_center.setBackground(Colour.YELLOW);
		wcf_center.setWrap(true); // �Ƿ���

		/** */
		/** ************��Ԫ���ʽ�������****************** */
		// �ϲ���Ԫ��,ע��mergeCells(col0,row0,col1,row1) --�д�0��ʼ,col1Ϊ��Ҫ�ϲ����ڼ���,��Ҳһ��
		sheet.mergeCells(0, 0, 0, 1); // �ϲ����൥Ԫ��
		sheet.mergeCells(1, 0, 3, 0);
		sheet.mergeCells(4, 0, 6, 0);
		sheet.mergeCells(7, 0, 11, 0);
		sheet.mergeCells(12, 0, 13, 0);
		if (realanswernumber0 > 1) {
			sheet.mergeCells(12, 2, 12, 1 + realanswernumber0); // �ϲ���������ػ��⡱��Ԫ��
			sheet.mergeCells(13, 2, 13, 1 + realanswernumber0);
		}
		sheet.addCell(new Label(0, 0, "Analysis of Answers", wcf_title));
		sheet.addCell(new Label(1, 0, "�������", wcf_title));
		sheet.addCell(new Label(4, 0, "�������", wcf_title));
		sheet.addCell(new Label(7, 0, "�������", wcf_title));
		sheet.addCell(new Label(12, 0, "�������", wcf_title));
		sheet.addCell(new Label(1, 1, "֧��Ʊ����Upvotes��", wcf_title));
		sheet.addCell(new Label(2, 1, "������Ŀ��Comment Numbers��", wcf_title));
		sheet.addCell(new Label(3, 1, "�������ݣ�Comment Content��", wcf_title));
		sheet.addCell(new Label(4, 1, "�ش𳤶ȣ�Content Length��", wcf_title));
		sheet.addCell(new Label(5, 1, "�������ӣ�URLs��", wcf_title));
		sheet.addCell(new Label(6, 1, "�ش����ݣ� AnswerContent��", wcf_title));
		sheet.addCell(new Label(7, 1, "���߷�˿��Author Followers��", wcf_title));
		sheet.addCell(new Label(8, 1, "����ְҵ��Author Profession��", wcf_title));
		sheet.addCell(new Label(9, 1, "��������Know About��", wcf_title));
		sheet.addCell(new Label(10, 1, "�ش�������Author Answers��", wcf_title));
		sheet.addCell(new Label(11, 1, "ż����Ŀ��Following Others��", wcf_title));
		sheet.addCell(new Label(12, 1, "������ػ��⣨Related Topics��", wcf_title));
		sheet.addCell(new Label(13, 1, "�����ע������Question Want Answers��",
				wcf_title));

		// �ж�·���ļ��Ƿ���ڣ����û�еĻ�Excel���������ݣ��������ݼӵ�����
		System.out.println("\n��ʼ������ " + path);
		File file = new File(path);
		if (!file.exists()) {
			System.out.println(path + "  �����ڣ�Excel��������Ϊ��");
		} else {
			Document doc = parsePathText(path); // �õ�������Ķ���
			// table���� ��ҳ������
			// String answernumber = AnswerNumber(doc); //���ش���Ŀ,�ӵ�Excel����
			int realanswernumber = RealAnswerNumber(doc); // ����ʵ�ʵĴ���Ŀ
			// ѭ��������д𰸣�ȡǰ��λ��,д�뵽Excel����
			// Integer.parseInt(answernumber.substring(0, 2))
			for (int i = 0; i < realanswernumber; i++) {
				String serial = "author" + (i + 1);
				sheet.addCell(new Label(0, i + 2, serial, wcf_center));

				// ������أ�֧��Ʊ����Upvotes����������Ŀ��Comment Numbers�����������ݣ�Comment
				// Content��
				String answervotes = Upvotes(doc, i);
				String comment_numbers = Comment_Numbers(doc, i);

				sheet.addCell(new Label(1, i + 2, answervotes, wcf_center));
				sheet.addCell(new Label(2, i + 2, comment_numbers, wcf_center));
				sheet.addCell(new Label(3, i + 2, "null", wcf_center));

				// ������أ��ش𳤶ȣ�Content Length�����������ӣ�URLs�����ش����ݣ� AnswerContent��
				int content_length = Content_Length(doc, i);
				String urls = URLs(doc, i);
				String answercontent = AnswerContent(doc, i);
				sheet.addCell(new Label(4, i + 2, "" + content_length,
						wcf_center));
				sheet.addCell(new Label(5, i + 2, urls, wcf_center));
				sheet.addCell(new Label(6, i + 2, answercontent, wcf_center));

				// ������أ����߷�˿��Author Followers��������ְҵ��Author Profession������������Know
				// About�����ش�������Author Answers����ż����Ŀ��Following Others��
				String author_path = "f:/test/" + keyword + m + "_" + n
						+ "_author_" + i + ".html";
				File author_file = new File(author_path);
				if (!author_file.exists()) {
					System.out.println(author_path + "  �����ڣ�û�и����ߵ���Ϣ������");
				} else {
					Document author_doc = parsePathText(author_path);
					String author_followers = Author_Followers(author_doc, i);
					String author_info = Author_Info(doc, i);
					String author_knowabout = Author_KnowAbout(author_doc, i);
					String author_answers = Author_Answers(author_doc, i);
					String author_following = Author_Following(author_doc, i);
					sheet.addCell(new Label(7, i + 2, author_followers,
							wcf_center));
					sheet.addCell(new Label(8, i + 2, author_info, wcf_center));
					sheet.addCell(new Label(9, i + 2, author_knowabout,
							wcf_center));
					sheet.addCell(new Label(10, i + 2, author_answers,
							wcf_center));
					sheet.addCell(new Label(11, i + 2, author_following,
							wcf_center));
				}
			}
			// ������أ�������ػ��⣨Related Topics���������ע������Question Want Answers��
			String related_topics = Related_Topics(doc);
			String want_answers = Want_Answers(doc);
			sheet.addCell(new Label(12, 2, related_topics, wcf_center));
			sheet.addCell(new Label(13, 2, want_answers, wcf_center));
			System.out.println(path + " �Ѿ��ɹ������� " + "f:/test/" + keyword + m
					+ "_" + n + ".xls ��");
		}
		workbook.write();
		/** */
		/** *********�ر��ļ�************* */
		workbook.close();
		// JOptionPane.showMessageDialog(null, "�ɹ�������ȡ��Excel���С���Ŀ¼Ϊ��f:test");
	}

	/**
	 * ����ָ��·����html�ļ�doc
	 * 
	 * @param path
	 */
	public static Document parsePathText(String path) {
		Document doc = null;
		try {
			File input = new File(path);
			doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com"); // http://www.quora.com/Can-I-get-help-about-Binary-search
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String text = doc.body().text();
		return doc;
	}
}