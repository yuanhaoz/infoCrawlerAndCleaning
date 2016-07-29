package data;

import java.io.File;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import excel.ExcelSet;

public class ExcelReadWrite {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			excel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("I am sorry...");
			e.printStackTrace();
		}
	}
	
	public static String getId(String keyword){
		if(!keyword.equals("")){
			String seq = keyword.split("tree")[1];
			if(seq.contains("_")){
				String seqReal = seq.split("_")[0];
				System.out.println(seqReal);
				return seqReal;
			} else {
				System.out.println(seq);
				return seq;
			}
		} else {
			return "";
		}
	}
	
	public static void excel() throws Exception {
		// 读取源excel
		String excelPath = "F:\\02-CQA网站中问题答案质量评估\\13-Quora网站质量定义标准寻找\\Binary_tree_Analysis.xls";
		Workbook wb = Workbook.getWorkbook(new File(excelPath));
		Sheet st = wb.getSheet(0);
		int rows = st.getRows();
		// 写新的excel
		String filepath = "F:\\02-CQA网站中问题答案质量评估\\13-Quora网站质量定义标准寻找\\Binary_tree_Analysis_id.xls";
		WritableWorkbook workbook = Workbook.createWorkbook(new File(filepath));
		WritableSheet sheet = workbook.createSheet("信息抽取", 0);
		WritableCellFormat wcf_title = ExcelSet.setTitleText(); 
		sheet.addCell(new Label(0, 0, "QA", wcf_title));
		sheet.addCell(new Label(1, 0, "Upvote", wcf_title));
		sheet.addCell(new Label(2, 0, "Comment", wcf_title));
		sheet.addCell(new Label(3, 0, "Upvote + Comment*2", wcf_title));
		sheet.addCell(new Label(4, 0, "ID", wcf_title));
		sheet.setRowView(0, 700, false);       
		WritableCellFormat wcf_center = ExcelSet.setCenterText();
		
		sheet.setColumnView(0, 20);
		sheet.setColumnView(1, 20);
		sheet.setColumnView(2, 20);
		sheet.setColumnView(3, 20);
		
		for(int i = 1; i < rows; i++){
			String qa = st.getCell(0, i).getContents();
			String upvote = st.getCell(1, i).getContents();
			String comment = st.getCell(2 , i).getContents();
			String tag = st.getCell(3 , i).getContents();
			String id = getId(qa);
			sheet.setRowView(i, 700, false); // 设置行高
			sheet.addCell(new Label(0, i, qa, wcf_center));
			sheet.addCell(new Label(1, i, upvote, wcf_center));
			sheet.addCell(new Label(2, i, comment, wcf_center));
			sheet.addCell(new Label(3, i, tag, wcf_center));
			sheet.addCell(new Label(4, i, id, wcf_center));
		}
		
		workbook.write();
		workbook.close();
		
	}
	
	public static void excelTag() throws Exception {
		// 读取源excel
		String excelPath = "F:\\02-CQA网站中问题答案质量评估\\13-Quora网站质量定义标准寻找\\Binary_tree_Analysis_id.xls";
		Workbook wb = Workbook.getWorkbook(new File(excelPath));
		Sheet st = wb.getSheet(0);
		int rows = st.getRows();
		// 写新的excel
		String filepath = "F:\\02-CQA网站中问题答案质量评估\\13-Quora网站质量定义标准寻找\\Binary_tree_Analysis_id.xls";
		WritableWorkbook workbook = Workbook.createWorkbook(new File(filepath));
		WritableSheet sheet = workbook.createSheet("信息抽取", 0);
		WritableCellFormat wcf_title = ExcelSet.setTitleText(); 
		sheet.addCell(new Label(0, 0, "QA", wcf_title));
		sheet.addCell(new Label(1, 0, "Upvote", wcf_title));
		sheet.addCell(new Label(2, 0, "Comment", wcf_title));
		sheet.addCell(new Label(3, 0, "Upvote + Comment*2", wcf_title));
		sheet.addCell(new Label(4, 0, "ID", wcf_title));
		sheet.setRowView(0, 700, false);       
		WritableCellFormat wcf_center = ExcelSet.setCenterText();
		
		sheet.setColumnView(0, 20);
		sheet.setColumnView(1, 20);
		sheet.setColumnView(2, 20);
		sheet.setColumnView(3, 20);
		
		for(int i = 1; i < rows; i++){
			String qa = st.getCell(0, i).getContents();
			String upvote = st.getCell(1, i).getContents();
			String comment = st.getCell(2 , i).getContents();
			String tag = st.getCell(3 , i).getContents();
			String id = getId(qa);
			sheet.setRowView(i, 700, false); // 设置行高
			sheet.addCell(new Label(0, i, qa, wcf_center));
			sheet.addCell(new Label(1, i, upvote, wcf_center));
			sheet.addCell(new Label(2, i, comment, wcf_center));
			sheet.addCell(new Label(3, i, tag, wcf_center));
			sheet.addCell(new Label(4, i, id, wcf_center));
		}
		
		workbook.write();
		workbook.close();
		
	}

}
