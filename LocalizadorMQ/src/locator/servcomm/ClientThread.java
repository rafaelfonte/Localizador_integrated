package locator.servcomm;

import android.app.ProgressDialog;
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
public class ClientThread extends AsyncTask<String,Void,String[]> {
    private Context myContext;
    private boolean exists = false;
    private Socket sock;
    private PrintWriter writer;
    private DataInputStream is;
    private SocketAddress sockaddr;
    private int timeoutMs = 2000;   // 2 seconds
    private int actionToPerform;
    private String [] act_args;


    private ProgressDialog dialog;

    public ClientThread(Context cx, int action, String args[]){
        myContext = cx;
        actionToPerform = action;
        act_args = args;
    }

    @Override
    protected void onPreExecute(){
        this.dialog = new ProgressDialog(myContext);
        switch(actionToPerform){
            case Constants.ctSignUpAct:
                this.dialog.setMessage("Signing up...");
                break;
            case Constants.ctAuthAct:
                this.dialog.setMessage("Authenticating...");
                break;
            case Constants.ctGetAct:
                this.dialog.setMessage("Fetching events...");
                break;
            case Constants.ctSubsEventAct:
                this.dialog.setMessage("Subscribing...");
                break;
            case Constants.ctUnsubsEventAct:
                this.dialog.setMessage("Unsubscribing...");
                break;
            case Constants.ctMakeEventAct:
                this.dialog.setMessage("Making event...");
                break;
            case Constants.ctUpdateEventAct:
                this.dialog.setMessage("Updating event...");
                break;
            case Constants.ctGetRoomsAct:
                this.dialog.setMessage("Fetching room info...");
            default:

        }
        this.dialog.show();
    }
    @Override
    protected String[] doInBackground(String... v){
        String [] resultStr = null;
        try{
            sockaddr = new InetSocketAddress(Constants.SRV_IP,Constants.SRV_PORT);
            sock = new Socket();
            sock.connect(sockaddr, timeoutMs);
            writer = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
            is = new DataInputStream(sock.getInputStream());

            System.out.println();
            switch(actionToPerform){
                case Constants.ctSignUpAct:
                    System.out.println("Signing up...");
                    resultStr = signUp(act_args[0],act_args[1]);
                    break;
                case Constants.ctAuthAct:
                    System.out.println("Authenticating...");
                    resultStr = authenticate(act_args[0],act_args[1]);
                    break;
                case Constants.ctGetAct:
                    System.out.println("Fetching events...");
                    resultStr = authenticate(act_args[0],act_args[1]);
                    if(resultStr[0].equals(Constants.ctHelloMsg))
                        resultStr = getEvents(act_args[0]);
                    break;
                case Constants.ctSubsEventAct:
                    System.out.println("Subscribing...");
                    resultStr = authenticate(act_args[0],act_args[1]);
                    if(resultStr[0].equals(Constants.ctHelloMsg))
                        resultStr = subscribeEvent(act_args[0],act_args[2]);
                    break;
                case Constants.ctUnsubsEventAct:
                    System.out.println("Unsubscribing...");
                    resultStr = authenticate(act_args[0],act_args[1]);
                    if(resultStr[0].equals(Constants.ctHelloMsg))
                        resultStr = unSubscribeEvent(act_args[0],act_args[2]);
                    break;
                case Constants.ctMakeEventAct:
                    System.out.println("Making event...");
                    resultStr = authenticate(act_args[0],act_args[1]);
                    if(resultStr[0].equals(Constants.ctHelloMsg))
                        resultStr = makeEvent(act_args[2],act_args[3],act_args[4],act_args[5],act_args[6],act_args[7],act_args[8]);
                    break;
                case Constants.ctUpdateEventAct:
                    System.out.println("Updating event...");
                    resultStr = authenticate(act_args[0],act_args[1]);
                    if(resultStr[0].equals(Constants.ctHelloMsg))
                        resultStr = updateEvent(act_args[2],act_args[3],act_args[4],act_args[5],act_args[6],act_args[7],act_args[8],act_args[9]);
                    break;
                case Constants.ctGetRoomsAct:
                    System.out.println("Fetching rooms...");
                    resultStr = authenticate(act_args[0],act_args[1]);
                    if(resultStr[0].equals(Constants.ctHelloMsg))
                        resultStr = getRooms(act_args[0]);
                    break;
                case Constants.ctGetUsersAct:
                    System.out.println("Fetching users...");
                    resultStr = authenticate(act_args[0],act_args[1]);
                    if(resultStr[0].equals(Constants.ctHelloMsg))
                        resultStr = getUsers(act_args[0]);
                    break;
                default:

            }
            writer.close();
            is.close();
            sock.close();
        }
        catch(Exception e){
            System.out.println("Exception " + e);
        }
        return resultStr;
    }

