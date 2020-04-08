package com.ask.eventman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.content.Intent;
import android.net.Uri;
import java.util.Timer;
import java.util.TimerTask;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.animation.ObjectAnimator;
import android.view.animation.LinearInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.view.View;
import android.widget.AdapterView;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class HomeActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private Toolbar _toolbar;
	private FloatingActionButton _fab;
	private DrawerLayout _drawer;
	private HashMap<String, Object> map = new HashMap<>();
	private double i = 0;
	private String substr = "";
	private double nav_mem = 0;
	private String evt_key = "";
	private double j = 0;
	private String username = "";
	
	private ArrayList<HashMap<String, Object>> map_list = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> filtered = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> Joined = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> i_joined = new ArrayList<>();
	private ArrayList<String> joined_events_list = new ArrayList<>();
	private ArrayList<String> dt_tym = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> live = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> userlist = new ArrayList<>();
	
	private LinearLayout container_home;
	private LinearLayout page;
	private LinearLayout bottom_nav;
	private LinearLayout fragments_holder;
	private LinearLayout home_frag;
	private LinearLayout trending_frag;
	private LinearLayout live_frag;
	private LinearLayout joined_frag;
	private LinearLayout my_events_frag;
	private LinearLayout search_container;
	private LinearLayout event_list_container;
	private EditText event_search_text;
	private LinearLayout search_wrapper;
	private ImageView search_imgbtn;
	private ListView event_list;
	private ListView trending_events;
	private ListView live_events;
	private ListView joined_events;
	private ListView my_events;
	private LinearLayout home_wrapper;
	private LinearLayout trending_wrapper;
	private LinearLayout live_wrapper;
	private LinearLayout upcoming_wrapper;
	private LinearLayout myevents_wrapper;
	private ImageView home_img;
	private ImageView trending_img;
	private ImageView live_img;
	private ImageView joined_img;
	private ImageView created_img;
	private LinearLayout _drawer_drawer_container;
	private LinearLayout _drawer_linear1;
	private LinearLayout _drawer_settings;
	private LinearLayout _drawer_feedback;
	private LinearLayout _drawer_bug_report;
	private LinearLayout _drawer_help;
	private LinearLayout _drawer_about;
	private LinearLayout _drawer_logout;
	private LinearLayout _drawer_linear3;
	private ImageView _drawer_user_ico;
	private TextView _drawer_user;
	private ImageView _drawer_imageview1;
	private TextView _drawer_textview1;
	private ImageView _drawer_imageview3;
	private TextView _drawer_textview3;
	private ImageView _drawer_imageview4;
	private TextView _drawer_textview2;
	private ImageView _drawer_imageview5;
	private TextView _drawer_textview4;
	private ImageView _drawer_imageview2;
	private TextView _drawer_textview5;
	private ImageView _drawer_imageview6;
	private TextView _drawer_textview7;
	private TextView _drawer_textview6;
	
	private Intent itnt = new Intent();
	private TimerTask timer;
	private AlertDialog.Builder dialog;
	private ObjectAnimator obj_anim_def = new ObjectAnimator();
	private DatabaseReference dbase = _firebase.getReference("/events");
	private ChildEventListener _dbase_child_listener;
	private FirebaseAuth dbase_auth;
	private OnCompleteListener<AuthResult> _dbase_auth_create_user_listener;
	private OnCompleteListener<AuthResult> _dbase_auth_sign_in_listener;
	private OnCompleteListener<Void> _dbase_auth_reset_password_listener;
	private RequestNetwork net_req;
	private RequestNetwork.RequestListener _net_req_request_listener;
	private DatabaseReference dbase_user = _firebase.getReference("/users");
	private ChildEventListener _dbase_user_child_listener;
	private DatabaseReference joined_ = _firebase.getReference("/joined");
	private ChildEventListener _joined__child_listener;
	private Calendar cal = Calendar.getInstance();
	private Calendar start = Calendar.getInstance();
	private Calendar end = Calendar.getInstance();
	private DatabaseReference dbusers = _firebase.getReference("/users");
	private ChildEventListener _dbusers_child_listener;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.home);
		com.google.firebase.FirebaseApp.initializeApp(this);
		initialize(_savedInstanceState);
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
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
		_fab = (FloatingActionButton) findViewById(R.id._fab);
		
		_drawer = (DrawerLayout) findViewById(R.id._drawer);ActionBarDrawerToggle _toggle = new ActionBarDrawerToggle(HomeActivity.this, _drawer, _toolbar, R.string.app_name, R.string.app_name);
		_drawer.addDrawerListener(_toggle);
		_toggle.syncState();
		
		LinearLayout _nav_view = (LinearLayout) findViewById(R.id._nav_view);
		
		container_home = (LinearLayout) findViewById(R.id.container_home);
		page = (LinearLayout) findViewById(R.id.page);
		bottom_nav = (LinearLayout) findViewById(R.id.bottom_nav);
		fragments_holder = (LinearLayout) findViewById(R.id.fragments_holder);
		home_frag = (LinearLayout) findViewById(R.id.home_frag);
		trending_frag = (LinearLayout) findViewById(R.id.trending_frag);
		live_frag = (LinearLayout) findViewById(R.id.live_frag);
		joined_frag = (LinearLayout) findViewById(R.id.joined_frag);
		my_events_frag = (LinearLayout) findViewById(R.id.my_events_frag);
		search_container = (LinearLayout) findViewById(R.id.search_container);
		event_list_container = (LinearLayout) findViewById(R.id.event_list_container);
		event_search_text = (EditText) findViewById(R.id.event_search_text);
		search_wrapper = (LinearLayout) findViewById(R.id.search_wrapper);
		search_imgbtn = (ImageView) findViewById(R.id.search_imgbtn);
		event_list = (ListView) findViewById(R.id.event_list);
		trending_events = (ListView) findViewById(R.id.trending_events);
		live_events = (ListView) findViewById(R.id.live_events);
		joined_events = (ListView) findViewById(R.id.joined_events);
		my_events = (ListView) findViewById(R.id.my_events);
		home_wrapper = (LinearLayout) findViewById(R.id.home_wrapper);
		trending_wrapper = (LinearLayout) findViewById(R.id.trending_wrapper);
		live_wrapper = (LinearLayout) findViewById(R.id.live_wrapper);
		upcoming_wrapper = (LinearLayout) findViewById(R.id.upcoming_wrapper);
		myevents_wrapper = (LinearLayout) findViewById(R.id.myevents_wrapper);
		home_img = (ImageView) findViewById(R.id.home_img);
		trending_img = (ImageView) findViewById(R.id.trending_img);
		live_img = (ImageView) findViewById(R.id.live_img);
		joined_img = (ImageView) findViewById(R.id.joined_img);
		created_img = (ImageView) findViewById(R.id.created_img);
		_drawer_drawer_container = (LinearLayout) _nav_view.findViewById(R.id.drawer_container);
		_drawer_linear1 = (LinearLayout) _nav_view.findViewById(R.id.linear1);
		_drawer_settings = (LinearLayout) _nav_view.findViewById(R.id.settings);
		_drawer_feedback = (LinearLayout) _nav_view.findViewById(R.id.feedback);
		_drawer_bug_report = (LinearLayout) _nav_view.findViewById(R.id.bug_report);
		_drawer_help = (LinearLayout) _nav_view.findViewById(R.id.help);
		_drawer_about = (LinearLayout) _nav_view.findViewById(R.id.about);
		_drawer_logout = (LinearLayout) _nav_view.findViewById(R.id.logout);
		_drawer_linear3 = (LinearLayout) _nav_view.findViewById(R.id.linear3);
		_drawer_user_ico = (ImageView) _nav_view.findViewById(R.id.user_ico);
		_drawer_user = (TextView) _nav_view.findViewById(R.id.user);
		_drawer_imageview1 = (ImageView) _nav_view.findViewById(R.id.imageview1);
		_drawer_textview1 = (TextView) _nav_view.findViewById(R.id.textview1);
		_drawer_imageview3 = (ImageView) _nav_view.findViewById(R.id.imageview3);
		_drawer_textview3 = (TextView) _nav_view.findViewById(R.id.textview3);
		_drawer_imageview4 = (ImageView) _nav_view.findViewById(R.id.imageview4);
		_drawer_textview2 = (TextView) _nav_view.findViewById(R.id.textview2);
		_drawer_imageview5 = (ImageView) _nav_view.findViewById(R.id.imageview5);
		_drawer_textview4 = (TextView) _nav_view.findViewById(R.id.textview4);
		_drawer_imageview2 = (ImageView) _nav_view.findViewById(R.id.imageview2);
		_drawer_textview5 = (TextView) _nav_view.findViewById(R.id.textview5);
		_drawer_imageview6 = (ImageView) _nav_view.findViewById(R.id.imageview6);
		_drawer_textview7 = (TextView) _nav_view.findViewById(R.id.textview7);
		_drawer_textview6 = (TextView) _nav_view.findViewById(R.id.textview6);
		dialog = new AlertDialog.Builder(this);
		dbase_auth = FirebaseAuth.getInstance();
		net_req = new RequestNetwork(this);
		
		search_imgbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				substr = event_search_text.getText().toString();
				i = 0;
				for(int _repeat10 = 0; _repeat10 < (int)(map_list.size()); _repeat10++) {
					map = map_list.get((int)i);
					if (map_list.get((int)i).get("Title").toString().contains(substr)) {
						filtered.add(map);
					}
					i++;
				}
				event_list.setAdapter(new Event_listAdapter(filtered));
				((BaseAdapter)event_list.getAdapter()).notifyDataSetChanged();
			}
		});
		
		event_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				map = map_list.get((int)_position);
				try {
					itnt.putExtra("Title", map.get("Title").toString());
					itnt.putExtra("Description", map.get("Description").toString());
					itnt.putExtra("Type", map.get("Type").toString());
					itnt.putExtra("Start Date", map.get("Start Date").toString());
					itnt.putExtra("Start Time", map.get("Start Time").toString());
					itnt.putExtra("End Date", map.get("End Date").toString());
					itnt.putExtra("End Time", map.get("End Time").toString());
					itnt.putExtra("Location", map.get("Location").toString());
					itnt.putExtra("Latitude", map.get("Latitude").toString());
					itnt.putExtra("Longitude", map.get("Longitude").toString());
					itnt.putExtra("Owner", map.get("Owner").toString());
					itnt.putExtra("eventkey", map.get("key").toString());
				}
				catch(Exception e)
				{
					SketchwareUtil.showMessage(getApplicationContext(), "Something wrong with the Event");
				}
				itnt.setClass(getApplicationContext(), EventPageActivity.class);
				startActivity(itnt);
			}
		});
		
		trending_events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				
			}
		});
		
		live_events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				map = live.get((int)_position);
				try {
					itnt.putExtra("Title", map.get("Title").toString());
					itnt.putExtra("Description", map.get("Description").toString());
					itnt.putExtra("Type", map.get("Type").toString());
					itnt.putExtra("Start Date", map.get("Start Date").toString());
					itnt.putExtra("Start Time", map.get("Start Time").toString());
					itnt.putExtra("End Date", map.get("End Date").toString());
					itnt.putExtra("End Time", map.get("End Time").toString());
					itnt.putExtra("Location", map.get("Location").toString());
					itnt.putExtra("Latitude", map.get("Latitude").toString());
					itnt.putExtra("Longitude", map.get("Longitude").toString());
					itnt.setClass(getApplicationContext(), EventPageActivity.class);
					startActivity(itnt);
				}
				catch(Exception e)
				{
					SketchwareUtil.showMessage(getApplicationContext(), "Something wrong with the Event");
				}
			}
		});
		
		joined_events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				map = i_joined.get((int)_position);
				try {
					itnt.putExtra("Title", map.get("Title").toString());
					itnt.putExtra("Description", map.get("Description").toString());
					itnt.putExtra("Type", map.get("Type").toString());
					itnt.putExtra("Start Date", map.get("Start Date").toString());
					itnt.putExtra("Start Time", map.get("Start Time").toString());
					itnt.putExtra("End Date", map.get("End Date").toString());
					itnt.putExtra("End Time", map.get("End Time").toString());
					itnt.putExtra("Location", map.get("Location").toString());
					itnt.putExtra("Latitude", map.get("Latitude").toString());
					itnt.putExtra("Longitude", map.get("Longitude").toString());
					itnt.putExtra("eventkey", map.get("key").toString());
				}
				catch(Exception e)
				{
					SketchwareUtil.showMessage(getApplicationContext(), "Something wrong with the Event");
				}
				itnt.setClass(getApplicationContext(), EventPageActivity.class);
				startActivity(itnt);
			}
		});
		
		my_events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				map = filtered.get((int)_position);
				try {
					itnt.putExtra("Title", map.get("Title").toString());
					itnt.putExtra("Description", map.get("Description").toString());
					itnt.putExtra("Type", map.get("Type").toString());
					itnt.putExtra("Start Date", map.get("Start Date").toString());
					itnt.putExtra("Start Time", map.get("Start Time").toString());
					itnt.putExtra("End Date", map.get("End Date").toString());
					itnt.putExtra("End Time", map.get("End Time").toString());
					itnt.putExtra("Location", map.get("Location").toString());
					itnt.putExtra("Latitude", map.get("Latitude").toString());
					itnt.putExtra("Longitude", map.get("Longitude").toString());
					itnt.putExtra("Owner", map.get("Owner").toString());
					itnt.putExtra("eventkey", map.get("key").toString());
				}
				catch(Exception e)
				{
					SketchwareUtil.showMessage(getApplicationContext(), "Something wrong with the Event");
				}
				itnt.setClass(getApplicationContext(), EventPageActivity.class);
				startActivity(itnt);
			}
		});
		
		home_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				dbase.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						map_list = new ArrayList<>();
						try {
							GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								HashMap<String, Object> _map = _data.getValue(_ind);
								map_list.add(_map);
							}
						}
						catch (Exception _e) {
							_e.printStackTrace();
						}
						try{
							event_list.setAdapter(new Event_listAdapter(map_list));
							((BaseAdapter)event_list.getAdapter()).notifyDataSetChanged();
						}
						catch(Exception e)
						{
							SketchwareUtil.showMessage(getApplicationContext(), "Error Reloading Events");
						}
					}
					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
				home_img.setImageResource(R.drawable.home_icon_1);
				trending_img.setImageResource(R.drawable.trending_3);
				live_img.setImageResource(R.drawable.icon_2);
				joined_img.setImageResource(R.drawable.icon_5);
				created_img.setImageResource(R.drawable.icon_4);
				home_frag.setVisibility(View.VISIBLE);
				setTitle("Home");
				_fab.setVisibility(View.GONE);
				nav_mem = 0;
			}
		});
		
		trending_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				home_img.setImageResource(R.drawable.home_icon_2);
				trending_img.setImageResource(R.drawable.trending_2);
				live_img.setImageResource(R.drawable.icon_2);
				joined_img.setImageResource(R.drawable.icon_5);
				created_img.setImageResource(R.drawable.icon_4);
				home_frag.setVisibility(View.GONE);
				trending_frag.setVisibility(View.VISIBLE);
				setTitle("Trending");
				_fab.setVisibility(View.GONE);
			}
		});
		
		live_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				home_img.setImageResource(R.drawable.home_icon_2);
				trending_img.setImageResource(R.drawable.trending_3);
				live_img.setImageResource(R.drawable.icon_1);
				joined_img.setImageResource(R.drawable.icon_5);
				created_img.setImageResource(R.drawable.icon_4);
				home_frag.setVisibility(View.GONE);
				trending_frag.setVisibility(View.GONE);
				live_frag.setVisibility(View.VISIBLE);
				setTitle("Live");
				_fab.setVisibility(View.GONE);
			}
		});
		
		joined_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				home_img.setImageResource(R.drawable.home_icon_2);
				trending_img.setImageResource(R.drawable.trending_3);
				live_img.setImageResource(R.drawable.icon_2);
				joined_img.setImageResource(R.drawable.icon_6);
				created_img.setImageResource(R.drawable.icon_4);
				home_frag.setVisibility(View.GONE);
				trending_frag.setVisibility(View.GONE);
				live_frag.setVisibility(View.GONE);
				joined_frag.setVisibility(View.VISIBLE);
				setTitle("Joined Events");
				_fab.setVisibility(View.GONE);
			}
		});
		
		created_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				home_img.setImageResource(R.drawable.home_icon_2);
				trending_img.setImageResource(R.drawable.trending_3);
				live_img.setImageResource(R.drawable.icon_2);
				joined_img.setImageResource(R.drawable.icon_5);
				created_img.setImageResource(R.drawable.icon_3);
				home_frag.setVisibility(View.GONE);
				trending_frag.setVisibility(View.GONE);
				live_frag.setVisibility(View.GONE);
				joined_frag.setVisibility(View.GONE);
				my_events.setVisibility(View.VISIBLE);
				setTitle("My Events");
				_fab.setVisibility(View.VISIBLE);
			}
		});
		
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				itnt.setClass(getApplicationContext(), CreateEventActivity.class);
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
		
		_net_req_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _response = _param2;
				
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				
			}
		};
		
		_dbase_user_child_listener = new ChildEventListener() {
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
		dbase_user.addChildEventListener(_dbase_user_child_listener);
		
		_joined__child_listener = new ChildEventListener() {
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
		joined_.addChildEventListener(_joined__child_listener);
		
		_dbusers_child_listener = new ChildEventListener() {
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
		dbusers.addChildEventListener(_dbusers_child_listener);
		
		_drawer_settings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				SketchwareUtil.showMessage(getApplicationContext(), "Sorry , settings module is yet to be added.");
			}
		});
		
		_drawer_feedback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				itnt.setClass(getApplicationContext(), FeedbackActivity.class);
				startActivity(itnt);
			}
		});
		
		_drawer_bug_report.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				itnt.setClass(getApplicationContext(), BugReportActivity.class);
				startActivity(itnt);
			}
		});
		
		_drawer_help.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				itnt.setClass(getApplicationContext(), HelpActivity.class);
				startActivity(itnt);
			}
		});
		
		_drawer_about.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				itnt.setClass(getApplicationContext(), AboutActivity.class);
				startActivity(itnt);
			}
		});
		
		_drawer_logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				dialog.setTitle("Logout");
				dialog.setMessage("Are you sure want to Logout?");
				dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						FirebaseAuth.getInstance().signOut();
						itnt.setClass(getApplicationContext(), LoginActivity.class);
						startActivity(itnt);
					}
				});
				dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				dialog.create().show();
			}
		});
		
		_dbase_auth_create_user_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		_dbase_auth_sign_in_listener = new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(Task<AuthResult> _param1) {
				final boolean _success = _param1.isSuccessful();
				final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
				
			}
		};
		
		_dbase_auth_reset_password_listener = new OnCompleteListener<Void>() {
			@Override
			public void onComplete(Task<Void> _param1) {
				final boolean _success = _param1.isSuccessful();
				
			}
		};
	}
	private void initializeLogic() {
		setTitle("Home");
		_CardView("#ffffff", 1, 10, bottom_nav);
		_CardView("#f5f5f5", 1, 10, page);
		_RippleEffect("#2196f3", home_img);
		_RippleEffect("#2196f3", trending_img);
		_RippleEffect("#2196f3", live_img);
		_RippleEffect("#2196f3", joined_img);
		_RippleEffect("#2196f3", created_img);
		_RippleEffect("#2196f3", search_imgbtn);
		home_img.setImageResource(R.drawable.home_icon_1);
		trending_wrapper.setVisibility(View.GONE);
		dbase.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot _dataSnapshot) {
				map_list = new ArrayList<>();
				try {
					GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
					for (DataSnapshot _data : _dataSnapshot.getChildren()) {
						HashMap<String, Object> _map = _data.getValue(_ind);
						map_list.add(_map);
					}
				}
				catch (Exception _e) {
					_e.printStackTrace();
				}
				try{
					event_list.setAdapter(new Event_listAdapter(map_list));
					((BaseAdapter)event_list.getAdapter()).notifyDataSetChanged();
				}
				catch(Exception e){
					SketchwareUtil.showMessage(getApplicationContext(), "Error loading Events!....");
				}
				
				i = 0;
				for(int _repeat43 = 0; _repeat43 < (int)(map_list.size()); _repeat43++) {
					try{
						if (map_list.get((int)i).get("Owner").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
							map = map_list.get((int)i);
							filtered.add(map);
						}
					}
					catch(Exception e){
					}
					i++;
				}
				try{
					my_events.setAdapter(new My_eventsAdapter(filtered));
					((BaseAdapter)my_events.getAdapter()).notifyDataSetChanged();
				}
				catch(Exception e)
				{
					SketchwareUtil.showMessage(getApplicationContext(), "Error loading my events!...");
				}
			}
			@Override
			public void onCancelled(DatabaseError _databaseError) {
			}
		});
		joined_.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot _dataSnapshot) {
				Joined = new ArrayList<>();
				try {
					GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
					for (DataSnapshot _data : _dataSnapshot.getChildren()) {
						HashMap<String, Object> _map = _data.getValue(_ind);
						Joined.add(_map);
					}
				}
				catch (Exception _e) {
					_e.printStackTrace();
				}
				i = 0;
				for(int _repeat85 = 0; _repeat85 < (int)(Joined.size()); _repeat85++) {
					if (Joined.get((int)i).get("user").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
						joined_events_list.add(Joined.get((int)i).get("eventkey").toString());
					}
					i++;
				}
				dbase.addListenerForSingleValueEvent(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot _dataSnapshot) {
						map_list = new ArrayList<>();
						try {
							GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
							for (DataSnapshot _data : _dataSnapshot.getChildren()) {
								HashMap<String, Object> _map = _data.getValue(_ind);
								map_list.add(_map);
							}
						}
						catch (Exception _e) {
							_e.printStackTrace();
						}
						j = 0;
						for(int _repeat102 = 0; _repeat102 < (int)(map_list.size()); _repeat102++) {
							if (joined_events_list.contains(map_list.get((int)j).get("key").toString())) {
								map = map_list.get((int)j);
								i_joined.add(map);
							}
							start.set(Calendar.DAY_OF_MONTH, (int)(Double.parseDouble(map_list.get((int)j).get("Start Date").toString().substring((int)(0), (int)(2)))));
							start.set(Calendar.MONTH, (int)(Double.parseDouble(map_list.get((int)j).get("Start Date").toString().substring((int)(3), (int)(5))) - 1));
							start.set(Calendar.YEAR, (int)(Double.parseDouble(map_list.get((int)j).get("Start Date").toString().substring((int)(6), (int)(10)))));
							start.set(Calendar.HOUR, (int)(Double.parseDouble(map_list.get((int)j).get("Start Time").toString().substring((int)(0), (int)(2)))));
							start.set(Calendar.MINUTE, (int)(Double.parseDouble(map_list.get((int)j).get("Start Time").toString().substring((int)(3), (int)(5)))));
							end.set(Calendar.DAY_OF_MONTH, (int)(Double.parseDouble(map_list.get((int)j).get("End Date").toString().substring((int)(0), (int)(2)))));
							end.set(Calendar.MONTH, (int)(Double.parseDouble(map_list.get((int)j).get("End Date").toString().substring((int)(3), (int)(5))) - 1));
							end.set(Calendar.YEAR, (int)(Double.parseDouble(map_list.get((int)j).get("End Date").toString().substring((int)(6), (int)(10)))));
							end.set(Calendar.HOUR, (int)(Double.parseDouble(map_list.get((int)j).get("End Time").toString().substring((int)(0), (int)(2)))));
							end.set(Calendar.MINUTE, (int)(Double.parseDouble(map_list.get((int)j).get("End Time").toString().substring((int)(3), (int)(5)))));
							cal = Calendar.getInstance();
							substr = substr.concat(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(start.getTime()).concat(" ".concat(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(end.getTime()))));
							if (((long)(cal.getTimeInMillis() - start.getTimeInMillis()) > 0) && ((long)(end.getTimeInMillis() - cal.getTimeInMillis()) > 0)) {
								map = map_list.get((int)j);
								live.add(map);
							}
							j++;
						}
						joined_events.setAdapter(new Joined_eventsAdapter(i_joined));
						((BaseAdapter)joined_events.getAdapter()).notifyDataSetChanged();
						live_events.setAdapter(new Live_eventsAdapter(live));
						((BaseAdapter)live_events.getAdapter()).notifyDataSetChanged();
						FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/abc.txt"), substr);
					}
					@Override
					public void onCancelled(DatabaseError _databaseError) {
					}
				});
			}
			@Override
			public void onCancelled(DatabaseError _databaseError) {
			}
		});
		search_container.setVisibility(View.GONE);
		_fab.setVisibility(View.GONE);
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
	public void onResume() {
		super.onResume();
		
	}
	
	@Override
	public void onBackPressed() {
		dialog.setTitle("Exit");
		dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				finish();
			}
		});
		dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface _dialog, int _which) {
				
			}
		});
		dialog.create().show();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		dbusers.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot _dataSnapshot) {
				userlist = new ArrayList<>();
				try {
					GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {};
					for (DataSnapshot _data : _dataSnapshot.getChildren()) {
						HashMap<String, Object> _map = _data.getValue(_ind);
						userlist.add(_map);
					}
				}
				catch (Exception _e) {
					_e.printStackTrace();
				}
				i = 0;
				for(int _repeat11 = 0; _repeat11 < (int)(userlist.size()); _repeat11++) {
					map = userlist.get((int)i);
					if (map.get("mail").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
						username = map.get("name").toString();
						_drawer_user.setText(username);
						
						break;
					}
					i++;
				}
			}
			@Override
			public void onCancelled(DatabaseError _databaseError) {
			}
		});
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
	
	
	public class Event_listAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public Event_listAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		@Override
		public View getView(final int _position, View _view, ViewGroup _viewGroup) {
			LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View _v = _view;
			if (_v == null) {
				_v = _inflater.inflate(R.layout.list_element, null);
			}
			
			final LinearLayout elmnt_container = (LinearLayout) _v.findViewById(R.id.elmnt_container);
			final ImageView event_dp = (ImageView) _v.findViewById(R.id.event_dp);
			final LinearLayout evt_title_cont = (LinearLayout) _v.findViewById(R.id.evt_title_cont);
			final LinearLayout linear5 = (LinearLayout) _v.findViewById(R.id.linear5);
			final TextView event_title = (TextView) _v.findViewById(R.id.event_title);
			final TextView location = (TextView) _v.findViewById(R.id.location);
			final ImageView imageview2 = (ImageView) _v.findViewById(R.id.imageview2);
			
			_CardView("#ffffff", 5, 15, elmnt_container);
			event_dp.setImageResource(R.drawable.bg_img_2);
			imageview2.setImageResource(R.drawable.ic_bookmark_black);
			try{
				event_title.setText(_data.get((int)_position).get("Title").toString());
				location.setText(_data.get((int)_position).get("Owner").toString());
			}
			catch(Exception e)
			{
				SketchwareUtil.showMessage(getApplicationContext(), "Unable to set data to event list item");
			}
			
			return _v;
		}
	}
	
	public class Trending_eventsAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public Trending_eventsAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		@Override
		public View getView(final int _position, View _view, ViewGroup _viewGroup) {
			LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View _v = _view;
			if (_v == null) {
				_v = _inflater.inflate(R.layout.list_element, null);
			}
			
			final LinearLayout elmnt_container = (LinearLayout) _v.findViewById(R.id.elmnt_container);
			final ImageView event_dp = (ImageView) _v.findViewById(R.id.event_dp);
			final LinearLayout evt_title_cont = (LinearLayout) _v.findViewById(R.id.evt_title_cont);
			final LinearLayout linear5 = (LinearLayout) _v.findViewById(R.id.linear5);
			final TextView event_title = (TextView) _v.findViewById(R.id.event_title);
			final TextView location = (TextView) _v.findViewById(R.id.location);
			final ImageView imageview2 = (ImageView) _v.findViewById(R.id.imageview2);
			
			_CardView("#ffffff", 5, 15, elmnt_container);
			event_dp.setImageResource(R.drawable.bg_img_2);
			imageview2.setImageResource(R.drawable.ic_bookmark_black);
			try{
				event_title.setText(_data.get((int)_position).get("Title").toString());
				location.setText(_data.get((int)_position).get("Type").toString());
			}
			catch(Exception e)
			{
				SketchwareUtil.showMessage(getApplicationContext(), "Unable to set data to event list item");
			}
			
			return _v;
		}
	}
	
	public class Live_eventsAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public Live_eventsAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		@Override
		public View getView(final int _position, View _view, ViewGroup _viewGroup) {
			LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View _v = _view;
			if (_v == null) {
				_v = _inflater.inflate(R.layout.list_element, null);
			}
			
			final LinearLayout elmnt_container = (LinearLayout) _v.findViewById(R.id.elmnt_container);
			final ImageView event_dp = (ImageView) _v.findViewById(R.id.event_dp);
			final LinearLayout evt_title_cont = (LinearLayout) _v.findViewById(R.id.evt_title_cont);
			final LinearLayout linear5 = (LinearLayout) _v.findViewById(R.id.linear5);
			final TextView event_title = (TextView) _v.findViewById(R.id.event_title);
			final TextView location = (TextView) _v.findViewById(R.id.location);
			final ImageView imageview2 = (ImageView) _v.findViewById(R.id.imageview2);
			
			_CardView("#ffffff", 5, 15, elmnt_container);
			event_dp.setImageResource(R.drawable.bg_img_2);
			imageview2.setImageResource(R.drawable.ic_bookmark_black);
			try{
				event_title.setText(_data.get((int)_position).get("Title").toString());
				location.setText(_data.get((int)_position).get("Owner").toString());
			}
			catch(Exception e)
			{
				SketchwareUtil.showMessage(getApplicationContext(), "Unable to set data to event list item");
			}
			
			return _v;
		}
	}
	
	public class Joined_eventsAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public Joined_eventsAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		@Override
		public View getView(final int _position, View _view, ViewGroup _viewGroup) {
			LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View _v = _view;
			if (_v == null) {
				_v = _inflater.inflate(R.layout.list_element, null);
			}
			
			final LinearLayout elmnt_container = (LinearLayout) _v.findViewById(R.id.elmnt_container);
			final ImageView event_dp = (ImageView) _v.findViewById(R.id.event_dp);
			final LinearLayout evt_title_cont = (LinearLayout) _v.findViewById(R.id.evt_title_cont);
			final LinearLayout linear5 = (LinearLayout) _v.findViewById(R.id.linear5);
			final TextView event_title = (TextView) _v.findViewById(R.id.event_title);
			final TextView location = (TextView) _v.findViewById(R.id.location);
			final ImageView imageview2 = (ImageView) _v.findViewById(R.id.imageview2);
			
			_CardView("#ffffff", 5, 15, elmnt_container);
			event_dp.setImageResource(R.drawable.bg_img_2);
			imageview2.setImageResource(R.drawable.ic_bookmark_black);
			try{
				event_title.setText(_data.get((int)_position).get("Title").toString());
				location.setText(_data.get((int)_position).get("Owner").toString());
			}
			catch(Exception e)
			{
				SketchwareUtil.showMessage(getApplicationContext(), "Unable to set data to event list item");
			}
			
			return _v;
		}
	}
	
	public class My_eventsAdapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public My_eventsAdapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		@Override
		public View getView(final int _position, View _view, ViewGroup _viewGroup) {
			LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View _v = _view;
			if (_v == null) {
				_v = _inflater.inflate(R.layout.list_element, null);
			}
			
			final LinearLayout elmnt_container = (LinearLayout) _v.findViewById(R.id.elmnt_container);
			final ImageView event_dp = (ImageView) _v.findViewById(R.id.event_dp);
			final LinearLayout evt_title_cont = (LinearLayout) _v.findViewById(R.id.evt_title_cont);
			final LinearLayout linear5 = (LinearLayout) _v.findViewById(R.id.linear5);
			final TextView event_title = (TextView) _v.findViewById(R.id.event_title);
			final TextView location = (TextView) _v.findViewById(R.id.location);
			final ImageView imageview2 = (ImageView) _v.findViewById(R.id.imageview2);
			
			_CardView("#ffffff", 5, 15, elmnt_container);
			event_dp.setImageResource(R.drawable.bg_img_2);
			imageview2.setImageResource(R.drawable.ic_bookmark_black);
			try{
				event_title.setText(_data.get((int)_position).get("Title").toString());
				location.setText(_data.get((int)_position).get("Type").toString());
			}
			catch(Exception e)
			{
				SketchwareUtil.showMessage(getApplicationContext(), "Unable to set data to event list item");
			}
			
			return _v;
		}
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
