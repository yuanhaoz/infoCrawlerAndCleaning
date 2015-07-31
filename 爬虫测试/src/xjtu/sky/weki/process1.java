package xjtu.sky.weki;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * ArraylistÅÅĞò
 */
public class process1 {
	public static void main(String[] args) {
		Student zlj = new Student("¶¡ÏşÓî", 21);
		Student dxy = new Student("ÕÔËÄ", 22);
		Student cjc = new Student("ÕÅÈı", 11);
		Student lgc = new Student("ÁõÎä", 19);
		List<Student> studentList = new ArrayList<Student>();
		studentList.add(zlj);
		studentList.add(dxy);
		studentList.add(cjc);
		studentList.add(lgc);
		System.out.println("°´ÕÕÄêıgÅÅĞò£º");
		Collections.sort(studentList, new SortByAge());
		for (Student student : studentList) {
			System.out.println(student.getName() + " / " + student.getAge());
		}
		System.out.println(" ========= ");
		System.out.println("°´ÕÕĞÕÃûÅÅĞò");
		Collections.sort(studentList, new SortByName());
		for (Student student : studentList) {
			System.out.println(student.getName() + " / " + student.getAge());
		}
	}
}

class SortByAge implements Comparator {
	public int compare(Object o1, Object o2) {
		Student s1 = (Student) o1;
		Student s2 = (Student) o2;
		if (s1.getAge() > s2.getAge())
			return 1;
		else if (s1.getAge() == s2.getAge()) {
			return 0;
		}
		return -1;
	}
}

class SortByName implements Comparator {
	public int compare(Object o1, Object o2) {
		Student s1 = (Student) o1;
		Student s2 = (Student) o2;
		if (s1.getName().compareTo(s2.getName()) < 0)
			return -1;
		else if (s1.getName().compareTo(s2.getName()) > 0) {
			return 1;
		}
		return 0;
	}
}

class Student {
	private int age;
	private String name;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Student(String name, int age) {
		this.age = age;
		this.name = name;
	}
}
