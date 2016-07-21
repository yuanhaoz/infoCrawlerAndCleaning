package data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import base.DirFile;
import basic.QuoraWebPage;
import Jsoup.JsoupParse;

/**
 * 思路：队列实现
 * 解析程序是：getContent()函数
 * 实现对数据的解析，原来有496个关键词，只产生448文件夹
 * 对于一些不存在标签的没有产生文件夹
 * Comb_sort维基网页是乱码
 * Graph_isomorphism_problem维基网页需另外处理
 * write函数负责依照目录名写其内容
 * 
 */

public class CrawlerWiki {
	private static String cata = "02-CQA网站中问题答案质量评估";
//	private static String catalog = "F:\\"+cata+"\\二叉树\\wiki";
	private static String catalog = "F:\\"+cata+"\\wiki_数据结构标注20主题";
	private static String catalog2 = "F:\\test\\kt";

	public static void main(String[] args) {
		String keyword = "Binary_tree2";
		test(keyword);
	}

	/**
	 * 具体实现
	 */
	public static void test(String keyword) {
		// 爬取二叉树目录页面
		String filePath = catalog + "\\Binary_trees_Category.html";
		String Binary_trees_Category = "https://en.wikipedia.org/wiki/Category:Binary_trees";
		crawlerWiki(filePath, Binary_trees_Category);
		// 解析目录页面，得到上下位术语
		Document doc = JsoupParse.parsePathText(filePath);
		HashMap<String, String> map = getRelated(doc);
		// 爬取上下位术语网页
		crawler(map);
		// 解析上下位术语网页
		getContentAll();
	}
	
	/**
	 * 得到所有关键词内容
	 */
	public static void getContentAll(){
		ArrayList<String> cata = DirFile.getFolderFileNamesFromDirectorybyArraylist(catalog);
		for(int i = 0; i < cata.size(); i++){
			String keyword = cata.get(i);
			String path = catalog + "\\" + keyword;
			String filePath = path + "\\" + keyword + ".html";
			Document doc = JsoupParse.parsePathText(filePath);
			getContent(doc, keyword);// 按标签存储内容
			getContent_all(doc, keyword);// 存储所有内容
			getSummary(doc, keyword);// 得到总结信息
		}
	}
	
	/**
	 * 得到单个关键词的所有内容
	 */
	public static void getContent_all(Document doc, String keyword) {
		// 判断是否存在目录，有些页面没有
		Elements body = doc.select("div.mw-content-ltr");
		if (body.size() != 0) {
			Element bodyreal = body.get(0);
			Elements childs = bodyreal.children(); // 所有子节点标签
			LinkedList<Element> list = new LinkedList<Element>(); // 链表：用于存储所有兄弟节点元素
			// 将所有节点元素加入链表中
			for (Element e : childs) {
				list.offer(e); // offer()加入元素
			}
			String all = "";
			for (int i = 0; i < list.size(); i++) {
				Element child = list.get(i); // 取出并删除
				// 去除无用的链接文本等
				String childtxt = child.text();
				Boolean useless = childtxt.equals("See also[edit]")
						|| childtxt.equals("References[edit]")
						|| childtxt.equals("External links[edit]")
						|| childtxt.equals("Further[edit]")
						|| childtxt.equals("Notes[edit]")
						|| childtxt.equals("Further reading[edit]"); // 判断标题是否为无用的
				if(!useless){
					childtxt = childtxt.replace("[edit]", "");
					all = all + childtxt + "\n";
				} else {
					break;
				}
			}
			
			try {
				CrawlerWiki.writeFile(keyword, keyword + "_all", all);
			} catch (Exception e1) {
				e1.printStackTrace();
				System.out.println("写文件出错:" + keyword + "    " + keyword + "_all");
			}
		} else {
			System.out.println(keyword + "不存在任何目录...");
		}
	}
	
