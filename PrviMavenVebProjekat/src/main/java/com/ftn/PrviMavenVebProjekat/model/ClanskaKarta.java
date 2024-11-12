package com.ftn.PrviMavenVebProjekat.model;

import java.util.List;

public class ClanskaKarta {

	private Long id;
	private String registarskiBroj;
	private Korisnik korisnik;
	private List<Knjiga> iznajmljenjeKnjige;

	public ClanskaKarta() {
	}

	public ClanskaKarta(Long id, String registarskiBroj, Korisnik korisnik, List<Knjiga> iznajmljenjeKnjige) {
		super();
		this.id = id;
		this.registarskiBroj = registarskiBroj;
		this.korisnik = korisnik;
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

	public Korisnik getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}
	
	
	
}
