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
 * ���߳�����
 * �������ʵ�֣�����ҳ��-->����ҳ��-->����ҳ��
 * @author zhengtaishuai
 */
public class PageDown {
	public static void main(String[] args) throws InterruptedException {
		TestThread2 t = new TestThread2();
		// �������ĸ��̣߳���ʵ������Դ�����Ŀ��
		new Thread(t).start();
		new Thread(t).start();
		new Thread(t).start();
	    new Thread(t).start();
	}
}

/**
 * ʵ��Runnable�ӿ�       ����ҳ����߳�����ʵ��
 * @author zhengtaishuai
 */
class TestThread2 implements Runnable {
	public void run() {
		pagedown();
	}
	private int i = 0;
	
	// ���ݹؼ������飬��ȡÿ���ؼ��ʵ�������ҳ
	public void pagedown() {
		String threadname = Thread.currentThread().getName();
		System.out.println(threadname + "--->> �ս���pagedown����...");

		String[] keyword = getkeyword("testthread");
		int length = keyword.length;
		tag2 a = new tag2();
		while (i < length) {
			String filepath = null;
			String url = null;
			synchronized (this) { // ͬ����ֻ�ܱ�һ���߳�ռ�ã����ڸ�ÿ���ؼ����½�һ���ļ���
				String path = a.GetCatalog(keyword[i]);
				filepath = path + keyword[i] + ".html";
				System.out.println("������ȡ�ؼ��ʣ�" + keyword[i]);
				url = "http://www.quora.com/search?q=" + keyword[i++]; // i++�Զ�ת������һ���ؼ���
				System.out.println("url is : " + url);
				System.out.println("filepath is : " + filepath);
			}
			downPage(filepath, url);
		}
		System.out.println(threadname + "--->> �뿪pagedown����...");
	}
	
	public void downPage(String filepath, String url){
		File file = new File(filepath);
		if (!file.exists()) {
			WebDriver driver = new InternetExplorerDriver();
			driver.manage().timeouts()
					.pageLoadTimeout(10, TimeUnit.SECONDS); // 5����û�򿪣����¼���
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
				Thread.sleep(5000); // ��������ʱ����Ի�ȡ���������
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
			System.out.println(filepath + "�Ѿ����ڣ������ٴ���ȡ...");
		}
	}

	//������ҳ
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

	// �õ����еĹؼ��ʣ������ǿγ���������ǹؼ�������
	public String[] getkeyword(String course) {
		File file0 = new File("F:/����/�γ�����/" + course);
		File[] files = file0.listFiles();
		String[] keyword = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			String filename = files[i].getName();
			if (filename.contains(".html")) { // �ļ������治Ҫ�з�.html���ļ�
				keyword[i] = filename.substring(0, filename.length() - 5);
//				System.out.println("keyword:" + keyword[i]);
			}
		}
		return keyword;
	}

}

/**
 * ʵ��Runnable�ӿ�       ����ҳ����߳�����ʵ��
 * @author zhengtaishuai
 */
class TestThread3 implements Runnable {
	public void run() {
		pagedown();
	}
	private int i = 0;
	
	// ���ݹؼ������飬��ȡÿ���ؼ��ʵ�������ҳ
	public void pagedown() {
		String threadname = Thread.currentThread().getName();
		System.out.println(threadname + "--->> �ս���pagedown����...");

		String[] keyword = getkeyword("testthread");
		int length = keyword.length;
		tag2 a = new tag2();
		while (i < length) {
			String filepath = null;
			String url = null;
			synchronized (this) { // ͬ����ֻ�ܱ�һ���߳�ռ�ã����ڸ�ÿ���ؼ����½�һ���ļ���
				String path = a.GetCatalog(keyword[i]);
				filepath = path + keyword[i] + ".html";
				System.out.println("������ȡ�ؼ��ʣ�" + keyword[i]);
				url = "http://www.quora.com/search?q=" + keyword[i++];
				System.out.println("url is : " + url);
				System.out.println("filepath is : " + filepath);
			}
			File file = new File(filepath);
			if (!file.exists()) {
				WebDriver driver = new InternetExplorerDriver();
				driver.manage().timeouts()
						.pageLoadTimeout(10, TimeUnit.SECONDS); // 5����û�򿪣����¼���
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
					Thread.sleep(5000); // ��������ʱ����Ի�ȡ���������
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
				System.out.println(filepath + "�Ѿ����ڣ������ٴ���ȡ...");
			}
		}
		System.out.println(threadname + "--->> �뿪pagedown����...");
	}

	// �õ����еĹؼ��ʣ������ǿγ���������ǹؼ�������
	public String[] getkeyword(String course) {
		File file0 = new File("F:/����/�γ�����/" + course);
		File[] files = file0.listFiles();
		String[] keyword = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			String filename = files[i].getName();
			if (filename.contains(".html")) { // �ļ������治Ҫ�з�.html���ļ�
				keyword[i] = filename.substring(0, filename.length() - 5);
			}
		}
		return keyword;
	}
	
	//������ҳ
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
