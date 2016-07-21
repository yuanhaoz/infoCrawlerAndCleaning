/**
 * 
 */
package webpage;

import java.util.Vector;

import base.DirFile;

/**
 * @author weibifan
 *目的：下载多个编辑历史页面。
 *分为2种：
 *1、编辑历史页面。 一个条目对应多个编辑历史页面。需要进行简单解析，才能确定后续页面。
 *2、历史页面。一个条目对应多个编辑历史页面。由条目和oldid唯一确定。
 *
 *编辑历史页面地址的格式。
 *举例：http://en.wikipedia.org/w/index.php?title=Main_Page&offset=20050401164511&limit=500&action=history
 *offset如果为空表示从当前开始。
 *limit表示编辑历史页面的条目有多少。
 *
 *历史页面地址的格式。
 *举例：http://en.wikipedia.org/w/index.php?title=Main_Page&oldid=2536920
 *
 *问题：下载可能不全。多运行几次。
 */
public class WikiHistoryPage {

	static String limitValue="500";  //最大500，再大也没有用
	static String offsetValue="";
	static String offsetValueNext="";
	/**
	 * 
	 */
	public WikiHistoryPage() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String title="Main_Page";		
		title="Wikipedia:Database_reports/Templates_transcluded_on_the_most_pages";		
		Vector<String> editHistoryPages=getEditHistoryPageByTitle(title);
		String storeRoot="F:\\EG\\";
		String name=title.replace("/", "-");
		name=name.replace(":", "-");
		for(int i=0;i<editHistoryPages.size();i++){
			
			String outFile=storeRoot+name+"_"+String.valueOf(i)+".html";
			DirFile.storeString2File(editHistoryPages.get(i), outFile);
		}
	}
	
	//返回多个编辑历史页面。调用getNextOffsetValue();
	public static Vector<String> getEditHistoryPageByTitle(String title) {
		Vector<String> editHistoryPages=new Vector<String>();
		String fullUrl = buildEditHistoryPageUrl(title);
		String editHis= WikiWebPage.getPageBufferFromWeb(fullUrl).toString();
		editHistoryPages.add(editHis);
		System.out.println("get the first edit history page.");
		String offsetValueNext=getNextOffsetValue(editHis);
		int index=0;
		while(!offsetValueNext.isEmpty()){
			fullUrl = buildEditHistoryPageUrl(title,offsetValueNext);
			editHis= WikiWebPage.getPageBufferFromWeb(fullUrl).toString();
			editHistoryPages.add(editHis);
			index++;
			System.out.println("get the "+index+" edit history page.");
			offsetValueNext=getNextOffsetValue(editHis);
		}
		
		return editHistoryPages;
	}
	
	//解析编辑历史页面，得到下一个编辑历史页面的地址。
	public static String getNextOffsetValue(String pageBuf){
		
		/*
		 <a href="/w/index.php?title=Main_Page&amp;offset=20040308034652&amp;limit=500&amp;action=history" title="Main Page" rel="next" class="mw-nextlink">older 500</a>
		 */
		String str=">older 500</a>";
		int pos=pageBuf.indexOf(str);
		if(pos<0){
			//第1种可能：该页面编辑历史不足500条。
			//第2种可能：出错。
//			System.out.println(pageBuf);
			return "";
		}
		int pos2=pageBuf.indexOf(str, pos+str.length());
		if(pos2<0){
			//第1种可能：该页面编辑历史不足500条。
			//第2种可能：出错。
//			System.out.println(pageBuf);
			return "";
		}
		
		String str2="offset=";
		int pos3=pageBuf.lastIndexOf(str2, pos2);
		if(pos3<0){
			System.err.println("pos3 error!");
			return "";
		}
		int beginIndex=pos3+str2.length();
		
		String str3="&amp;limit=";
		int pos4=pageBuf.indexOf(str3, pos3);
		if(pos4<0){
			System.err.println("pos4 error!");
			return "";
		}
		int endIndex=pos4;
		offsetValueNext=pageBuf.substring(beginIndex, endIndex);
		
		return offsetValueNext;
	}
	
	public static String getFirstEditHistoryPageByTitle(String title) {
		String fullUrl = buildEditHistoryPageUrl(title);
		return WikiWebPage.getPageBufferFromWeb(fullUrl).toString();
	}
	
	public static String buildEditHistoryPageUrl(String term){
		return buildEditHistoryPageUrl(term,"");
	}
	
	public static String buildEditHistoryPageUrl(String term, String offsetValue){
		String url="http://en.wikipedia.org/w/index.php?title=";
		url=url+term;
		url=url+"&offset="+offsetValue;
		url=url+"&limit="+limitValue;
		url=url+"&action=history";
		return url;
	}
	
	public static String getHistoryPageByTitle(String title, String oldidValue) {
		String fullUrl = buildHistoryPageUrl(title,oldidValue);
		return WikiWebPage.getPageBufferFromWeb(fullUrl).toString();
	}
	//http://en.wikipedia.org/w/index.php?title=Main_Page&oldid=2536920
    public static String buildHistoryPageUrl(String term, String oldidValue){
		String url="http://en.wikipedia.org/w/index.php?title=";
		url=url+term;
		url=url+"&oldid="+oldidValue;
		return url;
	}
}
