package PromMasterPlugin;

import java.util.ArrayList;

public class Person {
  private Name name;
  private int age;
  private ArrayList<String> Originators;
  public Person() {
    age = 0;
  }
 
  public Name getName() {
    return name;
  }
 
  public void setName(Name name) {
    this.name = name;
  }
 
  public int getAge() {
    return age;
  }
 
  public void setAge(int age) {
    this.age = age;
  }
  
  public void setOrigintators(ArrayList<String> Originators)
  {
	  Originators=Originators;
  }
}
