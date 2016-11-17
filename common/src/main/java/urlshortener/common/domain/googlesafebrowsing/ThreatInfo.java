package urlshortener.common.domain.googlesafebrowsing;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
	"threatTypes",
	"platformTypes",
	"threatEntryTypes",
	"threatEntries"
})
public class ThreatInfo {
	
	@JsonProperty("threatTypes")
    private String[] threatTypes= new String []{"MALWARE", "SOCIAL_ENGINEERING", "UNWANTED_SOFTWARE", "POTENTIALLY_HARMFUL_APPLICATION"};
	@JsonProperty("platformTypes")
    private String[] platformTypes = new String [] {"WINDOWS", "LINUX", "ANDROID", "OSX", "IOS", "ANY_PLATFORM", "ALL_PLATFORMS", "CHROME"};
	@JsonProperty("threatEntryTypes")
    private String[] threatEntryTypes = new String [] {"URL"};
	@JsonProperty("threatEntries")
	private ArrayList<ThreatEntry> threatEntries = new ArrayList<ThreatEntry>();
	

	/**
	*
	* @return
	* The threatTypes
	*/
	
	public ThreatInfo(ThreatEntry entry)
	{
		threatEntries.add(entry);
	}
	
	@JsonProperty("threatTypes")
	public String[] getThreatTypes() {
		return threatTypes;
	}
	
	/**
	*
	* @param threatTypes
	* The threatTypes
	*/
	@JsonProperty("threatTypes")
	public void setThreatTypes(String[] threatTypes) {
		this.threatTypes = threatTypes;
	}
	
	/**
	*
	* @return
	* The platformTypes
	*/
	@JsonProperty("platformTypes")
	public String[] getPlatformTypes() {
		return platformTypes;
	}
	
	/**
	*
	* @param platformTypes
	* The platformTypes
	*/
	@JsonProperty("platformTypes")
	public void setPlatformTypes(String[] platformTypes) {
		this.platformTypes = platformTypes;
	}
	
	/**
	*
	* @return
	* The threatEntryTypes
	*/
	@JsonProperty("threatEntryTypes")
	public String[] getThreatEntryTypes() {
		return threatEntryTypes;
	}
	
	/**
	*
	* @param threatEntryTypes
	* The threatEntryTypes
	*/
	@JsonProperty("threatEntryTypes")
	public void setThreatEntryTypes(String[] threatEntryTypes) {
		this.threatEntryTypes = threatEntryTypes;
	}
	
	/**
	*
	* @return
	* The threatEntries
	*/
	@JsonProperty("threatEntries")
	public ArrayList<ThreatEntry> getThreatEntries() {
		return threatEntries;
	}
	
	/**
	*
	* @param threatEntries
	* The threatEntries
	*/
	@JsonProperty("threatEntries")
	public void setThreatEntries(ArrayList<ThreatEntry> threatEntries) {
		this.threatEntries = threatEntries;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}