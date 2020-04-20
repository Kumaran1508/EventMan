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
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.ClipData;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class CreateEventActivity extends AppCompatActivity {
	
	public final int REQ_CD_LOGO_PICKER = 101;
	private Timer _timer = new Timer();
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private Uri icon;
	
	private Toolbar _toolbar;
	private double i = 0;
	private String logopath = "";
	
	private ArrayList<String> TypeOfEvent = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> evtlist = new ArrayList<>();
	private ArrayList<String> evts = new ArrayList<>();
	
	private ScrollView create_event_cntnr;
	private LinearLayout create_event_p1;
	private LinearLayout linear20;
	private LinearLayout title_container;
	private LinearLayout description_cntnr;
	private LinearLayout type_container;
	private LinearLayout start_date_cntnr;
	private LinearLayout start_time_cntnr;
	private LinearLayout end_date_cntnr;
	private LinearLayout end_time_cntnr;
	private LinearLayout linear16;
	private ImageView evt_logo;
	private TextView tit;
	private EditText title;
	private TextView des;
	private EditText desc;
	private TextView typ;
	private Spinner type;
	private TextView dt;
	private TextView startdate;
	private TextView textview1;
	private TextView starttime;
	private TextView textview3;
	private TextView enddate;
	private TextView textview4;
	private TextView endtime;
	private LinearLayout linear17;
	private Button button1;
	private Button button2;
	
	private Intent itnt = new Intent();
	private DatabaseReference dbase = _firebase.getReference("/events");
	private ChildEventListener _dbase_child_listener;
	private AlertDialog.Builder dlog;
	private Intent logo_picker = new Intent(Intent.ACTION_GET_CONTENT);
	private TimerTask t;
	private AlertDialog.Builder start_cal;
	private Calendar end_cal = Calendar.getInstance();
	private Calendar cur_cal = Calendar.getInstance();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.create_event);
		com.google.firebase.FirebaseApp.initializeApp(this);
		initialize(_savedInstanceState);
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
		}
		else {
			initializeLogic();
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		_toolbar = (Toolbar) findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		create_event_cntnr = (ScrollView) findViewById(R.id.create_event_cntnr);
		create_event_p1 = (LinearLayout) findViewById(R.id.create_event_p1);
		linear20 = (LinearLayout) findViewById(R.id.linear20);
		title_container = (LinearLayout) findViewById(R.id.title_container);
		description_cntnr = (LinearLayout) findViewById(R.id.description_cntnr);
		type_container = (LinearLayout) findViewById(R.id.type_container);
		start_date_cntnr = (LinearLayout) findViewById(R.id.start_date_cntnr);
		start_time_cntnr = (LinearLayout) findViewById(R.id.start_time_cntnr);
		end_date_cntnr = (LinearLayout) findViewById(R.id.end_date_cntnr);
		end_time_cntnr = (LinearLayout) findViewById(R.id.end_time_cntnr);
		linear16 = (LinearLayout) findViewById(R.id.linear16);
		evt_logo = (ImageView) findViewById(R.id.evt_logo);
		tit = (TextView) findViewById(R.id.tit);
		title = (EditText) findViewById(R.id.title);
		des = (TextView) findViewById(R.id.des);
		desc = (EditText) findViewById(R.id.desc);
		typ = (TextView) findViewById(R.id.typ);
		type = (Spinner) findViewById(R.id.type);
		dt = (TextView) findViewById(R.id.dt);
		startdate = (TextView) findViewById(R.id.startdate);
		textview1 = (TextView) findViewById(R.id.textview1);
		starttime = (TextView) findViewById(R.id.starttime);
		textview3 = (TextView) findViewById(R.id.textview3);
		enddate = (TextView) findViewById(R.id.enddate);
		textview4 = (TextView) findViewById(R.id.textview4);
		endtime = (TextView) findViewById(R.id.endtime);
		linear17 = (LinearLayout) findViewById(R.id.linear17);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		dlog = new AlertDialog.Builder(this);
		logo_picker.setType("image/*");
		logo_picker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		start_cal = new AlertDialog.Builder(this);
		
		evt_logo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				//startActivityForResult(logo_picker, REQ_CD_LOGO_PICKER);
				CropImage.activity()
						.setGuidelines(CropImageView.Guidelines.ON)
						.setAspectRatio(1,1)
						.setActivityTitle("Event Iconss")
						.setActivityMenuIconColor(R.color.design_default_color_secondary)
						.setBackgroundColor(R.color.design_default_color_background)
						.setGuidelinesColor(R.color.design_default_color_background)
						.start(CreateEventActivity.this);
			}
		});
		
		startdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				final Calendar c = Calendar.getInstance(); 
				int mYear = c.get(Calendar.YEAR); 
				// current year 
				int mMonth = c.get(Calendar.MONTH); 
				// current month 
				int mDay = c.get(Calendar.DAY_OF_MONTH); 
				// current day 
				// date picker dialog 
				DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventActivity.this, new DatePickerDialog.OnDateSetListener() 
				{ @Override public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
					{ 
						// set day of month , month and year value in the edit text
						startdate.setText(String.format("%02d",dayOfMonth) + "/" +String.format("%02d",(monthOfYear + 1)) + "/" + year); 
					} 
				}, mYear, mMonth, mDay); 
				datePickerDialog.show();
			}
		});
		
		starttime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Calendar mcurrentTime = Calendar.getInstance(); 
				int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY); 
				int minute = mcurrentTime.get(Calendar.MINUTE); 
				TimePickerDialog mTimePicker; mTimePicker = new TimePickerDialog(CreateEventActivity.this, new TimePickerDialog.OnTimeSetListener() 
				{ 
					@Override 
					public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) 
					{ 
						String hr=String.format("%02d",selectedHour);
						String min=String.format("%02d",selectedMinute);
						starttime.setText( hr+ ":" +min ); 
					} 
				}, hour, minute,false); mTimePicker.setTitle("Select Time"); mTimePicker.show();
			}
		});
		
		enddate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				final Calendar c = Calendar.getInstance(); 
				int mYear = c.get(Calendar.YEAR); 
				// current year 
				int mMonth = c.get(Calendar.MONTH); 
				// current month 
				int mDay = c.get(Calendar.DAY_OF_MONTH); 
				// current day 
				// date picker dialog 
				DatePickerDialog datePickerDialog = new DatePickerDialog(CreateEventActivity.this, new DatePickerDialog.OnDateSetListener() 
				{ @Override public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
					{ 
						// set day of month , month and year value in the edit text
						enddate.setText(String.format("%02d",dayOfMonth) + "/" +String.format("%02d",(monthOfYear + 1)) + "/" + year); 
					} 
				}, mYear, mMonth, mDay); 
				datePickerDialog.show();
			}
		});
		
		endtime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				Calendar mcurrentTime = Calendar.getInstance(); 
				int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY); 
				int minute = mcurrentTime.get(Calendar.MINUTE); 
				TimePickerDialog mTimePicker; mTimePicker = new TimePickerDialog(CreateEventActivity.this, new TimePickerDialog.OnTimeSetListener() 
				{ 
					@Override 
					public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) 
					{ 
						String hr=String.format("%02d",selectedHour);
						String min=String.format("%02d",selectedMinute);
						endtime.setText( hr+ ":" +min ); 
					} 
				}, hour, minute,false); mTimePicker.setTitle("Select Time"); mTimePicker.show();
			}
		});
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				onBackPressed();
				finish();
			}
		});
		
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				dbase.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						evtlist = new ArrayList<>();
						try {
							GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								HashMap<String, Object> _map = _data.getValue(_ind);
								evtlist.add(_map);
							}
						}
						catch (Exception _e) {
							_e.printStackTrace();
						}
						i = 0;
						for(int _repeat28 = 0; _repeat28 < (int)(evtlist.size()); _repeat28++) {
							evts.add(evtlist.get((int)i).get("Title").toString());
							i++;
						}
					}
					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
				if (evts.contains(title.getText().toString())) {
					dlog.setMessage("Choose a different Event Name");
					dlog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							title.requestFocus();
						}
					});
					dlog.create().show();
					return;
				}
				if (title.getText().toString().length() == 0) {
					dlog.setTitle("Enter a valid Title");
					dlog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							title.requestFocus();
						}
					});
					dlog.create().show();
					return;
				}
				if ((startdate.getText().toString().length() > 10) || ((enddate.getText().toString().length() > 10) || ((starttime.getText().toString().length() > 5) || (endtime.getText().toString().length() > 5)))) {
					SketchwareUtil.showMessage(getApplicationContext(), "Invalid Credentials");
					return;
				}
				cur_cal.set(Calendar.YEAR, (int)(Double.parseDouble(startdate.getText().toString().substring((int)(6), (int)(10)))));
				cur_cal.set(Calendar.MONTH, (int)(Double.parseDouble(startdate.getText().toString().substring((int)(3), (int)(5))) - 1));
				cur_cal.set(Calendar.DAY_OF_MONTH, (int)(Double.parseDouble(startdate.getText().toString().substring((int)(0), (int)(2)))));
				cur_cal.set(Calendar.HOUR, (int)(Double.parseDouble(starttime.getText().toString().substring((int)(0), (int)(2)))));
				cur_cal.set(Calendar.MINUTE, (int)(Double.parseDouble(starttime.getText().toString().substring((int)(3), (int)(5)))));
				end_cal.set(Calendar.YEAR, (int)(Double.parseDouble(enddate.getText().toString().substring((int)(6), (int)(10)))));
				end_cal.set(Calendar.MONTH, (int)(Double.parseDouble(enddate.getText().toString().substring((int)(3), (int)(5))) - 1));
				end_cal.set(Calendar.DAY_OF_MONTH, (int)(Double.parseDouble(enddate.getText().toString().substring((int)(0), (int)(2)))));
				end_cal.set(Calendar.HOUR, (int)(Double.parseDouble(endtime.getText().toString().substring((int)(0), (int)(2)))));
				end_cal.set(Calendar.MINUTE, (int)(Double.parseDouble(endtime.getText().toString().substring((int)(3), (int)(5)))));
				if ((long)(end_cal.getTimeInMillis() - cur_cal.getTimeInMillis()) < 0) {
					dlog.setTitle("Invalid date and time");
					dlog.setMessage(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(cur_cal.getTime()).concat("    ".concat(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(end_cal.getTime()))));
					dlog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							enddate.requestFocus();
						}
					});
					dlog.create().show();
					return;
				}
				itnt.putExtra("Title", title.getText().toString());
				itnt.putExtra("Description", desc.getText().toString());
				itnt.putExtra("Type", TypeOfEvent.get((int)(type.getSelectedItemPosition())));
				itnt.putExtra("Start Date", startdate.getText().toString());
				itnt.putExtra("Start Time", starttime.getText().toString());
				itnt.putExtra("End Date", enddate.getText().toString());
				itnt.putExtra("End Time", endtime.getText().toString());
				itnt.putExtra("icon",icon);
				itnt.setClass(getApplicationContext(), CreateEvent_2Activity.class);
				startActivity(itnt);
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
	}
	private void initializeLogic() {
		TypeOfEvent.add("Private");
		TypeOfEvent.add("Public");
		type.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, TypeOfEvent));
		_CardView("#2979ff", 5, 10, button1);
		_CardView("#2979ff", 5, 10, button2);
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);

		if(_requestCode==203){
			CropImage.ActivityResult activityResult=CropImage.getActivityResult(_data);
			if(_resultCode==RESULT_OK){
				icon=activityResult.getUri();
				evt_logo.setImageURI(icon);


			}
		}
	}
	
	@Override
	public void onBackPressed() {
		itnt.setClass(getApplicationContext(), HomeActivity.class);
		startActivity(itnt);
		finish();
	}
	private void _CardView (final String _color, final double _radius, final double _shadow, final View _view) {
		android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable(); gd.setColor(Color.parseColor(_color)); gd.setCornerRadius((int)_radius); _view.setBackground(gd); try { if(Build.VERSION.SDK_INT >= 21) { _view.setElevation((int)_shadow); } } catch (Exception e) {}
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
	
}
