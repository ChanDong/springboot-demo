package com.example.springbootdemo.service;

import com.example.springbootdemo.dao.UserDAO;
import com.example.springbootdemo.domain.User;
import com.example.springbootdemo.utils.ExcelUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by shixi03 on 2018/5/28.
 */
@Service
public class ExcelService {

    @Autowired
    private UserDAO userDAO;

    public void userListByExcel(MultipartFile excel, HttpServletRequest request) {
        String excelPath = ExcelUtil.listByExcel(excel, request);
        this.getAllByExcel(excelPath);
    }

    private void getAllByExcel(String filePath) {
        Workbook workbook = ExcelUtil.getWorkbook(filePath);
        for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
            Sheet sheet = workbook.getSheetAt(numSheet);
            if (sheet == null) {
                break;
            }

            int firstRowIndex = sheet.getFirstRowNum();
            int lastRowIndex = sheet.getLastRowNum();

            for (int rowIndex = firstRowIndex + 1; rowIndex <= lastRowIndex; rowIndex++) {
                Row currentRow = sheet.getRow(rowIndex);
                int firstColumnIndex = currentRow.getFirstCellNum();
                int lastColumnIndex = currentRow.getLastCellNum();

                for (int columnIndex = firstColumnIndex; columnIndex < lastColumnIndex; columnIndex++) {
                    String id = ExcelUtil.getCellValue(currentRow.getCell(columnIndex++), true);
                    String name = ExcelUtil.getCellValue(currentRow.getCell(columnIndex++) ,true);
                    String age = ExcelUtil.getCellValue(currentRow.getCell(columnIndex++), true);
                    User user = new User();
                    user.setId(Integer.parseInt(id));
                    user.setName(name);
                    user.setAge(Integer.parseInt(age));
                    userDAO.insertUser(user);
                }
            }
        }
    }
}
