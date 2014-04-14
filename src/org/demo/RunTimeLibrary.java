package org.demo;

public class RunTimeLibrary {

	String id;
	String description;
	
	public RunTimeLibrary(String id, String description)
	{
		this.id = id;
		this.description = description;
	}
	
	public String Id(){return this.id;}
	public void Id(String id){this.id = id;}
	
	public String Description(){return this.description;}
	public void Description(String description){this.description = description;}
	
}
