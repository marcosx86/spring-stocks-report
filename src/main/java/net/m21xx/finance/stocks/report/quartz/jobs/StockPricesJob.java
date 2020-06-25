package net.m21xx.finance.stocks.report.quartz.jobs;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;
import net.m21xx.finance.stocks.report.model.Summary;
import net.m21xx.finance.stocks.report.repository.SummariesRepository;

@Slf4j
public class StockPricesJob extends QuartzJobBean {

	@Autowired
	private SummariesRepository summariesRepo;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info(String.format("Job %s ran.", getClass().getName()));
		
		List<Summary> lst = summariesRepo.getActiveStocksSummaries();
		log.info(String.format("%d papéis ativos na carteira", lst.size()));
	}

}
