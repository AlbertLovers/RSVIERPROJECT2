package com.adm.database.daos;

import com.adm.domain.Adres;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Profile({"persistence", "production" })
public class AdresDAO extends GenericDAOImpl<Adres, Long>{
	/*
	 * In principe is onderstaande constructor voldoende om de generiekeDAO te implementeren. 
	 * Methodes kunnen toegevoegd of overschreven worden voor verdere implementatie.
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3248106029175492982L;

	// Constructor geeft automatisch de entityClass (Adres.class) door aan GenericDAOImpl
	protected AdresDAO() {
		super(Adres.class);
		// TODO Auto-generated constructor stub
	}

}
