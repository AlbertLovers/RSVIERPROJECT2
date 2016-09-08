package com.adm.web.controllers;

import com.adm.database.daos.KlantDAO;
import com.adm.database.service.KlantService;
import com.adm.domain.Adres;
import com.adm.domain.AdresType;
import com.adm.domain.BestelArtikel;
import com.adm.domain.Klant;
import com.adm.web.forms.KlantRegisterForm;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Milan_Verheij on 15-08-16.
 *
 * Client controller
 *
 */

//TODO ClientController -> Exception handling in general

@SuppressWarnings("restriction")
@Controller
@Component
@RequestMapping("/klant")
@Transactional
@SessionAttributes({ "klant", "plaatje", "shoppingCart"})
public class KlantController {

    private KlantDAO klantDAO;
    private MessageSource messageSource;

        private static String pictureFolder = "C:/harrie/uploads/data/profilePictures/"; // Windows
//    private String pictureFolder = "/tmp/harrie/uploads/data/profilePictures/"; // Unix-Based

    @Autowired
    public KlantController(KlantService klantService, MessageSource messageSource) {
        this.klantDAO = klantService.getDAO();
        this.messageSource = messageSource;
    }

    /** CLIENT REGISTER METHOD (GET) **/
    @RequestMapping(value = "/register", method = GET)
    public String showRegistrationForm(Model model) {

        // Add registerform to model and sent to user
        model.addAttribute("klantRegisterForm", new KlantRegisterForm());

        // Go to register form
        return "klant/klantRegister";
    }

    /** CLIENT REGISTER METHOD (POST) **/
    @RequestMapping(value = "/register", method = POST)
    public String processRegistration(
            @Valid KlantRegisterForm klantRegisterForm,
            Errors errors,
            Model model)
            throws Exception {

        // If the form has error's in input return to the user
        if (errors.hasErrors()) {
            return "klant/klantRegister";
        }

        // Save client to repository
        Klant nieuweKlant = klantRegisterForm.toKlant();
        nieuweKlant = klantDAO.makePersistent(nieuweKlant);

        // Save profilePicture to a file
        saveProfilePicture(nieuweKlant.getId(), klantRegisterForm.getProfilePicture());

        // Add client to model
//        model.addAttribute("klant", nieuweKlant);

        // Return to client list
//        return showProfile(model, nieuweKlant);
        return "redirect:/login";
    }

    /** CLIENT PROFILE PAGE **/
    @RequestMapping(value = "/profile", method = GET)
    public String showProfile(Model model, Klant klant) throws Exception {

    	
        // Find client and set the profilePicture TODO: Prob wat dubbel her en der, streamlinen
        klant = klantDAO.findById(klant.getId());
        klant.setClientProfilePicture(getProfilePicture(klant.getId()));
        model.addAttribute(klant);

        // If there no active client yet, return to clientList
        if (klant.getEmail() == null) {
            return showClients(model);
        }

        // Initialize address data (lazy loading)
        Hibernate.initialize(klant.getAdresGegevens()); //TODO: Hibernate.initialize in client JPA-independent

        // Fix language settings for Adress Type
        Iterator<Adres> adresIterator = klant.getAdresGegevens().keySet().iterator();

        while (adresIterator.hasNext()) {
            Adres adres = adresIterator.next();
            AdresType adresType = adres.getType();
            adres.setAdresTypeString(messageSource.getMessage(adresType.toString(), new Object[]{}, LocaleContextHolder.getLocale()));
        }

        // Add Attributes in map
        model.addAttribute("adresMap", klant.getAdresGegevens());
        model.addAttribute("klant", klant);

        return "klant/klantProfile";
    }

    /** CLIENT LIST METHOD **/
    @RequestMapping(value = "/klanten", method = GET)
    @Secured({"ROLE_ADMIN"})
    public String showClients(Model model) throws Exception {
        List<Klant> klantenLijst = klantDAO.findAll();

        ArrayList<String> plaatjesList = new ArrayList<>();

        // Laad de profielpictures in
        for (int i = 0; i < klantenLijst.size(); i++) {
            byte[] array = Files.readAllBytes(new File(pictureFolder
                    + klantenLijst.get(i).getId() + ".jpg").toPath());

            plaatjesList.add((i), Base64.encode(array));
        }

        model.addAttribute("plaatjesList", plaatjesList);
        model.addAttribute("klantenList", klantenLijst);
        return "klant/klantenLijst";
    }

