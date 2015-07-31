package xjtu.sky.threadtest;

public class ThreadDemo9_3 {
	public static void main(String[] args) {
//		test0();
//		test1();
		test2();
	}
	
	public static void test0(){
		TestThread t = new TestThread();
		//一个线程对象只能启动一次
		t.start();
		t.start();
		t.start();
		t.start();
	}
	
	public static void test1(){
		//启动了四个线程，分别执行各自的操作
		new TestThread().start();
		new TestThread().start();
		new TestThread().start();
		new TestThread().start();
	}
	
	public static void test2(){
		TestThread1 t = new TestThread1() ;
		//启动了四个线程，并实现了资源共享的目的
		new Thread(t).start();
		new Thread(t).start();
		new Thread(t).start();
		new Thread(t).start();
	}
}

//继承Thread类
class TestThread extends Thread{
	private int tickets = 20;
	public void run(){
		while(true)
		{
			if(tickets > 0){
				System.out.println(Thread.currentThread().getName() + "出售票" + tickets--);
			}
		}
	}
}

//实现Runnable接口
class TestThread1 implements Runnable{
	private int tickets = 20;
	public void run(){
		while(true)
		{
			if(tickets > 0){
				System.out.println(Thread.currentThread().getName() + "出售票" + tickets--);
			}
		}
	}
}

