package xjtu.sky.quoraStudy;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Border;
import jxl.write.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@SuppressWarnings("deprecation")
public class KeyWordMatch {

	public static void main(String[] args) {
		try {
			long start = System.currentTimeMillis();
//			KeyWordMatch a = new KeyWordMatch();
//			a.match3("Circumference");
			test("test");
			long end = System.currentTimeMillis();
			System.out.println("总共耗时： " + (end - start) / 1000 + "秒...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 根据关键词，处理所有Excel，匹配所有满足条件的
	 */
	public static void test(String course) throws Exception {
		KeyWordMatch t = new KeyWordMatch();
		File file0 = new File("F:/术语/课程术语/" + course);
		File[] files = file0.listFiles();
		for (int i = 0; i < files.length; i++) {
			String filename = files[i].getName();
			if(!filename.contains(".")){
				String keyword = filename;
				System.out.println(keyword);
				t.match(keyword);
				t.match1(keyword);
				t.match3(keyword);
//				t.match6(keyword);
			}
		}
	}
	
	/**
	 * 统计知识碎片总数
	 */
	public static void test1(String course) throws Exception {
		KeyWordMatch t = new KeyWordMatch();
		File file0 = new File("F:/术语/课程术语/" + course);
		File[] files = file0.listFiles();
		int sum = 0;
		for (int i = 0; i < files.length; i++) {
			String filename = files[i].getName();
			if(!filename.contains(".")){
				String keyword = filename;
//				System.out.println(keyword);
				int num = t.match5(keyword);
				sum = sum + num;
				System.out.println("知识碎片总数：" + sum);
			}
		}
	}
	
	/**
	 * match3没有用这个目录，match1和2用的是这个目录
	 * 根据关键词所在文件夹，返回结果一般为："F:/已爬取数/2-3+heap/"
	 */
	public String GetCatalog(String keyword) {
		String catalog = null;
		File file0 = new File("F:/已爬取数/");
		File[] files = file0.listFiles();
		for (int i = 0; i < files.length; i++) {
			String name = files[i].getName();
			if(name.equals(keyword)){
				catalog = "F:/已爬取数/" + name + "/";
			}
		}
		return catalog;
	}
	
	/**
	 * 处理excel表，将其中包含关键词的单元格分离到一个表格中(主题匹配结果)
	 */
	public void match(String keyword) throws Exception {
		String catalog = tag2.GetKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0);
//		int rsColumns = rs.getColumns();
		int rsRows = rs.getRows();
		String[] keywordarray = keyword.split("\\+");
		System.out.println("关键词的组成有： ");
		for(int i = 0; i < keywordarray.length; i++){
			System.out.println(keywordarray[i]);
		}
		WritableWorkbook workbook = Workbook.createWorkbook(new File(
				catalog + keyword + "-tag_changed1.xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0);
		int row = 0;
		for (int i = 0; i < rsRows; i++) {
			Cell cell = rs.getCell(1, i);
			String content = cell.getContents();
			int count = 0;
			for (int j = 0; j < keywordarray.length; j++) {
				// 在字符串中找关键词出现的次数
				Pattern p = Pattern.compile("(?i)" + keywordarray[j]);
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
					WritableCellFormat wcf_center = set();
					sheet.addCell(new Label(col, row, rs.getCell(col, i).getContents(), wcf_center));
				}
			}
		}
		for (int col = 0; col < 11; col++) {
			sheet.setRowView(row, 1300, false);
			WritableCellFormat wcf_center = set();
			sheet.addCell(new Label(col, 0, rs.getCell(col, 0).getContents(), wcf_center));
		}
		workbook.write();
		workbook.close();
	}
	
	/**
	 * 处理excel表，将其中0-500单词长度的单元格分离到一个表格中
	 */
	public void match1(String keyword) throws Exception {
		String catalog = tag2.GetKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag_changed1.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0);
//		int rsColumns = rs.getColumns();
		int rsRows = rs.getRows();
		String[] keywordarray = keyword.split("\\+");
		System.out.println("关键词的组成有： ");
		for(int i = 0; i < keywordarray.length; i++){
			System.out.println(keywordarray[i]);
		}
		WritableWorkbook workbook = Workbook.createWorkbook(new File(
				catalog + keyword + "-tag_changed2.xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0);
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
				for (int col = 0; col < 11; col++) {
					sheet.setRowView(row, 1300, false);
					WritableCellFormat wcf_center = set();
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
		String catalog = tag2.GetKeywordCatalog(keyword);
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
						WritableCellFormat wcf_center = set();
						sheet.addCell(new Label(col, row11, rs.getCell(col, row11).getContents(), wcf_center));
					}
				}
				System.out.println("将相应内容写到" + (inital + 1) + "行和" + end + "行之间" + "\n");
			}
		}
		workbook.write(); // 写表格而不是写单元格，所以不能放在循环里面，必须放在最后
		workbook.close();
	}
	
	
	/**
	 * 处理excel表，问题和回答中只要有出现关键词，就将原Excel表中的第五列中的值 1 改为 0，写到一张新的Excel表中
	 */
	public void match3(String keyword) throws Exception {
		String catalog = tag2.GetKeywordCatalog(keyword);
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
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog + keyword + "-tag_changed.xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0);     //准备新建的写的excel
		for (int i = 0; i < rsRows; i++) {
			//将原来的一到三列写到新Excel中，内容不变
			sheet.setColumnView(0, 20);
			sheet.setColumnView(1, 60);
			WritableCellFormat wcf_center = set();
			for (int col = 0; col < 11; col++) {
				sheet.setRowView(i, 1300, false);
				sheet.addCell(new Label(col, i, rs.getCell(col, i).getContents(), wcf_center));
			}
			//处理第四列，检查文本片段是否与关键词相关
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
	 * 匹配是否存在标签
	 */
	public void match4(String keyword) throws Exception {
		String catalog = tag2.GetKeywordCatalog(keyword);
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
			WritableCellFormat wcf_center = set();
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
		String catalog = tag2.GetKeywordCatalog(keyword);
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
			WritableCellFormat wcf_center = set();
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
	 * 统计知识碎片个数
	 */
	public int match5(String keyword) throws Exception {
		String catalog = tag2.GetKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag_changed3.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0);              //得到excel表格
		int rsRows = rs.getRows() - 1;
		System.out.println(path + "包含知识碎片数目：" + rsRows);
		return rsRows;
	}
	
	/**
	 * 经过预处理后的数据，只保留匹配后值为1的还有单词长度在0-500之间的数据
	 */
	public void match7(String keyword) throws Exception {
		String catalog = tag2.GetKeywordCatalog(keyword);
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
			WritableCellFormat wcf_center = set();
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
	
	
	/**
	 * 设置Excel表格单元格格式
	 */
	public WritableCellFormat set() throws Exception{
		WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
		WritableCellFormat wcf_center = new WritableCellFormat(NormalFont);
		wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN);
		wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);
		wcf_center.setAlignment(Alignment.CENTRE);
		wcf_center.setWrap(true);
		return wcf_center;
	}
}
