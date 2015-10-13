package org.jasig.cas;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class RESTfulTest {

	private static final Logger LOGGER = Logger.getLogger(RESTfulTest.class
			.getName());
	private static final String USERNAME = "18675559296";
	private static final String PASSWORD = "zhaokai";
	private static final String CAS_URL = "https://sso.kingdee.com/v1/tickets";

	//@Test
	public void testLogin() throws Exception {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("username", USERNAME);
		params.put("password", PASSWORD);
		LOGGER.info("Post Params:" + params.toString());
		LOGGER.info("Post Response:" + excutePost(CAS_URL, params.toString()));
	}

	@Test
	public void testLoginWithSpring() throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(CAS_URL);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("username", USERNAME));
		nvps.add(new BasicNameValuePair("password", PASSWORD));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		CloseableHttpResponse response2 = httpclient.execute(httpPost);

		try {
			LOGGER.info(response2.getStatusLine().toString());
			LOGGER.info("TTT_____+====");
			HttpEntity entity2 = response2.getEntity();
			EntityUtils.consume(entity2);
		} finally {
			response2.close();
		}
	}

	public static String excutePost(String targetURL, String urlParameters) {
		URL url;
		HttpURLConnection connection = null;
		try {
			// Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();

		} catch (Exception e) {

			e.printStackTrace();
			return null;

		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

}
