package com.graficothymeleaf.app.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.graficothymeleaf.app.util.WebClientSelic;

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

		JSONArray ret = selic.buscaSerieTxSelicUltimosValores(500);

		List<Object> regis = new ArrayList<>();

		regis.add(new Object[] { "Data", "Valor" });

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

	public class Registro {

		private String data;
		private BigDecimal valor;

		public String getMesAno() {
			return data;
		}

		public void setMes(String data) {
			this.data = data;
		}

		public BigDecimal getValor() {
			return valor;
		}

		public void setValor(BigDecimal valor) {
			this.valor = valor;
		}

		public Registro(String data, BigDecimal valor) {
			this.data = data;
			this.valor = valor;
		}

		public Registro() {

		}

	}

}
