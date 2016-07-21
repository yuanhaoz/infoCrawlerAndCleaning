package tagoperation;

import java.util.ArrayList;
import java.util.Iterator;

import base.DirFile;

public class TagClassifyInstance {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TagClassify t = new TagClassify();
		try {
			String catalog = "file/标注术语/";
			
			 //读取所有文件夹名
			ArrayList<String> a = DirFile.getFolderFileNamesFromDirectorybyArraylist(catalog); 
			Iterator<String> it = a.iterator();   //设置迭代器
			while(it.hasNext()){                  //判断是否有下一个
				long start = System.currentTimeMillis();
				String keyword = it.next();       //得到关键词
				t.tagKnowledgeGet(keyword);
				t.tagClassify(keyword);
				long end = System.currentTimeMillis();
				System.out.println("爬取" + keyword + "的所有信息用时：" + (end - start)/1000 + "秒...");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
