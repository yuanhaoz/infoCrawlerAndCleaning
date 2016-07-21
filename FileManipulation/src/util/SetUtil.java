package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

/**
 * 求集合的若干操作，包括:<br/> 1).求两个集合的交集 <br/>2).求两个集合忽略大小写的交集<br/> 3).求两个集合的并集<br/>
4).求两个集合忽略大小写的并集<br/> 5).求两个集合的差集<br/> 6).将某个集合转换为小写<br/> 7).从指定路径读取集合
 <br/>8).将集合写入到指定路径<br/> 9).求Vector集合的不重复集合 <br/>10).将HashSet转换成Vector
 * @author MJ
 * @description 
 */
public class SetUtil {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Vector<String> v=new Vector<String>();
		v.add("中国txt");
		SetUtil.writeSetToFile(v, "f://test.csv");
	}
	
	/**
	 * 求取集合v1和v2的交集
	 * @param v1
	 *            集合V1
	 * @param v2
	 *            集合V2
	 * @return V1和V2的交集
	 */
	public static Vector<String> getInterSet(Vector<String> v1,
			Vector<String> v2) {
		Vector<String> interV = new Vector<String>();
		for (String s1 : v1) {
			if (v2.contains(s1) && !interV.contains(s1))
				interV.add(s1);
		}
		return interV;
	}

	/**
	 * V1和V2忽略大小写的交集
	 * @param v1集合V1
	 * @param v2集合V2
	 * @return V1和V2忽略大小写的交集
	 */
	public static Vector<String> getInterSetIgnoreCase(Vector<String> v1,
			Vector<String> v2) {
		Vector<String> v1L = getLowerCaseSet(v1);
		Vector<String> v2L = getLowerCaseSet(v2);
		Vector<String> interV = new Vector<String>();
		for (String s1 : v1L) {
			if (v2L.contains(s1) && !interV.contains(s1))
				interV.add(s1);
		}
		return interV;
	}

	/**
	 * V1和V2的并集
	 * @param v1
	 *            集合V1
	 * @param v2
	 *            集合V2
	 * @return V1和V2的并集
	 */
	public static Vector<String> getUnionSet(Vector<String> v1,
			Vector<String> v2) {
		Vector<String> unionV = new Vector<String>();
		for (String s1 : v1) {
			if (!unionV.contains(s1))
				unionV.add(s1);
		}
		for (String s2 : v2) {
			if (!unionV.contains(s2))
				unionV.add(s2);
		}
		return unionV;
	}

	/**
	 * V1和V2忽略大小写的并集
	 * @param v1
	 *            集合V1
	 * @param v2
	 *            集合V2
	 * @return V1和V2忽略大小写的并集
	 */
	public static Vector<String> getUnionSetIgnoreCase(Vector<String> v1,
			Vector<String> v2) {
		Vector<String> v1L = getLowerCaseSet(v1);
		Vector<String> v2L = getLowerCaseSet(v2);
		Vector<String> unionV = new Vector<String>();
		for (String s1 : v1L) {
			if (!unionV.contains(s1))
				unionV.add(s1);
		}
		for (String s2 : v2L) {
			if (!unionV.contains(s2))
				unionV.add(s2);
		}
		return unionV;
	}

	/**
	 * v1和v2的差集，即在v1中但不在v2中
	 * @param v1
	 *            集合v1
	 * @param v2
	 *            集合v2
	 * @return v1和v2的差集，即在v1中但不在v2中
	 */
	public static Vector<String> getSubSet(Vector<String> v1, Vector<String> v2) {
		Vector<String> subV = new Vector<String>();
		for (String s1 : v1) {
			if (!v2.contains(s1))
				subV.add(s1);
		}
		return subV;
	}

	/**
	 * v全部变小写后的集合
	 * @param v
	 *            要转换的集合
	 * @return v全部变小写后的集合
	 */
	public static Vector<String> getLowerCaseSet(Vector<String> v) {
		Vector<String> lowerV = new Vector<String>();
		for (String s : v) {
			lowerV.add(s.toLowerCase());
		}
		return lowerV;
	}

	/**
	 * 从指定文件读取的字符串集合，每一行都将看做一个元素（非常有用）
	 * @param filePath
	 *            文件路徑
	 * @return 从该文件读取的字符串集合
	 */
	public static Vector<String> readSetFromFile(String filePath) {
		Vector<String> v = new Vector<String>();
		try {
			File f=new File(filePath);
			InputStreamReader read = new InputStreamReader (new FileInputStream(f));
			BufferedReader br = new BufferedReader(read);
			String s = br.readLine();
			while (s != null) {
				v.add(s);
				s = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return v;
	}
	
	/**
	 * 按照指定编码从指定文件读取的字符串集合
	 * @param filePath 文件路徑
	 * @param Charset 读取文件的编码
	 * @return 从该文件读取的字符串集合
	 */
	public static Vector<String> readSetFromFile(String filePath,String Charset) {
		Vector<String> v = new Vector<String>();
		try {
			File f=new File(filePath);
			InputStreamReader read = new InputStreamReader (new FileInputStream(f),Charset);
			BufferedReader br = new BufferedReader(read);
			String s = br.readLine();
			while (s != null) {
				v.add(s);
				s = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return v;
	}

	/**
	 * 将指定集合写入到文件中，每个元素对应一行
	 * @param v
	 *            要写入的集合
	 * @param filePath
	 *            写入的文件路径
	 */
	public static void writeSetToFile(Vector<String> v, String filePath) {
		try {
			File f=new File(filePath);
			File fParent=f.getParentFile();
			fParent.mkdirs();
			OutputStreamWriter writer = new OutputStreamWriter (new FileOutputStream(f));
			BufferedWriter bw = new BufferedWriter(writer);
			for (String s : v) {
				bw.write(s);
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 按照指定编码，将指定集合写入到文件中
	 * @param v
	 *            要写入的集合
	 * @param Charset 读取文件的编码
	 * @param filePath
	 *            写入的文件路径
	 */
	public static void writeSetToFile(Vector<String> v, String filePath,String Charset) {
		try {
			File f=new File(filePath);
			File fParent=f.getParentFile();
			fParent.mkdirs();
			OutputStreamWriter writer = new OutputStreamWriter (new FileOutputStream(f),Charset);
			BufferedWriter bw = new BufferedWriter(writer);
			for (String s : v) {
				bw.write(s);
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 将集合v追加到文件filePath后面
	 * @param v
	 * @param filePath 将v中的内容追加到指定的文件中
	 */
	public static void appendSetToFile(Vector<String> v, String filePath) {
		try {
			FileWriter fw = new FileWriter(filePath,true);
			BufferedWriter bw = new BufferedWriter(fw);
			for (String s : v) {
				bw.write(s);
				bw.newLine();
			}
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取v对应的不重复的集合
	 * @param v
	 *            需要求不重复的Vector集合
	 * @return v不重复的集合
	 */
	public static Vector<String> getNoRepeatVector(Vector<String> v) {
		Vector<String> vNoRepeat = new Vector<String>();
		for (int i = 0; i < v.size(); i++) {
			String s = v.get(i);
			if (!vNoRepeat.contains(s))
				vNoRepeat.add(s);
		}
		return vNoRepeat;
	}
	
	/**
	 * 获取v对应的忽略大小写不重复的集合
	 * @param v
	 *            需要求不重复的Vector集合
	 * @return v 忽略大小写不重复的集合
	 */
	public static Vector<String> getNoRepeatVectorIgnoreCase(Vector<String> v) {
		Vector<String> vNoRepeat = new Vector<String>();
		Vector<String> vIgnoreCase=new Vector<String>();
		for (int i = 0; i < v.size(); i++) {
			String s = v.get(i);
			if (!vNoRepeat.contains(s)&&!vIgnoreCase.contains(s.toLowerCase())){
				vNoRepeat.add(s);
				vIgnoreCase.add(s.toLowerCase());
			}
		}
		return vNoRepeat;
	}
	
	/**
	 * 将HashSet转换成Vector
	 * @param hs
	 * @return 将hs转换成Vector
	 */
	public static Vector<String> getVectorFromHashSet(HashSet<String> hs){
		Vector<String> v=new Vector<String>();
		Iterator<String> it=hs.iterator();
		while(it.hasNext()){
			v.add(it.next());
		}
		return v;
	}

}
