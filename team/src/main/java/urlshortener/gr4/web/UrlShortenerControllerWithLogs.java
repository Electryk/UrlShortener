package urlshortener.gr4.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;

import javax.servlet.http.HttpServletRequest;

import urlshortener.common.domain.ShortURL;
import urlshortener.common.web.UrlShortenerController;

@RestController
public class UrlShortenerControllerWithLogs extends UrlShortenerController {

	private static final Logger logger = LoggerFactory.getLogger(UrlShortenerControllerWithLogs.class);

	@Override
	@RequestMapping(value = "/{id:(?!link|index).*}", method = RequestMethod.GET)
	public ResponseEntity<?> redirectTo(@PathVariable String id, HttpServletRequest request) {
		logger.info("Requested redirection with hash " + id);
		return super.redirectTo(id, request);
	}

	@Override
	public ResponseEntity<ShortURL> shortener(@RequestParam("url") String url,
											  @RequestParam(value = "sponsor", required = false) String sponsor,
											  HttpServletRequest request) {
		logger.info("Requested new short for uri " + url);
		logger.info("IP CLIENT - " + request.getRemoteAddr());
		getLatLngByIp(request.getRemoteAddr());
		return super.shortener(url, sponsor, request);
	}
	
	private void getLatLngByIp(String ip) {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("location/GeoLiteCity.dat").getFile());
		try {
	
			LookupService lookup = new LookupService(file, LookupService.GEOIP_MEMORY_CACHE);
			Location locationServices = lookup.getLocation("155.210.224.201");
			
			logger.info(ip);
			logger.info("" + locationServices);
			if (locationServices != null) {
				logger.info(String.valueOf(locationServices.latitude));
				logger.info(String.valueOf(locationServices.longitude));
				logger.info(locationServices.city);
				logger.info(locationServices.countryName);
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
