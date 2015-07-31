package test1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 基于HtmlUnit抓取网页内容（有问题）
 *
 * @author www.yshjava.cn
 */
public class HtmlUnitSpider {

    public static void main(String[] s) throws Exception {
        //目标网页
        String url = "http://www.quora.com/What-is-the-best-way-to-learn-something-when-you-are-tired";
        //模拟特定浏览器FIREFOX_3
        WebClient spider = new WebClient(BrowserVersion.INTERNET_EXPLORER_9);
               
       // WebClient webClient = new WebClient(BrowserVersion.FIREFOX_24);
        //设置webClient的相关参数
        spider.getOptions().setJavaScriptEnabled(true);
        spider.getOptions().setCssEnabled(false);
        spider.setAjaxController(new NicelyResynchronizingAjaxController());
        spider.getOptions().setTimeout(50000);
        spider.getOptions().setThrowExceptionOnScriptError(false);
        //模拟浏览器打开一个目标网址
        HtmlPage rootPage= spider.getPage(url);
        System.out.println("为了获取js执行的数据 线程开始沉睡等待");
        Thread.currentThread();
		Thread.sleep(1000);          //主要是这个线程的等待 因为js加载也是需要时间的
        System.out.println("线程结束沉睡");
        String html = rootPage.asXml();
        System.out.println(html);

        //将网页保存到本地的html文件中
        String filepath = "f:/a2.html";
        saveHtml(filepath, html);
        
        //打印网页内容
        //System.out.println(page.getWebResponse().getContentAsString());
        //关闭所有窗口
        spider.closeAllWindows();
    }
    /*
     * 将网页的string存到HTML里面，并保存到本地
     */
    public static void saveHtml(String filepath, String str){        
        try {
            OutputStreamWriter outs = new OutputStreamWriter(new FileOutputStream(filepath, true), "utf-8");
            outs.write(str);
            outs.close();
        } catch (IOException e) {
            System.out.println("Error at save html...");
            e.printStackTrace();
        }
    }
}