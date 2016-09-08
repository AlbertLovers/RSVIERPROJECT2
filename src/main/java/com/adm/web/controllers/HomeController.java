package com.adm.web.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import com.adm.database.daos.KlantDAO;
import com.adm.database.service.KlantService;
import com.adm.domain.Klant;
import com.adm.domain.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by Milan_Verheij on 15-08-16.
 *
 * Home controller
 *
 * Voegt vanaf begin af aan een Klant-object toe aan het model, als het goed is
 * is die klant een global session klant.
 *
 */

@SuppressWarnings("unused")
@Controller
@Component
@RequestMapping({"/", "/homepage", "/index"})
@SessionAttributes({"klant", "winkelwagen", "shoppingCart"})
public class HomeController {

    private Klant klant;
	private ShoppingCart shoppingCart;
    private KlantDAO klantDAO;

    @Autowired
    public HomeController(KlantService klantService) {
        this.klantDAO = klantService.getDAO();
    }

    @RequestMapping(method=GET)
    public String home(Model model, Klant klant, ShoppingCart shoppingCart) {

    	model.addAttribute("klant", klant);
        model.addAttribute("shoppingCart", shoppingCart);

        return "home";
    }

    @RequestMapping(value = "/about", method = GET)
    public String about(Model model, Klant klant, ShoppingCart shoppingCart) {
        model.addAttribute("klant", klant);
        model.addAttribute("shoppingCart", shoppingCart);

        return "about";
    }

    @RequestMapping(value = "/terms", method = GET)
    public String terms(Model model, Klant klant, ShoppingCart shoppingCart) {
        model.addAttribute("klant", klant);
        model.addAttribute("shoppingCart", shoppingCart);

        return "terms";
    }

    @RequestMapping(value = "/contact", method = GET)
    public String contact(Model model, Klant klant, ShoppingCart shoppingCart) {
        model.addAttribute("klant", klant);
        model.addAttribute("shoppingCart", shoppingCart);

        return "contact";
    }

    @RequestMapping(value="/logout", method = GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response, Model model, Klant klant) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        // Reset client when logging out
        klant = new Klant();
        model.addAttribute(klant);

        return "redirect:/";
    }

    @RequestMapping(value = "/loginSuccess", method = GET)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public String loginSuccess(Model model, ShoppingCart shoppingCart, Klant klant) {

        if (klant.getAchternaam() == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String klantEmail = auth.getName();

            long id = Long.parseLong(klantDAO.findKlantId(klantEmail));

            // Find the persisting client
            klant = klantDAO.findById(id);

            // Add the client to the model
            model.addAttribute(klant);
        }

        model.addAttribute("shoppingCart", shoppingCart);

        return "home";
    }

}
