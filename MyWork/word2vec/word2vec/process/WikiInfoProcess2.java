package word2vec.process;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import base.DirFile;

/**
 * 思路：程序主要分三步对数据进行处理
 * 第一步：匹配每个关键词文件夹中包含标准标签的目录txt，将其保存，否则丢弃
 * 第二步：将每个关键词中的文件按照所属的标准标签的类别分别存储到相应的标准标签文件夹中，按照标准标签组织文件
 * 第三步：将标准标签文件夹中的所有txt内容整合到一个txt文件中，按照课程名组织
 * 附：程序处理结束后，还需将代表相同意思的标签的文本整合，例如：use/application；properties/property/feature；
 * 
 * 几个文件夹是按照指定的几个步骤生成的：
 * “课程术语（维基copy1）”是匹配标准的标签得到的所有txt，按照不同的关键词整理的；
 * “课程术语（维基copy2）”是将所有的txt按照标准的标签放到标签的文件夹里，按照不同的标签整理的；
 * “课程术语（维基copy3）”是将所有的txt按照标签分别全部整理到一个txt文件；
 * “课程术语（维基copy4）”是对于上一步中有些标签代表相同的意思，将其整合，例如：use/application；properties/property/feature；
 */

public class WikiInfoProcess2 {
	
	// 实例
	private static WikiInfoProcess2 wiki = new WikiInfoProcess2();

	// 将标签存储到ArrayList中
	public ArrayList<String> getLabel() {
		// 由于后面的比较使用的是字符串的contain方法，是包含关系，所以不考虑复数形式（properties除外）
		// 因为复数匹配的结果会出现在单数匹配的结果中，导致重复
		// (use/application)>(application)，(properties/property/feature)>(properties)
		String[] tag = { "algorithm", "example", "application", "properties",
				"property", "feature", "definition", "implementation",
				"operation", "history", "type", "deletion", "insertion", "use",
				"description", "method", };
		ArrayList<String> label = new ArrayList<String>();
		for (int i = 0; i < tag.length; i++) {
			label.add(tag[i]);
		}
		return label;
	}
		
	// 复制文件
	// 每次写文件覆盖文件
	public static void copyFile(File sourceFile, File targetFile) throws IOException {
		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);

		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(targetFile); // 覆盖源文件写入
		BufferedOutputStream outBuff = new BufferedOutputStream(output);

		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		// 刷新此缓冲的输出流
		outBuff.flush();

