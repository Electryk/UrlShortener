package urlshortener.gr4;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import urlshortener.gr4.web.UrlShortenerControllerWithLogs;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;
import java.nio.charset.Charset;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= RANDOM_PORT)
@DirtiesContext
public class SystemTests {

	@Value("${local.server.port}")
	private int port = 0;

	@Test
	public void testHome() throws Exception {
		ResponseEntity<String> entity = new TestRestTemplate().getForEntity(
				"http://localhost:" + this.port, String.class);
		assertThat(entity.getStatusCode(), is(HttpStatus.OK));
		//assertThat(entity.getHeaders().getContentType(), is(new MediaType("text", "html", Charset.forName("UTF-8"))));
		assertThat(entity.getBody(), containsString("<title>URL"));
	}

	@Test
	public void testCss() throws Exception {
		ResponseEntity<String> entity = new TestRestTemplate().getForEntity(
				"http://localhost:" + this.port
						+ "/webjars/bootstrap/3.3.5/css/bootstrap.min.css", String.class);
		assertThat(entity.getStatusCode(), is(HttpStatus.OK));
		assertThat(entity.getHeaders().getContentType(), is(MediaType.valueOf("text/css")));
		assertThat(entity.getBody(), containsString("body"));
	}

	@Test
	public void testCreateLink() throws Exception {
		ResponseEntity<String> entity = postLink("http://example.com/");
		assertThat(entity.getStatusCode(), is(HttpStatus.CREATED));
		assertThat(entity.getHeaders().getLocation(), is(new URI("http://localhost:"+ this.port+"/f684a3c4")));
		assertThat(entity.getHeaders().getContentType(), is(new MediaType("application", "json", Charset.forName("UTF-8"))));
		ReadContext rc = JsonPath.parse(entity.getBody());
		assertThat(rc.read("$.hash"), is("f684a3c4"));
		assertThat(rc.read("$.uri"), is("http://localhost:"+ this.port+"/f684a3c4"));
		assertThat(rc.read("$.target"), is("http://example.com/"));
		assertThat(rc.read("$.sponsor"), is(nullValue()));
	}

	@Test
	public void testRedirection() throws Exception {
		postLink("http://example.com/");
		ResponseEntity<String> entity = new TestRestTemplate().getForEntity(
				"http://localhost:" + this.port
						+ "/f684a3c4", String.class);
		assertThat(entity.getStatusCode(), is(HttpStatus.TEMPORARY_REDIRECT));
		assertThat(entity.getHeaders().getLocation(), is(new URI("http://example.com/")));
	}
	
	@Test
	public void testLocationPublicIP() throws Exception {
		//IP PUBLICA DE PRUEBA = 155.210.211.33
		String IP = "155.210.211.33";
		UrlShortenerControllerWithLogs UrlSC = new UrlShortenerControllerWithLogs();
		JSONObject location = UrlSC.getLocationByIP(IP);
		
		assertEquals(location.getString("status"), "success");
		assertEquals(location.has("city"), true);
		assertEquals(location.has("country"), true);
		assertEquals(location.has("lat"), true);
		assertEquals(location.has("lon"), true);
		assertEquals(location.has("ip"), true);
		assertEquals(location.has("regionName"), true);
		assertEquals(location.has("org"), true);
	}
	
	@Test
	public void testLocationPrivateIP() throws Exception {
		//IP PUBLICA DE PRUEBA = 127.0.0.1
		String IP = "127.0.0.1";
		UrlShortenerControllerWithLogs UrlSC = new UrlShortenerControllerWithLogs();
		JSONObject location = UrlSC.getLocationByIP(IP);

		assertEquals(location.getString("status"), "fail");
		assertEquals(location.has("ip"), true);
		assertEquals(location.has("message"), true);
		assertEquals(location.getString("message"), "reserved range");
		assertEquals(location.has("city"), false);
		assertEquals(location.has("country"), false);
		assertEquals(location.has("lat"), false);
		assertEquals(location.has("lon"), false);
		assertEquals(location.has("regionName"), false);
		assertEquals(location.has("org"), false);
	}

	private ResponseEntity<String> postLink(String url) {
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
		parts.add("url", url);
		return new TestRestTemplate().postForEntity(
				"http://localhost:" + this.port+"/link", parts, String.class);
	}



}