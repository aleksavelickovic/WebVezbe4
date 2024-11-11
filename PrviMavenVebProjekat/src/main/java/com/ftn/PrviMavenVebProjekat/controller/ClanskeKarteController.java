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
import com.ftn.PrviMavenVebProjekat.model.ClanskeKarte;
import com.ftn.PrviMavenVebProjekat.model.Knjige;

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

		memorijaAplikacije.put(ClanskeKarteController.CKARTE_KEY, clanskekarte);

	}
	@GetMapping
	@ResponseBody
	public String sveknjige(){
		return "<!DOCTYPE html>\r\n"
				+ "<html lang=\"en\">\r\n"
				+ "<head>\r\n"
				+ "    <meta charset=\"UTF-8\">\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
				+ "    <title>Prikaz svih clanskih karata</title>\r\n"
				+ "</head>\r\n"
				+ "<body>\r\n"
				+ "   <h1>Registarski broj: </h1>\r\n"
				+ "   <p></p>\r\n"
				+ "   <h1>Iznajmljene knjige: </h1>\r\n"
				+ "</body>\r\n"
				+ "</html>";
	}

}
