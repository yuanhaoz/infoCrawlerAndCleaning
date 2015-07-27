package tagoperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import base.DirFile;
import basic.KeywordCatalogDesign;
import excel.ExcelSet;

public class ExampleExperiment {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ExampleExperiment t = new ExampleExperiment();
		try {
			String catalog = "file/标注术语/";
			
			 //读取所有文件夹名
			ArrayList<String> a = DirFile.getFolderFileNamesFromDirectorybyArraylist(catalog); 
			Iterator<String> it = a.iterator();   //设置迭代器
			
			while(it.hasNext()){                  //判断是否有下一个
				long start = System.currentTimeMillis();
				
				String keyword = it.next();       //得到关键词
				
				
//				t.exampleExperiment(keyword);
//				t.exampleExperiment2(keyword);
//				t.exampleExperiment3(keyword);
//				
//				t.assumption1(keyword);
//				t.assumption2(keyword);
//				t.assumption3(keyword);
//				t.assumption4(keyword);
//				t.assumption3new(keyword);
//				t.assumption4new(keyword);
//				t.assumptionAll(keyword);
				
				t.wekaFileProduceStep1(keyword);
				t.wekaFileProduceStep2(keyword);
				
				long end = System.currentTimeMillis();
				System.out.println("爬取" + keyword + "的所有信息用时：" + (end - start)/1000 + "秒...");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * weka分析文件产生函数：将标注过的数据中的Example知识碎片标注为1，用于最后的分类
	 * 输入是 keyword + -tag_changed3.xls 表，输出是 keyword + -weka.xls 表
	 */
	@Test
	public void wekaFileProduceStep1(String keyword) throws Exception {
		
		//得到关键词目录
		String catalog = KeywordCatalogDesign.getTagKeywordCatalog(keyword);
		
		//读取人工标注的表格
		String path = catalog + keyword + "-tag_changed3.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0); // 得到excel表格
		int rsRows = rs.getRows();
//		int rsColumns = rs.getColumns();
		
		// 为每个标签建立一个Excel表
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
				+ keyword + "-wekaStep1.xls"));
		WritableSheet sheet = workbook.createSheet("weka分析", 0); // 准备新建的写的excel
		WritableCellFormat wcf_center = ExcelSet.setCenterText();

		// 标题行不变
		sheet.setColumnView(0, 20);
		sheet.setColumnView(1, 20);
		for (int col = 0; col < 14; col++) {
			sheet.addCell(new Label(col, 0, rs.getCell(col, 0).getContents(), wcf_center));
		}		
		sheet.addCell(new Label(14, 0, "keywordMatch", wcf_center));
		sheet.addCell(new Label(15, 0, "tagExample", wcf_center));
		
		//将原表的内容写到新表中（前14列，除人工的标注的标签外）
		for (int col = 0; col < 14; col++) {
			for(int row = 1; row < rsRows; row++){
				sheet.setRowView(row, 1300, false);
				sheet.addCell(new Label(col, row, rs.getCell(col, row).getContents(), wcf_center));
			}
		}
		
		//用于关键词匹配
		String[] word = keyword.split("\\+");      //将关键词以+分开存到数组里面，用于匹配文本
		String[] wordexample = {"example", "instance", "sample"};      //是否存在example词
		String[] wordis = {"is", "are"};      //是否存在疑问词
		
		// 判断碎片是否存在人工标注的Example标签，如果存在将该列设为1
		for(int row = 1; row < rsRows; row++){
			
			//获取碎片内容，是否存在关键词，问题答案标签，投票数，粉丝数
			String content = rs.getCell(1, row).getContents();
			String contentmatchresult = matcher(content, word);
			String wordexamplematchresult = matcher(content, wordexample);
			String wordismatchresult = matcher(content, wordis);
			
			// 判断是否存在上面三种词汇
			if(contentmatchresult.equals("1") && wordexamplematchresult.equals("1") && wordismatchresult.equals("1")){
				sheet.addCell(new Label(14, row, "1", wcf_center));
			} else {
				sheet.addCell(new Label(14, row, "0", wcf_center));
			}
			
			//判断是否人工将其标注为1
			String tag1 = rs.getCell(14, row).getContents();
			String tag2 = rs.getCell(15, row).getContents();
			String tag3 = rs.getCell(16, row).getContents();
			String tag4 = rs.getCell(17, row).getContents();
			String example = "example";
			if(tag1.equals(example) || tag2.equals(example) || tag3.equals(example) 
					|| tag4.equals(example)){
				sheet.addCell(new Label(15, row, "1", wcf_center));
			}else{
				sheet.addCell(new Label(15, row, "0", wcf_center));
			}
		}
		
		workbook.write();
		workbook.close();

		System.out.println(keyword + "处理完毕...\n");

	}
	
