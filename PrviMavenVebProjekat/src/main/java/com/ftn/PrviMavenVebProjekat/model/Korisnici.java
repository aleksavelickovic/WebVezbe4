package com.ftn.PrviMavenVebProjekat.model;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Korisnici {
	private Map<Long, Korisnik> korisnici = new HashMap<>();
	private long nextId = 1L;
	private static Korisnici instance;

	/** Cita knjige iz datoteke i smesta ih u asocijativnu listu knjiga. 
	 * 
	 *  TODO parsirati trazeni fajl na nacin kako je to zapoceto, kreirati objekte klase Knjiga i sacuvati ih u kolekciji
	 * */
	private Korisnici() {

		try {
			Path path = Paths.get(getClass().getClassLoader().getResource("korisnici.txt").toURI());
			System.out.println(path.toFile().getAbsolutePath());
			List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));

			for (String line : lines) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				
				String[] tokens = line.split(";");
				Long id = Long.parseLong(tokens[0]);
				String ime = tokens[1];
				String prezime = tokens[2];
				String email = tokens[3];
				korisnici.put(id, new Korisnik(id, ime, prezime, email));			
				
				if(nextId<id)
					nextId=id;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Korisnici getInstance() {
		 if (instance == null) {
	            instance = new Korisnici();
	        }
	        return instance;
	}
	
	public Korisnik findOne(Long id) {
		return korisnici.get(id);
	}
	
	public List<Korisnik> findAll() {
		return new ArrayList<Korisnik>(korisnici.values());
	}
}
