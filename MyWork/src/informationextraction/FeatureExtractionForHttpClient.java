package informationextraction;

/**
 * 针对HttpClient爬取的问题页面
 * zhengyuanhao  2015/6/30
 * 简单实现：quora  
 * 实现功能：1.解析本地的“问题网页”和“作者网页”；
 *          2.解析函数的 对象 是 解析指定路径的html文件得到的doc对象；
 *          3.分别实现了对问题页面的问题信息和答案信息的抽取，
 *            对作者页面的个人信息，粉丝数等信息的抽取。
 * "特征抽取"			
 *      
 * 测试结果如果不错可以将其添加到基本包里面作为基本包或者功能使用
 * 
 */

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class FeatureExtractionForHttpClient {

	/**
	 * 用于得到爬取数据的时间
	 */
	public static String getCrawlerTime(String filePath) throws Exception {
		//得到文件的上次修改时间
		File file = new File(filePath);
		long time = file.lastModified();
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//设置日期格式 
		String crawlerTime = df.format(new Date(time));   // new Date()为获取当前系统时间
//		System.out.println("CrawlerTime1 is ：" + crawlerTime);
		return crawlerTime;
	}
	
//	/**
//	 * 实现功能：解析答案数目，返回答案数目Int
//	 * 解析对象：问题网页
//	 * 解析标签形式：2 Answers
//	 * @param doc
//	 */
//	public static int countAnswerNumber(Document doc) {
//		Elements answer_count = doc.select("div.answer_count");
//		String answer_count_s = answer_count.get(0).text();
////		int number = Integer.parseInt(answer_count_s.substring(0, answer_count_s.length()-8));
////		System.out.println("答案数目为：" + number);
//		String[] a = answer_count_s.split(" ");
//		int answerNumber = Integer.parseInt(a[0]);
//		System.out.println("答案数目为：" + answerNumber);
//		return answerNumber;
//	}

	/**
	 * 实现功能：得到问题网页实际答案数目，返回答案数目Int
	 * 解析对象：问题网页
	 * 解析标签形式：div.pagedlist_item
	 * @param doc
	 */
	public static int countRealAnswerNumber(Document doc) {
		int number = 0;
		Elements real_answer_count;
		
//		针对httpclient方法爬虫得到的网页，现在解析的网页都是针对使用selenium的网页		
//		real_answer_count = doc.select("div.pagedlist_item");
//		number = real_answer_count.size() - 4;
//		System.out.println("真实答案数目：" + number);
//		boolean more = doc.select("div.EmptyAnswerSuggestions").isEmpty();
//		if (!more) {
//			number = 0;
//		}
		
//		针对httpclient方法爬虫得到的网页，现在解析的网页都是针对使用selenium的网页
//		real_answer_count = doc.select("div.pagedlist_item");
//		if (real_answer_count.size() == 2 || real_answer_count.size() == 1) {
//			number = real_answer_count.size() - 1;
//		} else {
//			number = real_answer_count.size() - 2;
//		}
//		System.out.println("实际答案数目为：" + number);
		
//		针对httpclient方法爬虫得到的网页（最新的网页）
//		real_answer_count = doc.select("div.pagedlist_item");
//		
//		if (real_answer_count.size() != 0) {
//			Elements answer_count = real_answer_count.select("div.Answer AnswerBase");
//			if(answer_count.size() !=0) {
//				System.out.println("存在pagedlist_item，且有回答");
//				number = answer_count.size();
//			} else {				
//				System.out.println("存在pagedlist_item，但是没有回答");
//				number = 0;
//			}
//			number = real_answer_count.size() - 1;
//		} else {
//			System.out.println("不存在pagedlist_item，所以更没有回答");
//			number = 0;
//		}
//		System.out.println("实际答案数目为：" + number);
		
//		通用方法
		real_answer_count = doc.select("span.feed_item_answer_user");
		number = real_answer_count.size();
//		System.out.println("真实答案数目：" + number);
		
		return number;
	}

//	/**
//	 * 实现功能：解析问题关注人数，返回关注人数 string
//	 * 解析对象：问题网页
//	 * 解析标签形式：
//	 * @param doc
//	 */
//	public static String questionWantAnswers(Document doc) {
//		Elements want_answers = doc.select("div.header")
//				.select("div.primary_item").select("span").select("a[class]");
//		if(want_answers.size() == 0){
//			want_answers = doc.select("div.reviews_header_wrapper")
//					.select("div.primary_item").select("a[class]").select("span");
//		}
//		Element a = want_answers.get(0);
//		Elements b = a.select("span[class]");
//		if (b.size() == 0) {
//			System.out.println("Want_Answers人数为0！！！");
//			return "null";
//		} else {
//			String want_answer = b.text();
//			System.out.println("Want_Answers人数为：" + want_answer);
//			return want_answer;
//		}
//	}
	
	/**
	 * 实现功能：解析问题内容，返回问题内容 string
	 * 解析对象：问题网页
	 * 解析标签形式：
	 * @param doc
	 */
	public static String questionContent(Document doc) {
		Elements as = doc.select("div.grid_page");
		if(as.size() != 0){
			Element a = as.get(0);
			Elements name = a.select("div.header").select("div.question_text_edit").select("h1");
			if (name.size() == 0) {
				return "";
			} else {
				String name_s = name.get(0).text();
//				System.out.println("问题内容为：" + name_s);
				return name_s;
			}
		} else {
			return "";
		}
		
	}
	
	/**
	 * 实现功能：解析问题附加信息，返回问题附加信息 string
	 * 解析对象：问题网页
	 * 解析标签形式：
	 * @param doc
	 */
	public static String questionExpandInfo(Document doc) {
		Elements as = doc.select("div.grid_page");
		if(as.size() != 0){
			Element a = as.get(0);
			Elements expandInfo = a.select("div.header").select("div.question_details");
			if (expandInfo.size() == 0) {
				return "";
			} else {
				String expandInfo_s = expandInfo.get(0).text();
//				System.out.println("问题附加信息为：" + expandInfo_s);
				return expandInfo_s;
			}
		} else {
			return "";
		}
		
	}

	/**
	 * 实现功能：解析问题单词长度（包括空格,一个"单词"为一个词），返回答案长度Int
	 * 解析对象：问题网页
	 * 解析标签形式：
	 * @param doc
	 */
	public static int questionContentWordLength(Document doc) {
		String content = FeatureExtractionForHttpClient.questionContent(doc);
		// 使用split得到文本的单词数目
		String[] answers = content.split(" ");
		int length = answers.length;
//		System.out.println("问题单词长度为：" + length);
		return length;
	}

/**
	 * 实现功能：解析答案内容，返回回答者的答案内容 string
	 * 解析对象：问题网页（第n条回答）
	 * 解析标签形式：
	 * @param doc,n
	 */
	public static String answerContent(Document doc, int n) {
		String answer_context_s = null;
//		System.out.print("第" + (n+1) + "条回答内容为：");
		Elements contents = doc.select("div.pagedlist_item");
		Elements content_i = contents.select("div[class^=Answer Answer]");
		if(n < content_i.size()){
			String content = content_i.get(n).attr("id");
			Elements title = doc.select("div.header").select("div.question_text_edit");
			Elements content_j = doc.select("div[class^=Answer Answer]")
					.select("div#" + content + "_content");
			Elements content_k = content_j.select("div[class^=ExpandedQText]")
					.select("span.rendered_qtext");
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
		
			try{
				answer_context_s = per.text(); // 内容可能副本，取其中的第一个即可
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("得到答案内容出错...");
			}
			// 输出答案内容
//			System.out.println(answer_context_s);
		
			return answer_context_s;
		} else {
			return "";
		}
		
	}

//	/**
//	 * 实现功能：解析问题字符长度（包括空格,一个"字符"为一个词），返回答案长度Int
//	 * 解析对象：问题网页
//	 * 解析标签形式：
//	 * @param doc
//	 */
//	public static int questionContentCharLength(Document doc) {
//		Element a = doc.select("div.grid_page").get(0);
//		Elements name = a.select("div.header").select("div.question_text_edit").select("h1");
//		if (name.size() == 0) {
//			return 0;
//		} else {
//			String name_s = name.get(0).text();
//			int length = name_s.length();
//			System.out.println("问题字符长度为：" + length);
//			return length;
//		}
//	}

//	/**
//	 * 实现功能：解析问题是否评论，返回Boolen变量0或1
//	 * 解析对象：问题网页
//	 * 解析标签形式：
//	 * @param doc
//	 */
//	public static String questionCommentNumbers(Document doc) {
//		Elements questions = doc.select("div.header").select("div[class^=ActionBar]");
//		if(questions.size() == 0){
//			questions = doc.select("div.reviews_header_wrapper").select("div[class^=ActionBar]");
//		}
//		Element question = questions.get(0);
//		Elements comment = question.select("div.action_item")
//				.select("span[action_click]").select("span[id]")
//				.select("a[class]");
//		if (comment.size() == 0) {
//			System.out.println("问题不存在评论！！！");
//			return "0";
//		} else {
//			Elements b = comment.select("span[class]");
//			if (b.size() == 0) {
//				System.out.println("问题不存在评论！！！");
//				return "0";
//			} else {
//				String comment_number = b.text();
//				System.out.println("问题评论数目为：" + comment_number);
//				return "1";
//			}
//		}
//	}

//	/**
//	 * 实现功能：解析与问题相关的话题，返回相关话题 string
//	 * 解析对象：问题网页
//	 * 解析标签形式：
//	 * @param doc,n
//	 */
//	public static String questionRelatedTopics(Document doc) {
//		Elements related_topics = doc.select("div.grid_page_left_col")
//				.select("div.BreadCrumbList").select("div[id]");
//		String related_topics_s = "Related_Topics: ";
//		for (int i = 0; i < related_topics.size(); i++) {
//			Element a = related_topics.get(i);
//			related_topics_s = related_topics_s + "\n" + a.text();
//		}
//		return related_topics_s;
//	}

//	/**
//	 * 实现功能：解析支持票数，返回回答者答案的支持票数Int
//	 * 解析对象：问题网页（第n条回答）
//	 * 解析标签形式：
//	 * @param doc,n
//	 */
//	public static String answerUpvotes(Document doc, int n) {
//		Elements answer_voters = doc.select("div.Answer")
//				.select("div.primary_item").select("a[class]").select("span");
//		String answer_voters_s = answer_voters.get(n).text();
//		System.out.println("支持票数为：" + answer_voters_s);
//		if (answer_voters_s.equals("0")) {
//			System.out.println("支持票数为0，请注意！！！");
//		}
//		return answer_voters_s;
//	}

//	/**
//	 * 实现功能：解析答案是否评论，返回Boolen变量0或1
//	 * 解析对象：问题网页（第n条回答）
//	 * 解析标签形式：
//	 * @param doc,n
//	 */
//	public static String answerCommentNumbers(Document doc, int n) {
//		Elements comment_numbers = doc.select("div.pagedlist_item")
//				.select("div[class$= ActionBar]");
//		System.out.println("评论总数目：" + comment_numbers.size());
//		if(n < comment_numbers.size()){
//			Element per_answer = comment_numbers.get(n);
//			Elements comment = per_answer.select("div.action_item")
//					.select("span[action_click]").select("span[id]")
//					.select("a[class]");
//			if (comment.size() == 0) {
//				System.out.println("答案" + n + "不存在评论！！！");
//				return "0";
//			} else {
//				Elements b = comment.select("span[class]");
//				if (b.size() == 0) {
//					System.out.println("答案" + n + "不存在评论！！！");
//					return "0";
//				} else {
//					String comment_number = b.text();
//					System.out.println("答案" + n + "的评论数目为：" + comment_number);
//					return "1";
//				}
//			}
//		} else {
//			return "0";
//		}
//		
//	}
	
	/**
	 * 实现功能：解析答案长度（包括空格,一个"单词"为一个词），返回答案长度Int
	 * 解析对象：问题网页（第n条回答）
	 * 解析标签形式：
	 * @param doc,n
	 */
	public static int answerContentWordLength(Document doc, int n) {
		String content = FeatureExtractionForHttpClient.answerContent(doc, n);
		// 使用split得到文本的单词数目
		String[] answers = content.split(" ");
		int content_length = answers.length;
//		System.out.println("第" + (n+1) + "条回答单词长度为：" + content_length);
		return content_length;
	}

//	/**
//	 * 实现功能：解析答案长度（包括空格,一个"字符"为一个词），返回答案长度Int
//	 * 解析对象：问题网页（第n条回答）
//	 * 解析标签形式：
//	 * @param doc,n
//	 */
//	public static int answerContentCharLength(Document doc, int n) {
//		Elements contents = doc.select("div.Answer");
//		Element content_i = contents.get(2 * n);
//		String content = content_i.attr("id");
//		Elements title = doc.select("div.header").select("div.question_text_edit");
//		Elements content_j = doc.select("div.Answer").select("div#" + content + "_content");
//		Elements content_k = content_j.select("div[class^=Expandable]").select("span.inline_editor_value").select("div[id$=outer]").select("div[id$=container]");
//		Element per;
//		if (content_k.size() == 0) {
//			if(content_j.size() == 0){   //如果答案中没有出现回答，就令答案为空
//				per = title.first();
//			}else{
//				per = content_j.first();
//			}
//		} else {
//			per = content_k.first();
//		}
//
//		String answer_context_s = null;
//		try{
//			answer_context_s = per.text(); // 内容可能副本，取其中的第一个即可
//		}catch(Exception e){
//			e.printStackTrace();
//			System.out.println("得到答案内容出错...");
//		}
//		// 使用split得到文本的单词数目
//		// String[] answers = answer_context_s.split(" ");
//		int content_length = answer_context_s.length();
//		System.out.println("回答字符长度为：" + content_length);
//		return content_length;
//	}

	/**
	 * 实现功能：解析内容得到链接，返回链接地址String
	 * 解析对象：问题网页（第n条回答）
	 * 解析标签形式：
	 * @param doc,n
	 */
	public static String answerURLs(Document doc, int n) {
//		String answer_context_s = null;
//		System.out.print("第" + (n+1) + "条回答内容为：");
		Elements contents = doc.select("div.pagedlist_item");
		Elements content_i = contents.select("div[class^=Answer Answer]");
		if(n < content_i.size()){
			String content = content_i.get(n).attr("id");
			Elements title = doc.select("div.header").select("div.question_text_edit");
			Elements content_j = doc.select("div[class^=Answer Answer]")
					.select("div#" + content + "_content");
			Elements content_k = content_j.select("div[class^=ExpandedQText]")
					.select("span.rendered_qtext");
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
//				System.out.println("第" + (n+1) + "条回答 不 包含链接");
				return "0";
			} else {
				String urls = ""; // 用于保存链接
				for (int i = 0; i < a.size(); i++) {
					Element b = a.get(i);
					urls = urls + "\n" + b.attr("href");
				}
//				System.out.println("第" + (n+1) + "条回答包含链接："+urls);
				String exist = "1";
				return exist;
			}
		} else {
			return "0";
		}
		
	}

	/**
	 * 实现功能：解析作者的个人信息，返回作者的个人信息 string
	 * 解析对象：问题网页（第n条回答的作者个人信息）
	 * 解析标签形式：
	 * @param doc,n
	 */
	public static String authorName(Document doc, int n) {
		Elements author_name = doc.select("span.feed_item_answer_user");
		Element a = author_name.get(n); // 单个回答
		// 用户存在Quora User匿名的情况
		Elements c = a.select("a.user");
		String author_name_s = null;
		if (c.size() == 0) {
			author_name_s = "Quora User(匿名用户)";
		} else {
			author_name_s = c.text();
		}
//		System.out.println("第" + (n + 1) + "位回答者姓名为：" + author_name_s);
		return author_name_s;
	}
	
