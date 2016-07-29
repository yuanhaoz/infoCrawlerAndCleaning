package data;

/**
 * 处理数据用于词向量模型进行词向量训练
 * @author yuanhao
 *
 */

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

public class DataWord2vec {
	private static String cata = "02-CQA网站中问题答案质量评估";
	private static String path = "F:\\"+cata+"\\二叉树\\quora\\question";
	private static String txtpath = "F:\\"+cata+"\\二叉树\\quora\\question\\txt";
	private static String pathTag = "F:\\"+cata+"\\二叉树\\quora\\标注";
	private static String pathTagTxt = "F:\\"+cata+"\\二叉树\\quora\\question\\bianry_tree_txt";

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String fileName = "Binary_tree";
		int questionNumber =  getQuestionNumber(fileName);
		mergeQA(fileName, questionNumber);
		createTxt(fileName);
		
//		String path = "F:\\data_predeal_quora\\data_structure_tag";
//		File[] file = new File(path).listFiles();
//		for(int i = 0; i < file.length; i++){
//			String fileName = file[i].getName();
//			System.out.println(fileName);
//			int questionNumber =  getQuestionNumber(fileName);
//			mergeQA(fileName, questionNumber);
//			createTxt(fileName);
//		}
		
	}
	
	/**
	 * 实现功能：将问题内容和回答内容合并
	 * @param filepath
	 */
	public static int getQuestionNumber(String keyword) throws Exception {
//		String keyword = "Array";
		String soucrePath = path + "\\" + keyword + ".xls";
		// 读取原来的表格
		Workbook sourceWorkbook = Workbook.getWorkbook(new File(soucrePath));
		Sheet sourceSheet = sourceWorkbook.getSheet(0);
		int rows = sourceSheet.getRows();
		String seq = sourceSheet.getCell(8, rows - 1).getContents();
		int questionNumber = Integer.parseInt(seq) + 1;
		System.out.println(questionNumber);
		return questionNumber;
	}
	
	/**
	 * 实现功能：将问题内容和回答内容合并(问题和每一条回答)
	 */
	public static void mergeQA(String keyword, int questionNumber) throws Exception {
//		String keyword = "Array";
//		int questionNumber = 37;
		String soucrePath = path + "\\" + keyword + ".xls";
		String targetPath = path + "\\" + keyword + "-word2vec.xls";
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
			// 读取数据，合并问题和回答
			for(int j = 1; j < rows; j++){
				// 得到需要的信息
				String qa = sourceSheet.getCell(0, j).getContents();
				String content = sourceSheet.getCell(1, j).getContents();
				String sequence = sourceSheet.getCell(8, j).getContents();
//				System.out.println(sequence);
				
				int questionId = Integer.parseInt(sequence);// 得到Excel表格中每个问题的序号
				if(questionId == i){// 对于同一问题
					String[] arr = qa.split("_");
					if(arr.length == 2){// 判断是否为问题，若是，则赋值，由于是从第一行开始读取，所以始终不会为空
						questionid = qa;
						questionContent = content;
//						System.out.println("问题"+i+"是： " + questionid);
//						System.out.println("问题内容"+i+"是： " + questionContent);
					}
					if(arr.length == 3){// 判断是否为可以标注的答案，合并标签
						String mergeId = qa;
						String mergeContent = questionContent + "\n" + content;
						mergeContent = mergeContent.replace("Expanded information：", "");
						mergeContent = mergeContent.replace("该问题网页不存在问题的附加信息...", "");
						mergeContent = mergeContent.replace("★", "");
						mergeContent = mergeContent.replace("�", "");
						mergeContent = mergeContent.replaceAll("\n", "");
//						System.out.println("回答"+i+"是： " + mergeId);
//						System.out.println("回答内容"+i+"是： " + mergeContent);
						row++; // 新的Excel表格行数 +1
						targetSheet.setRowView(row, 800, false);// 设置行高
						// 将问题写到新的Excel表格中
						targetSheet.addCell(new Label(0, row, mergeId, wcf_center));
						targetSheet.addCell(new Label(1, row, mergeContent, wcf_center));
					} 
				} else {
//					System.out.println("不是第" + i + "个问题...");
				}

			}
		}
		ExcelSet.close(targetWorkbook);
		
	}
	
	/**
	 * 实现功能：将问题内容和回答内容合并(问题和所有的回答)
	 */
	public static void mergeQAAll(String keyword, int questionNumber) throws Exception {
//		String keyword = "Array";
//		int questionNumber = 37;
		String soucrePath = pathTag + "\\" + keyword + ".xls";
		String targetPath = pathTag + "\\" + keyword + "-word2vec.xls";
		// 读取原来的表格
		Workbook sourceWorkbook = Workbook.getWorkbook(new File(soucrePath));
		Sheet sourceSheet = sourceWorkbook.getSheet(0);
		int rows = sourceSheet.getRows();
		// 生成新的表格
		WritableWorkbook targetWorkbook = Workbook.createWorkbook(new File(targetPath));
		WritableSheet targetSheet = targetWorkbook.createSheet("word2vec", 0);
		WritableCellFormat wcf_center = ExcelSet.setCenterText();   //设置单元格正文格式
		// 记录新的行数，用于Excel表格的写入
		int row = -1;
		// 判断是不是同一问题的，Array有37个问题
		for(int i = 0; i < questionNumber; i++){
			String questionContent = "";//每个问题有一个问题内容
			String mergeId = "";// 合并后的ID
			String mergeContent = "";// 合并后的Content
			// 读取数据，合并问题和回答
			for(int j = 1; j < rows; j++){
				// 得到需要的信息
				String qa = sourceSheet.getCell(0, j).getContents();
				String content = sourceSheet.getCell(1, j).getContents();
				String sequence = sourceSheet.getCell(8, j).getContents();
//				System.out.println(sequence);
				
				int questionId = Integer.parseInt(sequence);// 得到Excel表格中每个问题的序号
				if(questionId == i){// 对于同一问题
					String[] arr = qa.split("_");
					if(arr.length == 2){// 判断是否为问题，若是，则赋值，由于是从第一行开始读取，所以始终不会为空
						questionContent = content;
						mergeId = qa;
//						System.out.println("问题"+i+"是： " + questionid);
//						System.out.println("问题内容"+i+"是： " + questionContent);
					}
					if(arr.length == 3){// 判断是否为可以标注的答案，合并标签
						mergeContent = mergeContent + questionContent + "\n" + content;
						mergeContent = mergeContent.replace("Expanded information：", "");
						mergeContent = mergeContent.replace("该问题网页不存在问题的附加信息...", "");
						mergeContent = mergeContent.replace("★", "");
						mergeContent = mergeContent.replace("�", "");
						mergeContent = mergeContent.replaceAll("\n", "");
//						System.out.println("回答"+i+"是： " + mergeId);
//						System.out.println("回答内容"+i+"是： " + mergeContent);
						row++; // 新的Excel表格行数 +1
						targetSheet.setRowView(row, 800, false);// 设置行高
						// 将问题写到新的Excel表格中
						targetSheet.addCell(new Label(0, row, mergeId, wcf_center));
						targetSheet.addCell(new Label(1, row, mergeContent, wcf_center));
					} 
				} else {
//					System.out.println("不是第" + i + "个问题...");
				}

			}
		}
		ExcelSet.close(targetWorkbook);
		
	}

	/**
	 * 实现功能：将问题内容和回答内容合并
	 * @param filepath
	 */
	public static void createTxt(String keyword) throws Exception {
//		String keyword = "Array";
		String soucrePath = path + "\\" + keyword + "-word2vec.xls";
		// 读取原来的表格
		Workbook sourceWorkbook = Workbook.getWorkbook(new File(soucrePath));
		Sheet sourceSheet = sourceWorkbook.getSheet(0);
		int rows = sourceSheet.getRows();
		// 生成相应TXT文件
		new File(txtpath).mkdir();
		for(int i = 0; i < rows; i++){
			String id = sourceSheet.getCell(0, i).getContents();
			String content = sourceSheet.getCell(1, i).getContents();
//			System.out.println(content);
			String filePath = txtpath + "\\" + id + ".txt";
			File txtFile = new File(filePath);
			FileWriter output = new FileWriter(txtFile);
			output.write(content);
			output.close();
		}
	}

}