	/**
	 * weka分析文件产生函数2：将我们需要的特征抽取出来
	 * 输入是 keyword + -tag_changed3.xls 表，输出是 keyword + -weka.xls 表
	 */
	@Test
	public void wekaFileProduceStep2(String keyword) throws Exception {
		
		//得到关键词目录
		String catalog = KeywordCatalogDesign.getTagKeywordCatalog(keyword);
		
		//读取人工标注的表格
		String path = catalog + keyword + "-wekaStep1.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0); // 得到excel表格
		int rsRows = rs.getRows();
//		int rsColumns = rs.getColumns();
		
		// 为每个标签建立一个Excel表
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
				+ keyword + "-wekaStep2.xls"));
		WritableSheet sheet = workbook.createSheet("weka分析", 0); // 准备新建的写的excel
		WritableCellFormat wcf_center = ExcelSet.setCenterText();

		// 判断碎片是否存在人工标注的Example标签，如果存在将该列设为1
		for(int row = 0; row < rsRows; row++){
			sheet.addCell(new Label(0, row, rs.getCell(3, row).getContents(), wcf_center));
			sheet.addCell(new Label(2, row, rs.getCell(14, row).getContents(), wcf_center));
			sheet.addCell(new Label(3, row, rs.getCell(15, row).getContents(), wcf_center));
			
			//有些用户的follower信息不存在的情况
			String follower = rs.getCell(6, row).getContents();
			if(follower.equals("")){
				sheet.addCell(new Label(1, row, "0", wcf_center));
			}else{
				sheet.addCell(new Label(1, row, follower, wcf_center));
			}
			
		}
		
		workbook.write();
		workbook.close();

		System.out.println(keyword + "处理完毕...\n");

	}
	
	/**
	 * 假设1：问题符合
	 * 将可以标注的知识碎片按照不同的标签存到不同的Excel表中 
	 * 输入是 keyword + -tag.xls 表，输出是 keyword + -tag（假设1）.xls 表
	 */
	public void assumption1(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.getTagKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0); // 得到excel表格
		int rsRows = rs.getRows();
		int rsColumns = rs.getColumns();
		
		// 为每个标签建立一个Excel表
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
				+ keyword + "-tag（假设1）.xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0); // 准备新建的写的excel
		WritableCellFormat wcf_center = ExcelSet.setCenterText();

		// 标题行不变
		sheet.setColumnView(0, 20);
		sheet.setColumnView(1, 20);
		for (int col = 0; col < rsColumns - 1; col++) {
			sheet.addCell(new Label(col, 0, rs.getCell(col, 0).getContents(), wcf_center));
		}		
		sheet.addCell(new Label(rsColumns - 1, 0, "sequence", wcf_center));
		sheet.addCell(new Label(rsColumns, 0, "基于规则的自动标签", wcf_center));
		
		//将原表的内容写到新表中
		for (int col = 0; col < rsColumns; col++) {
			for(int row = 1; row < rsRows; row++){
				sheet.setRowView(row, 1300, false);
				sheet.addCell(new Label(col, row, rs.getCell(col, row).getContents(), wcf_center));
			}
		}
		
		
		//判断规则1
		//通过自己定义的规则给问题知识碎片打Example标签
		int sum = Integer.parseInt(rs.getCell(12, rsRows - 1).getContents()) + 1;     //得到问题总数
		for (int i = 0; i < sum; i++){
			for(int j = 1; j < rsRows; j++){    // j 代表Excel表中的行数
				
				//得到每一行的第12列的值
				int sequence = Integer.parseInt(rs.getCell(12, j).getContents());
				
				//判断其是否与问题编号相同
				if(sequence == i){
					
					String qtoras = rs.getCell(2, j).getContents();
					
					String[] word = keyword.split("\\+");      //将关键词以+分开存到数组里面，用于匹配文本
					String[] wordexample = {"example", "instance", "sample"};      //是否存在example词
					String[] wordis = {"is", "are"};      //是否存在疑问词
					
					// 判断该碎片是问题
					if (qtoras.equals("1")) {
						
						//获取碎片内容，是否存在关键词，问题答案标签，投票数，粉丝数
						String content = rs.getCell(1, j).getContents();
//						String upvote = rs.getCell(3, j).getContents();
//						String follower = rs.getCell(6, j).getContents();
//						String exist = rs.getCell(11, j).getContents();
						
						String contentmatchresult = matcher(content, word);
						String wordexamplematchresult = matcher(content, wordexample);
						String wordismatchresult = matcher(content, wordis);
						
						String matchresult = "0";
						// 判断是否存在上面三种词汇
						if(contentmatchresult.equals("1") && wordexamplematchresult.equals("1") && wordismatchresult.equals("1")){
							matchresult = "1";
						} else {
							matchresult = "0";
						}
						
						// 判断问题是否同时满足上述两种条件，如果满足则将其标为example标签，再判断其答案的内容
						if(matchresult.equals("1")){
							
							//将问题标记为 Example
							sheet.addCell(new Label(13, j, "example", wcf_center));

						}
					}
					
				}
			}
			
		}
		
		workbook.write();
		workbook.close();

		System.out.println(keyword + "处理完毕...\n");

	}
	
	/**
	 * 假设2：假设1不成立的情况下直接判断回答是否存在Example关键词
	 * 将可以标注的知识碎片按照不同的标签存到不同的Excel表中 
	 * 输入是 keyword + -tag.xls 表，输出是 keyword + -tag（假设2）.xls 表
	 */
	public void assumption2(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.getTagKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0); // 得到excel表格
		int rsRows = rs.getRows();
		int rsColumns = rs.getColumns();
		
		// 为每个标签建立一个Excel表
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
				+ keyword + "-tag（假设2）.xls"));
