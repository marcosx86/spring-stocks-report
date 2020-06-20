package net.m21xx.finance.stocks.report.model;

import java.math.BigDecimal;
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
@Entity @Table(name = Tax.TABLE_NAME)
@SequenceGenerator(name = "seq_taxes", sequenceName = "seqTaxes")
public class Tax implements BaseModel<Integer> {

	public static final String TABLE_NAME = "taxes";
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "seqOrders")
	@Column(name = "id", nullable = false)	
	private Integer id;
	
	@Column(name = "stock", nullable = false)
	private String stock;
	
	@Column(name = "date", nullable = false)
	private Date date;
	
	@Column(name = "dueprice", nullable = false)
	private BigDecimal duePrice;
	
}
