package models;

import models.enums.Uloga;

import java.util.Date;
import java.util.List;

public class Korisnik {
	private String email, ime, prezime, organizacija;
	private Uloga uloga;
	private List<Aktivnost> aktivnosti;

	public Korisnik(Uloga uloga) {
		this.uloga = uloga;
	}

	public Korisnik(String email, String ime, String prezime, String organizacija, Uloga uloga, List<Aktivnost> aktivnosti) {
		this.email = email;
		this.ime = ime;
		this.prezime = prezime;
		this.organizacija = organizacija;
		this.uloga = uloga;
		this.aktivnosti = aktivnosti;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getOrganizacija() {
		return organizacija;
	}

	public void setOrganizacija(String organizacija) {
		this.organizacija = organizacija;
	}

	public Uloga getUloga() {
		return uloga;
	}

	public void setUloga(Uloga uloga) {
		this.uloga = uloga;
	}

	public List<Aktivnost> getAktivnosti() {
		return aktivnosti;
	}

	public void setAktivnosti(List<Aktivnost> aktivnosti) {
		this.aktivnosti = aktivnosti;
	}
}
