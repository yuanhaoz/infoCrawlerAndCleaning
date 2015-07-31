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
 * 优势：可以得到答案的时间信息，并且能够调用JS操作滚动条下拉页面刷新页面
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
	 * 用于爬取关键词页面，调用tag2.GetCatalog()，生成相应关键词的文件夹
	 * 中间拉动四次滚动条，7秒内没打开页面会重开
	 * 输入是关键词（2-3+heap），问题页面序号（n）和网址，输出是文件夹中的关键词页面
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
			driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);    //5秒内没打开，重新加载
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
//				Thread.sleep(5000);             //调整休眠时间可以获取更多的内容
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
			System.out.println(filepath + "已经存在，不必再次爬取...");
		}
	}
	
	
	/**
	 * 用于爬取问题页面，调用tag2.GetCatalog2()，在以有文件夹中生成后续网页
	 * 中间拉动三次滚动条，7秒内没打开页面会重开
	 * 输入是关键词（2-3+heap），问题页面序号（n）和网址，输出是文件夹中的问题网页
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
			driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);    //5秒内没打开，重新加载
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
//				Thread.sleep(5000);             //调整休眠时间可以获取更多的内容
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
			System.out.println(filepath + "已经存在，不必再次爬取...");
		}
	}
	
	/**
     * 用于爬取作者页面，用的是以前的方法，httpclient
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
    	        saveHtml(filepath, htm_str);      //保存文件
    	        System.out.println("爬取成功:" + " 网页长度为  " + entity.getContentLength());
    	    }  
    	}
    	catch(Exception err) {
    		System.err.println("爬取失败...失败原因: " + err.getMessage()); 		
    	}
    	finally {
            //关闭连接，释放资源
            hc.getConnectionManager().shutdown();
        }
	}
    
    /**
	 * 用于爬取作者页面，用的是模拟客户端的方法
	 * @author zhengtaishuai
	 */
	public void pagedown4(String keyword, String filename, String url) throws InterruptedException {
//		System.setProperty("webdriver.firefox.bin","D:\\Program Files\\Mozilla Firefox\\firefox.exe");
//		WebDriver driver = new FirefoxDriver();
		WebDriver driver = new InternetExplorerDriver();
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);    //5秒内没打开，重新加载
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
//			Thread.sleep(5000);             //调整休眠时间可以获取更多的内容
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
	 * 保存网页
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


