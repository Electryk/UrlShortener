package urlshortener.common.domain.googlesafebrowsing;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"url"
})
public class ThreatEntry {

	@JsonProperty("url")
	private String url;

	public ThreatEntry(String url)
	{
		this.url=url;
	}

	/**
	*
	* @return
	* The url
	*/
	@JsonProperty("url")
	public String getUrl() {
		return url;
	}
	
	/**
	*
	* @param url
	* The url
	*/
	@JsonProperty("url")
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
