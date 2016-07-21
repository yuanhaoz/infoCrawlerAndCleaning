package experiment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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

public class WikiInfoProcess {
	
	// 实例
	private static WikiInfoProcess wiki = new WikiInfoProcess();

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
	
	// 复制所有文件（第一步：匹配单个关键词的txt）
	// 匹配所有满足标注标签的文件
	public void labelMatchStep1(String keyword) {
		// 获取标准标签
		ArrayList<String> standardLabel = wiki.getLabel();
		// 定位每个主题文件夹
		String keywordPath = "F:/课程术语（维基）/Data_structure/" + keyword + "/";
		File[] f = new File(keywordPath).listFiles();
		// 设置目标文件目录
		String targetKeywordPath = "F:/课程术语（维基copy1）/Data_structure/" + keyword;
		File targetPath = new File(targetKeywordPath);
		if(!targetPath.exists()){
			targetPath.mkdir();
		} else {
//			System.out.println(targetPath + "目标目录路径存在...");
		}
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
					File targetFile = new File(targetKeywordPath+"/"+f[i].getName());
					try {
//						System.out.println(keyword + "/" + txtName + "复制...");
						copyFile(sourceFile, targetFile);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println(keyword + "/" + txtName + "文件复制出错...");
					}
				} else {
//					System.out.println(keyword + "/" + txtName + "不复制...");
				}
			}
		}
	}
	
	// 复制所有文件（第二步：对所有关键词进行处理）
	// 匹配所有满足标注标签的文件
	public void labelMatchStep2() {
		// 读取所有文件夹名
		String catalog = "F:/课程术语（维基）/Data_structure/";
		ArrayList<String> keywordSet = DirFile
				.getFolderFileNamesFromDirectorybyArraylist(catalog);
		// 对每个主题文件夹下的txt文件进行比较复制
		for (int i = 0; i < keywordSet.size(); i++) {
			String keyword = keywordSet.get(i);
//			System.out.println(keyword);
			// 定位每个主题文件夹
			labelMatchStep1(keyword);
		}
	}
		
	// 复制文件
	// 每次写文件覆盖文件
	public void copyFile(File sourceFile, File targetFile)
			throws IOException {
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
	
	// 标签分类（第一步：将与该标签有关的所有文本汇总到一个txt中）
	// 按照标准标签的名字分别将txt存到这些标签对应的文件夹下
	public void labelClassifyStep1(String keyword){
		// 获取标准标签
		ArrayList<String> standardLabel = wiki.getLabel();
		// 定位每个主题文件夹
		String keywordPath = "F:/课程术语（维基copy1）/Data_structure/" + keyword + "/";
		File[] f = new File(keywordPath).listFiles();
		// 设置目标文件目录
		ArrayList<String> targetKeywordPath = new ArrayList<String>();
		// 每个标准标签对应一个路径
		for(int i = 0; i < standardLabel.size(); i++){
			targetKeywordPath.add("F:/课程术语（维基copy2）/Data_structure/" + standardLabel.get(i));
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
					// 命名规则1：目标文件名与原来相同：Properties.txt
//					File targetFile = new File(targetKeywordPath.get(j) + "/" + f[i].getName());
					// 命名规则2：目标文件名在原来的基础上加上关键词信息：2-3_tree_Properties.txt
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
	public void labelClassifyStep2() {
		// 读取所有文件夹名
		String catalog = "F:/课程术语（维基copy1）/Data_structure/";
		ArrayList<String> keywordSet = DirFile
				.getFolderFileNamesFromDirectorybyArraylist(catalog);
		// 对每个主题文件夹下的txt文件进行标准标签整合
		for (int i = 0; i < keywordSet.size(); i++) {
			String keyword = keywordSet.get(i);
			labelClassifyStep1(keyword);
		}
	}
	
	// 标签整合（第一步：处理单个标签的数据到一个txt中）
	// 对每个标签文件夹下的txt文件按照标签整合到一个txt文件中
	public void labelJoinStep1(String label){
		String sourcePath = "F:/课程术语（维基copy2）/Data_structure/" + label;
		File[] f = new File(sourcePath).listFiles();
		// 设置目标文件目录
		String targetPath = "F:/课程术语（维基copy3）/Data_structure/";
		
		// 比较每个主题的目录是不是标准的
		for (int i = 0; i < f.length; i++) {
			// 设置复制源文件和目标文件
			File sourceFile = f[i];
			File targetFile = new File(targetPath + label + ".txt");
			try {
				copyFileFollow(sourceFile, targetFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// 标签整合（第二步：处理所有标签的数据到对应标签的txt中）
	// 对每个标签文件夹下的txt文件按照标签整合到一个txt文件中
	public void labelJoinStep2() {
		// 读取所有文件夹名
		String catalog = "F:/课程术语（维基copy2）/Data_structure/";
		ArrayList<String> keywordSet = DirFile
				.getFolderFileNamesFromDirectorybyArraylist(catalog);
		// 对每个标签文件夹下的txt文件按照标签整合到一个txt文件中
		for (int i = 0; i < keywordSet.size(); i++) {
			String keyword = keywordSet.get(i);
			labelJoinStep1(keyword);
		}
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
		
//		labelMatchStep1(keyword);
//		labelMatchStep2();
		
//		labelClassifyStep1(keyword);
//		labelClassifyStep2();
		
//		String label = "definition";
//		labelJoinStep1(label);
		labelJoinStep2();
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		wiki.workflow();
	}

}
