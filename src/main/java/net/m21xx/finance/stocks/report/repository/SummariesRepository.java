package net.m21xx.finance.stocks.report.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import net.m21xx.finance.stocks.report.model.Summary;
import net.m21xx.finance.stocks.report.repository.generics.AbstractRepository;

@Repository
public class SummariesRepository extends AbstractRepository<Summary, Integer> {

	public SummariesRepository() {
		setClazz(Summary.class);
	}

	public Summary getSummaryBeforeDate(Date date, String stock) {
		System.out.println(String.format("==> getSummaryBeforeDate(%s, %s)", date, stock));
		
		StringBuilder sb = new StringBuilder();
		sb.append(" from ");
		sb.append(getClazz().getName());
		sb.append(" where stock = :stock");
		sb.append(" and date <= :date");
		sb.append(" order by date DESC");
		
		Query qry = getEntityManager().createQuery(sb.toString(), Summary.class);
		qry.setParameter("stock", stock);
		qry.setParameter("date", date);
		qry.setMaxResults(1);
		
		Summary res; 
		try {
			res = (Summary) qry.getSingleResult();
		}
		catch (NoResultException ex) {
			res = null;
		}
		System.out.println(String.format("<== getSummaryBeforeDate = %s", res));
		
		return res;
	}

	@SuppressWarnings("unchecked")
	public List<Summary> getActiveStocksSummaries() {
		StringBuilder sb = new StringBuilder();
		sb.append(" from ");
		sb.append(getClazz().getName());
		sb.append(" as a where 1 not in (select 1 from ");
		sb.append(getClazz().getName());
		sb.append(" as b where b.stock = a.stock");
		sb.append(" and b.date > a.date)");
		sb.append(" and a.count > 0 ");
		
		Query qry = getEntityManager().createQuery(sb.toString(), Summary.class);
		
		return qry.getResultList();
	}

}
