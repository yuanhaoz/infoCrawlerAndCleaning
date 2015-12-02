package xjtu.sky.threadtest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import xjtu.sky.quora.tag2;

/**
 * 多线程爬虫
 * 广度优先实现，主题页面-->问题页面-->作者页面
 * @author zhengtaishuai
 */
public class PageDown {
	public static void main(String[] args) throws InterruptedException {
		TestThread2 t = new TestThread2();
		// 启动了四个线程，并实现了资源共享的目的
		new Thread(t).start();
		new Thread(t).start();
		new Thread(t).start();
	    new Thread(t).start();
	}
}

/**
 * 实现Runnable接口       主题页面多线程爬虫实现
 * @author zhengtaishuai
 */
class TestThread2 implements Runnable {
	public void run() {
		pagedown();
	}
	private int i = 0;
	
	// 根据关键词数组，爬取每个关键词的主题网页
	public void pagedown() {
		String threadname = Thread.currentThread().getName();
		System.out.println(threadname + "--->> 刚进入pagedown方法...");

		String[] keyword = getkeyword("testthread");
		int length = keyword.length;
		tag2 a = new tag2();
		while (i < length) {
			String filepath = null;
			String url = null;
			synchronized (this) { // 同步，只能被一个线程占用，用于给每个关键词新建一个文件夹
				String path = a.GetCatalog(keyword[i]);
				filepath = path + keyword[i] + ".html";
				System.out.println("正在爬取关键词：" + keyword[i]);
				url = "http://www.quora.com/search?q=" + keyword[i++]; // i++自动转换到下一个关键词
				System.out.println("url is : " + url);
				System.out.println("filepath is : " + filepath);
			}
			downPage(filepath, url);
		}
		System.out.println(threadname + "--->> 离开pagedown方法...");
	}
	
	public void downPage(String filepath, String url){
		File file = new File(filepath);
		if (!file.exists()) {
			WebDriver driver = new InternetExplorerDriver();
			driver.manage().timeouts()
					.pageLoadTimeout(10, TimeUnit.SECONDS); // 5秒内没打开，重新加载
			while (true) {
				try {
					driver.get(url);
				} catch (Exception e) {
					driver.quit();
					driver = new InternetExplorerDriver();
					driver.manage().timeouts()
							.pageLoadTimeout(10, TimeUnit.SECONDS);
					continue;
				}
				break;
			}
			System.out.println("Page title is: " + driver.getTitle());
			// roll the page
			JavascriptExecutor JS = (JavascriptExecutor) driver;
			try {
				JS.executeScript("scrollTo(0, 5000)");
				System.out.println("1");
				Thread.sleep(5000); // 调整休眠时间可以获取更多的内容
				JS.executeScript("scrollTo(5000, 10000)");
				System.out.println("2");
				Thread.sleep(5000);
				JS.executeScript("scrollTo(10000, 30000)"); // document.body.scrollHeight
				System.out.println("3");
				Thread.sleep(5000);
				JS.executeScript("scrollTo(10000, 80000)"); // document.body.scrollHeight
				System.out.println("4");
				Thread.sleep(5000);
			} catch (Exception e) {
				System.out.println("Error at loading the page ...");
				driver.quit();
			}
			// save page
			String html = driver.getPageSource();
			saveHtml(filepath, html);
			System.out.println("save finish");
			// Close the browser
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			driver.quit();
		} else {
			System.out.println(filepath + "已经存在，不必再次爬取...");
		}
	}

	//保存网页
	public void saveHtml(String filepath, String str) {
		try {
			OutputStreamWriter outs = new OutputStreamWriter(
					new FileOutputStream(filepath, true), "UTF-8");
			outs.write(str);
			outs.close();
		} catch (IOException e) {
			System.out.println("Error at save html...");
			e.printStackTrace();
		}
	}

