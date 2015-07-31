package z.y.h.simplestyle;

/**
 * zhengyuanhao  2015/7/28
 * 简单实现：simple style
 * 实现功能：1.主函数入口
 * 			2.爬取 simple style 上所有图片的流程
 */

public class MainFlow {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CrawlerAllPage crawler = new CrawlerAllPage();
		CatalogOperation catalog = new CatalogOperation();
		
		try {
			
			/*********************** 爬取开始 ***********************/
			catalog.setPictureCatalog();    			// 生成第一层页面目录
			crawler.crawlerParentPage();   		 		// 爬取第一层网页
			catalog.setPictureCatalogForChildPage();  	// 生成第二层页面目录
			crawler.crawlerChildPage();     			// 爬取第二层网页
			crawler.crawlerChildPageImage();     		// 爬取第二层网页中的图片
			/*********************** 爬取结束 ***********************/
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
