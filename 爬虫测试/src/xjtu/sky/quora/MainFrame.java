package xjtu.sky.quora;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -4209263941008740114L;
	private JTabbedPane funPane;
	
	public MainFrame() {
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
		// frame����
		this.setVisible(true);
		this.setLocation(150, 2);
		this.setSize(1030, 850);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("��Ƭ��֪ʶ�ɼ�����ϴ");
//		this.setResizable(false);    //���ô��ڴ�С���ɸı�
		c.setBackground(Color.WHITE);
		try {
			Properties props = new Properties();
			props.put("logoString", "my company");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// �����������
		funPane = new JTabbedPane();// �������
		funPane.setBackground(Color.white);
		funPane.setForeground(new Color(115, 97, 255));
		// final AddResourcePanel jpAddResource = new AddResourcePanel();
		final Frame1 jpSearch2 = new Frame1();
		final Frame2 jpSearch3 = new Frame2();
		final Frame4 jpSearch5 = new Frame4();
//		final Frame5 jpSearch6 = new Frame5();
		funPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		funPane.setOpaque(false);
		// ������岼��
		c.add(funPane, BorderLayout.CENTER);
		funPane.addTab("���ݲɼ�", jpSearch2);
		funPane.addTab("������ϴ", jpSearch3);
		funPane.addTab("����ͼչʾ(prefuse)", Frame3.demo("data/socialnet_quora.xml", "name"));
		funPane.addTab("��̬ͼչʾ(prefuse)", jpSearch5);
//		funPane.addTab("�ṹͼչʾ(prefuse)", jpSearch6);
		

	}

	public static void main(String[] args) {
		new MainFrame();
	}
}
