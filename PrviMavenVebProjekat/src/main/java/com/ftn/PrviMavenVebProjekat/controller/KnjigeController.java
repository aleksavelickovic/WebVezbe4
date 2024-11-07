package com.ftn.PrviMavenVebProjekat.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.basic.BasicComboBoxUI.ListDataHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.engine.ElementDefinition;

import com.ftn.PrviMavenVebProjekat.bean.SecondConfiguration.ApplicationMemory;
import com.ftn.PrviMavenVebProjekat.model.Knjiga;
import com.ftn.PrviMavenVebProjekat.model.Knjige;

@Controller
@RequestMapping(value="/knjige")
public class KnjigeController implements ApplicationContextAware {

	public static final String KNJIGE_KEY = "knjige";
	
	@Autowired
	private ServletContext servletContext;
	private String bURL; 
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private ApplicationMemory memorijaAplikacije;

	/** pristup ApplicationContext */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	    this.applicationContext = applicationContext;  
	}
	
	/** inicijalizacija podataka za kontroler */
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {	
		bURL = servletContext.getContextPath()+"/";
		memorijaAplikacije = applicationContext.getBean(ApplicationMemory.class);
	    Knjige knjige = new Knjige(); 
	    
		
//		servletContext.setAttribute(KnjigeController.KNJIGE_KEY, knjige);	
		
		memorijaAplikacije.put(KnjigeController.KNJIGE_KEY, knjige);
	}
	
	/** pribavnjanje HTML stanice za prikaz svih entiteta, get zahtev 
	 * @throws IOException */
	// GET: knjige
	@GetMapping
	@ResponseBody
	public void index(HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		Knjige knjige = (Knjige) memorijaAplikacije.get(KNJIGE_KEY);
		
		File htmlFile = new ClassPathResource("static/template.html").getFile();
		Document document = Jsoup.parse(htmlFile, "UTF-8");
		
		Element body = document.select("body").first();
		
		Element tableNode = new Element(Tag.valueOf("table"), "");
		Element caption = new Element(Tag.valueOf("caption"), "");
		Element thRow = new Element(Tag.valueOf("tr"), "");
		Element thId = new Element(Tag.valueOf("th"), "").text("ID");
		Element thName = new Element(Tag.valueOf("th"), "").text("Name");
		Element thRegNo = new Element(Tag.valueOf("th"), "").text("Reg No.");
		Element thLang = new Element(Tag.valueOf("th"), "").text("Laungauge");
		Element thPages = new Element(Tag.valueOf("th"), "").text("Pages");
		Element thDetalji = new Element(Tag.valueOf("th"), "").text("Detalji");
		
		thRow.appendChild(thId);
		thRow.appendChild(thName);
		thRow.appendChild(thRegNo);
		thRow.appendChild(thLang);
		thRow.appendChild(thPages);
		thRow.appendChild(thDetalji);
		
		tableNode.appendChild(caption);
		tableNode.appendChild(thRow);
		
		for (Knjiga knjiga : knjige.findAll()) {
			Element rowElement = new Element(Tag.valueOf("tr"), "");
			Element tdIdElement = new Element(Tag.valueOf("td"), "").text(knjiga.getId().toString());
			Element tdName = new Element(Tag.valueOf("td"), "").text(knjiga.getNaziv());
			Element tdRegNo = new Element(Tag.valueOf("td"), "").text(knjiga.getRegistarskiBrojPrimerka());
			Element tdLaungauge = new Element(Tag.valueOf("td"), "").text(knjiga.getJezik());
			Element tdPages = new Element(Tag.valueOf("td"), "").text(String.valueOf(knjiga.getBrojStranica()));
			Element tdButton = new Element(Tag.valueOf("td"), "");
			Element Href = new Element(Tag.valueOf("a"), "").attr("href", bURL + "knjige/details?id=" + knjiga.getId()).text("Detalji");
			tdButton.appendChild(Href);
			
			rowElement.appendChild(tdIdElement);
			rowElement.appendChild(tdName);
			rowElement.appendChild(tdRegNo);
			rowElement.appendChild(tdLaungauge);
			rowElement.appendChild(tdPages);
			rowElement.appendChild(tdButton);
			
			tableNode.appendChild(rowElement);
		}
		body.appendChild(tableNode);
		out.write(document.html());
		
		return;
	}
	
	/** pribavnjanje HTML stanice za unos novog entiteta, get zahtev */
	// GET: knjige/dodaj
	@GetMapping(value="/add")
	public String create() {
		return "/dodaj-knjigu.html";
	}
	
	/** obrada podataka forme za unos novog entiteta, post zahtev */
	// POST: knjige/add
	@PostMapping(value = "/add")
	@ResponseBody
	public void create(@RequestParam String naziv, @RequestParam String registarskiBrojPrimerka, 
			@RequestParam String jezik, @RequestParam int brojStranica, HttpServletResponse response) throws IOException {	
		Knjige knjige = (Knjige) memorijaAplikacije.get(KNJIGE_KEY);
		knjige.save(new Knjiga(null, naziv, registarskiBrojPrimerka, jezik, brojStranica));
		response.sendRedirect(bURL + "knjige");
		return;
	
	}
	
	/** obrada podataka forme za izmenu postojećeg entiteta, post zahtev */
	// POST: knjige/edit
	@PostMapping(value="/edit")
	public void edit(@ModelAttribute Knjiga knjigaEdited , HttpServletResponse response) throws IOException {	
	
	}
	
	/** obrada podataka forme za za brisanje postojećeg entiteta, post zahtev */
	// POST: knjige/delete
	@PostMapping(value="/delete")
	public void delete(@RequestParam Long id, HttpServletResponse response) throws IOException {
		
	}
	
	/** pribavnjanje HTML stanice za prikaz određenog entiteta , get zahtev */
	// GET: knjige/details?id=1
	@GetMapping(value="/details")
	@ResponseBody
	public String details(@RequestParam Long id) {
		Knjige knjige = (Knjige) memorijaAplikacije.get(KNJIGE_KEY);
		Knjiga knjiga = knjige.findOne(id);
		
		return "<!DOCTYPE html>\r\n"
				+ "<html>\r\n"
				+ "<head>\r\n"
				+ "<meta charset=\"UTF-8\"> \r\n"
				+ "<title>Detalji o izabranoj knjizi</title>\r\n"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"/PrviMavenVebProjekat/css/StiloviTabela.css\"/>\r\n"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"/PrviMavenVebProjekat/css/StiloviHorizontalniMeni.css\"/>		\r\n"
				+ "</head>\r\n"
				+ "<body>\r\n"
				+ "\r\n"
				+ "<h1>Naziv: "+knjiga.getNaziv()+"</h1>\r\n"
				+ "<h1>Registarski Broj: "+knjiga.getRegistarskiBrojPrimerka()+"</h1>\r\n"
				+ "<h1>Jezik: "+knjiga.getJezik()+"</h1>\r\n"
				+ "<h1>Broj Stranica: "+knjiga.getBrojStranica()+"</h1>\r\n"
				+ "\r\n"
				+ "</body>\r\n"
				+ "</html>";
	}
}