//		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
//				+ keyword + "-tagbyruletest(测试无第二种情况).xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0); // 准备新建的写的excel
		WritableCellFormat wcf_center = ExcelSet.setCenterText();

		// 标题行不变
		sheet.setColumnView(0, 20);
		sheet.setColumnView(1, 20);
		for (int col = 0; col < rsColumns - 1; col++) {
			sheet.addCell(new Label(col, 0, rs.getCell(col, 0).getContents(), wcf_center));
		}		
		sheet.addCell(new Label(rsColumns - 1, 0, "sequence", wcf_center));
		sheet.addCell(new Label(rsColumns, 0, "基于规则的自动标签", wcf_center));
		
		//将原表的内容写到新表中
		for (int col = 0; col < rsColumns; col++) {
			for(int row = 1; row < rsRows; row++){
				sheet.setRowView(row, 1300, false);
				sheet.addCell(new Label(col, row, rs.getCell(col, row).getContents(), wcf_center));
			}
		}
		
		
		//判断规则1，问题中存在标签example和答案是否存在
		//通过自己定义的规则给知识碎片打Example的标签
		int sum = Integer.parseInt(rs.getCell(12, rsRows - 1).getContents()) + 1;     //得到问题总数
		for (int i = 0; i < sum; i++){
			for(int j = 1; j < rsRows; j++){    // j 代表Excel表中的行数
				
				//得到每一行的第12列的值
				int sequence = Integer.parseInt(rs.getCell(12, j).getContents());
				
				//判断其是否与问题编号相同
				if(sequence == i){
					
					String qtoras = rs.getCell(2, j).getContents();
					
//					String[] word = keyword.split("\\+");      //将关键词以+分开存到数组里面，用于匹配文本
					String[] wordexample = {"example", "instance", "sample"};      //是否存在example词
//					String[] wordis = {"is", "are"};      //是否存在疑问词
					
//					// 判断该碎片是问题
//					if (qtoras.equals("1")) {
//						
//						//获取碎片内容，是否存在关键词，问题答案标签，投票数，粉丝数
//						String content = rs.getCell(1, j).getContents();
//						String upvote = rs.getCell(3, j).getContents();
//						String follower = rs.getCell(6, j).getContents();
//						String exist = rs.getCell(11, j).getContents();
//						
//						String contentmatchresult = matcher(content, word);
//						String wordexamplematchresult = matcher(content, wordexample);
//						String wordismatchresult = matcher(content, wordis);
//						
//						String matchresult = "0";
//						// 判断是否存在上面三种词汇
//						if(contentmatchresult.equals("1") && wordexamplematchresult.equals("1") && wordismatchresult.equals("1")){
//							matchresult = "1";
//						} else {
//							matchresult = "0";
//						}
//						
//						// 判断问题是否同时满足上述两种条件，如果不满足直接，判断其答案是否含Example
//						if(matchresult.equals("0")){
//							
//							// 判断答案是否可以标签example
//							for(int m = 0; m < rsRows; m++){    // j 代表Excel表中的行数
//								
//								// 得到每一行的第12列的值
//								int sequence1 = Integer.parseInt(rs.getCell(12, m).getContents());
//								
//								// 判断其是否与问题编号相同
//								if(sequence1 == i){
//									
//									// 判断该碎片是答案
//									if (qtoras.equals("0")) {
//										
//										// 获取碎片内容
//										String contentanswer = rs.getCell(1, m).getContents();
//										
//										// 判断内容是否存在example标签
//										String contentanswermatchresult = matcher(contentanswer, wordexample);
//										if(contentanswermatchresult.equals("1")){
//											sheet.addCell(new Label(13, m, "example", wcf_center));
//										}
//									
//									}
//								}
//							}
//						}
//					}
					
					// 判断该碎片是答案
					if (qtoras.equals("0")) {
						
						//获取碎片内容，是否存在关键词，问题答案标签，投票数，粉丝数
						String contentanswer = rs.getCell(1, j).getContents();
						
						//判断内容是否存在example标签
						String contentanswermatchresult = matcher(contentanswer, wordexample);
						if(contentanswermatchresult.equals("1")){
							sheet.addCell(new Label(13, j, "example", wcf_center));
						}
					}
					
				}
			}
			
		}
		
		workbook.write();
		workbook.close();

		System.out.println(keyword + "处理完毕...\n");

	}
	
	/**
	 * 假设3：假设1成立的情况下，如果问题的upvote值不为0，判断回答是否存在upvote
	 * 将可以标注的知识碎片按照不同的标签存到不同的Excel表中 
	 * 输入是 keyword + -tag.xls 表，输出是 keyword + -tag（假设3）.xls 表
	 */
	public void assumption3(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.getTagKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0); // 得到excel表格
		int rsRows = rs.getRows();
		int rsColumns = rs.getColumns();
		
		// 为每个标签建立一个Excel表
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
				+ keyword + "-tag（假设3）.xls "));
//		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
//				+ keyword + "-tagbyruletest(测试无第二种情况).xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0); // 准备新建的写的excel
		WritableCellFormat wcf_center = ExcelSet.setCenterText();

		// 标题行不变
		sheet.setColumnView(0, 20);
		sheet.setColumnView(1, 20);
		for (int col = 0; col < rsColumns - 1; col++) {
			sheet.addCell(new Label(col, 0, rs.getCell(col, 0).getContents(), wcf_center));
		}		
		sheet.addCell(new Label(rsColumns - 1, 0, "sequence", wcf_center));
		sheet.addCell(new Label(rsColumns, 0, "基于规则的自动标签", wcf_center));
		
		//将原表的内容写到新表中
		for (int col = 0; col < rsColumns; col++) {
			for(int row = 1; row < rsRows; row++){
				sheet.setRowView(row, 1300, false);
				sheet.addCell(new Label(col, row, rs.getCell(col, row).getContents(), wcf_center));
			}
		}
		
		
		//判断规则1，问题中存在标签example和答案是否存在
		//通过自己定义的规则给知识碎片打Example的标签
		int sum = Integer.parseInt(rs.getCell(12, rsRows - 1).getContents()) + 1;     //得到问题总数
		for (int i = 0; i < sum; i++){
			for(int j = 1; j < rsRows; j++){    // j 代表Excel表中的行数
				
				//得到每一行的第12列的值
				int sequence = Integer.parseInt(rs.getCell(12, j).getContents());
				
				//判断其是否与问题编号相同
				if(sequence == i){
					
					String qtoras = rs.getCell(2, j).getContents();
					
					String[] word = keyword.split("\\+");      //将关键词以+分开存到数组里面，用于匹配文本
					String[] wordexample = {"example", "instance", "sample"};      //是否存在example词
					String[] wordis = {"is", "are"};      //是否存在疑问词
					
					// 判断该碎片是问题
					if (qtoras.equals("1")) {
						
						//获取碎片内容，是否存在关键词，问题答案标签，投票数，粉丝数
						String content = rs.getCell(1, j).getContents();
						String upvote = rs.getCell(3, j).getContents();
//						String follower = rs.getCell(6, j).getContents();
//						String exist = rs.getCell(11, j).getContents();
						
						String contentmatchresult = matcher(content, word);
						String wordexamplematchresult = matcher(content, wordexample);
						String wordismatchresult = matcher(content, wordis);
						
						String matchresult = "0";
						// 判断是否存在上面三种词汇
						if(contentmatchresult.equals("1") && wordexamplematchresult.equals("1") && wordismatchresult.equals("1")){
							matchresult = "1";
						} else {
							matchresult = "0";
						}
						
						String upvoteresult = "0";
						// 判断问题是否存在upvote数
						if(!upvote.equals("0")){
							upvoteresult = "1";
						} else {
							upvoteresult = "0";
						}
						
						// 判断问题是否同时满足上述两种条件，如果满足则将其标为example标签，再判断其答案的内容
						if(matchresult.equals("1") && upvoteresult.equals("1")){
							
							//将问题标记为 Example
							sheet.addCell(new Label(13, j, "example", wcf_center));
							
							//判断答案是否可以标签example
							for(int m = 1; m < rsRows; m++){    // j 代表Excel表中的行数
								
								//得到每一行的第12列的值
								int sequence1 = Integer.parseInt(rs.getCell(12, m).getContents());
								
								//判断其是否与问题编号相同
								if(sequence1 == i){
									
									// 判断该碎片是答案
									if (qtoras.equals("0")) {
										
										//获取投票数
										String upvoteanswer = rs.getCell(3, m).getContents();
										
										//判断答案upvote值是否为0
										if(!upvoteanswer.equals("0")){										
											sheet.addCell(new Label(13, m, "example", wcf_center));
										}
									
									}
								}
							}
						}
					}
					
				}
			}
			
		}
		
		workbook.write();
		workbook.close();

		System.out.println(keyword + "处理完毕...\n");

	}
	
	/**
	 * 假设4：假设1成立的情况下，如果问题的follower值不为0，判断回答是否存在upvote
	 * 将可以标注的知识碎片按照不同的标签存到不同的Excel表中 
	 * 输入是 keyword + -tag.xls 表，输出是 keyword + -tag（假设4）.xls 表
	 */
	public void assumption4(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.getTagKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0); // 得到excel表格
		int rsRows = rs.getRows();
		int rsColumns = rs.getColumns();
		
		// 为每个标签建立一个Excel表
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
				+ keyword + "-tag（假设4）.xls"));
