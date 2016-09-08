package com.adm.domain;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Component
public class Adres implements Serializable {

	//Datafield

	/**
	 * 
	 */
	private static final long serialVersionUID = -3459574420668481569L;

	@Id
	@SequenceGenerator(name = "adresId", sequenceName = "zadres_sequence", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "adresId")
	private long id;

	@Column(nullable = false)
	private String straatnaam;

	@Column(nullable = false)
	private String postcode;

	@Column(nullable = false)
	private String toevoeging;

	@Column(nullable = false)
	private int huisnummer;

	@Column(nullable = false)
	private String woonplaats;

	@Column(nullable = false)
	private String datumAanmaak;

	@Column(nullable = false)
	private String datumGewijzigd;

	@Column(nullable = false)
	private String adresActief;

	@ManyToMany(mappedBy = "adresGegevens", cascade={CascadeType.MERGE, CascadeType.PERSIST})
	@Column(nullable = false)
	protected Set<Klant> klant = new LinkedHashSet<Klant>();

	@OneToOne(cascade = CascadeType.MERGE)
	protected AdresType type;

    // Translation AdresType -> Readable Multi Language String
    @Transient
    private String adresTypeString;


	//Constructors
	public Adres() {}

	// Constructor met basis gegevens
	public Adres(String straatnaam, String postcode, String toevoeging,
			int huisnummer, String woonplaats, AdresType adresType) {
		this.straatnaam = straatnaam;
		this.postcode = postcode;
		this.toevoeging = toevoeging;
		this.huisnummer = huisnummer;
		this.woonplaats = woonplaats;
		this.type = adresType;
	}

	// Constructor met basis gegevens en gegevens welke enkel bij tests worden gewijzigd maar wel van
	// belang zijn voor het opvragen van gegevens etc. in de DAO's
	public Adres(String straatnaam, String postcode, String toevoeging,
			int huisnummer, String woonplaats, AdresType adresType, String datumAanmaak,
			 String datumGewijzigd, String adresActief) {
		this.straatnaam = straatnaam;
		this.postcode = postcode;
		this.toevoeging = toevoeging;
		this.huisnummer = huisnummer;
		this.woonplaats = woonplaats;
		this.type = adresType;
		this.datumAanmaak = datumAanmaak;
		this.datumGewijzigd = datumGewijzigd;
		this.adresActief = adresActief;
	}

	//Getters and Setters
	public String getStraatnaam() {
		return straatnaam;
	}
	public String getPostcode() {
		return postcode;
	}
	public String getToevoeging() {
		return toevoeging;
	}
	public int getHuisnummer() {
		return huisnummer;
	}
	public String getWoonplaats() {
		return woonplaats;
	}
	public long getId() {
		return id;
	}
	public String getDatumAanmaak() {
		return datumAanmaak;
	}
	public String getDatumGewijzigd() {
		return datumGewijzigd;
	}
	public String getAdresActief() {
		return adresActief;
	}
	public Set<Klant> getKlant() {
		return klant;
	}
	public void setKlant(Set<Klant> klant) {
		this.klant = klant;
	}
	public void setStraatnaam(String straatnaam) {
		this.straatnaam = straatnaam;
	}
	public void voegKlantToe(Klant klant){
		this.klant.add(klant);
	}
	public void haalKlantWeg(Klant klant){
		if(this.klant.contains(klant))
			this.klant.remove(klant);
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public void setToevoeging(String toevoeging) {
		this.toevoeging = toevoeging;
	}
	public void setHuisnummer(int huisnummer) {
		this.huisnummer = huisnummer;
	}
	public void setWoonplaats(String woonplaats) {
		this.woonplaats = woonplaats;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setDatumAanmaak(String datumAanmaak) {
		this.datumAanmaak = datumAanmaak;
	}
	public void setDatumGewijzigd(String datumGewijzigd) {
		this.datumGewijzigd = datumGewijzigd;
	}
	public void setAdresActief(String adresActief) {
		this.adresActief = adresActief;
	}
	public AdresType getType() {
		return type;
	}
	public void setType(AdresType type) {
		this.type = type;
	}
    public String getAdresTypeString() {
        return adresTypeString;
    }
    public void setAdresTypeString(String adresTypeString) {
        this.adresTypeString = adresTypeString;
    }

    @Override
	public String toString() {
		return "[" +
				straatnaam + ", " +
				postcode + ", " +
				toevoeging + ", " +
				huisnummer + ", " +
				woonplaats + "]";
	}

	/**
	 * Een adres-object wordt geacht gelijk te zijn als zowel de postcode, huisnummer
	 * en de toevoeging overeen komen.
	 *
	 * @param obj Een adres-object om mee te vergelijken.
	 * @return Een waarde true of false.
	 */
	@Override
	public boolean equals(Object obj) {
		if (postcode.equals(((Adres)obj).getPostcode()) &&
				huisnummer == ((Adres)obj).getHuisnummer() &&
				toevoeging.equals(((Adres)obj).getToevoeging()))
			return true;
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(13, 37).toHashCode();
	}
	//TODO: Unieker maken
}