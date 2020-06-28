DELETE FROM orders;
DELETE FROM summaries;
DELETE FROM taxes;

INSERT INTO public.scheduler_job_info (cron_expression, job_class, job_group, job_name, cron_job, repeat_time)
VALUES ( '0 */5 * ? * *', 'net.m21xx.finance.stocks.report.quartz.jobs.StockPricesJob', 'GroupCron', 'StockPricesJob', true, NULL);

--INSERT INTO `quartz_demo_db`.`scheduler_job_info` (`cron_expression`, `job_class`, `job_group`, `job_name`, `cron_job`, `repeat_time`)
--VALUES ( NULL, 'com.helixz.quartz.demo.jobs.SimpleJob', 'Test_Job', 'Simple Job', '\0', '600000');