//		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
//				+ keyword + "-tagbyruletest(测试无第二种情况).xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0); // 准备新建的写的excel
		WritableCellFormat wcf_center = ExcelSet.setCenterText();

		// 标题行不变
		sheet.setColumnView(0, 20);
		sheet.setColumnView(1, 20);
		for (int col = 0; col < rsColumns - 1; col++) {
			sheet.addCell(new Label(col, 0, rs.getCell(col, 0).getContents(), wcf_center));
		}		
		sheet.addCell(new Label(rsColumns - 1, 0, "sequence", wcf_center));
		sheet.addCell(new Label(rsColumns, 0, "基于规则的自动标签", wcf_center));
		
		//将原表的内容写到新表中
		for (int col = 0; col < rsColumns; col++) {
			for(int row = 1; row < rsRows; row++){
				sheet.setRowView(row, 1300, false);
				sheet.addCell(new Label(col, row, rs.getCell(col, row).getContents(), wcf_center));
			}
		}
		
		
		//判断规则1，问题中存在标签example和答案是否存在
		//通过自己定义的规则给知识碎片打Example的标签
		int sum = Integer.parseInt(rs.getCell(12, rsRows - 1).getContents()) + 1;     //得到问题总数
		for (int i = 0; i < sum; i++){
			for(int j = 1; j < rsRows; j++){    // j 代表Excel表中的行数
				
				//得到每一行的第12列的值
				int sequence = Integer.parseInt(rs.getCell(12, j).getContents());
				
				//判断其是否与问题编号相同
				if(sequence == i){
					
					String qtoras = rs.getCell(2, j).getContents();
					
					String[] word = keyword.split("\\+");      //将关键词以+分开存到数组里面，用于匹配文本
					String[] wordexample = {"example", "instance", "sample"};      //是否存在example词
					String[] wordis = {"is", "are"};      //是否存在疑问词
					
					// 判断该碎片是问题
					if (qtoras.equals("1")) {
						
						//获取碎片内容，是否存在关键词，问题答案标签，投票数，粉丝数
						String content = rs.getCell(1, j).getContents();
//						String upvote = rs.getCell(3, j).getContents();
						String follower = rs.getCell(6, j).getContents();
//						String exist = rs.getCell(11, j).getContents();
						
						String contentmatchresult = matcher(content, word);
						String wordexamplematchresult = matcher(content, wordexample);
						String wordismatchresult = matcher(content, wordis);
						
						String matchresult = "0";
						// 判断是否存在上面三种词汇
						if(contentmatchresult.equals("1") && wordexamplematchresult.equals("1") && wordismatchresult.equals("1")){
							matchresult = "1";
						} else {
							matchresult = "0";
						}
						
						String followerresult = "0";
						// 判断问题是否存在follower数
						if(!follower.equals("0")){
							followerresult = "1";
						} else {
							followerresult = "0";
						}
						
						// 判断问题是否同时满足上述两种条件，如果满足则将其标为example标签，再判断其答案的内容
						if(matchresult.equals("1") && followerresult.equals("1")){
							
							// 将问题标记为 Example
							sheet.addCell(new Label(13, j, "example", wcf_center));
							
							// 判断答案是否可以标签example
							for(int m = 0; m < rsRows; m++){    // j 代表Excel表中的行数
								
								// 得到每一行的第12列的值
								int sequence1 = Integer.parseInt(rs.getCell(12, m).getContents());
								
								// 判断其是否与问题编号相同
								if(sequence1 == i){
									
									// 判断该碎片是答案
									if (qtoras.equals("0")) {
										
										// 获取粉丝数
										String followeranswer = rs.getCell(6, m).getContents();
										
										// 判断内容是否存在example标签
										if(!followeranswer.equals("0")){										
											sheet.addCell(new Label(13, m, "example", wcf_center));
										}
									
									}
								}
							}
						}
					}
					
				}
			}
			
		}
		
		workbook.write();
		workbook.close();

		System.out.println(keyword + "处理完毕...\n");

	}
	
	/**
	 * 假设总体分析：将四个假设放在一起考虑
	 * 将可以标注的知识碎片按照不同的标签存到不同的Excel表中 
	 * 输入是 keyword + -tag.xls 表，输出是 keyword + -tag（总假设）.xls 表
	 */
	public void assumptionAll(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.getTagKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0); // 得到excel表格
		int rsRows = rs.getRows();
		int rsColumns = rs.getColumns();
		
		// 为每个标签建立一个Excel表
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
				+ keyword + "-tagbyrule(总假设).xls"));
