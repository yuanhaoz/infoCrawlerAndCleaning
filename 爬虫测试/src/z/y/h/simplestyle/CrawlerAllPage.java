package z.y.h.simplestyle;

/**
 * zhengyuanhao  2015/7/28
 * 简单实现：simple style
 * 实现功能：1.得到第一层页面链接
 * 			2.爬取第一层页面
 *			3.得到第二层页面链接
 *			4.爬取第二层页面 
 *			5.得到第二层页面的图片链接
 *			6.爬取第二层页面的图片
 */

import java.io.File;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Jsoup.JsoupParse;

public class CrawlerAllPage {

	/**
	 * 用途：得到第一层页面链接
	 * 输入：
	 * 输出：第一层页面链接ArrayList
	 * @author zhengyuanhao
	 */
	public ArrayList<String> getParentUrl() throws Exception {
		ArrayList<String> pageUrl = new ArrayList<String>(); 
		
		// 第一层链接基本形式
		String originUrl = "http://www.simple-style.com/page/";
		
		// 总共有32层第一层页面
		for (int i = 1; i < 33; i++) {
			String everyUrl = originUrl + i;
			pageUrl.add(everyUrl);
		}
		return pageUrl;
	}
	
	/**
	 * 用途：爬取第一层页面
	 * 输入：
	 * 输出：每个第一层目录下的第一层网页
	 * @author zhengyuanhao
	 */
	public void crawlerParentPage() throws Exception {
		CatalogOperation catalog = new CatalogOperation();
		Crawler crawler = new Crawler();
		ArrayList<String> pageUrl = getParentUrl();
		for (int i = 1; i < 33; i++) {
			String parentUrl = pageUrl.get(i - 1);                         // 第一层页面链接
			String fileCatalog = catalog.getPictureCatalog(i);             // 文件存储路径
			String filePath = fileCatalog + "page" + i + ".html";          // 文件存储名称
			File file = new File(filePath);
			if(!file.exists()){
				crawler.crawler(filePath, parentUrl);                      // 爬取第一层页面（selenium）
//				crawler.crawlerByHttpClient(filePath, parentUrl);          // 爬取第一层页面（httpClient）
				System.out.println(filePath + "已保存...");
			} else {
				System.out.println(filePath + "已存在, 无需爬取...");
			}
			
		}
	}
	
	/**
	 * 用途：得到第n个第一层页面的所有子页面的链接
	 * 输入：解析对象doc
	 * 输出：第二层页面链接数组链表ArrayList
	 * @author zhengyuanhao
	 */
	public ArrayList<String> getChildUrl(Document doc) throws Exception {
		ArrayList<String> childUrl = new ArrayList<String>();
		Elements hrefs = doc.select("div#content").select("div.post").select("div.entry");  // 一个子链接
		for (int i = 0; i < hrefs.size(); i++) {
			Element href = hrefs.get(i);
//			String sequence = href.select("h2").attr("id");                 // 图片编号
//			String title = href.select("h2").select("a").text();			// 图片标题
			String url = href.select("h2").select("a").attr("href");		// 子页面链接
			System.out.println("url is : " + url);
			childUrl.add(url);
		}
		return childUrl;
	}
	
	/**
	 * 用途：爬取第二层页面
	 * 输入：
	 * 输出：每个第二层目录下的第二层网页
	 * @author zhengyuanhao
	 */
	public void crawlerChildPage() throws Exception {
		CatalogOperation catalog = new CatalogOperation();
		Crawler crawler = new Crawler();
		for (int i = 1; i < 33; i++) {
			String fileCatalog = catalog.getPictureCatalog(i);              // 定位第一层目录
			String fileName = fileCatalog + "page" + i + ".html";          
			Document doc = JsoupParse.parsePathText(fileName);				// 解析第一层网页
			ArrayList<String> childUrl = getChildUrl(doc);					// 得到所有第二层网页链接
			for(int j = 0; j < childUrl.size(); j++){
				
				// 得到第二层目录  和   文件路径
				String pictureCatalog = catalog.getPictureCatalogForChildPage(i, j);
				String filePath = pictureCatalog + "page" + i + "_" + j + ".html"; 
				 
				// 爬取第二层页面
				File file = new File(filePath);
				if(!file.exists()){
					crawler.crawler(filePath, childUrl.get(j));             // selenium
//					crawler.crawlerByHttpClient(filePath, childUrl.get(j)); // httpClient
					System.out.println(filePath + "已保存...");
				} else {
					System.out.println(filePath + "已存在, 无需爬取...");
				}
				
			}
		}
	}
	
	/**
	 * 用途：得到第二层页面的图片链接
	 * 输入：解析对象doc
	 * 输出：每个第二层网页中的图片链接
	 * @author zhengyuanhao
	 */
	public ArrayList<String> getImageUrl(Document doc) {
		ArrayList<String> imgUrl = new ArrayList<String>();
		Elements img = doc.select("div.entry").select("p").select("img");
		for (int i = 0; i < img.size(); i++) {
			String src = img.get(i).attr("src");
			imgUrl.add(src);
			System.out.println("image src is: " + src);
		}
		return imgUrl;
	}
	
	/**
	 * 用途：爬取第二层页面的图片
	 * 输入：
	 * 输出：每个第二层目录下的图片资源
	 * @author zhengyuanhao
	 */
	public void crawlerChildPageImage() throws Exception {
		CatalogOperation catalog = new CatalogOperation();
		for (int i = 1; i < 33; i++) {
			String fileCatalog = catalog.getPictureCatalog(i);              // 定位第一层目录
			String fileName = fileCatalog + "page" + i + ".html";          
			Document doc = JsoupParse.parsePathText(fileName);				// 解析第一层网页
			ArrayList<String> childUrl = getChildUrl(doc);					// 得到所有第二层网页链接
			for(int j = 0; j < childUrl.size(); j++){
				
				// 得到第二层目录  和   文件路径
				String pictureCatalog = catalog.getPictureCatalogForChildPage(i, j);
				String filePath = pictureCatalog + "page" + i + "_" + j + ".html"; 
				
				// 解析第二层网页，得到图片链接
				Document docChild = JsoupParse.parsePathText(filePath);		
				ArrayList<String> imgUrl = getImageUrl(docChild);
				
				// 保存链接到txt文件中
				String txtFileName = pictureCatalog + "imgUrls.txt";
				Extraction.writeImageUrlToFile(imgUrl, txtFileName);
				
				// 爬取图片
				for (int m = 0; m < imgUrl.size(); m++) {
					
					//得到图片链接 和 保存路径
					String url = imgUrl.get(m);
					String imageFileName = pictureCatalog + "page" + i + "_" + j + "_" + m + ".jpeg";
					
					//爬取图片
					Extraction.downLoadImage(url, imageFileName);
				}
				System.out.println("page" + i + "_" + j + "  图片爬取结束...");
			}
			System.out.println("page" + i + "  图片爬取结束...");
		}
	}
	
	
}
