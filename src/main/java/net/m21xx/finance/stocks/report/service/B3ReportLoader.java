package net.m21xx.finance.stocks.report.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import net.m21xx.finance.stocks.report.model.Summary;
import net.m21xx.finance.stocks.report.model.Tax;
import net.m21xx.finance.stocks.report.repository.OrdersRepository;
import net.m21xx.finance.stocks.report.repository.SummariesRepository;
import net.m21xx.finance.stocks.report.repository.TaxesRepository;

@Service
public class B3ReportLoader {

	private DateTimeFormatter dmyFmt = DateTimeFormatter.ofPattern("dd/MM/yy");
	
	@Autowired
	private OrdersRepository ordersRepo;

	@Autowired
	private SummariesRepository summariesRepo;

	@Autowired
	private TaxesRepository taxesRepo;

	@Transactional
	public void pushReport(String pFilename) {
		try {
			File file = new File(pFilename);
			
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
						
						order.setDate(convertStringToDate(row.getCell(1).toString()));
						order.setOrderType(OrderType.fromString(row.getCell(3).toString().trim()));
						order.setCount(new Double(row.getCell(8).getNumericCellValue()).intValue());
						order.setStock(parseStockSymbol(row.getCell(6).toString()));
						order.setPrice(BigDecimal.valueOf(row.getCell(9).getNumericCellValue()));
						
						ordersRepo.persistOrUpdate(order);
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
		}	
	}

//	private BigDecimal parseStringToNumber(String pNumberString) {
//		NumberFormat numFmt = NumberFormat.getInstance();
//		try {
//			return BigDecimal.valueOf(numFmt.parse(pNumberString.trim()).doubleValue());
//		} catch (ParseException e) {
//			e.printStackTrace();
//			return new BigDecimal(0);
//		}
//	}

	private String parseStockSymbol(String pSymbol) {
		StringBuilder sb = new StringBuilder();
		
		Pattern pat = Pattern.compile("([A-Za-z]+[0-9]+)(F)?");
		Matcher match = pat.matcher(pSymbol.trim());
		if (match.matches() && match.groupCount() > 1) {
			sb.append(match.group(1));
		}
		
		return sb.toString();
	}

	private Date convertStringToDate(String pDateString) {
		LocalDate orderDate = LocalDate.parse(pDateString.trim(), dmyFmt);
		Date dt = (Date.from(orderDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		return dt;
	}

	@Transactional
	public void summarizeOrders() {
		List<Date> dates = ordersRepo.findAllOrderDates();
		
		for (Date date : dates) {
			List<String> stocks = ordersRepo.findAllStocksByDate(date);
			System.out.println(String.format("-> On day %s", date));
			
			for (String stock : stocks) {
				List<Order> buyOrders = ordersRepo.findAllOrders(date, stock, OrderType.COMPRA);
				List<Order> sellOrders = ordersRepo.findAllOrders(date, stock, OrderType.VENDA);
				boolean dayTrade = buyOrders.size() > 0 && sellOrders.size() > 0;
				
				// prepara o sumário do dia
				Summary todaySummary = new Summary();
				todaySummary.setDate(date);
				todaySummary.setStock(stock);
				todaySummary.setDayTrade(dayTrade);
				
				// soma todas as operações de compra
				int currentCount = 0;
				double currentPrice = 0;
				for (Order bo : buyOrders) {
					currentCount += bo.getCount();
					currentPrice += bo.getCount() * bo.getPrice().doubleValue();
				}
				
				double averagePrice = 0D;
				int finalCount = currentCount;
				
				// caso tenha um sumário anterior, adiciona ao cálculo de preço médio (PM)
				Summary lastSummary = summariesRepo.getSummaryBeforeDate(date, stock);
				if (lastSummary != null) {
					finalCount += lastSummary.getCount();
					averagePrice = (currentPrice + lastSummary.getAveragePrice().doubleValue() * lastSummary.getCount()) / finalCount;
				}
				else {
					if (buyOrders.size() > 0) {
						averagePrice = currentPrice / currentCount;
					}
				}
				
				if (averagePrice == 0D) {
					throw new RuntimeException();
				}
				
				try {
					todaySummary.setAveragePrice(BigDecimal.valueOf(averagePrice));
				} catch (Exception e) {
					throw e;
				}
				System.out.println(String.format("---> Stock %s had %d buys and %d sells, and is %s", 
						stock, buyOrders.size(), sellOrders.size(), dayTrade ? "day trade" : "NOT day trade"));
				if (lastSummary != null) {
					System.out.println(String.format("-----> AvgPrice before %5.4f and now is %5.4f", lastSummary.getAveragePrice().doubleValue(),
							averagePrice));
				}
				else {
					System.out.println(String.format("-----> AvgPrice now is %5.4f", averagePrice));
				}
				
				if (sellOrders.size() > 0) {
					double profitValue = 0;
					for (Order so : sellOrders) {
						double sellPrice = so.getPrice().doubleValue();
						double balanceValue = (sellPrice - averagePrice) * so.getCount();
						System.out.println(String.format("-------> Sold %d for %5.4f, order result was %5.4f", so.getCount(), sellPrice,
								balanceValue));
						profitValue += balanceValue;
						finalCount -= so.getCount();
					}
					
					System.out.println(String.format("-----> For all today sell operations your profit was %5.4f", profitValue));
					
					if (dayTrade) {
						double dueValue = profitValue * 0.19;
						double retainedValue = profitValue * 0.01;
						
						Tax tax = new Tax();
						tax.setDate(date);
						tax.setStock(stock);
						tax.setDuePrice(BigDecimal.valueOf(dueValue));
						
						System.out.println(String.format("-------> For this day trade, exchange retained %5.4f, and you additional 19%% is %5.4f", 
								retainedValue, dueValue));
						
						taxesRepo.persistOrUpdate(tax);
					}
				}
				
				todaySummary.setCount(finalCount);
				summariesRepo.persistOrUpdate(todaySummary);
				
				System.out.println(String.format("---> %s status is %d papers @ %5.4f, with $ %5.4f", stock, finalCount, averagePrice,
						finalCount * averagePrice));
			}
		}
	}
	
	public void printEverything() {
		
		
	}

}
