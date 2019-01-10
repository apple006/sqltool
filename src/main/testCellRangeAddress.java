package main;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by J on 2017/5/9
 */
public class testCellRangeAddress {

    public static void main(String arg[]) throws IOException {
        // 创建一个webbook，对应一个Excel文件
        XSSFWorkbook wb = new XSSFWorkbook();
        // 在webbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = wb.createSheet("补贴、余额消费、红包明细、商家入账");
        //创建合并单元格
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 2);
        sheet.addMergedRegion(cra);
        cra = new CellRangeAddress(0, 0, 3, 6);
        sheet.addMergedRegion(cra);
        cra = new CellRangeAddress(0, 0, 7, 10);
        sheet.addMergedRegion(cra);
        // 在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        XSSFRow row = sheet.createRow(0);
        // 创建单元格，并设置值表头 设置表头居中
        XSSFCellStyle style = wb.createCellStyle();
//        style.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
//        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
//        style.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
//        style.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
//        style.setAlignment(XSSFCellStyle.VERTICAL_CENTER); // 创建一个居中格式
//        style.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        sheet.setColumnWidth((short) 0, (short) (35.7 * 100));
        sheet.setColumnWidth((short) 1, (short) (35.7 * 100));
        sheet.setColumnWidth((short) 2, (short) (35.7 * 100));
        sheet.setColumnWidth((short) 3, (short) (35.7 * 100));
        sheet.setColumnWidth((short) 4, (short) (35.7 * 100));
        sheet.setColumnWidth((short) 5, (short) (35.7 * 100));
        sheet.setColumnWidth((short) 6, (short) (35.7 * 100));
        sheet.setColumnWidth((short) 7, (short) (35.7 * 100));
        sheet.setColumnWidth((short) 8, (short) (35.7 * 100));
        sheet.setColumnWidth((short) 9, (short) (35.7 * 100));
        sheet.setColumnWidth((short) 10, (short) (35.7 * 100));
        XSSFCell cell = row.createCell(0);
        cell.setCellValue("移动视频");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("收款");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("付款");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellStyle(style);

        cell = row.createCell(9);
        cell.setCellStyle(style);

        cell = row.createCell(10);
        cell.setCellStyle(style);

