package com.adm.database.daos;

import com.adm.domain.Artikel;
import java.util.List;

import javax.persistence.Query;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Profile({"persistence", "production" })
public class ArtikelDAO extends GenericDAOImpl<Artikel, Long>{

	/*
	 * In principe is onderstaande constructor voldoende om de generiekeDAO te implementeren. 
	 * Methodes kunnen toegevoegd of overschreven worden voor verdere implementatie.
	 */

	/**
	 *
	 */
	private static final long serialVersionUID = -3307130286758868777L;

	// Constructor geeft automatisch de entityClass (Artikel.class) door aan GenericDAOImpl
	protected ArtikelDAO() {
		super(Artikel.class);
	}

	public List<Artikel> findByType(String type) {
		Query q = entityManager.createQuery("from Artikel where artikelType like '" + type + "'");
		@SuppressWarnings("unchecked")
		List<Artikel> artikelen = q.getResultList();

		return artikelen;
	}


}