//	/**
//	 * 实现功能：解析作者的个人信息，返回作者的个人信息 string
//	 * 解析对象：问题网页（第n条回答的作者个人信息）
//	 * 解析标签形式：
//	 * @param doc,n
//	 */
//	public static String authorInfo(Document doc, int n) {
//		Elements author_name = doc.select("span.feed_item_answer_user");
//		Element a = author_name.get(n); // 单个回答
//		// 用户职业存在未添加的情况
//		Elements b = a.select("span.hidden");
//		String profession = null;
//		if (b.size() == 0) {
//			profession = "未添加职业信息";
//		} else {
//			profession = b.text();
//		}
//		String author_info_s = profession;
//		System.out.println("第" + (n + 1) + "位回答者职业信息为：" + author_info_s);
//		return author_info_s;
//	}

	/**
	 * 实现功能：解析作者回答问题提供的答案数目，返回作者领域 string
	 * 解析对象：作者网页（第n条回答的作者页面）（n可去）
	 * 解析标签形式：
	 * @param doc,n
	 */
	public static String authorAnswers(Document doc, int n) {
		Elements author_Answers = doc.select("div.primary").select("span[class]");
		if(author_Answers.size() == 0){
//			System.out.println("没有得到作者的答案总数信息，建议重新爬取作者页面...");
			return null;
		}else{
			String author_Answers_s = author_Answers.get(0).text();
//			System.out.println("第" + (n + 1) + "位作者 回答 问题的数目为：" + author_Answers_s);
			return author_Answers_s;
		}
	}
	
	/**
	 * 实现功能：解析作者回答问题提供的问题数目，返回作者领域 string
	 * 解析对象：作者网页（第n条回答的作者页面）（n可去）
	 * 解析标签形式：
	 * @param doc,n
	 */
	public static String authorQuestions(Document doc, int n) {
		Elements authorQuestion = doc.select("div.primary").select("a").select("span");
//		System.out.println(authorQuestion);
		if(authorQuestion.size() == 0){
//			System.out.println("没有得到作者的答案总数信息，建议重新爬取作者页面...");
			return null;
		}else{
			if(authorQuestion.size() != 1){
				String authorQuestionCount = authorQuestion.get(1).text();
//				System.out.println("第" + (n + 1) + "位作者 提出 问题的数目为：" + authorQuestionCount);
				return authorQuestionCount;
			} else {
//				System.out.println("第" + (n + 1) + "位作者 提出 问题的数目--->难以获得");
				return "";
			}
			
		}
	}

	/**
	 * 实现功能：解析作者的粉丝数目，返回作者的粉丝数 string
	 * 解析对象：作者网页（第n条回答的作者页面）（n可去）
	 * 解析标签形式：
	 * @param doc,n
	 */
	public static String authorFollowers(Document doc, int n) {
		Elements author_followers = doc.select("div.secondary").select("span[class]");
		if(author_followers.size() == 0){
//			System.out.println("没有得到作者粉丝信息，建议重新爬取作者页面......");
			return null;
		}else{
			String author_followers_s = author_followers.get(0).text();
//			System.out.println("第" + (n + 1) + "位作者follower数目为：" + author_followers_s);
			return author_followers_s;
		}
	}

	/**
	 * 实现功能：解析作者偶像数目，返回作者偶像数目 string
	 * 解析对象：作者网页（第n条回答的作者页面）（n可去）
	 * 解析标签形式：
	 * @param doc,n
	 */
	public static String authorFollowing(Document doc, int n) {
		Elements author_following = doc.select("div.secondary").select("span[class]");
		if(author_following.size() == 0){
//			System.out.println("没有得到作者粉丝信息，建议重新爬取作者页面......");
			return null;
		}else{
			if(author_following.size() != 1){
				String author_following_s = author_following.get(1).text();
//				System.out.println("第" + (n + 1) + "位作者following数目为：" + author_following_s);
				return author_following_s;
			} else {
//				System.out.println("第" + (n + 1) + "位作者following数目--->难以获得");
				return "";
			}
		}
	}

	/**
	 * 实现功能：解析作者擅长回答的主要问题领域，返回作者领域 string
	 * 解析对象：作者网页（第n条回答的作者页面）（n可去）
	 * 解析标签形式：
	 * @param doc,n
	 */
	public static String authorKnowAbout(Document doc, int n) {
		Elements author_Answers = doc.select("div.layout_3col_right");
		if(author_Answers.size() == 0){
//			System.out.println("没有得到作者的领域信息，建议重新爬取作者页面...");
			return null;
		}else{
			Elements a = author_Answers.select("div[class$=ProfileExperienceList]");
			if (a.size() == 0) {
//				System.out.println("该作者无擅长领域！！！");
				return "";
			} else {
				Elements TopicName = a.select("li[class]").select("span.name_text");
				Elements answer_count = a.select("li[class]").select("span.answers_link");
				Elements answer_endorsements = a.select("li[class]")
						.select("div.UserTopicInfoLink").select("span[id]");
				// int num = TopicName.size();
				String author_Answers_s = "";
				for (int i = 0; i < answer_count.size(); i++) {
					author_Answers_s = author_Answers_s 
							+ TopicName.get(i).text() + "："
							+ answer_count.get(i).text() + " + "
							+ answer_endorsements.get(i).text() + "\n";
				}
//				System.out.println("第" + (n + 1) + "位作者回答擅长的领域主要为：" + author_Answers_s);
				return author_Answers_s;
			}
		}
	}

}