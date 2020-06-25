package net.m21xx.finance.stocks.report;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import net.m21xx.finance.stocks.report.quartz.SchedulerService;
import net.m21xx.finance.stocks.report.service.B3ReportLoader;

@SpringBootApplication
public class FinanceStocksReport {

	public static void main(String[] args) {
		SpringApplication.run(FinanceStocksReport.class, args);
	}
	
	@Autowired
	private B3ReportLoader reportLoader;
	
	@Autowired
	private SchedulerService schedulerService;
	
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		schedulerService.startAllSchedulers();
		
		return args -> {
			for (String str : args) {
				reportLoader.pushReport(str);
			}
			
			reportLoader.summarizeOrders();
			
			reportLoader.printEverything();
			
			crawler();
		};
	}

	private void crawler() {
		Document doc;
		Elements elems;
		Element elem;
		
		try {
			doc = Jsoup.connect("https://www.meusdividendos.com/acao/MGLU3").get();
			System.out.println(String.format("Title: %s", doc.title()));
			elems = doc.select("div.wrapper section.content div.box-profile p b"); // NullPointerException
			elem = elems.first();
			System.out.println(String.format("Value is = %s", elem.text()));
			
			doc = Jsoup.connect("https://www.guiainvest.com.br/raiox/default.aspx?sigla=MGLU3").get();
			System.out.println(String.format("Title: %s", doc.title()));
			elems = doc.select("div#areaConteudo div#barraHeader div#divPerfilResumo li#liCotacao span"); // NullPointerException
			elem = elems.first();
			System.out.println(String.format("Value is = %s", elem.text()));
			
			doc = Jsoup.connect("https://app.tororadar.com.br/analise/mglu3").get();
			System.out.println(String.format("Title: %s", doc.title()));
			elems = doc.select("div.content-wrapper div.an-content div.analise-summary div.analise-summary-info div.oscilation"); // NullPointerException
			elem = elems.first();
			System.out.println(String.format("Value is = %s", elem.text()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
