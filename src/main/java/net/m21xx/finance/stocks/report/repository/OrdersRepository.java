package net.m21xx.finance.stocks.report.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import net.m21xx.finance.stocks.report.enums.OrderType;
import net.m21xx.finance.stocks.report.model.Order;
import net.m21xx.finance.stocks.report.repository.generics.AbstractRepository;

@Repository
public class OrdersRepository extends AbstractRepository<Order, Integer> {

	public OrdersRepository() {
		setClazz(Order.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Date> findAllOrderDates() {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct date from ");
		sb.append(getClazz().getName());
		
		Query qry = getEntityManager().createQuery(sb.toString(), Date.class);
		
		return qry.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllStocksByDate(Date date) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct stock from ");
		sb.append(getClazz().getName());
		sb.append(" where date = :date");
		
		Query qry = getEntityManager().createQuery(sb.toString(), String.class);
		qry.setParameter("date", date);
		
		return qry.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Order> findAllOrders(Date date, String stock, OrderType orderType) {
		StringBuilder sb = new StringBuilder();
		sb.append(" from ");
		sb.append(getClazz().getName());
		sb.append(" where date = :date");
		sb.append(" and stock = :stock");
		sb.append(" and ordertype = :ordertype");
//		sb.append(" order by date ASC");
		
		Query qry = getEntityManager().createQuery(sb.toString(), Order.class);
		qry.setParameter("date", date);
		qry.setParameter("stock", stock);
		qry.setParameter("ordertype", orderType.getValue());
		
		return qry.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllStockSymbols() {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct stock from ");
		sb.append(getClazz().getName());
		
		Query qry = getEntityManager().createQuery(sb.toString(), String.class);
		
		return qry.getResultList();
	}

}
