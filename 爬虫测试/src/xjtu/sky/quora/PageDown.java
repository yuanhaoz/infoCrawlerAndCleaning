package xjtu.sky.quora;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
//import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;


/**
 * ���ƣ����Եõ��𰸵�ʱ����Ϣ�������ܹ�����JS��������������ҳ��ˢ��ҳ��
 * @author zhengtaishuai
 */
@SuppressWarnings("deprecation")
public class PageDown {

	public static void main(String[] args) throws InterruptedException {
		PageDown test = new PageDown();
		String url = "http://byzt.xjtu.edu.cn/byxc1.htm";
		String filename = "Quora";
		test.pagedown(filename, url);
	}
	
	
	/**
	 * ������ȡ�ؼ���ҳ�棬����tag2.GetCatalog()��������Ӧ�ؼ��ʵ��ļ���
	 * �м������Ĵι�������7����û��ҳ����ؿ�
	 * �����ǹؼ��ʣ�2-3+heap��������ҳ����ţ�n������ַ��������ļ����еĹؼ���ҳ��
	 * @author zhengtaishuai
	 */
	public void pagedown(String keyword, String url) throws InterruptedException {
		tag2 a = new tag2();
		String path = a.GetCatalog(keyword);
		String filepath = path + keyword + ".html";
		System.out.println("filepath is : " + filepath);
		File file = new File(filepath);
		if(!file.exists()){
//			System.setProperty("webdriver.firefox.bin","D:\\Program Files\\Mozilla Firefox\\firefox.exe");
//			WebDriver driver = new FirefoxDriver();
			WebDriver driver = new InternetExplorerDriver();
			driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);    //5����û�򿪣����¼���
			while (true){
				try{
					driver.get(url);
				}
				catch (Exception e)
				{
					driver.quit();
					driver = new InternetExplorerDriver();
					driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
					continue;
				}
				break;
			}
			System.out.println("Page title is: " + driver.getTitle());
			// roll the page
//			JavascriptExecutor JS = (JavascriptExecutor) driver;
//			try {
//				JS.executeScript("scrollTo(0, 5000)");
//				System.out.println("1");
//				Thread.sleep(5000);             //��������ʱ����Ի�ȡ���������
//				JS.executeScript("scrollTo(5000, 10000)");
//				System.out.println("2");
//				Thread.sleep(5000);
//				JS.executeScript("scrollTo(10000, 30000)"); // document.body.scrollHeight
//				System.out.println("3");
//				Thread.sleep(5000);
//				JS.executeScript("scrollTo(10000, 80000)"); // document.body.scrollHeight
//				System.out.println("4");
//				Thread.sleep(5000);
//			} catch (Exception e) {
//				System.out.println("Error at loading the page ...");
//				driver.quit();
//			}
			// save page
			String html = driver.getPageSource();
			saveHtml(filepath, html);
			System.out.println("save finish");
			// Close the browser
			Thread.sleep(2000);
			driver.quit();
		}else{
			System.out.println(filepath + "�Ѿ����ڣ������ٴ���ȡ...");
		}
	}
	
	
	/**
	 * ������ȡ����ҳ�棬����tag2.GetCatalog2()���������ļ��������ɺ�����ҳ
	 * �м��������ι�������7����û��ҳ����ؿ�
	 * �����ǹؼ��ʣ�2-3+heap��������ҳ����ţ�n������ַ��������ļ����е�������ҳ
	 * @author zhengtaishuai
	 */
	public void pagedown2(String keyword, int n, String url) throws InterruptedException {
		tag2 a = new tag2();
		String path = a.GetCatalog2(keyword);
		String filepath = path + keyword + n + ".html";
		System.out.println("filepath is : " + filepath);
		File file = new File(filepath);
		if(!file.exists()){
//			System.setProperty("webdriver.firefox.bin","D:\\Program Files\\Mozilla Firefox\\firefox.exe");
//			WebDriver driver = new FirefoxDriver();
			WebDriver driver = new InternetExplorerDriver();
			driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);    //5����û�򿪣����¼���
			while (true){
				try{
					driver.get(url);
				}
				catch (Exception e)
				{
					driver.quit();
					driver = new InternetExplorerDriver();
					driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
					continue;
				}
				break;
			}
			System.out.println("Page title is: " + driver.getTitle());
			// roll the page
//			JavascriptExecutor JS = (JavascriptExecutor) driver;
//			try {
//				JS.executeScript("scrollTo(0, 5000)");
//				System.out.println("1");
//				Thread.sleep(5000);             //��������ʱ����Ի�ȡ���������
//				JS.executeScript("scrollTo(5000, 10000)");
//				System.out.println("2");
//				Thread.sleep(5000);
//				/*JS.executeScript("scrollTo(10000, 30000)"); // document.body.scrollHeight
//				System.out.println("3");
//				Thread.sleep(5000);
//				JS.executeScript("scrollTo(30000, 80000)"); // document.body.scrollHeight
//				System.out.println("4");
//				Thread.sleep(3000);*/
//			} catch (Exception e) {
//				System.out.println("Error at loading the page ...");
//				driver.quit();
//			}
			// save page
			String html = driver.getPageSource();
			saveHtml(filepath, html);
			System.out.println("save finish");
			// Close the browser
			Thread.sleep(2000);
			driver.quit();
		}else{
			System.out.println(filepath + "�Ѿ����ڣ������ٴ���ȡ...");
		}
	}
	
	/**
     * ������ȡ����ҳ�棬�õ�����ǰ�ķ�����httpclient
	 * @author zhengtaishuai
     */
    @SuppressWarnings({ "resource" })
	public void pagedown3(String keyword, String filename, String url) throws Exception{
    	HttpClient hc = new DefaultHttpClient();
    	try
    	{
    		String charset = "utf-8";
    		tag2 a = new tag2();
    		String path = a.GetCatalog2(keyword);
    		String filepath = path + filename;
    		System.out.println("filepath is : " + filepath);
    	    System.out.println(String.format("\nFetching %s...", url));   	        	    
    	    HttpGet hg = new HttpGet(url);     
    	    hg.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    	    hg.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
    	    hg.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:7.0.1) Gecko/20100101 Firefox/7.0.1)");
    	    hg.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
    	    hg.setHeader("Host", "www.quora.com");
            hg.setHeader("Connection", "Keep-Alive");
    	    HttpResponse response = hc.execute(hg);
    	    HttpEntity entity = response.getEntity();   	       	        
    	    InputStream htm_in = null;       
    	    if(entity != null){
    	        htm_in = entity.getContent();
    	        String htm_str = InputStream2String(htm_in,charset);
    	        saveHtml(filepath, htm_str);      //�����ļ�
    	        System.out.println("��ȡ�ɹ�:" + " ��ҳ����Ϊ  " + entity.getContentLength());
    	    }  
    	}
    	catch(Exception err) {
    		System.err.println("��ȡʧ��...ʧ��ԭ��: " + err.getMessage()); 		
    	}
    	finally {
            //�ر����ӣ��ͷ���Դ
            hc.getConnectionManager().shutdown();
        }
	}
    
    /**
	 * ������ȡ����ҳ�棬�õ���ģ��ͻ��˵ķ���
	 * @author zhengtaishuai
	 */
	public void pagedown4(String keyword, String filename, String url) throws InterruptedException {
//		System.setProperty("webdriver.firefox.bin","D:\\Program Files\\Mozilla Firefox\\firefox.exe");
//		WebDriver driver = new FirefoxDriver();
		WebDriver driver = new InternetExplorerDriver();
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);    //5����û�򿪣����¼���
		while (true){
			try{
				driver.get(url);
			}
			catch (Exception e)
			{
				driver.quit();
				driver = new InternetExplorerDriver();
				driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
				continue;
			}
			break;
		}
		System.out.println(String.format("\nFetching %s...", url));
//		System.out.println("Page title is: " + driver.getTitle());
		// roll the page
//		JavascriptExecutor JS = (JavascriptExecutor) driver;
//		try {
//			JS.executeScript("scrollTo(0, 1000)");
//			System.out.println("1");
//			Thread.sleep(5000);             //��������ʱ����Ի�ȡ���������
//		} catch (Exception e) {
//			System.out.println("Error at loading the page ...");
//			driver.quit();
//		}
		// save page
		String html = driver.getPageSource();
		tag2 a = new tag2();
		String path = a.GetCatalog2(keyword);
		String filepath = path + filename;
		System.out.println("filepath is : " + filepath);
		saveHtml(filepath, html);
		System.out.println("save finish");
		// Close the browser
		Thread.sleep(2000);
		driver.quit();
	}
	
	/**
	 * ������ҳ
	 * @author zhengtaishuai
	 */
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
	
    public static String InputStream2String(InputStream in_st,String charset) throws IOException{
        BufferedReader buff = new BufferedReader(new InputStreamReader(in_st, charset));
        StringBuffer res = new StringBuffer();
        String line = "";
        while((line = buff.readLine()) != null){
            res.append(line);
        }
        return res.toString();
    }
    
    
}


