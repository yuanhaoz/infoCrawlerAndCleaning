package test;

import Jsoup.JsoupParse;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import webpage.QuoraWebPage;
import base.DirFile;
import basic.KeywordCatalogDesign;

/**
 * 实现Runnable接口       主题页面多线程爬虫实现
 * @author zhengtaishuai
 */
class thread implements Runnable {
	
	//需要同步的变量定义为private
	private int i = 0;
	private int j = 0;
	
	/**
	 * 实现功能：用于爬取某门课程的一个主题数据
	 *          输入是课程名，输出是 主题页面 --> 问题页面 --> 作者页面
	 *          深度优先
	 */
	public void crawlerByDepth(String course) throws Exception {
		String threadname = Thread.currentThread().getName();
		System.out.println(threadname + "--->> 刚进入pagedown方法...");
		
		// 为关键词建立目录
		KeywordCatalogDesign.setKeywordCatalog(course);
		
		// 读取所有文件名
		String catalog = "file/datacollection/" + course;
		ArrayList<String> a = DirFile.getFileNamesFromDirectorybyArraylist(catalog);
//		int length = a.size();
//		System.out.println("length:" + length);
		while(i < a.size()){
			String keyword = null;
			
			//需要同步
			synchronized (this) {
				//得到关键词，需要同步
				keyword = a.get(i);
				System.out.println("正在爬取关键词：" + keyword);
				i++;
			}
			long start = System.currentTimeMillis();
			
			// 爬取主题页面
			crawlerSubjectPages(keyword);
			
			// 爬取问题页面和作者页面
			crawlerQuestionAndAuthorPages(keyword);    
      
			long end = System.currentTimeMillis();
			System.out.println("爬取"+keyword +"的所有信息用时："+(end-start)/1000+"秒...");
		}
		
//		Iterator<String> it = a.iterator();
//		while(it.hasNext()){
//			//需要同步
//			synchronized (this) {
//				//得到关键词
//				keyword = it.next();
//				System.out.println("keyword:" + keyword);
//			}
////			long start = System.currentTimeMillis();
//			
//			// 爬取主题页面
//			crawlerSubjectPages(keyword);   
//			// 爬取问题页面和作者页面
//			crawlerQuestionAndAuthorPages(keyword);    
//      
////			long end = System.currentTimeMillis();
////			System.out.println("爬取"+keyword +"的所有信息用时："+(end-start)/1000+"秒...");
//		}
		
		System.out.println(threadname + "--->> 离开pagedown方法...");
	}
	
	/**
	 * 存在一些问题，需要改进
	 * 实现功能：用于爬取某门课程的一个主题数据
	 *          输入是课程名，输出是 主题页面 --> 问题页面 --> 作者页面
	 *          广度优先，逐层爬取（两层：1.主题页面；2.问题+作者页面）
	 */
	public void crawlerByWidth(String course) throws Exception {
		String threadname = Thread.currentThread().getName();
		System.out.println(threadname + "--->> 刚进入pagedown方法...");
		
		// 为关键词建立目录
		KeywordCatalogDesign.setKeywordCatalog(course);
		
		// 读取所有文件名
		String catalog = "file/datacollection/" + course;
		ArrayList<String> a = DirFile.getFileNamesFromDirectorybyArraylist(catalog);
		
		// 爬取主题页面
		while(i < a.size()){
			String keyword = null;
			
			//需要同步
			synchronized (this) {
				//得到关键词，需要同步
				keyword = a.get(i);
				System.out.println("正在爬取关键词：" + keyword);
				i++;
			}
			long start = System.currentTimeMillis();
			
			// 爬取主题页面
			crawlerSubjectPages(keyword);
			
			long end = System.currentTimeMillis();
			System.out.println("爬取"+keyword +"的所有信息用时："+(end-start)/1000+"秒...");
		}
		// 爬取问题页面和作者页面
		while(j < a.size()){
			String keyword = null;
			//需要同步
			synchronized (this) {
				//得到关键词，需要同步
				keyword = a.get(j);
				System.out.println("正在爬取关键词：" + keyword);
				j++;
			}
			// 爬取问题页面和作者页面
			crawlerQuestionAndAuthorPages(keyword);    
		}
		System.out.println(threadname + "--->> 离开pagedown方法...");
	}
	
	/**
	 * 实现功能：用于爬取某门课程的一个主题数据
	 *          输入是主题名和课程名，输出是主题相关网页
	 */
	public void crawlerKeyword(String keyword, String course) throws Exception {
		KeywordCatalogDesign.setKeywordCatalog(course);           //为关键词建立目录
		crawlerSubjectPages(keyword);     //爬取主题页面
		crawlerQuestionAndAuthorPages(keyword);            //爬取问题页面和作者页面
		System.out.println("爬取" + keyword + "所有信息用时...");
	}

	/**
	 * 实现功能：用于得到爬取数据的时间
	 */
	public String getCrawlerTime(String filePath) throws Exception {
		//得到文件的上次修改时间
		File file = new File(filePath);
		long time = file.lastModified();
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//设置日期格式 
		String crawlerTime = df.format(new Date(time));   // new Date()为获取当前系统时间
		System.out.println("CrawlerTime is ：" + crawlerTime);
		return crawlerTime;
	}


