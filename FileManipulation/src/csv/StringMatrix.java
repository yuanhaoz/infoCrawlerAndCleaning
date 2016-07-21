/**
 * 
 */
package csv;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author weibifan
 * 功能：使用string作为下标
 * 
Java Matrix Library
已有 510 次阅读 2012-6-4 10:03 |系统分类:科研笔记|关键词:Java矩阵库
0. LAPACK - Linear Algebra PACKage
0.1) 项目主页
http://www.netlib.org/lapack/
这个不是JAVA库 

1. EJML - Efficient Java Matrix Library 
1.1) 项目主页 
http://code.google.com/p/efficient-java-matrix-library/
1.2）与其它库比较介绍
http://java.dzone.com/announcements/introduction-efficient-java
2. Apache Math
1.1) 项目主页
http://commons.apache.org/math/userguide/linear.html
3. Colt
3.1) 项目主页
http://acs.lbl.gov/software/colt/
4. JAMA - A Java Matrix Package
4.1) 项目主页
http://math.nist.gov/javanumerics/jama/
4.2) 相关网站
http://math.nist.gov/javanumerics/
5. UJMP - Universal Java Matrix Package
5.1) 项目主页
http://sourceforge.net/projects/ujmp/
6. Other... ...
 */
public class StringMatrix {
	int[][] data=null;
	Map<String,Integer> rowIndex=new HashMap<String,Integer>();
	Map<String,Integer> colIndex=new HashMap<String,Integer>();

	/**
	 * 
	 */
	public StringMatrix(Set<String> row, Set<String> col) {
		// TODO Auto-generated constructor stub
		data=new int[row.size()][col.size()];
//		for(int i=0;i<row.size();i++){
//			for(int j=0;j<col.size();j++){
//				data[i][j]=0;
//			}
//		}
		
		//String[][] t=new String[row.size()][4];
		int i=0;
		for(String str:row){
			rowIndex.put(str, i++);
		}
		
		int j=0;
		for(String str:col){
			colIndex.put(str, j++);
		}
		
	}
	
	public int getValue(String row,String col){
		Integer i=rowIndex.get(row);
		Integer j=colIndex.get(col);
		if(i==null || j==null){
			System.err.println("error:"+row+":"+col);
		}
		return data[i][j];
	}
	public int[] getRow(String row){
		Integer i=rowIndex.get(row);
		
		if(i==null){
			System.err.println("error:"+row);
		}
		return data[i];
	}
	
	public int setValue(String row,String col,int value){
		Integer i=rowIndex.get(row);
		Integer j=colIndex.get(col);
		if(i==null || j==null){
			System.err.println("error:"+row+":"+col);
		}
		return data[i][j]=value;
	}
	
	public int addOne(String row,String col){
		Integer i=rowIndex.get(row);
		Integer j=colIndex.get(col);
		if(i==null || j==null){
			System.err.println("error:"+row+":"+col);
		}
		//System.out.println(row+"_"+col+" before:"+data[i][j]);
		data[i][j]=data[i][j]+1;
		//System.out.println(row+"_"+col+" after :"+data[i][j]);
		return data[i][j];
	}
	
	public void printAll(){
		System.out.println("print all data:");
		for(int i=0;i<data.length;i++){
			
			for(int j=0;j<data[i].length;j++){
				System.out.print(" "+data[i][j]);
			}
			System.out.println();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Set<String> row=new HashSet<String>();
		Set<String> col=new HashSet<String>();
		
		row.add("row1");
		row.add("row2");
		row.add("row3");
		
		col.add("col1");
		col.add("col2");
		col.add("col3");
		
		StringMatrix matrix =new StringMatrix(row,col);
		
		matrix.addOne("row1", "col2");
		matrix.addOne("row1", "col2");
		
		System.out.println(matrix.getValue("row1", "col2"));
		
		int[] result=matrix.getRow("row1");
		for(int i=0;i<result.length;i++){
			System.out.print(" "+result[i]);
		}
		
		matrix.printAll();
	}

}
