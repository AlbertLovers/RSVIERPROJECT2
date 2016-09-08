package com.adm.database.daos;

import com.adm.domain.AdresType;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Profile({"persistence", "production" })
public class AdresTypeDAO extends GenericDAOImpl<AdresType, Long>{
	/*
	 * In principe is onderstaande constructor voldoende om de generiekeDAO te implementeren. 
	 * Methodes kunnen toegevoegd of overschreven worden voor verdere implementatie.
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4129604068893418239L;

	// Constructor geeft automatisch de entityClass (AdresType.class) door aan GenericDAOImpl
	protected AdresTypeDAO() {
		super(AdresType.class);
	}

}
