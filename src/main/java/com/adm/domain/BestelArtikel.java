package com.adm.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class BestelArtikel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7442307765204064473L;

	@Id
	@SequenceGenerator(name = "bestArtid", sequenceName = "zBestel_artikel_sequence", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "bestArtid")
	private long id;

	@ManyToOne
	@JoinColumn(name = "artikelId", nullable = false)
	private Artikel artikel;

	@ManyToOne
	@JoinColumn(name = "BestellingId", nullable = false)
	private Bestelling bestelling;

	@ManyToOne
	@JoinColumn(name = "prijsId", nullable = false)
	private Prijs prijs;
	
	@Column(nullable = false)
	private int aantal;
	
	public BestelArtikel(){}

	public BestelArtikel(Bestelling bestelling, Artikel artikel, int aantal){
		this.artikel = artikel;
		this.bestelling = bestelling;
		this.aantal = aantal;		
	}

	public BestelArtikel(Prijs prijs, Artikel artikel, int aantal){
		this.artikel = artikel;
		this.prijs = prijs;
		this.aantal = aantal;
	}
	
	public long getId() {
		return id;
	}

	public Artikel getArtikel() {
		return artikel;
	}

	public Bestelling getBestelling() {
		return bestelling;
	}

	public int getAantal() {
		return aantal;
	}

	public Prijs getPrijs() {
		return prijs;
	}

	public void setPrijs(Prijs prijs) {
		this.prijs = prijs;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setArtikel(Artikel artikel) {
		this.artikel = artikel;
	}

	public void setBestelling(Bestelling bestelling) {
		this.bestelling = bestelling;
	}

	public void setAantal(int aantal) {
		this.aantal = aantal;
	}	

	
	//Maak hashcode adhv artikelnaam en bestellingnummer
	@Override
	public int hashCode() {
		if(bestelling == null ){
			bestelling = new Bestelling();
		}
		if(bestelling.getBestelNummer() == null)
			bestelling.setBestelNummer("0");
		
		return BestelArtikel.class.hashCode() + artikel.getArtikelNaam().hashCode() + bestelling.getBestelNummer().hashCode();

	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		BestelArtikel other = (BestelArtikel) obj;
		if (hashCode() == other.hashCode())
			return true;
		if (this == obj)
			return true;

		if (getClass() != obj.getClass())
			return false;
		if (hashCode() != other.hashCode())
			return false;
		return true;
	}

}