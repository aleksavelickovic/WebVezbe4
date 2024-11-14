package com.ftn.PrviMavenVebProjekat.controller;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
	
//	public static final String CKARTE_KEY = "clanskekarte";
	
	

	@Autowired
	private ServletContext servletContext;
	private String bURL;

	@Autowired
	private ApplicationContext applicationContext;

//	@Autowired
//	private ApplicationMemory memorijaAplikacije;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		bURL = servletContext.getContextPath() + "/";
//		memorijaAplikacije = applicationContext.getBean(ApplicationMemory.class);
//		ClanskeKarte clanskekarte = ClanskeKarte.getInstance();
//		Korisnici korisnici = Korisnici.getInstance();
//
//		memorijaAplikacije.put(ClanskeKarteController.CKARTE_KEY, clanskekarte);
		
//		ArrayList<Knjiga> knjigeUKorpi = new ArrayList<Knjiga>();
//		memorijaAplikacije.put("knjigeukorpi", knjigeUKorpi);

	}
	@GetMapping
	@ResponseBody
	public String sveclanskekarte(HttpSession session){
		ClanskeKarte clanskekarte = (ClanskeKarte) session.getAttribute(GeneralController.CKARTE_KEY);
//		session.setAttribute("korpa", memorijaAplikacije.get("knjigeukorpi"));
		
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
				+ "<th>Prikaz detalja: </th>"
				+ "</tr>";
		
		
		for(ClanskaKarta clanskaKarta : clanskekarte.findAll()) {
			retHTML += "<tr>"
					+ "<td>" + clanskaKarta.getRegistarskiBroj() + "</td>"
					+ "<td>" + clanskaKarta.getKorisnik().getIme() + " " + clanskaKarta.getKorisnik().getPrezime() + " /email:  "+ clanskaKarta.getKorisnik().getEmail() +  "</td>";
			if (clanskaKarta.getIznajmljenjeKnjige().size() != 0) {
			    for (Knjiga knjiga : clanskaKarta.getIznajmljenjeKnjige()) {
			        retHTML += "<td>"+knjiga.getNaziv()+"</td>";
			    }
			} else {
			    retHTML += "<td>Nema iznajmljenih knjiga!</td>";
			}
					
			retHTML	+= "<td><a href = " + bURL + "clanskekarte/details?id="+clanskaKarta.getId()+">Detalji</a></td>"
					+ "</tr>";
		}
		
		retHTML += "</table>"
				+ "<a href=index.html>Pocetna</a>"
				+ "</body>\r\n"
				+ "</html>";
		return retHTML;
	}
	
	@GetMapping(value = "/add")
	@ResponseBody
	public String create(HttpSession session) {
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
		
		for (Korisnik korisnik : (ArrayList<Korisnik>) ((Korisnici) session.getAttribute(GeneralController.KORISNICI_KEY)).findAll()) {
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
	public void create(@RequestParam String registarskibroj, @RequestParam Long korisnik, HttpServletResponse response, HttpSession session)
			throws IOException {
		ClanskeKarte clanskekarte = (ClanskeKarte) session.getAttribute(GeneralController.CKARTE_KEY);
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
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/zaduzi")
	public void zaduzi(@RequestParam Long idknjige, HttpServletResponse response, HttpSession session) throws IOException {
		ClanskeKarte clanskekarte = (ClanskeKarte) session.getAttribute(GeneralController.CKARTE_KEY);
		Knjige knjige = (Knjige) session.getAttribute(GeneralController.KNJIGE_KEY);
		ArrayList<Knjiga> knjigeUKorpi = (ArrayList<Knjiga>) session.getAttribute(GeneralController.KORPA_KEY);
//		session.setAttribute("korpa", knjigeUKorpi);
		
//		clanskekarte.findOne(id).getIznajmljenjeKnjige().add(knjige.findOne(idknjige));
		
		knjigeUKorpi.add(knjige.findOne(idknjige));
		knjige.findOne(idknjige).setIzdata(true);
		
		response.sendRedirect(bURL + "clanskekarte");
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/zaduzisveknjige")
	public void zaduzisve(@RequestParam Long id, HttpServletResponse response, HttpSession session) throws IOException {
		ClanskeKarte clanskekarte = (ClanskeKarte) session.getAttribute(GeneralController.CKARTE_KEY);
		Knjige knjige = (Knjige) session.getAttribute(GeneralController.KNJIGE_KEY);
//		ArrayList<Knjiga> knjigeUKorpi = (ArrayList<Knjiga>) memorijaAplikacije.get("knjigeukorpi");
		
		
		for (Knjiga knjiga : (ArrayList<Knjiga>) session.getAttribute(GeneralController.KORPA_KEY)) {
//			Knjiga knjiga = knjige.findOne(i);
			clanskekarte.findOne(id).getIznajmljenjeKnjige().add(knjiga);
			knjiga.setIzdata(true);
//			((HashMap) session.getAttribute("korpa")).remove(knjiga);
		}
		ArrayList<Knjiga> knjigeUKorpi = (ArrayList<Knjiga>) session.getAttribute(GeneralController.KORPA_KEY);
		knjigeUKorpi.clear();
		response.sendRedirect(bURL + "clanskekarte");
	}
	
	@GetMapping(value = "/details")
	@ResponseBody
	public String detalji(@RequestParam Long id, HttpSession session) {
		ClanskeKarte clanskekarte = (ClanskeKarte) session.getAttribute(GeneralController.CKARTE_KEY);
		ClanskaKarta clanskaKarta = clanskekarte.findOne(id);
		String retHTML = "<!DOCTYPE html>\r\n"
				+ "<html lang=\"en\">\r\n"
				+ "<head>\r\n"
				+ "    <meta charset=\"UTF-8\">\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
				+ "    <title>Detalji clanske karte</title>\r\n"
				+ "</head>\r\n"
				+ "<body>\r\n"
				+ "   <h1>Registarski broj: </h1>\r\n"
				+ "   <p>"+clanskaKarta.getRegistarskiBroj()+"</p>\r\n"
				+ "   <h1>Ime I prezime korisnika: </h1>\r\n"
				+ "   <p>"+clanskaKarta.getKorisnik().getIme() + " " + clanskaKarta.getKorisnik().getPrezime() +"</p>\r\n"
				+ "   <h1>Iznajmljene knjige: </h1>\r\n";
		if (clanskaKarta.getIznajmljenjeKnjige().size() != 0) {
		    for (Knjiga knjiga : clanskaKarta.getIznajmljenjeKnjige()) {
		        retHTML += "<p>" + knjiga.getNaziv() + "</p>"
		        		+ "<a href="+bURL+"clanskekarte/razduzivanje?id="+knjiga.getId()+"&idkarte="+clanskaKarta.getId()+">Razduzi ovu knjigu</a>";
		    }
		} else {
		    retHTML += "<p>Nema iznajmljenih knjiga na ovu clansku kartu!</p>";
		}
		retHTML += "<a href="+bURL+"clanskekarte/zaduzisveknjige?id="+clanskaKarta.getId()+">Zaduzi sve knjige na ovu clansku kartu</a>";
		retHTML += "<br><br>"
				+ "<a href="+bURL+">Pocetna</a>"
				+ "</body>";
		return retHTML;
	}
	
	@GetMapping(value = "/razduzivanje")
	public void razduziKnjigu(@RequestParam Long id,@RequestParam Long idkarte , HttpServletResponse response, HttpServletRequest request, HttpSession session) throws IOException {
		ClanskeKarte clanskekarte = (ClanskeKarte) session.getAttribute(GeneralController.CKARTE_KEY);
		ClanskaKarta clanskaKarta = clanskekarte.findOne(idkarte);
		Knjige knjige = (Knjige) session.getAttribute("knjige");
		
		clanskaKarta.getIznajmljenjeKnjige().remove(knjige.findOne(id));
		knjige.findOne(id).setIzdata(false);
		
		response.sendRedirect(bURL + "clanskekarte/details?id=" + idkarte);
		return;
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/korpa")
	@ResponseBody
	public String korpa(HttpSession session) {
		ArrayList<Knjiga> knjigeUKorpi = (ArrayList<Knjiga>) session.getAttribute(GeneralController.KORPA_KEY);
		String retHTML = "";
		retHTML += "<!DOCTYPE html>\r\n"
				+ "<html>\r\n"
				+ "<head>\r\n"
				+ "<meta charset=\"UTF-8\"> \r\n"
				+ "<title>Knjige</title>\r\n"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"/PrviMavenVebProjekat/css/StiloviTabela.css\"/>\r\n"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"/PrviMavenVebProjekat/css/StiloviHorizontalniMeni.css\"/>		\r\n"
				+ "</head>\r\n"
				+ "<body>\r\n";
		if (knjigeUKorpi.size() != 0) {		
			for (Knjiga knjiga : knjigeUKorpi) {
				retHTML += "<p>"+knjiga.getNaziv()+"</p>";
			}
		}
		else {
			retHTML += "<p>Nema knjiga u korpi</p>";
		}
		
		retHTML	+= "</body>\r\n"
				+ "</html>";
		
		return retHTML;
	}
	
}
