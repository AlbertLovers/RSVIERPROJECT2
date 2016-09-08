package com.adm.web.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.adm.database.daos.ArtikelDAO;
import com.adm.database.daos.BestellingDAO;
import com.adm.database.service.ArtikelService;
import com.adm.database.service.BestellingService;
import com.adm.domain.Artikel;
import com.adm.domain.BestelArtikel;
import com.adm.domain.Bestelling;
import com.adm.domain.Betaalwijze;
import com.adm.domain.Betaling;
import com.adm.domain.Factuur;
import com.adm.domain.Klant;
import com.adm.domain.Prijs;
import com.adm.domain.ShoppingCart;

@Controller
@Component
@Transactional
@SessionAttributes({ "klant", "shoppingCart", "nieuweBestelling"})
public class BestellingController {

	private BestellingDAO bestellingDAO;
	private ArtikelDAO artikelDAO;


	@Autowired(required=true)
	@Qualifier(value="bestellingService")
	public void setBestellingService(BestellingService bs){
		bestellingDAO = bs.getBestellingDAO();
	}

	@Autowired(required=true)
	@Qualifier(value="artikelService")
	public void setArtikelDAO(ArtikelService as){
		artikelDAO = as.getArtikelDAO();
	}


	/*
	 * Bestelling maken
	 *
	 */

	@RequestMapping(value = "/bestelling", method = RequestMethod.GET)
	public String listBestelling(Model model, Klant klant, HashSet<BestelArtikel> winkelwagen) {

		List<Bestelling> bestellingen = bestellingDAO.findBestellingByKlantId(klant.getId());
		model.addAttribute("bestellingList", bestellingen);
		return "bestelling/bestellingLijst";
	}

	@RequestMapping(value = "/bestelling/catalogus", method = RequestMethod.GET)
	public String viewCatalogus(Model model) {

		//Zet een List<BestelArtikel> in het model
		model.addAttribute("artikelen", setCatalogusPrijs(artikelDAO.findAll()));

		return "bestelling/catalogus";
	}

	/*
	 *
	 * Individuele bestellingen
	 *
	 */
	@RequestMapping(value = "/bestelling/select/{bestelId}", method = RequestMethod.GET)
	public String listBestelling(@PathVariable("bestelId") long bestelId, Model model, Klant klant) {

		Bestelling bestelling = bestellingDAO.findById(bestelId);
		if(klant.getId() != bestelling.getKlant().getId()){
			return "home";
		}
		List<BestelArtikel> bestelArtikelen = bestellingDAO.findBestellingArtikelen(bestelId);



		BigDecimal totaal = totaalPrijsBestelling(bestelArtikelen.iterator());


		Factuur factuur = bestelling.getFactuurSet().iterator().next();
		Betaling betaling = factuur.getBetalingSet().iterator().next();

		model.addAttribute("bestelArtikelen", bestelArtikelen);
		model.addAttribute("factuur", factuur);
		model.addAttribute("betalingswijze", betaling.getBetaalwijze().getBetaalWijze());
		model.addAttribute("totaalPrijsExBtw", totaal);
		model.addAttribute("totaalPrijsIncBtw", totaal.multiply(new BigDecimal("1.21")));

		return "bestelling/factuur";
	}

	@RequestMapping(value = "/bestelling/bevestigen", method = RequestMethod.GET)
	public String kiesBetaalmethode(Model model, HttpSession session, ShoppingCart winkelwagen, Klant klant) {

		Bestelling bestelling = new Bestelling();
		bestelling.setBestelNummer(Calendar.getInstance().get(Calendar.YEAR) + "-" + bestellingDAO.getCount());
		bestelling.setKlant(klant);
		bestelling.setDatumAanmaak(new Date().toString());
		bestelling.setFactuurSet(new LinkedHashSet<Factuur>());

		ArrayList<BestelArtikel> list = new ArrayList<BestelArtikel>();
		list.addAll(winkelwagen.getWinkelwagen());


		Betaling betaling = new Betaling();
		betaling.setBetaalwijze(new Betaalwijze());

		model.addAttribute("nieuweBestelling", bestelling);
		model.addAttribute("artikelLijst", list);
		model.addAttribute("betaling", betaling);
		model.addAttribute("totaalPrijs", totaalPrijsBestelling(winkelwagen.getWinkelwagen().iterator()));

		return "bestelling/betaling/betaling";
	}

