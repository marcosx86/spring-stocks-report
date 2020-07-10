package net.m21xx.finance.stocks.report.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.m21xx.finance.stocks.report.model.Order;
import net.m21xx.finance.stocks.report.repository.OrdersRepository;

@Service
public class OrdersService {

	@Autowired
	private OrdersRepository repository;

	public List<String> getAllStockSymbols() {
		return repository.getAllStockSymbols();
	}
	
	@Transactional
	public void persistOrder(Order obj) {
		repository.persistOrMerge(obj);
	}
	
}
