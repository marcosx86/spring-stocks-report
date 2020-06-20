package net.m21xx.finance.stocks.report.repository;

import java.util.Date;

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

}
