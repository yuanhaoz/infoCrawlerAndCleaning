package z.y.h.simplestyle;

/**
 * zhengyuanhao  2015/7/28
 * ��ʵ�֣�simple style
 * ʵ�ֹ��ܣ�1.���������
 * 			2.��ȡ simple style ������ͼƬ������
 */

public class MainFlow {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CrawlerAllPage crawler = new CrawlerAllPage();
		CatalogOperation catalog = new CatalogOperation();
		
		try {
			
			/*********************** ��ȡ��ʼ ***********************/
			catalog.setPictureCatalog();    			// ���ɵ�һ��ҳ��Ŀ¼
			crawler.crawlerParentPage();   		 		// ��ȡ��һ����ҳ
			catalog.setPictureCatalogForChildPage();  	// ���ɵڶ���ҳ��Ŀ¼
			crawler.crawlerChildPage();     			// ��ȡ�ڶ�����ҳ
			crawler.crawlerChildPageImage();     		// ��ȡ�ڶ�����ҳ�е�ͼƬ
			/*********************** ��ȡ���� ***********************/
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
