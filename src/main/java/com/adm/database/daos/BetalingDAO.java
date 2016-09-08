package com.adm.database.daos;

import com.adm.domain.Betaling;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Profile({"persistence", "production" })
public class BetalingDAO extends GenericDAOImpl<Betaling, Long>{
	/*
	 * In principe is onderstaande constructor voldoende om de generiekeDAO te implementeren. 
	 * Methodes kunnen toegevoegd of overschreven worden voor verdere implementatie.
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2622959985715762705L;

	// Constructor geeft automatisch de entityClass (Betaling.class) door aan GenericDAOImpl
	protected BetalingDAO() {
		super(Betaling.class);
	}
	

}