	// 得到所有的关键词，输入是课程名，输出是关键词数组
	public String[] getkeyword(String course) {
		File file0 = new File("F:/术语/课程术语/" + course);
		File[] files = file0.listFiles();
		String[] keyword = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			String filename = files[i].getName();
			if (filename.contains(".html")) { // 文件夹下面不要有非.html的文件
				keyword[i] = filename.substring(0, filename.length() - 5);
//				System.out.println("keyword:" + keyword[i]);
			}
		}
		return keyword;
	}

}

/**
 * 实现Runnable接口       主题页面多线程爬虫实现
 * @author zhengtaishuai
 */
class TestThread3 implements Runnable {
	public void run() {
		pagedown();
	}
	private int i = 0;
	
	// 根据关键词数组，爬取每个关键词的主题网页
	public void pagedown() {
		String threadname = Thread.currentThread().getName();
		System.out.println(threadname + "--->> 刚进入pagedown方法...");

		String[] keyword = getkeyword("testthread");
		int length = keyword.length;
		tag2 a = new tag2();
		while (i < length) {
			String filepath = null;
			String url = null;
			synchronized (this) { // 同步，只能被一个线程占用，用于给每个关键词新建一个文件夹
				String path = a.GetCatalog(keyword[i]);
				filepath = path + keyword[i] + ".html";
				System.out.println("正在爬取关键词：" + keyword[i]);
				url = "http://www.quora.com/search?q=" + keyword[i++];
				System.out.println("url is : " + url);
				System.out.println("filepath is : " + filepath);
			}
			File file = new File(filepath);
			if (!file.exists()) {
				WebDriver driver = new InternetExplorerDriver();
				driver.manage().timeouts()
						.pageLoadTimeout(10, TimeUnit.SECONDS); // 5秒内没打开，重新加载
				while (true) {
					try {
						driver.get(url);
					} catch (Exception e) {
						driver.quit();
						driver = new InternetExplorerDriver();
						driver.manage().timeouts()
								.pageLoadTimeout(10, TimeUnit.SECONDS);
						continue;
					}
					break;
				}
				System.out.println("Page title is: " + driver.getTitle());
				// roll the page
				JavascriptExecutor JS = (JavascriptExecutor) driver;
				try {
					JS.executeScript("scrollTo(0, 5000)");
					System.out.println("1");
					Thread.sleep(5000); // 调整休眠时间可以获取更多的内容
					JS.executeScript("scrollTo(5000, 10000)");
					System.out.println("2");
					Thread.sleep(5000);
					JS.executeScript("scrollTo(10000, 30000)"); // document.body.scrollHeight
					System.out.println("3");
					Thread.sleep(5000);
					JS.executeScript("scrollTo(10000, 80000)"); // document.body.scrollHeight
					System.out.println("4");
					Thread.sleep(5000);
				} catch (Exception e) {
					System.out.println("Error at loading the page ...");
					driver.quit();
				}
				// save page
				String html = driver.getPageSource();
				saveHtml(filepath, html);
				System.out.println("save finish");
				// Close the browser
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				driver.quit();
			} else {
				System.out.println(filepath + "已经存在，不必再次爬取...");
			}
		}
		System.out.println(threadname + "--->> 离开pagedown方法...");
	}

	// 得到所有的关键词，输入是课程名，输出是关键词数组
	public String[] getkeyword(String course) {
		File file0 = new File("F:/术语/课程术语/" + course);
		File[] files = file0.listFiles();
		String[] keyword = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			String filename = files[i].getName();
			if (filename.contains(".html")) { // 文件夹下面不要有非.html的文件
				keyword[i] = filename.substring(0, filename.length() - 5);
			}
		}
		return keyword;
	}
	
	//保存网页
	public void saveHtml(String filepath, String str) {
		try {
			OutputStreamWriter outs = new OutputStreamWriter(
					new FileOutputStream(filepath, true), "UTF-8");
			outs.write(str);
			outs.close();
		} catch (IOException e) {
			System.out.println("Error at save html...");
			e.printStackTrace();
		}
	}

}
