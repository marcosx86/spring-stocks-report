package net.m21xx.finance.stocks.report.service;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.m21xx.finance.stocks.report.model.CrawlerConfig;
import net.m21xx.finance.stocks.report.repository.CrawlerConfigRepository;
import net.m21xx.finance.stocks.report.util.Util;

@Service
@Slf4j
public class CrawlerService {

	private static final String PATTERN_STOCK = "$$STOCK$$";
	
	@Autowired
	private CrawlerConfigRepository repository;

	public CrawlerConfig getLastSucceededConfig() {
		return repository.getConfigByDate(CrawlerConfigRepository.CONFIG_SUCCESS_NEWER);
	}

	@Transactional
	public Double getStringByPattern(String strStock, CrawlerConfig config) {
		if (!Util.isNullOrEmptyOrBlank(strStock) && config != null) {
			
			Double ret;
			String aux = getStringFromSite(strStock, config);
			if (aux != null) {
				ret = extractValueFromHaystack(config, aux);
				
				config.setLastSuccess(new Date());
				repository.persistOrMerge(config);
			}
			else {
				ret = null;
			}
			
			return ret;
		}
		
		return null;
	}

	private String getStringFromSite(String stock, CrawlerConfig config) {
		if (Util.isNullOrEmptyOrBlank(config.getUrl())) {
			return null;
		}
		
		String strUrl = config.getUrl();
		if (!strUrl.contains(PATTERN_STOCK)) {
			return null;
		}
		
		strUrl = strUrl.replace(PATTERN_STOCK, stock);
		
		String ret;
		try {
			Document doc = Jsoup.connect(strUrl).get();
			Elements elems = doc.select(config.getSelector());
			Element elem = elems.first();
			ret = elem.text();
		} catch (NullPointerException e) {
			log.warn(String.format("Pattern not found on URL: %s (NullPointerException)", strUrl));
			ret = null;
		} catch (IOException e) {
			log.warn(String.format("Could not retrieve from URL: %s, caused by %s, message: %s", strUrl, e.getClass(),
					e.getMessage()));
			ret = null;
		}
		
		return ret;
	}
	
	private Double extractValueFromHaystack(CrawlerConfig config, String haystack) {
		Double dbl;
		String aux = null;
		try {
			Pattern pat = Pattern.compile(config.getRegexMatcher());
			Matcher mat = pat.matcher(haystack);
			aux = mat.replaceAll(config.getRegexReplace());
			dbl = Double.valueOf(aux);
		} catch (IndexOutOfBoundsException e) {
			log.warn(String.format(
					"Could not extract value from \"%s\", provided replace pattern string (%s) mismatch search pattern!",
					haystack, config.getRegexReplace()));
			dbl = null;
		} catch (NumberFormatException e) {
			log.warn(String.format("Could not convert value from \"%s\", please check patterns!", aux));
			dbl = null;
		}
		
		return dbl;
	}

}
