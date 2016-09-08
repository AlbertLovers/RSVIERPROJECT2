package com.adm.database.daos;

import com.adm.domain.Factuur;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Profile({"persistence", "production" })
public class FactuurDAO extends GenericDAOImpl<Factuur, Long>	{
	/*
	 * In principe is onderstaande constructor voldoende om de generiekeDAO te implementeren. 
	 * Methodes kunnen toegevoegd of overschreven worden voor verdere implementatie.
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6398159887518006767L;

	// Constructor geeft automatisch de entityClass (Factuur.class) door aan GenericDAOImpl
	protected FactuurDAO() {
		super(Factuur.class);
	} 
}
