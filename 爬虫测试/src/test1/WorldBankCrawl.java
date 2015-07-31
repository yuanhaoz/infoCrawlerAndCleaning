package test1;         

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 可以爬去自己搜索的网页
 * 
 */
public class WorldBankCrawl {
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		String  url = "http://www.quora.com/What-distinguishes-a-good-software-engineer-from-a-great-one";//想采集的网址
        String refer = "http://www.quora.com/";
        URL link = new URL(url); 
        WebClient wc=new WebClient();
        WebRequest request=new WebRequest(link); 
        request.setCharset("UTF-8");
        request.setProxyHost("202.117.54.39");
        request.setProxyPort(8888);
        request.setAdditionalHeader("Referer", refer);//设置请求报文头里的refer字段
        ////设置请求报文头里的User-Agent字段
        request.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
        //wc.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
        //wc.addRequestHeader和request.setAdditionalHeader功能应该是一样的。选择一个即可。
        //其他报文头字段可以根据需要添加
        wc.getCookieManager().setCookiesEnabled(true);//开启cookie管理
        wc.getOptions().setJavaScriptEnabled(true);//开启js解析。对于变态网页，这个是必须的
        wc.getOptions().setCssEnabled(true);//开启css解析。对于变态网页，这个是必须的。
        wc.getOptions().setThrowExceptionOnFailingStatusCode(false);
        wc.getOptions().setThrowExceptionOnScriptError(false);
        wc.getOptions().setTimeout(10000);

        //模拟浏览器打开一个目标网址
        HtmlPage page = wc.getPage(url);       
        if(page==null)
        {
            System.out.println("采集 " + url + " 失败!!!");
            return ;
        }
        //网页内容保存在content里
        String content=page.asXml();
        if(content==null)
        {
            System.out.println("采集 " + url + " 失败!!!");
            return ;
        }
        
        System.out.println("为了获取js执行的数据 线程开始沉睡等待");
        try {
			Thread.sleep(300000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}          //主要是这个线程的等待 因为js加载也是需要时间的
        System.out.println("线程结束沉睡");
        String html = page.asXml();
           
        //System.out.println("内容是：" + content);       
        //将网页保存到本地的html文件中
        String filepath = "f:/6.html";
        saveHtml(filepath, html);     //html  content
       
	}
/*
 * 将网页的string存到HTML里面，并保存到本地
 */  
public static void saveHtml(String filepath, String str){        
    try {
        OutputStreamWriter outs = new OutputStreamWriter(new FileOutputStream(filepath, true), "utf-8");
        outs.write(str);
        outs.close();
        System.out.println("保存成功！！！");
    } catch (IOException e) {
        System.out.println("Error at save html...");
        e.printStackTrace();
    }
}
}