//		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
//				+ keyword + "-tagbyruletest(测试无第二种情况).xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0); // 准备新建的写的excel
		WritableCellFormat wcf_center = ExcelSet.setCenterText();

		// 标题行不变
		sheet.setColumnView(0, 20);
		sheet.setColumnView(1, 20);
		for (int col = 0; col < rsColumns - 1; col++) {
			sheet.addCell(new Label(col, 0, rs.getCell(col, 0).getContents(), wcf_center));
		}		
		sheet.addCell(new Label(rsColumns - 1, 0, "sequence", wcf_center));
		sheet.addCell(new Label(rsColumns, 0, "基于规则的自动标签", wcf_center));
		
		//将原表的内容写到新表中
		for (int col = 0; col < rsColumns; col++) {
			for(int row = 1; row < rsRows; row++){
				sheet.setRowView(row, 1300, false);
				sheet.addCell(new Label(col, row, rs.getCell(col, row).getContents(), wcf_center));
			}
		}
		
		
		//判断规则1，问题中存在标签example和答案是否存在
		//通过自己定义的规则给知识碎片打Example的标签
		int sum = Integer.parseInt(rs.getCell(12, rsRows - 1).getContents()) + 1;     //得到问题总数
		for (int i = 0; i < sum; i++){
			for(int j = 1; j < rsRows; j++){    // j 代表Excel表中的行数
				
				//得到每一行的第12列的值
				int sequence = Integer.parseInt(rs.getCell(12, j).getContents());
				
				//判断其是否与问题编号相同
				if(sequence == i){
					
					String qtoras = rs.getCell(2, j).getContents();
					
					String[] word = keyword.split("\\+");      //将关键词以+分开存到数组里面，用于匹配文本
					String[] wordexample = {"example", "instance", "sample"};      //是否存在example词
					String[] wordis = {"is", "are"};      //是否存在疑问词
					
					// 判断该碎片是问题
					if (qtoras.equals("1")) {
						
						//获取碎片内容，是否存在关键词，问题答案标签，投票数，粉丝数
						String content = rs.getCell(1, j).getContents();
						String upvote = rs.getCell(3, j).getContents();
						String follower = rs.getCell(6, j).getContents();
//						String exist = rs.getCell(11, j).getContents();
						
						String contentmatchresult = matcher(content, word);
						String wordexamplematchresult = matcher(content, wordexample);
						String wordismatchresult = matcher(content, wordis);
						
						String matchresult = "0";
						// 判断是否存在上面三种词汇
						if(contentmatchresult.equals("1") && wordexamplematchresult.equals("1") && wordismatchresult.equals("1")){
							matchresult = "1";
						} else {
							matchresult = "0";
						}
						
						String upvoteandfollowerresult = "0";
						// 判断问题是否存在upvote/follower数
						if(!upvote.equals("0") && !follower.equals("0")){
							upvoteandfollowerresult = "1";
						} else {
							upvoteandfollowerresult = "0";
						}
						
						// 判断问题是否同时满足上述两种条件，如果满足则将其标为example标签，再判断其答案的内容
						if(matchresult.equals("1") && upvoteandfollowerresult.equals("1")){
							sheet.addCell(new Label(13, j, "example", wcf_center));
							
							//判断答案是否可以标签example
							for(int m = 0; m < rsRows; m++){    // j 代表Excel表中的行数
								
								//得到每一行的第12列的值
								int sequence1 = Integer.parseInt(rs.getCell(12, m).getContents());
								
								//判断其是否与问题编号相同
								if(sequence1 == i){
									
									// 判断该碎片是答案
									if (qtoras.equals("0")) {
										
										//获取投票数，粉丝数
										String upvoteanswer = rs.getCell(3, m).getContents();
										String followeranswer = rs.getCell(6, m).getContents();
										
										//判断内容是否存在example标签
										if(!upvoteanswer.equals("0") || !followeranswer.equals("0")){										
											sheet.addCell(new Label(13, m, "example", wcf_center));
										}
									
									}
								}
							}
						}
						
						
						
					}
					
					
					// 判断该碎片是答案
					if (qtoras.equals("0")) {
						
						//获取碎片内容，是否存在关键词，问题答案标签，投票数，粉丝数
						String contentanswer = rs.getCell(1, j).getContents();
						
						//判断内容是否存在example标签
						String contentanswermatchresult = matcher(contentanswer, wordexample);
						if(contentanswermatchresult.equals("1")){
							sheet.addCell(new Label(13, j, "example", wcf_center));
						}
					}
					
					
				}
			}
			
			
			
			
		}
		
		workbook.write();
		workbook.close();

		System.out.println(keyword + "处理完毕...\n");

	}
	
	/**
	 * 假设3和假设4：分别处理
	 * 将可以标注的知识碎片按照不同的标签存到不同的Excel表中 
	 * 输入是 keyword +-tag_changed3（新标准）.xls 表，输出是 keyword + -tag（新假设3、4）.xls 表
	 */
	public void assumption3New(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.getTagKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag_changed3（新标准）.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0); // 得到excel表格
		int rsRows = rs.getRows();
//		int rsColumns = rs.getColumns();
		
		// 为每个标签建立一个Excel表
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
				+ keyword + "-tag（新假设3）.xls "));
//		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
//				+ keyword + "-tagbyruletest(测试无第二种情况).xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0); // 准备新建的写的excel
		WritableCellFormat wcf_center = ExcelSet.setCenterText();

		// 标题行不变
		sheet.setColumnView(0, 20);
		sheet.setColumnView(1, 20);
		for (int col = 0; col < 14; col++) {
			sheet.addCell(new Label(col, 0, rs.getCell(col, 0).getContents(), wcf_center));
		}		
		sheet.addCell(new Label(14, 0, "基于规则的自动标签", wcf_center));
		
		//将原表的内容写到新表中
		for (int col = 0; col < 14; col++) {
			for(int row = 1; row < rsRows; row++){
				sheet.setRowView(row, 1300, false);
				sheet.addCell(new Label(col, row, rs.getCell(col, row).getContents(), wcf_center));
			}
		}
		
		//判断是否存在upvote值
		for(int i = 1; i < rsRows; i++){
			String upvote = rs.getCell(3, i).getContents();
			String upvoteresult = "0";
			// 判断问题是否存在upvoter数
			if(!upvote.equals("0")){
				upvoteresult = "1";
			} else {
				upvoteresult = "0";
			}
			if(!upvoteresult.equals("0")){										
				sheet.addCell(new Label(14, i, "example", wcf_center));
			}
		}
		
		workbook.write();
		workbook.close();

		System.out.println(keyword + "处理完毕...\n");

	}
	
	/**
	 * 假设3和假设4：分别处理
	 * 将可以标注的知识碎片按照不同的标签存到不同的Excel表中 
	 * 输入是 keyword +-tag_changed3（新标准）.xls 表，输出是 keyword + -tag（新假设3、4）.xls 表
	 */
	public void assumption4New(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.getTagKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag_changed3（新标准）.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0); // 得到excel表格
		int rsRows = rs.getRows();
//		int rsColumns = rs.getColumns();
		
		// 为每个标签建立一个Excel表
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
				+ keyword + "-tag（新假设4）.xls "));
