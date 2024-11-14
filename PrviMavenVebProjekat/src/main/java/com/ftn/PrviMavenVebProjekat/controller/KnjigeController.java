package com.ftn.PrviMavenVebProjekat.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
@RequestMapping(value = "/knjige")
public class KnjigeController implements ApplicationContextAware {

//	public static final String KNJIGE_KEY = "knjige";

	@Autowired
	private ServletContext servletContext;
	private String bURL;

	@Autowired
	private ApplicationContext applicationContext;

//	@Autowired
//	private ApplicationMemory memorijaAplikacije;

	/** pristup ApplicationContext */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/** inicijalizacija podataka za kontroler */
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		bURL = servletContext.getContextPath() + "/";
//		memorijaAplikacije = applicationContext.getBean(ApplicationMemory.class);
//		Knjige knjige = Knjige.getInstance();

//		servletContext.setAttribute(KnjigeController.KNJIGE_KEY, knjige);	

//		memorijaAplikacije.put(KnjigeController.KNJIGE_KEY, knjige);
	}

	/**
	 * pribavnjanje HTML stanice za prikaz svih entiteta, get zahtev
	 * 
	 * @throws IOException
	 */
	// GET: knjige
	@GetMapping
	@ResponseBody
	public void index(HttpServletResponse response, HttpSession session) throws IOException {
		PrintWriter out = response.getWriter();

		Knjige knjige = (Knjige) session.getAttribute(GeneralController.KNJIGE_KEY);

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
		Element thObrisi = new Element(Tag.valueOf("th"), "").text("Brisanje");

		thRow.appendChild(thId);
		thRow.appendChild(thName);
		thRow.appendChild(thRegNo);
		thRow.appendChild(thLang);
		thRow.appendChild(thPages);
		thRow.appendChild(thDetalji);
		thRow.appendChild(thObrisi);

		tableNode.appendChild(caption);
		tableNode.appendChild(thRow);

		for (Knjiga knjiga : knjige.findAll()) {
			if (knjiga.isIzdata() == false) {
				Element rowElement = new Element(Tag.valueOf("tr"), "");
				Element tdIdElement = new Element(Tag.valueOf("td"), "").text(knjiga.getId().toString());
				Element tdName = new Element(Tag.valueOf("td"), "").text(knjiga.getNaziv());
				Element tdRegNo = new Element(Tag.valueOf("td"), "").text(knjiga.getRegistarskiBrojPrimerka());
				Element tdLaungauge = new Element(Tag.valueOf("td"), "").text(knjiga.getJezik());
				Element tdPages = new Element(Tag.valueOf("td"), "").text(String.valueOf(knjiga.getBrojStranica()));
				Element tdButton = new Element(Tag.valueOf("td"), "");
				Element Href = new Element(Tag.valueOf("a"), "").attr("href", bURL + "knjige/details?id=" + knjiga.getId())
						.text("Detalji");
				tdButton.appendChild(Href);
				Element tdObrisi = new Element(Tag.valueOf("td"), "");
				Element hrefObrisi = new Element(Tag.valueOf("form"), "")
						.attr("action", bURL + "knjige/delete?id=" + knjiga.getId()).attr("method", "post");
				Element btnObrisi = new Element(Tag.valueOf("button"), "").attr("type", "submit").text("Obrisi");
				hrefObrisi.appendChild(btnObrisi);
				tdObrisi.appendChild(hrefObrisi);

				rowElement.appendChild(tdIdElement);
				rowElement.appendChild(tdName);
				rowElement.appendChild(tdRegNo);
				rowElement.appendChild(tdLaungauge);
				rowElement.appendChild(tdPages);
				rowElement.appendChild(tdButton);
				rowElement.appendChild(tdObrisi);

				tableNode.appendChild(rowElement);
			}
		}
		body.appendChild(tableNode);
		Element Href = new Element(Tag.valueOf("a"), "").attr("href", bURL).text("Pocetna");
		body.appendChild(Href);
		out.write(document.html());

		return;
	}

	/** pribavnjanje HTML stanice za unos novog entiteta, get zahtev */
	// GET: knjige/dodaj
	@GetMapping(value = "/add")
	public String create() {
		return "/dodaj-knjigu.html";
	}

	/** obrada podataka forme za unos novog entiteta, post zahtev */
	// POST: knjige/add
	@PostMapping(value = "/add")
	@ResponseBody
	public void create(@RequestParam String naziv, @RequestParam String registarskiBrojPrimerka,
			@RequestParam String jezik, @RequestParam int brojStranica, HttpServletResponse response, HttpSession session)
			throws IOException {
		Knjige knjige = (Knjige) session.getAttribute(GeneralController.KNJIGE_KEY);
		knjige.save(new Knjiga(null, naziv, registarskiBrojPrimerka, jezik, brojStranica));
		response.sendRedirect(bURL + "knjige");
		return;

	}

	/** obrada podataka forme za izmenu postojećeg entiteta, post zahtev */
	// POST: knjige/edit
	@PostMapping(value = "/edit")
	public void edit(@ModelAttribute Knjiga knjigaEdited, HttpServletResponse response, HttpSession session) throws IOException {
		Knjige knjige = (Knjige) session.getAttribute(GeneralController.KNJIGE_KEY);
		Knjiga knjigaOriginal = knjige.findOne(knjigaEdited.getId());

		knjigaOriginal.setNaziv(knjigaEdited.getNaziv());
		knjigaOriginal.setRegistarskiBrojPrimerka(knjigaEdited.getRegistarskiBrojPrimerka());
		knjigaOriginal.setJezik(knjigaEdited.getJezik());
		knjigaOriginal.setBrojStranica(knjigaEdited.getBrojStranica());

		response.sendRedirect(bURL + "knjige");
		return;
	}

	/** obrada podataka forme za za brisanje postojećeg entiteta, post zahtev */
	// POST: knjige/delete
	@PostMapping(value = "/delete")
	public void delete(@RequestParam Long id, HttpServletResponse response, HttpSession session) throws IOException {
		Knjige knjige = (Knjige) session.getAttribute(GeneralController.KNJIGE_KEY);
		knjige.delete(id);
		response.sendRedirect(bURL + "knjige");
	}

	/** pribavnjanje HTML stanice za prikaz određenog entiteta , get zahtev */
	// GET: knjige/details?id=1
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/details")
	@ResponseBody
	public String details(@RequestParam Long id, HttpSession session) {
		Knjige knjige = (Knjige) session.getAttribute(GeneralController.KNJIGE_KEY);
		Knjiga knjiga = knjige.findOne(id);
		ArrayList<Knjiga> knjigeUKorpi = (ArrayList<Knjiga>) session.getAttribute(GeneralController.KORPA_KEY);

		String retHTML = "<!DOCTYPE html>\r\n" + "<html>\r\n" + "<head>\r\n" + "<meta charset=\"UTF-8\">\r\n"
				+ "<title>Prikaz detalja, izmena i brisanje knjige</title>\r\n" + "</head>\r\n" + "<body>\r\n"
				+ "	<form action=\"/PrviMavenVebProjekat/knjige/edit?id=" + knjiga.getId() + "\" method=\"post\">\r\n"
				+ "		<label for=\"naziv\">Naziv: </label>\r\n"
				+ "		<input type = \"text\" name= \"naziv\" value=\"" + knjiga.getNaziv() + "\" /> <br>\r\n"
				+ "		<label for=\"registarskiBrojPrimerka\">registarskiBrojPrimerka: </label>\r\n"
				+ "		<input type = \"text\" name= \"registarskiBrojPrimerka\" value=\""
				+ knjiga.getRegistarskiBrojPrimerka() + "\" /> <br>\r\n"
				+ "		<label for=\"jezik\">Jezik: </label>\r\n"
				+ "		<input type = \"text\" name= \"jezik\" value=\"" + knjiga.getJezik() + "\" /> <br>\r\n"
				+ "		<label for=\"brojStranica\">Broj Stranica: </label>\r\n"
				+ "		<input type = \"number\" name= \"brojStranica\" value=\"" + knjiga.getBrojStranica()
				+ "\" /> <br>\r\n" + "		<input type = \"submit\" value = \"Potvrdi\"/>\r\n" + "	</form>\r\n"
				+ "	<form action=\"/PrviMavenVebProjekat/knjige/delete?id=" + knjiga.getId() + "\" method=\"post\">\r\n"
				+ "\r\n" + "		<input type = \"submit\" value = \"Obrisi ovu knjigu\"/>\r\n" + "	</form>"
				+ "<br>";
		if (knjigeUKorpi.contains(knjiga)) {
			retHTML += "<a href="+bURL+"clanskekarte/ukloniizkorpe?id="+knjiga.getId()+">Ukloni iz korpe</a>";
		}

		if (knjiga.isIzdata() == false) {
			retHTML += "<a href="+ bURL+ "clanskekarte/zaduzi?idknjige="+knjiga.getId()+">Zaduzi ovu knjigu</a>";
		}
		else {
			retHTML += "<h3>Ova knjiga je izdata!!!</h3>";
		}
		retHTML += "</body>\r\n" + "</html>";
		return retHTML;
	}
}
