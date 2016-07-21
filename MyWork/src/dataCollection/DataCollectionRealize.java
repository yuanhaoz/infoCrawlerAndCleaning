package dataCollection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import base.DirFile;
import basic.KeywordCatalogDesign;

public class DataCollectionRealize {

	public static void main(String[] args){
		try {
			crawler("Data_structure3");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
//			System.out.println("程序结束时间：" + System.currentTimeMillis() + "秒...");
		}
//		crawlerKeyword("2-3+heap", "Data_structure2");
	}

	/**
	 * 实现功能：用于爬取某门课程所有主题下的所有网页
	 *          输入是课程名，输出是所有网页
	 * @param course
	 */
	public static void crawler(String course) throws Exception {
		String catalog = "file/datacollection/" + course;
		ArrayList<String> a = DirFile.getFileNamesFromDirectorybyArraylist(catalog);//读取所有文件名
		KeywordCatalogDesign.setKeywordCatalog(course);//为关键词建立目录
		Iterator<String> it = a.iterator();//设置迭代器
		while(it.hasNext()){//判断是否有下一个
			long start = System.currentTimeMillis();
			String keyword = it.next();//得到关键词
			DataCollection.crawlerSubjectPages(keyword);//爬取主题页面
			DataCollection.crawlerQuestionAndAuthorPages(keyword);//爬取问题页面和作者页面
			long end = System.currentTimeMillis();
			System.out.println("爬取" + keyword + "的所有信息用时：" + (end - start)/1000 + "秒...");
		}
	}
	
	/**
	 * 实现功能：用于爬取某门课程的一个主题数据
	 *          输入是主题名，输出是主题相关网页
	 * @param course
	 */
	public static void crawlerKeyword(String keyWord, String course) throws Exception {
		KeywordCatalogDesign.setKeywordCatalog(course);//为关键词建立目录
		DataCollection.crawlerSubjectPages(keyWord);//爬取主题页面
		DataCollection.crawlerQuestionAndAuthorPages(keyWord);//爬取问题页面和作者页面
		System.out.println("爬取" + keyWord + "所有信息用时...");
	}

}
