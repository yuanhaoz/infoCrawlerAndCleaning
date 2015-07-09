package webpage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/*weibifan 2013年3月13日
 * 1)term必须是下划线，不能是空格。
 * 
 * 目前支持的类型：
 * Wiki类型：en，simple
 * Page类型：Article，Category
 */
public class WikiWebPage {

	//simple
	static String wikiType="en"; 
	
	//空表示 article page
	//Category: 表示目录页面
	static String pageType="";
	
	
	public WikiWebPage() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String term = "dog";
		
		StringBuffer sb = WikiWebPage.getNormalPageByTerm(term);
	}


	//Englishi wikipedia
	public static StringBuffer getNormalPageByTerm(String term) {
		String url = buildNormalPageUrl(term);
		return getPageBufferFromWeb(url);
	}
	//兼容马健代码
	public static StringBuffer webPageByTitle(String title) {
		String fullUrl = buildNormalPageUrl(title);
		return getPageBufferFromWeb(fullUrl);
	}
		
	// 构建一般页面的URL
	public static String buildNormalPageUrl(String term) {
		return "http://en.wikipedia.org/wiki/" + term;
	}

	public static String buildCategoryUrl(String term) {
		return "http://en.wikipedia.org/wiki/Category:" + term;
	}
	
	//爬取simple wiki 网页
	public static String getSimpleArticleByTitle(String title) {
		String fullUrl = buildSimpleArticleUrl(title);
		return getPageBufferFromWeb(fullUrl).toString();
	}
	
	public static String buildSimpleArticleUrl(String term) {
		return "http://simple.wikipedia.org/wiki/" + term;
	}
	
	// 根据ULR从网上获取HTML文件
	public static StringBuffer getPageBufferFromWeb(String url) {
			return getPageBufferFromUrl(url, null);
			// return getPageBufferFromWeb3(url);
	}

	//传出参数没有回车，
	//返回值有回车，便于人工查看。
	public static StringBuffer getPageBufferFromUrl(String url,
			StringBuffer outBuffer) {
		StringBuffer sb = new StringBuffer();
		try {
			URL my_url = new URL(url);

			// BufferedReader br = new BufferedReader(new InputStreamReader(
			// my_url.openStream()));
			
			// 欺骗网站
//			 HttpURLConnection hc = (HttpURLConnection)my_url.openConnection();
//			// HttpURLConnection.setFollowRedirects(true);
//			// hc.setRequestMethod("GET");
//			 hc.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
//			// hc.connect();
//			// System.out.println(hc.getHeaderFields());
//			// System.out.println(hc.getHeaderField("User-Agent"));
//			 BufferedReader br = new BufferedReader(new InputStreamReader(hc.getInputStream()));

			// 方案2：
			HttpClient client = new DefaultHttpClient();
			HttpGet method = new HttpGet(url);
			HttpResponse httpResponse = client.execute(method);
			BufferedReader br = null;
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Can not download url: " + url);
				return sb;
			}
			InputStream is = httpResponse.getEntity().getContent();
			// do something with the input stream
			br = new BufferedReader(new InputStreamReader(is));
//			System.out.println(br.readLine());

			 // 从来没有想到问题是这个循环有问题，一直以为是爬取的问题。
			String strTemp = "";
			while (null != (strTemp = br.readLine())) {
				sb.append(strTemp + "\r\n");
//				System.out.println(strTemp);
				if (null != outBuffer)
					outBuffer.append(strTemp);
			}
			System.out.println("get page successful form " + url);
			if (null != outBuffer)
				outBuffer.append(br.toString());
		} catch (Exception ex) {
			// ex.printStackTrace();
			System.err.println("page not found: " + url);
		}

		return sb;
	}

	//------未经充分测试
	public static String getPageBufferFromUrl3(String urlStr) {
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(urlStr);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.connect();
			InputStream inputStream = connection.getInputStream();
			byte[] bs = new byte[connection.getContentLength()];
			inputStream.read(bs);
			sb.append(new String(bs, "UTF-8"));
		} catch (Exception ex) {
			// ex.printStackTrace();
			System.err.println("page not found: " + urlStr);
		}

		return sb.toString();
	}
	
	public static BufferedReader readURL(String url){
        URL urlcon;
        BufferedReader in = null;
        try {
            urlcon = new URL(url);
            HttpURLConnection connection = (HttpURLConnection)urlcon.openConnection();
            HttpURLConnection.setFollowRedirects(true);
            
            System.setProperty("http.agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0)");
            connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0)");
            System.out.println(connection.getHeaderField("User-Agent"));
            connection.connect();
            in = new BufferedReader(
                                    new InputStreamReader(
                                        connection.getInputStream()));

            String header = connection.getHeaderField(0);
            System.out.println(header);
            System.out.println("---Start of headers---");
            int i = 1;
            while ((header = connection.getHeaderField(i)) != null) {
                String key = connection.getHeaderFieldKey(i);
                System.out.println(((key==null) ? "" : key + ": ") + header);
                i++;
            }
            System.out.println(connection.getHeaderField("http.agent"));
            System.out.println("---End of headers---");
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return in;
    }
}
