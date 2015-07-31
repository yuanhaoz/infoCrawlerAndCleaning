package xjtu.sky.quoraStudy;

/**
 * zhengyuanhao  2015/6/30
 * ��ʵ�֣�quora  
 * ʵ�ֹ��ܣ�1.�������صġ�������ҳ���͡�������ҳ����
 *          2.���������� ���� �� ����ָ��·����html�ļ��õ���doc����
 *          3.�ֱ�ʵ���˶�����ҳ���������Ϣ�ʹ���Ϣ�ĳ�ȡ��
 *            ������ҳ��ĸ�����Ϣ����˿������Ϣ�ĳ�ȡ��
 * 
 */

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class PageAnalysis {
	
	/**
	 * ������
	 */
	public static void main(String[] args) throws Exception {

	}

	/**
	 * ʵ�ֹ��ܣ���������Ŀ�����ش���ĿInt
	 * ��������������ҳ
	 * ������ǩ��ʽ��2 Answers
	 * @param doc
	 */
	public static int CountAnswerNumber(Document doc) {
		Elements answer_count = doc.select("div.answer_count");
		String answer_count_s = answer_count.get(0).text();
//		int number = Integer.parseInt(answer_count_s.substring(0, answer_count_s.length()-8));
//		System.out.println("����ĿΪ��" + number);
		String[] a = answer_count_s.split(" ");
		int answernumber = Integer.parseInt(a[0]);
		System.out.println("����ĿΪ��" + answernumber);
		return answernumber;
	}

	/**
	 * ʵ�ֹ��ܣ��õ�������ҳʵ�ʴ���Ŀ�����ش���ĿInt
	 * ��������������ҳ
	 * ������ǩ��ʽ��div.pagedlist_item
	 * @param doc
	 */
	public static int CountRealAnswerNumber(Document doc) {
//		int real_answer_count_s = 0;
		Elements real_answer_count = doc.select("div.pagedlist_item");
		int number = real_answer_count.size() - 4;
		boolean more = doc.select("div.EmptyAnswerSuggestions").isEmpty();
		if (!more) {
			number = 0;
		}
//		���httpclient��������õ�����ҳ�����ڽ�������ҳ�������ʹ��selenium����ҳ
//		if (real_answer_count.size() == 2 || real_answer_count.size() == 1) {
//			real_answer_count_s = real_answer_count.size() - 1;
//		} else {
//			real_answer_count_s = real_answer_count.size() - 2;
//		}
		System.out.println("ʵ�ʴ���ĿΪ��" + number);
		return number;
	}

	/**
	 * ʵ�ֹ��ܣ����������ע���������ع�ע���� string
	 * ��������������ҳ
	 * ������ǩ��ʽ��
	 * @param doc
	 */
	public static String QuestionWantAnswers(Document doc) {
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
	 * ʵ�ֹ��ܣ������������ݣ������������� string
	 * ��������������ҳ
	 * ������ǩ��ʽ��
	 * @param doc
	 */
	public static String QuestionContent(Document doc) {
		Element a = doc.select("div.grid_page_center_col").get(0);
		Elements name = a.select("div.header").select("div.question_text_edit").select("h1");
		if (name.size() == 0) {
			return "��������ҳ����������...";
		} else {
			String name_s = name.get(0).text();
			System.out.println("��������Ϊ��" + name_s);
			return name_s;
		}
	}
	
	/**
	 * ʵ�ֹ��ܣ��������⸽����Ϣ���������⸽����Ϣ string
	 * ��������������ҳ
	 * ������ǩ��ʽ��
	 * @param doc
	 */
	public static String QuestionExpandInfo(Document doc) {
		Element a = doc.select("div.grid_page_center_col").get(0);
		Elements expandinfo = a.select("div.header").select("div.question_details").select("div.expanded_q_text");
		if (expandinfo.size() == 0) {
			return "��������ҳ����������ĸ�����Ϣ...";
		} else {
			String expandinfo_s = expandinfo.get(0).text();
			System.out.println("���⸽����ϢΪ��" + expandinfo_s);
			return expandinfo_s;
		}
	}

	/**
	 * ʵ�ֹ��ܣ��������ⵥ�ʳ��ȣ������ո�,һ��"����"Ϊһ���ʣ������ش𰸳���Int
	 * ��������������ҳ
	 * ������ǩ��ʽ��
	 * @param doc
	 */
	public static int QuestionContentWordLength(Document doc) {
		Element a = doc.select("div.grid_page_center_col").get(0);
		Elements name = a.select("div.header").select("div.question_text_edit").select("h1");
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
	 * ʵ�ֹ��ܣ����������ַ����ȣ������ո�,һ��"�ַ�"Ϊһ���ʣ������ش𰸳���Int
	 * ��������������ҳ
	 * ������ǩ��ʽ��
	 * @param doc
	 */
	public static int QuestionContentCharLength(Document doc) {
		Element a = doc.select("div.grid_page_center_col").get(0);
		Elements name = a.select("div.header").select("div.question_text_edit").select("h1");
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
	 * ʵ�ֹ��ܣ����������Ƿ����ۣ�����Boolen����0��1
	 * ��������������ҳ
	 * ������ǩ��ʽ��
	 * @param doc
	 */
	public static String QuestionCommentNumbers(Document doc) {
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
	 * ʵ�ֹ��ܣ�������������صĻ��⣬������ػ��� string
	 * ��������������ҳ
	 * ������ǩ��ʽ��
	 * @param doc,n
	 */
	public static String QuestionRelatedTopics(Document doc) {
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
	 * ʵ�ֹ��ܣ�����֧��Ʊ�������ػش��ߴ𰸵�֧��Ʊ��Int
	 * ��������������ҳ����n���ش�
	 * ������ǩ��ʽ��
	 * @param doc,n
	 */
	public static int AnswerUpvotes(Document doc, int n) {
		Elements answer_voters = doc.select("div.Answer")
				.select("div.primary_item").select("a[class]").select("span");
		String answer_voters_s = answer_voters.get(n).text();
		System.out.println("֧��Ʊ��Ϊ��" + answer_voters_s);
		if (answer_voters_s.equals("0")) {
			System.out.println("֧��Ʊ��Ϊ0����ע�⣡����");
		}
		return Integer.parseInt(answer_voters_s);
	}

	/**
	 * ʵ�ֹ��ܣ��������Ƿ����ۣ�����Boolen����0��1
	 * ��������������ҳ����n���ش�
	 * ������ǩ��ʽ��
	 * @param doc,n
	 */
	public static String AnswerCommentNumbers(Document doc, int n) {
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
	 * ʵ�ֹ��ܣ������𰸳��ȣ������ո�,һ��"����"Ϊһ���ʣ������ش𰸳���Int
	 * ��������������ҳ����n���ش�
	 * ������ǩ��ʽ��
	 * @param doc,n
	 */
	public static int AnswerContentWordLength(Document doc, int n) {
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
	 * ʵ�ֹ��ܣ������𰸳��ȣ������ո�,һ��"�ַ�"Ϊһ���ʣ������ش𰸳���Int
	 * ��������������ҳ����n���ش�
	 * ������ǩ��ʽ��
	 * @param doc,n
	 */
	public static int AnswerContentCharLength(Document doc, int n) {
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
	 * ʵ�ֹ��ܣ��������ݵõ����ӣ��������ӵ�ַString
	 * ��������������ҳ����n���ش�
	 * ������ǩ��ʽ��
	 * @param doc,n
	 */
	public static String AnswerURLs(Document doc, int n) {
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
	 * ʵ�ֹ��ܣ����������ݣ����ػش��ߵĴ����� string
	 * ��������������ҳ����n���ش�
	 * ������ǩ��ʽ��
	 * @param doc,n
	 */
	public static String AnswerContent(Document doc, int n) {
		System.out.println("\n�ش�����Ϊ��");
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
		System.out.println(answer_context_s);
//		for (int j = 0; j < answer_context_s.length() / 100; j++) { // ÿ����ʾ100�ַ�
//			System.out.println(answer_context_s.substring(j * 100, j * 100 + 100));
//			System.out.println(answer_context_s.substring(j * 100, answer_context_s.length())); // ��ʾʣ��ģ�����12����Ϊȥ������ġ�Embed
//		}
		return answer_context_s;
	}

	/**
	 * ʵ�ֹ��ܣ��������ߵĸ�����Ϣ���������ߵĸ�����Ϣ string
	 * ��������������ҳ����n���ش�����߸�����Ϣ��
	 * ������ǩ��ʽ��
	 * @param doc,n
	 */
	public static String AuthorInfo(Document doc, int n) {
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
	 * ʵ�ֹ��ܣ��������ߵķ�˿��Ŀ���������ߵķ�˿�� string
	 * ��������������ҳ����n���ش������ҳ�棩��n��ȥ��
	 * ������ǩ��ʽ��
	 * @param doc,n
	 */
	public static String AuthorFollowers(Document doc, int n) {
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
	 * ʵ�ֹ��ܣ����������ó��ش����Ҫ�������򣬷����������� string
	 * ��������������ҳ����n���ش������ҳ�棩��n��ȥ��
	 * ������ǩ��ʽ��
	 * @param doc,n
	 */
	public static String AuthorKnowAbout(Document doc, int n) {
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
	 * ʵ�ֹ��ܣ��������߻ش������ṩ�Ĵ���Ŀ�������������� string
	 * ��������������ҳ����n���ش������ҳ�棩��n��ȥ��
	 * ������ǩ��ʽ��
	 * @param doc,n
	 */
	public static String AuthorAnswers(Document doc, int n) {
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
	 * ʵ�ֹ��ܣ���������ż����Ŀ����������ż����Ŀ string
	 * ��������������ҳ����n���ش������ҳ�棩��n��ȥ��
	 * ������ǩ��ʽ��
	 * @param doc,n
	 */
	public static String AuthorFollowing(Document doc, int n) {
		Elements author_following = doc.select("div.grid_page_left_col")
				.select("div.secondary").select("span[class]");
		String author_following_s = author_following.get(1).text();
		System.out.println("��" + (n + 1) + "λ����following��ĿΪ��"
				+ author_following_s);
		return author_following_s;
	}

}