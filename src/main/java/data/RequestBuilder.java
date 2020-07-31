package data;

public class RequestBuilder {

    RequestHeader requestHeader = new RequestHeader();

    CCVIdRequest request = new CCVIdRequest();

    public RequestBuilder header(String id)
    {
        requestHeader.setRequestId(id);

        request.setRequestHeader(requestHeader);

        return this;
    }


    public RequestBuilder workflowId(String id)
    {
        request.setWorkflowId(id);

        return this;
    }

    public RequestBuilder attributes(Attributes attributes)
    {
        request.setAttributes(attributes);

        return this;
    }

    public CCVIdRequest build()
    {
        return request;
    }

}
