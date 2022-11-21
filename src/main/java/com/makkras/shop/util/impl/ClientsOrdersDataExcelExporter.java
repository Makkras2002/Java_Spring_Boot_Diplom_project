package com.makkras.shop.util.impl;

import com.makkras.shop.entity.CompleteClientsOrder;
import com.makkras.shop.entity.ComponentClientsOrder;
import com.makkras.shop.exception.CustomServiceException;
import com.makkras.shop.util.ExcelExporter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ClientsOrdersDataExcelExporter implements ExcelExporter {
    private static final String ORDER_COMPLETED = "Подтверждён";
    private static final String ORDER_NOT_COMPLETED = "Не подтверждён";
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private void writeHeaderLine() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("UserOrders");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        font.setColor(new XSSFColor(Color.green));
        style.setFont(font);
        createCell(row, 0, "ID заказа", style);
        createCell(row, 1, "Имя клиента", style);
        createCell(row, 2, "Email клиента", style);
        createCell(row, 3, "Название товара", style);
        createCell(row, 4, "Количество товара в заказе (ед.)", style);
        createCell(row, 5, "Полная цена за данный вид товара (BYN)", style);
        createCell(row, 6, "Дата совершения", style);
        createCell(row, 7, "Адрес доставки", style);
        createCell(row, 8, "Цена доставки", style);
        createCell(row, 9, "Статус заказа", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Long) {
            cell.setCellValue((Long)value);
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) value).doubleValue());
        } else if (value instanceof LocalDate) {
            cell.setCellValue(value.toString());
        }
        else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines(List<CompleteClientsOrder> ordersData) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        int rowCount = 1;
        Row row = sheet.createRow(rowCount++);
        for (CompleteClientsOrder clientsOrder : ordersData) {
            int startRowIndex = row.getRowNum();
            createCell(row, 0, clientsOrder.getCompleteClientsOrderId(), style);
            createCell(row, 1, clientsOrder.getUser().getLogin(), style);
            createCell(row, 2, clientsOrder.getUser().getEmail(), style);
            createCell(row, 6, clientsOrder.getCompleteClientsOrderDate(), style);
            createCell(row, 7, clientsOrder.getDeliveryAddress(), style);
            createCell(row, 8, clientsOrder.getDeliveryPrice(), style);
            if(clientsOrder.isCompleted()) {
                createCell(row, 9, ORDER_COMPLETED, style);
            } else {
                createCell(row, 9, ORDER_NOT_COMPLETED, style);
            }
            for(ComponentClientsOrder componentClientsOrder : clientsOrder.getClientsComponentOrders()) {
                createCell(row,3,componentClientsOrder.getProduct().getProductName(),style);
                createCell(row,4,componentClientsOrder.getOrderedProductAmount(),style);
                createCell(row,5,componentClientsOrder.getOrderedProductFullPrice(),style);
                row = sheet.createRow(rowCount++);
            }
            if(clientsOrder.getClientsComponentOrders().size()>1) {
                sheet.addMergedRegion(new CellRangeAddress(startRowIndex,row.getRowNum()-1,0,0));
                sheet.addMergedRegion(new CellRangeAddress(startRowIndex,row.getRowNum()-1,1,1));
                sheet.addMergedRegion(new CellRangeAddress(startRowIndex,row.getRowNum()-1,2,2));
                sheet.addMergedRegion(new CellRangeAddress(startRowIndex,row.getRowNum()-1,6,6));
                sheet.addMergedRegion(new CellRangeAddress(startRowIndex,row.getRowNum()-1,7,7));
                sheet.addMergedRegion(new CellRangeAddress(startRowIndex,row.getRowNum()-1,8,8));
                sheet.addMergedRegion(new CellRangeAddress(startRowIndex,row.getRowNum()-1,9,9));
            }
        }
    }

    @Override
    public void export(HttpServletResponse response, List<CompleteClientsOrder> clientsOrders) throws CustomServiceException {
        try {
            writeHeaderLine();
            writeDataLines(clientsOrders);
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (IOException exception) {
            throw new CustomServiceException(exception.getMessage());
        }

    }
}
