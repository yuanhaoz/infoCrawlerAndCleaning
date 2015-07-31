package z.y.h.simplestyle;

/**
 * zhengyuanhao  2015/7/28
 * ��ʵ�֣�simple style
 * ʵ�ֹ��ܣ�1.�õ���һ��ҳ������
 * 			2.��ȡ��һ��ҳ��
 *			3.�õ��ڶ���ҳ������
 *			4.��ȡ�ڶ���ҳ�� 
 *			5.�õ��ڶ���ҳ���ͼƬ����
 *			6.��ȡ�ڶ���ҳ���ͼƬ
 */

import java.io.File;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Jsoup.JsoupParse;

public class CrawlerAllPage {

	/**
	 * ��;���õ���һ��ҳ������
	 * ���룺
	 * �������һ��ҳ������ArrayList
	 * @author zhengyuanhao
	 */
	public ArrayList<String> getParentUrl() throws Exception {
		ArrayList<String> pageUrl = new ArrayList<String>(); 
		
		// ��һ�����ӻ�����ʽ
		String originUrl = "http://www.simple-style.com/page/";
		
		// �ܹ���32���һ��ҳ��
		for (int i = 1; i < 33; i++) {
			String everyUrl = originUrl + i;
			pageUrl.add(everyUrl);
		}
		return pageUrl;
	}
	
	/**
	 * ��;����ȡ��һ��ҳ��
	 * ���룺
	 * �����ÿ����һ��Ŀ¼�µĵ�һ����ҳ
	 * @author zhengyuanhao
	 */
	public void crawlerParentPage() throws Exception {
		CatalogOperation catalog = new CatalogOperation();
		Crawler crawler = new Crawler();
		ArrayList<String> pageUrl = getParentUrl();
		for (int i = 1; i < 33; i++) {
			String parentUrl = pageUrl.get(i - 1);                         // ��һ��ҳ������
			String fileCatalog = catalog.getPictureCatalog(i);             // �ļ��洢·��
			String filePath = fileCatalog + "page" + i + ".html";          // �ļ��洢����
			File file = new File(filePath);
			if(!file.exists()){
				crawler.crawler(filePath, parentUrl);                      // ��ȡ��һ��ҳ�棨selenium��
//				crawler.crawlerByHttpClient(filePath, parentUrl);          // ��ȡ��һ��ҳ�棨httpClient��
				System.out.println(filePath + "�ѱ���...");
			} else {
				System.out.println(filePath + "�Ѵ���, ������ȡ...");
			}
			
		}
	}
	
	/**
	 * ��;���õ���n����һ��ҳ���������ҳ�������
	 * ���룺��������doc
	 * ������ڶ���ҳ��������������ArrayList
	 * @author zhengyuanhao
	 */
	public ArrayList<String> getChildUrl(Document doc) throws Exception {
		ArrayList<String> childUrl = new ArrayList<String>();
		Elements hrefs = doc.select("div#content").select("div.post").select("div.entry");  // һ��������
		for (int i = 0; i < hrefs.size(); i++) {
			Element href = hrefs.get(i);
//			String sequence = href.select("h2").attr("id");                 // ͼƬ���
//			String title = href.select("h2").select("a").text();			// ͼƬ����
			String url = href.select("h2").select("a").attr("href");		// ��ҳ������
			System.out.println("url is : " + url);
			childUrl.add(url);
		}
		return childUrl;
	}
	
	/**
	 * ��;����ȡ�ڶ���ҳ��
	 * ���룺
	 * �����ÿ���ڶ���Ŀ¼�µĵڶ�����ҳ
	 * @author zhengyuanhao
	 */
	public void crawlerChildPage() throws Exception {
		CatalogOperation catalog = new CatalogOperation();
		Crawler crawler = new Crawler();
		for (int i = 1; i < 33; i++) {
			String fileCatalog = catalog.getPictureCatalog(i);              // ��λ��һ��Ŀ¼
			String fileName = fileCatalog + "page" + i + ".html";          
			Document doc = JsoupParse.parsePathText(fileName);				// ������һ����ҳ
			ArrayList<String> childUrl = getChildUrl(doc);					// �õ����еڶ�����ҳ����
			for(int j = 0; j < childUrl.size(); j++){
				
				// �õ��ڶ���Ŀ¼  ��   �ļ�·��
				String pictureCatalog = catalog.getPictureCatalogForChildPage(i, j);
				String filePath = pictureCatalog + "page" + i + "_" + j + ".html"; 
				 
				// ��ȡ�ڶ���ҳ��
				File file = new File(filePath);
				if(!file.exists()){
					crawler.crawler(filePath, childUrl.get(j));             // selenium
//					crawler.crawlerByHttpClient(filePath, childUrl.get(j)); // httpClient
					System.out.println(filePath + "�ѱ���...");
				} else {
					System.out.println(filePath + "�Ѵ���, ������ȡ...");
				}
				
			}
		}
	}
	
	/**
	 * ��;���õ��ڶ���ҳ���ͼƬ����
	 * ���룺��������doc
	 * �����ÿ���ڶ�����ҳ�е�ͼƬ����
	 * @author zhengyuanhao
	 */
	public ArrayList<String> getImageUrl(Document doc) {
		ArrayList<String> imgUrl = new ArrayList<String>();
		Elements img = doc.select("div.entry").select("p").select("img");
		for (int i = 0; i < img.size(); i++) {
			String src = img.get(i).attr("src");
			imgUrl.add(src);
			System.out.println("image src is: " + src);
		}
		return imgUrl;
	}
	
	/**
	 * ��;����ȡ�ڶ���ҳ���ͼƬ
	 * ���룺
	 * �����ÿ���ڶ���Ŀ¼�µ�ͼƬ��Դ
	 * @author zhengyuanhao
	 */
	public void crawlerChildPageImage() throws Exception {
		CatalogOperation catalog = new CatalogOperation();
		for (int i = 1; i < 33; i++) {
			String fileCatalog = catalog.getPictureCatalog(i);              // ��λ��һ��Ŀ¼
			String fileName = fileCatalog + "page" + i + ".html";          
			Document doc = JsoupParse.parsePathText(fileName);				// ������һ����ҳ
			ArrayList<String> childUrl = getChildUrl(doc);					// �õ����еڶ�����ҳ����
			for(int j = 0; j < childUrl.size(); j++){
				
				// �õ��ڶ���Ŀ¼  ��   �ļ�·��
				String pictureCatalog = catalog.getPictureCatalogForChildPage(i, j);
				String filePath = pictureCatalog + "page" + i + "_" + j + ".html"; 
				
				// �����ڶ�����ҳ���õ�ͼƬ����
				Document docChild = JsoupParse.parsePathText(filePath);		
				ArrayList<String> imgUrl = getImageUrl(docChild);
				
				// �������ӵ�txt�ļ���
				String txtFileName = pictureCatalog + "imgUrls.txt";
				Extraction.writeImageUrlToFile(imgUrl, txtFileName);
				
				// ��ȡͼƬ
				for (int m = 0; m < imgUrl.size(); m++) {
					
					//�õ�ͼƬ���� �� ����·��
					String url = imgUrl.get(m);
					String imageFileName = pictureCatalog + "page" + i + "_" + j + "_" + m + ".jpeg";
					
					//��ȡͼƬ
					Extraction.downLoadImage(url, imageFileName);
				}
				System.out.println("page" + i + "_" + j + "  ͼƬ��ȡ����...");
			}
			System.out.println("page" + i + "  ͼƬ��ȡ����...");
		}
	}
	
	
}
