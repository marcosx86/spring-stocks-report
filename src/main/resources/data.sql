--DELETE FROM orders;
--DELETE FROM summaries;
--DELETE FROM taxes;

INSERT INTO public.scheduler_job_info (cron_expression, job_class, job_group, job_name, cron_job, repeat_time)
VALUES ( '0 */5 * ? * *', 'net.m21xx.finance.stocks.report.quartz.jobs.StockPricesJob', 'GroupCron', 'StockPricesJob', true, NULL);

--INSERT INTO `quartz_demo_db`.`scheduler_job_info` (`cron_expression`, `job_class`, `job_group`, `job_name`, `cron_job`, `repeat_time`)
--VALUES ( NULL, 'com.helixz.quartz.demo.jobs.SimpleJob', 'Test_Job', 'Simple Job', '\0', '600000');

INSERT INTO PUBLIC.CRAWLER_CONFIG (ID, URL, SELECTOR, REGEX_MATCHER, REGEX_REPLACE, LAST_SUCCESS, ENABLED)
VALUES ( seq_crawler_config.nextval, 'https://www.meusdividendos.com/acao/$$STOCK$$', 'div.wrapper section.content div.box-profile p b', '[^0-9]*([0-9]+)\.([0-9]+)[^0-9]*', '$1.$2', today, true);

INSERT INTO PUBLIC.CRAWLER_CONFIG (ID, URL, SELECTOR, REGEX_MATCHER, REGEX_REPLACE, LAST_SUCCESS, ENABLED)
VALUES ( seq_crawler_config.nextval, 'https://www.guiainvest.com.br/raiox/default.aspx?sigla=$$STOCK$$', 'div#areaConteudo div#barraHeader div#divPerfilResumo li#liCotacao span', '[^0-9]*([0-9]+)\.([0-9]+)[^0-9]*', '$1.$2', today, true);

INSERT INTO PUBLIC.CRAWLER_CONFIG (ID, URL, SELECTOR, REGEX_MATCHER, REGEX_REPLACE, LAST_SUCCESS, ENABLED)
VALUES ( seq_crawler_config.nextval, 'https://app.tororadar.com.br/analise/', 'div.content-wrapper div.an-content div.analise-summary div.analise-summary-info div.oscilation', '[^0-9]*([0-9]+)\.([0-9]+)[^0-9]*', '$1.$2', today, true);
