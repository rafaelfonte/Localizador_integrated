package locator.localizadormq;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import locator.servcomm.ClientThread;
import locator.servcomm.Constants;


public class MainActivity extends Activity {

    Context activity_ctx;

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
              String username = ((EditText)findViewById(R.id.username_field)).getText().toString();
              String password = ((EditText)findViewById(R.id.password_field)).getText().toString();
              //Remove this before pushing
              load(username,password);
              startActivity(intent);
              //End of removal part
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
    }
	
	private void load(String user, String pass)
	{
	      Room roomArray[] = new Room[14] ;
	      for(int i=0;i<7;i++)
	      {
		      	roomArray[i*2] = new Room("SalaEsperta",0,0,i,30);
		      	roomArray[i*2+1] = new Room("SalaEsperta",0,1,i,30);
	      }
	      User.mainUser = new User(user,pass);
	      //Event specialEvent = new Event(3,"2014-12-25 14:00:00");
	      Event eventArray[] = new Event[12] ;
	      for(int i=0;i<12;i++)
	      {
		      	eventArray[i] = new Event(i%14,"2014-"+(i+1)+"-25 14:00:00","01:50:00");
		      	User.mainUser.addEvent(eventArray[i]);
	      }
	      
	}
  
}
