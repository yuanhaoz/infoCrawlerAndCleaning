package data;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import informationextraction.FeatureExtractionForSelenium;
import informationextraction.InformationExtraction2MysqlNew;

import org.jsoup.nodes.Document;

import base.DirFile;
import basic.MysqlConnection_binarytree;
import excel.ExcelSet;
import Jsoup.JsoupParse;

public class ExtractionWiki {

	// 导入mysqloperation类
	static MysqlConnection_binarytree mysqlCon = new MysqlConnection_binarytree();
	// 准备sql语句
	private static String sql;
	// 影响行数（数据变更后，影响行数都是大于0，等于0时没变更，所以说如果变更失败，那么影响行数必定为负）
	// private int i = -1;
	// 结果集
	// private ResultSet rs;
	private static String cata = "02-CQA网站中问题答案质量评估";
	private static String path = "F:\\"+cata+"\\二叉树\\wiki";
	private static FeatureExtractionForSelenium extract = new FeatureExtractionForSelenium();
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		workflow();
	}
	
	public static void workflow() throws Exception{
		String keyword = "Red–black tree";
		UTF8ASCII();
		extract2Excel(keyword);
		extract2ExcelAll();
		extract2ExcelTogether();
	}
	
	/**
	 * 实现功能：抽取所有术语页面的内容到一个Excel（所有术语一张表）
	 */
	public static void extract2ExcelTogether() throws Exception {
		String filepath = path + "\\Binary_tree_all.xls";
		WritableWorkbook workbook = Workbook.createWorkbook(new File(filepath));
		WritableSheet sheet = workbook.createSheet("信息抽取", 0);
		initalTitle(sheet);
		WritableCellFormat wcf_center = ExcelSet.setCenterText();   //设置单元格正文格式
		sheet.setColumnView(0, 20);
		sheet.setColumnView(1, 100);
		int row = 1;
		ArrayList<String> word = DirFile.getFolderFileNamesFromDirectorybyArraylist(path);
		for(int i = 0; i < word.size(); i++){
			String topic = word.get(i);
			if(topic.equals("Red–black tree")){
				System.out.println("Red–black tree has some problems...");
			}else{
				HashMap<String, String> map_all = getAll(topic);
				//存放信息
				for(Entry<String, String> entry : map_all.entrySet()){
					String title = entry.getKey();
					String content = entry.getValue();
					sheet.addCell(new Label(0, row, title, wcf_center));
					sheet.addCell(new Label(1, row, content, wcf_center));
					sheet.setRowView(row, 1300, false); // 设置行高
					row++;
				}
			}
		}
		
		ExcelSet.close(workbook);  //关闭工作空间
	}
	
	/**
	 * 实现功能：批处理（每个术语单独一张表）
	 */
	public static void extract2ExcelAll() throws Exception {
//		String keyword = "Red–black tree";
//		extract2Excel(keyword);
		ArrayList<String> word = DirFile.getFolderFileNamesFromDirectorybyArraylist(path);
		for(int i = 0; i < word.size(); i++){
			String topic = word.get(i);
			extract2Excel(topic);
		}
	}
	
	/**
	 * 实现功能：抽取每个术语页面的所有信息和细节信息到Excel表格（每个术语单独一张表）
	 */
	public static void extract2Excel(String keyword) throws Exception {
		String filepath = path + "\\" + keyword + "\\" + keyword + ".xls";
		WritableWorkbook workbook = Workbook.createWorkbook(new File(filepath));
		WritableSheet sheet = workbook.createSheet("信息抽取", 0);
		initalTitle(sheet);
		WritableCellFormat wcf_center = ExcelSet.setCenterText();   //设置单元格正文格式
		sheet.setColumnView(0, 20);
		sheet.setColumnView(1, 100);
		HashMap<String, String> map_all = getAll(keyword);
		HashMap<String, String> map_summary = getSummary(keyword);
		HashMap<String, String> map_detail = getDetail(keyword);
		//存放信息
		int row = 1;
		for(Entry<String, String> entry : map_all.entrySet()){
			String title = entry.getKey();
			String content = entry.getValue();
			sheet.addCell(new Label(0, row, title, wcf_center));
			sheet.addCell(new Label(1, row, content, wcf_center));
			sheet.setRowView(row, 1300, false); // 设置行高
			row++;
		}
//		System.out.println(row);
		for(Entry<String, String> entry : map_summary.entrySet()){
			String title = entry.getKey();
			String content = entry.getValue();
			sheet.addCell(new Label(0, row, title, wcf_center));
			sheet.addCell(new Label(1, row, content, wcf_center));
			sheet.setRowView(row, 1300, false); // 设置行高
			row++;
		}
//		System.out.println(row);
		for(Entry<String, String> entry : map_detail.entrySet()){
			String title = entry.getKey();
			String content = entry.getValue();
			sheet.addCell(new Label(0, row, title, wcf_center));
			sheet.addCell(new Label(1, row, content, wcf_center));
			sheet.setRowView(row, 1300, false); // 设置行高
			row++;
		}
		ExcelSet.close(workbook);  //关闭工作空间
	}

	/**
	 * 实现功能：建立工作表，输入是表格存储路径filepath，输出是表格对象WritableSheet
	 */
	public static WritableSheet createSheet(String filepath) throws Exception {
		WritableWorkbook workbook = Workbook.createWorkbook(new File(filepath));
		WritableSheet sheet = workbook.createSheet("信息抽取", 0);
		return sheet;
	}
	
	/**
	 * 实现功能：设置表格第一行
	 */
	public static void initalTitle(WritableSheet sheet) throws Exception {
		WritableCellFormat wcf_title = ExcelSet.setTitleText();   //设置单元格正文格式
		sheet.addCell(new Label(0, 0, "Title", wcf_title));
		sheet.addCell(new Label(1, 0, "Content", wcf_title));
		sheet.setRowView(0, 700, false);                         // 设置行高
	}
	
	/**
	 * 实现功能：得到维基网页的细节内容
	 */
	public static HashMap<String, String> getDetail(String keyword) throws Exception {
		String filePath = path + "\\" + keyword;
		ArrayList<String> file = DirFile.getFileNamesFromDirectorybyArraylist(filePath);
		HashMap<String, String> map = new HashMap<String, String>();
		for(int i = 0; i < file.size(); i++){
			String fileName = file.get(i);
//			System.out.println(fileName);
			if(fileName.equals(keyword)||fileName.equals(keyword+"_all")||fileName.equals(keyword+"_summary")){
//				System.out.println("...");
			} else {
				String fileNamePath = filePath + "\\" + fileName + ".txt";
				String content = DirFile.getStringFromPathFile(fileNamePath);
//				System.out.println("content：" + content);
				map.put(fileName, content);
			}
		}
		return map;
	}
	
	/**
	 * 实现功能：得到维基网页的总结内容
	 */
	public static HashMap<String, String> getSummary(String keyword) throws Exception {
		String filePath = path + "\\" + keyword + "\\" + keyword + "_summary.txt";
		HashMap<String, String> map = new HashMap<String, String>();
		String content = DirFile.getStringFromPathFile(filePath);
//		System.out.println("content：" + content);
		map.put(keyword + "_summary", content);
		return map;
	}
	
	/**
	 * 实现功能：得到维基术语网页的所有内容
	 */
	public static HashMap<String, String> getAll(String keyword) throws Exception {
		String filePath = path + "\\" + keyword + "\\" + keyword + "_all.txt";
		HashMap<String, String> map = new HashMap<String, String>();
		String content = DirFile.getStringFromPathFile(filePath);
//		System.out.println("content：" + content);
		map.put(keyword + "_all", content);
		return map;
	}
	
	/**
	 * 实现功能：UTF8编码转为ASCII码
	 */
	public static void UTF8ASCII(){
		String in = "F:\\高质量\\二叉树\\wiki";
		ArrayList<String> list = DirFile.getFolderFileNamesFromDirectorybyArraylist(in);
		for(int i = 0; i < list.size(); i++){
			String word = list.get(i);
			String path = in + "\\" + word;
			File[] f = new File(path).listFiles();
			for(int j = 0; j < f.length; j++){
				String fileName = f[j].getName();
				if(fileName.contains(".txt")){
					String inPath = path + "\\" + fileName;
					String outPath = path + "\\" + fileName;
					String text = DirFile.getStringFromPathFile(inPath);
					String textDeal = UTF8Code.unicodeReplace(text).toString();
					DirFile.storeString2File(textDeal, outPath);
				}
			}
		}
	}

}
