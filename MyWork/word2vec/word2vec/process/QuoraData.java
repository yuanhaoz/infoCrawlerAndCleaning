package word2vec.process;

import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import excel.ExcelSet;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class QuoraData {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String path = "F:\\data_predeal_quora\\data_structure_tag";
		File[] file = new File(path).listFiles();
		for(int i = 0; i < file.length; i++){
			String fileName = file[i].getName();
			System.out.println("正在处理关键词：" + fileName);
			int questionNumber =  getQuestionNumber(fileName);
			mergeQA(fileName, questionNumber);
			createTxt(fileName);
		}
		
//		String[] sourceTag = {"method", "", "", ""};
//		String[] targetTag = {"feature", "definition", "storage", ""};
//		mergeTag(sourceTag, targetTag);
		
	}
	
	/**
	 * 实现功能：将问题内容和回答内容合并
	 * @param filepath
	 */
	public static int getQuestionNumber(String keyword) throws Exception {
//		String keyword = "Array";
		String path = "F:\\data_predeal_quora\\data_structure_tag" + "\\" + keyword;
		String soucrePath = path + "\\" + keyword + "-tag_filtering.xls";
		// 读取原来的表格
		Workbook sourceWorkbook = Workbook.getWorkbook(new File(soucrePath));
		Sheet sourceSheet = sourceWorkbook.getSheet(0);
		int rows = sourceSheet.getRows();
		String seq = sourceSheet.getCell(12, rows - 1).getContents();
		int questionNumber = Integer.parseInt(seq) + 1;
		System.out.println(keyword + "包含问题数目为：" +questionNumber);
		return questionNumber;
	}
	
	/**
	 * 实现功能：将问题内容和回答内容合并
	 * @param filepath
	 */
	public static void createTxt(String keyword) throws Exception {
//		String keyword = "Array";
		String path = "F:\\data_predeal_quora\\data_structure_tag" + "\\" + keyword;
		String soucrePath = path + "\\" + keyword + "-tag_word2vec.xls";
		// 读取原来的表格
		Workbook sourceWorkbook = Workbook.getWorkbook(new File(soucrePath));
		Sheet sourceSheet = sourceWorkbook.getSheet(0);
		int rows = sourceSheet.getRows();
		// 生成相应TXT文件
		new File("F:\\data_predeal_quora\\data_structure_tag_txt").mkdir();
		String targetPath = "F:\\data_predeal_quora\\data_structure_tag_txt" + "\\" + keyword;
		new File(targetPath).mkdir();
		System.out.println(keyword + " 含有问答对数目为：" + rows);
		for(int i = 0; i < rows; i++){
			String id = sourceSheet.getCell(0, i).getContents();
			String content = sourceSheet.getCell(5, i).getContents();
//			System.out.println(content);
			String filePath = targetPath + "\\" + id + ".txt";
			File txtFile = new File(filePath);
			FileWriter output = new FileWriter(txtFile);
			output.write(content);
			output.close();
		}
	}
	
	/**
	 * 实现功能：将问题内容和回答内容合并
	 * @param filepath
	 */
	public static void mergeQA(String keyword, int questionNumber) throws Exception {
//		String keyword = "Array";
//		int questionNumber = 37;
		String path = "F:\\data_predeal_quora\\data_structure_tag" + "\\" + keyword;
		String soucrePath = path + "\\" + keyword + "-tag_filtering.xls";
		String targetPath = path + "\\" + keyword + "-tag_word2vec.xls";
		// 读取原来的表格
		Workbook sourceWorkbook = Workbook.getWorkbook(new File(soucrePath));
		Sheet sourceSheet = sourceWorkbook.getSheet(0);
		int rows = sourceSheet.getRows();
		int columns = sourceSheet.getColumns();
		// 生成新的表格
		WritableWorkbook targetWorkbook = Workbook.createWorkbook(new File(targetPath));
		WritableSheet targetSheet = targetWorkbook.createSheet("word2vec", 0);
		WritableCellFormat wcf_center = ExcelSet.setCenterText();   //设置单元格正文格式
		// 记录新的行数，用于Excel表格的写入
		int row = -1;
		// 判断是不是同一问题的，Array有37个问题
		for(int i = 0; i < questionNumber; i++){
			String questionid = "";//每个问题有一个问题ID
			String questionContent = "";//每个问题有一个问题内容
			String[] questionTag = {"", "", "", ""};// 每个问题标注的四个标签
			// 读取数据，合并问题和回答
			for(int j = 1; j < rows; j++){
				// 得到需要的信息
				String qa = sourceSheet.getCell(0, j).getContents();
				String content = sourceSheet.getCell(1, j).getContents();
				String sequence = sourceSheet.getCell(12, j).getContents();
				String exist = sourceSheet.getCell(13, j).getContents();
				String[] tag = { sourceSheet.getCell(14, j).getContents(), 
						sourceSheet.getCell(15, j).getContents(),
						sourceSheet.getCell(16, j).getContents(),
						sourceSheet.getCell(17, j).getContents() };
//				System.out.println(sequence);
				
				int questionId = Integer.parseInt(sequence);// 得到Excel表格中每个问题的序号
				if(questionId == i){// 对于同一问题
					if(!qa.contains("_")&& exist.equals("1")){// 判断是否为问题，若是，则赋值，由于是从第一行开始读取，所以始终不会为空
						questionid = qa;
						questionTag = tag;
						questionContent = content;
//						System.out.println("问题"+i+"是： " + questionid);
//						System.out.println("问题内容"+i+"是： " + questionContent);
//						System.out.println("问题标签"+i+"是： " + questionTag[0] + questionTag[1]
//								+ questionTag[2] + questionTag[3]);
					}
					if(qa.contains("_") && exist.equals("1")){// 判断是否为可以标注的答案，合并标签
						String mergeId = qa;
						String[] mergeTag = QuoraData.mergeTag(questionTag, tag);
						String mergeContent = QuoraData.mergeContent(questionContent, content);
						mergeContent = mergeContent.replace("Expanded information：", "");
						mergeContent = mergeContent.replace("该问题网页不存在问题的附加信息...", "");
						mergeContent = mergeContent.replace("★", "");
						mergeContent = mergeContent.replace("�", "");
						mergeContent = mergeContent.replaceAll("\n", "");
//						System.out.println("回答"+i+"是： " + mergeId);
//						System.out.println("回答内容"+i+"是： " + mergeContent);
//						System.out.println("回答标签"+i+"是： " + mergeTag[0] + mergeTag[1]
//								+ mergeTag[2] + mergeTag[3]);
						row++; // 新的Excel表格行数 +1
						targetSheet.setRowView(row, 800, false);// 设置行高
						// 将问题写到新的Excel表格中
						QuoraData.writeExcel(targetSheet, wcf_center, row, mergeId, mergeTag, mergeContent);
					} 
				} else {
//					System.out.println("不是第" + i + "个问题...");
				}

			}
		}
		ExcelSet.close(targetWorkbook);
		
	}
	
	// 实现功能：按行列写Excel
	public static void writeExcel(WritableSheet sheet, WritableCellFormat wcf_center, int row,
			String id, String[] tag, String content) throws Exception{
		sheet.addCell(new Label(0, row, id, wcf_center));
		sheet.addCell(new Label(1, row, tag[0], wcf_center));
		sheet.addCell(new Label(2, row, tag[1], wcf_center));
		sheet.addCell(new Label(3, row, tag[2], wcf_center));
		sheet.addCell(new Label(4, row, tag[3], wcf_center));
		sheet.addCell(new Label(5, row, content, wcf_center));
	}
	
	// 实现功能：合并问题和它下面一条回答的标签
	public static String[] mergeTag(String[] sourceTag, String[] targetTag){
		String[] tag = new String[sourceTag.length];
		Set set = new HashSet();// set中无重复的数据
		int id = 0;
		// 将两个数组合并为一个数组
		for(int i = 0; i < sourceTag.length; i++){
			if(!sourceTag[i].equals("")){// 判断不为空
				set.add(sourceTag[i]);
			}
		}
		for(int i = 0; i < targetTag.length; i++){
			if(!targetTag[i].equals("")){// 判断不为空
				set.add(targetTag[i]);
			}
		}
//		System.out.println(set.size());
		
		// 去除数组中重复的数
		Iterator it = set.iterator();
		while(it.hasNext()){
			id++;
			String tagName = it.next().toString();
//			System.out.println(tagName);
//			System.out.println(id);
			tag[id - 1] = tagName;
		}
		if(id < 4){
			for(int j = id; j < 4; j++){
				tag[j] = "";
			}
		}

//		for(int i = 0; i < tag.length; i++){
//			System.out.println("tag" + i + " is: " + tag[i]);
//		}
		return tag;
	}
	
	// 实现功能：合并问题和答案的内容
	public static String mergeContent(String question, String answer){
		String result = question + "\n" + answer;
		return result;
	}

}
