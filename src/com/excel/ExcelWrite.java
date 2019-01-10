package com.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWrite {
	private File file = null;
	public ExcelWrite(File file){
		this.file = file;
	}
	
	public void writeExcelValue(ArrayList<String[]> arr) throws IOException  {
		//第一步，创建一个webbook，对应一个Excel文件
		XSSFWorkbook wb = new XSSFWorkbook();
	    //第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
	    XSSFSheet sheet = wb.createSheet("表1");
	    //第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
//	    HSSFRow headName = sheet.createRow((int)0);
//	    HSSFRow headCode = sheet.createRow((int)1);
	    //第四步，创建单元格，并设置值表头  设置表头居中
	    XSSFCellStyle style = wb.createCellStyle();
	    int size = arr.size();
	    int columnSize = arr.get(0).length;
        for (int i = 0; i < size; i++) {
        	XSSFRow createRow = sheet.createRow(i);
        	for (short j = 0; j < columnSize; j++) {
        		createRow.createCell(j).setCellValue(arr.get(i)[j]);
			}
		}
        //第六步，将文件存到指定位置
        FileOutputStream fout = new FileOutputStream(file.getPath());
        wb.write(fout);
        fout.close();
    }

}  
