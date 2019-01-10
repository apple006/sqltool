package com.excel;

public class ImpDefaultExcelCheckAndPack implements IExcelCheckAndPack{

	private ExcelReader reader;
	public ImpDefaultExcelCheckAndPack(ExcelReader reader){
		this.reader = reader;
	}
	public String check(RowEntity row) throws CheckException {
		return success;
	}

	public Object pack( RowEntity row) {
		String[] readExcelTitle = reader.getReadExcelTitle();
		String [] arr = new String[readExcelTitle.length];
		for (int i = 0; i < arr.length; i++) {
			try {
				arr[i] = row.getValue(readExcelTitle[i]);
			} catch (CheckException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
		}
		return arr;
	}


}
