package net.m21xx.finance.stocks.report.quartz;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulerRepository extends JpaRepository<SchedulerJobInfo, Long> {

}
