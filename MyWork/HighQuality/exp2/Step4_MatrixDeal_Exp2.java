package exp2;

/**
 * 平铺矩阵，matlab实现
 * 计算相似矩阵，matlab实现
 */
import java.io.File;

import matlabMethod.matrixDeal;
import base.DirFile;

public class Step4_MatrixDeal_Exp2 {
	
	private static String cata = "02-CQA网站中问题答案质量评估";
	private static String path = "F:\\"+cata+"\\数据结构标注20主题_Step3";

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		////////////////////////////////////////////
		/**  主要是matlab的程序(transferMetrixCatlogNew.m)，这个程序调用的有点问题      */
		///////////////////////////////////////////
		workflow();
	}
	
	public static void workflow() throws Exception{
		// 删除空文件
		String wikiPath = "F:\\高质量\\二叉树\\word2vecNew\\wiki\\file3_postdeal_step1";
		String wikiPathOut = "F:\\高质量\\二叉树\\word2vecNew\\wiki\\summary";
		getWikiSummary(wikiPath, wikiPathOut);
		
		// 将每个主题下的所有文本复制到一个文件夹下面
//		String sourcePath = path + "\\txt" + id + "Tokenizer\\file3_postdeal_step2";
//		String targetPath = path + "\\txt" + id + "Tokenizer\\file4_matrixdeal_step1";
//		mergeTxt(sourcePath, targetPath);
		
		// 读取源文件夹下面的所有矩阵TXT，统计其中行数和列数，并得出其中的最大值和最小值
//		String targetPath2 = path + "\\txt" + id + "Tokenizer\\file4_matrixdeal_step1_info.txt";
//		getMatrixSize(targetPath, targetPath2);
		
		// 拓展所有文件
//		String source = "F:\\高质量\\数据结构标注20主题_Step2\\txt1Tokenizer\\file3_postdeal_step2";
//		String targetAdd = "F:\\高质量\\数据结构标注20主题_Step2\\txt1Tokenizer\\file4_matrixdeal_step3(add)";
//		String targetTile = "F:\\高质量\\数据结构标注20主题_Step2\\txt1Tokenizer\\file4_matrixdeal_step3(tile)";
//		int matrix_row = 100;
//		int matrix_column = 50;
//		matrixTileALL(source, targetTile, matrix_row, matrix_column);
//		matrixAddALL(source, targetAdd, matrix_row, matrix_column);
	}
	
	/**
	 * 实现功能：将wiki中的summary文件拷贝到新的文件夹中
	 */
	public static void getWikiSummary(String wikiPath, String wikiPathOut){
//		String wikiPath = "F:\\高质量\\二叉树\\word2vecNew\\wiki\\file3_postdeal_step1";
//		String wikiPath = "F:\\高质量\\二叉树\\word2vecNew\\wiki\\summary";
		new File(wikiPathOut).mkdir();
		File[] fs = new File(wikiPath).listFiles();
		for(int i = 0; i < fs.length; i++){
			String keyword = fs[i].getName();
			String sumPath = wikiPath + "\\" + keyword;
//			String sumPathOut = wikiPathOut + "\\" + keyword;
//			new File(sumPathOut).mkdir();
//			DirFile.copyFile(sumPath + "\\" + keyword + "_summary.txt", sumPathOut + "\\" + keyword + "_summary.txt");
			DirFile.copyFile(sumPath + "\\" + keyword + "_summary.txt", wikiPathOut + "\\" + keyword + "_summary.txt");
		}
	}
	
	/**
	 * 实现功能：将每个主题下的所有文本复制到一个文件夹下面
	 */
	public static void mergeTxt(String sourcePath, String targetPath){
		System.out.println("---------------------------------------------");
//		String targetPath = "F:\\高质量\\数据结构标注20主题_Step2\\txt1Tokenizer\\file4_matrixdeal_step1";
		new File(targetPath).mkdir();
//		String sourcePath = "F:\\高质量\\数据结构标注20主题_Step2\\txt1Tokenizer\\file3_postdeal_step1";
		File[] files = new File(sourcePath).listFiles();
		for(int i = 0; i < files.length; i++){
			String keyword = files[i].getName();// 得到文件夹名字
			System.out.println("正在处理文件：" + keyword);
			String keywordPath = sourcePath + "\\" + keyword;
			File[] keywordFile = new File(keywordPath).listFiles();
			for(int j = 0; j < keywordFile.length; j++){
				String fileName = keywordFile[j].getName();// 得到文件名字
				String filePath = keywordPath + "\\" + fileName;
				String targetFilePath = targetPath + "\\" + fileName;
				DirFile.copyFile(filePath, targetFilePath);
			}
		}
	}
	
	/**
	 * 读取源文件夹下面的所有矩阵TXT，统计其中行数和列数，并得出其中的最大值和最小值
	 * size_maxmin_Method在matlab中定义如下，有返回值，因此在java调用该函数时第一个参数为返回值的个数，设为8表示返回所有返回值
	 * 到Object[]对象数组中
	 * function [min_x, min_y, max_x, max_y, min_x_name, min_y_name, max_x_name, max_y_name]
	 *  = size_maxmin_Method(sourcePath, targetPath)
	 *  
	 * @throws Exception
	 */
	public static void getMatrixSize(String sourcePath, String targetPath) throws Exception{
		System.out.println("---------------------------------------------");
		System.out.println("正在获取矩阵大小...");
		matrixDeal mat = new matrixDeal();
//		String sourcePath = "F:\\word2vec\\data_structure_tag_txt\\test";
//		String targetPath = "F:\\word2vec\\data_structure_tag_txt\\test.txt";
		Object[] source = {sourcePath};
		Object[] target = {targetPath};
		// 读取源文件夹下面的所有矩阵TXT，统计其中行数和列数，并得出其中的最大值和最小值
		Object[] info = mat.size_maxmin_Method(8, source, target);
		System.out.println("info length:" + info.length);
		System.out.println("info content:");
		for(int i = 0; i < 8; i++){
			System.out.println("第" + (i+1) + "个返回值为：" + info[i]);
		}
	}
	
	/**
	 *  调用matlab矩阵处理函数拓展矩阵（拓展方式为：平铺）
	 *  需要将路径分装为object[]，以供matlab函数调用
	 * @param source：源文件夹路径（带拓展文件），如 array(文件夹)--->array0_0.txt(待拓展文件)
	 * @param target：目标文件夹路径（拓展后文件）
	 * @param matrix_row：拓展后的矩阵行数
	 * @param matrix_column：拓展后的矩阵列数
	 * @throws Exception
	 */
	public static void matrixTile(String source, String target, int matrix_row, int matrix_column) throws Exception{
		matrixDeal mat = new matrixDeal();
		Object[] sourcePath = {source};
		Object[] targetPath = {target};
//		mat.transferMetrixMethodTile(sourcePath, targetPath, matrix_row, matrix_column);
		mat.transferMetrixMethodTileNew(sourcePath, targetPath, matrix_row, matrix_column);
		
	}

	/**
	 *  调用matlab矩阵处理函数拓展矩阵（拓展方式为：加1）
	 *  需要将路径分装为object[]，以供matlab函数调用
	 * @param source：源文件夹路径（带拓展文件），如 array(文件夹)--->array0_0.txt(待拓展文件)
	 * @param target：目标文件夹路径（拓展后文件）
	 * @param matrix_row：拓展后的矩阵行数
	 * @param matrix_column：拓展后的矩阵列数
	 * @throws Exception
	 */
	public static void matrixAdd(String source, String target, int matrix_row, int matrix_column) throws Exception{
		matrixDeal mat = new matrixDeal();
		Object[] sourcePath = {source};
		Object[] targetPath = {target};
//		mat.transferMetrixMethodAdd1(sourcePath, targetPath, matrix_row, matrix_column);
		mat.transferMetrixMethodAdd1New(sourcePath, targetPath, matrix_row, matrix_column);
		
	}
	
	/**
	 * 两层文件夹目录对矩阵进行拓展（拓展方式为：平铺）
	 * @param source：源文件夹路径（包含多个主题的带拓展文件，如   data(文件夹)--->array(文件夹)--->array0_0.txt(待拓展文件)）
	 * @param target：目标文件夹路径（组织结构和源文件夹相同）
	 * @param matrix_row：拓展后的矩阵行数
	 * @param matrix_column：拓展后的矩阵列数
	 * @throws Exception
	 */
	public static void matrixTileALL(String source, String target, int matrix_row, int matrix_column) throws Exception{
		System.out.println("---------------------------------------------");
		File[] file = new File(source).listFiles();
		new File(target).mkdir();
		for(int i = 0; i < file.length; i++){
			String fileName = file[i].getName();
			String sourcePath = source + "\\" + fileName;
			String targetPath = target + "\\" + fileName;
			System.out.println("正在处理：" + sourcePath);
			System.out.println("处理后路径：" + targetPath);
			new File(targetPath).mkdir();
			matrixTile(sourcePath, targetPath, matrix_row, matrix_column);
		}
	}

	/**
	 * 两层文件夹目录对矩阵进行拓展（拓展方式为：加1）
	 * @param source：源文件夹路径（包含多个主题的带拓展文件，如   data(文件夹)--->array(文件夹)--->array0_0.txt(待拓展文件)）
	 * @param target：目标文件夹路径（组织结构和源文件夹相同）
	 * @param matrix_row：拓展后的矩阵行数
	 * @param matrix_column：拓展后的矩阵列数
	 * @throws Exception
	 */
	public static void matrixAddALL(String source, String target, int matrix_row, int matrix_column) throws Exception{
		System.out.println("---------------------------------------------");
		File[] file = new File(source).listFiles();
		new File(target).mkdir();
		for(int i = 0; i < file.length; i++){
			String fileName = file[i].getName();
			String sourcePath = source + "\\" + fileName;
			String targetPath = target + "\\" + fileName;
			System.out.println("正在处理：" + sourcePath);
			System.out.println("处理后路径：" + targetPath);
			new File(targetPath).mkdir();
			matrixAdd(sourcePath, targetPath, matrix_row, matrix_column);
		}
	}
	

}
