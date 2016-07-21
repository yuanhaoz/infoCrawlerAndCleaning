package experiment;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import base.DirFile;
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

public class WikiInfoExtraction {
	private static WikiInfoExtraction a = new WikiInfoExtraction();
	private static String catalog = "F:/data_predeal_wiki/课程术语（维基）/Data_structure/";

	// 得到主题词名
	public void getKeyword(Document doc) {
		Elements first = doc.select("h1.firstHeading");
		String keyword = first.first().text();
		System.out.println("主题为：" + keyword);
	}

	// 得到Contents中的一级标题
	public ArrayList<String> getFirstTitle(Document doc) {
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

	// 得到Contents中的多级标题
	public ArrayList<String> getTitle(Document doc) {
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

	// 得到Content
	public void getContent(Document doc, String keyword) {
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
			// 从链表中取出元素，判断如果是标题h2，h3等就建立txt
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
							|| title.equals("Further reading"); // 判断标题是否为无用的
					
					// 上述的四个目录内容是不需要的
					// 对于只存在上述四个目录的网页不会生成相应文件夹，例如2-3_heap
					if (!useless) { 
						String content = "";
						// Elements brothers = child.siblingElements();
						// if(brothers.size()!=0){
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
							// System.out.println("*******************************");
						}
						// 由于Graph_isomorphism_problem.html存在特殊情况，所以进行下面修改
						// 另外一种思路是在主程序读文件时忽略该文件
//						try {
//							Elements brotherClass = brother
//									.select("span.mw-headline"); // 选取兄弟节点的
//							while (brotherClass.size() == 0) { // 大小为0，表示下个节点不是标题节点（h2等）
//								content = content + brother.text() + "\n"; // 若为图片，无内容
//								// System.out.println(brother.text());
//								brother = brother.nextElementSibling(); // 寻找下一个兄弟节点
//								try {
//									brotherClass = brother
//											.select("span.mw-headline");
//								} catch (Exception e) {
//									break;
//								}
//								// System.out.println("*******************************");
//							}
//						} catch (Exception e2) {
//							System.out.println(keyword + "\n" + title
//									+ "不存在后继兄弟节点...");
//						}

						// 将该小部分内容写到文件中
						try {
							a.writeFile(keyword, title, content);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							System.out.println("写文件出错:" + keyword + "    " + title);
						}
					} else {
//						 System.out.println(keyword+"不存在有用的目录，只存在See also等目录...");
					}
				} else {
					// System.out.println("++++++++++++++++++++++++");
					// System.out.println("该节点不是标题节点...");
				}
			}
		} else {
			System.out.println(keyword + "不存在任何目录...");
		}

	}

	// 按照不同标题名写内容
	public void writeFile(String keyword, String file, String content)
			throws Exception {
		// 创建存储目录
//		String catalog = "F:/data_predeal_wiki/课程术语（维基）/Data_structure/";
		File path = new File(catalog + keyword);
		if (!path.exists()) {
			path.mkdir();
		} else {
			// System.out.println("...");
		}

		// 创建待写文件

		// Boolean a = file.contains("*");
		// 判断是否存在 /\<>*?|:" 这些特殊符号
		Boolean a = file.matches("[^/\\\\<>*?|\"]+\\.[^/\\\\<>*?|\"]+");
		if (!a) {
			// System.out.println("文件名不规范:" + file);
			file = file.replace("*", " ");
			file = file.replace("/", " ");
			file = file.replace("|", " ");
			file = file.replace("<", " ");
			file = file.replace(">", " ");
			file = file.replace("?", " ");
			file = file.replace("\"", " ");
			file = file.replace("\\", " ");
			file = file.replace(":", " ");
			// System.out.println("修改后文件名:" + file);
		} else {
			// System.out.println("文件名规范...");
		}

		String filepath = catalog + keyword + "/" + file + ".txt";
		File f = new File(filepath);
		if (!f.exists()) {
			f.createNewFile();
		} else {
			// System.out.println("。。。");
		}
		// 写文件
		BufferedWriter output = new BufferedWriter(new FileWriter(f));
		output.write(content);
		output.close();
	}

	// 具体实现
	public static void test(String keyword) {
		// 解析对象
		// String keyword = "A-star_algorithm";
		String path = catalog + keyword + ".html";
		Document doc = JsoupParse.parsePathText(path);

		// 解析函数
		// a.getKeyword(doc);
		// a.getFirstTitle(doc);
		// a.getTitle(doc);
		a.getContent(doc, keyword);
	}

	// 具体实现
	public static void realize() {
		// 解析对象
//		String catalog = "F:/课程术语（维基）/Data_structure/";
		ArrayList<String> keywordSet = DirFile
				.getFileNamesFromDirectorybyArraylist(catalog); // 读取所有文件名
		for (int i = 0; i < keywordSet.size(); i++) {
			String keyword = keywordSet.get(i);
//			System.out.println(keyword);
			// 由于Graph_isomorphism_problem.html存在特殊情况，所以进行下面修改
			// 另外一种思路是在主程序读文件时忽略该文件
			// Comb_sort网页是乱码
			if(!keyword.equals("Graph_isomorphism_problem")){
				String path = "F:/课程术语（维基）/Data_structure/" + keyword + ".html";
				Document doc = JsoupParse.parsePathText(path);
				a.getContent(doc, keyword);
			} else {
				System.out.println("Graph_isomorphism_problem.html网页是个例外，" + "选择单独分析，不在主程序里分析...");
			}
			
		}
	}

	// 主函数
	public static void main(String[] args) {
		// 单个词分析
		String keyword = "Binary_tree2";
		test(keyword);

		// 整体分析
//		realize();
	}

}
