package dataFiltering;

/**
 * zhengyuanhao  2015/6/30
 * 简单实现：quora  
 * 实现功能：数据预处理
 * 			1.关键词匹配
 * 			2.长度<500
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import excel.ExcelSet;
import basic.KeywordCatalogDesign;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class KeywordMatchOld {

	public static void main(String[] args) {
		try {
			long start = System.currentTimeMillis();
			
			KeywordMatchOld t = new KeywordMatchOld();
//			t.filtering("Computer_network");
//			t.filtering("Data_mining");
//			t.filtering("Data_structure");
//			
//			t.filtering("test");
			
			t.matchKeywordNotDelete("Binomial+distribution");
			t.matchKeywordDelete("Binomial+distribution");
			t.matchKeywordAndLength("Binomial+distribution");
			
			long end = System.currentTimeMillis();
			System.out.println("总共耗时： " + (end - start) / 1000 + "秒...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 根据关键词，处理所有Excel，匹配所有满足条件的
	 */
	public void filtering(String course) throws Exception {
		KeywordMatchOld t = new KeywordMatchOld();
		File file0 = new File("file/datacollection/" + course);
		File[] files = file0.listFiles();
		for (int i = 0; i < files.length; i++) {
			String fileName = files[i].getName();
			if(!fileName.contains(".")){
				String keyWord = fileName;
				System.out.println(keyWord);
				t.matchKeywordDelete(keyWord);
				t.matchKeywordAndLength(keyWord);
				t.matchKeywordNotDelete(keyWord);
			}
		}
	}
	
	/**
	 * 实现功能：1.处理excel表，问题和回答中只要有出现关键词，就将原Excel表中的第五列中的值 1 改为 0，
	 * 			写到一张新的Excel表中
	 * 			2.输入是信息抽取结果：-tag.xls
	 *			3.输出是主题词匹配后列值改变结果：-tag_keywordnotdelete.xls
	 * @param keyword
	 */
	public void matchKeywordNotDelete(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.GetKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0);              //得到excel表格
		int rsRows = rs.getRows();
		String[] keywordArray = keyword.split("\\+");      //将关键词以+分开存到数组里面，用于匹配文本
		System.out.println("关键词的组成有： ");
		for(int i = 0; i < keywordArray.length; i++){
			System.out.println(keywordArray[i]);
		}
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog + keyword + "-tag_keywordnotdelete.xls"));
//		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog + keyword + "-tag_changed.xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0);     //准备新建的写的excel
		for (int i = 0; i < rsRows; i++) {
			//将原来的一到三列写到新Excel中，内容不变
			sheet.setColumnView(0, 20);
			sheet.setColumnView(1, 60);
			WritableCellFormat wcf_center = ExcelSet.setCenterText();
			for (int col = 0; col < 11; col++) {
				sheet.setRowView(i, 1300, false);
				sheet.addCell(new Label(col, i, rs.getCell(col, i).getContents(), wcf_center));
			}
			sheet.addCell(new Label(12, i, rs.getCell(12, i).getContents(), wcf_center));
			//处理第11列，检查文本片段是否与关键词相关
			Cell cell = rs.getCell(1, i);
			String content = cell.getContents();
			int count = 0;
			for (int j = 0; j < keywordArray.length; j++) {
				// 在字符串中找关键词出现的次数
				Pattern p = Pattern.compile("(?i)" + keywordArray[j]);    //忽略大小写
				Matcher m = p.matcher(content);
				while (m.find()) {
					count++;
				}
			}
			System.out.println("关键词出现次数: " + count);
			if (count == 0) {
				if(i == 0){
					sheet.addCell(new Label(11, i, rs.getCell(11, 0).getContents(), wcf_center));
				}else{
					sheet.addCell(new Label(11, i, "0", wcf_center));
					System.out.println("第 " + i + " 个单元中没有出现关键词，与主题无关，将第五列标0...");
				}
			} else {
				sheet.addCell(new Label(11, i, "1", wcf_center));
				System.out.println("第 " + i + " 个单元中出现关键词，第五列值不变...");
			}
		}
		workbook.write();
		workbook.close();
		System.out.println(keyword + "处理完毕...\n");
	}


	/**
	 * 实现功能：1.处理excel表，将其中包含关键词的单元格分离到一个表格中(主题匹配结果)
	 *          去除不包含关键词的单元
	 *          2.输入是信息抽取结果：-tag.xls
	 * 			3.输出是主题词匹配（去除不含主题词的数据）的的结果：-tag_keyworddelete.xls
	 * @param keyword
	 */
	public void matchKeywordDelete(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.GetKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0);
