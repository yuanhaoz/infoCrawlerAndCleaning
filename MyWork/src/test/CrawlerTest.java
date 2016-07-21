package test;

import webpage.QuoraWebPage;

public class CrawlerTest {

	public static void main(String[] args) throws Exception {
		
		QuoraWebPage test = new QuoraWebPage();
		String url = "https://class.coursera.org/mmds-003/lecture";
		String filePath = "testdata/mmds.html";
		test.seleniumCrawlerAuthor(filePath, url);
		
//		String filepath = "F:/photo.html";
//		test.httpClientCrawler(filepath, url);
		
	}

}
