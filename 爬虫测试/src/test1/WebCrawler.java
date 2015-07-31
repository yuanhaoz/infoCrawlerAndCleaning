package test1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {
	ArrayList<String> allurlSet = new ArrayList<String>();//���е���ҳurl����Ҫ����Ч��ȥ�ؿ��Կ���HashSet
	ArrayList<String> notCrawlurlSet = new ArrayList<String>();//δ��������ҳurl
	HashMap<String, Integer> depth = new HashMap<String, Integer>();//������ҳ��url���
	int crawDepth  = 2; //�������
	int threadCount = 10; //�߳�����
	int count = 0; //��ʾ�ж��ٸ��̴߳���wait״̬
	public static final Object signal = new Object();   //�̼߳�ͨ�ű���
	
	public static void main(String[] args) {
		final WebCrawler wc = new WebCrawler();
//		wc.addUrl("http://www.126.com", 1);
		wc.addUrl("http://www.cnblogs.com", 1);
		long start= System.currentTimeMillis();
		System.out.println("��ʼ����.........................................");
		wc.begin();
		
		while(true){
			if(wc.notCrawlurlSet.isEmpty()&& Thread.activeCount() == 1||wc.count==wc.threadCount){
				long end = System.currentTimeMillis();
				System.out.println("�ܹ�����"+wc.allurlSet.size()+"����ҳ");
				System.out.println("�ܹ���ʱ"+(end-start)/1000+"��");
				System.exit(1);
//				break;
			}
			
		}
	}
	private void begin() {
		for(int i=0;i<threadCount;i++){
			new Thread(new Runnable(){
				public void run() {
//					System.out.println("��ǰ����"+Thread.currentThread().getName());
//					while(!notCrawlurlSet.isEmpty()){ ----------------------------------��1��
//						String tmp = getAUrl();
//						crawler(tmp);
//					}
					while (true) { 
//						System.out.println("��ǰ����"+Thread.currentThread().getName());
						String tmp = getAUrl();
						if(tmp!=null){
							crawler(tmp);
						}else{
							synchronized(signal) {  //------------------��2��
								try {
									count++;
									System.out.println("��ǰ��"+count+"���߳��ڵȴ�");
									signal.wait();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							
						}
					}
				}
			},"thread-"+i).start();
		}
	}
	public synchronized  String getAUrl() {
		if(notCrawlurlSet.isEmpty())
			return null;
		String tmpAUrl;
//		synchronized(notCrawlurlSet){
			tmpAUrl= notCrawlurlSet.get(0);
			notCrawlurlSet.remove(0);
//		}
		return tmpAUrl;
	}
//	public synchronized  boolean isEmpty() {
//		boolean f = notCrawlurlSet.isEmpty();
//		return f;
//	}
	
	public synchronized void  addUrl(String url,int d){
			notCrawlurlSet.add(url);
			allurlSet.add(url);
			depth.put(url, d);
	}
	
	//����ҳsUrl
	public  void crawler(String sUrl){
		URL url;
		try {
				url = new URL(sUrl);
//				HttpURLConnection urlconnection = (HttpURLConnection)url.openConnection(); 
				URLConnection urlconnection = url.openConnection();
				urlconnection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
				InputStream is = url.openStream();
				BufferedReader bReader = new BufferedReader(new InputStreamReader(is));
				StringBuffer sb = new StringBuffer();//sbΪ��������ҳ����
				String rLine = null;
				while((rLine=bReader.readLine())!=null){
					sb.append(rLine);
					sb.append("/r/n");
				}
				
				int d = depth.get(sUrl);
				System.out.println("����ҳ"+sUrl+"�ɹ������Ϊ"+d+" �����߳�"+Thread.currentThread().getName()+"����");
				if(d<crawDepth){
					//������ҳ���ݣ�������ȡ����
					parseContext(sb.toString(),d+1);
				}
//				System.out.println(sb.toString());

			
		} catch (IOException e) {
//			crawlurlSet.add(sUrl);
//			notCrawlurlSet.remove(sUrl);
			e.printStackTrace();
		}
	}

	//��context��ȡurl��ַ
	public  void parseContext(String context,int dep) {
	    String regex = "<a href.*?/a>";
//		String regex = "<title>.*?</title>";
		String s = "fdfd<title>��&nbsp;��</title><a href=\"http://www.iteye.com/blogs/tag/Google\">Google</a>fdfd<>";
		// String regex ="http://.*?>";
		Pattern pt = Pattern.compile(regex);
		Matcher mt = pt.matcher(context);
		while (mt.find()) {
//			System.out.println(mt.group());
			Matcher myurl = Pattern.compile("href=\".*?\"").matcher(
					mt.group());
			while(myurl.find()){
				String str = myurl.group().replaceAll("href=\"|\"", "");
//				System.out.println("��ַ��:"+ str);
				if(str.contains("http:")){ //ȡ��һЩ����url�ĵ�ַ
					if(!allurlSet.contains(str)){
						addUrl(str, dep);//����һ���µ�url
						if(count>0){ //����еȴ����̣߳�����
							synchronized(signal) {  //---------------------��2��
								count--;
								signal.notify();
							}
						}
						
					}
				}
			}
		}
	}
}	
