package visualization;

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
		// frame设置
		this.setVisible(true);
		this.setLocation(150, 2);
		this.setSize(1030, 850);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("碎片化知识采集与清洗");
//		this.setResizable(false);    //设置窗口大小不可改变
		c.setBackground(Color.WHITE);
		try {
			Properties props = new Properties();
			props.put("logoString", "my company");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// 功能面板设置
		funPane = new JTabbedPane();// 功能面板
		funPane.setBackground(Color.white);
		funPane.setForeground(new Color(115, 97, 255));
		// final AddResourcePanel jpAddResource = new AddResourcePanel();
		final DataCollectionAndCleaning jpSearch2 = new DataCollectionAndCleaning();
		final WekaExperiment jpSearch3 = new WekaExperiment();
		final DataPrefuseGraph jpSearch5 = new DataPrefuseGraph();
//		final Frame5 jpSearch6 = new Frame5();
		funPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		funPane.setOpaque(false);
		// 功能面板布局
		c.add(funPane, BorderLayout.CENTER);
		funPane.addTab("数据采集", jpSearch2);
		funPane.addTab("数据清洗", jpSearch3);
		funPane.addTab("辐射图展示(prefuse)", DataPrefuseTree.demo("file/data/socialnet_quora.xml", "name"));
		funPane.addTab("动态图展示(prefuse)", jpSearch5);
//		funPane.addTab("结构图展示(prefuse)", jpSearch6);
		

	}

	public static void main(String[] args) {
		new MainFrame();
	}
}
