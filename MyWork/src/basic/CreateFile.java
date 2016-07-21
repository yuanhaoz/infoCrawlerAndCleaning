package basic;

import java.io.File;
import java.io.IOException;

public class CreateFile {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String catalog = "file/datacollection/Data_structure/";
		File file = new File(catalog + "Array.html");
		CreateFile cf = new CreateFile();
		cf.create(file);
	}
	
	/**
	 * 实现功能：创建文件
	 * 
	 * @param keyword
	 */
	public void create(File file){
		if (!file.exists()) {
			try {
				file.createNewFile();  
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("创建文件失败...");
			}
		}
	}

}