//		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
//				+ keyword + "-tagbyruletest(测试无第二种情况).xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0); // 准备新建的写的excel
		WritableCellFormat wcf_center = ExcelSet.setCenterText();

		// 标题行不变
		sheet.setColumnView(0, 20);
		sheet.setColumnView(1, 20);
		for (int col = 0; col < 14; col++) {
			sheet.addCell(new Label(col, 0, rs.getCell(col, 0).getContents(), wcf_center));
		}		
		sheet.addCell(new Label(14, 0, "基于规则的自动标签", wcf_center));
		
		//将原表的内容写到新表中
		for (int col = 0; col < 14; col++) {
			for(int row = 1; row < rsRows; row++){
				sheet.setRowView(row, 1300, false);
				sheet.addCell(new Label(col, row, rs.getCell(col, row).getContents(), wcf_center));
			}
		}
		
		//判断是否存在follower值
		for(int i = 1; i < rsRows; i++){
			String fillower = rs.getCell(6, i).getContents();
			String followerresult = "0";
			// 判断问题是否存在upvoter数
			if(!fillower.equals("0")){
				followerresult = "1";
			} else {
				followerresult = "0";
			}
			if(!followerresult.equals("0")){										
				sheet.addCell(new Label(14, i, "example", wcf_center));
			}
		}
		
		workbook.write();
		workbook.close();

		System.out.println(keyword + "处理完毕...\n");

	}

	/**
	 * upvote
	 * 将可以标注的知识碎片按照不同的标签存到不同的Excel表中 
	 * 输入是 keyword + -tag_opera.xls 表，输出是 keyword
	 * + “标签” 表
	 */
	public void exampleExperiment(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.getTagKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0); // 得到excel表格
		int rsRows = rs.getRows();
		int rsColumns = rs.getColumns();
		
		// 为每个标签建立一个Excel表
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
				+ keyword + "-tagbyrule(upvote).xls"));
//		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
//				+ keyword + "-tagbyruletest(测试无第二种情况).xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0); // 准备新建的写的excel
		WritableCellFormat wcf_center = ExcelSet.setCenterText();

		// 标题行不变
		sheet.setColumnView(0, 20);
		sheet.setColumnView(1, 20);
		for (int col = 0; col < rsColumns - 1; col++) {
			sheet.addCell(new Label(col, 0, rs.getCell(col, 0).getContents(), wcf_center));
		}		
		sheet.addCell(new Label(rsColumns - 1, 0, "sequence", wcf_center));
		sheet.addCell(new Label(rsColumns, 0, "基于规则的自动标签", wcf_center));
		
		//将原表的内容写到新表中
		for (int col = 0; col < rsColumns; col++) {
			for(int row = 1; row < rsRows; row++){
				sheet.setRowView(row, 1300, false);
				sheet.addCell(new Label(col, row, rs.getCell(col, row).getContents(), wcf_center));
			}
		}
		
		
		//判断规则1，问题中存在标签example和答案是否存在
		//通过自己定义的规则给知识碎片打Example的标签
		int sum = Integer.parseInt(rs.getCell(12, rsRows - 1).getContents()) + 1;     //得到问题总数
		for (int i = 0; i < sum; i++){
			for(int j = 1; j < rsRows; j++){    // j 代表Excel表中的行数
				
				//得到每一行的第12列的值
				int sequence = Integer.parseInt(rs.getCell(12, j).getContents());
				
				//判断其是否与问题编号相同
				if(sequence == i){
					
					String qtoras = rs.getCell(2, j).getContents();
					
					String[] word = keyword.split("\\+");      //将关键词以+分开存到数组里面，用于匹配文本
					String[] wordexample = {"example", "instance", "sample"};      //是否存在example词
					String[] wordis = {"is", "are"};      //是否存在疑问词
					
					// 判断该碎片是问题
					if (qtoras.equals("1")) {
						
						//获取碎片内容，是否存在关键词，问题答案标签，投票数，粉丝数
						String content = rs.getCell(1, j).getContents();
						String upvote = rs.getCell(3, j).getContents();
//						String follower = rs.getCell(6, j).getContents();
//						String exist = rs.getCell(11, j).getContents();
						
						String contentmatchresult = matcher(content, word);
						String wordexamplematchresult = matcher(content, wordexample);
						String wordismatchresult = matcher(content, wordis);
						
						String matchresult = "0";
						// 判断是否存在上面三种词汇
						if(contentmatchresult.equals("1") && wordexamplematchresult.equals("1") && wordismatchresult.equals("1")){
							matchresult = "1";
						} else {
							matchresult = "0";
						}
						
						String upvoteresult = "0";
						// 判断问题是否存在upvote/follower数
						if(!upvote.equals("0")){
							upvoteresult = "1";
						} else {
							upvoteresult = "0";
						}
						
						// 判断问题是否同时满足上述两种条件，如果满足则将其标为example标签，再判断其答案的内容
						if(matchresult.equals("1") && upvoteresult.equals("1")){
							sheet.addCell(new Label(13, j, "example", wcf_center));
							
							//判断答案是否可以标签example
							for(int m = 1; m < rsRows; m++){    // j 代表Excel表中的行数
								
								//得到每一行的第12列的值
								int sequence1 = Integer.parseInt(rs.getCell(12, m).getContents());
								
								//判断其是否与问题编号相同
								if(sequence1 == i){
									
									// 判断该碎片是答案
									if (qtoras.equals("0")) {
										
										//获取碎片内容，是否存在关键词，问题答案标签，投票数，粉丝数
										String contentanswer = rs.getCell(1, m).getContents();
										String upvoteanswer = rs.getCell(3, m).getContents();
//										String followeranswer = rs.getCell(6, m).getContents();
										
										//判断内容是否存在example标签
										String contentanswermatchresult = matcher(contentanswer, wordexample);
										if(contentanswermatchresult.equals("1")){
											sheet.addCell(new Label(13, m, "example", wcf_center));
										} else if(!upvoteanswer.equals("0")){										
											sheet.addCell(new Label(13, m, "example", wcf_center));
										}
									
									}
								}
							}
						}
					}
					
					// 判断该碎片是答案
					if (qtoras.equals("0")) {
						
						//获取碎片内容，是否存在关键词，问题答案标签，投票数，粉丝数
						String contentanswer = rs.getCell(1, j).getContents();
//						String upvoteanswer = rs.getCell(3, j).getContents();
//						String followeranswer = rs.getCell(6, j).getContents();
						
						//判断内容是否存在example标签
						String contentanswermatchresult = matcher(contentanswer, wordexample);
						if(contentanswermatchresult.equals("1")){
							sheet.addCell(new Label(13, j, "example", wcf_center));
						}
					}
					
				}
			}
			
			
			
			
		}
		
		workbook.write();
		workbook.close();

		System.out.println(keyword + "处理完毕...\n");

	}
	
	
	
	/**
	 * follower
	 * 将可以标注的知识碎片按照不同的标签存到不同的Excel表中 
	 * 输入是 keyword + -tag_opera.xls 表，输出是 keyword
	 * + “标签” 表
	 */
	public void exampleExperiment2(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.getTagKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0); // 得到excel表格
		int rsRows = rs.getRows();
		int rsColumns = rs.getColumns();
		
		// 为每个标签建立一个Excel表
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
				+ keyword + "-tagbyrule(follower).xls"));
