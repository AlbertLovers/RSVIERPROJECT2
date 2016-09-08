package com.adm.database.daos;

import com.adm.domain.Prijs;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Profile({"persistence", "production" })
public class PrijsDAO extends GenericDAOImpl<Prijs, Long>	 {
	/*
	 * In principe is onderstaande constructor voldoende om de generiekeDAO te implementeren. 
	 * Methodes kunnen toegevoegd of overschreven worden voor verdere implementatie.
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4940588918388930738L;

	// Constructor geeft automatisch de entityClass (Artikel.class) door aan GenericDAOImpl
	protected PrijsDAO() {
		super(Prijs.class);
	}

}
