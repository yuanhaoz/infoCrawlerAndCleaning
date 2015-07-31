package test1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * ����HtmlUnitץȡ��ҳ���ݣ������⣩
 *
 * @author www.yshjava.cn
 */
public class HtmlUnitSpider {

    public static void main(String[] s) throws Exception {
        //Ŀ����ҳ
        String url = "http://www.quora.com/What-is-the-best-way-to-learn-something-when-you-are-tired";
        //ģ���ض������FIREFOX_3
        WebClient spider = new WebClient(BrowserVersion.INTERNET_EXPLORER_9);
               
       // WebClient webClient = new WebClient(BrowserVersion.FIREFOX_24);
        //����webClient����ز���
        spider.getOptions().setJavaScriptEnabled(true);
        spider.getOptions().setCssEnabled(false);
        spider.setAjaxController(new NicelyResynchronizingAjaxController());
        spider.getOptions().setTimeout(50000);
        spider.getOptions().setThrowExceptionOnScriptError(false);
        //ģ���������һ��Ŀ����ַ
        HtmlPage rootPage= spider.getPage(url);
        System.out.println("Ϊ�˻�ȡjsִ�е����� �߳̿�ʼ��˯�ȴ�");
        Thread.currentThread();
		Thread.sleep(1000);          //��Ҫ������̵߳ĵȴ� ��Ϊjs����Ҳ����Ҫʱ���
        System.out.println("�߳̽�����˯");
        String html = rootPage.asXml();
        System.out.println(html);

        //����ҳ���浽���ص�html�ļ���
        String filepath = "f:/a2.html";
        saveHtml(filepath, html);
        
        //��ӡ��ҳ����
        //System.out.println(page.getWebResponse().getContentAsString());
        //�ر����д���
        spider.closeAllWindows();
    }
    /*
     * ����ҳ��string�浽HTML���棬�����浽����
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