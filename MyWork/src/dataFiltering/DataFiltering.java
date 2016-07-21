package dataFiltering;

/**
 * zhengyuanhao  2015/6/30
 * 简单实现：quora  
 * 实现功能：清洗信息抽取的结果，使用主题词匹配方法
 *			"数据清洗"
 *
 */

import java.util.ArrayList;
import java.util.Iterator;

import dataFiltering.KeywordMatchNew;
import base.DirFile;

public class DataFiltering {
	
	public static void main(String[] args) throws Exception {
		realize();
	}
	
	/**
	 * 实现功能：实现数据清洗
	 * @param course
	 */
	public static void realize() throws Exception {
//		filtering("Computer_network");
//		filtering("Data_mining");
//		filtering("Data_structure");
		filtering("Data_structure_excel112");
//		filtering("Data_structure_excel59");
//		filtering("Data_structure");
	}
	
	/**
	 * 实现功能：用于清洗某门课程所有主题下的所有网页
	 *          输入是课程名，输出是Excel
	 * @param course
	 */
	public static void filtering(String course) throws Exception {
		KeywordMatchNew k = new KeywordMatchNew();
		String catalog = "file/datacollection/" + course;
		ArrayList<String> a = DirFile.getFolderFileNamesFromDirectorybyArraylist(catalog);  //读取所有文件名
		Iterator<String> it = a.iterator();   //设置迭代器
		while(it.hasNext()){                  //判断是否有下一个
			long start = System.currentTimeMillis();
			String keyword = it.next();       //得到关键词
			k.matchKeywordNotDelete(keyword);    //生成keywordnotdelete文件，保留关键词匹配结果和单词长度信息
			k.matchKeywordDelete(keyword);       //生成keyworddelete文件，只保留关键词匹配成立，单词长度未处理
			k.matchKeywordAndLength(keyword);    //生成filtering文件，只保留关键词匹配成立，且单词长度在0-500之间的
			long end = System.currentTimeMillis();
			System.out.println("解析" + keyword + "的所有信息用时：" + (end - start)/1000 + "秒...");
		}
	}
	
}
