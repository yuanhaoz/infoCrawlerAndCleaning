package z.y.h.simplestyle;

/**
 * zhengyuanhao  2015/7/28
 * ��ʵ�֣�simple style
 * ʵ�ֹ��ܣ�1.���ɴ洢��ҳ��ͼƬ���ļ���
 * 			2.��λ����Ӧ�ļ���
 */

import java.io.File;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import Jsoup.JsoupParse;
import base.DirFile;

public class CatalogOperation {

	/**
	 * ʵ�ֹ��ܣ����ɵ�һ��ҳ���ļ���
	 * @author zhengyuanhao
	 */
	public void setPictureCatalog() {
		String catalog = "file/simplestyle/";
		for (int i = 1; i < 33; i++) {
			String pictureCatalog = catalog + "page" + i;
			File pictureFileCatalog = new File(pictureCatalog);
			if (!pictureFileCatalog.exists()) {
				pictureFileCatalog.mkdir();
				System.out.println(pictureCatalog + "  mkdir successful...");
			} else {
				System.out.println(pictureCatalog + 
						"  already exist, don't need to do mkdir operation!!!");
			}
		}
	}
	
	/**
	 * ʵ�ֹ��ܣ����ɵڶ���ҳ���ļ���
	 * @author zhengyuanhao
	 */
	public void setPictureCatalogForChildPage() {
		for(int i = 1; i < 33; i++){
			String fileName = "file/simplestyle/page" + i + "/page" + i + ".html"; 
			Document doc = JsoupParse.parsePathText(fileName);
			Elements hrefs = doc.select("div#content").select("div.post").select("div.entry"); 
			for (int j = 0; j < hrefs.size(); j++) {
				String pictureCatalog = "file/simplestyle/page" + i + "/" + "page" + i + "_" + j + "/";
				File a = new File(pictureCatalog);
				if (!a.exists()) {
					a.mkdir();
					System.out.println(pictureCatalog + "  mkdir successful...");
				} else {
					System.out.println(pictureCatalog + 
							"  already exist, don't need to do mkdir operation!!!");
				}
			}
		}
		
	}

	/**
	 * ���ܣ����ݸ�ҳ���ţ���λ����Ӧ�洢ͼƬĿ¼ 
	 * @author zhengyuanhao
	 */
	public String getPictureCatalog(int n) {
		String catalog = "file/simplestyle/";
		ArrayList<String> a = DirFile.getFolderFileNamesFromDirectorybyArraylist(catalog); // ��ȡ�����ļ���
		String pageCatalog = "page" + n;   //��n����ҳ��
		String pictureCatalog = null;
		for (int i = 0; i < a.size(); i++) {
			String getCatalogName = a.get(i);
			if (getCatalogName.equals(pageCatalog)) {    //�Ƚ��Ƿ���ͬ
				pictureCatalog = catalog + pageCatalog + "/";
				System.out.println("ͼƬ�洢·��Ϊ��" + pictureCatalog);
			}
		}
		return pictureCatalog;
	}
	
	/**
	 * ���ܣ�������ҳ����⣬��λ����Ӧ�洢��ҳ���Ŀ¼
	 * @author zhengyuanhao
	 */
	public String getPictureCatalogForChildPage(int n, int m) {
		String pictureCatalog = "file/simplestyle/page" + n + "/" + "page" + n + "_" + m + "/";
		System.out.println("ͼƬ�洢·��Ϊ��" + pictureCatalog);
		return pictureCatalog;
	}
	
}
