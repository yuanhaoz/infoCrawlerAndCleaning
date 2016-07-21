package webpage;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;


/**
 * 优势：可以得到答案的时间信息，并且能够调用JS操作滚动条下拉页面刷新页面
 * @author zhengtaishuai
 */
public class QuoraWebPagetest {
	private WebDriver driver = null;
	private String url = "http://www.baidu.com";
	
	public QuoraWebPagetest(){
		driver = new InternetExplorerDriver();
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		
	}
	public static void main(String[] args)  {
		QuoraWebPagetest tt = new QuoraWebPagetest();
		try {
			tt.seleniumCrawlerSubject();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void seleniumCrawlerSubject() throws InterruptedException {
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
	}
    
}