        //第一行写入表头
        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue("渠道");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("paygate中文名");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("渠道商户号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("笔数");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("金额");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("手续费");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("净收款额");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("笔数");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellValue("金额");
        cell.setCellStyle(style);

        cell = row.createCell(9);
        cell.setCellValue("手续费");
        cell.setCellStyle(style);

        cell = row.createCell(10);
        cell.setCellValue("付款总额");
        cell.setCellStyle(style);
        //模拟数据源
        List list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
        list.add(9);
        list.add(0);
        for (int i = 1; i <= list.size(); i++) {
            row = sheet.createRow(i + 1);
            // ，创建单元格，并设置值
            cell = row.createCell(0);//渠道
            cell.setCellValue(1000000000);
            cell.setCellStyle(style);

            cell = row.createCell(1);//paygate中文名
            cell.setCellValue(1000000000);
            cell.setCellStyle(style);

            cell = row.createCell(2);//渠道商户号
            cell.setCellValue(1000000000);
            cell.setCellStyle(style);

            cell = row.createCell(3);//笔数
            cell.setCellValue(1000000000);
            cell.setCellStyle(style);

            cell = row.createCell(4);//金额
            cell.setCellValue(1000000000);
            cell.setCellStyle(style);

            cell = row.createCell(5);//手续费
            cell.setCellValue(1000000000);
            cell.setCellStyle(style);

            cell = row.createCell(6);//净收款额
            cell.setCellValue(1000000000);
            cell.setCellStyle(style);

            cell = row.createCell(7);//笔数
            cell.setCellValue(1000000000);
            cell.setCellStyle(style);

            cell = row.createCell(8);//金额
            cell.setCellValue(1000000000);
            cell.setCellStyle(style);

            cell = row.createCell(9);//手续费
            cell.setCellValue(1000000000);
            cell.setCellStyle(style);

            cell = row.createCell(10);//付款总额
            cell.setCellValue(1000000000);
            cell.setCellStyle(style);
        }
        cra = new CellRangeAddress(list.size()+2, list.size()+2, 0, 2);
        sheet.addMergedRegion(cra);
        row = sheet.createRow(list.size()+2);
        cell = row.createCell(0);
        cell.setCellValue("合计");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("nihao");
        cell.setCellStyle(style);

        //第二个区域
        cra = new CellRangeAddress(list.size()+5, list.size()+5, 0, 2);
        sheet.addMergedRegion(cra);
        cra = new CellRangeAddress(list.size()+5, list.size()+5, 3, 6);
        sheet.addMergedRegion(cra);

        row = sheet.createRow(list.size()+5);
        cell = row.createCell(0);
        cell.setCellValue("移动视频");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("付款");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellStyle(style);

        row = sheet.createRow(list.size()+6);
        cell = row.createCell(0);
        cell.setCellValue("渠道");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("paygate中文名");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("渠道商户号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("笔数");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("金额");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("手续费");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("付款金额");
        cell.setCellStyle(style);

        //模拟数据源
        List list1 = new ArrayList();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);
        list1.add(5);
        list1.add(6);
        list1.add(7);
        list1.add(8);
        list1.add(9);
        list1.add(0);
        for (int i = 1; i <= list1.size(); i++) {
            row = sheet.createRow(i + 6+list.size());
            // ，创建单元格，并设置值
            cell = row.createCell(0);//渠道
            cell.setCellValue(1);
            cell.setCellStyle(style);

            cell = row.createCell(1);//paygate中文名
            cell.setCellValue(1);
            cell.setCellStyle(style);

            cell = row.createCell(2);//渠道商户号
            cell.setCellValue(1);
            cell.setCellStyle(style);

            cell = row.createCell(3);//笔数
            cell.setCellValue(1);
            cell.setCellStyle(style);

            cell = row.createCell(4);//金额
            cell.setCellValue(1);
            cell.setCellStyle(style);

            cell = row.createCell(5);//手续费
            cell.setCellValue(1);
            cell.setCellStyle(style);

            cell = row.createCell(6);//付款金额
            cell.setCellValue(1);
            cell.setCellStyle(style);
        }
        cra = new CellRangeAddress(list.size()+list1.size()+7, list.size()+list1.size()+7, 0, 2);
        sheet.addMergedRegion(cra);
        row = sheet.createRow(list.size()+list1.size()+7);
        cell = row.createCell(0);
        cell.setCellValue("合计");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("nihao");
        cell.setCellStyle(style);

        //第三个区域
        cra = new CellRangeAddress(list.size()+list1.size()+10, list.size()+list1.size()+12, 0, 0);
        sheet.addMergedRegion(cra);

        cra = new CellRangeAddress(list.size()+list1.size()+10, list.size()+list1.size()+10, 1, 2);
        sheet.addMergedRegion(cra);

        cra = new CellRangeAddress(list.size()+list1.size()+10, list.size()+list1.size()+10, 3, 4);
        sheet.addMergedRegion(cra);

        cra = new CellRangeAddress(list.size()+list1.size()+10, list.size()+list1.size()+10, 5, 6);
        sheet.addMergedRegion(cra);

        cra = new CellRangeAddress(list.size()+list1.size()+10, list.size()+list1.size()+10, 7, 8);
        sheet.addMergedRegion(cra);

        row = sheet.createRow(list.size()+list1.size()+10);
        cell = row.createCell(0);
        cell.setCellValue("移动视频");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("补贴");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("余额消费");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("红包");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("商家入账");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellStyle(style);

        row = sheet.createRow(list.size()+list1.size()+11);
        cell = row.createCell(0);
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("笔数");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("金额");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("笔数");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("金额");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("笔数");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("金额");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("笔数");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellValue("金额");
        cell.setCellStyle(style);

        row = sheet.createRow(list.size()+list1.size()+12);
        cell = row.createCell(0);
        cell.setCellStyle(style);
        String name = "D://1.xlsx";
        FileOutputStream out = new FileOutputStream(new File(name));
        wb.write(out);
        wb.close();
        out.close();
    }
}
