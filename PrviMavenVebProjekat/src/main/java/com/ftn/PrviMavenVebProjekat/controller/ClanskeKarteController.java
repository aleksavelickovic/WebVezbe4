package com.ftn.PrviMavenVebProjekat.controller;

import java.security.PublicKey;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ftn.PrviMavenVebProjekat.bean.SecondConfiguration.ApplicationMemory;
import com.ftn.PrviMavenVebProjekat.model.ClanskaKarta;
import com.ftn.PrviMavenVebProjekat.model.ClanskeKarte;
import com.ftn.PrviMavenVebProjekat.model.Knjige;
import com.ftn.PrviMavenVebProjekat.model.Korisnici;

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
				+ "</tr>";
		
//				+ "   <h1>Iznajmljene knjige: </h1>\r\n";
		
		for(ClanskaKarta clanskaKarta : clanskekarte.findAll()) {
			retHTML += "<tr>"
					+ "<td>" + clanskaKarta.getRegistarskiBroj() + "</td>"
					+ "<td>" + clanskaKarta.getKorisnik().getIme() + " " + clanskaKarta.getKorisnik().getPrezime() + "</td>"
					+ "</tr>";
		}
		
		retHTML += "</table>"
				+ "</body>\r\n"
				+ "</html>";
		return retHTML;
	}

}
