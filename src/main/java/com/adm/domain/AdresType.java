package com.adm.domain;


import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
@Component
public class AdresType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4811340707485123892L;

	public AdresType() {}

	@Id
	@SequenceGenerator(name = "adresTypeId", sequenceName = "zadresType_sequence", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "adresTypeId")
	private long id;

	@Column
	private String adres_type;

	private static final String[] type = {"harrie.adres.homeAddress", "harrie.adres.workAddress", "harrie.adres.deliveryAddress"};

	// Getters and setters
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAdres_type() {
		return adres_type;
	}
	public void setAdres_type(int adres_type) {
		this.adres_type = type[adres_type];
	}
	public static String[] getType() {
		return type;
	}
	public void setAdres_type(String adres_type) {
		this.adres_type = adres_type;
	}

	@Override
	public String toString() {
		return adres_type;
	}
}
