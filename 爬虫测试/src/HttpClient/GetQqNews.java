package HttpClient;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import HttpClient.HttpConnectionManager;


/** test
 * @author lidongyang
 * @createtime Oct 23, 2012 11:05:33 AM
 */

public class GetQqNews {

    public static void main(String[] args){
        HttpConnectionManager httpConnectionManager = new HttpConnectionManager();
        String html = httpConnectionManager.getHtml("http://www.qq.com");
        Document doc = Jsoup.parse(html);
        Elements newsList = doc.select("[class=ft fl]").select("ul").select("li").select("a");
        for (Element element : newsList) {
            System.out.println(element.attr("href") + "----" + element.text());
        }
    }
    
}
