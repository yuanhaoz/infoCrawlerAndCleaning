package xjtu.sky.quoraStudy;

/**
 * zhengyuanhao  2015/6/30
 * ��ʵ�֣�quora  
 * ʵ�ֹ��ܣ�Jsoup��ȡHTML��������ļ��ַ�����
 *          1.��һ��URL����һ��Document����
 *          2.����һ���ļ�����Document����
 *          3.����һ��html�ַ�����
 * 
 */

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupParse {

	/**
	 * ʵ�ֹ��ܣ�����һ���ļ�����Document���� ����ָ��·����html�ļ�
	 * @param path
	 */
	public static Document parsePathText(String path) {
		Document doc = null;
		try {
			File input = new File(path);
			doc = Jsoup.parse(input, "UTF-8", "http://www.quora.com");  //Quora��վ
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	/**
	 * ʵ�ֹ��ܣ���һ��URL����һ��Document����
	 * @param URL
	 */
	public static Document parseURLText(String URL) {
		Document doc = null;
		try {
			doc = Jsoup.connect(URL).get();
//			String title = doc.title();     
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	/**
	 * ʵ�ֹ��ܣ�����һ��html�ַ���
	 * @param 
	 */
	public static Document parseHtmlText() {
		String html = "<html><head><title>First parse</title></head>"
				  + "<body><p>Parsed HTML into a doc.</p></body></html>";
		Document doc = Jsoup.parse(html);
		return doc;
	}
	
	/**
	 * ʵ�ֹ��ܣ�������
	 * @param 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	
}
