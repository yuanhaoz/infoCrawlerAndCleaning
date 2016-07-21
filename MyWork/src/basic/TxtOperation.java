package basic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TxtOperation {

	/**
     * 在Text原有内容中写入新的内容，不覆盖原来内容
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
