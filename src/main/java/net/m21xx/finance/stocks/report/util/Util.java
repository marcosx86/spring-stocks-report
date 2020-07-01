package net.m21xx.finance.stocks.report.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

	private static final String PATTERN_STOCK_SYMBOL = "([A-Za-z0-9]{4}[0-9]+)(F)?";

	public static boolean isNullOrEmpty(String stock) {
		return stock == null || "".equals(stock);
	}

	public static boolean isNullOrEmptyOrBlank(String stock) {
		return isNullOrEmpty(stock) || "".equals(stock.trim());
	}

	public static String parseStockSymbol(String symbol) {
		StringBuilder sb = new StringBuilder();
		
		Pattern pat = Pattern.compile(PATTERN_STOCK_SYMBOL);
		Matcher match = pat.matcher(symbol.trim());
		if (match.matches() && match.groupCount() > 1) {
			sb.append(match.group(1));
		}
		
		return sb.toString();
	}
	
	public static Date convertStringToDate(String pDateString, DateTimeFormatter dmyFmt) {
		LocalDate orderDate = LocalDate.parse(pDateString.trim(), dmyFmt);
		Date dt = (Date.from(orderDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		
		return dt;
	}

}
