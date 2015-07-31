package xjtu.sky.threadtest;

public class ThreadDemo9_3 {
	public static void main(String[] args) {
//		test0();
//		test1();
		test2();
	}
	
	public static void test0(){
		TestThread t = new TestThread();
		//һ���̶߳���ֻ������һ��
		t.start();
		t.start();
		t.start();
		t.start();
	}
	
	public static void test1(){
		//�������ĸ��̣߳��ֱ�ִ�и��ԵĲ���
		new TestThread().start();
		new TestThread().start();
		new TestThread().start();
		new TestThread().start();
	}
	
	public static void test2(){
		TestThread1 t = new TestThread1() ;
		//�������ĸ��̣߳���ʵ������Դ�����Ŀ��
		new Thread(t).start();
		new Thread(t).start();
		new Thread(t).start();
		new Thread(t).start();
	}
}

//�̳�Thread��
class TestThread extends Thread{
	private int tickets = 20;
	public void run(){
		while(true)
		{
			if(tickets > 0){
				System.out.println(Thread.currentThread().getName() + "����Ʊ" + tickets--);
			}
		}
	}
}

//ʵ��Runnable�ӿ�
class TestThread1 implements Runnable{
	private int tickets = 20;
	public void run(){
		while(true)
		{
			if(tickets > 0){
				System.out.println(Thread.currentThread().getName() + "����Ʊ" + tickets--);
			}
		}
	}
}

