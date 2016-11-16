package urlshortener.common.domain.googlesafebrowsing;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"client",
"threatInfo"
})
public class RequestFormat {

@JsonProperty("client")
private Client client;
@JsonProperty("threatInfo")
private ThreatInfo threatInfo;


	public RequestFormat(Client client, ThreatInfo info)
	{
		this.client=client;
		this.threatInfo=info;
	}

	
	/**
	*
	* @return
	* The client
	*/
	@JsonProperty("client")
	public Client getClient() {
		return client;
	}
	
	/**
	*
	* @param client
	* The client
	*/
	@JsonProperty("client")
	public void setClient(Client client) {
		this.client = client;
	}
	
	/**
	*
	* @return
	* The threatInfo
	*/
	@JsonProperty("threatInfo")
	public ThreatInfo getThreatInfo() {
		return threatInfo;
	}
	
	/**
	*
	* @param threatInfo
	* The threatInfo
	*/
	@JsonProperty("threatInfo")
	public void setThreatInfo(ThreatInfo threatInfo) {
		this.threatInfo = threatInfo;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
