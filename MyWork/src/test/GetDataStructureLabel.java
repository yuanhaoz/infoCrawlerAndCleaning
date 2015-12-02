package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class GetDataStructureLabel {

	public static ArrayList<String> getlabel(){
		ArrayList<String> label = new ArrayList<String>();
		String filepath = "F:/数据结构爬取主题/Data_structure_label.xls";
		File labelFile = new File(filepath);
		try {
			Workbook wb = Workbook.getWorkbook(labelFile);
			Sheet s = wb.getSheet(0);
			int row = s.getRows();
			for (int i = 0; i < row; i++) {
				label.add(s.getCell(0, i).getContents());
			}
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//创建文件
		String catalog = "F:/数据结构爬取主题/";
		for(int i = 0; i < label.size(); i++){
			System.out.println(label.get(i));
			String htmlPath = catalog + label.get(i) + ".html";
			File html = new File(htmlPath);
			if(!html.exists()){
				try {
					html.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("********************************");
				}
			}else{
//				System.out.println(">>>>>>>>>>>>>>>>>>>>>>");
			}
		}
		System.out.println(label.size());
		
		return label;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getlabel();
	}

}
