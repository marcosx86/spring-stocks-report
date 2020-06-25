package net.m21xx.finance.stocks.report.repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import net.m21xx.finance.stocks.report.model.Stock;
import net.m21xx.finance.stocks.report.repository.generics.AbstractRepository;

public class StocksRepository extends AbstractRepository<Stock, Integer> {

	public StocksRepository() {
		setClazz(Stock.class);
	}
	
	public Stock findByStock(String stock) {
		StringBuilder sb = new StringBuilder();
		sb.append(" from ");
		sb.append(getClazz().getName());
		sb.append(" where stock = :stock");
		
		Query qry = getEntityManager().createQuery(sb.toString(), Stock.class);
		qry.setParameter("stock", stock);
		qry.setMaxResults(1);
		
		Stock res; 
		try {
			res = (Stock) qry.getSingleResult();
		}
		catch (NoResultException ex) {
			res = null;
		}
		
		return res;
	}
	
}
