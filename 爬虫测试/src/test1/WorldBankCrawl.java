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
 * ������ȥ�Լ���������ҳ
 * 
 */
public class WorldBankCrawl {
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		String  url = "http://www.quora.com/What-distinguishes-a-good-software-engineer-from-a-great-one";//��ɼ�����ַ
        String refer = "http://www.quora.com/";
        URL link = new URL(url); 
        WebClient wc=new WebClient();
        WebRequest request=new WebRequest(link); 
        request.setCharset("UTF-8");
        request.setProxyHost("202.117.54.39");
        request.setProxyPort(8888);
        request.setAdditionalHeader("Referer", refer);//����������ͷ���refer�ֶ�
        ////����������ͷ���User-Agent�ֶ�
        request.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
        //wc.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
        //wc.addRequestHeader��request.setAdditionalHeader����Ӧ����һ���ġ�ѡ��һ�����ɡ�
        //��������ͷ�ֶο��Ը�����Ҫ���
        wc.getCookieManager().setCookiesEnabled(true);//����cookie����
        wc.getOptions().setJavaScriptEnabled(true);//����js���������ڱ�̬��ҳ������Ǳ����
        wc.getOptions().setCssEnabled(true);//����css���������ڱ�̬��ҳ������Ǳ���ġ�
        wc.getOptions().setThrowExceptionOnFailingStatusCode(false);
        wc.getOptions().setThrowExceptionOnScriptError(false);
        wc.getOptions().setTimeout(10000);

        //ģ���������һ��Ŀ����ַ
        HtmlPage page = wc.getPage(url);       
        if(page==null)
        {
            System.out.println("�ɼ� " + url + " ʧ��!!!");
            return ;
        }
        //��ҳ���ݱ�����content��
        String content=page.asXml();
        if(content==null)
        {
            System.out.println("�ɼ� " + url + " ʧ��!!!");
            return ;
        }
        
        System.out.println("Ϊ�˻�ȡjsִ�е����� �߳̿�ʼ��˯�ȴ�");
        try {
			Thread.sleep(300000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}          //��Ҫ������̵߳ĵȴ� ��Ϊjs����Ҳ����Ҫʱ���
        System.out.println("�߳̽�����˯");
        String html = page.asXml();
           
        //System.out.println("�����ǣ�" + content);       
        //����ҳ���浽���ص�html�ļ���
        String filepath = "f:/6.html";
        saveHtml(filepath, html);     //html  content
       
	}
/*
 * ����ҳ��string�浽HTML���棬�����浽����
 */  
public static void saveHtml(String filepath, String str){        
    try {
        OutputStreamWriter outs = new OutputStreamWriter(new FileOutputStream(filepath, true), "utf-8");
        outs.write(str);
        outs.close();
        System.out.println("����ɹ�������");
    } catch (IOException e) {
        System.out.println("Error at save html...");
        e.printStackTrace();
    }
}
}

