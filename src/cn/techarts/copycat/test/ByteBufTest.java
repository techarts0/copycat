package cn.techarts.copycat.test;

import org.junit.Test;
import org.junit.Assert;

public class ByteBufTest {
	@Test
	public void testCRLF() {
		byte r = '\r', n = '\n';
		Assert.assertEquals(r, 0X0D);
		Assert.assertEquals(n, 0X0A);
	}
	
	@Test
	public void testClone() {
		var p1 = new Person(1, 20, "Wuzhangmeng");
		var p2 = p1.clone();
		Assert.assertEquals(p2.getAge(), 20);
	}
	
	class Person implements Cloneable{
		private int id;
		private int age;
		private String name;
		
		public Person(int id, int age, String name) {
			this.id = id;
			this.age = age;
			this.name = name;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
	
		@Override
		public Person clone(){
			try {
				return (Person)super.clone();
			}catch(Exception e) {
				return null;
			}
		}
	}
}
