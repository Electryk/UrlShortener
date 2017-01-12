package urlshortener.gr4.geoIpLocation;

import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import urlshortener.common.domain.Location;
import urlshortener.common.repository.LocationRepository;

public class LocationIp {
	
	/**
	 * This function is the construct of the object associated with the geoLocation functionality.
	 */
	public LocationIp() {}
	
	/**
	 * This function connect with the location api, process the api's response and save
	 * this info in the data base.
	 */
	public void saveLocation(String ip, String hash, LocationRepository locationRepository) {
		JSONObject locationIp = getLocationByIP(ip);
		createAndSaveLocation(hash, locationIp, locationRepository);
	}
	
	/**
	 * This fucntion get an ArrayList of all Locations associated to <pattern> 
	 * between <dateInit> and <dateEnd>
	 */
	public List<Location> getLocationsByHash(String pattern, Long dateInit, Long dateEnd, 
			LocationRepository locationRepository) {
		
		//Convert <dateInit> and <dateEnd> to Timestamps
		Timestamp initDate = new Timestamp(dateInit);
		Timestamp endDate = new Timestamp(dateEnd);
		
		return locationRepository.listByPattern(pattern, initDate, endDate);
	}
	
	/**
	 * This function get a location associated to <ip> and return
	 * a JSONObject with the ip's location.
	 */
	public JSONObject getLocationByIP(String ip) {
		JSONObject obj = null;
		try {
			//Get LAT, LNG and other info associated with this ip.
			if (ip.compareTo("127.0.0.1") != 0) {
				RestTemplate restTemplate = new RestTemplate();
				ResponseEntity<String> response = 
					restTemplate.getForEntity("http://localhost:9000/" + ip, String.class);				

				//Save the result in JSON Object
				obj = new JSONObject(response.getBody());
				obj.put("ip", ip);
			} else {
				Timestamp date = new Timestamp(System.currentTimeMillis());
				String privateRangeIp = "{\"ip\":\"127.0.0.1\",\"dateTime\":\"" + date + "\",\"country\":\"private range\"}";
				
				//Save the result in JSON Object
				obj = new JSONObject(privateRangeIp);
				obj.put("ip", ip);
			}

			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return obj;
	}
	
	/**
	 * This function creates a new object location and save one in the spring's data base.
	 */
	public void createAndSaveLocation(String hash, JSONObject locationIp, LocationRepository locationRepository) {
		try {
			Location location = null;
			
			/*
			 * Distinct to IP in public range or IP in private range. The response of the Location API doesn't
			 * equals in public and private ranges.
			 */
			if (locationIp.getString("country").compareTo("private range") != 0) {
				//Public range
				location = new Location(hash, locationIp.getString("city"), locationIp.getString("country"),
					locationIp.getString("lat"), locationIp.getString("lon"), locationIp.getString("ip"),
					locationIp.getString("regionName"), locationIp.getString("org"), null, 
					new Timestamp(System.currentTimeMillis()));
				
			} else {
				//Private range
				location = new Location(hash, null, "private range",
					null, null, locationIp.getString("ip"),
					null, null, null, new Timestamp(System.currentTimeMillis()));
			}
			
			//Save the location in the data base.
			location = locationRepository.save(location);
			
			logger.info(location!=null?"["+hash+"] saved with id [" + location.getId() 
				+ " in Location]" : "[" + hash + "] was not saved");
			logger.info(location.toString());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This fucntion get an ArrayList of all Locations associated to area 
	 * of a circle which radious is formated by center and limitPoint.
	 */
	public List<Location> getCorrectListByArea(List<Location> list, String center, String limitPoint) {
		try {
			JSONObject centerObj = new JSONObject(center);
			JSONObject limitPointObj = new JSONObject(limitPoint);

			//Calculate radious of two points.
			double x = limitPointObj.getDouble("lat") - centerObj.getDouble("lat");
			double y = limitPointObj.getDouble("lng") - centerObj.getDouble("lng");
			double radious = Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0));
			
			List<Location> listAux = new ArrayList<Location>();

			for (int i = 0; i < list.size(); i++) {
				//Calculate the radious
				String lat = list.get(i).getLatitude();
				String lng = list.get(i).getLongitude();

				if (lat != null && lng != null) {
					Double pointX = Double.parseDouble(lat);
					Double pointY = Double.parseDouble(lng);
					x = pointX - centerObj.getDouble("lat");
					y = pointY - centerObj.getDouble("lng");

					Double radiousIp = Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0));

					if (radiousIp <= radious) {
						listAux.add(list.get(i));
					}
				}
			}

			list = listAux;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}
}