    public String[] authenticate(String user, String password) throws Exception{
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
            return new String[]{Constants.ctErrorMsg,responseMessage};
        }
        return new String[]{Constants.ctHelloMsg,responseMessage};
    }
    public String [] signUp(String user, String password) throws Exception{
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
            return new String[]{Constants.ctErrorMsg,responseMessage};
        }
        return new String[]{Constants.ctOkMsg,responseMessage};
    }
    public String[] getEvents(String username) throws Exception{
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
        String subsList = "";

        if(responseType.equals(Constants.ctOkMsg)){
            System.out.println("Received " + Constants.ctOkMsg + ", messages fetched.");
            System.out.println("List of events: \n" + response.get("list_events").toString());
            subsList = response.getString("subs_list");
        }
        else{
            System.out.println("Error: error returned - {(" + responseType + "),(" + response.get("message") + ")}...");
            return new String[]{Constants.ctErrorMsg,response.get("message").toString()};
        }
        return new String[]{Constants.ctOkMsg,response.get("list_events").toString(),subsList};
    }
    public String[] subscribeEvent(String username, String eventID) throws Exception{
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
            return new String[]{Constants.ctErrorMsg,response.get("message").toString()};
        }
        return new String[]{Constants.ctOkMsg,response.get("message").toString()};
    }

    public String[] unSubscribeEvent(String username, String eventID) throws Exception{
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
            return new String[]{Constants.ctErrorMsg,response.get("message").toString()};
        }
        return new String[]{Constants.ctOkMsg,response.get("message").toString()};
    }


    //TODO: consider the possibility of making events...
    public String[] makeEvent(String roomID, String ev_n, String capacity, String description, String timestamp, String duration, String priv) throws Exception{

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
            return new String[]{Constants.ctErrorMsg,response.get("message").toString()};
        }
        return new String[]{Constants.ctOkMsg,response.get("message").toString()};
    }


    public String[] updateEvent(String ID, String roomID, String ev_n, String capacity, String description, String timestamp, String duration, String priv) throws Exception{

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
            return new String[]{Constants.ctErrorMsg,response.get("message").toString()};
        }
        return new String[]{Constants.ctOkMsg,response.get("message").toString()};
    }
    public String[] getRooms(String username) throws Exception{
            /*
            Request schema
            {
                type : "GET_ROOMS"
                username : "John Doe"
            }
            Response schema
            {
                response : "OK"
vector with

roomID : "1/2/3...",
name : "INF Sistemas Embarcados",
description : "",
capacity : "1024",
side : "0/1",
pos : "0-6"
}
            }
            */
        //Forming the message...
        JSONObject query = new JSONObject();
        query.put("type",Constants.ctGetRoomsMsg);
        query.put("username", username);

        //Sending it away...
        sendMsgToServer(query.toString());
        System.out.println("Sent GET JSON, awaiting response.");

        //Now we wait for a response from the server...
        JSONObject response = new JSONObject(recvMsgFromServer());
        String responseType = response.get("response").toString();

        if(responseType.equals(Constants.ctOkMsg)){
            System.out.println("Received " + Constants.ctOkMsg + ", messages fetched.");
            System.out.println("List of rooms: \n" + response.get("list_rooms").toString());
        }
        else{
            System.out.println("Error: error returned - {(" + responseType + "),(" + response.get("message") + ")}...");
            return new String[]{Constants.ctErrorMsg,response.get("message").toString()};
        }
        return new String[]{Constants.ctOkMsg,response.get("list_rooms").toString()};
    }

    public String[] getUsers(String username) throws Exception{
            /*
            Request schema
            {
                type : "GET_ROOMS"
                username : "John Doe"
            }
            Response schema
            {
                response : "OK"
vector with

roomID : "1/2/3...",
name : "INF Sistemas Embarcados",
description : "",
capacity : "1024",
side : "0/1",
pos : "0-6"
}
            }
            */
        //Forming the message...
        JSONObject query = new JSONObject();
        query.put("type",Constants.ctGetUsersMsg);
        query.put("username", username);

        //Sending it away...
        sendMsgToServer(query.toString());
        System.out.println("Sent GET JSON, awaiting response.");

        //Now we wait for a response from the server...
        JSONObject response = new JSONObject(recvMsgFromServer());
        String responseType = response.get("response").toString();

        if(responseType.equals(Constants.ctOkMsg)){
            System.out.println("Received " + Constants.ctOkMsg + ", messages fetched.");
            System.out.println("List of users: \n" + response.get("list_users").toString());
        }
        else{
            System.out.println("Error: error returned - {(" + responseType + "),(" + response.get("message") + ")}...");
            return new String[]{Constants.ctErrorMsg,response.get("message").toString()};
        }
        return new String[]{Constants.ctOkMsg,response.get("list_users").toString()};
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
    protected void onPostExecute(String [] result){
        //Toast.makeText(myContext,result + " resultado " + exists,100).show();
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
        //System.out.println("Resultado : " + result[0]);
        //System.out.println("MyAppContext : " + myContext);
        //Toast.makeText(myContext,result[0],100).show();
    }

}



//signUp("nicolas","password");
//authenticate("nicolas","password");
//makeEvent("1","nome do evento","30","uma coisa louca la","2014-12-25 14:00","2 hours","true");
//Authenticate
//authenticate("rafael","password");
//authenticate("pangare", "password");
//makeEvent("1","nome do evento","30","uma coisa louca la","2014-12-25 14:00","2 hours","true");
//getEvents("pangare");
//unSubscribeEvent("deathadder","3");
//updateEvent("3","1","evento do gerson","30","uma coisa louca la","2014-11-27 14:00","2 hours","true");
//fetch