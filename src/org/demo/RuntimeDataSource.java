package org.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.omg.SendingContext.RunTime;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class RuntimeDataSource {

	
	
	public static void main(String[] args) throws IOException {
		HashMap<Integer,RunTimeLibrary> runTimeLibraries = null;//getRunTimesFromFile();
		if(runTimeLibraries == null || runTimeLibraries.size() > 0)
		{		
				runTimeLibraries = getRunTimesFromWeb();
		}
		
		for(int i = 0; i < runTimeLibraries.size(); i++)
		{
			System.out.println("#" + runTimeLibraries.get(i).id + ": " + runTimeLibraries.get(i).Description());
			
		}
		
	}
	
	public static HashMap<Integer, RunTimeLibrary> getRunTimesFromFile() throws IOException{
		HashMap<Integer, RunTimeLibrary> runTimeLibraries = new HashMap<Integer, RunTimeLibrary>();
		File file = new File("RunTimeLibraryObjects.txt");
		if (file.isFile() && file.canRead()) {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null && !line.isEmpty()) {
		   String[] entry = line.split("|");
		   //runTimeLibraries.put(Integer.parseInt(entry[0]), new RunTimeLibrary(entry[1], entry[2]));
		   System.out.println("key: " + entry[0] + " Value: id-" + entry[1] +": " + entry[2]);
		}
		br.close();
		}
		return runTimeLibraries;
	}
	
	public static HashMap<Integer, RunTimeLibrary> getRunTimesFromWeb() throws FileNotFoundException, UnsupportedEncodingException
	{
WebDriver browser = new FirefoxDriver();
		
		// This is the url with the blog post
		String url = "http://social.bioware.com/1467178/blog/3681";
		
		// load the url
		browser.get(url);
		
		browser.manage().window().maximize();
		
		if(browser.getTitle().contains("Choose Language"))
		{
			WebElement englishCheckbox = browser.findElement(By.xpath("//*[@id='lang_option_1']"));
			englishCheckbox.click();
			
			WebElement submitButton = browser.findElement(By.xpath("//*[@id='language_chooser_form']/input[2]"));
			submitButton.click();
		}
		
		String xpathLocation = "//*[@id='content']/table/tbody/tr/td/div[1]/table/tbody/tr/td/div[4]";
		
		WebElement blogPost = browser.findElement(By.xpath(xpathLocation));
		
		HashMap<Integer, RunTimeLibrary> runTimeLibraries = new HashMap<Integer,RunTimeLibrary>();
		int previousKey = 0;
		int currentHashMapKey = 0;
		String[] lines = blogPost.getText().split(System.getProperty("line.separator"));
		
		for(int i = 0; i < lines.length; i++)
		{
			if(i == 0)
			{
				// This is the first line, do nothing
			}
			else if (lines[i].isEmpty())
			{
				// It's an empty line, do nothing
			}
			else
			{
				if(!Character.isDigit(lines[i].charAt(0))){
					RunTimeLibrary runTimeLibrary = runTimeLibraries.get(previousKey);
					
					runTimeLibrary.Description(runTimeLibrary.Description() + lines[i]);
					runTimeLibraries.put(previousKey,runTimeLibrary);
				}
				else{
					int runTimeLibraryIdIndex = lines[i].indexOf(".");
					String currentKey = lines[i].substring(0, runTimeLibraryIdIndex);
					
					String currentValue = lines[i].substring(runTimeLibraryIdIndex+1,lines[i].length()).trim();
					
					runTimeLibraries.put(currentHashMapKey, new RunTimeLibrary(currentKey, currentValue));

					previousKey = currentHashMapKey; currentHashMapKey++;

				}

				
				
			}
			
		}
		
		
		
		browser.close();
		
		PrintWriter writer = new PrintWriter("RunTimeLibraryObjects.txt", "UTF-8");
		for(int i = 0; i < runTimeLibraries.size(); i++)
		{
			writer.println(i + "|" + runTimeLibraries.get(i).id + "|" + runTimeLibraries.get(i).Description());
			
		}
		writer.close();
		
		
		return runTimeLibraries;
	}

}
