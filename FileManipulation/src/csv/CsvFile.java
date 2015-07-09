package csv;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Vector;

/*目标：在内存中操作csv文件
 * 基本思路
 * 1) 按列操作，csv的第1行决定了csv的宽度
 * 2) 使用Vector<String>来存放列
 * 3) 默认不自动保存在内存的修改
 * 基本功能：csv文件的加载，添加，内存中读写，保存
 * 
 */
public class CsvFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	//---------------------------------------------------------------------------------------------------
	//不对Vector中的String进行split
	public static void directSave(Collection<String> col,String outfile){
		try {
			FileWriter writer = new FileWriter(outfile);
			for (String line : col) {
				writer.write(line + "\r\n");
			}
			writer.close();
			System.out.println("文件写入完成，行数为："+col.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void directSave(String term, Collection<String> col,String outfile){
		Vector<String> lines=new Vector<String>();
		for (String line : col) {
			line=term+","+line;
			lines.add(line);
		}
		directSave(lines,outfile);
	}
	public static void directSave( Vector<String> col1, Vector<String> col2,String outfile){
		if(col1.size() !=col2.size()){
			return;
		}
		Vector<String> lines=new Vector<String>();
		for(int i=0;i<col1.size();i++){
			String line=col1.get(i)+","+col2.get(i);
			lines.add(line);
		}
		directSave(lines,outfile);
	}
	
	public static void directAppend(Collection<String> col,String outfile){
		try {
			File file=new File(outfile);
			if(!file.exists()){
				System.err.println("文件不存在，准备创建："+outfile);
				file.createNewFile();
			}
			FileWriter writer = new FileWriter(outfile,true);
			for (String line : col) {
				writer.write(line + "\r\n");
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//-----------------------------------------------------------------------------------------------
	public static Vector<String> directReadFirstCol(String openFile){
		return directReadCol(0,openFile);
	}
	
	//从0开始。
	public static Vector<String> directReadCol(int colIndex, String openFile){
		System.out.println("open File: "+openFile);
		Vector<String> col=new Vector<String>();
		FileReader fr;
		try {
			fr = new FileReader(openFile);
			BufferedReader br = new BufferedReader(fr); // 可以按行读取
			String str = null;
			while ((str = br.readLine()) != null) {
				String items[]=str.split(",");
				col.add(items[colIndex]);
			}
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return col;
	}
	
	//按行读取到vector中。
	public static Vector<String> directRead(String openFile){
		Vector<String> col=new Vector<String>();
		FileReader fr;
		try {
			fr = new FileReader(openFile);
			BufferedReader br = new BufferedReader(fr); // 可以按行读取
			String str = null;
			while ((str = br.readLine()) != null) {	
				col.add(str);
			}
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return col;
	}
	
	public static int directRead(Collection<String> contents, String openFile){
		
		FileReader fr;
		try {
			fr = new FileReader(openFile);
			BufferedReader br = new BufferedReader(fr); // 可以按行读取
			String str = null;
			while ((str = br.readLine()) != null) {	
				contents.add(str);
			}
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return contents.size();
	}
	
	
}
