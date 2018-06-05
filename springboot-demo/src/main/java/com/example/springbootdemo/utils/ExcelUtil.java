package com.example.springbootdemo.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.piccolo.io.FileFormatException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 协助用Excel来批量上传数据
 */
public class ExcelUtil {
    private static final String EXTENSION_XLS = "xls";
    private static final String EXTENSION_XLSX = "xlsx";

    /**
      * 取得Workbook对象(xls和xlsx对象不同,不过都是Workbook的实现类) 
      * xls:HSSFWorkbook 
      * xlsx：XSSFWorkbook
     */
    public static Workbook getWorkbook(String filePath) {
        Workbook workbook = null;
        InputStream is;
        try {
            is = new FileInputStream(filePath);
            if (filePath.endsWith(EXTENSION_XLS)) {
                workbook = new HSSFWorkbook(is);
            } else if (filePath.endsWith(EXTENSION_XLSX)) {
                workbook = new XSSFWorkbook(is);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return workbook;
    }

    /**
       读取excel文件内容 
     */
    private static void readExcel(String filePath) throws FileNotFoundException, FileFormatException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("传入的文件不存在:" + filePath);
        }

        if (!(filePath.endsWith(EXTENSION_XLS) || filePath.endsWith(EXTENSION_XLSX))) {
            throw new FileFormatException("传入的文件不是Excel");
        }
    }

    /**
     * 取单元格的值
     * @param cell 单元格对象 
     * @param treatAsStr 为true时，当做文本来取值 (取到的是文本，不会把“1”取成“1.0”)
     * @return
     */
    public static String getCellValue(Cell cell, boolean treatAsStr) {
        if (cell == null) {
            return "";
        }
        if (treatAsStr) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else {
            return cell.getStringCellValue();
        }
    }

    /**
     * 获取文件的路径，并转存文件
     */
    public static String listByExcel(MultipartFile excel, HttpServletRequest request) {
        Date date;
        String cover;
        String excelPath = "";

        if (!excel.isEmpty()) {
            try {
                date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                cover = sdf.format(date);

                //文件保存路径
                excelPath = request.getSession().getServletContext().getRealPath("/") + "excels\\"
                    + cover + "." + excel.getOriginalFilename().split("\\.")[1];

                File file = new File(excelPath);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdir();
                }

                //转存文件
                excel.transferTo(new File(excelPath));
                readExcel(excelPath);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return excelPath;
    }
}
