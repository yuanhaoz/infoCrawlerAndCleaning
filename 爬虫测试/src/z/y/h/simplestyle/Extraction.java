package z.y.h.simplestyle;

/**
 * zhengyuanhao  2015/7/28
 * ��ʵ�֣�simple style
 * ʵ�ֹ��ܣ�1.����ͼƬ���ӵ��ļ�
 * 			2.����ͼƬ���ӣ���ȡͼƬ������
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
	 * ��;������ͼƬ���ӵ��ļ�
	 * ���룺ͼƬ����ArrayList  ��   �ļ���
	 * ��������浽���ص������ļ�
	 * @author zhengyuanhao
	 */
	public static void writeImageUrlToFile(ArrayList<String> imgUrl, String fileName) {
        FileWriter writer;
        try {
            writer = new FileWriter(fileName);
            String str = "image url is��";
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
	 * ��;������ͼƬ���ӣ���ȡͼƬ������
	 * ���룺ͼƬ����url  ��   �ļ���
	 * ���������ͼƬ
	 * @author zhengyuanhao
	 */
	public static void downLoadImage(String url, String imageFileName){  
		byte[] btImg = getImageFromNetByUrl(url);  
		if (null != btImg && btImg.length > 0) {
			System.out.println("��ȡ����" + btImg.length + " �ֽ�");
			writeImageToDisk(btImg, imageFileName);
		} else {
			System.out.println("û�дӸ����ӻ������");
		}
	}
	
	/**
	 * ��;�����ֽ���д���ļ���
	 * ���룺�ֽ��� byte[] ��   �ļ���
	 * ����������ļ�
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
	 * ��;������ͼƬurl��ͼƬ��Ϣ���浽�ֽ�����
	 * ���룺ͼƬ����strUrl
	 * ������ֽ���byte[]
	 * @author zhengyuanhao
	 */
	public static byte[] getImageFromNetByUrl(String strUrl){  
        try {  
            URL url = new URL(strUrl);  
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
            conn.setRequestMethod("GET");  
            conn.setConnectTimeout(15 * 1000);  
            InputStream inStream = conn.getInputStream();	//ͨ����������ȡͼƬ����  
            byte[] btImg = readInputStream(inStream);		//�õ�ͼƬ�Ķ���������  
            return btImg;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }
	
	/**
	 * ��;���õ�ͼƬ�Ķ���������
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
