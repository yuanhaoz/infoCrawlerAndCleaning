package knowledgeForest;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jxl.Sheet;
import jxl.Workbook;
import basic.MysqlConnection_binarytree;

public class TopicRelationDeal {
	
	static MysqlConnection_binarytree mysqlCon = new MysqlConnection_binarytree();
	private static String sql;
	private static String TopicPath = "F:\\d3\\数据结构数据";

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		test();
	}
	
	public static void test() throws Exception{
		String excelPath = TopicPath + "\\Knowledgetopic_DS.xls";
		getTopic(excelPath);
//		storeTopic2Mysql();
		storeTopicRelation2Mysql();
	}
	
	/**
	 * 得到主题词之间的关系（编号）
	 */
	public static ArrayList<Integer> getTopicRelation(String excelPath) throws Exception{
		String topicPath = TopicPath + "\\Knowledgetopic_DS.xls";
		Set<String> topic = getTopic(topicPath);
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
	 * 得到主题词之间的关系（主题名字）
	 */
	public static ArrayList<String> getTopicRelationByName(String excelPath) throws Exception{
		String topicPath = TopicPath + "\\Knowledgetopic_DS.xls";
		Set<String> topic = getTopic(topicPath);
		InputStream is = new FileInputStream(excelPath);
		Workbook rwb = Workbook.getWorkbook(is);
		Sheet sheet = rwb.getSheet(0);
		int rows = sheet.getRows();
		ArrayList<String> re = new ArrayList<String>();// List集合用于：父亲 + 儿子（用于函数返回）
		ArrayList<String> parent = new ArrayList<String>();// List集合用于：主题关系的父亲（允许重复）
		ArrayList<String> child = new ArrayList<String>();// Set集合用于：主题关系的儿子（允许重复）
		for(int i = 0; i < rows; i++){
			String parentTopic = sheet.getCell(0, i).getContents();
			String childTopic = sheet.getCell(1, i).getContents();
			String parentName = "";
			String childName = "";
			Iterator<String> it = topic.iterator();
			while(it.hasNext()){
				String s = it.next();
				if(parentTopic.equals(s)){
					parentName = parentTopic;
				} else if(childTopic.equals(s)){
					childName = childTopic;
				} else {
//					System.out.println("...");
				}
			}
			if(!parentName.equals("") && !childName.equals("")){
				parent.add(parentName);
				child.add(childName);
			}
		}
		// 合并信息用于返回
		if(parent.size() == child.size()){
			for(int i = 0; i < parent.size(); i++){
				String parentName = parent.get(i);
				String childName = child.get(i);
				re.add(parentName);//偶数下标为父亲
				re.add(childName);//奇数下标为儿子
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
			set.add(column0);
		}
		System.out.println("topic length：" + set.size());
		return set;
	}
	
	/**
	 * 存储主题关系信息
	 */
	public static void storeTopicRelation2Mysql() throws Exception {
		try {
			String excelPath = TopicPath + "\\数据结构认知关系标注.xls";
			// 存入主题对应的"编号"到数据库中
			ArrayList<Integer> list = getTopicRelation(excelPath);
			for(int i = 0; i < list.size()/2; i++){
				int parent = list.get(i * 2);// 偶数下标为父亲
				int child = list.get(i * 2 + 1);// 奇数下标为儿子
				sql = "replace into topic_relation_DS values (?, ?)";
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
			
			// 存入主题对应的"名字"到数据库中
			ArrayList<String> listName = getTopicRelationByName(excelPath);
			for(int i = 0; i < listName.size()/2; i++){
				String parent = listName.get(i * 2);// 偶数下标为父亲
				String child = listName.get(i * 2 + 1);// 奇数下标为儿子
				sql = "replace into topic_relation_DS_data values (?, ?)";
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
			String excelPath = TopicPath + "\\Knowledgetopic_DS.xls";
			Set<String> set = getTopic(excelPath);
			Iterator<String> it = set.iterator();
			int id = -1;
			while(it.hasNext()){
				String s = it.next();
				id++;
				sql = "replace into topic_DS values (?, ?)";
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
