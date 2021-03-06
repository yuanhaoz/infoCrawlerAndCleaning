package tagoperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import base.DirFile;
import basic.KeywordCatalogDesign;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import excel.ExcelSet;

public class TagClassify {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TagClassify t = new TagClassify();
		try {
			String catalog = "file/标注术语/";
			
			 //读取所有文件夹名
			ArrayList<String> a = DirFile.getFolderFileNamesFromDirectorybyArraylist(catalog); 
			Iterator<String> it = a.iterator();   //设置迭代器
			while(it.hasNext()){                  //判断是否有下一个
				long start = System.currentTimeMillis();
				String keyword = it.next();       //得到关键词
				t.tagKnowledgeGet(keyword);
				t.tagClassify(keyword);
				long end = System.currentTimeMillis();
				System.out.println("爬取" + keyword + "的所有信息用时：" + (end - start)/1000 + "秒...");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 将有标签的知识碎片分离到一个新的表格之中
	 * 输入是 keyword + -tag_changed3.xls 表，输出是 keyword + -tag_opera.xls  表
	 */
	public void tagKnowledgeGet(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.getTagKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag_changed3.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0);              //得到excel表格
		int rsRows = rs.getRows();
		int rsColumns = rs.getColumns();
		int newrow = 0;
		
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog + keyword + "-tag_opera.xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0);     //准备新建的写的excel
		WritableCellFormat wcf_center = ExcelSet.setCenterText();
		
//		标题行不变
		sheet.setColumnView(0, 20);
		sheet.setColumnView(1, 20);
		for (int col = 0; col < 15; col++) {
			sheet.addCell(new Label(col, 0, rs.getCell(col, 0).getContents(), wcf_center));
		}
		
		for (int i = 0; i < rsRows; i++) {

//			获取有标签的知识碎片
			sheet.setRowView(i, 1300, false);
			String tagable = rs.getCell(13, i).getContents();
			if(tagable.equals("1")){
				newrow++;
				for(int j = 0; j < rsColumns; j++){
					sheet.setRowView(newrow, 1300, false);
					sheet.addCell(new Label(j, newrow, rs.getCell(j, i).getContents(), wcf_center));
				}
			}
		}
		
		workbook.write();
		workbook.close();
		System.out.println(keyword + "处理完毕...\n");
	}
	
	
	/**
	 * 将可以标注的知识碎片按照不同的标签存到不同的Excel表中
	 * 输入是 keyword + -tag_opera.xls 表，输出是 keyword + “标签”  表
	 */
	public void tagClassify(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.getTagKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag_opera.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0);              //得到excel表格
		int rsRows = rs.getRows();
		int rsColumns = rs.getColumns();
		
		
		Set<String> set = new HashSet<String>();
		for (int i = 1; i < rsRows; i++) {
			
//			获取标签列表
			String tag1 = rs.getCell(14, i).getContents();
			if(!tag1.equals("")){
				set.add(tag1);
			}
			String tag2 = rs.getCell(15, i).getContents();
			if(!tag2.equals("")){
				set.add(tag2);
			}
			String tag3 = rs.getCell(16, i).getContents();
			if(!tag3.equals("")){
				set.add(tag3);
			}
			String tag4 = rs.getCell(17, i).getContents();
			if(!tag4.equals("")){
				set.add(tag4);
			}
		}
		
		
		 Iterator<String> it = set.iterator();
         while(it.hasNext()){
        	 String facet = it.next();
        	 System.out.println("分面为：" + facet);
        	 
        	 //为每个标签建立一个Excel表
        	 WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog + keyword + "-" + facet + ".xls"));
        	 WritableSheet sheet = workbook.createSheet("标签", 0);     //准备新建的写的excel
        	 WritableCellFormat wcf_center = ExcelSet.setCenterText();
        	 
//     		标题行不变
     		sheet.setColumnView(0, 20);
     		sheet.setColumnView(1, 20);
     		for (int col = 0; col < 15; col++) {
     			sheet.addCell(new Label(col, 0, rs.getCell(col, 0).getContents(), wcf_center));
     		}
     		
			int newrow = 0;
			// 判断记录出现过标签
			for (int i = 0; i < rsRows; i++) {

				// 获取标签列表
				String tag1 = rs.getCell(14, i).getContents();
				String tag2 = rs.getCell(15, i).getContents();
				String tag3 = rs.getCell(16, i).getContents();
				String tag4 = rs.getCell(17, i).getContents();
				if (facet.equals(tag1) || facet.equals(tag2) || facet.equals(tag3) || facet.equals(tag4)) {
					newrow++;
					for(int j = 0; j < rsColumns; j++){
						sheet.setRowView(newrow, 1300, false);
						sheet.addCell(new Label(j, newrow, rs.getCell(j, i).getContents(), wcf_center));
					}
				}

     		}
			
			workbook.write();
			workbook.close();
			System.out.println(keyword + "中的分面：" + facet + "处理完毕...\n");
			
         }
         
         System.out.println(keyword + "处理完毕...\n");
		
	}
	
	
}
