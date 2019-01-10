package com.excel;

public class DefaultExcelCheckAndPack implements IExcelCheckAndPack{

	public String check(RowEntity row) throws CheckException {
		return success;
	}

	public Object pack(RowEntity row) {

		return new Object();
	}


}