	/**
	 * 得到单个关键词内容
	 */
	public static void getContent(Document doc, String keyword) {
		// 判断是否存在目录，有些页面没有
		Elements body = doc.select("div.mw-content-ltr");
		// Element body = doc.select("div.mw-content-ltr").get(0); // 得到主题内容
		if (body.size() != 0) { // 如果不存在目录，就不分析，不生成文件夹，也不生成文件
			Element bodyreal = body.get(0);
			Elements childs = bodyreal.children(); // 所有子节点标签
			LinkedList<Element> list = new LinkedList<Element>(); // 链表：用于存储所有兄弟节点元素
			// 将所有节点元素加入链表中
			for (Element e : childs) {
				list.offer(e); // offer()加入元素
				// System.out.println(e.text());
			}
			for (int i = 0; i < list.size(); i++) {
				Element child = list.get(i); // 取出并删除
				Elements childClass = child.select("span.mw-headline"); // 选取h2，h3，h4
				// 存在h2，h3节点
				if (childClass.size() != 0) {
					Element h2 = childClass.get(0); // 得到h2，h3标题名，只有一个元素
					String title = h2.text();
					// System.out.println(title);
					Boolean useless = title.equals("See also")
							|| title.equals("References")
							|| title.equals("External links")
							|| title.equals("Further")
							|| title.equals("Notes")
							|| title.equals("Further reading"); // 判断标题是否为无用的
					// 上述的四个目录内容是不需要的
					// 对于只存在上述四个目录的网页不会生成相应文件夹，例如2-3_heap
					if (!useless) { 
						String content = "";
						Element brother = child.nextElementSibling(); // 得到该标题名后的一个兄弟节点
						Elements brotherClass = brother.select("span.mw-headline"); // 选取兄弟节点的
						while (brotherClass.size() == 0) { // 大小为0，表示下个节点不是标题节点（h2等）
							content = content + brother.text() + "\n"; // 若为图片，无内容
							// System.out.println(brother.text());
							brother = brother.nextElementSibling(); // 寻找下一个兄弟节点
							try {
								brotherClass = brother.select("span.mw-headline");
							} catch (Exception e) {
								break;
							}
//							System.out.println("*******************************");
						}
						// 将该小部分内容写到文件中
						try {
							CrawlerWiki.writeFile(keyword, title, content);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							System.out.println("写文件出错:" + keyword + "    " + title);
						}
					} else {
						break;
//						System.out.println(keyword+"不存在有用的目录，只存在See also等目录...");
					}
				} else {
//					System.out.println("该节点不是标题节点...");
				}
			}
		} else {
			System.out.println(keyword + "不存在任何目录...");
		}
	}

	/**
	 * 得到Contents中的summary
	 */
	public static void getSummary(Document doc, String keyword) {
		// 判断是否存在目录，有些页面没有
		Elements body = doc.select("div.mw-content-ltr");
		if (body.size() != 0) {
			Element bodyreal = body.get(0);
			Elements childs = bodyreal.children(); // 所有子节点标签
			LinkedList<Element> list = new LinkedList<Element>(); // 链表：用于存储所有兄弟节点元素
//			LinkedList<Element> list1 = new LinkedList<Element>(); // 链表：用于存储所有兄弟节点元素
			// 将所有节点元素加入链表中
			for (Element e : childs) {
				list.offer(e); // offer()加入元素
//				list1.offer(e); // offer()加入元素
			}
			int tocId = 0;
			for (int i = 0; i < list.size(); i++) {
				Element child = list.get(i); // 取出并删除
				Elements toc = child.select("div#toc");
				if(toc.size()!=0){
					tocId = i;
//					System.out.println(keyword + "  目录为："+i);
				}
			}
			String all = "";
			for (int i = 0; i < tocId; i++) {
				Element child = list.get(i); // 取出并删除
				// 保存所有内容
				all = all + child.text() + "\n";
			}
//			System.out.println(all);
			try {
				CrawlerWiki.writeFile(keyword, keyword + "_summary", all);
			} catch (Exception e1) {
				e1.printStackTrace();
				System.out.println("写文件出错:" + keyword + "    " + keyword + "_summary");
			}
		} else {
			System.out.println(keyword + "不存在任何目录...");
		}
	}
	
