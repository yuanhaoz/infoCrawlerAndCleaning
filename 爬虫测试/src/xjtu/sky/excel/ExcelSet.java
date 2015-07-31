package xjtu.sky.excel;

/**
 * zhengyuanhao  2015/6/30
 * 简单实现：quora  
 * 实现功能：Excel单元格格式设置
 *          1.设置行高和列宽；
 *          2.合并单元格；
 *          3.设置标题单元格和正文单元格格式。
 * 
 */

import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Border;
import jxl.write.BorderLineStyle;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;

@SuppressWarnings("deprecation")
public class ExcelSet {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 实现功能：设置列宽
	 * @param sheet
	 */
	public void setColumnView(WritableSheet sheet) throws Exception{
		sheet.setColumnView(0, 20);
		sheet.setColumnView(1, 20);
		sheet.setColumnView(2, 20);
		sheet.setColumnView(3, 20);
		sheet.setColumnView(4, 20);
		sheet.setColumnView(5, 40);
		sheet.setColumnView(6, 80);
		sheet.setColumnView(7, 20);
		sheet.setColumnView(8, 20);
		sheet.setColumnView(9, 40);
		sheet.setColumnView(10, 20);
		sheet.setColumnView(11, 20);
		sheet.setColumnView(12, 20);
		sheet.setColumnView(13, 20);
	}
	
	/**
	 * 实现功能：设置行高
	 * @param sheet
	 */
	public void setRowView(WritableSheet sheet) throws Exception{
		sheet.setRowView(0, 800, false);
		sheet.setRowView(1, 1000, false);
	}
	
	/**
	 * 实现功能：合并单元格
	 * @param sheet
	 */
	public void mergeCells(WritableSheet sheet) throws Exception{
		sheet.mergeCells(0, 0, 0, 1); // 合并分类单元格		
		sheet.mergeCells(1, 0, 3, 0);
	}
	
	/**
	 * 实现功能：设置"标题"单元格的格式
	 * @param 
	 */
	public static WritableCellFormat setTitleText() throws Exception{
		WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);  // 字体
		// 用于标题
		WritableCellFormat wcf_title = new WritableCellFormat(BoldFont);
		wcf_title.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
		wcf_title.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直对齐
		wcf_title.setAlignment(Alignment.CENTRE); // 水平对齐
		wcf_title.setBackground(Colour.GRAY_25);
		wcf_title.setWrap(true); // 是否换行
		return wcf_title;
	}
	
	/**
	 * 实现功能：设置"正文"单元格的格式
	 * @param 
	 */
	public static WritableCellFormat setCenterText() throws Exception{
		WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);  // 字体
		// 用于正文
		WritableCellFormat wcf_center = new WritableCellFormat(NormalFont);
		wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN);  // 线条
		wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);   // 垂直对齐
		wcf_center.setAlignment(Alignment.CENTRE);  
		wcf_center.setWrap(true);   // 是否换行
		return wcf_center;
	}
	
	
}
