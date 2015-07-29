package webpage;

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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;


/**
 * 优势：可以得到答案的时间信息，并且能够调用JS操作滚动条下拉页面刷新页面
 * @author zhengyuanhao
 */
@SuppressWarnings("deprecation")
public class QuoraWebPage {

	public static void main(String[] args) throws Exception {
		QuoraWebPage test = new QuoraWebPage();
		String url = "http://www.baidu.com";
		String filePath = "testdata/photo.html";
//		String filepath = "F:/photo.html";
//		test.httpClientCrawler(filepath, url);
		test.seleniumCrawlerAuthor(filePath, url);
	}
	
	/**
	 * 实现功能：爬取主题页面，输入是 “页面保存路径filepath” 和 “主题页面链接url”
	 *          下拉四次主题页面，10秒内没打开页面会重开（问题数目为49-59）
	 *          调整  下拉次数  可以得到问题数目会  增加
	 * 使用技术：Selenium
	 * @param filePath, url
	 * 
	 */
	public static void seleniumCrawlerSubject(String filePath, String url) throws InterruptedException {
		File file = new File(filePath);
		if(!file.exists()){
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
			JavascriptExecutor JS = (JavascriptExecutor) driver;
			try {
				JS.executeScript("scrollTo(0, 5000)");
				System.out.println("1");
				Thread.sleep(5000);             //调整休眠时间可以获取更多的内容
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
			saveHtml(filePath, html);
			System.out.println("save finish");
			// Close the browser
			Thread.sleep(2000);
			driver.quit();
		}else{
			System.out.println(filePath + "已经存在，不必再次爬取...");
		}
	}
	
	/**
	 * 实现功能：爬取问题页面，输入是 “页面保存路径filepath” 和 “问题页面链接url”
	 *          下拉四次主题页面，10秒内没打开页面会重开（答案数目一般都在10条以内）
	 *          调整   下拉次数    可以得到较为完整的  答案较多  的问题页面
	 * 使用技术：Selenium
	 * @param filePath, url
	 * 
	 */
	public void seleniumCrawlerQuestion(String filePath, String url) throws InterruptedException {
		File file = new File(filePath);
		if(!file.exists()){
//			System.setProperty("webdriver.firefox.bin","D:\\Program Files\\Mozilla Firefox\\firefox.exe");
//			WebDriver driver = new FirefoxDriver();
			System.out.println("hehe");
			WebDriver driver = new InternetExplorerDriver();
			System.out.println("hehe");
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
			JavascriptExecutor JS = (JavascriptExecutor) driver;
			try {
				JS.executeScript("scrollTo(0, 5000)");
				System.out.println("1");
				Thread.sleep(5000);             //调整休眠时间可以获取更多的内容
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
				e.printStackTrace();
				driver.quit();
			}
			// save page
			String html = driver.getPageSource();
			saveHtml(filePath, html);
			System.out.println("save finish");
			// Close the browser
			Thread.sleep(2000);
			driver.quit();
		}else{
			System.out.println(filePath + "已经存在，不必再次爬取...");
		}
	}
	
	/**
	 * 实现功能：爬取作者页面，输入是 “页面保存路径filepath” 和 “作者页面链接url”
	 *          10秒内没打开页面会重开
	 *          无需下来页面，所需信息都在页面顶部
	 * 使用技术：Selenium
	 * @param filePath, url
	 * 
	 */
	public void seleniumCrawlerAuthor(String filePath, String url) throws InterruptedException {
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
		// save page
		String html = driver.getPageSource();
		saveHtml(filePath, html);
		System.out.println("save finish");
		// Close the browser
		Thread.sleep(2000);
		driver.quit();
	}
	
	/**
	 * 实现功能：httpClient方法爬取所有页面
	 * 			输入是 “页面保存路径filepath” 和 “主题页面链接”
	 * 使用技术：httpClient
	 * @param filePath, url
	 * 
	 */
	public void httpClientCrawler(String filePath, String url) throws Exception{
		@SuppressWarnings("resource")
		HttpClient hc = new DefaultHttpClient();
		try
		{
			String charset = "utf-8";
			System.out.println("filepath is : " + filePath);
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
		    InputStream htmInput = null;       
		    if(entity != null){
		        htmInput = entity.getContent();
		        String htmString = inputStream2String(htmInput,charset);
		        saveHtml(filePath, htmString);      //保存文件
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
	 * 实现功能：保存html字符串流到本地html文件
	 * 			输入是 “页面保存路径filepath” 和 “html字符串源码”
	 * @param filePath, str
	 */
	public static void saveHtml(String filePath, String str) {
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
	 * 实现功能：输入流转为字符串流
	 * 			输入是 “输入流InputStream” 和 “编码方式charset”
	 * @param in_st, charset
	 */
    public static String inputStream2String(InputStream in_st,String charset) throws IOException{
        BufferedReader buff = new BufferedReader(new InputStreamReader(in_st, charset));
        StringBuffer res = new StringBuffer();
        String line = "";
        while((line = buff.readLine()) != null){
            res.append(line);
        }
        return res.toString();
    }
    
    
}