//		int rsColumns = rs.getColumns();
		int rsRows = rs.getRows();
		String[] keywordArray = keyword.split("\\+");
		System.out.println("关键词的组成有： ");
		for(int i = 0; i < keywordArray.length; i++){
			System.out.println(keywordArray[i]);
		}
		WritableWorkbook workbook = Workbook.createWorkbook(new File(
				catalog + keyword + "-tag_keyworddelete.xls"));
//		WritableWorkbook workbook = Workbook.createWorkbook(new File(
//				catalog + keyword + "-tag_changed1.xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0);
		WritableCellFormat wcf_center = ExcelSet.setCenterText();
		int row = 0;
		for (int i = 0; i < rsRows; i++) {
			Cell cell = rs.getCell(1, i);
			String content = cell.getContents();
			int count = 0;
			for (int j = 0; j < keywordArray.length; j++) {
				// 在字符串中找关键词出现的次数
				Pattern p = Pattern.compile("(?i)" + keywordArray[j]);
				Matcher m = p.matcher(content);
				while (m.find()) {
					count++;
				}
			}
			System.out.println("出现次数: " + count);
			if (count == 0) {
				System.out.println("第 " + i + " 个单元的文本中没有出现关键词，与主题无关，去除该文本...");
			} else {
				row++;
				sheet.setColumnView(0, 20);
				sheet.setColumnView(1, 60);
				for (int col = 0; col < 11; col++) {
					sheet.setRowView(row, 1300, false);
					sheet.addCell(new Label(col, row, rs.getCell(col, i).getContents(), wcf_center));
				}
				sheet.addCell(new Label(11, row, rs.getCell(11, i).getContents(), wcf_center));
				sheet.addCell(new Label(12, row, rs.getCell(12, i).getContents(), wcf_center));
			}
		}
		
		//第一行不变
		for (int col = 0; col < 13; col++) {
			sheet.setRowView(0, 1300, false);
			sheet.addCell(new Label(col, 0, rs.getCell(col, 0).getContents(), wcf_center));
		}
		workbook.write();
		workbook.close();
	}
	
	/**
	 * 实现功能：1.处理excel表，将其中0-500单词长度的单元格分离到一个表格中
	 * 			2.输入是主题词匹配结果结果：-tag_keyworddelete.xls
	 *			3.输出是处理长度后结果：-tag_filtering.xls
	 * @param keyword
	 */
	public void matchKeywordAndLength(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.GetKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag_keyworddelete.xls";
//		String path = catalog + keyword + "-tag_changed1.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0);
//		int rsColumns = rs.getColumns();
		int rsRows = rs.getRows();
		String[] keywordArray = keyword.split("\\+");
		System.out.println("关键词的组成有： ");
		for(int i = 0; i < keywordArray.length; i++){
			System.out.println(keywordArray[i]);
		}
//		WritableWorkbook workbook = Workbook.createWorkbook(new File(
//				catalog + keyword + "-tag_changed2.xls"));
		WritableWorkbook workbook = Workbook.createWorkbook(new File(
				catalog + keyword + "-tag_filtering.xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0);
		WritableCellFormat wcf_center = ExcelSet.setCenterText();
		
		int row = -1;
		for (int i = 0; i < rsRows; i++) {
			Cell cell = rs.getCell(1, i);
			String content = cell.getContents();
			String[] contents = content.split(" ");
			int length = 0;
			if(i == 0){
				length = 1;
			}else{
				length = contents.length;
			}
			if (length > 500) {
				System.out.println("第 " + i + " 个单元的文本单词长度大于500，去除该文本...");
			} else {
				row++;
				sheet.setColumnView(0, 20);
				sheet.setColumnView(1, 60);
				for (int col = 0; col < 13; col++) {
					sheet.setRowView(row, 1300, false);
					sheet.addCell(new Label(col, row, rs.getCell(col, i).getContents(), wcf_center));
				}
			}
		}
		workbook.write();
		workbook.close();
	}

	
	/**
	 * 处理excel表，问题和回答中只要有出现关键词，就将他们写到新的excel表中，否则去掉问题和回答
	 */
	public void match2(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.GetKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0);              //得到excel表格
		int rsColumns = rs.getColumns();         //得到行数和列数
		int rsRows = rs.getRows();
		String[] keywordarray = keyword.split("\\+");      //将关键词以+分开存到数组里面，用于匹配文本
		System.out.println("关键词的组成有： ");
		for(int i = 0; i < keywordarray.length; i++){
			System.out.println(keywordarray[i]);
		}
		WritableWorkbook workbook = Workbook.createWorkbook(new File(
				catalog + keyword + "-tag_changed2.xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0);     //准备新建的写的excel
		int inital = 0;  //记录问题的位置
		int end = 0;    //记录最后一个答案的位置
		int total = Integer.parseInt(rs.getCell(12, rsRows - 1).getContents());  //该关键词记录问题的总数
		for (int t = 0; t < total; t++) {     //将每个问题的题目和回答为一整体进行分析
			int row1 = 0;   //第t个问题和答案的总数目
			int count = 0;  //记录关键词匹配成功次数
			for (int i = 0; i < rsRows; i++) {   
				int serial = Integer.parseInt(rs.getCell(12, i).getContents());
				if (serial == t) {      
					end = i;   
					row1++;          
					Cell cell = rs.getCell(1, i);
					String content = cell.getContents();
					for (int j = 0; j < keywordarray.length; j++) {
						// 在字符串中找关键词出现的次数
						Pattern p = Pattern.compile("(?i)" + keywordarray[j]);
						Matcher m = p.matcher(content);
						while (m.find()) {
							count++;
						}
					}
				}
			}
			end = end + 1;
			inital = end - row1;
			System.out.println("初始位置： " + (inital + 1) + "； 结束位置： " + end);
			System.out.println("出现次数: " + count);
			if (count == 0) {
				System.out.println("第 " + t + " 个问题和回答中没有出现关键词，与主题无关，去除该文本..." + "\n");
			} else {     //第t个问题和回答出现过关键词，所以要全部保存 
				sheet.setColumnView(0, 20);
				sheet.setColumnView(1, 60);
				for (int col = 0; col < rsColumns; col++) {   //循环将问题和回答写到excel表中
					for(int row11 = inital; row11 < end; row11++){
						sheet.setRowView(row11, 1300, false);
						WritableCellFormat wcf_center = ExcelSet.setCenterText();
						sheet.addCell(new Label(col, row11, rs.getCell(col, row11).getContents(), wcf_center));
					}
				}
				System.out.println("将相应内容写到" + (inital + 1) + "行和" + end + "行之间" + "\n");
			}
		}
		workbook.write();
		workbook.close();
	}
	
	
	/**
	 * 匹配是否存在标签
	 */
	public void match4(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.GetKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag_changed3.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0);              //得到excel表格
		int rsRows = rs.getRows();
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog + keyword + "-tag_changed4.xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0);     //准备新建的写的excel
		for (int i = 0; i < rsRows; i++) {
			//将原来的1到13列写到新Excel中，内容不变
			sheet.setColumnView(0, 20);
			sheet.setColumnView(1, 60);
			WritableCellFormat wcf_center = ExcelSet.setCenterText();
			for (int col = 0; col < 18; col++) {
				sheet.setRowView(i, 1300, false);
				sheet.addCell(new Label(col, i, rs.getCell(col, i).getContents(), wcf_center));
			}
//			Cell tag = rs.getCell(13, i);
			Cell tag1 = rs.getCell(14, i);
			Cell tag2 = rs.getCell(15, i);
			Cell tag3 = rs.getCell(16, i);
			Cell tag4 = rs.getCell(17, i);
			String[] keywordarray = {tag1.getContents(), tag2.getContents(), 
					tag3.getContents(), tag4.getContents()};
			
			//处理第13列，检查文本片段是否与关键词相关
			Cell cell = rs.getCell(1, i);
			String content = cell.getContents();
			int count = 0;
			for (int j = 0; j < keywordarray.length; j++) {
				// 在字符串中找关键词出现的次数
				if(!keywordarray[j].equals("")){
					System.out.println(keywordarray[j]);
					Pattern p = Pattern.compile("(?i)" + keywordarray[j]);    //忽略大小写
					Matcher m = p.matcher(content);
					while (m.find()) {
						count++;
					}
				}
			}
			System.out.println("关键词出现次数: " + count);
			if (count == 0) {
				sheet.addCell(new Label(18, i, "0", wcf_center));
				System.out.println("第 " + i + " 个单元中没有出现标签...");
			} else {
				sheet.addCell(new Label(18, i, "1", wcf_center));
				System.out.println("第 " + i + " 个单元中出现标签...");
			}
		}
		workbook.write();
		workbook.close();
		System.out.println(keyword + "处理完毕...\n");
	}
	
	/**
	 * 匹配是否存在标签，列的情况不同
	 */
	public void match6(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.GetKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag_changed3.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0);              //得到excel表格
		int rsRows = rs.getRows();
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog + keyword + "-tag_changed4.xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0);     //准备新建的写的excel
		for (int i = 0; i < rsRows; i++) {
			//将原来的1到13列写到新Excel中，内容不变
			sheet.setColumnView(0, 20);
			sheet.setColumnView(1, 60);
			WritableCellFormat wcf_center = ExcelSet.setCenterText();
			for (int col = 0; col < 10; col++) {
				sheet.setRowView(i, 1300, false);
				sheet.addCell(new Label(col, i, rs.getCell(col, i).getContents(), wcf_center));
			}
//			Cell tag = rs.getCell(13, i);
			Cell tag1 = rs.getCell(7, i);
			Cell tag2 = rs.getCell(8, i);
			Cell tag3 = rs.getCell(9, i);
			String[] keywordarray = {tag1.getContents(), tag2.getContents(), 
					tag3.getContents()};
			
			//处理第13列，检查文本片段是否与关键词相关
			Cell cell = rs.getCell(1, i);
			String content = cell.getContents();
			int count = 0;
			for (int j = 0; j < keywordarray.length; j++) {
				// 在字符串中找关键词出现的次数
				if(!keywordarray[j].equals("")){
					System.out.println(keywordarray[j]);
					Pattern p = Pattern.compile("(?i)" + keywordarray[j]);    //忽略大小写
					Matcher m = p.matcher(content);
					while (m.find()) {
						count++;
					}
				}
			}
			System.out.println("关键词出现次数: " + count);
			if (count == 0) {
				sheet.addCell(new Label(12, i, "0", wcf_center));
				System.out.println("第 " + i + " 个单元中没有出现标签...");
			} else {
				sheet.addCell(new Label(12, i, "1", wcf_center));
				System.out.println("第 " + i + " 个单元中出现标签...");
			}
		}
		workbook.write();
		workbook.close();
		System.out.println(keyword + "处理完毕...\n");
	}
	
	
	
	/**
	 * 经过预处理后的数据，只保留匹配后值为1的还有单词长度在0-500之间的数据
	 */
	public void match7(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.GetKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0);              //得到excel表格
		int rsRows = rs.getRows();
		String[] keywordarray = keyword.split("\\+");      //将关键词以+分开存到数组里面，用于匹配文本
		System.out.println("关键词的组成有： ");
		for(int i = 0; i < keywordarray.length; i++){
			System.out.println(keywordarray[i]);
		}
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog + keyword + "-tag_changed2.xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0);     //准备新建的写的excel
		for (int i = 0; i < rsRows; i++) {
			//将原来的1到10列写到新Excel中，内容不变
			sheet.setColumnView(0, 20);
			sheet.setColumnView(1, 60);
			WritableCellFormat wcf_center = ExcelSet.setCenterText();
			for (int col = 0; col < 10; col++) {
				sheet.setRowView(i, 1300, false);
				sheet.addCell(new Label(col, i, rs.getCell(col, i).getContents(), wcf_center));
			}
			//处理第12列，检查文本片段是否与关键词相关
			Cell cell = rs.getCell(1, i);
			String content = cell.getContents();
			int count = 0;
			for (int j = 0; j < keywordarray.length; j++) {
				// 在字符串中找关键词出现的次数
				Pattern p = Pattern.compile("(?i)" + keywordarray[j]);    //忽略大小写
				Matcher m = p.matcher(content);
				while (m.find()) {
					count++;
				}
			}
			System.out.println("关键词出现次数: " + count);
			if (count == 0) {
				if(i == 0){
					sheet.addCell(new Label(11, i, rs.getCell(4, 0).getContents(), wcf_center));
				}else{
					sheet.addCell(new Label(11, i, "0", wcf_center));
					System.out.println("第 " + i + " 个单元中没有出现关键词，与主题无关，将第五列标0...");
				}
			} else {
				sheet.addCell(new Label(11, i, "1", wcf_center));
				System.out.println("第 " + i + " 个单元中出现关键词，第五列值不变...");
			}
		}
		workbook.write();
		workbook.close();
		System.out.println(keyword + "处理完毕...\n");
	}

}