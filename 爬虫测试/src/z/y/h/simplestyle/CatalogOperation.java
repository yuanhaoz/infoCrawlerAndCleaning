package z.y.h.simplestyle;

/**
 * zhengyuanhao  2015/7/28
 * 简单实现：simple style
 * 实现功能：1.生成存储网页和图片的文件夹
 * 			2.定位到相应文件夹
 */

import java.io.File;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import Jsoup.JsoupParse;
import base.DirFile;

public class CatalogOperation {

	/**
	 * 实现功能：生成第一层页面文件夹
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
	 * 实现功能：生成第二层页面文件夹
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
	 * 功能：根据父页面编号，定位到相应存储图片目录 
	 * @author zhengyuanhao
	 */
	public String getPictureCatalog(int n) {
		String catalog = "file/simplestyle/";
		ArrayList<String> a = DirFile.getFolderFileNamesFromDirectorybyArraylist(catalog); // 读取所有文件名
		String pageCatalog = "page" + n;   //第n个父页面
		String pictureCatalog = null;
		for (int i = 0; i < a.size(); i++) {
			String getCatalogName = a.get(i);
			if (getCatalogName.equals(pageCatalog)) {    //比较是否相同
				pictureCatalog = catalog + pageCatalog + "/";
				System.out.println("图片存储路径为：" + pictureCatalog);
			}
		}
		return pictureCatalog;
	}
	
	/**
	 * 功能：根据子页面标题，定位到相应存储子页面的目录
	 * @author zhengyuanhao
	 */
	public String getPictureCatalogForChildPage(int n, int m) {
		String pictureCatalog = "file/simplestyle/page" + n + "/" + "page" + n + "_" + m + "/";
		System.out.println("图片存储路径为：" + pictureCatalog);
		return pictureCatalog;
	}
	
}
