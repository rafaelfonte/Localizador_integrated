package locator.localizadormq;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      load();
      final Intent intent = new Intent(this, EnrichedMap.class);
      
      final Button button = (Button) findViewById(R.id.button1);
      button.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
        	  Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
              //Autenticacao
        	  startActivity(intent);
          }
      });
    }
	
	private void load()
	{
	      Room roomArray[] = new Room[14] ;
	      for(int i=0;i<7;i++)
	      {
		      	roomArray[i*2] = new Room("SalaEsperta",0,0,i,30);
		      	roomArray[i*2+1] = new Room("SalaEsperta",0,1,i,30);
	      }
	      User.mainUser = new User("Nicolas","12345");
	      //Event specialEvent = new Event(3,"2014-12-25 14:00:00");
	      Event eventArray[] = new Event[12] ;
	      for(int i=0;i<12;i++)
	      {
		      	eventArray[i] = new Event(i%14,"2014-"+(i+1)+"-25 14:00:00");
		      	User.mainUser.addEvent(eventArray[i]);
	      }
	      
	}
  
}
