package com.adm.database.daos;

import com.adm.domain.Klant;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;

@Repository
@Transactional
@Component
@Profile({"persistence", "production" })
public class KlantDAO extends GenericDAOImpl<Klant, Long> {

	/*
	 * In principe is onderstaande constructor voldoende om de generiekeDAO te implementeren. 
	 * Methodes kunnen toegevoegd of overschreven worden voor verdere implementatie.
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = -3729533840646851297L;

	// Constructor geeft automatisch de entityClass (Klant.class) door aan GenericDAOImpl
	protected KlantDAO() {
		super(Klant.class);
	}

	// Method to find the client according to the email
	public String findKlantId(String klantEmail) {
		Query q = entityManager.createQuery("SELECT id FROM Klant WHERE email = \'" + klantEmail + "\'");
		String klantId = q.getSingleResult().toString();
		return klantId;
	}
}
