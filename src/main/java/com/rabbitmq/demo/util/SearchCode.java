package com.rabbitmq.demo.util;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;


public class SearchCode
{	
	
	private static String rootPath = "D:\\test\\";
	
	private static String dirPath = "E:\\test\\result.xls";
	
	public static void main(String[] args) throws IOException {
		process(dirPath);
	}
	
	public static void process(String dirFile) throws UnsupportedEncodingException, FileNotFoundException {
		
		
		
		ArrayList<String> listFileName = new ArrayList<String>(); 
        getAllFileName(rootPath,listFileName);
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        data = getData(listFileName);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("sheet1");
        HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
        HSSFRow hssfRow = sheet.createRow(0);
		HSSFCell cell0 = hssfRow.createCell(0, CellType.STRING);
		HSSFCell cell1 = hssfRow.createCell(1, CellType.STRING);
		cell0.setCellValue("文件");
		cell0.setCellStyle(cellStyle);
		cell1.setCellValue("汉字");
		cell1.setCellStyle(cellStyle);
		sheet.setColumnWidth(0, 10000);
		sheet.setColumnWidth(1, 10000);
		for (int i=0; i<data.size(); i++) {
			HSSFRow row = sheet.createRow(i+1);
			Map<String, String> maps = data.get(i);
			Set<String> value = maps.keySet();
			value.stream().forEach(item -> {
				HSSFCell hCell0 = row.createCell(0);
				HSSFCell hCell1 = row.createCell(1);
				hCell0.setCellValue(item);
				hCell0.setCellStyle(cellStyle);
				hCell1.setCellValue(maps.get(item));
				hCell1.setCellStyle(cellStyle);
			});
			
		}
		OutputStream fos = new FileOutputStream(dirFile);
		try {
			workbook.write(fos);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static List<Map<String, String>> getData(List<String> listFileName) {
		
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		if (listFileName.size() >0) {
			for (String filePath: listFileName) {
	        	File file = new File(filePath);
	    		String[] types = new String[]{".vue", ".properties", ".xml", ".java", ".txt"};
	            List<String> typeList = Arrays.asList(types);
	            if (!file.isDirectory() && filePath.lastIndexOf(".") != -1 
	            		&& typeList.contains(filePath.substring(filePath.lastIndexOf(".")))) {
	            	try {	
	            		InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
	                    BufferedReader br = new BufferedReader(isr);
	                    String str = "" ;
	                    int index = 1;
	                    int i=0;
	                    while ((str=br.readLine()) != null) {
	                    	str=new String(str.getBytes(),"utf-8");
	                    	str=str.trim().replaceAll(" ", "");
	                    	String result = "";        
	                    	int b1 = str.lastIndexOf("//");
	                    	int a1 = str.indexOf("//");
	                    	int a2 = str.indexOf("/*");
	                    	int a3 = str.indexOf("*");
	                    	int a4 = str.indexOf("#");
	                    	int a5 = str.indexOf("<!--");
	                    	int a6 = str.indexOf("-->");
	                    	if (a1 == 0 || a2 == 0 || a3 == 0 || a4 == 0) {
	                    		index++;
	                    		continue;
	                    	}
	                    	result = (b1 > 0 && b1 >= a1) ? str.substring(0, b1) : str;  //过滤掉写在行尾的注释
	                    	if (a6 !=-1 && a5 != -1 && a5 != 0)  result = result.substring(0,a5); //过滤掉写在行尾的注释
	                    	if (filePath.substring(filePath.lastIndexOf(".")).equals(".xml")) {	
	                    		
	                    		if (i == 0) {
	                    			if (a5 != -1) {
	                    				i = 1;
	                    				if (a6 != -1) i=0;
	                    				index++;
	                    				continue;
	                    			} else {
	                    				if (checkLine(result).length()>0) {
	        	                    		Map<String, String> map = new HashMap<String, String>();
	        	                    		String relativePath = file.getPath().replace(rootPath, "");
	        	                    		map.put(relativePath + " " + "(" + index + ")", checkLine(result));
	        	                    		data.add(map);
	        	                		} 
	                    				index++;
	                    				continue;
	                    			}
	                    		} else  {
	                    			if (a6 != -1) {
	                    				i=0;
	                    			} 
	                    			index++;
	                    			continue;
	                    		}
	                    	}
	                    }
	                    isr.close();
	                    br.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						
					}
	            }
	        }
		}
		return data;
	}
	
	public static void getAllFileName(String path,ArrayList<String> fileName) throws UnsupportedEncodingException
    {
		path = new String(path.getBytes(),"utf-8");
        File file = new File(path);
        File[] files = file.listFiles();
        if(files != null && files.length >0) {
        	List<File> fileList = Arrays.asList(files);
        	fileList.stream().forEach(item -> {
        		fileName.add(item.getPath());
        	});
                         
        for(File a:files)
        {
            if(a.isDirectory())
            {
                getAllFileName(a.getAbsolutePath(),fileName);
            }
        }
        } else {
        	fileName.add(file.getPath());
        }
    }
	
	public static void fileAction(String filePath, File dirFile) throws FileNotFoundException, Exception {
		File file = new File(filePath);
		
		String[] types = new String[]{".vue", ".properties", ".xml", ".java", ".txt"};
        List<String> typeList = Arrays.asList(types);
        if (!file.isDirectory() && filePath.lastIndexOf(".") != -1 
        		&& typeList.contains(filePath.substring(filePath.lastIndexOf(".")))) {
        	check(filePath, dirFile);
        }
     
	}
	
	public static void check(String filePath, File dirFile) throws Exception, FileNotFoundException {
		File file = new File(filePath);		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		HSSFSheet sheet = workbook.createSheet();
		sheet.setDefaultColumnStyle(0, cellStyle);
		sheet.setDefaultColumnStyle(1, cellStyle);
		FileOutputStream fos = new FileOutputStream("excel.xls", true);
		InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String str = "" ;
        HSSFRow hssfRow = sheet.createRow(0);
    	hssfRow.createCell(0, CellType.STRING).setCellValue("文件");
    	hssfRow.createCell(1, CellType.STRING).setCellValue("汉字");
        int i=1;
        while ((str=br.readLine()) != null) {
        	if (str.equals("")) continue;
        	str=str.trim().replaceAll(" ", "");
        	String result = "";        
        	int b1 = str.lastIndexOf("//");
        	int a1 = str.indexOf("//");
        	int a2 = str.indexOf("/*");
        	int a3 = str.indexOf("*");
        	int a4 = str.indexOf("#");
        	int a5 = str.indexOf("<!--");
        	
        	if (a1 == 0 || a2 == 0 || a3 == 0 || a4 == 0) continue;
        	
        	result = (b1 > 0 && b1 >= a1) ? str.substring(0, b1) : str;  //过滤掉写在行尾的注释
        	if (checkLine(result).length()>0) {
        		hssfRow = sheet.createRow(i);
            	hssfRow.createCell(0).setCellValue(file.getPath());
            	hssfRow.createCell(1).setCellValue(checkLine(result));
            	i++;
    		}            	        	     	            	            	            	           	            
        }	
        workbook.write(fos);
        fos.flush();
        fos.close();
        br.close();
	}
	
	public static String checkLine(String param) {
		 StringBuilder result = new StringBuilder();
		 char[] c = param.toCharArray();
         for (char item : c) {
        	 if (isChinese(String.valueOf(item))) {
        		 result.append(item);
        	 }
         }
         return result.toString();
     } 
	
	   /**
     * 判断是否为汉字
     * @param str 字
     * @return
     */
    public static boolean isChinese(String str) {
      Pattern p_str = Pattern.compile("[\\u4e00-\\u9fa5]+");
      Matcher m = p_str.matcher(str);
      if(m.find()&&m.group(0).equals(str)){
       return true;
      }
      return false;
    }
    
    
}
