package com.adm.domain;

import java.util.Date;
import java.util.LinkedHashSet;
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
public class Factuur {

	@Id
	@SequenceGenerator(name = "factuurId", sequenceName = "zfactuur_sequence", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "factuurId")
	private long id;
	
	@Column(nullable = false)
	private String factuurNummer;
	
	@Column(nullable = false)
	private Date factureringsDatum;
	
	@OneToMany(mappedBy = "factuur", cascade = CascadeType.ALL)
	@Column
	private Set<Betaling> betalingSet = new LinkedHashSet<Betaling>();
	
	@ManyToOne(optional = false)
	private Bestelling bestelling;

	public void voegBetalingToe(Betaling betaling){
		betalingSet.add(betaling);
	}

	public void haalBetalingWeg(Betaling betaling){
		if(betalingSet.contains(betaling))
			betalingSet.remove(betaling);
	}
	
	public long getId() {
		return id;
	}

	public String getFactuurNummer() {
		return factuurNummer;
	}

	public Date getFactureringsDatum() {
		return factureringsDatum;
	}

	public Set<Betaling> getBetalingSet() {
		return betalingSet;
	}

	public Bestelling getBestelling() {
		return bestelling;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setFactuurNummer(String factuurNummer) {
		this.factuurNummer = factuurNummer;
	}

	public void setFactureringsDatum(Date factureringsDatum) {
		this.factureringsDatum = factureringsDatum;
	}

	public void setBetalingSet(Set<Betaling> betalingSet) {
		this.betalingSet = betalingSet;
	}

	public void setBestelling(Bestelling bestelling) {
		this.bestelling = bestelling;
	}
	
}
