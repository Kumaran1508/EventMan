package com.ask.eventman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.util.*;
import java.text.*;
import java.util.HashMap;
import android.widget.LinearLayout;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import android.content.Intent;
import android.net.Uri;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.view.View;
import java.text.DecimalFormat;

public class CreateEvent_2Activity extends AppCompatActivity implements OnMapReadyCallback{
	
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private Toolbar _toolbar;
	private HashMap<String, Object> map = new HashMap<>();
	private double lat = 0;
	private double lng = 0;
	private boolean isEventCreated = false;
	private String logo_url = "";
	
	private LinearLayout create_evt2_cntnr;
	private LinearLayout linear6;
	private LinearLayout linear7;
	private LinearLayout linear8;
	private MapView mapview2;
	private GoogleMapController _mapview2_controller;
	private TextView textview2;
	private EditText loc_add;
	private Button button2;
	
	private DatabaseReference dbase = _firebase.getReference("/events");
	private ChildEventListener _dbase_child_listener;
	private Intent itnt = new Intent();
	private FirebaseAuth dbauth;
	private OnCompleteListener<AuthResult> _dbauth_create_user_listener;
	private OnCompleteListener<AuthResult> _dbauth_sign_in_listener;
	private OnCompleteListener<Void> _dbauth_reset_password_listener;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.create_event_2);
		com.google.firebase.FirebaseApp.initializeApp(this);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		_toolbar = (Toolbar) findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		create_evt2_cntnr = (LinearLayout) findViewById(R.id.create_evt2_cntnr);
		linear6 = (LinearLayout) findViewById(R.id.linear6);
		linear7 = (LinearLayout) findViewById(R.id.linear7);
		linear8 = (LinearLayout) findViewById(R.id.linear8);
		mapview2 = (MapView) findViewById(R.id.mapview2);
		mapview2.onCreate(_savedInstanceState);
		mapview2.getMapAsync(this);
		textview2 = (TextView) findViewById(R.id.textview2);
		loc_add = (EditText) findViewById(R.id.loc_add);
		button2 = (Button) findViewById(R.id.button2);
		dbauth = FirebaseAuth.getInstance();

		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				map.put("Title", getIntent().getStringExtra("Title"));
				map.put("Description", getIntent().getStringExtra("Description"));
				map.put("Type", getIntent().getStringExtra("Type"));
				map.put("Start Date", getIntent().getStringExtra("Start Date"));
				map.put("Start Time", getIntent().getStringExtra("Start Time"));
				map.put("End Date", getIntent().getStringExtra("End Date"));
				map.put("End Time", getIntent().getStringExtra("End Time"));
				map.put("Latitude", String.valueOf(lat));
				map.put("Longitude", String.valueOf(lng));
				map.put("Location", loc_add.getText().toString());
				map.put("Owner", FirebaseAuth.getInstance().getCurrentUser().getEmail());
				isEventCreated = true;
				map.put("key", dbase.push().getKey());
				dbase.child(map.get("key").toString()).updateChildren(map);
				itnt.setClass(getApplicationContext(), HomeActivity.class);
				startActivity(itnt);
				finish();
			}
		});
		
		_dbase_child_listener = new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildChanged(DataSnapshot _param1, String _param2) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onChildMoved(DataSnapshot _param1, String _param2) {
				
			}
			
			@Override
			public void onChildRemoved(DataSnapshot _param1) {
				GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
				final String _childKey = _param1.getKey();
				final HashMap<String, Object> _childValue = _param1.getValue(_ind);
				
			}
			
			@Override
			public void onCancelled(DatabaseError _param1) {
				final int _errorCode = _param1.getCode();
				final String _errorMessage = _param1.getMessage();
				
			}
		};
		dbase.addChildEventListener(_dbase_child_listener);
		
		_dbauth_create_user_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		_dbauth_sign_in_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		_dbauth_reset_password_listener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				
			}
		};
	}


	private void initializeLogic() {
		_RippleEffect("Grey", button2);
		_CardView("#2979ff", 5, 5, button2);
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapview2.onDestroy();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		mapview2.onStart();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mapview2.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mapview2.onResume();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		mapview2.onStop();
	}
	private void _CardView (final String _color, final double _radius, final double _shadow, final View _view) {
		android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable(); gd.setColor(Color.parseColor(_color)); gd.setCornerRadius((int)_radius); _view.setBackground(gd); try { if(Build.VERSION.SDK_INT >= 21) { _view.setElevation((int)_shadow); } } catch (Exception e) {}
	}
	
	
	private void _RippleEffect (final String _color, final View _view) {
		android.content.res.ColorStateList clr = new android.content.res.ColorStateList(new int[][]{new int[]{}},new int[]{
			Color.parseColor(_color)}); 
		android.graphics.drawable.RippleDrawable ripdr = new android.graphics.drawable.RippleDrawable(clr, null, null);
		_view.setBackground(ripdr);
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels(){
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels(){
		return getResources().getDisplayMetrics().heightPixels;
	}

	@Override
	public void onMapReady(final GoogleMap googleMap) {
		final MarkerOptions marker = new MarkerOptions().position(new LatLng(0,0)).draggable(true);
		final Marker mrk=googleMap.addMarker(marker);

		googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				marker.position(latLng);
				mrk.setPosition(latLng);
				lat=marker.getPosition().latitude;
				lng=marker.getPosition().longitude;
				Toast.makeText(CreateEvent_2Activity.this, "Marker Clicked"+lat+" "+lng, Toast.LENGTH_SHORT).show();
			}
		});

	}
}
