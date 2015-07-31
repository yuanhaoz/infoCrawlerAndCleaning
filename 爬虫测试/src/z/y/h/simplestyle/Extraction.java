package z.y.h.simplestyle;

/**
 * zhengyuanhao  2015/7/28
 * 简单实现：simple style
 * 实现功能：1.保存图片链接到文件
 * 			2.根据图片链接，爬取图片到本地
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Extraction {
	
	/**
	 * 用途：保存图片链接到文件
	 * 输入：图片链接ArrayList  和   文件名
	 * 输出：保存到本地的链接文件
	 * @author zhengyuanhao
	 */
	public static void writeImageUrlToFile(ArrayList<String> imgUrl, String fileName) {
        FileWriter writer;
        try {
            writer = new FileWriter(fileName);
            String str = "image url is：";
            for(int i = 0; i < imgUrl.size(); i++){
            	str = str + "\n" + imgUrl.get(i);
            }
            writer.write(str);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * 用途：根据图片链接，爬取图片到本地
	 * 输入：图片链接url  和   文件名
	 * 输出：本地图片
	 * @author zhengyuanhao
	 */
	public static void downLoadImage(String url, String imageFileName){  
		byte[] btImg = getImageFromNetByUrl(url);  
		if (null != btImg && btImg.length > 0) {
			System.out.println("读取到：" + btImg.length + " 字节");
			writeImageToDisk(btImg, imageFileName);
		} else {
			System.out.println("没有从该连接获得内容");
		}
	}
	
	/**
	 * 用途：将字节流写到文件中
	 * 输入：字节流 byte[] 和   文件名
	 * 输出：本地文件
	 * @author zhengyuanhao
	 */
	public static void writeImageToDisk(byte[] img, String fileName){  
        try {  
            File file = new File(fileName);
            System.out.println(fileName);
            if(!file.exists()){       
                file.createNewFile();
            } 
            FileOutputStream fops = new FileOutputStream(file);  
            fops.write(img);  
            fops.flush();  
            fops.close();  
            System.out.println(fileName);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
	
	/**
	 * 用途：根据图片url将图片信息保存到字节流中
	 * 输入：图片链接strUrl
	 * 输出：字节流byte[]
	 * @author zhengyuanhao
	 */
	public static byte[] getImageFromNetByUrl(String strUrl){  
        try {  
            URL url = new URL(strUrl);  
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
            conn.setRequestMethod("GET");  
            conn.setConnectTimeout(15 * 1000);  
            InputStream inStream = conn.getInputStream();	//通过输入流获取图片数据  
            byte[] btImg = readInputStream(inStream);		//得到图片的二进制数据  
            return btImg;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }
	
	/**
	 * 用途：得到图片的二进制数据
	 * @author zhengyuanhao
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        while( (len=inStream.read(buffer)) != -1 ){  
            outStream.write(buffer, 0, len);  
        }  
        inStream.close();  
        return outStream.toByteArray();  
    }
	
}
