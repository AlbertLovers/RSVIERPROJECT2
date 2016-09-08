package com.adm.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity
public class Bestelling implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7141412659590558982L;

	@Id
	@SequenceGenerator(name = "bestellingId", sequenceName = "zBestelling_sequence", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "bestellingId")
	private long id;

	@Column(nullable = false)
	private String bestelNummer;
	
	@Column(nullable = false)
	private boolean bestellingActief = true;

	@Column(nullable = false)
	private String datumAanmaak;
	
	@OneToMany(mappedBy = "bestelling", cascade = CascadeType.ALL)
	@Column
	private Set<Factuur> factuurSet = new HashSet<Factuur>();
	
	@ManyToOne(optional = false)
	private Klant klant;

	@OneToMany(mappedBy = "bestelling",
	        cascade = CascadeType.ALL)
	@Column(nullable = false)
	protected Set<BestelArtikel> bestelArtikelSet = new HashSet<BestelArtikel>();

	public Bestelling(){
		bestelArtikelSet = new HashSet<BestelArtikel>();
	}

	public Bestelling(long id, Klant klant, Set<BestelArtikel> bestelArtikelSet, String datumAanmaak){
		this.id = id;
		this.klant = klant;
		this.bestelArtikelSet = bestelArtikelSet;
		this.datumAanmaak = datumAanmaak;
		bestellingActief = true;
	}

	public Bestelling(long id, Klant klant, Set<BestelArtikel> bestelArtikelSet){
		this.id = id;
		this.klant = klant;
		this.bestelArtikelSet = bestelArtikelSet;
		bestellingActief = true;
	}

	
	public long getId() {
		return id;
	}

	public String getBestelNummer() {
		return bestelNummer;
	}

	public boolean isBestellingActief() {
		return bestellingActief;
	}

	public String getDatumAanmaak() {
		return datumAanmaak;
	}

	public Klant getKlant() {
		return klant;
	}

	public Set<BestelArtikel> getBestelArtikelSet() {
		return bestelArtikelSet;
	}

	public Set<Factuur> getFactuurSet() {
		return factuurSet;
	}

	public void setFactuurSet(Set<Factuur> factuurSet) {
		this.factuurSet = factuurSet;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setBestelNummer(String bestelNummer) {
		this.bestelNummer = bestelNummer;
	}

	public void setBestellingActief(boolean bestellingActief) {
		this.bestellingActief = bestellingActief;
	}

	public void setDatumAanmaak(String datumAanmaak) {
		this.datumAanmaak = datumAanmaak;
	}

	public void setKlant(Klant klant) {
		this.klant = klant;
	}

	public void setBestelArtikelSet(Set<BestelArtikel> bestelArtikelSet) {
		this.bestelArtikelSet = bestelArtikelSet;
	}

	public void voegArtikelToe(BestelArtikel bestelArtikel){
		if(bestelArtikelSet == null)
			bestelArtikelSet = new HashSet<BestelArtikel>();
		bestelArtikelSet.add(bestelArtikel);
	}

	public void verwijderArtikel(BestelArtikel bestelArtikel){
		if(bestelArtikelSet.contains(bestelArtikel))
			bestelArtikelSet.remove(bestelArtikel);
	}

}