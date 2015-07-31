package HttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 * ��ʾ��¼
 * @author xiyang
 *
 */
@SuppressWarnings("deprecation")
public class Demo3 {
	public static void main(String[] args) throws ClientProtocolException, IOException {
		@SuppressWarnings("resource")
		HttpClient httpclient = new DefaultHttpClient();
		
		//���õ�¼����
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("email", "18710617839"));
		formparams.add(new BasicNameValuePair("password", "201019930619512a"));
		UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams, "UTF-8");
		
		//�½�Http  post����
		HttpPost httppost = new HttpPost("http://www.renren.com/Home.do");
		httppost.setEntity(entity1);

		//�������󣬵õ���Ӧ
		HttpResponse response = httpclient.execute(httppost);
	
		String set_cookie = response.getFirstHeader("Set-Cookie").getValue();
		
		//��ӡCookieֵ
		System.out.println(set_cookie.substring(0,set_cookie.indexOf(";")));
		
		//��ӡ���صĽ��
		HttpEntity entity = response.getEntity();
		
		StringBuilder result = new StringBuilder();
		if (entity != null) {
			InputStream instream = entity.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(instream));
			String temp = "";
			while ((temp = br.readLine()) != null) {
				String str = new String(temp.getBytes(), "utf-8");
				result.append(str);
			}
		}
		System.out.println(result);
	}
}