	/**
	 * 实现功能：根据关键词，得到主题页面的链接（一个页面）
	 * @param keyword
	 */
	public String getSubjectURLs(String keyword){
		String url = "http://www.quora.com/search?q=" + keyword;
		System.out.println("主题页面链接为：" + url);
		return url;
	}
	
	/**
	 * 实现功能：解析主题网页，得到主题页面中所有问题页面的链接
	 *          链接数量一般在49-59之间（爬取时拉动四次）
	 * @param keyword
	 */
	public String[] getQuestionURLs(String keyword) throws Exception {
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
	public ArrayList<String> getAuthorURLs(String keyword, int n){
		String path = KeywordCatalogDesign.GetKeywordCatalog(keyword);
		String filePath = path + keyword + n + ".html";
		
		System.out.println("filePath:" + filePath);
		
		Document doc = JsoupParse.parsePathText(filePath);
		
		//旧版Quora网站（2015年9月之前的）
//		Elements authors = doc.select("div.grid_page_center_col").select("div.pagedlist_item").select("div.author_info");
		
		//新版Quora网站（2015年9月之前的）（网站更新后标签发生了变化）
		Elements authors = doc.select("div.grid_page_center_col").select("div.pagedlist_item").select("span.feed_item_answer_user");
		ArrayList<String> url = new ArrayList<String>();    //最后有三个作者是例外
		System.out.println("作者个数："+authors.size());
		
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
	public void crawlerSubjectPages(String keyword) throws Exception {
		// 得到主题文件夹
		String keywordCatalog = KeywordCatalogDesign.GetKeywordCatalog(keyword);
		// 设置主题页面保存路径
		String filePath = keywordCatalog + keyword + ".html";
//		System.out.println("主题页面保存路径为：" + filePath);
		// 得到主题页面链接
		String subjectUrls = getSubjectURLs(keyword);        
		
		// 爬取主题页面
		System.out.println("开始爬取问题页面..." );
		QuoraWebPage.seleniumCrawlerSubject(filePath, subjectUrls);          
		System.out.println("开始爬取问题页面..." );
		
//		String crawlerTime = getCrawlerTime(filePath);
//		System.out.println("爬取时间为：" + crawlerTime);
	}
	
	/**
	 * 实现功能：爬取单个主题下的所有问题页面和作者页面（爬取阶段2）
	 * 			输入是主题词keyword
	 * @param keyword
	 */
	public void crawlerQuestionAndAuthorPages(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.GetKeywordCatalog(keyword);
		String[] urls = getQuestionURLs(keyword);			   //得到问题页面链接
		String[] testResult = { "aa", "bb" };
		if (urls.equals(testResult)) {
			System.out.println("不存在第一层链接！！！");
		} else {
			for (int n = 0; n < urls.length; n++) {
				QuoraWebPage a = new QuoraWebPage();
				
				//设置问题页面保存路径
				String fileName = keyword + n + ".html";
				String filePath = catalog + fileName;          
				
				//爬取问题网页
				System.out.println("开始爬取问题页面..." );
				a.seleniumCrawlerQuestion(filePath, urls[n]);  
				System.out.println("爬取问题页面结束..." );
				
//				String crawlerTime = getCrawlerTime(filePath);
//				System.out.println("爬取时间为：" + crawlerTime);
				
				//爬取问题页面中的作者页面
				System.out.println("开始爬取作者页面..." );
				crawlerAuthorPages(keyword, n);                
				System.out.println("爬取作者页面结束...");
			}
		}
	}

	/**
	 * 实现功能：爬取“某个”问题页面中的作者页面（爬取阶段2.1）
	 * @param keyword,n
	 */
	public void crawlerAuthorPages(String keyword, int n) throws Exception {
		String catalog = KeywordCatalogDesign.GetKeywordCatalog(keyword);
		ArrayList<String> authorUrls = getAuthorURLs(keyword, n);         //得到作者页面链接
		for (int m = 0; m < authorUrls.size(); m++) {
			//设置作者页面保存路径
			String fileName = keyword + n + "_author_" + m + ".html";
			String filePath = catalog + fileName;       
			
			File file1 = new File(filePath);						  //判断作者页面是否存在
			if(!file1.exists()){
				System.out.println("\n"+filePath + "不存在，需要爬取...");
				
				//爬取作者页面
				QuoraWebPage a = new QuoraWebPage();
				a.seleniumCrawlerAuthor(filePath, authorUrls.get(m)); 
				
				System.out.println("保存文件名："+filePath);
				//爬取时间
//				String crawlerTime = getCrawlerTime(filePath);
//				System.out.println("爬取时间为：" + crawlerTime);
			}else{
				System.out.println(filePath + "存在，不需要爬取...");
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String course  = "threadtest";
		try {
			crawlerByDepth(course);
//			crawlerByWidth(course);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			JOptionPane.showMessageDialog(null, "alert", "alert", JOptionPane.ERROR_MESSAGE); 
		}
	}
}
public class ThreadTest2 {
	public static void main(String[] args) {
		thread tr = new thread();
		// 设置启动线程数目
		int threadnumber = 3;
		// 弹出窗口
		JOptionPane.showConfirmDialog(null, "Hello Taishuai", "Begin", JOptionPane.YES_NO_OPTION); 
		// 启动线程
		for(int i = 0; i < threadnumber; i++){
			new Thread(tr).start();
		}
	}
}
