package net.m21xx.finance.stocks.report.repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import net.m21xx.finance.stocks.report.model.CrawlerConfig;
import net.m21xx.finance.stocks.report.repository.generics.AbstractRepository;

@Repository
public class CrawlerConfigRepository extends AbstractRepository<CrawlerConfig, Integer> {

	public static final int CONFIG_SUCCESS_NEWER = 1;

	public CrawlerConfigRepository() {
		setClazz(CrawlerConfig.class);
	}

	public CrawlerConfig getConfigByDate(int configSuccessOrder) {
		StringBuilder sb = new StringBuilder();
		sb.append(" from ");
		sb.append(getClazz().getName());
		sb.append(" where enabled = :enabled");
		sb.append(" order by lastSuccess ");
		sb.append(configSuccessOrder == CONFIG_SUCCESS_NEWER ? "DESC" : "ASC");
		
		Query qry = getEntityManager().createQuery(sb.toString(), CrawlerConfig.class);
		qry.setParameter("enabled", true);
		qry.setMaxResults(1);
		
		CrawlerConfig res; 
		try {
			res = (CrawlerConfig) qry.getSingleResult();
		}
		catch (NoResultException ex) {
			res = null;
		}
		
		return res;
	}
	
}
