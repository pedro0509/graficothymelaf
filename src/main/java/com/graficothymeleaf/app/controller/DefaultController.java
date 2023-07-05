package com.graficothymeleaf.app.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.graficothymeleaf.app.util.WebClientSelic;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.source.ByteArrayOutputStream;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class DefaultController {

	Logger logger = LoggerFactory.getLogger(DefaultController.class);

	@GetMapping("/")
	public String index(Model model) throws IOException {
		model.addAttribute("grafico", getChartData());
		return "inicio";
	}

	private List<Object> getChartData() throws IOException {

		WebClientSelic selic = new WebClientSelic();

		JSONArray ret = selic.buscaSerieTxSelicUltimosValores(50);

		List<Object> regis = new ArrayList<>();

		regis.add(new Object[] { "Data", "Série 11" });

		for (int i = 0; i < ret.length(); i++) {
			JSONObject o = ret.getJSONObject(i);
			regis.add(new Object[] { o.getString("data"), o.getBigDecimal("valor") });
		}

//		try {
//
//			List<Registro> users = new Gson().fromJson(ret.toString(), new TypeToken<List<Registro>>() {
//			}.getType());
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}

		return regis;

	}

	@GetMapping("/pdfImpressao")
	public ModelAndView getPDFImpressao()
			throws IOException, ParseException, MalformedURLException, java.io.IOException {
		ModelAndView modelAndView = new ModelAndView("impressoes/impressao");
		modelAndView.addObject("grafico", getChartData());

		return modelAndView;
	}

	@SuppressWarnings("resource")
	@GetMapping("/pdf")
	public ResponseEntity<?> getPDF(RedirectAttributes attributes, HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ParseException, MalformedURLException, java.io.IOException {

		java.net.URL url = new java.net.URL("http://localhost:8080/pdfImpressao");

		/* Setup Source and target I/O streams */

		ByteArrayOutputStream target = new ByteArrayOutputStream();

		WebClient webClient = new WebClient();
		// You might need this configuration if HtmlUnit fails without it
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.waitForBackgroundJavaScript(10 * 1000);
		HtmlPage page = webClient.getPage(url);
		String xml = page.asXml();

		/* Setup converter properties. */
		ConverterProperties converterProperties = new ConverterProperties();
		converterProperties.setBaseUri("http://localhost:8080");
		converterProperties.setCharset("UTF-8");

		/* Call convert method */
		HtmlConverter.convertToPdf(xml, target, converterProperties);

		/* extract output as bytes */
		byte[] bytes = target.toByteArray();

		/* Send the response as downloadable PDF */
//		return ResponseEntity.ok()
//				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Relatorio Localidade.pdf")
//				.contentType(MediaType.APPLICATION_PDF).body(bytes);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		headers.add("Content-Disposition", "inline;attachment;filename=Relatorio.pdf");

		/* Send the response as downloadable PDF */
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(bytes);
	}

//	public class Registro {
//
//		private String data;
//		private BigDecimal valor;
//
//		public String getMesAno() {
//			return data;
//		}
//
//		public void setMes(String data) {
//			this.data = data;
//		}
//
//		public BigDecimal getValor() {
//			return valor;
//		}
//
//		public void setValor(BigDecimal valor) {
//			this.valor = valor;
//		}
//
//		public Registro(String data, BigDecimal valor) {
//			this.data = data;
//			this.valor = valor;
//		}
//
//		public Registro() {
//
//		}
//
//	}

}
