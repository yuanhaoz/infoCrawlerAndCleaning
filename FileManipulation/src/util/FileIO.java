package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

//
public class FileIO {
	//将String写入file
	public static void writeFile(String str,String fileName) throws IOException{
//		String fileName="";
		File f=new File(fileName);
		OutputStreamWriter writer;
		writer = new OutputStreamWriter (new FileOutputStream(f));//true：是否追加
		BufferedWriter bw = new BufferedWriter(writer);
		bw.write(str);
		bw.flush();
		bw.close();
	}
	
	public static String readFile(String fileName){
    	FileReader fr;
    	StringBuffer sb=new StringBuffer();
    	File f=new File(fileName);
//    	if(f.exists())
		try {
			fr = new FileReader(fileName);
			BufferedReader br=new BufferedReader(fr);
			String temp=br.readLine();
			while(temp!=null){
				sb=sb.append(temp+" ");
				temp=br.readLine();
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String s=sb.toString();
    	sb.delete(0, sb.length());
		return s;
    }
	
	
	//read from the directory
	 /**
     * 将filepath指定的文件读取到字符串中，不包含换行符
     * @param filePath 要读取的文件路径
     */
	
	/*//从文件中读取内容，返回String 用空格分割。
	public String fileRead(String fileName) throws IOException{
		File f=new File(fileName);
    	InputStreamReader read = new InputStreamReader (new FileInputStream(f));//保留换行符的
		BufferedReader br = new BufferedReader(read);
		String line=br.readLine().trim();
		String str=line+" ";
		while(line!=null){
			str+=str+" ";
			line=br.readLine();
		}
		br.close();
		return str;
	}*/
	
	
	//读文件列表  获得dimofFratures
	public static ArrayList<String> getFeatureDim(String dir) throws IOException {
		ArrayList<String> strs=new ArrayList<String>();
		File f=new File(dir);
		File childs[]=f.listFiles();
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<childs.length;i++){
			String fileName=childs[i].getName();
			try {
				FileReader fr=new FileReader(dir+fileName);
				BufferedReader br = new BufferedReader(fr);
				String temp=br.readLine();
				while(temp!=null){
					sb=sb.append(temp);
					temp=br.readLine();
				}
				br.close();
				fr.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String s=sb.toString().trim();
			sb.delete(0, sb.length());
			String temp[]=s.split(" ");
//			System.out.println("i="+i+"  "+fileName);
			for(int j=0;j<temp.length;j++){
				if(!strs.contains(temp[j])&&temp[j]!="\n"){
					strs.add(temp[j]);
				}
			}
		}
		return strs;
	}
//	//写arff文件
//	public static void writeArff(String outputFile, double[][] tfidf, int[][] labels) throws IOException{
//		DecimalFormat df=new DecimalFormat("0.000000"); 
//		File f=new File(outputFile);
//		OutputStreamWriter writer;
//		try {
//			writer = new OutputStreamWriter (new FileOutputStream(f));//true：是否追加
//			BufferedWriter bw = new BufferedWriter(writer);
//			bw.write("@relation features\n\n");
////			System.out.println(TextFeature.dimOfFeatures);
////			System.out.println(TextFeature.dimOfFiles);
//			for(int i=1;i<=TextFeature.dimOfFeatures;i++){ //验证？
//				bw.write("@attribute feature_");
//				bw.write(String.valueOf(i));
//				bw.write(" numeric\n");
//			}
//			for(int j=1;j<=TextFeature.dimOfLabels;j++){
//				bw.write("@attribute label_");	
//				bw.write(String.valueOf(j));
//				bw.write(" {0,1}\n");
//			}	
//			bw.write("\n@data\n");
//			for(int i=0;i<TextFeature.dimOfFiles;i++){
//				for(int j=0;j<TextFeature.dimOfFeatures;j++){
//					bw.write(df.format(tfidf[i][j])+",");
//				}
//				for(int j=0;j<labels[i].length;j++){
//					if(j<labels[i].length-1){
////						System.out.println("i="+i+"  j="+j);
//						bw.write(labels[i][j]+",");
//					}
//					else bw.write(labels[i][j]+"\n");
//				}
//			}
//			bw.flush();
//	    	bw.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	/*//写arff add degree
		public static void writeArff_degree(String outputFile, double[][] tfidf, int[][] labels) throws IOException{
			
			DecimalFormat df=new DecimalFormat("0.000000"); 
			File f=new File(outputFile);
			OutputStreamWriter writer;
			try {
				writer = new OutputStreamWriter (new FileOutputStream(f));//true：是否追加
				BufferedWriter bw = new BufferedWriter(writer);
				bw.write("@relation features\n\n");
//				System.out.println(TextFeature.dimOfFeatures);
//				System.out.println(TextFeature.dimOfFiles);
				for(int i=1;i<=TextFeature.dimOfFeatures;i++){ //验证？
					bw.write("@attribute feature_");
					bw.write(String.valueOf(i));
					bw.write(" numeric\n");
				}
				for(int j=1;j<=TextFeature.dimOfLabels;j++){
					bw.write("@attribute label_");	
					bw.write(String.valueOf(j));
					bw.write(" {0,1}\n");
				}	
				bw.write("\n@data\n");
				for(int i=0;i<TextFeature.dimOfFiles;i++){
					for(int j=0;j<TextFeature.dimOfFeatures;j++){
						bw.write(df.format(tfidf[i][j])+",");
					}
					for(int j=0;j<labels[i].length;j++){
						if(j<labels[i].length-1){
//							System.out.println("i="+i+"  j="+j);
							bw.write(labels[i][j]+",");
						}
						else bw.write(labels[i][j]+"\n");
					}
				}
				bw.flush();
		    	bw.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//写arff add between
	public static void writeArff_between(String outputFile, double[][] tfidf, int[][] labels) throws IOException{
		
		DecimalFormat df=new DecimalFormat("0.000000"); 
		File f=new File(outputFile);
		OutputStreamWriter writer;
		try {
			writer = new OutputStreamWriter (new FileOutputStream(f));//true：是否追加
			BufferedWriter bw = new BufferedWriter(writer);
			bw.write("@relation features\n\n");
//						System.out.println(TextFeature.dimOfFeatures);
//						System.out.println(TextFeature.dimOfFiles);
			for(int i=1;i<=TextFeature.dimOfFeatures;i++){ //验证？
				bw.write("@attribute feature_");
				bw.write(String.valueOf(i));
				bw.write(" numeric\n");
			}
			for(int j=1;j<=TextFeature.dimOfLabels;j++){
				bw.write("@attribute label_");	
				bw.write(String.valueOf(j));
				bw.write(" {0,1}\n");
			}	
			bw.write("\n@data\n");
			for(int i=0;i<TextFeature.dimOfFiles;i++){
				for(int j=0;j<TextFeature.dimOfFeatures;j++){
					bw.write(df.format(tfidf[i][j])+",");
				}
				for(int j=0;j<labels[i].length;j++){
					if(j<labels[i].length-1){
//									System.out.println("i="+i+"  j="+j);
						bw.write(labels[i][j]+",");
					}
					else bw.write(labels[i][j]+"\n");
				}
			}
			bw.flush();
	    	bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	public static void writeXml(String fileName,int dim) throws IOException{
		File f=new File(fileName);
		OutputStreamWriter writer;
		try {
			writer = new OutputStreamWriter (new FileOutputStream(f));//true：是否追加
			BufferedWriter bw = new BufferedWriter(writer);
			bw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
			bw.write("<labels xmlns=\"http://mulan.sourceforge.net/labels\">\r\n");
			for(int i=1;i<=dim;i++){
				bw.write("<label name=\"label_"+i+"\"></label>\r\n");
			}
			bw.write("</labels>\r\n");
			bw.flush();
	    	bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args[]) throws IOException{
		/*File f=new File(outputPath);
		if(!f.exists())
			f.mkdirs();*/
		FileIO fio=new FileIO();
		String inputPath="E:\\dataMining\\treeTraversal_preDeal\\";
		ArrayList<String> strs=new ArrayList<String>();
		strs=fio.getFeatureDim(inputPath);
		String rst="";
//		for(int i=0;i<strs.size();i++){
//			System.out.println(strs.get(i));
//			rst+=strs.get(i)+"\n";
//		}
		System.out.println("features.size():"+strs.size());
//		FileIO.writeFile(rst, fileName);
	}
}
