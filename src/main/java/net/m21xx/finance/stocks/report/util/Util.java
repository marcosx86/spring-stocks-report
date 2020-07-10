package net.m21xx.finance.stocks.report.util;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

	private static final String PATTERN_STOCK_SYMBOL = "([A-Za-z0-9]{4}[0-9]+)(F)?( .*)?";
	private static final String PATTERN_INTER_NOTA_CORRETAGEM = ".*_NotaCor_([0-9]{2})([0-9]{2})([0-9]{4})_[0-9]+\\.xls";

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
		if (match.matches() && match.groupCount() == 3) {
			sb.append(match.group(1));
		}
		
		return sb.toString();
	}
	
	public static Date parseDateFromInterFilename(String filename) {
		Pattern pat = Pattern.compile(PATTERN_INTER_NOTA_CORRETAGEM);
		Matcher match = pat.matcher(filename.trim());
		if (match.matches() && match.groupCount() == 3) {
			int y = Integer.parseInt(match.group(3));
			int m = Integer.parseInt(match.group(2));
			int d = Integer.parseInt(match.group(1));
			
			LocalDate dt = LocalDate.of(y, m, d);
			return Date.from(dt.atStartOfDay().atZone(ZoneOffset.systemDefault()).toInstant());
		}
		
		return null;
	}
	
	public static Date convertStringToDate(String pDateString, DateTimeFormatter dmyFmt) {
		LocalDate orderDate = LocalDate.parse(pDateString.trim(), dmyFmt);
		Date dt = (Date.from(orderDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		
		return dt;
	}

	public static List<String> searchForFilenames(String path, String pattern) {
		File file = new File(path);
		
		return searchForFilenames(file, pattern);
	}
	
	private static List<String> searchForFilenames(File directory, String pattern) {
		if (directory.isDirectory()) {
			List<String> files = new ArrayList<String>();
			boolean flagPattern = pattern != null && !"".equals(pattern);
			if (flagPattern) {
				pattern = pattern.toLowerCase();
			}
			
			String[] innerFiles = directory.list();
			for (String innerFile : innerFiles) {
				String fullInnerFile = directory.getAbsolutePath() + File.separator + innerFile;
				File file = new File(fullInnerFile);
				
				if (file.isDirectory()) {
					List<String> mostInnerFiles = searchForFilenames(file, pattern);
					files.addAll(mostInnerFiles);
				}
				else {
					boolean flagOk = !flagPattern || innerFile.toLowerCase().endsWith(pattern);
					
					if (flagOk) {
						files.add(fullInnerFile);
					}
				}
			}
			
			return files;
		}
		
		return null;
	}

}
