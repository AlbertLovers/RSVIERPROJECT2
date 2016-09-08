package com.adm.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity(name = "prijs")
public class Prijs implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = -8765138219730703338L;

	@Id
	@SequenceGenerator(name = "prijsId", sequenceName = "zprijs_sequence", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "prijsId")
	protected long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinTable(name = "prijsArtikel",
	joinColumns = @JoinColumn(name = "prijsId", nullable = false),
	inverseJoinColumns = @JoinColumn(name = "artikelId", nullable = false))
	Artikel artikel;
	
	@Column(nullable = false)
	BigDecimal prijs;
	
	@Column(nullable = false)
	Date datumAanmaak = new Date(System.currentTimeMillis());

	@OneToMany(mappedBy = "prijs")
	@Column(nullable = false)
	protected Set<BestelArtikel> bestelArtikel = new HashSet<BestelArtikel>();
	
	public Prijs(){
	}
	public Prijs(BigDecimal prijs) {
		this.prijs = prijs;
	}
	
	public Prijs(BigDecimal prijs, Artikel artikel) {
		this.prijs = prijs;
		this.artikel = artikel;
	}

	public long getId() {
		return id;
	}
	public BigDecimal getPrijs() {
		return prijs;
	}

	public Set<BestelArtikel> getBestelArtikel() {
		return bestelArtikel;
	}
	public Date getDatumAanmaak() {
		return datumAanmaak;
	}

	public Artikel getArtikel() {
		return artikel;
	}

	public void setArtikel(Artikel artikel) {
		this.artikel = artikel;
	}

	public void setPrijs(BigDecimal prijs) {
		this.prijs = prijs;
	}

	public void setBestelArtikel(Set<BestelArtikel> bestelArtikel) {
		this.bestelArtikel = bestelArtikel;
	}
	public void setDatumAanmaak(Date datumAanmaak) {
		this.datumAanmaak = datumAanmaak;
	}
	public void setId(long id) {
		this.id = id;
	}
}
