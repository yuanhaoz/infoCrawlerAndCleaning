package test1;

import java.awt.*;
import javax.swing.*;
import java.awt.Container;

public class framebg {

	public framebg() {
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("背景图设置");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon img = new ImageIcon("file/background.jpg");// 这是背景图片
		JLabel imgLabel = new JLabel(img);// 将背景图放在标签里

		frame.getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE));// 这里是关键，将背景标签添加到jfram的LayeredPane面板里
		imgLabel.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());// 设置背景标签的位置
		Container cp = frame.getContentPane();
		cp.setLayout(new BorderLayout());
		JButton but = new JButton("anniu");// 创建按钮
		cp.add(but, "North");// 将按钮添加入窗口的内容面板

		((JPanel) cp).setOpaque(false); // 将内容面板设为透明，这样LayeredPane面板中的背景才能显示出来

		frame.setSize(500, 300);
		frame.setVisible(true);

	}

}