package locator.servcomm;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
//Call with (new ClientThread()).execute(new String("blasdas"));
public class ClientThread extends AsyncTask<String,Void,String> {
    private Context myContext;
    private boolean exists = false;
    private Socket sock;
    private PrintWriter writer;
    private DataInputStream is;
    private SocketAddress sockaddr;
    private int timeoutMs = 2000;   // 2 seconds

    @Override
    protected String doInBackground(String... v){
        String resultStr = "";
        try{
            sockaddr = new InetSocketAddress(Constants.SRV_IP,Constants.SRV_PORT);
            sock = new Socket();
            sock.connect(sockaddr, timeoutMs);
            writer = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
            is = new DataInputStream(sock.getInputStream());


            //signUp("nicolas","password");
            authenticate("nicolas","password");
            makeEvent("1","nome do evento","30","uma coisa louca la","2014-12-25 14:00","2 hours","true");
            //Authenticate
            //authenticate("rafael","password");
            //authenticate("pangare", "password");
            //makeEvent("1","nome do evento","30","uma coisa louca la","2014-12-25 14:00","2 hours","true");
            //getEvents("pangare");
            //unSubscribeEvent("deathadder","3");
            //updateEvent("3","1","evento do gerson","30","uma coisa louca la","2014-11-27 14:00","2 hours","true");
            //fetch

            //
            System.out.println("Authentication successful.");



            exists = true;
            writer.close();
            is.close();
            sock.close();
            resultStr = "Great success!";
        }
        catch(Exception e){
            System.out.println("Exception " + e);
        }
        return resultStr;
    }

    public boolean authenticate(String user, String password) throws Exception{
            /*
            Request schema
            {
                type : "AUTH"
                username : "John Doe"
                password : "password"
            }

            Response schema
            {
                response : "HELLO"/ "ERROR"
                message : "???"
            }
            */
        //Forming the message...
        JSONObject query = new JSONObject();
        query.put("type",Constants.ctAuthMsg);
        query.put("username", user);
        query.put("password", password);

        //Sending it away...
        sendMsgToServer(query.toString());
        System.out.println("Sent auth JSON, awaiting response.");

        //Now we wait for a response from the server...
        JSONObject response = new JSONObject(recvMsgFromServer());
        String responseType = response.get("response").toString();
        String responseMessage = response.get("message").toString();

        if(responseType.equals(Constants.ctHelloMsg)){
            System.out.println("Received " + Constants.ctHelloMsg + ", we can move with our application.");
        }
        else{
            System.out.println("Error: error returned - {(" + responseType + "),(" + responseMessage + ")}...");
            return false;
        }
        return true;
    }
    public boolean signUp(String user, String password) throws Exception{
            /*
            Request schema
            {
                type : "SIGNUP"
                username : "John Doe"
                password : "password"
            }

            Response schema
            {
                response : "OK"/"ERROR"
                message : "???"
            }
            */
        //Forming the message...
        JSONObject query = new JSONObject();
        query.put("type",Constants.ctSignUpMsg);
        query.put("username", user);
        query.put("password", password);

        //Sending it away...
        sendMsgToServer(query.toString());
        System.out.println("Sent signUp JSON, awaiting response.");

        //Now we wait for a response from the server...
        JSONObject response = new JSONObject(recvMsgFromServer());
        String responseType = response.get("response").toString();
        String responseMessage = response.get("message").toString();

        if(responseType.equals(Constants.ctOkMsg)){
            System.out.println("Received " + Constants.ctOkMsg + ", successfully signed up!");
        }
        else{
            System.out.println("Error: error returned - {(" + responseType + "),(" + responseMessage + ")}...");
            return false;
        }
        return true;
    }
    public boolean getEvents(String username) throws Exception{
            /*
            Request schema
            {
                type : "GET"
                username : "John Doe"
            }
            Response schema
            {
                response : "OK"
vector with

ID : "1/2/3/4/5/6/..."
roomID : "0/1/2/3...",
name : "INF Sistemas Embarcados",
capacity : "1024",
description : "",
timestamp : "",
duration : "",
private : ""
}
            }
            */
        //Forming the message...
        JSONObject query = new JSONObject();
        query.put("type",Constants.ctGetMsg);
        query.put("username", username);

        //Sending it away...
        sendMsgToServer(query.toString());
        System.out.println("Sent GET JSON, awaiting response.");

        //Now we wait for a response from the server...
        JSONObject response = new JSONObject(recvMsgFromServer());
        String responseType = response.get("response").toString();

        if(responseType.equals(Constants.ctOkMsg)){
            System.out.println("Received " + Constants.ctOkMsg + ", messages fetched.");
            System.out.println("List of events: \n" + response.get("list_events").toString());
        }
        else{
            System.out.println("Error: error returned - {(" + responseType + "),(" + response.get("message") + ")}...");
            return false;
        }
        return true;
    }
    public boolean subscribeEvent(String username, String eventID) throws Exception{
            /*
            Request schema
            {
                type : "SUBSCRIBE_EVENT",
                username : "John Doe",
                eventID : "1"
            }
            Response schema
            {
                response : "OK/ERROR"
                message : "???"
            }
            */
        //Forming the message...
        JSONObject query = new JSONObject();
        query.put("type",Constants.ctSubsEventMsg);
        query.put("username", username);
        query.put("eventID",eventID);

        //Sending it away...
        sendMsgToServer(query.toString());
        System.out.println("Sent SUBSCRIBE_EVENT JSON, awaiting response.");

        //Now we wait for a response from the server...
        JSONObject response = new JSONObject(recvMsgFromServer());
        String responseType = response.get("response").toString();

        if(responseType.equals(Constants.ctOkMsg)){
            System.out.println("Received " + Constants.ctOkMsg + ", messages fetched.");
            System.out.println("Message: " + response.get("message").toString());
        }
        else{
            System.out.println("Error: error returned - {(" + responseType + "),(" + response.get("message") + ")}...");
            return false;
        }
        return true;
    }

