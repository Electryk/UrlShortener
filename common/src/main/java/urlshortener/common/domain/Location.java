package urlshortener.common.domain;

public class Location {
	private Long id;
	private String shortURL;
	private String city;
	private String country;
	private String latitude;
	private String longitude;
	private String ip;
	private String regionName;
	private String organization;
	
	public Location(String shortURL, String city, String country, String latitude, String longitude,
			String ip, String regionName, String organization, Long id) {
		this.id = id;
		this.shortURL = shortURL;
		this.city = city;
		this.country = country;
		this.latitude = latitude;
		this.longitude = longitude;
		this.ip = ip;
		this.regionName = regionName;
		this.organization = organization;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getShortURL() {
		return shortURL;
	}

	public void setShortURL(String shortURL) {
		this.shortURL = shortURL;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	@Override
	public String toString() {
		return "Location [id=" + id + ", shortURL=" + shortURL + ", city=" + city + ", country=" + country
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", ip=" + ip + ", regionName=" + regionName
				+ ", organization=" + organization + "]";
	}

}
