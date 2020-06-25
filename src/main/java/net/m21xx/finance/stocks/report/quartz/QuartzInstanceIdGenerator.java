package net.m21xx.finance.stocks.report.quartz;

import java.util.UUID;

import org.quartz.SchedulerException;
import org.quartz.spi.InstanceIdGenerator;

public class QuartzInstanceIdGenerator implements InstanceIdGenerator {

	@Override
	public String generateInstanceId() throws SchedulerException {
		try {
			return UUID.randomUUID().toString();
		} catch (Exception ex) {
			throw new SchedulerException("Couldn't generate UUID!", ex);
		}
	}

}