    public boolean unSubscribeEvent(String username, String eventID) throws Exception{
            /*
            Request schema
            {
                type : "UNSUBSCRIBE_EVENT",
                username : "John Doe",
                eventID : "1"
            }
            Response schema
            {
                response : "OK/ERROR"
                message : "???"
            }
            */
        //Forming the message...
        JSONObject query = new JSONObject();
        query.put("type",Constants.ctUnsubsEventMsg);
        query.put("username", username);
        query.put("eventID",eventID);

        //Sending it away...
        sendMsgToServer(query.toString());
        System.out.println("Sent UNSUBSCRIBE_EVENT JSON, awaiting response.");

        //Now we wait for a response from the server...
        JSONObject response = new JSONObject(recvMsgFromServer());
        String responseType = response.get("response").toString();

        if(responseType.equals(Constants.ctOkMsg)){
            System.out.println("Received " + Constants.ctOkMsg + ", messages fetched.");
            System.out.println("Message: " + response.get("message").toString());
        }
        else{
            System.out.println("Error: error returned - {(" + responseType + "),(" + response.get("message") + ")}...");
            return false;
        }
        return true;
    }


    //TODO: consider the possibility of making events...
    public boolean makeEvent(String roomID, String ev_n, String capacity, String description, String timestamp, String duration, String priv) throws Exception{

            /*
            * {
type : "MAKE_EVENT",
roomID : "0/1/2/3...",
name : "INF Sistemas Embarcados",
capacity : "1024",
description : "",
timestamp : "",
duration : "",
private : ""
}



3. Resposta
{
response : "OK/ERROR"
message : "Sdadiasdia"
}
            * */
        //Forming the message...
        JSONObject query = new JSONObject();
        query.put("type",Constants.ctMakeEventMsg);
        query.put("roomID", roomID);
        query.put("name", ev_n);
        query.put("capacity", capacity);
        query.put("description", description);
        query.put("timestamp", timestamp);
        query.put("duration", duration);
        query.put("private", priv);

        //Sending it away...
        writer.println(query.toString());
        writer.flush();
        System.out.println("Sent makeEvent JSON, awaiting response.");

        //Now we wait for a response from the server...
        JSONObject response = new JSONObject(is.readLine());

        String responseType = response.get("response").toString();
        String responseMessage = response.get("message").toString();

        if(responseType.equals(Constants.ctOkMsg)){
            System.out.println("Received " + Constants.ctOkMsg + ", event registered.");
        }
        else{
            System.out.println("Error: error returned - {(" + responseType + "),(" + responseMessage + ")}...");
            return false;
        }
        return true;
    }


    public boolean updateEvent(String ID, String roomID, String ev_n, String capacity, String description, String timestamp, String duration, String priv) throws Exception{

            /*
            * {
type : "MAKE_EVENT",
roomID : "0/1/2/3...",
name : "INF Sistemas Embarcados",
capacity : "1024",
description : "",
timestamp : "",
duration : "",
private : ""
}



3. Resposta
{
response : "OK/ERROR"
message : "Sdadiasdia"
}
            * */
        //Forming the message...
        JSONObject query = new JSONObject();
        query.put("type",Constants.ctUpdateEventMsg);
        query.put("ID",ID);
        query.put("roomID", roomID);
        query.put("name", ev_n);
        query.put("capacity", capacity);
        query.put("description", description);
        query.put("timestamp", timestamp);
        query.put("duration", duration);
        query.put("private", priv);

        //Sending it away...
        writer.println(query.toString());
        writer.flush();
        System.out.println("Sent updateEvent JSON, awaiting response.");

        //Now we wait for a response from the server...
        JSONObject response = new JSONObject(is.readLine());

        String responseType = response.get("response").toString();
        String responseMessage = response.get("message").toString();

        if(responseType.equals(Constants.ctOkMsg)){
            System.out.println("Received " + Constants.ctOkMsg + ", event updated successfully.");
        }
        else{
            System.out.println("Error: error returned - {(" + responseType + "),(" + responseMessage + ")}...");
            return false;
        }
        return true;
    }
        /*
        public boolean alterEvent(String user, String password) throws Exception{
            //Forming the message...
            JSONObject query = new JSONObject();
            query.put("type","AUTH");
            query.put("username", user);
            query.put("password", password);

            //Sending it away...
            writer.println(query.toString());
            writer.flush();
            System.out.println("Sent auth JSON, awaiting response.");

            //Now we wait for a response from the server...
            JSONObject response = new JSONObject(is.readLine());

            String responseType = response.get("response").toString();
            String responseMessage = response.get("message").toString();

            System.out.println("Response: " + responseType + "; Message: " + responseMessage);

            if(responseType.equals(Constants.ctHelloMsg)){
                System.out.println("Received " + Constants.ctHelloMsg + ", we can move on to update our application.");
            }
            return true;
        }*/

    public void sendMsgToServer(String msg) throws Exception{
        writer.println(msg);
        writer.flush();
    }

    public String recvMsgFromServer() throws Exception{
        return is.readLine();
    }

    @Override
    protected void onPostExecute(String result){
        Toast.makeText(myContext,result + " resultado " + exists,100).show();
    }

}
