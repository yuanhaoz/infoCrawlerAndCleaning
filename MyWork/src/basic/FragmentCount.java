package basic;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import excel.ExcelSet;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * zhengyuanhao  2015/7/8
 * 简单实现：quora  
 * 实现功能：统计知识碎片总数
 *
 */

public class FragmentCount {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			fragmentAmountAll("Data_structure");
			fragmentAmountAll("Computer_network");
			fragmentAmountAll("Data_mining");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("出错...");
		}
	}
	
	/**
	* 统计某门  "学科" 的知识碎片总数
	*/
	public static void fragmentAmountAll(String course) throws Exception {
		
		//创建表格fragmentAccount.xls用于存放某门课程下所有主题的碎片数量
		String filepath = "file/datacollection/" + course + "-fragmentAccount.xls";
//		String filepath = "file/datacollection/" + course + "-fragmentAccount(过滤后).xls";
		WritableWorkbook workbook = Workbook.createWorkbook(new File(filepath));
		WritableSheet sheet = workbook.createSheet(course + "碎片数量统计", 0);
		
		//获取课程下的主题列表
		File file = new File("file/datacollection/" + course);
		File[] files = file.listFiles();
		
		//存放每个主题的碎片数量
		int[] fragment = new int[files.length];
		
		//存放碎片总数
		int sum = 0;
		int j = -1;  //
		
		//遍历主题的数据集
		for (int i = 0; i < files.length; i++) {
			String filename = files[i].getName();
			if(!filename.contains(".")){
				j++;
				String keyword = filename;
//				System.out.println(keyword);
				
				//得到单个主题的碎片数量
				int num = fragmentAmountSingle(keyword);
				
				//数据存到链表中
				fragment[i] = num;
				sheet.addCell(new Label(0, j, convertKeyword.convert(keyword)));
				sheet.addCell(new Label(1, j, fragment[i] + ""));	
				
				//计算总的数量
				sum = sum + num;
				System.out.println(course + "包含知识碎片总数：" + sum);
			}
		}
		
		
		//表格最后一行添加该门课程的碎片总数
		sheet.addCell(new Label(0, files.length / 2, course));
		sheet.addCell(new Label(1, files.length / 2 , sum + ""));
		
		//关闭工作空间
		ExcelSet.close(workbook);
	}


	/**
	 * 统计某个  "关键词" 的知识碎片个数
	 */
	public static int fragmentAmountSingle(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.GetKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag.xls";               //过滤前
//		String path = catalog + keyword + "-tag_changed2.xls";      //过滤后
		File file = new File(path);
		System.out.println(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0);              //得到excel表格
		int rsRows = rs.getRows() - 1;
		System.out.println(keyword + "包含知识碎片数目：" + rsRows);
		return rsRows;
	}
	
	/**
	 * 统计某个  "关键词" 的知识碎片个数（输入包含课程名）
	 */
	public static int fragmentAmountSingle(String course, String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.GetKeywordCatalog(course, keyword);
		String path = catalog + keyword + "-tag.xls";
		File file = new File(path);
		System.out.println("路径为：" + path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0);              //得到excel表格
		int rsRows = rs.getRows() - 1;
		System.out.println(keyword + "包含知识碎片数目：" + rsRows);
		return rsRows;
	}
	
	
}
