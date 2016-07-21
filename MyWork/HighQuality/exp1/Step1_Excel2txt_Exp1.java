package exp1;

/**
 * 处理数据用于词向量模型进行词向量训练
 * @author yuanhao
 *
 */

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import excel.ExcelSet;

public class Step1_Excel2txt_Exp1 {
	
	private static String cata = "02-CQA网站中问题答案质量评估";
	private static String path = "F:\\"+cata+"\\数据结构标注20主题";
	private static String pathStep1 = "F:\\"+cata+"\\数据结构标注20主题_Step1";

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		go();
	}
	
	/**
	 * 工作流
	 */
	public static void go() throws Exception{
		step1();
		step2();
		step3();
//		txt3DeleteAnswer();
	}
	
	/**
	 * 读取Excel每行存到一个txt中（txt总数取决于碎片1136）
	 */
	public static void step1() throws Exception{
		new File(pathStep1).mkdir();
		new File(pathStep1 + "\\txt1FromExcel\\").mkdir();
		ArrayList<String> keyword = getKeyword(path);// 关键词
		for(int i = 0; i < keyword.size(); i++){
			String fileName = keyword.get(i);//得到文件夹名
			String soucrePath = path + "\\" + fileName + "\\" + fileName + "-tag_filtering.xls";// 解析excel保存路径
			String targetPath = path + "\\" + fileName + "\\" + fileName + "_word2vec_QorA.xls";// 生成excel保存路径
			String txtpath1 = pathStep1 + "\\txt1FromExcel\\" + fileName;// 生成txt保存路径
			new File(txtpath1).mkdir();
			int questionNumber =  getQuestionNumber(fileName);
			mergeQA1(fileName, questionNumber, soucrePath, targetPath);
			createTxt(fileName, targetPath, txtpath1);
		}
	}
	
	/**
	 * 将Excel的某个问题和它下面的每个回答存到一个txt中（txt总数取决于答案总数760）
	 */
	public static void step2() throws Exception{
		new File(pathStep1).mkdir();
		new File(pathStep1 + "\\txt2FromExcel\\").mkdir();
		ArrayList<String> keyword = getKeyword(path);// 关键词
		for(int i = 0; i < keyword.size(); i++){
			String fileName = keyword.get(i);//得到文件夹名
			String soucrePath = path + "\\" + fileName + "\\" + fileName + "-tag_filtering.xls";// 解析excel保存路径
			String targetPath = path + "\\" + fileName + "\\" + fileName + "_word2vec_QA.xls";// 生成excel保存路径
			String txtpath2 = pathStep1 + "\\txt2FromExcel\\" + fileName;// 生成txt保存路径
			new File(txtpath2).mkdir();
			int questionNumber =  getQuestionNumber(fileName);
			mergeQA2(fileName, questionNumber, soucrePath, targetPath);
			createTxt(fileName, targetPath, txtpath2);
		}
	}
	
	
	/**
	 * 将Excel的某个问题和它下面的所有回答存到一个txt中（txt总数取决于问题总数396）（有4个问题有问题，没有获取到，原则上是400个问题）
	 */
	public static void step3() throws Exception{
		new File(pathStep1).mkdir();
		new File(pathStep1 + "\\txt3FromExcel\\").mkdir();
		ArrayList<String> keyword = getKeyword(path);// 关键词
		for(int i = 0; i < keyword.size(); i++){
			String fileName = keyword.get(i);//得到文件夹名
			String soucrePath = path + "\\" + fileName + "\\" + fileName + "-tag_filtering.xls";// 解析excel保存路径
			String targetPath = path + "\\" + fileName + "\\" + fileName + "_word2vec_QAS.xls";// 生成excel保存路径
			String txtpath3 = pathStep1 + "\\txt3FromExcel\\" + fileName;// 生成txt保存路径
			new File(txtpath3).mkdir();
			int questionNumber =  getQuestionNumber(fileName);
			mergeQA3(fileName, questionNumber, soucrePath, targetPath);
			createTxt(fileName, targetPath, txtpath3);
		}
	}
	
	/**
	 * 将Excel的某个问题和它下面的所有回答存到一个txt中（txt总数取决于问题总数396）（有4个问题有问题，没有获取到，原则上是400个问题）
	 */
	public static void step1Old() throws Exception{
		ArrayList<String> keyword = getKeyword(path);// 关键词
		for(int i = 0; i < keyword.size(); i++){
			String fileName = keyword.get(i);//得到文件夹名
			String soucrePath = path + "\\" + fileName + "\\" + fileName + "-tag_filtering.xls";// 解析excel保存路径
			String targetPath = path + "\\" + fileName + "\\" + fileName + "_word2vec_QorA.xls";// 生成excel保存路径
			String txtpath1 = path + "\\" + fileName + "\\txt1";// 生成txt保存路径
			int questionNumber =  getQuestionNumber(fileName);
			mergeQA1(fileName, questionNumber, soucrePath, targetPath);
			createTxt(fileName, targetPath, txtpath1);
		}
	}
	
	/**
	 * 将Excel的某个问题和它下面的每个回答存到一个txt中（txt总数取决于答案总数760）
	 */
	public static void step2Old() throws Exception{
		ArrayList<String> keyword = getKeyword(path);// 关键词
		for(int i = 0; i < keyword.size(); i++){
			String fileName = keyword.get(i);//得到文件夹名
			String soucrePath = path + "\\" + fileName + "\\" + fileName + "-tag_filtering.xls";// 解析excel保存路径
			String targetPath = path + "\\" + fileName + "\\" + fileName + "_word2vec_QA.xls";// 生成excel保存路径
			String txtpath2 = path + "\\" + fileName + "\\txt2";// 生成txt保存路径
			int questionNumber =  getQuestionNumber(fileName);
			mergeQA2(fileName, questionNumber, soucrePath, targetPath);
			createTxt(fileName, targetPath, txtpath2);
		}
	}
	
	/**
	 * 将Excel的某个问题和它下面的所有回答存到一个txt中（txt总数取决于问题总数396）（有4个问题有问题，没有获取到，原则上是400个问题）
	 */
	public static void step3Old() throws Exception{
		ArrayList<String> keyword = getKeyword(path);// 关键词
		for(int i = 0; i < keyword.size(); i++){
			String fileName = keyword.get(i);//得到文件夹名
			String soucrePath = path + "\\" + fileName + "\\" + fileName + "-tag_filtering.xls";// 解析excel保存路径
			String targetPath = path + "\\" + fileName + "\\" + fileName + "_word2vec_QAS.xls";// 生成excel保存路径
			String txtpath3 = path + "\\" + fileName + "\\txt3";// 生成txt保存路径
			int questionNumber =  getQuestionNumber(fileName);
			mergeQA3(fileName, questionNumber, soucrePath, targetPath);
			createTxt(fileName, targetPath, txtpath3);
		}
	}
	
	/**
	 * 实现功能：获取主题的问题总数
	 */
	public static int getQuestionNumber(String keyword) throws Exception {
		String soucrePath = path + "\\" + keyword + "\\" + keyword + "-tag_filtering.xls";
		// 读取原来的表格
		Workbook sourceWorkbook = Workbook.getWorkbook(new File(soucrePath));
		Sheet sourceSheet = sourceWorkbook.getSheet(0);
		int rows = sourceSheet.getRows();
		String seq = sourceSheet.getCell(12, rows - 1).getContents();// 问题编号所在的列
		int questionNumber = Integer.parseInt(seq) + 1;
		System.out.println(keyword + "主题下含有的问题总数是：" + questionNumber);
		return questionNumber;
	}
	
	/**
	 * 实现功能：将问题内容和回答内容合并(Excel每行保存到一个txt)
	 */
	public static void mergeQA1(String keyword, int questionNumber, String soucrePath, String targetPath) throws Exception {
//		String soucrePath = path + "\\" + keyword + "\\" + keyword + "-tag_filtering.xls";
//		String targetPath = path + "\\" + keyword + "\\" + keyword + "_word2vec_QorA.xls";
		// 读取原来的表格
		Workbook sourceWorkbook = Workbook.getWorkbook(new File(soucrePath));
		Sheet sourceSheet = sourceWorkbook.getSheet(0);
		int rows = sourceSheet.getRows();
		// 生成新的表格
		WritableWorkbook targetWorkbook = Workbook.createWorkbook(new File(targetPath));
		WritableSheet targetSheet = targetWorkbook.createSheet("word2vec", 0);
		WritableCellFormat wcf_center = ExcelSet.setCenterText();   //设置单元格正文格式
		// 记录新的行数，用于Excel表格的写入
		int row = -1;
		for(int j = 1; j < rows; j++){
			// 得到需要的信息
			String qa = sourceSheet.getCell(0, j).getContents();
			String content = sourceSheet.getCell(1, j).getContents();
			content = content.replace("Expanded information：", "");
			content = content.replace("该问题网页不存在问题的附加信息...", "");
			content = content.replace("★", "");
			content = content.replace("�", "");
			content = content.replaceAll("\n", "");
			row++; // 新的Excel表格行数 +1
			targetSheet.setRowView(row, 800, false);// 设置行高
			// 将问题写到新的Excel表格中
			targetSheet.addCell(new Label(0, row, qa, wcf_center));
			targetSheet.addCell(new Label(1, row, content, wcf_center));
		}
		ExcelSet.close(targetWorkbook);

	}
	
	/**
	 * 实现功能：将问题内容和回答内容合并(问题和每一条回答)
	 */
	public static void mergeQA2(String keyword, int questionNumber, String soucrePath, String targetPath) throws Exception {
//		String soucrePath = path + "\\" + keyword + "\\" + keyword + "-tag_filtering.xls";
//		String targetPath = path + "\\" + keyword + "\\" + keyword + "_word2vec_QA.xls";
		// 读取原来的表格
		Workbook sourceWorkbook = Workbook.getWorkbook(new File(soucrePath));
		Sheet sourceSheet = sourceWorkbook.getSheet(0);
		int rows = sourceSheet.getRows();
		// 生成新的表格
		WritableWorkbook targetWorkbook = Workbook.createWorkbook(new File(targetPath));
		WritableSheet targetSheet = targetWorkbook.createSheet("word2vec", 0);
		WritableCellFormat wcf_center = ExcelSet.setCenterText();   //设置单元格正文格式
		// 记录新的行数，用于Excel表格的写入
		int row = -1;
		// 判断是不是同一问题的，Array有37个问题
		for(int i = 0; i < questionNumber; i++){
			String questionContent = "";//每个问题有一个问题内容
			// 读取数据，合并问题和回答
			for(int j = 1; j < rows; j++){
				// 得到需要的信息
				String qa = sourceSheet.getCell(0, j).getContents();
				String content = sourceSheet.getCell(1, j).getContents();
				String sequence = sourceSheet.getCell(12, j).getContents();
//				String sequence = sourceSheet.getCell(8, j).getContents();
//				System.out.println(sequence);
				
				int questionId = Integer.parseInt(sequence);// 得到Excel表格中每个问题的序号
				if(questionId == i){// 对于同一问题
					String[] arr = qa.split("_");
					if(arr.length == 1){// 判断是否为问题，若是，则赋值，由于是从第一行开始读取，所以始终不会为空
						questionContent = content;
//						System.out.println("问题"+i+"是： " + questionid);
//						System.out.println("问题内容"+i+"是： " + questionContent);
					}
					if(arr.length == 2){// 判断是否为可以标注的答案，合并标签
						String mergeId = qa;
						String mergeContent = questionContent + "\n" + content;
						mergeContent = mergeContent.replace("Expanded information：", "");
						mergeContent = mergeContent.replace("该问题网页不存在问题的附加信息...", "");
						mergeContent = mergeContent.replace("★", "");
						mergeContent = mergeContent.replace("�", "");
						mergeContent = mergeContent.replaceAll("\n", "");
//						System.out.println("回答"+i+"是： " + mergeId);
//						System.out.println("回答内容"+i+"是： " + mergeContent);
						row++; // 新的Excel表格行数 +1
						targetSheet.setRowView(row, 800, false);// 设置行高
						// 将问题写到新的Excel表格中
						targetSheet.addCell(new Label(0, row, mergeId, wcf_center));
						targetSheet.addCell(new Label(1, row, mergeContent, wcf_center));
					} 
				} else {
//					System.out.println("不是第" + i + "个问题...");
				}

			}
		}
		ExcelSet.close(targetWorkbook);
		
	}
	
	/**
	 * 实现功能：将问题内容和回答内容合并(问题和所有的回答)
	 */
	public static void mergeQA3(String keyword, int questionNumber, String soucrePath, String targetPath) throws Exception {
//		String soucrePath = path + "\\" + keyword + "\\" + keyword + "-tag_filtering.xls";
//		String targetPath = path + "\\" + keyword + "\\" + keyword + "_word2vec_QAS.xls";
		// 读取原来的表格
		Workbook sourceWorkbook = Workbook.getWorkbook(new File(soucrePath));
		Sheet sourceSheet = sourceWorkbook.getSheet(0);
		int rows = sourceSheet.getRows();
		// 生成新的表格
		WritableWorkbook targetWorkbook = Workbook.createWorkbook(new File(targetPath));
		WritableSheet targetSheet = targetWorkbook.createSheet("word2vec", 0);
		WritableCellFormat wcf_center = ExcelSet.setCenterText();   //设置单元格正文格式
		// 记录新的行数，用于Excel表格的写入
		int row = -1;
		// 判断是不是同一问题的，Array有37个问题
		for(int i = 0; i < questionNumber; i++){
			String questionContent = "";//每个问题有一个问题内容
			String mergeId = "";// 合并后的ID
			String mergeContent = "";// 合并后的Content
			// 读取数据，合并问题和回答
			for(int j = 1; j < rows; j++){
				// 得到需要的信息
				String qa = sourceSheet.getCell(0, j).getContents();
				String content = sourceSheet.getCell(1, j).getContents();
				String sequence = sourceSheet.getCell(12, j).getContents();
//				String sequence = sourceSheet.getCell(8, j).getContents();
//				System.out.println(sequence);
				
				int questionId = Integer.parseInt(sequence);// 得到Excel表格中每个问题的序号
				if(questionId == i){// 对于同一问题
					String[] arr = qa.split("_");
					if(arr.length == 1){// 判断是否为问题，若是，则赋值，由于是从第一行开始读取，所以始终不会为空
						questionContent = content;
						mergeId = qa;
//						System.out.println("问题"+i+"是： " + questionid);
//						System.out.println("问题内容"+i+"是： " + questionContent);
					}
					if(arr.length == 2){// 判断是否为答案，合并所有答案
						mergeContent = mergeContent + "\n" + content;
						mergeContent = mergeContent.replace("Expanded information：", "");
						mergeContent = mergeContent.replace("该问题网页不存在问题的附加信息...", "");
						mergeContent = mergeContent.replace("★", "");
						mergeContent = mergeContent.replace("�", "");
						mergeContent = mergeContent.replaceAll("\n", "");
//						System.out.println("回答" + i + "是： " + mergeId);
//						System.out.println("回答内容" + i + "是： " + mergeContent);
						
					} 
				} else {
//					System.out.println("不是第" + i + "个问题...");
				}
			}
			// 每一个问题存到一行中
			row++; // 新的Excel表格行数 +1
			targetSheet.setRowView(row, 800, false);// 设置行高
			// 将问题写到新的Excel表格中
			targetSheet.addCell(new Label(0, row, mergeId, wcf_center));
			mergeContent = questionContent + mergeContent;
			targetSheet.addCell(new Label(1, row, mergeContent, wcf_center));
		}
		ExcelSet.close(targetWorkbook);
		
	}
	
	/**
	 * 实现功能：将问题内容和回答内容合并
	 * @param filepath
	 */
	public static void createTxt(String keyword, String sourcePath, String txtPath) throws Exception {
//		String soucrePath = path + "\\" + keyword + "\\" + keyword + "_word2vec_QorA.xls";
//		String txtpath1 = path + "\\" + keyword + "\\txt1";
		// 读取原来的表格
		Workbook sourceWorkbook = Workbook.getWorkbook(new File(sourcePath));
		Sheet sourceSheet = sourceWorkbook.getSheet(0);
		int rows = sourceSheet.getRows();
		// 生成相应TXT文件
		new File(txtPath).mkdir();
		for(int i = 0; i < rows; i++){
			String id = sourceSheet.getCell(0, i).getContents();
			String content = sourceSheet.getCell(1, i).getContents();
//			System.out.println(content);
			String filePath = txtPath + "\\" + id + ".txt";
			File txtFile = new File(filePath);
			FileWriter output = new FileWriter(txtFile);
			output.write(content);
			output.close();
		}
	}

	/**
	 * 得到根目录下子文件夹的名字
	 */
	public static ArrayList<String> getKeyword(String filePath) throws Exception{
		ArrayList<String> keyword = new ArrayList<String>();
		File[] files = new File(filePath).listFiles();
		for(int i = 0; i < files.length; i++){
			String fileName = files[i].getName();//得到文件夹名
			keyword.add(fileName);
		}
		return keyword;
	}
	
	/**
	 * 因为-tag_filtering.xls文件有问题，所以txt3中会有答案的信息，应该删除
	 */
	public static void txt3DeleteAnswer() throws Exception{
		ArrayList<String> keyword = getKeyword(path);// 关键词
		for(int i = 0; i < keyword.size(); i++){
			String fileName = keyword.get(i);//得到文件夹名
			String txtpath3 = path + "\\" + fileName + "\\txt3";// 生成txt保存路径
			File[] files = new File(txtpath3).listFiles();
			for ( int j = 0; j < files.length; j++ ) {
				String txtName = files[j].getName();
				String txtPath = txtpath3 + "\\" + txtName;
				if(txtName.equals(".txt")){//删除无用文件
					new File(txtPath).delete();
				}
				String[] arr = txtName.split("_");
				if(arr.length == 2){//删除答案文件
					new File(txtPath).delete();
				}
			}
		}
	}

}