    /** TUMBLE CLIENT STATUS METHOD **/
    @RequestMapping(value = "/tumble/{id}", method = GET)
    @Secured({"ROLE_ADMIN"})
    public String tumbleStatusClient(@PathVariable long id,
                                     Model model,
                                     @RequestParam(value="fromProfile", defaultValue="0") int fromProfilePage
                                    ) throws Exception {
        // Find the persisting client
        Klant klant = klantDAO.findById(id);

        // Tumble status with respect to the current status
        if (klant.getKlantActief().charAt(0) == '0')
            klant.setKlantActief("1");
        else
            klant.setKlantActief("0");

        // Fill in the date modified
        klant.setDatumGewijzigd( new Date().toString());

        // Persist the new clientinfo
        klantDAO.makePersistent(klant);

        // If the direct is from the profile page, redirect to the profilepage instead of the client list.
        if (fromProfilePage == 1) {
            klant.setClientProfilePicture(getProfilePicture(klant.getId()));
            model.addAttribute(klant);
            return showProfile(model, klant);
        }

        // Return to the client list
        return showClients(model);
    }

    /** SELECT CLIENT METHOD **/
    @RequestMapping(value = "/select/{id}", method = GET)
    @Secured({"ROLE_ADMIN"})
    public String selectClient(@PathVariable long id, Model model) throws Exception {
        // Find the persisting client
        Klant klant = klantDAO.findById(id);

        // Read in an the correct profile picture and encode in Base64 and add to client
        klant.setClientProfilePicture(getProfilePicture(klant.getId()));

        // Add the client and picture attributes to the model
        model.addAttribute(klant);

        // Show the profile
        return showProfile(model, klant);
    }


    /** MODIFY CLIENT (GET) METHOD **/
    @RequestMapping(value = "/modify/{id}", method = GET)
    public String modifyClient(@PathVariable long id, Model model) throws Exception {
        // Find client by ID
        Klant klant = klantDAO.findById(id);
        klant.setClientProfilePicture(getProfilePicture(klant.getId()));

        //  Put clientdata in regfisterForm (which is error-checked)
        KlantRegisterForm registerForm = new KlantRegisterForm(
                klant.getVoornaam(),
                klant.getAchternaam(),
                klant.getTussenvoegsel(),
                klant.getEmail(),
                "",
                null);

        //  Add clientdata to model (klant has to be there to retain the 'other' fields')
        model.addAttribute("klant", klant);
        model.addAttribute("klantRegisterForm", registerForm);

        return "klant/klantModify";
    }

    /** MODIFY CLIENT (POST) METHOD **/
    @RequestMapping(value = "/modify/{id}", method = POST)
    public String processModification(
            @Valid KlantRegisterForm klantRegisterForm,
            Errors errors,
            HashSet<BestelArtikel> winkelwagen,
            Klant klant,
            Model model,
            int fromProfileView)
            throws Exception {

        // If there are errors in the input, redirect to the modify page
        if (errors.hasErrors()) {
            return "klant/klantModify";
        }

        // Set the modifed parameters in the existing client-object
        klant.setVoornaam(klantRegisterForm.getVoornaam());
        klant.setAchternaam(klantRegisterForm.getAchternaam());
        klant.setTussenvoegsel(klantRegisterForm.getTussenvoegsel());
        klant.setPassword(klantRegisterForm.getPassword());

        // Set new profile picture if a new one is uploaded
        if (klantRegisterForm.getProfilePicture() != null) {
            saveProfilePicture(klant.getId(), klantRegisterForm.getProfilePicture());
        }

        // Save client to repository
        klant.setDatumGewijzigd(new Date().toString());
        klant = klantDAO.makePersistent(klant);
        Hibernate.initialize(klant.getAdresGegevens());
        model.addAttribute(klant);

        // Redirect to the client list or profile view
        if (fromProfileView == 1) {
            klant.setClientProfilePicture(getProfilePicture(klant.getId()));
            model.addAttribute(klant);
            return showProfile(model, klant);
        }

        return showClients(model);
    }

    /** METHOD FOR GETTING PROFILE PICTURE FROM SERVER **/
    private static String getProfilePicture(long id) throws Exception {
        byte[] profilePictureArray = Files.readAllBytes(new File(pictureFolder
                + id + ".jpg").toPath());

        String profileString = Base64.encode(profilePictureArray);

        return profileString;
    }

    /** METHOD FOR SAVING PROFILE PICTURE TO SERVER **/
    private static void saveProfilePicture(long id, MultipartFile profilePicture) throws Exception {
        // Save profilePicture to a file
        String profielFotoNaam = profilePicture.getOriginalFilename();
        profilePicture.transferTo(new File("/data/profilePictures/"
                + id + "." + (FilenameUtils.getExtension(profielFotoNaam))));
    }
}
