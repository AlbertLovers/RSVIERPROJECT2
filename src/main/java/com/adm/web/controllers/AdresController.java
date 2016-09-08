package com.adm.web.controllers;

import com.adm.database.daos.AdresDAO;
import com.adm.database.daos.KlantDAO;
import com.adm.database.service.AdresService;
import com.adm.database.service.KlantService;
import com.adm.domain.Adres;
import com.adm.domain.AdresType;
import com.adm.domain.Klant;
import com.adm.web.forms.AdresRegisterForm;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Milan_Verheij on 19-08-16.
 *
 * Address Controller
 */

@Controller
@Component
@RequestMapping("/klant/adres")
@Transactional
@SessionAttributes({"klant", "adres", "shoppingCart"})
public class AdresController {
	private AdresDAO adresDAO;
	private KlantDAO klantDAO;
	private MessageSource messageSource;

	@Autowired
	public AdresController(AdresService adresService, KlantService klantService, MessageSource messageSource) {
		this.adresDAO = adresService.getAdresDAO();
		this.klantDAO = klantService.getDAO();
		this.messageSource = messageSource;
	}

	/** REGISTER METHOD FOR ADDRESSES (GET) **/
	@RequestMapping(value = "/register", method = GET)
	public String showRegistrationForm(Model model, Klant klant) {

		// Add a registerform to the model including clientdata
		model.addAttribute("adresRegisterForm", new AdresRegisterForm());
		model.addAttribute("klant", klant);

		// Show the registerview
		return "klant/adres/adresRegister";
	}

	/** REGISTER METHOD FOR ADDRESSES (POST) **/ //TODO: Making known addresses addable
	@RequestMapping(value = "/register", method = POST)
	public String processRegistration(
			@Valid AdresRegisterForm adresRegisterForm,
			Errors errors,
			Klant klant)
					throws IOException {

		// If the user entered wrong data return to register form
		if (errors.hasErrors()) {
			return "klant/adres/adresRegister";
		}

		// Translate adressform into adress
		Adres nieuwAdres = adresRegisterForm.toAdres();

		// Persist the adress (date modified temp )
		String dateModified = klant.getDatumGewijzigd();
		nieuwAdres = adresDAO.makePersistent(nieuwAdres);
		klant.getAdresGegevens().put(nieuwAdres, nieuwAdres.getType());
		klant.setDatumGewijzigd(dateModified);

		// Persist the client
		klantDAO.makePersistent(klant);

		// Redirect to created profile
		return "redirect:/klant/profile";
	}

	/** TUMBLE STATUS METHOD **/
	@RequestMapping(value = "/tumble/{id}", method = GET)
	public String tumbleStatusAdres(@PathVariable long id, Klant klant, Model model, Adres adres) throws Exception {

		//Achtung, the id is the id of the value in the map in the client
		adres = adresDAO.findById(id);
		//        klant = klantDAO.findById(klant.getId());

		if (adres.getAdresActief().charAt(0) == '0')
			adres.setAdresActief("1");
		else
			adres.setAdresActief("0");

		adres.setDatumGewijzigd(new Date().toString());

		// Update status of address
		adresDAO.makePersistent(adres);

		// Update client info
		//        Hibernate.initialize(klant.getAdresGegevens());
		//        klant = klantDAO.makePersistent(klant);

		// Inject client into model for display
		//        model.addAttribute("klant", klant);

		// Redirect to created profile
		return "redirect:/klant/klanten";
	}

	/** MODIFY ADDRESS METHOD (GET) **/
	@RequestMapping(value = "/modify/{id}", method = GET)
	public String modifyClient(@PathVariable long id, Model model, Adres adres) throws Exception {
		// Find the persistent address in the databas
		adres = adresDAO.findById(id);

		// Fix language for adresType in address
		AdresType adresType = adres.getType();
		adres.setAdresTypeString(messageSource.getMessage(adresType.toString(), new Object[] {}, LocaleContextHolder.getLocale()));

		// Convert applicable address fields from address object to a adresRegisterForm
		AdresRegisterForm registerForm = new AdresRegisterForm(
				adres.getStraatnaam(),
				adres.getPostcode(),
				adres.getToevoeging(),
				String.valueOf(adres.getHuisnummer()),
				adres.getWoonplaats(),
				adres.getAdresTypeString(),
				adres.getAdresActief());

		// Add attributes, address has to be there to retain the data
		model.addAttribute("adresRegisterForm", registerForm);
		model.addAttribute("adres", adres);

		return "klant/adres/adresModify";
	}

	/** MODIFY ADDRESS METHOD (POST) **/
	@RequestMapping(value = "/modify/{id}", method = POST)
	public String processRegistration(
			@Valid AdresRegisterForm adresRegisterForm,
			Errors errors,
			Model model,
			Klant klant,
			Adres adres)
					throws Exception {

		if (errors.hasErrors()) {
			return "klant/adres/adresModify";
		}

		// Convert applicable adress data from form -> address
		adres.setStraatnaam(adresRegisterForm.getStraatnaam());
		adres.setHuisnummer(Integer.parseInt(adresRegisterForm.getHuisnummer()));
		adres.setToevoeging(adresRegisterForm.getToevoeging());
		adres.setPostcode(adresRegisterForm.getPostcode());
		adres.setWoonplaats(adresRegisterForm.getWoonplaats());

		// Save adres to repository
		adres.setDatumGewijzigd(new Date().toString());
		adres = adresDAO.makePersistent(adres);

		// Update client info
		Hibernate.initialize(klant.getAdresGegevens());
		klant.setDatumGewijzigd(new Date().toString());
		klant = klantDAO.makePersistent(klant);

		// Inject client into model for display
		model.addAttribute(klant);

		// Return to client profile
		return "redirect:/klant/profile";
	}

}
