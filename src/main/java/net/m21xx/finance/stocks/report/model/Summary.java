package net.m21xx.finance.stocks.report.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter @RequiredArgsConstructor @EqualsAndHashCode @AllArgsConstructor
@Entity @Table(name = Summary.TABLE_NAME)
@SequenceGenerator(name = "seq_summaries", sequenceName = "seqSummaries")
public class Summary implements BaseModel<Integer> {

	public static final String TABLE_NAME = "summaries";
	
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
	
	@Column(name = "avgprice", nullable = false)
	private BigDecimal averagePrice;
	
	@Column(name = "daytrade", nullable = false)
	private boolean dayTrade;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		sb.append(String.format("ID = %d", id == null ? 0 : id));
		
		Optional.of(date).ifPresent(val -> sb.append(String.format(", date = %s", val)));
		Optional.of(stock).ifPresent(val -> sb.append(String.format(", stock = %s", val)));
		Optional.of(count).ifPresent(val -> sb.append(String.format(", count = %s", val)));
		Optional.of(averagePrice).ifPresent(val -> sb.append(String.format(", averagePrice = %s", val)));
		Optional.of(dayTrade).ifPresent(val -> sb.append(String.format(", dayTrade = %s", val)));
		
		sb.append("]");
		
		return sb.toString();
	}

}
