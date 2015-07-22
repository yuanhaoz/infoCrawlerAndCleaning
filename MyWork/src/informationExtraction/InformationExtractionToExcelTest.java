package informationExtraction;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

import base.DirFile;

public class InformationExtractionToExcelTest {
	
	
	@Test
	public void extraction() throws Exception {
		long start = System.currentTimeMillis();
		 
		// 得到关键词
		String keyword = "Absolute+deviation";
		int pagelength = informationextractiontoexcel.QuestionPageNumber(keyword);
		informationextractiontoexcel.Down2Excel(keyword, pagelength); // 解析问题页面和作者页面
		
		long end = System.currentTimeMillis();
		System.out.println("解析" + keyword + "的所有信息用时：" + (end - start)/1000 + "秒...");
	}
}
