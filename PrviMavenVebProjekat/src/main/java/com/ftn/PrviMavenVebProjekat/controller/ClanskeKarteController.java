package com.ftn.PrviMavenVebProjekat.controller;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ftn.PrviMavenVebProjekat.bean.SecondConfiguration.ApplicationMemory;
import com.ftn.PrviMavenVebProjekat.model.ClanskaKarta;
import com.ftn.PrviMavenVebProjekat.model.ClanskeKarte;
import com.ftn.PrviMavenVebProjekat.model.Knjiga;
import com.ftn.PrviMavenVebProjekat.model.Knjige;
import com.ftn.PrviMavenVebProjekat.model.Korisnici;
import com.ftn.PrviMavenVebProjekat.model.Korisnik;

@Controller
@RequestMapping(value = "/clanskekarte")
public class ClanskeKarteController implements ApplicationContextAware {
	
	public static final String CKARTE_KEY = "clanskekarte";
	

	@Autowired
	private ServletContext servletContext;
	private String bURL;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private ApplicationMemory memorijaAplikacije;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		bURL = servletContext.getContextPath() + "/";
		memorijaAplikacije = applicationContext.getBean(ApplicationMemory.class);
		ClanskeKarte clanskekarte = new ClanskeKarte();
		Korisnici korisnici = Korisnici.getInstance();

		memorijaAplikacije.put(ClanskeKarteController.CKARTE_KEY, clanskekarte);

	}
	@GetMapping
	@ResponseBody
	public String sveknjige(){
		ClanskeKarte clanskekarte = (ClanskeKarte) memorijaAplikacije.get(CKARTE_KEY);
		
		String retHTML = "<!DOCTYPE html>\r\n"
				+ "<html lang=\"en\">\r\n"
				+ "<head>\r\n"
				+ "    <meta charset=\"UTF-8\">\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
				+ "    <title>Prikaz svih clanskih karata</title>\r\n"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"/PrviMavenVebProjekat/css/StiloviTabela.css\"/>\r\n"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"/PrviMavenVebProjekat/css/StiloviHorizontalniMeni.css\"/>"
				+ "</head>\r\n"
				+ "<body>\r\n"
				+ "<table>"
				+"<tr>"
				+ "<th>Registarski Broj</th>"
				+ "<th>Korisnik</th>"
				+ "<th>Iznajmljene knjige: </th>"
				+ "</tr>";
		
		
		for(ClanskaKarta clanskaKarta : clanskekarte.findAll()) {
			retHTML += "<tr>"
					+ "<td>" + clanskaKarta.getRegistarskiBroj() + "</td>"
					+ "<td>" + clanskaKarta.getKorisnik().getIme() + " " + clanskaKarta.getKorisnik().getPrezime() + " /email:  "+ clanskaKarta.getKorisnik().getEmail() +  "</td>";
					for (Knjiga knjiga : clanskaKarta.getIznajmljenjeKnjige()) {
						if(clanskaKarta.getIznajmljenjeKnjige().size() != 0) {
							retHTML += "<td>"+knjiga.getNaziv()+"</td>";
						}
						else {
							retHTML += "<td>Nema iznajmljenih knjiga</td>";
						}
					};
			retHTML	+= "</tr>";
		}
		
		retHTML += "</table>"
				+ "<a href=index.html>Pocetna</a>"
				+ "</body>\r\n"
				+ "</html>";
		return retHTML;
	}
	
	@GetMapping(value = "/add")
	@ResponseBody
	public String create() {
		String retHTMl = "<!DOCTYPE html>\r\n"
				+ "<html>\r\n"
				+ "<head>\r\n"
				+ "<meta charset=\"UTF-8\">\r\n"
				+ "<title>DODAJ CLANSKU KARTU</title>\r\n"
				+ "</head>\r\n"
				+ "<body>\r\n"
				+ "	<form action=\"/PrviMavenVebProjekat/clanskekarte/add\" method=\"post\">\r\n"
				+ "		<label for=\"registarskibroj\">Registarski Broj: </label>\r\n"
				+ "		<input type = \"text\" name= \"registarskibroj\" /> <br>\r\n"
				+ "		<select name=\"korisnik\">\r\n";
		
		for (Korisnik korisnik : Korisnici.getInstance().findAll()) {
			retHTMl += "<option value=\""+ korisnik.getId() +"\">  "+korisnik.getIme()+" "+ " " +" "+korisnik.getPrezime()+" </option>";
		}

		retHTMl += "		</select>\r\n"
				+ "		 <input type=\"submit\" value=\"Potvrdi\">\r\n"
				+ "	</form>\r\n"
				+ "<a href=index.html>Pocetna</a>"
				+ "</body>\r\n"
				+ "</html>";
		
		return retHTMl;
	}
	
	@PostMapping(value = "/add")
	@ResponseBody
	public void create(@RequestParam String registarskibroj, @RequestParam Long korisnik, HttpServletResponse response)
			throws IOException {
		ClanskeKarte clanskekarte = (ClanskeKarte) memorijaAplikacije.get(CKARTE_KEY);
		clanskekarte.save(new ClanskaKarta(null, registarskibroj, Korisnici.getInstance().findOne(korisnik), new ArrayList<Knjiga>()));
		response.sendRedirect(bURL + "clanskekarte");
		return;

	}
	
	@GetMapping(value = "/zaduzivanje")
	@ResponseBody
		public String zaduzivanje(@RequestParam Long idknjige){
		String retHTMl = "<!DOCTYPE html>\r\n"
				+ "<html>\r\n"
				+ "<head>\r\n"
				+ "<meta charset=\"UTF-8\">\r\n"
				+ "<title>DODAJ KNJIGU</title>\r\n"
				+ "</head>\r\n"
				+ "<body>\r\n"
				+ "	<form action=\"" + bURL + "clanskekarte/zaduzi\" method=\"post\">\r\n"
				+ "		<label for=\"naziv\">ID clankse karte: </label>\r\n"
				+ "		<input type = \"number\" name= \"id\" /> <br>\r\n"
				+ "		<input type = \"hidden\" name= \"idknjige\" value=\""+idknjige+"\" /> <br>\r\n"
						+ "<input type = \"submit\">Potrvrdi</input>"
				+ "	</form>\r\n"
				+ "</body>\r\n"
				+ "</html>";
		return retHTMl;
			
		}
	
	@PostMapping(value = "/zaduzi")
	public void zaduzi(@RequestParam Long id, @RequestParam Long idknjige, HttpServletResponse response) throws IOException {
		ClanskeKarte clanskekarte = (ClanskeKarte) memorijaAplikacije.get(CKARTE_KEY);
		Knjige knjige = Knjige.getInstance();
		
		clanskekarte.findOne(id).getIznajmljenjeKnjige().add(knjige.findOne(idknjige));
		knjige.findOne(idknjige).setIzdata(true);
		
		response.sendRedirect(bURL + "clanskekarte");
	}
	
}
