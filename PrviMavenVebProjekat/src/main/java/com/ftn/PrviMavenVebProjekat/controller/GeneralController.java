package com.ftn.PrviMavenVebProjekat.controller;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ftn.PrviMavenVebProjekat.bean.SecondConfiguration.ApplicationMemory;
import com.ftn.PrviMavenVebProjekat.model.ClanskeKarte;
import com.ftn.PrviMavenVebProjekat.model.Knjiga;
import com.ftn.PrviMavenVebProjekat.model.Knjige;
import com.ftn.PrviMavenVebProjekat.model.Korisnici;

@Controller
@RequestMapping(value = "/")
public class GeneralController implements ApplicationContextAware {
	
public static final String KNJIGE_KEY = "knjige";
public static final String CKARTE_KEY = "clanskekarte";
public static final String KORISNICI_KEY = "korisnici";
public static final String KORPA_KEY = "korpa";
	
	

	@Autowired
	private ServletContext servletContext;
	private String bURL;
//	@Autowired
//	private HttpSession session;
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
//		ClanskeKarte clanskekarte = ClanskeKarte.getInstance();
//		Korisnici korisnici = Korisnici.getInstance();
//
//		memorijaAplikacije.put(ClanskeKarteController.CKARTE_KEY, clanskekarte);
//		
//		ArrayList<Knjiga> knjigeUKorpi = new ArrayList<Knjiga>();
//		memorijaAplikacije.put("knjigeukorpi", knjigeUKorpi);

	}
	
	@GetMapping
	public String initialise(HttpSession session) {
		
		Knjige knjige = Knjige.getInstance();
		ClanskeKarte clanskekarte = ClanskeKarte.getInstance();	
		Korisnici korisnici = Korisnici.getInstance();
		ArrayList<Knjiga> knjigeUKorpi = new ArrayList<Knjiga>();
		
		session.setAttribute(KNJIGE_KEY, knjige);
		session.setAttribute(CKARTE_KEY, clanskekarte);
		session.setAttribute(KORISNICI_KEY, korisnici);
		session.setAttribute(KORPA_KEY, knjigeUKorpi);
		
		return "/index.html";
	}

}
