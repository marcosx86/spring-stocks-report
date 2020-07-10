package net.m21xx.finance.stocks.report.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;

import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.m21xx.finance.stocks.report.enums.OrderType;
import net.m21xx.finance.stocks.report.model.Order;
import net.m21xx.finance.stocks.report.util.Util;

@Service
public class ReportParserService {
	
	@Autowired
	private OrdersService ordersService;

	@Transactional
	public boolean parseCeiReport(File file) {
		try {
			DateTimeFormatter dmyFmt = DateTimeFormatter.ofPattern("dd/MM/yy");
			
			Workbook workbook = WorkbookFactory.create(file);
			Sheet sheet = workbook.getSheetAt(0);
			
			Iterator<Row> itRow = sheet.rowIterator();
			boolean start = false;
			while (itRow.hasNext()) {
				Row row = itRow.next();
				
				if (start) {
					Cell cell = row.getCell(3);
					if ("".equals(cell.getStringCellValue())) {
						break;
					}
					else {
						Order order = new Order();
						
						order.setDate(Util.convertStringToDate(row.getCell(1).toString(), dmyFmt));
						order.setOrderType(OrderType.fromString(row.getCell(3).toString().trim()));
						order.setCount(new Double(row.getCell(8).getNumericCellValue()).intValue());
						order.setStock(Util.parseStockSymbol(row.getCell(6).toString()));
						order.setPrice(BigDecimal.valueOf(row.getCell(9).getNumericCellValue()));
						
						ordersService.persistOrder(order);
					}
				}
				else {
					Cell cell = row.getCell(1);
					if ("Data Negócio".equals(cell.getStringCellValue())) {
						start = true;
						continue;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Transactional
	public boolean parseInterReport(File file, Date date) {
		try {
			Workbook workbook = WorkbookFactory.create(file);
			Sheet sheet = workbook.getSheetAt(0);
			
			Iterator<Row> itRow = sheet.rowIterator();
			while (itRow.hasNext()) {
				Row row = itRow.next();
				
				Cell cell = row.getCell(0);
				if ("1-Bovespa".equals(cell.getStringCellValue())) {
					cell = row.getCell(1);
					String str = cell.getStringCellValue();
					
					boolean isBuyOrder = "C".equals(str.trim());
					
					cell = row.getCell(5);
					str = cell.getStringCellValue();
					double orderQtd = Double.parseDouble(str);
					
					cell = row.getCell(6);
					str = cell.getStringCellValue();
					str = str.replaceAll("\\.", "").replaceAll(",", ".");
					double orderStockValue = Double.parseDouble(str);
					
					cell = row.getCell(3);
					String stock = cell.getStringCellValue();
					stock = Util.parseStockSymbol(stock);
					
					Order order = new Order();
					
					order.setDate(date);
					order.setOrderType(isBuyOrder ? OrderType.COMPRA : OrderType.VENDA);
					order.setStock(stock);
					order.setCount(new BigDecimal(orderQtd).intValue());
					order.setPrice(new BigDecimal(orderStockValue));
					
					ordersService.persistOrder(order);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
}
