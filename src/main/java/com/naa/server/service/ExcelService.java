package com.naa.server.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class ExcelService {

    public void processExcelFile(MultipartFile file) throws Exception {
        // Получаем InputStream из MultipartFile
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            // Берем первый лист из файла
            Sheet sheet = workbook.getSheetAt(0);

            // Итерируем по строкам и ячейкам листа
            for (Row row : sheet) {
                for (Cell cell : row) {
                    // Обрабатываем каждую ячейку в зависимости от ее типа
                    switch (cell.getCellType()) {
                        case STRING:
                            System.out.print(cell.getStringCellValue() + "\t");
                            break;
                        case NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t");
                            break;
                        case BOOLEAN:
                            System.out.print(cell.getBooleanCellValue() + "\t");
                            break;
                        default:
                            System.out.print(" \t");
                            break;
                    }
                }
                System.out.println();
            }
        }
    }
}
