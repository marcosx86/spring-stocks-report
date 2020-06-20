package net.m21xx.finance.stocks.report.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.m21xx.finance.stocks.report.enums.OrderType;
import net.m21xx.finance.stocks.report.model.converter.OrderTypeAttributeConverter;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = Order.TABLE_NAME)
@SequenceGenerator(name = "seq_orders", sequenceName = "seqOrders")
public class Order implements BaseModel<Integer> {

	public static final String TABLE_NAME = "orders";
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "seqOrders")
	@Column(name = "id", nullable = false)	
	private Integer id;
	
	@Column(name = "stock", nullable = false)
	private String stock;
	
	@Column(name = "date", nullable = false)
	private Date date;
	
	@Column(name = "count", nullable = false)
	private Integer count;
	
	@Column(name = "price", nullable = false)
	private BigDecimal price;
	
	@Column(name = "ordertype", nullable = false)
	//@Enumerated(EnumType.STRING)
	@Convert(converter = OrderTypeAttributeConverter.class)
	private OrderType orderType;

}
