package net.m21xx.finance.stocks.report.model.converter;

import javax.persistence.AttributeConverter;

import net.m21xx.finance.stocks.report.enums.OrderType;

//@Converter(autoApply = true)
public class OrderTypeAttributeConverter implements AttributeConverter<OrderType, String> {

	@Override
	public String convertToDatabaseColumn(OrderType attribute) {
		return attribute.getValue();
	}

	@Override
	public OrderType convertToEntityAttribute(String dbData) {
		return OrderType.fromString(dbData);
	}
	
}
