package pm;

import data.Attributes;
import data.PMRequest;
import data.PMResponse;
import data.VelocityStatsList;
import util.Connection;
import util.JSONUtil;
import vfs.TransactionData;

public class PMClient {




    public static PMResponse submit(Attributes attributes)
    {
        // send to scheduler a jar file and client name and get back a job id .

        // System.out.println(profileId);


        PMRequest pmRequest = new PMRequest();
        pmRequest.setAttributes(attributes);
        Connection app = new Connection("https://localhost:8280/");

        String result = app.sendSimple(JSONUtil.toJSON(pmRequest),"verify");

     //   System.out.println(result);

        return (PMResponse) JSONUtil.fromJSON(result,PMResponse.class);

    }


}
