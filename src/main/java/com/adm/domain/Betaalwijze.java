package com.adm.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

@Entity
@Table
@Component
public class Betaalwijze {

	@Transient
	private final String[] type = {"harrie.betaling.creditcard",
			"harrie.betaling.cash",
			"harrie.betaling.ideal",
			"harrie.betaling.paypal",
			"harrie.betaling.inkind"};

	@Id
	@SequenceGenerator(name = "betaalwijzeId", sequenceName = "zbetaalwijze_sequence", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "betaalwijzeId")
	private long id;

	@Column
	private String betaalWijze;

	@Transient 
	String value;
	
	public String getBetaalWijze() {
		return betaalWijze;
	}

	public void setBetaalWijze(String betaalWijze) {
		this.betaalWijze = betaalWijze;
	}

	public String[] getType() {
		return type;
	}

	public long getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
		setBetaalWijzeType();
	}

	public void setBetaalWijzeType(){
		betaalWijze = type[Integer.parseInt(value)];
	}

}
