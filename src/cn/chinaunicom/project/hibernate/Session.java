package cn.chinaunicom.project.hibernate;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;



public class Session {
	//假设从hbm.xml配置文件中读取出来了，存到map中
	String tableName="_Student";
	Map<String, String> cfs= new HashMap<String,String>();
	String[] methodNames;
	
	
	public Session(){
		cfs.put("_id", "id");
		cfs.put("_name", "name");
		cfs.put("_age", "age");
//		每一个属性都应该对应一个方法，getId,getName,getAge
		methodNames=new String[cfs.size()];
	}
	
	public void save(Student s) throws Exception{
		String sql=creatSQL();
		
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn=DriverManager.getConnection("jdbc:mysql://localhost/hibernate","root","123456");
		PreparedStatement ps=conn.prepareStatement(sql);
		
		
		for(int i=0;i<methodNames.length;i++){
			Method m=s.getClass().getMethod(methodNames[i]);
			Class r=m.getReturnType();
			if(r.getName().equals("java.lang.String")){
				String returnValue=(String)m.invoke(s);
				ps.setString(i+1, returnValue);
			}if(r.getName().equals("int")){
				Integer returnValue=(Integer)m.invoke(s);
				ps.setInt(i+1, returnValue);
			}
			System.out.println(m.getName()+"|"+r.getName());
		}
//		for(int i=0;i<cfs.size();i++){
//			ps.setxxx(i+1,s.getxxxx);
//		}
		ps.execute();
		ps.close();
		conn.close();
	}

	private String creatSQL() {
		
		int Index=0;
		String str1="";
		
		for(String s:cfs.keySet()){
			String v=cfs.get(s);
//			方法的首字母应该是大写
			v=Character.toUpperCase(v.charAt(0))+v.substring(1);
//			key对应的value值，前面加get=>getID...
			methodNames[Index]="get"+v;
			str1+= s +",";
			Index++;
		}
		str1=str1.substring(0,str1.length()-1);
		System.out.println(str1);
		
		
		String str2="";
		for(int i=0;i<cfs.size();i++){
			str2+="?,";
		}
//		将结尾多余的一个逗号去掉
		str2=str2.substring(0,str2.length()-1);
		System.out.println(str2);
		
//		INSERT INTO table_name (column1, column2,...) VALUES (value1, value2,....)
		String sql="insert into "+tableName+" ("+str1+")"+" values "+"("+str2+")";
		System.out.println(sql);
		return sql;
	}
	
}
