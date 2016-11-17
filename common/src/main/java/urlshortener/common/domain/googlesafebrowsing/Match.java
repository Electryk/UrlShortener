package urlshortener.common.domain.googlesafebrowsing;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Generated("org.jsonschema2pojo")
	@JsonPropertyOrder({
		"threatType",
		"platformType",
		"threat",
		"cacheDuration",
		"threatEntryType"
	})
	public class Match {
	
	@JsonProperty("threatType")
	private String threatType;
	@JsonProperty("platformType")
	private String platformType;
	@JsonProperty("threat")
	private Threat threat;
	@JsonProperty("cacheDuration")
	private String cacheDuration;
	@JsonProperty("threatEntryType")
	private String threatEntryType;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
	/**
	*
	* @return
	* The threatType
	*/
	@JsonProperty("threatType")
	public String getThreatType() {
		return threatType;
	}
	
	/**
	*
	* @param threatType
	* The threatType
	*/
	@JsonProperty("threatType")
	public void setThreatType(String threatType) {
		this.threatType = threatType;
	}
	
	/**
	*
	* @return
	* The platformType
	*/
	@JsonProperty("platformType")
	public String getPlatformType() {
		return platformType;
	}
	
	/**
	*
	* @param platformType
	* The platformType
	*/
	@JsonProperty("platformType")
	public void setPlatformType(String platformType) {
		this.platformType = platformType;
	}
	
	/**
	*
	* @return
	* The threat
	*/
	@JsonProperty("threat")
	public Threat getThreat() {
		return threat;
	}
	
	/**
	*
	* @param threat
	* The threat
	*/
	@JsonProperty("threat")
	public void setThreat(Threat threat) {
		this.threat = threat;
	}
	
	/**
	*
	* @return
	* The cacheDuration
	*/
	@JsonProperty("cacheDuration")
	public String getCacheDuration() {
		return cacheDuration;
	}
	
	/**
	*
	* @param cacheDuration
	* The cacheDuration
	*/
	@JsonProperty("cacheDuration")
	public void setCacheDuration(String cacheDuration) {
		this.cacheDuration = cacheDuration;
	}
	
	/**
	*
	* @return
	* The threatEntryType
	*/
	@JsonProperty("threatEntryType")
	public String getThreatEntryType() {
		return threatEntryType;
	}
	
	/**
	*
	* @param threatEntryType
	* The threatEntryType
	*/
	@JsonProperty("threatEntryType")
	public void setThreatEntryType(String threatEntryType) {
		this.threatEntryType = threatEntryType;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}
	
	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}
}