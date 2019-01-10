package com.excel;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


/**
 * 操作Excel表格的功能类
 * @param <T>
 */
public class ExcelReader<T> {
    private int heardNumber;
    private String path ;
    private String[] readExcelTitle;
    private byte[] ba = new byte[1024*1024*20];
    private IExcelCheckAndPack checkModel;

    /**
     * 设置表头行数，用以从第heardNumber+1行开始取数时使用
     * @param heardNumber
     * @param path  文件路径
     * @throws IOException 
     */
    public ExcelReader(int heardNumber,String path) throws IOException{
    	this.heardNumber =heardNumber-1;
    	this.path = path;
    	FileInputStream reader = new FileInputStream(path);
    	reader.read(ba);
    	checkModel = new DefaultExcelCheckAndPack();
    }
    /**
     * 默认第一行为表头
     * @param path  文件路径
     * @throws IOException 
     */
    public ExcelReader(String path) throws IOException{
    	this(0,path);
    	this.heardNumber =0;
    	this.path = path;
    	checkModel = new DefaultExcelCheckAndPack();
    }
    /**
     * 默认第一行为表头
     * @param path  文件路径
     */
    public ExcelReader(byte[] ba,int i){
    	this.heardNumber =i;
    	this.ba = ba;
    
    	checkModel = new DefaultExcelCheckAndPack();
    }
    /**
     * 读取Excel表格表头的内容
     * @param InputStream
     * @return String 表头内容的数组
     * @throws Exception 
     */
    private String[] readExcelTitle(HSSFRow row) throws CheckException {
    	if(row==null){
    		throw new CheckException("设置表头所在行："+(heardNumber+1)+"  没有表头数据");
    	}
        int colNum = row.getPhysicalNumberOfCells();
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            title[i] = getStringCellValue(row.getCell((short) i));
        }
        return title;
    }

    /**
     * 读取Excel数据内容
     * @param InputStream
     * @return Map 包含单元格数据内容的Map对象
     * @throws IOException 
     * @throws CheckException 
     * @throws Exception 
     */
    public ExcelEntity<T> readExcelContent() throws  IOException, CheckException {
        HSSFWorkbook wb = getWorkBook();
        ExcelEntity<T> excel = readExcelValue(wb);
        return excel;
    }
    
    /**
     * 
     * @return Excel文件对象
     * @throws IOException
     */
	private HSSFWorkbook getWorkBook() {
		if(ba==null||ba.length==0)return null;
		POIFSFileSystem fs = null;
		ByteArrayInputStream stream = null;
		HSSFWorkbook wb = null;
			try {
				stream = new ByteArrayInputStream(ba); 
				fs = new POIFSFileSystem(stream);
				 wb = new HSSFWorkbook(fs);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		return wb;
	}
	
	/**
	 * 读取Excel内容
	 * @param wb
	 * @return ExcelEntity 封装单元格数据
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private ExcelEntity<T> readExcelValue(HSSFWorkbook wb) throws CheckException {
		ExcelEntity<T> excel = new ExcelEntity<T>();
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row = sheet.getRow(heardNumber);
		 readExcelTitle = readExcelTitle(row);
		if(checkModel==null){
			try {
				throw new Exception("请事先IExcelCheckAndPack接口");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	    int rowNum = sheet.getLastRowNum();
		String value;
        
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = heardNumber+1; i <= rowNum; i++) {
        	int colNum = row.getPhysicalNumberOfCells();
//        	colNum = row.getLastCellNum();
        	RowEntity rowEntity = new RowEntity();
            row = sheet.getRow(i);
//            if(colNum!=readExcelTitle.length){
//            	throw new CheckException("第"+i+"行数据列数与表头所在行"+heardNumber+"列数不相同");
//            }
            int j = 0;
            while (j < colNum) {
            	value = getMergedRegionValue(sheet, i, j);
                rowEntity.put(readExcelTitle[j].trim().toUpperCase(), value);
                j++;
            }
            String check = checkModel.check(rowEntity);
            if(!IExcelCheckAndPack.success.equals(check)){
            	excel.clear();
            	throw new CheckException(check);
            }
            Object pack;
            try{
            	 pack = checkModel.pack(rowEntity);
            }catch(Exception e){
            	throw new CheckException("Excel表格中数据类型有错误！");
            }
        	excel.add((T) pack);
        }
        
        return excel;
	}
	
	public String[] getReadExcelTitle(){
		return readExcelTitle;
	}

	public String getMergedRegionValue(HSSFSheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
////			Region ca = sheet.getMergedRegion(i);
//			int firstColumn = ca.getColumnFrom();
//			int lastColumn = ca.getColumnTo();
//			int firstRow = ca.getRowFrom();
//			int lastRow = ca.getRowTo();
//			if (row >= firstRow && row <= lastRow) {
//				if (column >= firstColumn && column <= lastColumn) {
//					  HSSFRow fRow = sheet.getRow(firstRow);  
//					  return getStringCellValue(fRow.getCell((short) firstColumn));  
//				}
//			}
		}
		return getStringCellValue(sheet.getRow(row).getCell((short) column)) ;
	}
    /**
     * 获取单元格数据内容为字符串类型的数据
     * 
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    @SuppressWarnings("deprecation")
	private String getStringCellValue(HSSFCell cell) {
        String strCell = "";
        if(cell==null){
        	return strCell;
        }
        switch (cell.getCellType()) {
        case STRING:
            strCell = cell.getStringCellValue();
            break;
        case NUMERIC:
			double d = cell.getNumericCellValue();
			if (Math.round(d) - d == 0) {
				strCell = String.valueOf((long) Math.round(d));
			}else{
			strCell = String.valueOf(d);
			}
			break;
        case BOOLEAN:
            strCell = String.valueOf(cell.getBooleanCellValue());
            break;
        case BLANK:
            strCell = "";
            break;
        default:
            strCell = "";
            break;
        }
        return strCell;
    }
    
    /**
     * 得到校验封装模型
     * @return
     */
    public IExcelCheckAndPack  getCheckAndPackModel(){
    	return checkModel;
    }
    /**
     * 得到校验封装模型
     * @return
     */
    public void  setCheckAndPackModel(IExcelCheckAndPack checkModel){
    	this.checkModel = checkModel;
    }


    public static void main(String[] args) {
//		ExcelReader<TextVO> excelReader = new ExcelReader<TextVO>(3,"d:\\天津.xls");
//		ExcelEntity<TextVO> readExcelContent;
//		try {
//			readExcelContent = excelReader.readExcelContent();
//			List<TextVO> array = readExcelContent.getList();
//			for (int i = 0; i < array.size(); i++) {
//				TextVO rowEntity = array.get(i);
//				String value = rowEntity.getF();
//				System.out.println(rowEntity.getA()+"--"+rowEntity.getB()+" --"+rowEntity.getC()+" "+rowEntity.getD()+"-- "+rowEntity.getE()+" --"+rowEntity.getF());
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (CheckException e) {
//			e.printStackTrace();
//		}
    }
    
}