package locator.localizadormq;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Toast;
import locator.servcomm.ClientThread;
import locator.servcomm.Constants;
import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends Activity {

    Context activity_ctx;
    public String username = "";
    public String password = "";

	@Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      final Intent intent = new Intent(this, EnrichedMap.class);

      activity_ctx = this;

      final Button button = (Button) findViewById(R.id.button1);
      button.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
              //Toast.makeText(application_ctx = getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
              username = ((EditText)findViewById(R.id.username_field)).getText().toString();
              password = ((EditText)findViewById(R.id.password_field)).getText().toString();
             
              String[] result_attempt = null;
              //Autenticacao
              try {
                  System.out.println("About to create thread element.");
                  ClientThread auth = new ClientThread(activity_ctx, Constants.ctAuthAct, new String[]{username,password});
                  System.out.println("About to execute thread.");
                  auth.execute();
                  System.out.println("About to run get.");
                  result_attempt = auth.get();
              } catch (Exception e) {
                  Log.e("MainActivity", "An exception occurred during the initial authentication: ", e);
                  //System.out.println("An exception occurred during the initial authentication: " + e);
              }
              if (result_attempt != null){
                  if(result_attempt[0].equals(Constants.ctHelloMsg)) {
                      //Toast.makeText(getApplicationContext(), result_attempt[1], Toast.LENGTH_SHORT).show();
                      load(username,password);
                      startActivity(intent);
                  }
                  else{
                      Toast.makeText(getApplicationContext(), "Authentication failed miserably!", Toast.LENGTH_SHORT).show();
                  }
            }
          }
      });
        final CheckedTextView ctv = (CheckedTextView) findViewById(R.id.fieldSignUp);
        ctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(application_ctx = getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
                String username = ((EditText)findViewById(R.id.username_field)).getText().toString();
                String password = ((EditText)findViewById(R.id.password_field)).getText().toString();
                String[] result_attempt = null;
                //Autenticacao
                try {
                    System.out.println("About to create thread element.");
                    ClientThread auth = new ClientThread(activity_ctx, Constants.ctSignUpAct, new String[]{username,password});
                    System.out.println("About to execute thread.");
                    auth.execute();
                    System.out.println("About to run get.");
                    result_attempt = auth.get();
                } catch (Exception e) {
                    Log.e("MainActivity", "An exception occurred during the initial authentication: ", e);
                    //System.out.println("An exception occurred during the initial authentication: " + e);
                }
                if (result_attempt != null){
                    if(result_attempt[0].equals(Constants.ctOkMsg)) {
                        //Toast.makeText(getApplicationContext(), result_attempt[1], Toast.LENGTH_SHORT).show();
                        load(username,password);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Authentication failed miserably!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
	
	private void load(String user, String pass)
	{
          loadRooms();
	      /*Room roomArray[] = new Room[14] ;
	      for(int i=0;i<7;i++)
	      {
		      	roomArray[i*2] = new Room("SalaEsperta",0,0,i,30);
		      	roomArray[i*2+1] = new Room("SalaEsperta",0,1,i,30);
	      }*/
	      User.mainUser = new User(user,pass);
          loadEvents();
	      //Event specialEvent = new Event(3,"2014-12-25 14:00:00");
	      /*Event eventArray[] = new Event[12] ;
	      for(int i=0;i<12;i++)
	      {
		      	eventArray[i] = new Event(i%14,"2014-"+(i+1)+"-25 14:00:00","01:50:00");
		      	User.mainUser.addEvent(eventArray[i]);
	      }*/
	      
	}
    private void loadEvents(){
        try{
            ClientThread events = new ClientThread(activity_ctx,Constants.ctGetAct,new String[]{username,password});
            events.execute();
            String [] resultQuery = events.get();
            //System.out.println("result query: " + resultQuery[0] + ";;;;" + resultQuery[1]);
            String eventsSubscribed = resultQuery[2];
            int [] subscriptions = Constants.cvrtSubscriptions(eventsSubscribed);
            System.out.println("Events subscribed: " + eventsSubscribed);
            JSONArray jArr = new JSONArray(resultQuery[1]);
            JSONObject handler;
            for(int i = 0; i < jArr.length(); i++){
                handler = jArr.getJSONObject(i);
                Event ev = new Event(handler.getInt("ID") - 1, handler.getInt("roomID"), handler.getString("idOwner"), handler.getString("name"),
                        handler.getString("description"),handler.getInt("capacity"),handler.getInt("cap-taken"),handler.getString("timestamp"),
                        handler.getString("duration"),handler.getString("private").equals("t") ? true : false);
                if(Constants.contains(handler.getInt("ID"),subscriptions)){
                    System.out.println("Yes! Contains! " + handler.getInt("ID"));
                    User.mainUser.addEvent(ev);
                }
            }
        }
        catch(Exception e){
            Log.e("MainActivity", "An exception occurred room fetch: ", e);
        }
    }

    private void loadRooms(){
        try{
            ClientThread rooms = new ClientThread(activity_ctx,Constants.ctGetRoomsAct,new String[]{username,password});
            rooms.execute();
            String [] resultQuery = rooms.get();
            System.out.println("result query: " + resultQuery[0] + ";;;;" + resultQuery[1]);
            JSONArray jArr = new JSONArray(resultQuery[1]);
            JSONObject handler;
            for(int i = 0; i < jArr.length(); i++){
                handler = jArr.getJSONObject(i);
                new Room(handler.getString("name"),handler.getString("description"),0,handler.getInt("side"),handler.getInt("pos"),handler.getInt("max_cap"),handler.getInt("ID") - 1);
            }
        }
        catch(Exception e){
            Log.e("MainActivity", "An exception occurred room fetch: ", e);
        }
    }

  
}
