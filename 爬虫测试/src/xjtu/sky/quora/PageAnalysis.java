package xjtu.sky.quora;

/**
 * zhengyuanhao  2014/11/27
 * 简单实现：quora  
 * 解析本地的网页，将网页所需的内容保存到Excel表中
 * 输入是一个已经下载到本地的quora回答网页，输出是保存解析该网页结果的Excel文件
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
	 * 主函数
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
	 * 解析more标签得到所有回答，（失败了，原因是more的链接还是指向自己这个网页）
	 * 
	 * @param doc
	 */
	public static String MorePage(Document doc) {
		System.out.println("more的链接为：");
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
	 * 解析答案数目,返回答案数目string
	 * 
	 * @param doc
	 */
	public static int AnswerNumber(Document doc) {
		Elements answer_count = doc.select("div.answer_count");
		String answer_count_s = answer_count.get(0).text();
		System.out.println("答案数目为：" + answer_count_s);
		int number = Integer.parseInt(answer_count_s.substring(0, answer_count_s.length()-8));
		System.out.println("答案数目为：" + number);
		return number;
	}

	/**
	 * 得到现在网页上面的实际答案数目
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
		
//		System.out.println("实际答案数目为：" + number);
		
		return number;
	}

	/**
	 * 解析支持票数，返回回答者答案的支持票数string
	 * 
	 * @param doc,n
	 */
	public static String Upvotes(Document doc, int n) {
		Elements answer_voters = doc.select("div.Answer")
				.select("div.primary_item").select("a[class]").select("span");
		String answer_voters_s = answer_voters.get(n).text();
		System.out.println("支持票数为：" + answer_voters_s);
		if (answer_voters_s.equals("0")) {
			System.out.println("支持票数为0，请注意！！！");
		}
		return answer_voters_s;
	}

	/**
	 * 解析答案评论数目
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
			System.out.println("答案" + n + "不存在评论！！！");
			return "0";
		} else {
			Elements b = comment.select("span[class]");
			if (b.size() == 0) {
				System.out.println("答案" + n + "不存在评论！！！");
				return "0";
			} else {
				String comment_number = b.text();
				System.out.println("答案" + n + "的评论数目为：" + comment_number);
				return "1";
			}
		}
	}
	
	/**
	 * 解析问题评论数目
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
			System.out.println("问题不存在评论！！！");
			return "0";
		} else {
			Elements b = comment.select("span[class]");
			if (b.size() == 0) {
				System.out.println("问题不存在评论！！！");
				return "0";
			} else {
				String comment_number = b.text();
				System.out.println("问题评论数目为：" + comment_number);
				return "1";
			}
		}
	}

	/**
	 * 解析回答长度,算上空格,是以一个 单词 为一个词算的
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
			if(content_j.size() == 0){   //如果答案中没有出现回答，就令答案为空
				per = title.first();
			}else{
				per = content_j.first();
			}
		} else {
			per = content_k.first();
		}

		String answer_context_s = null;
		try{
			answer_context_s = per.text(); // 内容可能副本，取其中的第一个即可
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("得到答案内容出错...");
		}
		// 使用split得到文本的单词数目
		String[] answers = answer_context_s.split(" ");
		int content_length = answers.length;
		System.out.println("回答单词长度为：" + content_length);
		return content_length;
	}

	/**
	 * 解析回答长度,算上空格,是以一个 字符 为一个词算的
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
			if(content_j.size() == 0){   //如果答案中没有出现回答，就令答案为空
				per = title.first();
			}else{
				per = content_j.first();
			}
		} else {
			per = content_k.first();
		}

		String answer_context_s = null;
		try{
			answer_context_s = per.text(); // 内容可能副本，取其中的第一个即可
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("得到答案内容出错...");
		}
		// 使用split得到文本的单词数目
		// String[] answers = answer_context_s.split(" ");
		int content_length = answer_context_s.length();
		System.out.println("回答字符长度为：" + content_length);
		return content_length;
	}

	/**
	 * 解析内容得到链接
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
			if(content_j.size() == 0){            //如果答案中没有出现回答，就令答案为题目标题
				per = title.first();
			}else{
				per = content_j.first();
			}
		} else {
			per = content_k.first();
		}
		
		Elements a = per.select("a.external_link"); // 得到内容里面包含的链接
		if (a.size() == 0) {
			return "0";
		} else {
			String urls = "1" + "\n" + "链接是："; // 用于保存链接
			for (int i = 0; i < a.size(); i++) {
				Element b = a.get(i);
				urls = urls + "\n" + b.attr("href");
			}
			String exist = "1";
			return exist;
		}
	}

	/**
	 * 解析答案内容，返回回答者的答案内容 string
	 * @param doc, n
	 */
	public static String AnswerContent(Document doc, int n) {
//		System.out.println("\n回答内容为：");
		Elements contents = doc.select("div.Answer");
		Element content_i = contents.get(2 * n);
		String content = content_i.attr("id");
		Elements title = doc.select("div.header").select("div.question_text_edit");
		Elements content_j = doc.select("div.Answer").select("div#" + content + "_content");
		Elements content_k = content_j.select("div[class^=Expandable]").select("span.inline_editor_value").select("div[id$=outer]").select("div[id$=container]");
		Element per;
		if (content_k.size() == 0) {
			if(content_j.size() == 0){            //如果答案中没有出现回答，就令答案为题目标题
				per = title.first();
			}else{
				per = content_j.first();
			}
		} else {
			per = content_k.first();
		}

		String answer_context_s = null;
		try{
			answer_context_s = per.text(); // 内容可能副本，取其中的第一个即可
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("得到答案内容出错...");
		}
//		System.out.println(answer_context_s);
		
//		for (int j = 0; j < answer_context_s.length() / 100; j++) { // 每行显示100字符
//			System.out.println(answer_context_s.substring(j * 100, j * 100 + 100));
//			System.out.println(answer_context_s.substring(j * 100, answer_context_s.length())); // 显示剩余的，减掉12是因为去除多余的“Embed
//		}
		return answer_context_s;
	}

	/**
	 * 解析作者的粉丝数目，输入是根据本地作者页面解析的doc，输出是解析出来的作者的follower数目
	 * @param doc,n
	 */
	public static String Author_Followers(Document doc, int n) {
		Elements author_followers = doc.select("div.grid_page_left_col")
				.select("div.secondary").select("span[class]");
		if(author_followers.size() == 0){
			System.out.println("没有得到作者粉丝信息，建议重新爬取作者页面......");
			return null;
		}else{
			String author_followers_s = author_followers.get(0).text();
			System.out.println("第" + (n + 1) + "位作者follower数目为："
					+ author_followers_s);
			return author_followers_s;
		}
	}

	/**
	 * 解析回答者信息，返回回答者信息String，返回String类型以便将来调用，下同。（也可以返回空值）
	 * @param doc,n
	 */
	public static String Author_Info(Document doc, int n) {
		Elements author_name = doc.select("div.author_info").select(
				"span.feed_item_answer_user"); // 读者姓名user //row AnswerListDiv
		Element a = author_name.get(n); // 单个回答
		// 用户存在Quora User匿名的情况
		Elements c = a.select("a.user");
		String author_name_s = null;
		if (c.size() == 0) {
			author_name_s = "Quora User(匿名用户)";
			System.out.println("匿名用户：Quora User");
		} else {
			author_name_s = c.text();
			System.out.println("作者姓名为：" + author_name_s);
		}
		// 用户职业存在未添加的情况
		Elements b = a.select("span.hidden");
		String profession = null;
		if (b.size() == 0) {
			profession = "未添加职业信息";
			System.out.println("不存在职业！！！");
		} else {
			profession = b.text();
			System.out.println("职业信息为：" + profession);
		}
		String author_info_s = author_name_s + "：" + "\n" + profession;
		System.out.println("第" + (n + 1) + "位回答者信息为：");
		System.out.println(author_info_s);
		return author_info_s;
	}

	/**
	 * 解析作者擅长回答的主要问题领域，输入是根据本地作者页面解析的doc，输出是解析出来的作者的know about情况
	 * @param doc,n
	 */
	public static String Author_KnowAbout(Document doc, int n) {
		Elements Author_Answers = doc.select("div.grid_page_right_col");
		if(Author_Answers.size() == 0){
			System.out.println("没有得到作者的领域信息，建议重新爬取作者页面...");
			return null;
		}else{
			Elements a = Author_Answers.select("div[class$=ProfileExperienceList]");
			if (a.size() == 0) {
				System.out.println("该作者无擅长领域！！！");
				return "null，author has not filled out their profile";
			} else {
				Elements TopicName = a.select("li[class]").select("span.TopicName");
				Elements answer_count = a.select("li[class]").select(
						"div.answers_link");
				// int num = TopicName.size();
				String Author_Answers_s = "Know About：";
				for (int i = 0; i < answer_count.size(); i++) {
					Author_Answers_s = Author_Answers_s + "\n"
							+ TopicName.get(i).text() + " ： "
							+ answer_count.get(i).text();
				}
				System.out.println("第" + (n + 1) + "位作者回答擅长的领域主要为：" + Author_Answers_s);
				return Author_Answers_s;
			}
		}
	}

	/**
	 * 解析作者回答问题提供的答案数目，输入是根据本地作者页面解析的doc，输出是解析出来的作者的answer数目
	 * @param doc,n
	 */
	public static String Author_Answers(Document doc, int n) {
		Elements Author_Answers = doc.select("div.grid_page_left_col")
				.select("div.primary").select("span[class]");
		if(Author_Answers.size() == 0){
			System.out.println("没有得到作者的答案总数信息，建议重新爬取作者页面...");
			return null;
		}else{
			String Author_Answers_s = Author_Answers.get(1).text();
			System.out.println("第" + (n + 1) + "位作者回答问题的数目为：" + Author_Answers_s);
			return Author_Answers_s;
		}
	}

	/**
	 * 解析作者的偶像数目，输入是根据本地作者页面解析的doc，输出是解析出来的作者的following数目
	 * 
	 * @param doc
	 *            ,n
	 */
	public static String Author_Following(Document doc, int n) {
		Elements author_following = doc.select("div.grid_page_left_col")
				.select("div.secondary").select("span[class]");
		String author_following_s = author_following.get(1).text();
		System.out.println("第" + (n + 1) + "位作者following数目为："
				+ author_following_s);
		return author_following_s;
	}

	/**
	 * 解析与问题相关的话题
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
	 * 解析问题关注人数
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
			System.out.println("Want_Answers人数为0！！！");
			return "null";
		} else {
			String want_answer = b.text();
			System.out.println("Want_Answers人数为：" + want_answer);
			return want_answer;
		}
	}

	/**
	 * 将分析得到的内容转存到Excel表中
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
		/** **********创建工作簿************ */
		WritableWorkbook workbook = Workbook.createWorkbook(new File("f:/test/"
				+ keyword + m + "_" + n + ".xls"));
		/** */
		/** **********创建工作表************ */
		WritableSheet sheet = workbook.createSheet("答案总汇", 0);
		/** */
		/** *********设置列宽**************** */
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

		// 设置行高
		sheet.setRowView(0, 800, false);
		sheet.setRowView(1, 1000, false);
		Document doc0 = parsePathText(path);
		int realanswernumber0 = RealAnswerNumber(doc0);
		for (int i = 0; i < realanswernumber0; i++) {
			sheet.setRowView(i + 2, 2000, false); // 设置行高
		}

		/** */
		/** ************设置单元格字体************** */
		// 字体
		WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
		WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 12,
				WritableFont.BOLD);

		/** */
		/** ************以下设置几种格式的单元格************ */
		// 用于标题
		WritableCellFormat wcf_title = new WritableCellFormat(BoldFont);
		wcf_title.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
		wcf_title.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
		wcf_title.setAlignment(Alignment.CENTRE); // 水平对齐
		wcf_title.setBackground(Colour.GRAY_25);
		wcf_title.setWrap(true); // 是否换行

		// 用于正文左
		WritableCellFormat wcf_center = new WritableCellFormat(NormalFont);
		wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
		wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
		wcf_center.setAlignment(Alignment.CENTRE);
		// wcf_center.setBackground(Colour.YELLOW);
		wcf_center.setWrap(true); // 是否换行

		/** */
		/** ************单元格格式设置完成****************** */
		// 合并单元格,注意mergeCells(col0,row0,col1,row1) --列从0开始,col1为你要合并到第几列,行也一样
		sheet.mergeCells(0, 0, 0, 1); // 合并分类单元格
		sheet.mergeCells(1, 0, 3, 0);
		sheet.mergeCells(4, 0, 6, 0);
		sheet.mergeCells(7, 0, 11, 0);
		sheet.mergeCells(12, 0, 13, 0);
		if (realanswernumber0 > 1) {
			sheet.mergeCells(12, 2, 12, 1 + realanswernumber0); // 合并“问题相关话题”单元格
			sheet.mergeCells(13, 2, 13, 1 + realanswernumber0);
		}
		sheet.addCell(new Label(0, 0, "Analysis of Answers", wcf_title));
		sheet.addCell(new Label(1, 0, "反馈相关", wcf_title));
		sheet.addCell(new Label(4, 0, "内容相关", wcf_title));
		sheet.addCell(new Label(7, 0, "作者相关", wcf_title));
		sheet.addCell(new Label(12, 0, "问题相关", wcf_title));
		sheet.addCell(new Label(1, 1, "支持票数（Upvotes）", wcf_title));
		sheet.addCell(new Label(2, 1, "评论数目（Comment Numbers）", wcf_title));
		sheet.addCell(new Label(3, 1, "评论内容（Comment Content）", wcf_title));
		sheet.addCell(new Label(4, 1, "回答长度（Content Length）", wcf_title));
		sheet.addCell(new Label(5, 1, "包含链接（URLs）", wcf_title));
		sheet.addCell(new Label(6, 1, "回答内容（ AnswerContent）", wcf_title));
		sheet.addCell(new Label(7, 1, "作者粉丝（Author Followers）", wcf_title));
		sheet.addCell(new Label(8, 1, "作者职业（Author Profession）", wcf_title));
		sheet.addCell(new Label(9, 1, "作者领域（Know About）", wcf_title));
		sheet.addCell(new Label(10, 1, "回答总数（Author Answers）", wcf_title));
		sheet.addCell(new Label(11, 1, "偶像数目（Following Others）", wcf_title));
		sheet.addCell(new Label(12, 1, "问题相关话题（Related Topics）", wcf_title));
		sheet.addCell(new Label(13, 1, "问题关注人数（Question Want Answers）",
				wcf_title));

		// 判断路径文件是否存在：如果没有的话Excel表中无数据，否则将数据加到其中
		System.out.println("\n开始解析： " + path);
		File file = new File(path);
		if (!file.exists()) {
			System.out.println(path + "  不存在，Excel表中内容为空");
		} else {
			Document doc = parsePathText(path); // 得到解析后的对象
			// table内容 网页的内容
			// String answernumber = AnswerNumber(doc); //返回答案数目,加到Excel表中
			int realanswernumber = RealAnswerNumber(doc); // 返回实际的答案数目
			// 循环输出所有答案（取前两位）,写入到Excel表中
			// Integer.parseInt(answernumber.substring(0, 2))
			for (int i = 0; i < realanswernumber; i++) {
				String serial = "author" + (i + 1);
				sheet.addCell(new Label(0, i + 2, serial, wcf_center));

				// 反馈相关：支持票数（Upvotes），评论数目（Comment Numbers），评论内容（Comment
				// Content）
				String answervotes = Upvotes(doc, i);
				String comment_numbers = Comment_Numbers(doc, i);

				sheet.addCell(new Label(1, i + 2, answervotes, wcf_center));
				sheet.addCell(new Label(2, i + 2, comment_numbers, wcf_center));
				sheet.addCell(new Label(3, i + 2, "null", wcf_center));

				// 内容相关：回答长度（Content Length），包含链接（URLs），回答内容（ AnswerContent）
				int content_length = Content_Length(doc, i);
				String urls = URLs(doc, i);
				String answercontent = AnswerContent(doc, i);
				sheet.addCell(new Label(4, i + 2, "" + content_length,
						wcf_center));
				sheet.addCell(new Label(5, i + 2, urls, wcf_center));
				sheet.addCell(new Label(6, i + 2, answercontent, wcf_center));

				// 作者相关：作者粉丝（Author Followers），作者职业（Author Profession），作者领域（Know
				// About），回答总数（Author Answers），偶像数目（Following Others）
				String author_path = "f:/test/" + keyword + m + "_" + n
						+ "_author_" + i + ".html";
				File author_file = new File(author_path);
				if (!author_file.exists()) {
					System.out.println(author_path + "  不存在，没有该作者的信息！！！");
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
			// 话题相关：问题相关话题（Related Topics），问题关注人数（Question Want Answers）
			String related_topics = Related_Topics(doc);
			String want_answers = Want_Answers(doc);
			sheet.addCell(new Label(12, 2, related_topics, wcf_center));
			sheet.addCell(new Label(13, 2, want_answers, wcf_center));
			System.out.println(path + " 已经成功解析到 " + "f:/test/" + keyword + m
					+ "_" + n + ".xls 中");
		}
		workbook.write();
		/** */
		/** *********关闭文件************* */
		workbook.close();
		// JOptionPane.showMessageDialog(null, "成功将答案爬取到Excel表中——目录为：f:test");
	}

	/**
	 * 解析指定路径的html文件doc
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