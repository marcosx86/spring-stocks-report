package net.m21xx.finance.stocks.report.quartz;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(catalog = "public", name = "scheduler_job_info")
public class SchedulerJobInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String jobName;

	private String jobGroup;

	private String jobClass;

	private String cronExpression;

	private Long repeatTime;

	private Boolean cronJob;

}
