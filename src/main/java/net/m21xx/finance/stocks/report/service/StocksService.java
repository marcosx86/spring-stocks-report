package net.m21xx.finance.stocks.report.service;

import java.math.BigDecimal;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.m21xx.finance.stocks.report.model.Stock;
import net.m21xx.finance.stocks.report.repository.StocksRepository;

@Service
public class StocksService {

	@Autowired
	private StocksRepository repository;

	public Stock findByStock(String stock) {
		return repository.findByStock(stock);
	}

	@Transactional
	public void updateStockValue(String stockSymbol, Double stockValue) {
		Stock stock = findByStock(stockSymbol);
		if (stock == null) {
			stock = new Stock();
			stock.setStock(stockSymbol);
		}
		
		stock.setPrice(BigDecimal.valueOf(stockValue));
		stock.setUpdated(new Date());
		
		repository.persistOrMerge(stock);
	}
	
}
