package locator.localizadormq;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.widget.Toast;

import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.PolygonOverlay;

public class Room {
	//Angular coefficients for room position
	static final double alpha = 1.17038308;
	static final double beta = 1.06369782;
	//Complex Top Size
	static final double complexTopSize = 0.00205234012776;
	//Building Size
	static final double predioSideSize = 0.000597146548177;
	static final double predioTopSize =  0.000205234012776;
	//Room Size
	static final double salaTopSize = 0.9*(predioTopSize/2);
	static final double salaSideSize = 0.9*(predioSideSize/8);
	//Building Coordinates
	static final double predio1Lat = -30.067990;
	static final double predio1Lng = -51.121002;
	//Room Step Size (considering corridor)
	static final double salaTopStepLat = - 1.1*(predioTopSize/2)*Math.cos(alpha);
	static final double salaTopStepLng = + 1.1*(predioTopSize/2)*Math.sin(alpha);
	static final double salaSideStepLat = - 1.1*(predioSideSize/8)*Math.sin(beta);
	static final double salaSideStepLng = - 1.1*(predioSideSize/8)*Math.cos(beta);
	/*
	static final double predioTopStepLat = -0.000090;
	static final double predioTopStepLng = +0.000192;
	static final double predioSideStepLat = -0.000518;
	static final double predioSideStepLng = -0.000286;
	
	static final double roomTopStepLat = 	predioTopStepLat/2 +0.000001;
	static final double roomTopStepLng = 	predioTopStepLng/2 -0.000002; 
	static final double roomSideStepLat = 	predioSideStepLat +0.000002;
	static final double roomSideStepLng = 	predioSideStepLng +0.000001;
	
	/*
	 * Building Specific steps
	static double topStepLat = -0.000080; //(10x bigger to verify)
	static double topStepLng = +0.000189; //(10x bigger to verify)
	static double sideStepLat = -0.000522;
	static double sideStepLng = -0.000290;
	*/
	
	/*static double topStepLat = -0.000800;
	static double topStepLng = +0.001890;
	static double sideStepLat = -0.000522;
	static double sideStepLng = -0.000290;
	
	*/
	
	static int counter =0;
	static Paint paint;
	static Paint altPaint;
	
	int id;
	String name;
	int capacity;
	PolygonOverlay polygon;
	List<GeoPoint> polyData;
	double baseLat;
	double baseLng;
	double topStepLat;
	double topStepLng;
	double sideStepLat;
	double sideStepLng;
	
	static ArrayList<Room> allRooms = new ArrayList<Room>();
	public Room(String nome,double initialLat, double initialLng,int cap)
	{
		id = counter;
		counter++;
		name = nome;
		capacity = cap;
		
		topStepLat = -salaTopSize*Math.cos(alpha);
		topStepLng = +salaTopSize*Math.sin(alpha);
		sideStepLat = -salaSideSize*Math.sin(beta);
		sideStepLng = -salaSideSize*Math.cos(beta);
		
        polyData = new ArrayList<GeoPoint>();
		polyData.add(new GeoPoint(initialLat,initialLng));
		polyData.add(new GeoPoint(initialLat+topStepLat,initialLng+topStepLng));
		polyData.add(new GeoPoint(initialLat+topStepLat+sideStepLat,initialLng+topStepLng+sideStepLng));
		polyData.add(new GeoPoint(initialLat+sideStepLat,initialLng+sideStepLng));
		/*
        polygon = new PolygonOverlay(paint);
        polygon.setData(polyData);
        polygon.setKey("Polygon #"+id);

		polygon.setTapListener(new PolygonOverlay.OverlayTapListener() {			
			@Override
			public void onTap(GeoPoint gp, MapView mapView) {
				Toast.makeText(EnrichedMap.getContext(), "Sala"+name+id, Toast.LENGTH_SHORT).show();				
			}
		});
		*/
		allRooms.add(this);
	}
	public Room(String nome,int predio, int lado, int altura,int cap)
	{
		id = counter;
		counter++;
		name = nome;
		capacity = cap;
		baseLat = predio1Lat + salaTopStepLat*lado + salaSideStepLat*altura;
		baseLng = predio1Lng + salaTopStepLng*lado + salaSideStepLng*altura;
		topStepLat = -salaTopSize*Math.cos(alpha);
		topStepLng = +salaTopSize*Math.sin(alpha);
		sideStepLat = -salaSideSize*Math.sin(beta);
		sideStepLng = -salaSideSize*Math.cos(beta);
		
        polyData = new ArrayList<GeoPoint>();
		polyData.add(new GeoPoint(baseLat,baseLng));
		polyData.add(new GeoPoint(baseLat+topStepLat,baseLng+topStepLng));
		polyData.add(new GeoPoint(baseLat+topStepLat+sideStepLat,baseLng+topStepLng+sideStepLng));
		polyData.add(new GeoPoint(baseLat+sideStepLat,baseLng+sideStepLng));
        
        /*
        polygon.setTapListener(new PolygonOverlay.OverlayTapListener() {			
			@Override
			public void onTap(GeoPoint gp, MapView mapView) {
				Toast.makeText(EnrichedMap.getContext(), "Sala"+name+id, Toast.LENGTH_SHORT).show();				
			}
		});
	*/
		allRooms.add(this);
	}
	
	public static Room getRoom(int id)
	{
		return allRooms.get(id);
	}
	public static void drawRooms(MapView map)
	{
		for(Room r : allRooms)
		{	
			r.polygon = new PolygonOverlay(paint);
	        r.polygon.setData(r.polyData);
	        r.polygon.setKey("Polygon #"+r.id);
			map.getOverlays().add(r.polygon);
		}
		map.invalidate();
		//map.postInvalidate();
	}
	public void draw(MapView map)
	{
		polygon = new PolygonOverlay(altPaint);
        polygon.setData(polyData);
        polygon.setKey("Polygon #"+id);
		map.getOverlays().add(polygon);
		map.invalidate();
	}
	public void drawSpecial(final String string,MapView map)
	{
		polygon = new PolygonOverlay(altPaint);
        polygon.setData(polyData);
        polygon.setKey("Polygon #"+id);
        /*Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final int hours = calendar.get(Calendar.HOUR_OF_DAY);
        final int minutes = calendar.get(Calendar.MINUTE);
        */
        polygon.setTapListener(new PolygonOverlay.OverlayTapListener() {			
			@Override
			public void onTap(GeoPoint gp, MapView mapView) {
				Toast.makeText(EnrichedMap.getContext(), string, Toast.LENGTH_SHORT).show();				
			}
		});
		map.getOverlays().add(polygon);
		map.invalidate();
	}
	
	public static void init()
	{
		paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAlpha(50);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        
		altPaint = new Paint();
        altPaint.setColor(Color.GREEN);
        altPaint.setAlpha(80);
        altPaint.setAntiAlias(true);
        altPaint.setStyle(Paint.Style.FILL);
	}
		
}