//		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
//				+ keyword + "-tagbyruletest(测试无第二种情况).xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0); // 准备新建的写的excel
		WritableCellFormat wcf_center = ExcelSet.setCenterText();

		// 标题行不变
		sheet.setColumnView(0, 20);
		sheet.setColumnView(1, 20);
		for (int col = 0; col < rsColumns - 1; col++) {
			sheet.addCell(new Label(col, 0, rs.getCell(col, 0).getContents(), wcf_center));
		}		
		sheet.addCell(new Label(rsColumns - 1, 0, "sequence", wcf_center));
		sheet.addCell(new Label(rsColumns, 0, "基于规则的自动标签", wcf_center));
		
		//将原表的内容写到新表中
		for (int col = 0; col < rsColumns; col++) {
			for(int row = 1; row < rsRows; row++){
				sheet.setRowView(row, 1300, false);
				sheet.addCell(new Label(col, row, rs.getCell(col, row).getContents(), wcf_center));
			}
		}
		
		
		//判断规则1，问题中存在标签example和答案是否存在
		//通过自己定义的规则给知识碎片打Example的标签
		int sum = Integer.parseInt(rs.getCell(12, rsRows - 1).getContents()) + 1;     //得到问题总数
		for (int i = 0; i < sum; i++){
			for(int j = 1; j < rsRows; j++){    // j 代表Excel表中的行数
				
				//得到每一行的第12列的值
				int sequence = Integer.parseInt(rs.getCell(12, j).getContents());
				
				//判断其是否与问题编号相同
				if(sequence == i){
					
					String qtoras = rs.getCell(2, j).getContents();
					
					String[] word = keyword.split("\\+");      //将关键词以+分开存到数组里面，用于匹配文本
					String[] wordexample = {"example", "instance", "sample"};      //是否存在example词
					String[] wordis = {"is", "are"};      //是否存在疑问词
					
					// 判断该碎片是问题
					if (qtoras.equals("1")) {
						
						//获取碎片内容，是否存在关键词，问题答案标签，投票数，粉丝数
						String content = rs.getCell(1, j).getContents();
//						String upvote = rs.getCell(3, j).getContents();
						String follower = rs.getCell(6, j).getContents();
//						String exist = rs.getCell(11, j).getContents();
						
						String contentmatchresult = matcher(content, word);
						String wordexamplematchresult = matcher(content, wordexample);
						String wordismatchresult = matcher(content, wordis);
						
						String matchresult = "0";
						// 判断是否存在上面三种词汇
						if(contentmatchresult.equals("1") && wordexamplematchresult.equals("1") && wordismatchresult.equals("1")){
							matchresult = "1";
						} else {
							matchresult = "0";
						}
						
						String followerresult = "0";
						// 判断问题是否存在upvote/follower数
						if(!follower.equals("0")){
							followerresult = "1";
						} else {
							followerresult = "0";
						}
						
						// 判断问题是否同时满足上述两种条件，如果满足则将其标为example标签，再判断其答案的内容
						if(matchresult.equals("1") && followerresult.equals("1")){
							sheet.addCell(new Label(13, j, "example", wcf_center));
							
							//判断答案是否可以标签example
							for(int m = 0; m < rsRows; m++){    // j 代表Excel表中的行数
								
								//得到每一行的第12列的值
								int sequence1 = Integer.parseInt(rs.getCell(12, m).getContents());
								
								//判断其是否与问题编号相同
								if(sequence1 == i){
									
									// 判断该碎片是答案
									if (qtoras.equals("0")) {
										
										//获取碎片内容，是否存在关键词，问题答案标签，投票数，粉丝数
										String contentanswer = rs.getCell(1, m).getContents();
										String upvoteanswer = rs.getCell(3, m).getContents();
//										String followeranswer = rs.getCell(6, m).getContents();
										
										//判断内容是否存在example标签
										String contentanswermatchresult = matcher(contentanswer, wordexample);
										if(contentanswermatchresult.equals("1")){
											sheet.addCell(new Label(13, m, "example", wcf_center));
										} else if(!upvoteanswer.equals("0")){										
											sheet.addCell(new Label(13, m, "example", wcf_center));
										}
									
									}
								}
							}
						}
					}
					
					// 判断该碎片是答案
					if (qtoras.equals("0")) {
						
						//获取碎片内容，是否存在关键词，问题答案标签，投票数，粉丝数
						String contentanswer = rs.getCell(1, j).getContents();
//						String upvoteanswer = rs.getCell(3, j).getContents();
//						String followeranswer = rs.getCell(6, j).getContents();
						
						//判断内容是否存在example标签
						String contentanswermatchresult = matcher(contentanswer, wordexample);
						if(contentanswermatchresult.equals("1")){
							sheet.addCell(new Label(13, j, "example", wcf_center));
						}
					}
					
				}
			}
			
			
			
			
		}
		
		workbook.write();
		workbook.close();

		System.out.println(keyword + "处理完毕...\n");

	}
	
	
	/**
	 * follower and upvote
	 * 将可以标注的知识碎片按照不同的标签存到不同的Excel表中 
	 * 输入是 keyword + -tag_opera.xls 表，输出是 keyword
	 * + “标签” 表
	 */
	public void exampleExperiment3(String keyword) throws Exception {
		String catalog = KeywordCatalogDesign.getTagKeywordCatalog(keyword);
		String path = catalog + keyword + "-tag.xls";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = rwb.getSheet(0); // 得到excel表格
		int rsRows = rs.getRows();
		int rsColumns = rs.getColumns();
		
		// 为每个标签建立一个Excel表
		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
				+ keyword + "-tagbyrule(upvote_and_follower).xls"));
