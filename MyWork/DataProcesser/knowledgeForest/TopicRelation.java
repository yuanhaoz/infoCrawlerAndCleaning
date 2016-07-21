package knowledgeForest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jxl.Sheet;
import jxl.Workbook;

import org.jsoup.nodes.Document;

import data.UTF8Code;
import informationextraction.FeatureExtractionForSelenium;
import informationextraction.InformationExtraction2MysqlNew;
import Jsoup.JsoupParse;
import base.DirFile;
import basic.MysqlConnection_binarytree;

public class TopicRelation {
	
	// 导入mysqloperation类
	static MysqlConnection_binarytree mysqlCon = new MysqlConnection_binarytree();
	// 准备sql语句
	private static String sql;
	// 影响行数（数据变更后，影响行数都是大于0，等于0时没变更，所以说如果变更失败，那么影响行数必定为负）
	// private int i = -1;
	// 结果集
	// private ResultSet rs;
	private static String path1 = "D:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\webapps\\test\\relation";
	private static String path = "F:\\高质量\\二叉树\\wiki";
	private static FeatureExtractionForSelenium extract = new FeatureExtractionForSelenium();

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		test();
	}
	
	public static void test() throws Exception{
		String excelPath = path1 + "\\Data_structure.xls";
		getTopic(excelPath);
		storeTopic2Mysql();
		storeTopicRelation2Mysql();
	}
	
	/**
	 * 得到主题词之间的关系
	 */
	public static ArrayList<Integer> getTopicRelation(String excelPath) throws Exception{
		Set<String> topic = getTopic(excelPath);
		InputStream is = new FileInputStream(excelPath);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet sheet = rwb.getSheet(0);
		int rows = sheet.getRows();
		ArrayList<Integer> re = new ArrayList<Integer>();// List集合用于：父亲 + 儿子（用于函数返回）
		ArrayList<Integer> parent = new ArrayList<Integer>();// List集合用于：主题关系的父亲（允许重复）
		ArrayList<Integer> child = new ArrayList<Integer>();// Set集合用于：主题关系的儿子（允许重复）
		for(int i = 0; i < rows; i++){
			String parentTopic = sheet.getCell(0, i).getContents();
			String childTopic = sheet.getCell(1, i).getContents();
			int parentId = -1;
			int childId = -1;
			int id = -1;
			Iterator<String> it = topic.iterator();
			while(it.hasNext()){
				String s = it.next();
				id++;
				if(parentTopic.equals(s)){
					parentId = id;
				} else if(childTopic.equals(s)){
					childId = id;
				} else {
//					System.out.println("...");
				}
			}
			if(parentId > 0 && childId > 0){
				parent.add(parentId);
				child.add(childId);
			}
		}
		// 合并信息用于返回
		if(parent.size() == child.size()){
			for(int i = 0; i < parent.size(); i++){
				int parentId = parent.get(i);
				int childId = child.get(i);
				re.add(parentId);//偶数下标为父亲
				re.add(childId);//奇数下标为儿子
			}
		} else {
			System.out.println("大小不一致，关系得到不正确...");
		}
		return re;
	}
	
	/**
	 * 得到主题词
	 */
	public static Set<String> getTopic(String excelPath) throws Exception{
		InputStream is = new FileInputStream(excelPath);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet sheet = rwb.getSheet(0);
		int rows = sheet.getRows();
		Set<String> set = new HashSet<String>();//Set集合用于存放（无重复）的主题词
		for(int i = 0; i < rows; i++){
			String column0 = sheet.getCell(0, i).getContents();
			String column1 = sheet.getCell(1, i).getContents();
			set.add(column0);
			set.add(column1);
		}
		Iterator<String> it = set.iterator();
		String data = "";
		while(it.hasNext()){
			String s = it.next();
			data = data + s + "\n";
//			System.out.println(s);
		}
		DirFile.storeString2File(data, path1 + "\\DS.txt");
//		System.out.println("topic length：" + set.size());
		return set;
	}
	
	/**
	 * 存储主题关系信息
	 */
	public static void storeTopicRelation2Mysql() throws Exception {
		try {
			String excelPath = path1 + "\\Data_structure.xls";
			ArrayList<Integer> list = getTopicRelation(excelPath);
			for(int i = 0; i < list.size()/2; i++){
				int parent = list.get(i * 2);// 偶数下标为父亲
				int child = list.get(i * 2 + 1);// 奇数下标为儿子
				sql = "replace into topic_relation values (?, ?)";
				Object[] QuestionObject = new Object[] {parent, child};
				mysqlCon.doSql(sql, QuestionObject);
				int j = mysqlCon.getUpdateCount();
				if (j != -1) {
//					System.out.println("数据插入成功！");
				} else {
					System.out.println("数据插入失败！");
				}
				mysqlCon.getClose();
			}
		} catch (Exception ex) {
			System.out.println("Error : " + ex.toString());
		}
	}
	
	/**
	 * 存储主题信息
	 */
	public static void storeTopic2Mysql() throws Exception {
		try {
			String excelPath = path1 + "\\Data_structure.xls";
			Set<String> set = getTopic(excelPath);
			Iterator<String> it = set.iterator();
			int id = -1;
			while(it.hasNext()){
				String s = it.next();
				id++;
				sql = "replace into topic values (?, ?)";
				Object[] QuestionObject = new Object[] {id, s};
				mysqlCon.doSql(sql, QuestionObject);
				int i = mysqlCon.getUpdateCount();
				if (i != -1) {
//					System.out.println("数据插入成功！");
				} else {
					System.out.println("数据插入失败！");
				}
				mysqlCon.getClose();
			}
		} catch (Exception ex) {
			System.out.println("Error : " + ex.toString());
		}
	}

}
