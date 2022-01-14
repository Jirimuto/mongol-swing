package com.mongol.demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.mongol.encode.ZHConverter;

public class ChineseFileConverter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String inputDir = "D:\\Compiere2\\data\\export\\zh_CN";
		String outputDir = "D:\\Compiere2\\data\\export\\zh_HK";
		
        File directory = new File(inputDir);
        String filename[] = directory.list();
        	   
        try{ //read one text file 
			for (int i = 0; i < filename.length; i++) {
	        	
				File file = new File(filename[i]);
				if( !file.isDirectory() ){
					FileReader reader = new FileReader(inputDir+"\\"+filename[i]);
					StringBuffer fileData = new StringBuffer();
					char[] buf = new char[1024];
			        int numRead=0;
			        while((numRead=reader.read(buf)) != -1){
			            String readData = String.valueOf(buf, 0, numRead);
			            fileData.append(readData);
			            buf = new char[1024];
			        }
			        reader.close();
			        
			        String resultData = ZHConverter.convert(fileData.toString(), ZHConverter.TRADITIONAL);
			        
			        String outname = filename[i].substring(0, filename[i].indexOf("_Trl"))+"_Trl_zh_HK.xml";
			        				
			        FileWriter writer = new FileWriter(outputDir+"\\"+outname);
			        BufferedWriter out = new BufferedWriter(writer);
			        out.write(resultData);
			        out.close();
				}
			}
        }
        	catch(Exception e){ System.err.println(e.getMessage()); 
        }
	}

}
