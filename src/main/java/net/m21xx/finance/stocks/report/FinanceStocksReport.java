package net.m21xx.finance.stocks.report;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import net.m21xx.finance.stocks.report.quartz.SchedulerService;
import net.m21xx.finance.stocks.report.service.ReportParserService;
import net.m21xx.finance.stocks.report.util.Util;

@SpringBootApplication
public class FinanceStocksReport {

	public static void main(String[] args) {
		SpringApplication.run(FinanceStocksReport.class, args);
	}
	
	@Autowired
	private B3ReportLoader reportLoader;
	
	@Autowired
	private ReportParserService reportParser;
	
//	@Autowired
//	private SchedulerService schedulerService;
	
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			List<String> files = Util.searchForFilenames(
					"C:\\Users\\marco\\OneDrive\\Documentos\\Finanças\\Notas Corretagem Inter\\2020\\", 
					".xls");
			
			for (String str : files) {
				File file = new File(str);

//				reportParser.parseCeiReport(file);
				reportParser.parseInterReport(file, Util.parseDateFromInterFilename(str));
			}
			
			reportLoader.summarizeOrders();
			
			reportLoader.printEverything();
			
//			schedulerService.startAllSchedulers();
		};
	}

}
