package com.adm.web.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;


import com.adm.domain.*;
import com.adm.exceptions.NotLoggedInException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@Component
@Transactional
@SessionAttributes({ "klant", "shoppingCart", "artikel"})
public class WinkelwagenController {

	/** METHOD TO ADD AN PRODUCT TO THE SHOPPING CART **/
	@RequestMapping(value = "/bestelling/{bestellingId}", method = RequestMethod.POST)
	public String addArticleShoppingCart(Model model, Artikel artikel,
										 int aantal, ShoppingCart shoppingCart, Klant klant) throws Exception {
	    if (klant.getAchternaam() == null) {
	        throw new NotLoggedInException();
        }

		Iterator<Prijs> itPrijs = artikel.getPrijzen().iterator();
		Prijs p = null;
		while(itPrijs.hasNext()){
			p = itPrijs.next();
		}

		BestelArtikel bestArt = new BestelArtikel(p, artikel, aantal);

		shoppingCart.getWinkelwagen().add(bestArt);

		model.addAttribute("shoppingCart", shoppingCart);

		return "redirect:/bestelling/winkelwagen";
	}

	/** METHOD FOR SHOWING THE SHOPPING CART **/
	@RequestMapping(value = "/bestelling/winkelwagen", method = RequestMethod.GET)
	public String winkelwagen(Model model, ShoppingCart shoppingCart, Klant klant) {

		ArrayList<BestelArtikel> list = new ArrayList<BestelArtikel>();
		list.addAll(shoppingCart.getWinkelwagen());

		model.addAttribute("artikelen", list);
		model.addAttribute("totaalPrijs", totaalPrijsBestelling(shoppingCart.getWinkelwagen().iterator()));
		model.addAttribute("winkelwagen", shoppingCart.getWinkelwagen());
		
		return "bestelling/winkelwagen/winkelwagen";
	}

	/** METHOD FOR UPDATING THE SHOPPING CART **/
	@RequestMapping(value = "/bestelling/winkelwagen", method = RequestMethod.POST)
	public String editWinkelwagen(long artikelId, Model model, Integer aantal, Integer navbar, ShoppingCart shoppingCart,
                                  HttpServletRequest request, Klant klant) {
		
		Iterator<BestelArtikel> bestellingIterator = shoppingCart.getWinkelwagen().iterator();


		BestelArtikel bestArt = new BestelArtikel();
		while(bestellingIterator.hasNext()){
			bestArt = bestellingIterator.next();

			if(bestArt.getArtikel().getArtikelId() == artikelId){
				break;
			}

		}
		if(aantal > 0)
			bestArt.setAantal(aantal);
		else
			shoppingCart.getWinkelwagen().remove(bestArt);


		ArrayList<BestelArtikel> list = new ArrayList<BestelArtikel>();
		list.addAll(shoppingCart.getWinkelwagen());

		model.addAttribute("artikelen", list);
		model.addAttribute("totaalPrijs", totaalPrijsBestelling(shoppingCart.getWinkelwagen().iterator()));
		model.addAttribute("winkelwagen", shoppingCart.getWinkelwagen());
		model.addAttribute("betaalWijze", new Betaalwijze());


        // Checks if an item has been updated from the navbar. If so it will return to the last visited page
        // (aka previous page).
        if (navbar == 1) {
            return getPreviousPageByRequest(request).orElse("/"); //else go to home page
        }
        
		return "bestelling/betaling/betaling";
	}


	/* Extra methoden voor verwerken data */

	/*
	 * Rekent de totaalprijs van de 
	 * bestelling uit 
	 * 
	 */

	private BigDecimal totaalPrijsBestelling(Iterator<BestelArtikel> bestelArtikelen) {

        BigDecimal totaal = new BigDecimal("0");

        while (bestelArtikelen.hasNext()) {

            BestelArtikel BA = bestelArtikelen.next();
            BigDecimal prijs = BA.getPrijs().getPrijs();
            BigDecimal aantal = new BigDecimal(BA.getAantal());
            totaal = totaal.add(prijs.multiply(aantal));

        }

        return totaal;
    }

    /**
     * Returns the viewName to return for coming back to the sender url
     *
     * @param request Instance of {@link HttpServletRequest} or use an injected instance
     * @return Optional with the view name. Recomended to use an alternativa url with
     * {@link Optional#orElse(java.lang.Object)}
     */
    protected Optional<String> getPreviousPageByRequest(HttpServletRequest request)
    {
        return Optional.ofNullable(request.getHeader("Referer")).map(requestUrl -> "redirect:" + requestUrl);
    }
}