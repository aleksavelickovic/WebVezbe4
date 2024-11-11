package com.ftn.PrviMavenVebProjekat.model;

import java.util.List;

public class ClanskaKarta {

	private Long id;
	private String registarskiBroj;
	private List<Knjiga> iznajmljenjeKnjige;

	public ClanskaKarta() {
	}

	public ClanskaKarta(Long id, String registarskiBroj, List<Knjiga> iznajmljenjeKnjige) {
		super();
		this.id = id;
		this.registarskiBroj = registarskiBroj;
		this.iznajmljenjeKnjige = iznajmljenjeKnjige;
	}

	public String getRegistarskiBroj() {
		return registarskiBroj;
	}

	public void setRegistarskiBroj(String registarskiBroj) {
		this.registarskiBroj = registarskiBroj;
	}

	public List<Knjiga> getIznajmljenjeKnjige() {
		return iznajmljenjeKnjige;
	}

	public void setIznajmljenjeKnjige(List<Knjiga> iznajmljenjeKnjige) {
		this.iznajmljenjeKnjige = iznajmljenjeKnjige;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
