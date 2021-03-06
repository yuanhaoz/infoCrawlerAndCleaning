package dataCollection;

/**
 * zhengyuanhao  2015/6/30
 * 简单实现：quora  
 * 实现功能：采集Quora网站三门学科的数据
 * 			每门学科有100左右主题词
 * 
 */

import Jsoup.JsoupParse;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import webpage.QuoraWebPage;
import base.DirFile;
import basic.KeywordCatalogDesign;

public class DataCollection {
	
	public static void main(String[] args) throws Exception {
		realize();
	}
	
	
	/**
	 * 实现功能：数据采集
	 * @param course
	 */
	public static void realize() throws Exception {
		crawler("Computer_network");
	}
	
	/**
	 * 实现功能：用于爬取某门课程所有主题下的所有网页
	 *          输入是课程名，输出是所有网页
	 * @param course
	 */
	public static void crawler(String course) throws Exception {
		String catalog = "file/datacollection/" + course;
		ArrayList<String> a = DirFile.getFileNamesFromDirectorybyArraylist(catalog);  //读取所有文件名
		KeywordCatalogDesign.setKeywordCatalog(course);           //为关键词建立目录
		Iterator<String> it = a.iterator();   //设置迭代器
		while(it.hasNext()){                  //判断是否有下一个
			long start = System.currentTimeMillis();
			String keyWord = it.next();       //得到关键词
			crawlerSubjectPages(keyWord);     //爬取主题页面
			crawlerQuestionAndAuthorPages(keyWord);            //爬取问题页面和作者页面
			long end = System.currentTimeMillis();
			System.out.println("爬取" + keyWord + "的所有信息用时：" + (end - start)/1000 + "秒...");
		}
	}

	
	
	/**
	 * 用于得到爬取数据的时间
	 */
	public static String getCrawlerTime() throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//设置日期格式 
		String crawlerTime = df.format(new Date());   // new Date()为获取当前系统时间
		System.out.println("CrawlerTime1 is ：" + crawlerTime);
		return crawlerTime;
	}


	/**
	 * 实现功能：根据关键词，得到主题页面的链接（一个页面）
	 * @param keyword
	 */
	public static String getSubjectURLs(String keyword){
		String url = "http://www.quora.com/search?q=" + keyword;
		System.out.println("主题页面链接为：" + url);
		return url;
	}
	
	/**
	 * 实现功能：解析主题网页，
	 *          得到主题页面中所有问题页面的链接
	 *          链接数量一般在49-59之间（爬取时拉动四次）
	 * @param keyword
	 */
	public static String[] getQuestionURLs(String keyword) throws Exception {
		String path = KeywordCatalogDesign.GetKeywordCatalog(keyword) + keyword + ".html";
		System.out.println("路径为：" + path);
		// System.out.println(path);
		File file = new File(path);
		if (!file.exists()) {
			String[] testResult = { "aa", "bb" };
			System.out.println(path + "  不存在，得不到它的子页面链接！！！");
			return testResult;
		} else {
			Document doc = JsoupParse.parsePathText(path);
			Elements links = doc.select("div.pagedlist_item").select("div.title ")
					.select("a.question_link").select("a[href]");
			// print("\n" + path + " 所有问题的链接  Links: (%d)", links.size());
			String urls[] = new String[links.size()];
			for (int i = 0; i < links.size(); i++) {
				Element link = links.get(i);
				urls[i] = "http://www.quora.com" + link.attr("href"); // 将所有链接存到urls数组里面
				System.out.println(String.format("问题页面链接为：" + " * " + (i + 1) + ": <%s>  (%s)", urls[i], link.text()));
			}
			return urls;
		}
	}
	
	/**
	 * 实现功能：解析问题页面，
	 *          得到问题页面中所有作者页面的链接
	 *          keyword是主题词，n 是主题页面下的问题页面的序号
	 * @param keyword,n
	 */
	public static ArrayList<String> getAuthorURLs(String keyword, int n){
		String path = KeywordCatalogDesign.GetKeywordCatalog(keyword);
		String filePath = path + keyword + n + ".html";
		Document doc = JsoupParse.parsePathText(filePath);
		Elements authors = doc.select("div.grid_page_center_col").select("div.pagedlist_item").select("div.author_info");
		ArrayList<String> url = new ArrayList<String>();    //最后有三个作者是例外
		System.out.println(authors.size());
		for (int m = 0; m < authors.size(); m++) {
			Element a = authors.get(m);
			Elements b = a.select("a.user");
			if (b.size() == 0) {
				System.out.println("作者信息不存在！！！");
			} else {
				Element author = b.get(0);
				String urls = author.attr("href");
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
	 * 实现功能：爬取单个主题页面（爬取阶段1）
	 * 			输入是一门学科下面的单个主题词keyword
	 * @param keyword
	 */
	public static void crawlerSubjectPages(String keyword) throws Exception {
		String keywordCatalog = KeywordCatalogDesign.GetKeywordCatalog(keyword);   //得到主题文件夹
		String filePath = keywordCatalog + keyword + ".html";      //设置主题页面保存路径
		String subjectUrls = DataCollection.getSubjectURLs(keyword);         //得到主题页面链接
		QuoraWebPage.seleniumCrawlerSubject(filePath, subjectUrls);           //爬取主题页面
		String crawlerTime = getCrawlerTime();
		System.out.println("爬取时间为：" + crawlerTime);
	}
	
	/**
	 * 实现功能：爬取单个主题下的所有问题页面和作者页面（爬取阶段2）
	 * 			输入是主题词keyword
	 * @param keyword
	 */
	public static void crawlerQuestionAndAuthorPages(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.GetKeywordCatalog(keyword);
		String[] urls = getQuestionURLs(keyword);			   //得到问题页面链接
		String[] testResult = { "aa", "bb" };
		if (urls.equals(testResult)) {
			System.out.println("不存在第一层链接！！！");
		} else {
			for (int n = 0; n < urls.length; n++) {
				QuoraWebPage a = new QuoraWebPage();
				String fileName = keyword + n + ".html";
				String filePath = catalog + fileName;          //设置问题页面保存路径
				a.seleniumCrawlerQuestion(filePath, urls[n]);  //爬取问题网页
				String crawlerTime = getCrawlerTime();
				System.out.println("爬取时间为：" + crawlerTime);
				crawlerAuthorPages(keyword, n);                //爬取问题页面中的作者页面
			}
		}
	}

	/**
	 * 实现功能：爬取“某个”问题页面中的作者页面（爬取阶段2.1）
	 * @param keyword,n
	 */
	public static void crawlerAuthorPages(String keyword, int n) throws Exception {
		String catalog = KeywordCatalogDesign.GetKeywordCatalog(keyword);
		ArrayList<String> authorUrls = DataCollection.getAuthorURLs(keyword, n);         //得到作者页面链接
		for (int m = 0; m < authorUrls.size(); m++) {
			String fileName = keyword + n + "_author_" + m + ".html";
			String filePath = catalog + fileName;                     //设置作者页面保存路径
			File file1 = new File(filePath);						  //判断作者页面是否存在
			if(!file1.exists()){
				QuoraWebPage a = new QuoraWebPage();
				a.seleniumCrawlerAuthor(filePath, authorUrls.get(m));     //爬取作者页面
				String crawlerTime = getCrawlerTime();
				System.out.println("爬取时间为：" + crawlerTime);
			}else{
				System.out.println(filePath + "存在，不需要爬取...");
			}
		}
	}
}
