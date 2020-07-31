
package data;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "requestHeader",
    "workflowId",
    "attributes"
})
public class CCVIdRequest {

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("requestHeader")
    private RequestHeader requestHeader;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("workflowId")
    private String workflowId;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("attributes")
    private Attributes attributes;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("requestHeader")
    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("requestHeader")
    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("workflowId")
    public String getWorkflowId() {
        return workflowId;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("workflowId")
    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("attributes")
    public Attributes getAttributes() {
        return attributes;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("attributes")
    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
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
