package org.processmining.plugins.olapdiscovery.processmining.plugins.olapdiscovery.transformation;


/**
 *  Object to save relations 
*/
public class SocialRelation {

	protected  String resource1;
	protected  String resource2;
	protected  double strenght;
	
	public SocialRelation(String resource1,String resource2, double strenght) {
		// TODO Auto-generated constructor stub
		this.resource1=resource1;
		this.resource2=resource2;
		this.strenght=strenght;
	}
	
	public String GetResource1()
	{
		return resource1;
	}

	public String GetResource2()
	{
		return resource2;
	}
	
	public double GetRate()
	{
		return strenght;
	}


}
