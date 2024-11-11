package com.ftn.PrviMavenVebProjekat.model;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClanskeKarte {
	private Map<Long, ClanskaKarta> clanskekarte = new HashMap<>();
	private long nextId = 1L;

	/** Cita knjige iz datoteke i smesta ih u asocijativnu listu knjiga. 
	 * 
	 *  TODO parsirati trazeni fajl na nacin kako je to zapoceto, kreirati objekte klase Knjiga i sacuvati ih u kolekciji
	 * */
	public ClanskeKarte() {

		try {
			Path path = Paths.get(getClass().getClassLoader().getResource("clanskekarte.txt").toURI());
			System.out.println(path.toFile().getAbsolutePath());
			List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));

			for (String line : lines) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				
				String[] tokens = line.split(";");
				Long id = Long.parseLong(tokens[0]);
				String regBroj = tokens[1];
				clanskekarte.put(id, new ClanskaKarta(id, regBroj, new ArrayList<Knjiga>()));			
				
				if(nextId<id)
					nextId=id;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<ClanskaKarta> findAll() {
		return new ArrayList<ClanskaKarta>(clanskekarte.values());
	}
	
}
