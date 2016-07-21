package informationextraction;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class GetExcelFile {
	
	// 源文件夹
//	static String source = "file\\datacollection\\Data_structure_excel112";
//	static String source = "file\\datacollection\\Data_structure_excel59";
	static String source = "file\\datacollection\\Data_structure_already22";
	// 目标文件夹
//	static String target = "file\\datacollection\\Data_structure_onlyExcel112";
//	static String target = "file\\datacollection\\Data_structure_onlyExcel59";
	static String target = "file\\datacollection\\Data_structure_onlyAlready22";

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
//		getExcel();
		getExcelAll();
	}
	
	public static void getExcelAll() throws IOException{
		(new File(target)).mkdirs();
		File[] file = (new File(source)).listFiles();
		for(int i = 0; i < file.length; i++){
			String sourceFilePath = source + "\\" + file[i].getName();
			String targetFilePath = target + "\\" + file[i].getName();
			getExcel(sourceFilePath, targetFilePath);
			System.out.println(file[i].getName() + "处理完毕---------");
		}
	}
	
	public static void getExcel(String sourceFilePath, String targetFilePath) throws IOException{
		// 创建目标文件夹
		(new File(targetFilePath)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceFilePath)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].getName().contains(".xls")) {
				// 复制文件
				copyFile(file[i], new File(targetFilePath + File.separator + file[i].getName()));
			}
		}
		
	}
	
	// 复制文件
	public static void copyFile(File sourceFile, File targetFile) throws IOException {
		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);

		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(targetFile);
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

}
