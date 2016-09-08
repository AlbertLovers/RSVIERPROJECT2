package com.adm.database.daos;

import com.adm.domain.Betaalwijze;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Profile({"persistence", "production" })
public class BetaalwijzeDAO extends GenericDAOImpl<Betaalwijze, Long>{
	/*
	 * In principe is onderstaande constructor voldoende om de generiekeDAO te implementeren. 
	 * Methodes kunnen toegevoegd of overschreven worden voor verdere implementatie.
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3732709754533473393L;

	// Constructor geeft automatisch de entityClass (Betaalwijze.class) door aan GenericDAOImpl
	protected BetaalwijzeDAO() {
		super(Betaalwijze.class);
	}

}
