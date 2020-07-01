package net.m21xx.finance.stocks.report.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.m21xx.finance.stocks.report.model.Stock;
import net.m21xx.finance.stocks.report.repository.StocksRepository;

@RestController
@RequestMapping(path = "/stocks", produces = "application/json")
public class StocksController {

	@Autowired
	private StocksRepository stocksRepo;
	
	@GetMapping("/")
	public List<Stock> getAll() {
		return stocksRepo.findAll();
	}
	
}
