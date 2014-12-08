package locator.localizadormq;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ShowUser extends Activity{
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      
      final Button button = (Button) findViewById(R.id.button1);
      button.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
        	  Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
          }
      });
    }
}
