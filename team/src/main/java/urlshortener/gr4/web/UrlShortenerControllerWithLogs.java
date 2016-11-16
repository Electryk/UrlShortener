package urlshortener.gr4.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.URI;
import java.sql.Date;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import urlshortener.common.domain.Click;
import urlshortener.common.domain.Location;
import urlshortener.common.domain.ShortURL;
import urlshortener.common.repository.ClickRepository;
import urlshortener.common.repository.LocationRepository;
import urlshortener.common.web.UrlShortenerController;

@RestController
public class UrlShortenerControllerWithLogs extends UrlShortenerController {

	private static final Logger logger = LoggerFactory.getLogger(UrlShortenerControllerWithLogs.class);
	
	@Autowired
	protected LocationRepository locationRepository;

	@Override
	@RequestMapping(value = "/{id:(?!link|index).*}", method = RequestMethod.GET)
	public ResponseEntity<?> redirectTo(@PathVariable String id, HttpServletRequest request) {
		logger.info("Requested redirection with hash " + id);
		ResponseEntity<?> shortURL =  super.redirectTo(id, request);
		
		String ip = request.getRemoteAddr();
		logger.info("IP := " + ip);
		
		//IP PUBLICA DE PRUEBA = 155.210.211.33
		JSONObject locationIp = getLocationByIP(request.getRemoteAddr());
		//JSONObject locationIp = getLocationByIP(request.getRemoteAddr());
		
		createAndSaveLocation(id, locationIp);
		return shortURL;
	}

	@Override
	public ResponseEntity<ShortURL> shortener(@RequestParam("url") String url,
											  @RequestParam(value = "sponsor", required = false) String sponsor,
											  HttpServletRequest request) {
		logger.info("Requested new short for uri " + url);
		return super.shortener(url, sponsor, request);
	}
	
	public JSONObject getLocationByIP(String ip) {
		JSONObject obj = null;
		try {
			//Get LAT, LNG and other info associated with this ip.
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.getForEntity(
			        "http://ip-api.com/json/" + ip, String.class);
			
			//Save the result in JSON Object
			obj = new JSONObject(response.getBody());
			obj.put("ip", ip);
			logger.info(obj.toString());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return obj;
		
	}
	
	private void createAndSaveLocation(String hash, JSONObject locationIp) {
		try {
			Location location = null;
			if (locationIp.getString("status").compareTo("success") == 0) {
				location = new Location(hash, locationIp.getString("city"), locationIp.getString("country"),
						locationIp.getString("lat"), locationIp.getString("lon"), locationIp.getString("ip"),
						locationIp.getString("regionName"), locationIp.getString("org"), null);
				
				logger.info("INFO IP := " + location.toString());
			} else {
				location = new Location(hash, null, "private range",
						null, null, locationIp.getString("ip"),
						null, null, null);
			}
			location=locationRepository.save(location);
			logger.info(location!=null?"["+hash+"] saved with id ["+location.getId()+" in Location]":"["+hash+"] was not saved");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
