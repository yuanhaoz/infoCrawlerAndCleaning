package experiment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;

import org.junit.Test;

import basic.MysqlConnection;

public class InfoToTxt {

	//导入mysqloperation类
    MysqlConnection mysqlCon = new MysqlConnection();
    //准备sql语句
    private String sql;
    //影响行数（数据变更后，影响行数都是大于0，等于0时没变更，所以说如果变更失败，那么影响行数必定为负）
    private int i = -1;
    //结果集
    private ResultSet rs;
    
	/*
     * 遍历数据，写到text中
     * */
    @Test
    public void select() {
    	ArrayList<String> QA = new ArrayList<String>();
    	ArrayList<String> Content = new ArrayList<String>();
    	String chWord = "该问题网页不存在问题的附加信息...";
        //创建sql语句
        sql = "select * from fragment_filtering_datastructurenew";
        //执行sql语句
        mysqlCon.doSql(sql, null);
        //获取结果集
        rs = mysqlCon.getRS();
        //判断结果集是否为空
        if(rs != null){
            try {
                //将光标移动到结果集末端,注意此处不能使用rs.afterLast();否则为空值。
                rs.last();
                //获取结果集行数
                i = rs.getRow();
                //判断结果集是否存在
                if(i > 0){
                    //将光标移动到结果集前端
                    rs.beforeFirst();
                    //循环遍历所有行数
                    while(rs.next()){
                        //遍历每行元素的内容
                        QA.add(rs.getString("QA"));
                        String content = rs.getString("Content");
                        if(content.contains("★")){
    						content = content.substring(1, content.length());
    					}
                        if(content.contains(chWord)){
        					content = content.replace(content.substring(content.indexOf(chWord), 
        							content.indexOf(chWord) + chWord.length()), "");
        				}
                        Content.add(content);
                        //在控制台打印出结果
//                        System.out.println("QA:" + QA + "\tContent:" + Content);
                    }
                }else{
                    System.out.println("结果集为空！");
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            System.out.println("结果集不存在！");
        }
        //关闭链接
        mysqlCon.getClose();
        
        //内容写进文件
        for (int i = 0; i < QA.size(); i++) {
        	String filePath = "f:/experiment/" + QA.get(i) + ".txt";
        	File file = new File(filePath);
        	InfoToTxt a = new InfoToTxt();
        	a.writeToTxt(file, Content.get(i));
        }
    }
    
    /**
     * 测试单个关键词例子：
	 * 统计可以标注的碎片（针对一个关键词Absolute+deviation）
	 * @throws Exception
	 */
	public void selectTag() throws Exception {
		ArrayList<String> QA = new ArrayList<String>();
		ArrayList<String> Content = new ArrayList<String>();
		String keyword = "Absolute+deviation";
		String filepath = "F:/data_structure3/" + keyword + "/"; // 得到关键词文件夹路径
		String filename = filepath + keyword + "-tag_changed3.xls"; // 标注Excel文件
		System.out.println(filename);
		Workbook workbook = Workbook.getWorkbook(new File(filename)); // 读取Excel表
		Sheet sheet = workbook.getSheet(0);
		int row = sheet.getRows(); // 得到行数
		System.out.println(row);
		String chWord = "该问题网页不存在问题的附加信息...";
		for (int j = 1; j < row; j++) { // 从第二行开始分析
			String tag = sheet.getCell(13, j).getContents();
			System.out.println("第" + (j+1) + "行:" + tag);
			if (tag.equals("1")) { // 将可以标注的数据保存到文本中
				String id = keyword + sheet.getCell(12, j).getContents(); // txt存储文件名
				String content = sheet.getCell(1, j).getContents(); // 对应内容
				if(content.contains("★")){
					content = content.substring(1, content.length());
				}
				if(content.contains(chWord)){
					content = content.replace(content.substring(content.indexOf(chWord), 
							content.indexOf(chWord) + chWord.length()), "");
				}
				QA.add(id);
				Content.add(content);
				System.out.println("QA:" + id + "\tContent:" + content);
			}
		}
		// 将内容写到txt中
		for (int m = 0; m < QA.size(); m++) {
			String txtfilepath = filepath + QA.get(m) + ".txt";
			File file = new File(txtfilepath);
			InfoToTxt a = new InfoToTxt();
			a.writeToTxt(file, Content.get(m));
		}
	
	}

	/**
	 * 方法1：
	 * 统计可以标注的碎片
	 * 输入为某门课程及下面的课程名字
	 */
	public void selectTag(String course, String keyword) throws Exception {
		ArrayList<String> QA = new ArrayList<String>();
		ArrayList<String> Content = new ArrayList<String>();
		String filepath = "F:/" + course + "/" + keyword + "/"; // 得到关键词文件夹路径
		String filename = filepath + keyword + "-tag_changed3.xls"; // 标注Excel文件
		System.out.println(filename);
		Workbook workbook = Workbook.getWorkbook(new File(filename)); // 读取Excel表
		Sheet sheet = workbook.getSheet(0);
		int row = sheet.getRows(); // 得到行数
		System.out.println(row);
		String chWord = "该问题网页不存在问题的附加信息...";
		for (int j = 1; j < row; j++) { // 从第二行开始分析
			String tag = sheet.getCell(13, j).getContents();
			System.out.println("第" + (j+1) + "行:" + tag);
			if (tag.equals("1")) { // 将可以标注的数据保存到文本中
				String id = keyword + sheet.getCell(12, j).getContents(); // txt存储文件名
				String content = sheet.getCell(1, j).getContents(); // 对应内容
				if(content.contains("★")){ // 去除问题的星号
					content = content.substring(1, content.length());
				}
				if(content.contains(chWord)){ // 去除中文
					content = content.replace(content.substring(content.indexOf(chWord), 
							content.indexOf(chWord) + chWord.length()), "");
				}
				QA.add(id);
				Content.add(content);
				System.out.println("QA:" + id + "\tContent:" + content);
			}
		}
		// 将内容写到txt中
		for (int m = 0; m < QA.size(); m++) {
			String txtfilepath = filepath + QA.get(m) + ".txt";
			File file = new File(txtfilepath);
			InfoToTxt a = new InfoToTxt();
			a.writeToTxt(file, Content.get(m));
		}
	
	}

	/**
	 * 方法2:
	 * 统计可以标注的碎片（数据结构）
	 * 读取标注过的数据
	 * 输入课程路径，得到所有碎片（一次性处理）
	 */
	public void selectTag(String dir) throws Exception {
		File f = new File(dir); //dir为："F:/data_structure/"
		File childs[]=f.listFiles();
		String chWord = "该问题网页不存在问题的附加信息...";
		for (int i = 0; i < childs.length; i++){
			String keyword = childs[i].getName(); //得到关键词
			String filepath = dir + keyword + "/"; //得到关键词文件夹路径
			String filename = filepath + keyword + "-tag_changed3.xls"; //标注Excel文件
			System.out.println(filename);
			Workbook workbook = Workbook.getWorkbook(new File(filename)); //读取Excel表
			Sheet sheet = workbook.getSheet(0);
			int row = sheet.getRows(); //得到行数
//			System.out.println(row);
			for (int j = 1; j < row; j++){ //从第二行开始分析
				String tag = sheet.getCell(13, j).getContents();
				System.out.println("第" + (j+1) + "行:" + tag);
				if (tag.equals("1")) { // 将可以标注的数据保存到文本中
					String id = keyword + sheet.getCell(12, j).getContents(); // txt存储文件名
					String content = sheet.getCell(1, j).getContents(); // 对应内容
					if(content.contains("★")){
						content = content.substring(1, content.length());
					}
					if(content.contains(chWord)){
						content = content.replace(content.substring(content.indexOf(chWord), 
								content.indexOf(chWord) + chWord.length()), "");
					}
					System.out.println("QA:" + id + "\tContent:" + content);
					// 写到txt中
					String txtfilepath = filepath + id + ".txt"; // 得到写的文件的路径名
		        	System.out.println("txtfilepath:" + txtfilepath);
		        	File file = new File(txtfilepath);
		        	InfoToTxt a = new InfoToTxt();
		        	a.writeToTxt(file, content);
				}
			}
		}
	}
	
	/**
	 * 统计已经标注的碎片数量（数据结构）
	 * 目标：标注10000条数据（好忧伤~~~）
	 */
	public void count(String dir) throws Exception {
		File f = new File(dir); //dir为："F:/data_structure/"
		File childs[]=f.listFiles();
		int each = 0; // 得到已标注碎片数量
		for (int i = 0; i < childs.length; i++){
			String keyword = childs[i].getName(); //得到关键词
			String filepath = dir + keyword + "/"; //得到关键词文件夹路径
			String filename = filepath + keyword + "-tag_changed3.xls"; //标注Excel文件
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
	}
	
	/**
     * 在Text原有内容中写入新的内容，不覆盖原来内容
     * 读数据库中的“数据结构”表
     * @param file
     * @param txt
     */
	public void writeToTxt(File file, String txt) {
		String str = new String();
		String strNew = new String();
		try {
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//读取原来内容
			BufferedReader input = new BufferedReader(new FileReader(file));
			while ((str = input.readLine()) != null) {
				strNew += str + "\n";
			}
//			System.out.println("原来内容：" + strNew);
			input.close();
			//将新内容添加进去
			strNew += txt;
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(strNew);
			output.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
}
