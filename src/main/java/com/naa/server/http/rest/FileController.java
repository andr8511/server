package com.naa.server.http.rest;

import com.naa.server.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final ExcelService excelService;

    @Autowired
    public FileController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "Файл не выбран.";
        }

        try {
            excelService.processExcelFile(file);
            return "Файл успешно обработан.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Ошибка при обработке файла: " + e.getMessage();
        }
    }
}
