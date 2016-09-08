package com.adm.database.daos;

import com.adm.domain.BestelArtikel;
import com.adm.domain.Bestelling;

import java.util.List;

import javax.persistence.Query;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Component
@Profile({"persistence", "production" })
public class BestellingDAO extends GenericDAOImpl<Bestelling, Long>{
	/*
	 * In principe is onderstaande constructor voldoende om de generiekeDAO te implementeren. 
	 * Methodes kunnen toegevoegd of overschreven worden voor verdere implementatie.
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = -7768397408796295517L;

	// Constructor geeft automatisch de entityClass (Bestelling.class) door aan GenericDAOImpl
	protected BestellingDAO() {
		super(Bestelling.class);
	}

	@SuppressWarnings("unchecked")
	public List<Bestelling> findBestellingByKlantId(long id){
		Query q = entityManager.createQuery("from Bestelling where klant_id = " + id);
		List<Bestelling> bestellingen = q.getResultList();
		return bestellingen;
	}

	@SuppressWarnings("unchecked")
	public List<BestelArtikel> findBestellingArtikelen(long id) {
		Query q = entityManager.createQuery("from BestelArtikel where bestellingId = " + id);
		List<BestelArtikel> bestelArtikelen = q.getResultList();
		return bestelArtikelen;
	}
}