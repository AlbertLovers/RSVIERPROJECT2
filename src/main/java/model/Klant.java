package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Created by Milan_Verheij on 06-06-16.
 * Updated by Milan Verheij on 20-06-16 (nieuw DB-model).
 *
 * Model van Klant. Variabelen komen overeen met de tabel KLANT
 * in de database.
 *
 */

@Entity(name = "klant")
public class Klant {

    @Id
    @GeneratedValue
    @Column(name = "klantId")
    private long klantId;
    
    
    @OneToMany(mappedBy = "klant", fetch = FetchType.LAZY)
    protected Set<Bestelling> bestelLijst = new HashSet<>();
    
    
    @Column(name = "voornaam")
    private String voornaam;

    @Column(name = "achternaam")
    private String achternaam;

    @Column(name = "tussenvoegsel")
    private String tussenvoegsel;

    @Column(name = "email")
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datumAanmaak", updatable = false, nullable = false)
	private Date datumAanmaak = new Date();

    @Column(name = "datumGewijzigd")
    private String datumGewijzigd;

    @Column(name = "klantActief")
    private String klantActief;

    private ArrayList<Adres> adresGegevens = new ArrayList<Adres>();
    private Bestelling bestellingGegevens;

    //  Default public no-arg constructor
    public Klant() {
    }

    // Standaard public constructor met basis parameters
    public Klant(long klantId,
                 String voornaam,
                 String achternaam,
                 String tussenvoegsel,
                 String email,
                 Adres adresGegevens) {

        if (adresGegevens != null && adresGegevens.getPostcode() != null){
        	this.adresGegevens.add(adresGegevens);
        }
        

        this.klantId = klantId;
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.tussenvoegsel = tussenvoegsel;
        this.email = email;
    }

    // Constructor voor alle variabelen, wordt over het algemeen gebruikt tijdens testwerkzaamheden
    // en bij het opvragen van gegevens via de DAO's
    public Klant(long klantId,
                 String voornaam,
                 String achternaam,
                 String tussenvoegsel,
                 String email,
                 Date datumAanmaak,
                 String datumGewijzigd,
                 String klantActief,
                 Adres adresGegevens,
                 Bestelling bestellingGegevens) {

    	if (adresGegevens != null && adresGegevens.getPostcode() != null){
    		this.adresGegevens.add(adresGegevens);
    	}

        this.klantId = klantId;
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.tussenvoegsel = tussenvoegsel;
        this.email = email;
        this.datumAanmaak = datumAanmaak;
        this.datumGewijzigd = datumGewijzigd;
        this.klantActief = klantActief;
        this.bestellingGegevens = bestellingGegevens;
    }

    // Getters & setters
    public long getKlantId() {
        return klantId;
    }
    public void setKlantId(long klantId) {
        this.klantId = klantId;
    }
    public String getVoornaam() {
        return voornaam;
    }
    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }
    public String getAchternaam() {
        return achternaam;
    }
    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }
    public String getTussenvoegsel() {
        return tussenvoegsel;
    }
    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public ArrayList<Adres> getAdresGegevens() {
        return adresGegevens;
    }
    public Date getDatumAanmaak() {
        return datumAanmaak;
    }
    public void setDatumAanmaak(Date datumAanmaak) {
        this.datumAanmaak = datumAanmaak;
    }
    public String getDatumGewijzigd() {
        return datumGewijzigd;
    }
    public void setDatumGewijzigd(String datumGewijzigd) {
        this.datumGewijzigd = datumGewijzigd;
    }
    public String getKlantActief() {
        return klantActief;
    }
    public void setKlantActief(String klantActief) {
        this.klantActief = klantActief;
    }
    public void setAdresGegevens(Adres adresGegevens) {
        if(adresGegevens == null)
        	this.adresGegevens = null;
        else if(!this.adresGegevens.contains(adresGegevens))
        	this.adresGegevens.add(adresGegevens);
    } // Enkel bij testen gebruikt
    
    public Bestelling getBestellingGegevens() {
        return bestellingGegevens;
    }
    public void setBestellingGegevens(Bestelling bestellingGegevens) {
        this.bestellingGegevens = bestellingGegevens;
    } // Enkel bij testen gebruikt

    // Overrided methoden van Object etc.

    @Override
    public String toString() {
        return "[" + klantId + ", " +
                     voornaam + ", " +
                     achternaam + ", " +
                     tussenvoegsel + ", " +
                     email + ", " +
                     "Adresgegevens aanwezig:" +
                     (adresGegevens != null ? " ja" : " nee") +
                    "]";
    }

    /**
     * Een klant-object wordt geacht gelijk te zijn als het klantId hetzelfde is.
     *
     * @param obj Klant-Object om mee te vergelijken.
     * @return Een waarde true of false als de klant gelijk is.
     */
    @Override
    public boolean equals(Object obj) {
        if (klantId == ((Klant)obj).getKlantId())
            return true;
        return false;
    }
}
