package net.m21xx.finance.stocks.report.repository.generics;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.repository.Repository;

import lombok.Getter;
import lombok.Setter;
import net.m21xx.finance.stocks.report.model.BaseModel;

public abstract class AbstractRepository<T extends BaseModel<ID>, ID> implements Repository<T, ID> {

	@Getter
	@Setter
	private Class<T> clazz;
	
	@Getter
	@Setter
	private String orderBy;
	
	@PersistenceContext
	@Getter
	private EntityManager entityManager;
	
	public Optional<T> findById(ID id) {
		return Optional.of(entityManager.find(clazz, id));
	}
	
	public List<T> findAll() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" from ");
		sb.append(clazz.getName());
		
		if (orderBy != null && !"".equals(orderBy.trim())) {
			sb.append(" order by ");
			sb.append(orderBy);
		}
		
		return entityManager.createQuery(sb.toString(), clazz).getResultList();
	}
	
	public void persist(T obj) {
		entityManager.persist(obj);
		entityManager.flush();
	}
	
	public T merge(T obj) {
		return entityManager.merge(obj);
	}
	
	public T persistOrMerge(T obj) {
		if (obj.getId() == null) {
			persist(obj);
			return obj;
		}
		else {
			return merge(obj);
		}
	}

	public void delete(T obj) {
		entityManager.remove(obj);
	}
	
	public void deleteById(ID id) {
		Optional<T> obj = findById(id);
		obj.ifPresent(o -> delete(o));
	}
	
	public void flush() {
		entityManager.flush();
	}
	
}
