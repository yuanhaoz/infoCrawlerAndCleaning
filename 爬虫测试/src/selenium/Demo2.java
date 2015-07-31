package selenium;

/**
 * ��;����ȡ    http://www.simple-style.com/  ��վ��ͼƬ
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
	 * ��;����ȡ��վ
	 * ���룺��ȡ��վ����url�ͱ����ļ���filename
	 * ��������浽file�ļ����µ�html��ҳ
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
	 * ��;�������ַ������ļ�
	 * ���룺�ַ���str�ͱ����ļ�·��filePath
	 * ��������浽file�ļ����µ�html��ҳ
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
	 * ��;�������õ�ͼƬ��
	 * ���룺doc�ĵ�
	 * �����������title
	 * @author zhengyuanhao
	 */
	public static String getPictureName(Document doc) {
		Elements answer_voters = doc.select("div.Answer")
				.select("div.primary_item").select("a[class]").select("span");
		String answer_voters_s = answer_voters.text();
		System.out.println("֧��Ʊ��Ϊ��" + answer_voters_s);
		if (answer_voters_s.equals("0")) {
			System.out.println("֧��Ʊ��Ϊ0����ע�⣡����");
		}
		return answer_voters_s;
	}
	
	/**
	 * ����ָ��·����html�ļ�doc
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
