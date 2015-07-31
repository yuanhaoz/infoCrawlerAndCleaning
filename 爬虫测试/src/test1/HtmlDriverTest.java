package test1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * ����HtmlDriverץȡ��ҳ����
 *
 * @author www.yshjava.cn
 */
public class HtmlDriverTest {

    public static void main(String[] s) {
        //Ŀ����ҳ
        String url = "http://www.quora.com/Ive-been-in-China-for-a-year-and-keep-being-disappointed-while-doing-business-Why-cant-I-trust-any-Chinese-people";    //������ȥsearch�Ĺؼ��ʵ���ҳ
        HtmlUnitDriver driver = new HtmlUnitDriver();
        try {
            //����JS�ű�����
            driver.setJavascriptEnabled(false);
            //��Ŀ����ҳ
            driver.get(url);
            //��ȡ��ǰ��ҳԴ��
            String html = driver.getPageSource();
            //��ӡ��ҳԴ��
            System.out.println(html);
            
            //����ҳ���浽���ص�html�ļ���
            String filepath = "f:/test/quora1.html";
            saveHtml(filepath, html);         
        } catch (Exception e) {
            //��ӡ��ջ��Ϣ
            e.printStackTrace();
        } finally {
            try {
                //�رղ��˳�
                driver.close();
                driver.quit();
            } catch (Exception e) {
            }
        }
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
