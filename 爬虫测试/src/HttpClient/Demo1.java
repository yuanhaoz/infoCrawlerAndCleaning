package HttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * User: liuwentao
 * Time: 12-1-25 ����1:23
 */
@SuppressWarnings("deprecation")
public class Demo1 {
    /**
     * �� get �������� www.apache.org ����������
     *
     * @param args
     */
    public static void main(String[] args) {
        //����Ĭ�ϵ� HttpClient ʵ��
        @SuppressWarnings("resource")
		HttpClient httpClient = new DefaultHttpClient();
        try {
            //���� httpUriRequest ʵ��    http://www.apache.org/
            HttpGet httpGet = new HttpGet("http://www.baidu.com/");  //http://www.quora.com/search?q=binary+trees
            System.out.println("url=" + httpGet.getURI());
            //ִ�� get ����
            HttpResponse httpResponse = httpClient.execute(httpGet);
            //��ȡ��Ӧʵ��
            HttpEntity httpEntity = httpResponse.getEntity();
            //��ӡ��Ӧ״̬
            System.out.println(httpResponse.getStatusLine());
            if (httpEntity != null) {
                //��Ӧ���ݵĳ���
                long length = httpEntity.getContentLength();
                //��Ӧ����
                String content = EntityUtils.toString(httpEntity);
                System.out.println("----------------------------------------");  
                System.out.println("Response content length:" + length);
                System.out.println("----------------------------------------");  
                System.out.println("Response content:" + content);
            }

            //��Щ�̳���û����������
            httpGet.abort();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //�ر����ӣ��ͷ���Դ
            httpClient.getConnectionManager().shutdown();
        }
    }
}