		// 关闭流
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}
	
	// 复制文件
	// 每次写文件是在末尾添加新文件，不是覆盖
	public void copyFileFollow(File sourceFile, File targetFile) throws IOException {
		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);

		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(targetFile, true); // 不覆盖源文件写入
		BufferedOutputStream outBuff = new BufferedOutputStream(output);

		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		// 刷新此缓冲的输出流
		outBuff.flush();

		// 关闭流
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}
	
	// 按照关键词分类数据
	public void labelClassifyStep1(String keyword){
		// 获取标准标签
		ArrayList<String> standardLabel = wiki.getLabel();
		// 定位每个主题文件夹
		String keywordPath = "F:/data_predeal_wiki/课程术语（维基copy1）/Data_structure/" + keyword;
		File[] f = new File(keywordPath).listFiles();
		// 设置目标文件目录
		String targetKeywordPath = "F:/data_predeal_wiki/课程术语（维基new1）/Data_structure/" + keyword;
		File targetPath = new File(targetKeywordPath);
		targetPath.mkdir();		
		// 比较每个主题的目录是不是标准的
		for (int i = 0; i < f.length; i++) {
			// 得到每个txt名字
			String txtName = f[i].getName();
//			System.out.println("txt文件名为：" + txtName);
			// 循环比较是否满足标准标签
			for (int j = 0; j < standardLabel.size(); j++) {
				// 去除.txt后缀名
				String label = txtName.substring(0, txtName.length() - 4).toLowerCase();
				// 如果满足则复制文件
				if (label.contains(standardLabel.get(j))) {
					// 设置复制源文件和目标文件
					File sourceFile = f[i];
					// 命名规则：目标文件名在原来的基础上加上关键词信息：2-3_tree_Properties.txt
					File targetFile = new File(targetKeywordPath  + "/" + standardLabel.get(j) + ".txt");
					try {
						// System.out.println(keyword + "/" + txtName + "复制...");
						copyFileFollow(sourceFile, targetFile);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println(keyword + "/" + txtName + "文件复制出错...");
					}
				} else {
					// System.out.println(keyword + "/" + txtName + "不复制...");
				}
			}
		}
	}
	
	// 标签分类（第二步：处理所有关键词标签）
	// 按照标准标签的名字分别将txt存到这些标签对应的文件夹下
	public void labelClassifyStep2() {
		// 读取所有文件夹名
		String catalog = "F:/data_predeal_wiki/课程术语（维基copy1）/Data_structure/";
		ArrayList<String> keywordSet = DirFile.getFolderFileNamesFromDirectorybyArraylist(catalog);
		// 对每个主题文件夹下的txt文件进行标准标签整合
		for (int i = 0; i < keywordSet.size(); i++) {
			String keyword = keywordSet.get(i);
			labelClassifyStep1(keyword);
		}
	}
	
	// 标签分类（第一步：将与该标签有关的所有文本汇总到一个txt中）
	// 按照标准标签的名字分别将txt存到这些标签对应的文件夹下
	public void labelClassify1(String keyword){
		// 获取标准标签
		ArrayList<String> standardLabel = wiki.getLabel();
		// 定位每个主题文件夹
		String keywordPath = "F:/data_predeal_wiki/课程术语（维基new1）/Data_structure/" + keyword + "/";
		File[] f = new File(keywordPath).listFiles();
		// 设置目标文件目录
		ArrayList<String> targetKeywordPath = new ArrayList<String>();
		// 每个标准标签对应一个路径
		for(int i = 0; i < standardLabel.size(); i++){
			targetKeywordPath.add("F:/data_predeal_wiki/课程术语（维基new1）/Data_structure_tag/" + standardLabel.get(i));
		}
		// 每个标准标签新建文件夹
		for(int i = 0; i < targetKeywordPath.size(); i++){
			File targetPath = new File(targetKeywordPath.get(i));
			if (!targetPath.exists()) {
				targetPath.mkdir();
			} else {
				// System.out.println(targetPath + "目标目录路径存在...");
			}
		}

		// 比较每个主题的目录是不是标准的
		for (int i = 0; i < f.length; i++) {
			// 得到每个txt名字
			String txtName = f[i].getName();
			// System.out.println("txt文件名为：" + txtName);
			// 循环比较是否满足标准标签
			for (int j = 0; j < standardLabel.size(); j++) {
				// 去除.txt后缀名
				String label = txtName.substring(0, txtName.length() - 4).toLowerCase();
				// 如果满足则复制文件
				if (label.contains(standardLabel.get(j))) {
					// 设置复制源文件和目标文件
					File sourceFile = f[i];
					// 命名规则：目标文件名在原来的基础上加上关键词信息：2-3_tree_Properties.txt
					File targetFile = new File(targetKeywordPath.get(j) + "/"
							+ keyword + "_" + f[i].getName());
					try {
						// System.out.println(keyword + "/" + txtName + "复制...");
						copyFile(sourceFile, targetFile);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println(keyword + "/" + txtName + "文件复制出错...");
					}
				} else {
					// System.out.println(keyword + "/" + txtName + "不复制...");
				}
			}
		}
	}

	// 标签分类（第二步：处理所有关键词标签）
	// 按照标准标签的名字分别将txt存到这些标签对应的文件夹下
	public void labelClassify2() {
		// 读取所有文件夹名
		String catalog = "F:/data_predeal_wiki/课程术语（维基new1）/Data_structure/";
		ArrayList<String> keywordSet = DirFile
				.getFolderFileNamesFromDirectorybyArraylist(catalog);
		// 对每个主题文件夹下的txt文件进行标准标签整合
		for (int i = 0; i < keywordSet.size(); i++) {
			String keyword = keywordSet.get(i);
			labelClassify1(keyword);
		}
	}

	// 得到标注过的关键词
	public static ArrayList<String> getTag(){
		ArrayList<String> key = new ArrayList<String>();
		String path = "F:\\data_predeal_wiki\\课程术语（维基new1）\\数据结构标注术语";
		File[] files = new File(path).listFiles();
		for(int i = 0; i < files.length; i++){
			String keyword = files[i].getName();
			key.add(keyword);
//			System.out.println("keyword: " + keyword);
		}
//		System.out.println(files.length);
		return key;
	}
	
	// 得到所有上下位关系和兄弟关系
	public static void getAllFile() throws Exception{
		String sPath = "F:\\data_predeal_wiki\\课程术语（维基new1）\\Data_structure";
		String tPath = "F:\\data_predeal_wiki\\课程术语（维基new1）\\Data_structure_1";
		String excelPath = "F:\\data_predeal_wiki\\课程术语（维基new1）\\数据结构上下位关系.xls";
		Workbook workbook = Workbook.getWorkbook(new File(excelPath));
		Sheet sheet = workbook.getSheet(0);
		int rows = sheet.getRows();
		
		ArrayList<String> key = getTag();
		for(int i = 0; i < key.size(); i++){
			String keyword = key.get(i);
			keyword = keyword.replace("+", "_");
			
			// 将标注关键词的标签数据存储到新的文件夹中
			String keywordPath = sPath + "\\" + keyword;
			if(new File(keywordPath).exists()){
				String keywordPathT = tPath + "\\" + keyword;
				new File(keywordPathT).mkdir();
				File[] keywordFile = new File(keywordPath).listFiles();
				for(int j = 0; j < keywordFile.length; j++){
					String keywordFileName = keywordFile[j].getName();
					String sFile = keywordPath + "\\" + keywordFileName;
					String tFile = keywordPathT + "\\" + keyword + "_" + keywordFileName;
					copyFile(new File(sFile), new File(tFile));
				}
//				System.out.println(keyword);
			}
			
			// 寻找上位关系（父亲）
			for(int j = 1; j < rows; j++){
				String low = sheet.getCell(0, j).getContents();
				String high = sheet.getCell(1, j).getContents();// 上位关系术语
				if(low.equals(keyword)){//存在上位关系
					System.out.println("keyword: " + keyword + "\nhigh: " + high);
					String highPath = sPath + "\\" + high;// 上位关系路径
					String keywordPathT = tPath + "\\" + keyword;// 下位关系路径
					new File(keywordPathT).mkdir();
					if(new File(highPath).exists()){// 判断是否爬取该术语的数据
						File[] keywordFile = new File(highPath).listFiles();
						for(int k = 0; k < keywordFile.length; k++){
							String keywordFileName = keywordFile[k].getName();
							String sFile = highPath + "\\" + keywordFileName;// 源路径
							String tFile = keywordPathT + "\\" +"上位_"+ high + "_" + keywordFileName;// 目标路径
							copyFile(new File(sFile), new File(tFile));
						}
//						System.out.println(keyword);
					} else {
						System.out.println("未爬取该术语的数据:" + high);
					}
				} else {
//					System.out.println(".....");
				}
			}
			
			// 寻找下位关系（儿子）
			for(int j = 1; j < rows; j++){
				String high = sheet.getCell(1, j).getContents();
				String low = sheet.getCell(0, j).getContents();// 下位关系术语
				if(high.equals(keyword)){//存在下位关系
					System.out.println("keyword: " + keyword + "\nlow: " + low);
					String highPath = sPath + "\\" + low;// 下位关系路径
					String keywordPathT = tPath + "\\" + keyword;// 关键词路径
					new File(keywordPathT).mkdir();
					if(new File(highPath).exists()){// 判断是否爬取该术语的数据
						File[] keywordFile = new File(highPath).listFiles();
						for(int k = 0; k < keywordFile.length; k++){
							String keywordFileName = keywordFile[k].getName();
							String sFile = highPath + "\\" + keywordFileName;// 源路径
							String tFile = keywordPathT+"\\"+"下位_"+low+"_"+keywordFileName;// 目标路径
							copyFile(new File(sFile), new File(tFile));
						}
//						System.out.println(keyword);
					} else {
						System.out.println("未爬取该术语的数据:" + low);
					}
				} else {
//					System.out.println(".....");
				}
			}
			
			// 寻找兄弟关系（兄弟）
			for (int j = 1; j < rows; j++) {
				String low = sheet.getCell(0, j).getContents();
				String high = sheet.getCell(1, j).getContents();// 上位关系术语
				if (low.equals(keyword)) {// 存在上位关系
					// 循环搜索该词的上位词的所有下位词，即该词的兄弟节点
					for (int m = 1; m < rows; m++){
						String highbrother = sheet.getCell(1, m).getContents();// 上位
						String brother = sheet.getCell(0, m).getContents();// 下位（兄弟）
						if(high.equals(highbrother)){
							
							String brotherPath = sPath + "\\" + brother;// 兄弟关系路径
							String keywordPathT = tPath + "\\" + keyword;// 关键词路径
							new File(keywordPathT).mkdir();
							if (new File(brotherPath).exists()) {// 判断是否爬取该术语的数据
								File[] keywordFile = new File(brotherPath).listFiles();
								for (int k = 0; k < keywordFile.length; k++) {
									String keywordFileName = keywordFile[k].getName();
									String sFile = brotherPath + "\\" + keywordFileName;// 源路径
									String tFile = keywordPathT + "\\" + "兄弟_" + brother
											+ "_" + keywordFileName;// 目标路径
									copyFile(new File(sFile), new File(tFile));
								}
								// System.out.println(keyword);
							} else {
								System.out.println("未爬取该术语的数据:" + high);
							}
							
						} else {
							
						}
					}
					
					
					
				} else {
					// System.out.println(".....");
				}
			}
			
		}
	}

	
	public void workflow(){
//		String keyword = "(a,b)-tree";
//		String keyword = "2-3_tree";
//		String keyword = "2-3-4_tree";
//		String keyword = "AA_tree";
//		String keyword = "Abstract_data_structure";
//		String keyword = "Adaptive_sort";
//		String keyword = "Adjacency_list";
//		String keyword = "Adjacency_matrix";
//		String keyword = "A-law_algorithm";
//		String keyword = "Best-bin-first_search"; //无文件
		
//		labelClassifyStep1(keyword);
//		labelClassifyStep2();
//		labelClassify2();
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
//		getTag();
		try {
			getAllFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		wiki.workflow();
	}

}
