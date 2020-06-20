package net.m21xx.finance.stocks.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import net.m21xx.finance.stocks.report.service.B3ReportLoader;

@SpringBootApplication
public class FinanceStocksReport {

	public static void main(String[] args) {
		SpringApplication.run(FinanceStocksReport.class, args);
	}
	
	@Autowired
	private B3ReportLoader reportLoader;
	
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			for (String str : args) {
				reportLoader.pushReport(str);
			}
			
			reportLoader.summarizeOrders();
			
			reportLoader.printEverything();
		};
	}
	
}