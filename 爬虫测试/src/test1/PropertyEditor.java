package test1;

import java.io.FileInputStream;

import java.io.FileOutputStream;

import java.util.Properties;

public class PropertyEditor {

	public static void main(String[] args) throws Exception {

		Properties prop = new Properties();// ���Լ��϶���
		FileInputStream fis = new FileInputStream("file/prop.properties");// �����ļ�������
		prop.load(fis);// �������ļ���װ�ص�Properties������
		fis.close();// �ر���

		// ��ȡ����ֵ��sitename�����ļ��ж���
		System.out.println("��ȡ����ֵ��sitename=" + prop.getProperty("sitename"));

		// ��ȡ����ֵ��countryδ���ļ��ж��壬���ڴ˳����з���һ��Ĭ��ֵ���������޸������ļ�
		System.out.println("��ȡ����ֵ��country=" + prop.getProperty("country", "�й�"));

		// �޸�sitename������ֵ
		prop.setProperty("sitename", "Boxcode");

		// ���һ���µ�����studio
		prop.setProperty("studio", "Boxcode Studio");

		// �ļ������
		FileOutputStream fos = new FileOutputStream("prop.properties");

		// ��Properties���ϱ��浽����
		prop.store(fos, "Copyright (c) Boxcode Studio");
		fos.close();// �ر���

	}

}
