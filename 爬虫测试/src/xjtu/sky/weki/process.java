package xjtu.sky.weki;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Document;

import xjtu.sky.quora.PageAnalysis;
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

/**
 * ����F�̵�tag_headword.xls��񣬵õ����в��ظ��ĵ��ʣ������䰴����ĸ˳�����
 * Arraylist��̬����
 */
public class process {

	public static void main(String[] args) throws Exception {
		analysis();
	}

	/**
	 * ����ȥ��
	 */
	public static void analysis() throws Exception {
		File file = new File("F:/tag_headword.xls");
		Workbook rwb = Workbook.getWorkbook(new FileInputStream(file));
		Sheet rs = rwb.getSheet(0);
		int rsRows = rs.getRows();
		int rsColumns = rs.getColumns();
		ArrayList<String> list = new ArrayList<String>();
		for (int n = 0; n < rsColumns - 1; n++) {
			for (int m = 0; m < rsRows - 1; m++) {
				String text = rs.getCell(n, m).getContents(); 
				if (!text.equals("")) {     // ��Ԫ��Ϊ��ʱ�������浽list��̬������
					list.add(text);   
				}
			}
		}
		
		// ȥ�ط���1
		Collections.sort(list); // ����
		ArrayList<String> deallist1 = new ArrayList<String>();
		deallist1 = RemoveRepeated(list); // ȥ��1
		ArrayList<Integer> count = new ArrayList<Integer>();
		count = WordCount(list); 
		Collections.sort(deallist1); // ����
		// ȥ�ط���2
		ArrayList<String> deallist2 = new ArrayList<String>();
		deallist2 = removeDuplicate(list); // ȥ��2
		Collections.sort(deallist2); 
		// ȥ�ط���3
		ArrayList<String> deallist3 = new ArrayList<String>();
		deallist3 = removeDuplicate(list);  // ȥ��3
		Collections.sort(deallist3); 
		// ����̨������
		for (int i = 0; i < count.size(); i++) {
			System.out.println(count.get(i));    
		}
		for (int i = 0; i < deallist1.size(); i++) {
			System.out.println(deallist1.get(i));    // ʹ��ȥ�ط���1
		}
		System.out.println("��������������Ϊ��" + deallist1.size());
		// ����浽Excel��
		WritableWorkbook workbook = Workbook.createWorkbook(new File("F:/tag_headword(RemoveRepeated).xls"));
		WritableSheet sheet = workbook.createSheet("ȥ�غ�������", 0);
		for(int i = 0; i < deallist1.size(); i++){
			sheet.addCell(new Label(0, i, deallist1.get(i)));
		}
		workbook.write(); 
		workbook.close(); 
		System.out.println("��������д��Excel�У�·��Ϊ��F:/tag_headword(RemoveRepeated).xls");
	}

	/** ȥ���ظ��ĵ��ʣ�ֻ����һ�� **/
	public static ArrayList<String> RemoveRepeated(ArrayList<String> arr) {
		ArrayList<String> tmpArr = new ArrayList<String>();
		for (int i = 0; i < arr.size(); i++) {
			if (!tmpArr.contains(arr.get(i))) {
				tmpArr.add(arr.get(i));
			}
		}
		return tmpArr;
	}
	
	/** ȥ���ظ��ĵ��ʣ�ֻ����һ�� **/
	public static ArrayList<Integer> WordCount(ArrayList<String> arr) {
		ArrayList<Integer> count = new ArrayList<Integer>();
		for(int i = 0; i < arr.size(); i++){
			arr.get(i);
			count.add(i, 0);
			int a = 0;
			for(int j = 0; j < arr.size(); j++){
				if(arr.get(i).equals(arr.get(j))){
					count.set(i, ++a);
				}
			}
//			System.out.println(arr.get(i) + "��ͬ����" + count);
		}
		return count;
	}

	/** List order not maintained **/
	public static ArrayList<String> removeDuplicate(ArrayList arlList) {
		HashSet<String> h = new HashSet<String>(arlList);
		arlList.clear();
		arlList.addAll(h);
		return arlList;
	}

	/** List order maintained **/
	public static ArrayList<String> removeDuplicateWithOrder(ArrayList arlList) {
		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = arlList.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (set.add(element))
				newList.add(element);
		}
		arlList.clear();
		arlList.addAll(newList);
		return arlList;
	}

}
