package data;

public class RequestBuilder {

    RequestHeader requestHeader = new RequestHeader();

    Request request = new Request();

    public RequestBuilder header(String id)
    {
        requestHeader.setRequestId(id);

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

    public Request build()
    {
        return request;
    }

}
