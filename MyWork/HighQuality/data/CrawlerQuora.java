package data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Jsoup.JsoupParse;
import basic.QuoraWebPage;

public class CrawlerQuora {
	
	private static String cata = "02-CQA网站中问题答案质量评估";
	private static String path =  "F:\\"+cata+"\\二叉树\\quora";
	private static String pathQ =  "F:\\"+cata+"\\二叉树\\quora\\question";
	private static String pathAuthor =  "F:\\"+cata+"\\二叉树\\quora\\question\\author";
	private static String pathAuthorPage =  "F:\\"+cata+"\\二叉树\\quora\\question\\authorPage";
	private static String keyword =  "Binary_tree";

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		crawlerQuora();
		crawlerKeyword();
	}
	
	/**
	 * 爬取Quora二叉树话题页面所有问题
	 */
	public static void crawlerQuora(){
		String filePath = path + "\\Binary_tree(selenium).html";
		String url = "https://www.quora.com/topic/Binary-Trees/all_questions";
		try {
			QuoraWebPage.seleniumCrawlerTopic(filePath, url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 实现功能：用于爬取某门课程的一个主题数据，输入是主题名和课程名，输出是主题相关网页
	 */
	public static void crawlerKeyword() throws Exception {
		String keyword = "Binary_tree";
		long start = System.currentTimeMillis();
		storeQuestionURLs(keyword);//存储问题页面链接
		crawlerQAPages(keyword);//爬取问题页面和作者页面
		long end = System.currentTimeMillis();
		System.out.println("爬取" + keyword + "的所有信息用时：" + (end - start)/1000 + "秒...");
	}

	/**
	 * 实现功能：解析主题网页，得到主题页面中所有问题页面的链接，链接数量一般在49-59之间（爬取时拉动四次）
	 */
	public static String[] getQuestionURLs(String keyword) throws Exception {
		String filePath = path + "\\" + keyword + ".html";
		System.out.println(filePath);
		File file = new File(filePath);
		if (!file.exists()) {
			String[] testResult = { "aa", "bb" };
			System.out.println(filePath + "  不存在，得不到它的子页面链接！！！");
			return testResult;
		} else {
			Document doc = JsoupParse.parsePathText(filePath);
			Elements links = doc.select("a.question_link").select("a[href]");
			String urls[] = new String[links.size()];
			for (int i = 0; i < links.size(); i++) {
				Element link = links.get(i);
				urls[i] = "http://www.quora.com" + link.attr("href"); // 将所有链接存到urls数组里面
//				System.out.println(String.format("问题页面链接为：" + " * " + (i + 1) + ": <%s>  (%s)", urls[i], link.text()));
			}
			return urls;
		}
	}
	
	/**
	 * 实现功能：存储问题网页的链接
	 */
	public static void storeQuestionURLs(String keyword) throws Exception {
		String urls[] = getQuestionURLs(keyword);
		String url2txt = "";
		for(int i = 0; i < urls.length; i++){
			String url = urls[i];
			url2txt = url2txt + keyword + i + ": " + url + "\n";
		}
		String filePath = path + "\\" + keyword + "_question.txt";
		File f = new File(filePath);
		if (!f.exists()) {
			f.createNewFile();
		}
		// 写文件
		BufferedWriter output = new BufferedWriter(new FileWriter(f));
		output.write(url2txt);
		output.close();
	}
	
	/**
	 * 实现功能：解析问题页面，得到问题页面中所有作者页面的链接，keyword是主题词，n 是主题页面下的问题页面的序号
	 * selenium爬取
	 */
	public static ArrayList<String> getAuthorURLs(String keyword, int n){
		String filePath = pathQ + "\\" + keyword + n + ".html";
		Document doc = JsoupParse.parsePathText(filePath);
		// 新版Quora网站（2015年9月之前的）（网站更新后标签发生了变化）（Selenium爬取存在的问题）
		Elements authors = doc.select("div.pagedlist_item").select("span.feed_item_answer_user");
		ArrayList<String> url = new ArrayList<String>();    //最后有六个作者是例外（Selenium爬取存在的问题）
//		System.out.println("作者个数："+authors.size());
		
		for (int m = 0; m < authors.size() - 6; m++) {
			Element a = authors.get(m);
			Elements b = a.select("a.user");
			if (b.size() == 0) {
//				System.out.println("作者信息不存在！！！");
			} else {
				Element author = b.get(0);
				String urls = author.attr("href");
//				System.out.println(urls);
				if (urls.startsWith("http://")) {
					url.add(urls);
				} else if (urls.startsWith("/")) {
					url.add("http://www.quora.com/" + urls);
				}
			}
		}
		return url;
	}
	
	/**
	 * 实现功能：存储作者网页的链接
	 */
	public static void storeAuthorURLs(String keyword, int n) throws Exception {
		ArrayList<String> urls = getAuthorURLs(keyword, n);
		String url2txt = "";
		for(int i = 0; i < urls.size(); i++){
			String url = urls.get(i);
			url2txt = url2txt + keyword + n + "_author_" + i + ": " + url + "\n";
		}
		// keyword + n + "_author_" + m + ".html"
		new File(pathAuthor).mkdir();
		String filePath = pathAuthor + "\\" + keyword + n + "_author.txt";
		File f = new File(filePath);
		if (!f.exists()) {
			f.createNewFile();
		}
		// 写文件
		BufferedWriter output = new BufferedWriter(new FileWriter(f));
		output.write(url2txt);
		output.close();
	}
	
	/**
	 * 实现功能：爬取单个主题下的所有问题页面和作者页面（爬取阶段2），输入是主题词keyword
	 */
	public static void crawlerQAPages(String keyword) throws Exception {
		String catalog = path + "\\question";
		new File(catalog).mkdir();
		String[] urls = getQuestionURLs(keyword);//得到问题页面链接
		String[] testResult = { "aa", "bb" };
		if (urls.equals(testResult)) {
			System.out.println("不存在第一层链接！！！");
		} else {
			for (int n = 329; n < urls.length; n++) {
				//设置问题页面保存路径
				String fileName = keyword + n + ".html";
				String filePath = catalog + "\\" + fileName; 
				if(new File(filePath).exists()){
					System.out.println(filePath + " is existing...");
				} else {
					//爬取问题网页
//					QuoraWebPage.seleniumCrawlerQuestion(filePath, urls[n]);  
				}
				//爬取作者页面
//				storeAuthorURLs(keyword, n);
				crawlerAPages(keyword, n);  
			}
		}
	}

	/**
	 * 实现功能：爬取"某个"问题页面中的作者页面（爬取阶段2.1）
	 */
	public static void crawlerAPages(String keyword, int n) throws Exception {
		new File(pathAuthorPage).mkdir();
		ArrayList<String> authorUrls = CrawlerQuora.getAuthorURLs(keyword, n);// 得到作者页面链接
		for (int m = 0; m < authorUrls.size(); m++) {
			// 设置作者页面保存路径
			String fileName = keyword + n + "_author_" + m + ".html";
			String filePath = pathAuthorPage + "\\" + fileName;       
			
			File file1 = new File(filePath);// 判断作者页面是否存在
			if(!file1.exists()){
				System.out.println("\n" + filePath + "不存在，需要爬取...");
				// 爬取作者页面（webmagic爬虫）
				QuoraWebPage.webmagicCrawler(filePath, authorUrls.get(m)); 
				// 爬取作者页面（selenium爬虫）
//				QuoraWebPage.seleniumCrawlerAuthor(filePath, authorUrls.get(m)); 
				// httpclient爬虫
//				QuoraWebPage.httpClientCrawler(filePath, authorUrls.get(m));
				
				System.out.println("保存文件名："+filePath);
			}else{
				System.out.println(filePath + "存在，不需要爬取...");
			}
		}
	}

}
