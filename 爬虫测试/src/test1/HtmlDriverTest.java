package test1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * 基于HtmlDriver抓取网页内容
 *
 * @author www.yshjava.cn
 */
public class HtmlDriverTest {

    public static void main(String[] s) {
        //目标网页
        String url = "http://www.quora.com/Ive-been-in-China-for-a-year-and-keep-being-disappointed-while-doing-business-Why-cant-I-trust-any-Chinese-people";    //可以爬去search的关键词的网页
        HtmlUnitDriver driver = new HtmlUnitDriver();
        try {
            //禁用JS脚本功能
            driver.setJavascriptEnabled(false);
            //打开目标网页
            driver.get(url);
            //获取当前网页源码
            String html = driver.getPageSource();
            //打印网页源码
            System.out.println(html);
            
            //将网页保存到本地的html文件中
            String filepath = "f:/test/quora1.html";
            saveHtml(filepath, html);         
        } catch (Exception e) {
            //打印堆栈信息
            e.printStackTrace();
        } finally {
            try {
                //关闭并退出
                driver.close();
                driver.quit();
            } catch (Exception e) {
            }
        }
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
