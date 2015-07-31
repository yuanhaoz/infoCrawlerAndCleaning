package z.y.h.simplestyle;

/**
 * zhengyuanhao  2015/7/28
 * 简单实现：simple style
 * 实现功能：1.爬取方法（selenium 和 Httpclient）
 * 			2.
 */

import java.io.BufferedReader;
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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

@SuppressWarnings("deprecation")
public class Crawler {

	/**
	 * 用途：爬取网站（selenium）
	 * 输入：爬取网站链接url和保存文件路径名
	 * 输出：保存到file文件夹下的html网页
	 * @author zhengyuanhao
	 */
	public void crawler(String filePath, String url) throws InterruptedException {
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
		
		// save page
		String html = driver.getPageSource();
		saveHtml(filePath, html);
		System.out.println("Crawler is finished");
		
		// Close the browser
//		Thread.sleep(1000);
		driver.quit();
	}
	
	/**
	 * 用途：爬取网站（httpClient）
	 * 输入：爬取网站链接url和保存文件路径名
	 * 输出：保存到file文件夹下的html网页
	 * @author zhengyuanhao
	 */
	public void crawlerByHttpClient(String filePath, String url) throws InterruptedException {
		@SuppressWarnings({ "resource" })
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
		    hg.setHeader("Host", "www.simple-style.com");
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
	 * 用途：输入流转为字符串流
	 * 输入：输入流InputStream和编码方式charset
	 * 输出：字符流
	 * @author zhengyuanhao
	 */
    public String inputStream2String(InputStream in_st,String charset) throws IOException{
        BufferedReader buff = new BufferedReader(new InputStreamReader(in_st, charset));
        StringBuffer res = new StringBuffer();
        String line = "";
        while((line = buff.readLine()) != null){
            res.append(line);
        }
        return res.toString();
    }
	

}