	/**
	 * 爬取上下位术语网页
	 */
	public static void crawler(HashMap<String, String> map){
		for(Entry<String, String> entry : map.entrySet()){
			String keyword = entry.getKey();
			String url = entry.getValue();
			String path = catalog + "\\" + keyword;
			new File(path).mkdir();
			String filePath = path + "\\" + keyword + ".html";
			if(new File(filePath).exists()){
				System.out.println("已经存在，不用爬取...");
			} else {
				try {
					QuoraWebPage.httpClientCrawler(filePath, url);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	
	/**
	 * 得到二叉树有关的术语
	 */
	public static HashMap<String, String> getRelated(Document doc){
		HashMap<String, String> map = new HashMap<String, String>();
		Elements category = doc.select("div.mw-content-ltr")
				.select("div.mw-category").select("div.mw-category-group").select("li");
		String wikiUrl = "https://en.wikipedia.org";
		System.out.println(wikiUrl);
		System.out.println(category.size());
		for(int i = 0; i < category.size(); i++){
			String keyword = category.get(i).text();
			String url = wikiUrl + category.get(i).select("a[href]").attr("href");
			map.put(keyword, url);
//			System.out.println("keyword: " + keyword);
//			System.out.println("url: " + url);
		}
		return map;
	}

	/**
	 * 爬取wiki页面
	 */
	public static void crawlerWiki(String path, String url){
		if(new File(path).exists()){
			System.out.println("已经存在，不用爬取...");
		} else {
			try {
				QuoraWebPage.httpClientCrawler(path, url);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 按照不同标题名写内容
	 */
	public static void writeFile(String keyword, String file, String content) throws Exception {
		// 创建存储目录
		new File(catalog + "\\" + keyword).mkdir();
		// 创建待写文件
		// 判断是否存在 /\<>*?|:" 这些特殊符号
		Boolean a = file.matches("[^/\\\\<>*?|\"]+\\.[^/\\\\<>*?|\"]+");
		if (!a) {
//			System.out.println("文件名不规范:" + file);
			file = file.replace("*", " ");
			file = file.replace("/", " ");
			file = file.replace("|", " ");
			file = file.replace("<", " ");
			file = file.replace(">", " ");
			file = file.replace("?", " ");
			file = file.replace("\"", " ");
			file = file.replace("\\", " ");
			file = file.replace(":", " ");
//			System.out.println("修改后文件名:" + file);
		} else {
//			System.out.println("文件名规范...");
		}

		String filepath = catalog + "\\" + keyword + "\\" + file + ".txt";
		File f = new File(filepath);
		f.createNewFile();
		// 写文件
		BufferedWriter output = new BufferedWriter(new FileWriter(f));
		output.write(content);
		output.close();
	}

	/**
	 * 得到主题词名
	 */
	public static void getKeyword(Document doc) {
		Elements first = doc.select("h1.firstHeading");
		String keyword = first.first().text();
		System.out.println("主题为：" + keyword);
	}

	/**
	 * 得到Contents中的一级标题
	 */
	public static ArrayList<String> getFirstTitle(Document doc) {
		Elements h2 = doc.select("h2");
		ArrayList<String> firstTitle = new ArrayList<String>();
		if (h2.size() != 0) {
			h2 = h2.select("span.mw-headline");
			for (Element e : h2) {
				System.out.println(e.text());
				if (!e.text().equals("See also")
						&& !e.text().equals("References")
						&& !e.text().equals("External links")) {
					firstTitle.add(e.text());
				} else {
					// System.out.println("See also和References不需要");
				}
			}
		} else {
			System.out.println("不存在h2标签...");
		}
		return firstTitle;
	}

	/**
	 * 得到Contents中的多级标题
	 */
	public static ArrayList<String> getTitle(Document doc) {
		Elements h = doc.select("div.mw-content-ltr").select("h2,h3,h4");
		ArrayList<String> title = new ArrayList<String>();
		if (h.size() != 0) {
			h = h.select("span.mw-headline");
			for (Element e : h) {
				// System.out.println(e.text());
				if (!e.text().equals("See also")
						&& !e.text().equals("References")
						&& !e.text().equals("External links")) {
					title.add(e.text());
					System.out.println(e.text());
				} else {
					// System.out.println("See also和References不需要");
				}
			}
		} else {
			System.out.println("不存在h2、h3、h4标签...");
		}
		return title;
	}
	
}
