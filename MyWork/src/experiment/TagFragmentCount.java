package experiment;

/**
 * 统计已经标注的碎片数量
 * 读取文件路径："file/datacollection/tagcount/"下的三门学科标注数据
 * 生成结果：控制台显示输出；在上面路径下生成文件："标注碎片数量统计.txt"
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import jxl.Sheet;
import jxl.Workbook;

public class TagFragmentCount {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		TagFragmentCount a = new TagFragmentCount();
		int dm = a.count("file/datacollection/tagcount/Data_mining/");
		int ds = a.count("file/datacollection/tagcount/Data_structure/");
		int cn = a.count("file/datacollection/tagcount/Computer_network/");
		int count = dm + ds + cn; //总数
		String syso = "标注碎片数量统计结果\n"+"Data_structure:"+ds+"\n"+"Data_mining:"+dm+"\n"
				+"Computer_network:"+cn+"\n"+"count:"+count;
		System.out.println(syso);
		File summary = new File("file/datacollection/tagcount/标注碎片数量统计.txt");
		if(!summary.exists()){
			summary.createNewFile();
		}else{
			System.out.println("...");
		}
		//写文件
		BufferedWriter output = new BufferedWriter(new FileWriter(summary));
		output.write(syso);
		output.close();
	}
	
	/**
	 * 统计已经标注的碎片数量（数据结构）
	 * 目标：标注10000条数据（好忧伤~~~）
	 */
	public int count(String dir) throws Exception {
		File f = new File(dir); //dir为："F:/data_structure/"
		File childs[]=f.listFiles();
		int each = 0; // 得到已标注碎片数量
		for (int i = 0; i < childs.length; i++){
			String keyword = childs[i].getName(); //得到关键词
			String filepath = dir + keyword + "/"; //得到关键词文件夹路径
			String filename = filepath + keyword + "-tag_filtering.xls"; //标注Excel文件
			System.out.println(filename);
			Workbook workbook = Workbook.getWorkbook(new File(filename)); //读取Excel表
			Sheet sheet = workbook.getSheet(0);
			int row = sheet.getRows(); //得到行数
//			System.out.println(row);
			for (int j = 1; j < row; j++){ //从第二行开始分析
				String tag = sheet.getCell(13, j).getContents();
//				System.out.println("第" + (j+1) + "行:" + tag);
				if (tag.equals("1")) { // 将可以标注的数据保存到文本中
					each++;
				}
			}
		}
		System.out.println("已标注碎片总数为：" + each);
		return each;
	}

}
