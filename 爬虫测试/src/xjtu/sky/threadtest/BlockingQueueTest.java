package xjtu.sky.threadtest;

import java.util.concurrent.ArrayBlockingQueue;  
import java.util.concurrent.BlockingQueue;  
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  
public class BlockingQueueTest {  
  
    /** *//** 
     * ����װƻ�������� 
     */  
    public static class Basket{  
        // ���ӣ��ܹ�����3��ƻ��   
        BlockingQueue basket = new ArrayBlockingQueue< String>(3);  
          
        // ����ƻ������������   
        public void produce() throws InterruptedException{  
            // put��������һ��ƻ������basket���ˣ��ȵ�basket��λ��   
            basket.put("An apple");  
        }  
        // ����ƻ������������ȡ��   
        public Object consume() throws InterruptedException{  
            // get����ȡ��һ��ƻ������basketΪ�գ��ȵ�basket��ƻ��Ϊֹ   
            return basket.take();  
        }  
    }  
    //�����Է���   
    public static void testBasket() {  
        // ����һ��װƻ��������   
        final Basket basket = new Basket();  
        // ����ƻ��������   
        class Producer implements Runnable {  
            public void run() {  
                try {  
                    while (true) {  
                        // ����ƻ��   
                        System.out.println("������׼������ƻ����"   
                                + System.currentTimeMillis());  
                        basket.produce();  
                        System.out.println("����������ƻ����ϣ�"   
                                + System.currentTimeMillis());  
                        // ����300ms   
                        Thread.sleep(300);  
                    }  
                } catch (InterruptedException ex) {  
                }  
            }  
        }  
        // ����ƻ��������   
        class Consumer implements Runnable {  
            public void run() {  
                try {  
                    while (true) {  
                        // ����ƻ��   
                        System.out.println("������׼������ƻ����"   
                                + System.currentTimeMillis());  
                        basket.consume();  
                        System.out.println("����������ƻ����ϣ�"   
                                + System.currentTimeMillis());  
                        // ����1000ms   
                        Thread.sleep(1000);  
                    }  
                } catch (InterruptedException ex) {  
                }  
            }  
        }  
          
        ExecutorService service = Executors.newCachedThreadPool();  
        Producer producer = new Producer();  
        Consumer consumer = new Consumer();  
        service.submit(producer);  
        service.submit(consumer);  
        // ��������5s����������ֹͣ   
        try {  
            Thread.sleep(5000);  
        } catch (InterruptedException e) {  
        }  
        service.shutdownNow();  
    }  
  
    public static void main(String[] args) {  
        BlockingQueueTest.testBasket();  
    }  
}  
