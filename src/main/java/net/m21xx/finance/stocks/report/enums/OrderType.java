package net.m21xx.finance.stocks.report.enums;

import lombok.Getter;

public enum OrderType {
	
	COMPRA("C"), VENDA("V");

	@Getter
	private String value;
	
	OrderType(String value) {
		this.value = value;
	}
	
	public static OrderType fromString(String pString) {
		for (OrderType b : OrderType.values()) {
			if (b.value.equalsIgnoreCase(pString)) {
				return b;
			}
		}
		return null;
	}
	
}