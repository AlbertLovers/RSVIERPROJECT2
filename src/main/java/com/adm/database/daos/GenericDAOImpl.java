package com.adm.database.daos;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

import com.adm.database.interfaces.GenericDAO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Profile({"persistence", "production"})
public abstract class GenericDAOImpl<E, ID extends Serializable> implements GenericDAO<E, ID> {
	
	/*
	 * Een generiekeDAO heeft een entitymanager en een entityclass nodig om te werken.
	 * De entityManager kan in JavaEE geleverd worden door een subklasse die de persistence
	 * context begrijpt. Anders kan setEntityManager() gebruikt worden.
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = -2574464009767283339L;

	@PersistenceContext
	protected EntityManager entityManager;
	
	private final Class<E> entityClass;

	protected GenericDAOImpl(Class<E> entityClass) {
		this.entityClass = entityClass;
	}
	
	public void setEntityManager(EntityManager em) {
		this.entityManager = em;
	}
	// join transaction
	@Override
	public void joinTransaction() {
		if (!entityManager.isJoinedToTransaction()) {
			entityManager.joinTransaction();
		}
	}
	// finder methods
	public E findById(ID id) {
		return findById(id, LockModeType.NONE);
	}
	public E findById(ID id, LockModeType lockModeType) {
		return entityManager.find(entityClass, id, lockModeType);
	}
	public E findReferenceById(ID id) {
		return entityManager.getReference(entityClass, id);
	}
	
	public List<E> findAll() {
		CriteriaQuery<E> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(entityClass);
		criteriaQuery.select(criteriaQuery.from(entityClass));
		return entityManager.createQuery(criteriaQuery).getResultList();
	}
	public Long getCount() {
		CriteriaQuery<Long> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(Long.class);
		criteriaQuery.select(entityManager.getCriteriaBuilder().count(criteriaQuery.from(entityClass)));
		return entityManager.createQuery(criteriaQuery).getSingleResult();
	}

	// state management operations
	public E makePersistent(E instance) {
		// merge() handles transient AND detached instances
		return entityManager.merge(instance);
	}

	public void makeTransient(E instance) {
		entityManager.remove(instance);
	}
	public void checkVersion(E entity, boolean forceUpdate) {
		entityManager.lock(
				entity, 
				forceUpdate
					? LockModeType.OPTIMISTIC_FORCE_INCREMENT
					: LockModeType.OPTIMISTIC
		);
	}
}
