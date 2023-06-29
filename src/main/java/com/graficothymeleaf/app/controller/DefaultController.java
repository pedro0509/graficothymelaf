package com.graficothymeleaf.app.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.graficothymeleaf.app.util.WebClientSelic;

@Controller
public class DefaultController {

	Logger logger = LoggerFactory.getLogger(DefaultController.class);

	@GetMapping("/")
	public ModelAndView inicio(Model model) throws IOException {

		WebClientSelic selic = new WebClientSelic();

		JSONArray ret = selic.buscaSerieTxSelicUltimosValores(500);

		Map<String, Double> graphDataSelic = new TreeMap<>();
		
		try {

			List<Registro> users = new Gson().fromJson(ret.toString(), new TypeToken<List<Registro>>() {
			}.getType());

			users.stream().forEach(limp -> graphDataSelic.put(limp.getMesAno(), limp.getValor()));

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		ModelAndView modelAndView = new ModelAndView("inicio");
		modelAndView.addObject("grafico", graphDataSelic);

		return modelAndView;
	}

	public class Registro {

		private String data;
		private Double valor;

		public String getMesAno() {
			return data;
		}

		public void setMes(String data) {
			this.data = data;
		}

		public Double getValor() {
			return valor;
		}

		public void setValor(Double valor) {
			this.valor = valor;
		}

		public Registro(String data, Double valor) {
			this.data = data;
			this.valor = valor;
		}

		public Registro() {

		}

	}

}
