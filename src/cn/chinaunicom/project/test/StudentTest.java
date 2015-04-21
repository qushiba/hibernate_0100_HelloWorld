package cn.chinaunicom.project.test;

import cn.chinaunicom.project.hibernate.Session;
import cn.chinaunicom.project.hibernate.Student;

public class StudentTest {
	public static void main(String[] args) throws Exception{
		Student s = new Student();
		s.setId(2);
		s.setName("s2");
		s.setAge(2);
		 
		Session session= new Session();
		
		//把S的内容拿出来拼成SQL语句
		session.save(s);
		
		
		
	}

}
