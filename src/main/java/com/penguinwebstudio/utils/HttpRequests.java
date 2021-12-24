package com.penguinwebstudio.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequests {

	public static String postJSON(String url, String data) throws Exception {
		URL requestUrl = new URL(url);
		byte[] dataInBytes = data.toString().getBytes("UTF-8");
		HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(dataInBytes.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(dataInBytes);
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		StringBuilder stringBuilder = new StringBuilder();
		for (int c; (c = reader.read()) >= 0;) {
			stringBuilder.append((char) c);
		}
		String json = stringBuilder.toString();
		return json;
	}
	
}
