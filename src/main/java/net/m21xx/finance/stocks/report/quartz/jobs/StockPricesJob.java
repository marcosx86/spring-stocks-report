package net.m21xx.finance.stocks.report.quartz.jobs;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;
import net.m21xx.finance.stocks.report.model.CrawlerConfig;
import net.m21xx.finance.stocks.report.service.CrawlerService;
import net.m21xx.finance.stocks.report.service.OrdersService;
import net.m21xx.finance.stocks.report.service.StocksService;
import net.m21xx.finance.stocks.report.util.Util;

@Slf4j
public class StockPricesJob extends QuartzJobBean {

	private final String JOB_SYNC_GROUP = "StockPricesJob.ThreadSyncGroup";
	
	@Autowired
	private StocksService stocksService;
	
	@Autowired
	private OrdersService ordersService;
	
	@Autowired
	private CrawlerService crawlerService;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("Starting job");
		synchronized (JOB_SYNC_GROUP) {
			log.info("Synchronized");
			
			List<String> stocks = ordersService.getAllStockSymbols();
			
			CrawlerConfig config = crawlerService.getLastSucceededConfig();
			if (config == null) {
				log.warn("No valid crawler config was found! Exiting job.");
			}
			
			for (String stock : stocks) {
				if (!Util.isNullOrEmptyOrBlank(stock)) {
					log.info(String.format("Searching for stock symbol \"%s\"...", stock));
					Double stockValue = crawlerService.getStringByPattern(stock, config);
					if (stockValue == null) {
						log.warn(String.format("Could not find stock value for symbol \"%s\", will retry later.", stock));
					}
					else {
						stocksService.updateStockValue(stock, stockValue);
						log.info(String.format("Found value \"%5.2f\" for stock symbol \"%s\"", stockValue, stock));
					}
				}
			}
		}
		log.info("Ending job");
	}

}
