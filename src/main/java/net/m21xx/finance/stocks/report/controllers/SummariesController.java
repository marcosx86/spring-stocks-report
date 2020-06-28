package net.m21xx.finance.stocks.report.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.m21xx.finance.stocks.report.model.Summary;
import net.m21xx.finance.stocks.report.repository.SummariesRepository;

@RestController
@RequestMapping(path = "/summaries", produces = "application/json")
public class SummariesController {
	
	@Autowired
	private SummariesRepository summariesRepo;
	
	@GetMapping("/")
	public List<Summary> getAll() {
		return summariesRepo.findAll();
	}

	@GetMapping("/active")
	public List<Summary> getActive() {
		return summariesRepo.getActiveStocksSummaries();
	}

}
