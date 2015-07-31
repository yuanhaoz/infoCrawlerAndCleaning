package z.y.h.simplestyle;

/**
 * zhengyuanhao  2015/7/28
 * ��ʵ�֣�simple style
 * ʵ�ֹ��ܣ�1.��ȡ������selenium �� Httpclient��
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
	 * ��;����ȡ��վ��selenium��
	 * ���룺��ȡ��վ����url�ͱ����ļ�·����
	 * ��������浽file�ļ����µ�html��ҳ
	 * @author zhengyuanhao
	 */
	public void crawler(String filePath, String url) throws InterruptedException {
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
		
		// save page
		String html = driver.getPageSource();
		saveHtml(filePath, html);
		System.out.println("Crawler is finished");
		
		// Close the browser
//		Thread.sleep(1000);
		driver.quit();
	}
	
	/**
	 * ��;����ȡ��վ��httpClient��
	 * ���룺��ȡ��վ����url�ͱ����ļ�·����
	 * ��������浽file�ļ����µ�html��ҳ
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
		        saveHtml(filePath, htmString);      //�����ļ�
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
	 * ��;��������תΪ�ַ�����
	 * ���룺������InputStream�ͱ��뷽ʽcharset
	 * ������ַ���
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
