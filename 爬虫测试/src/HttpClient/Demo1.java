package HttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * User: liuwentao
 * Time: 12-1-25 下午1:23
 */
@SuppressWarnings("deprecation")
public class Demo1 {
    /**
     * 用 get 方法访问 www.apache.org 并返回内容
     *
     * @param args
     */
    public static void main(String[] args) {
        //创建默认的 HttpClient 实例
        @SuppressWarnings("resource")
		HttpClient httpClient = new DefaultHttpClient();
        try {
            //创建 httpUriRequest 实例    http://www.apache.org/
            HttpGet httpGet = new HttpGet("http://www.baidu.com/");  //http://www.quora.com/search?q=binary+trees
            System.out.println("url=" + httpGet.getURI());
            //执行 get 请求
            HttpResponse httpResponse = httpClient.execute(httpGet);
            //获取响应实体
            HttpEntity httpEntity = httpResponse.getEntity();
            //打印响应状态
            System.out.println(httpResponse.getStatusLine());
            if (httpEntity != null) {
                //响应内容的长度
                long length = httpEntity.getContentLength();
                //响应内容
                String content = EntityUtils.toString(httpEntity);
                System.out.println("----------------------------------------");  
                System.out.println("Response content length:" + length);
                System.out.println("----------------------------------------");  
                System.out.println("Response content:" + content);
            }

            //有些教程里没有下面这行
            httpGet.abort();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭连接，释放资源
            httpClient.getConnectionManager().shutdown();
        }
    }
}
