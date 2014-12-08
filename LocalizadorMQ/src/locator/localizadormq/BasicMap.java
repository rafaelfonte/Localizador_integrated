package locator.localizadormq;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;

public class BasicMap extends MapActivity {
    
	protected MapView map; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        
        init();
    }
    protected void init() {
    	this.setupMapView(new GeoPoint(-30.068334,-51.120298), 18);
    }
	protected void setupMapView(GeoPoint pt, int zoom) {
		this.map = (MapView) findViewById(R.id.map);
		map.getController().setZoom(zoom);
		map.getController().setCenter(pt);
		map.setBuiltInZoomControls(true);
	}
	protected int getLayoutId() {
	    return R.layout.simple_map;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	public String getText(EditText editText){
		String s = editText.getText().toString();
		if("".equals(s)) s=editText.getHint().toString();
		return s;
	}
	public void hideSoftKeyboard(View v){
		final InputMethodManager imm = (InputMethodManager)getSystemService(
				Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	
}