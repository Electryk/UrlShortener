package urlshortener.common.domain.googlesafebrowsing;


import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
	"clientId",
	"clientVersion"
})
public class Client {

	@JsonProperty("clientId")
	private String clientId;
	@JsonProperty("clientVersion")
	private String clientVersion;
	
	public Client(String clientID, String version)
	{
		this.clientId=clientID;
		this.clientVersion=version;
	}
	/**
	*
	* @return
	* The clientId
	*/
	@JsonProperty("clientId")
	public String getClientId() {
		return clientId;
	}
	
	/**
	*
	* @param clientId
	* The clientId
	*/
	@JsonProperty("clientId")
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	/**
	*
	* @return
	* The clientVersion
	*/
	@JsonProperty("clientVersion")
	public String getClientVersion() {
		return clientVersion;
	}
	
	/**
	*
	* @param clientVersion
	* The clientVersion
	*/
	@JsonProperty("clientVersion")
	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
	