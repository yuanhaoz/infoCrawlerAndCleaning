package selenium;

/**
 * 用途：爬取    http://www.simple-style.com/  网站的图片
 * @author zhengyuanhao
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class Demo2 {

	public static void main(String[] args) throws InterruptedException {
		Demo2 test = new Demo2();
		String url = "http://www.simple-style.com/";
		String filename = "simple-style";
		test.scroll(filename, url);
	}

	/**
	 * 用途：爬取网站
	 * 输入：爬取网站链接url和保存文件名filename
	 * 输出：保存到file文件夹下的html网页
	 * @author zhengyuanhao
	 */
	public void scroll(String fileName, String url) throws InterruptedException {
		WebDriver driver = new InternetExplorerDriver();
		driver.get(url);
		System.out.println("Page title is: " + driver.getTitle());
		// save page
		String html = driver.getPageSource();
		String filePath = "file/" + fileName + ".html";
		saveHtml(filePath, html);
		System.out.println("save finish");
		// Close the browser
		Thread.sleep(1000);
		driver.quit();
	}

	/**
	 * 用途：保存字符流到文件
	 * 输入：字符流str和保存文件路径filePath
	 * 输出：保存到file文件夹下的html网页
	 * @author zhengyuanhao
	 */
	public void saveHtml(String filePath, String str) {
		try {
			OutputStreamWriter outs = new OutputStreamWriter(new FileOutputStream(filePath, true), "utf-8");
			outs.write(str);
			outs.close();
		} catch (IOException e) {
			System.out.println("Error at save html...");
			e.printStackTrace();
		}
	}
	
	/**
	 * 用途：解析得到图片名
	 * 输入：doc文档
	 * 输出：标题名title
	 * @author zhengyuanhao
	 */
	public static String getPictureName(Document doc) {
		Elements answer_voters = doc.select("div.Answer")
				.select("div.primary_item").select("a[class]").select("span");
		String answer_voters_s = answer_voters.text();
		System.out.println("支持票数为：" + answer_voters_s);
		if (answer_voters_s.equals("0")) {
			System.out.println("支持票数为0，请注意！！！");
		}
		return answer_voters_s;
	}
	
	/**
	 * 解析指定路径的html文件doc
	 * @param path
	 */
	public static Document parsePathText(String path) {
		Document doc = null;
		try {
			File input = new File(path);
			doc = Jsoup.parse(input, "UTF-8", "http://www.simple-style.com/"); // http://www.quora.com/Can-I-get-help-about-Binary-search
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String text = doc.body().text();
		return doc;
	}
	
}
