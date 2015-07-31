package selenium;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
/**
 * 优势：可以得到答案的时间信息，并且能够调用JS操作滚动条下拉页面刷新页面
 * @author zhengtaishuai
 *
 */
public class Demo1 {

	public static void main(String[] args) throws InterruptedException {
		Demo1 test = new Demo1();
		String url = "http://www.quora.com/search?q=Min+heap";
		String filename = "Min+heap3";
		test.scroll(filename, url);
	}

	public void scroll(String filename, String url) throws InterruptedException {
		WebDriver driver = new InternetExplorerDriver();
		driver.get(url);
		System.out.println("Page title is: " + driver.getTitle());
		// roll the page
		JavascriptExecutor JS = (JavascriptExecutor) driver;
		try {
			JS.executeScript("scrollTo(0, 5000)");
			System.out.println("1");
			Thread.sleep(10000);
			JS.executeScript("scrollTo(5000, 10000)");
			System.out.println("2");
			Thread.sleep(10000);
			JS.executeScript("scrollTo(10000, 30000)");    //document.body.scrollHeight
			System.out.println("3");
			Thread.sleep(10000);
			JS.executeScript("scrollTo(30000, 60000)");
			System.out.println("4");
			Thread.sleep(10000);
		} catch (Exception e) {
			System.out.println("Error at loading the page ...");
			driver.quit();
		}
		// save page
		String html = driver.getPageSource();
		String filepath = "f:/test/" + filename + ".html";
		saveHtml(filepath, html);
		System.out.println("save finish");
		// Close the browser
		Thread.sleep(2000);
		driver.quit();
	}

	public static void saveHtml(String filepath, String str) {
		try {
			OutputStreamWriter outs = new OutputStreamWriter(
					new FileOutputStream(filepath, true), "utf-8");
			outs.write(str);
			outs.close();
		} catch (IOException e) {
			System.out.println("Error at save html...");
			e.printStackTrace();
		}
	}
}
