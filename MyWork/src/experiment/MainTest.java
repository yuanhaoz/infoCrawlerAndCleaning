package experiment;

import java.io.File;

public class MainTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		InfoToTxt a = new InfoToTxt();
		DealTagExcel b = new DealTagExcel();
		
		// 将数据结构的所有写到txt中
//		a.select();
		
		// 将数据结构的标注过多的数据写到txt中
		// 测试单个例子
//		a.selectTag(); //对"Absolute+deviation"进行处理
		// 方法1
//		String course = "data_structure3";
//		String dir = "F:/" + course +"/";
//		File f = new File(dir); //dir为："F:/data_structure/"
//		File childs[] = f.listFiles();
//		for (int i = 0; i < childs.length; i++){
//			String keyword = childs[i].getName(); //得到关键词
//			a.selectTag(course, keyword);
//		}
		// 方法2
//		String courseDir = "F:/data_structure/"; //对数据结构所有关键词处理
//		a.selectTag(courseDir);
		
		// 统计已经标注的碎片数量
//		String courseDir = "F:/data_structure/"; //对数据结构所有关键词处理
//		a.count(courseDir);
		
		//处理标注过的Excel，将答案内容等信息改正
//		String dir = "G:/快盘1/爬取数据/数据标注（进行中）/数据挖掘/";
//		String dir = "G:/快盘1/爬取数据/数据标注（进行中）/数据结构/";
		String dir = "G:/快盘1/爬取数据/数据标注（进行中）/计算机网络/";
		b.dealTagExcel(dir);
	}

}
