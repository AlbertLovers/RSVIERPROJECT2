package com.adm.domain;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class Betaling {

	@Id
	@SequenceGenerator(name = "betalingId", sequenceName = "zbetaling_sequence", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "betalingId")
	private long id;

	@Column(nullable = false)
	Date betaalDatum;

	@OneToOne(optional = false, cascade = CascadeType.ALL)
	Betaalwijze betaalwijze;

	@ManyToOne(optional = false)
	Klant klant;

	@ManyToOne(optional = false)
	Factuur factuur;

	@Column(nullable = true)
	String betalingsGegevens;

	public long getId() {
		return id;
	}

	public Date getBetaalDatum() {
		return betaalDatum;
	}

	public Betaalwijze getBetaalwijze() {
		return betaalwijze;
	}

	public Klant getKlant() {
		return klant;
	}

	public Factuur getFactuur() {
		return factuur;
	}

	public String getBetalingsGegevens() {
		return betalingsGegevens;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setBetaalDatum(Date betaalDatum) {
		this.betaalDatum = betaalDatum;
	}

	public void setBetaalwijze(Betaalwijze betaalwijze) {
		this.betaalwijze = betaalwijze;
	}

	public void setKlant(Klant klant) {
		this.klant = klant;
	}

	public void setFactuur(Factuur factuur) {
		this.factuur = factuur;
	}

	public void setBetalingsGegevens(String betalingsGegevens) {
		this.betalingsGegevens = betalingsGegevens;
	}


}
