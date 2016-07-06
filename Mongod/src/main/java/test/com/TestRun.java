package test.com;

public class TestRun implements Runnable{
	private int time;
	private String name;
	
	public TestRun(String name) {
		super();
		this.name = name;
	}

	public void run() {
		System.out.println("��ʼ��"+time);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("������"+time);
	}

}
