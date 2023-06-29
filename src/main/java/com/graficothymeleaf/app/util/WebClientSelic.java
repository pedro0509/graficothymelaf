package com.graficothymeleaf.app.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebClientSelic {

	private final String URL_BC = "https://api.bcb.gov.br/dados/serie/bcdata.sgs.11/dados";

	private static Logger log = LoggerFactory.getLogger(WebClientSelic.class);

	DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public JSONArray buscaSerieTxSelicPeriodo(LocalDate dtInicial, LocalDate dtFim) throws IOException {

		OkHttpClient client = new OkHttpClient();

		StringBuilder parametros = new StringBuilder();

		parametros.append("dataInicial=" + dtInicial.format(formatters) + "&");
		parametros.append("dataFinal=" + dtInicial.format(formatters) + "&");

		String url = URL_BC + "?formato=json&" + parametros.toString();

		Request request = new Request.Builder().url(url).get().addHeader("Content-Type", "application/json")
				.addHeader("cache-control", "no-cache").build();

		Response response = client.newCall(request).execute();

		if (response.code() != 200) {
			Log logger = LogFactory.getLog(getClass());
			logger.error(response.toString());
			return null;
		} else
			return new JSONArray(response.body().string());
	}

	public JSONArray buscaSerieTxSelicUltimosValores(int quantidade) throws IOException {

		OkHttpClient client = new OkHttpClient();

		String url = URL_BC + "/ultimos/" + quantidade + "?formato=json";

		Request request = new Request.Builder().url(url).get().addHeader("Content-Type", "application/json")
				.addHeader("cache-control", "no-cache").build();

		Response response = client.newCall(request).execute();

		if (response.code() != 200) {
			Log logger = LogFactory.getLog(getClass());
			logger.error(response.toString());
			return null;
		} else
			return new JSONArray(response.body().string());
	}

}
