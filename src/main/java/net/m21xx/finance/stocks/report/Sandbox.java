package net.m21xx.finance.stocks.report;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Sandbox {
	
	public static void main(String[] args) {
		regex();
		
		crawler();
	}

	private static void regex() {
		Pattern pat = Pattern.compile("[^0-9]*([0-9]+)\\.([0-9]+)[^0-9]*");
		Matcher mat = pat.matcher("R$ 69.00");
		System.out.println(mat.replaceAll("$1,$2")); // IndexOutOfBoundsException
	}
	
	private static void crawler() {
		Document doc;
		Elements elems;
		Element elem;
		
		try {
			doc = Jsoup.connect("https://www.meusdividendos.com/acao/MGLU3").get();
			System.out.println(String.format("Title: %s", doc.title()));
			elems = doc.select("div.wrapper section.content div.box-profile p b"); // NullPointerException
			elem = elems.first();
			System.out.println(String.format("Value is = %s", elem.text()));
			
			doc = Jsoup.connect("https://www.guiainvest.com.br/raiox/default.aspx?sigla=MGLU3").get();
			System.out.println(String.format("Title: %s", doc.title()));
			elems = doc.select("div#areaConteudo div#barraHeader div#divPerfilResumo li#liCotacao span"); // NullPointerException
			elem = elems.first();
			System.out.println(String.format("Value is = %s", elem.text()));
			
			doc = Jsoup.connect("https://app.tororadar.com.br/analise/mglu3").get();
			System.out.println(String.format("Title: %s", doc.title()));
			elems = doc.select("div.content-wrapper div.an-content div.analise-summary div.analise-summary-info div.oscilation"); // NullPointerException
			elem = elems.first();
			System.out.println(String.format("Value is = %s", elem.text()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
