package com.adm.database.daos;

import com.adm.domain.BestelArtikel;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Profile({"persistence", "production" })
public class BestelArtikelDAO extends GenericDAOImpl<BestelArtikel, Long>{
	/*
	 * In principe is onderstaande constructor voldoende om de generiekeDAO te implementeren. 
	 * Methodes kunnen toegevoegd of overschreven worden voor verdere implementatie.
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3683597458095833064L;

	// Constructor geeft automatisch de entityClass (BestelArtikel.class) door aan GenericDAOImpl
	protected BestelArtikelDAO() {
		super(BestelArtikel.class);
	}

}
