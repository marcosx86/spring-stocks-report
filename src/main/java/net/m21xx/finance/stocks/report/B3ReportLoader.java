package net.m21xx.finance.stocks.report;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

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

	@Autowired
	private OrdersRepository ordersRepo;

	@Autowired
	private SummariesRepository summariesRepo;

	@Autowired
	private TaxesRepository taxesRepo;

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
						
						taxesRepo.persistOrMerge(tax);
					}
				}
				
				todaySummary.setCount(finalCount);
				summariesRepo.persistOrMerge(todaySummary);
				
				System.out.println(String.format("---> %s status is %d papers @ %5.4f, with $ %5.4f", stock, finalCount, averagePrice,
						finalCount * averagePrice));
			}
		}
	}
	
	public void printEverything() {
		taxesRepo.setOrderBy(" date ASC ");
		List<Tax> taxes = taxesRepo.findAll();
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
		Map<String, Double> map = new HashMap<String, Double>();
		for (Tax tax : taxes) {
			String mesAno = sdf.format(tax.getDate());
			Double dbl;
			if (map.containsKey(mesAno)) {
				dbl = map.get(mesAno);
			}
			else {
				dbl = 0D;
			}
			dbl += tax.getDuePrice().doubleValue();
			map.put(mesAno, dbl);
		}
		
		for (String key : map.keySet()) {
			Double dbl = map.get(key);
			System.out.println(String.format("For %s your due is %5.4f", key, dbl));
		}
	}

}