	/* Bestelling naar database schrijven */
	@RequestMapping(value = "/bestelling/bevestigen", method = RequestMethod.POST)
	public String bestellingGeplaatst(Model model, Betaling betaling, HttpSession session, ShoppingCart winkelwagen,
			Klant klant) {

		Bestelling nieuweBestelling = (Bestelling)session.getAttribute("nieuweBestelling");

		nieuweBestelling = bestellingDAO.makePersistent(nieuweBestelling);



		nieuweBestelling.setBestelArtikelSet(winkelwagen.getWinkelwagen());

		Iterator<BestelArtikel> it = winkelwagen.getWinkelwagen().iterator();

		while(it.hasNext()){
			it.next().setBestelling(nieuweBestelling);
		}

		nieuweBestelling.setBestelArtikelSet(winkelwagen.getWinkelwagen());

		Factuur fact = maakFactuur(nieuweBestelling);


		System.out.println("Value: " + betaling.getBetaalwijze().getValue());

		betaling.setBetaalDatum(new java.sql.Date(System.currentTimeMillis()));
		betaling.getBetaalwijze().setBetaalWijzeType();
		betaling.setFactuur(fact);
		betaling.setKlant(nieuweBestelling.getKlant());

		fact.voegBetalingToe(betaling);

		nieuweBestelling.getFactuurSet().add(fact);

		// Clear shopping cart
		winkelwagen.setWinkelwagen(new HashSet<BestelArtikel>());
		return "bestelling/betaling/betaald";
	}



	/* Extra methoden voor verwerken data */


	/*
	 *
	 * Bouw factuur
	 * Factuurnummer is bestelnummer met het aantal facturen in de bestelling + 1
	 *
	 */
	private Factuur maakFactuur(Bestelling bestelling){

		Factuur factuur = new Factuur();

		factuur.setBestelling(bestelling);
		factuur.setFactureringsDatum(new java.sql.Date(System.currentTimeMillis()));
		factuur.setFactuurNummer(bestelling.getBestelNummer() + "-" + (bestelling.getFactuurSet().size() + 1));

		return factuur;
	}


	/*
	 * Rekent de totaalprijs van de 
	 * bestelling uit 
	 * 
	 */

	private BigDecimal totaalPrijsBestelling(Iterator<BestelArtikel> bestelArtikelen){

		BigDecimal totaal = new BigDecimal("0");

		while(bestelArtikelen.hasNext()){

			BestelArtikel BA = bestelArtikelen.next();
			BigDecimal prijs = BA.getPrijs().getPrijs();
			BigDecimal aantal = new BigDecimal(BA.getAantal());
			totaal = totaal.add( prijs.multiply(aantal) );

		}

		return totaal;
	}

	/*
	 * Loopt alle artikelen door om
	 * de laatste prijsnotatie in de
	 * artikelPrijs te zetten voor
	 * de catalogus weergave
	 * 
	 */
	private List<BestelArtikel> setCatalogusPrijs(List<Artikel> artikelen){
		List<BestelArtikel> artikelenLijst = new ArrayList<BestelArtikel>();
		Iterator<Artikel> it = artikelen.iterator();
		Artikel art;
		Prijs pr = new Prijs();
		while(it.hasNext()){

			art = it.next();
			Iterator<Prijs> prijsIt = art.getPrijzen().iterator();

			while(prijsIt.hasNext())
				pr = prijsIt.next();
			art.setArtikelPrijs(pr.getPrijs());
			artikelenLijst.add(new BestelArtikel(pr, art, 0));
		}
		return artikelenLijst;
	}
}