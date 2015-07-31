package selenium;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;

public class Demo0 {

	public static void main(String[] args) throws InterruptedException {
        WebDriver driver = new InternetExplorerDriver();
//        http://www.quora.com/Whats-the-dark-side-of-Silicon-Valley
//        String url = "http://www.quora.com/Why-are-trees-in-computer-science-generally-drawn-upside-down-from-how-trees-are-in-real-life";
        String url = "http://www.quora.com/search?q=binary+trees";
        driver.get(url);
        System.out.println("Page title is: " + driver.getTitle());
        WebElement element = driver.findElement(By.className("header"));
        WebElement target = driver.findElement(By.className("pagedlist_item"));  //ContentPageFeed  inline_answer_logged_out       
        WebElement target3 = driver.findElement(By.className("ContentPageFeed"));
        (new Actions(driver)).dragAndDrop(element, target).perform();
        Thread.sleep(20000);
        (new Actions(driver)).dragAndDrop(target, target3).perform();
        System.out.println("start");
        Thread.sleep(2000);
        System.out.println("finish"); 
        //获取当前网页源码
        String html = driver.getPageSource();           
        //将网页保存到本地的html文件中
        String filepath = "f:/test/c1.html";
        saveHtml(filepath, html);         
        System.out.println("byebye");                     
        //Close the browser
        //driver.quit();    
    }
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
