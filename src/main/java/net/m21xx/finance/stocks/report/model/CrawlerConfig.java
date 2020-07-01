package net.m21xx.finance.stocks.report.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = CrawlerConfig.TABLE_NAME)
@SequenceGenerator(name = "seq_crawler_config", sequenceName = "seqCrawlerConfig")
public class CrawlerConfig implements BaseModel<Integer> {

	public static final String TABLE_NAME = "crawler_config";
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "seqCrawlerConfig")
	@Column(name = "id", nullable = false)	
	private Integer id;
	
	@Column(name = "url", nullable = false)
	private String url;
	
	@Column(name = "selector", nullable = false)
	private String selector;
	
	@Column(name = "regex_matcher", nullable = false)
	private String regexMatcher;
	
	@Column(name = "regex_replace", nullable = false)
	private String regexReplace;
	
	@Column(name = "last_success", nullable = false)
	private Date lastSuccess;
	
	@Column(name = "enabled", nullable = false)
	private boolean enabled;
	
}