//		WritableWorkbook workbook = Workbook.createWorkbook(new File(catalog
//				+ keyword + "-tagbyruletest(测试无第二种情况).xls"));
		WritableSheet sheet = workbook.createSheet("标签", 0); // 准备新建的写的excel
		WritableCellFormat wcf_center = ExcelSet.setCenterText();

		// 标题行不变
		sheet.setColumnView(0, 20);
		sheet.setColumnView(1, 20);
		for (int col = 0; col < rsColumns - 1; col++) {
			sheet.addCell(new Label(col, 0, rs.getCell(col, 0).getContents(), wcf_center));
		}		
		sheet.addCell(new Label(rsColumns - 1, 0, "sequence", wcf_center));
		sheet.addCell(new Label(rsColumns, 0, "基于规则的自动标签", wcf_center));
		
		//将原表的内容写到新表中
		for (int col = 0; col < rsColumns; col++) {
			for(int row = 1; row < rsRows; row++){
				sheet.setRowView(row, 1300, false);
				sheet.addCell(new Label(col, row, rs.getCell(col, row).getContents(), wcf_center));
			}
		}
		
		
		//判断规则1，问题中存在标签example和答案是否存在
		//通过自己定义的规则给知识碎片打Example的标签
		int sum = Integer.parseInt(rs.getCell(12, rsRows - 1).getContents()) + 1;     //得到问题总数
		for (int i = 0; i < sum; i++){
			for(int j = 1; j < rsRows; j++){    // j 代表Excel表中的行数
				
				//得到每一行的第12列的值
				int sequence = Integer.parseInt(rs.getCell(12, j).getContents());
				
				//判断其是否与问题编号相同
				if(sequence == i){
					
					String qtoras = rs.getCell(2, j).getContents();
					
					String[] word = keyword.split("\\+");      //将关键词以+分开存到数组里面，用于匹配文本
					String[] wordexample = {"example", "instance", "sample"};      //是否存在example词
					String[] wordis = {"is", "are"};      //是否存在疑问词
					
					// 判断该碎片是问题
					if (qtoras.equals("1")) {
						
						//获取碎片内容，是否存在关键词，问题答案标签，投票数，粉丝数
						String content = rs.getCell(1, j).getContents();
						String upvote = rs.getCell(3, j).getContents();
						String follower = rs.getCell(6, j).getContents();
//						String exist = rs.getCell(11, j).getContents();
						
						String contentmatchresult = matcher(content, word);
						String wordexamplematchresult = matcher(content, wordexample);
						String wordismatchresult = matcher(content, wordis);
						
						String matchresult = "0";
						// 判断是否存在上面三种词汇
						if(contentmatchresult.equals("1") && wordexamplematchresult.equals("1") && wordismatchresult.equals("1")){
							matchresult = "1";
						} else {
							matchresult = "0";
						}
						
						String upvoteandfollowerresult = "0";
						// 判断问题是否存在upvote/follower数
						if(!upvote.equals("0") && !follower.equals("0")){
							upvoteandfollowerresult = "1";
						} else {
							upvoteandfollowerresult = "0";
						}
						
						// 判断问题是否同时满足上述两种条件，如果满足则将其标为example标签，再判断其答案的内容
						if(matchresult.equals("1") && upvoteandfollowerresult.equals("1")){
							sheet.addCell(new Label(13, j, "example", wcf_center));
							
							//判断答案是否可以标签example
							for(int m = 0; m < rsRows; m++){    // j 代表Excel表中的行数
								
								//得到每一行的第12列的值
								int sequence1 = Integer.parseInt(rs.getCell(12, m).getContents());
								
								//判断其是否与问题编号相同
								if(sequence1 == i){
									
									// 判断该碎片是答案
									if (qtoras.equals("0")) {
										
										//获取碎片内容，是否存在关键词，问题答案标签，投票数，粉丝数
										String contentanswer = rs.getCell(1, m).getContents();
										String upvoteanswer = rs.getCell(3, m).getContents();
//										String followeranswer = rs.getCell(6, m).getContents();
										
										//判断内容是否存在example标签
										String contentanswermatchresult = matcher(contentanswer, wordexample);
										if(contentanswermatchresult.equals("1")){
											sheet.addCell(new Label(13, m, "example", wcf_center));
										} else if(!upvoteanswer.equals("0")){										
											sheet.addCell(new Label(13, m, "example", wcf_center));
										}
									
									}
								}
							}
						}
					}
					
					// 判断该碎片是答案
					if (qtoras.equals("0")) {
						
						//获取碎片内容，是否存在关键词，问题答案标签，投票数，粉丝数
						String contentanswer = rs.getCell(1, j).getContents();
//						String upvoteanswer = rs.getCell(3, j).getContents();
//						String followeranswer = rs.getCell(6, j).getContents();
						
						//判断内容是否存在example标签
						String contentanswermatchresult = matcher(contentanswer, wordexample);
						if(contentanswermatchresult.equals("1")){
							sheet.addCell(new Label(13, j, "example", wcf_center));
						}
					}
					
				}
			}
			
			
			
			
		}
		
		workbook.write();
		workbook.close();

		System.out.println(keyword + "处理完毕...\n");

	}
	
	
	
	/**
	 * 判断一段文本中是否出现字符串数组中的某个字符串
	 * 输入是 代比较的文本 content 和 字符串数组 word[]，输出是 匹配结果（“1”为出现，“0”为不出现）
	 * 使用的匹配到方法是：JAVA正则表达式 Pattern和Matcher 
	 */
	public static String matcher(String content, String[] word){
		
		//记录出现次数
		int count = 0;
		
		//比较一段文本中是否存在某个关键词
		for (int i = 0; i < word.length; i++) {
			
			// 在content中找关键词出现的次数
			Pattern p = Pattern.compile("(?i)" + word[i]);    //忽略大小写
			Matcher m = p.matcher(content);
			while (m.find()) {
				count++;
			}
		}
		System.out.println("关键词出现次数: " + count);
		
		//出现次数为0时输出flag为0，否则为1
		if (count == 0) {
			return "0";
		} else {
			return "1";
		}
	}

}
