package experiment;

import informationextraction.InformationExtraction2Excel;

import java.io.File;

import basic.KeywordCatalogDesign;
import excel.ExcelSet;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class DealTagExcel {
	/**
	 * 处理标注过的Excel数据
	 */
	public void dealTagExcel(String dir) throws Exception {
		File f = new File(dir); //dir为："G:/快盘1/爬取数据/数据标注（进行中）/数据挖掘"
		File childs[]=f.listFiles();
		String chWord = "该问题网页不存在问题的附加信息...";
		for (int i = 0; i < childs.length; i++){
			String keyword = childs[i].getName(); //得到关键词
			String filepath = dir + keyword + "/"; //得到关键词文件夹路径
			String filename = filepath + keyword + "-tag_filtering.xls"; //标注Excel文件
			System.out.println(filename);
			Workbook workbook = Workbook.getWorkbook(new File(filename)); //读取Excel表
			Sheet sheet = workbook.getSheet(0);
			int row = sheet.getRows(); //得到行数
			int column = sheet.getRows(); //得到列数
//			System.out.println(row);
			//建立新的Excel表格
			String newfile = filepath + keyword + "-tag_filtering2.xls";
			WritableWorkbook writableworkbook = Workbook.createWorkbook(new File(newfile));
			WritableSheet writesheet = writableworkbook.createSheet("信息抽取", 0);
			InformationExtraction2Excel a = new InformationExtraction2Excel();
			a.initalTitle(writesheet);
			WritableCellFormat wcf_center = ExcelSet.setCenterText();   //设置单元格正文格式
			writesheet.mergeCells(14, 0, 17, 0);
			for (int col = 13; col < 15; col++) {
				writesheet.addCell(new Label(col, 0, sheet.getCell(col, 0).getContents(), wcf_center));
			}
			writesheet.setColumnView(0, 20);
			writesheet.setColumnView(1, 60);
			for (int j = 1; j < row; j++){ //从第二行开始分析
				for (int col = 2; col < 7; col++) {
					writesheet.setRowView(j, 1300, false);
					writesheet.addCell(new Label(col, j, sheet.getCell(col, j).getContents(), wcf_center));
				}
				for (int col = 9; col < 18; col++) {
					writesheet.setRowView(j, 1300, false);
					writesheet.addCell(new Label(col, j, sheet.getCell(col, j).getContents(), wcf_center));
				}
				String id = keyword + sheet.getCell(12, j).getContents(); // txt存储文件名
				String content = sheet.getCell(1, j).getContents(); // 对应内容
				if (content.contains("★")) {
					content = content.substring(1, content.length());
				}
				if (content.contains(chWord)) {
					content = content.replace(content.substring(content.indexOf(chWord),
								content.indexOf(chWord) + chWord.length()), "");
				}
//				System.out.println("QA:" + id + "\tContent:" + content);
				String profession = sheet.getCell(7, j).getContents(); //处理作者姓名
				if (profession.contains("：")) {
					profession = profession.substring(0, profession.indexOf("："));
				}
				String knowAbout = sheet.getCell(8, j).getContents(); //处理作者领域
				if (knowAbout.contains("Know About：")) {
					knowAbout = knowAbout.substring("Know About：".length(), knowAbout.length());
				}
				writesheet.addCell(new Label(0, j, id, wcf_center));
				writesheet.addCell(new Label(1, j, content, wcf_center));
				writesheet.addCell(new Label(7, j, profession, wcf_center));
				writesheet.addCell(new Label(8, j, knowAbout, wcf_center));
			}
			writableworkbook.write();
			writableworkbook.close();
		}
	}
}
