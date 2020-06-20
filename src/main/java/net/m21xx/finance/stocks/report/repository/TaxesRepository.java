package net.m21xx.finance.stocks.report.repository;

import org.springframework.stereotype.Repository;

import net.m21xx.finance.stocks.report.model.Tax;
import net.m21xx.finance.stocks.report.repository.generics.AbstractRepository;

@Repository
public class TaxesRepository extends AbstractRepository<Tax, Integer> {
	
	public TaxesRepository() {
		setClazz(Tax.class);
	}

}
