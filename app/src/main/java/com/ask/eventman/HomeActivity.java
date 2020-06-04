package com.ask.eventman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.widget.LinearLayout;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;

import java.util.HashMap;
import java.util.ArrayList;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.content.Intent;
import android.net.Uri;

import java.util.TimerTask;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.animation.ObjectAnimator;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;

import android.view.View;
import android.widget.AdapterView;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private StorageReference rootref = FirebaseStorage.getInstance().getReference();
	
	private Toolbar _toolbar;
	private FloatingActionButton _fab;
	private DrawerLayout _drawer;
	private HashMap<String, Object> map = new HashMap<>();
	private double i = 0;
	private String substr = "";
	private int getSelection = 0;
	private String evt_key = "";
	private double j = 0;
	private String username = "";
	
	private ArrayList<HashMap<String, Object>> map_list = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> filtered = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> userlist = new ArrayList<>();


	private LinearLayout home_frag;
	private LinearLayout search_container;
	private EditText event_search_text;
	private ImageView search_imgbtn;
	private ListView event_list;
	private LinearLayout _drawer_settings;
	private LinearLayout _drawer_feedback;
	private LinearLayout _drawer_bug_report;
	private LinearLayout _drawer_help;
	private LinearLayout _drawer_about;
	private LinearLayout _drawer_logout;
	private LinearLayout _drawer_linear3;
	private BottomNavigationView bottomNavigationView;

	private CircleImageView _drawer_user_ico;
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

	private int endLimit=7;
	private boolean isLoaded=Boolean.FALSE,isLoading=Boolean.FALSE;
	private long evt_count=0;
	private Fragment liveFrag = new LiveEvents();

	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.home);
		com.google.firebase.FirebaseApp.initializeApp(this);
		initialize(_savedInstanceState);

	}

	protected void loadEvents(){
		isLoading=Boolean.TRUE;
		Query LoadEvents;
		dbase.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if(evt_count!=dataSnapshot.getChildrenCount()){
					isLoaded=Boolean.FALSE;
				}
				evt_count = dataSnapshot.getChildrenCount();
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});
		if(isLoaded){
			isLoading=Boolean.FALSE;
			return;
		}
		if(map_list.size()>=evt_count && map_list.size()>0){
			isLoading=Boolean.FALSE;
			return;
		}
		else if (map_list.size()>0 && map_list.size()+endLimit<=evt_count){
			String lastElementKey = map_list.get(map_list.size()-1).get("key").toString();
			LoadEvents = dbase.orderByKey().startAt(lastElementKey).limitToFirst(endLimit);
			map_list.remove(map_list.size()-1);

		}
		else if(map_list.size()>0 && map_list.size()+endLimit>evt_count){
			String lastElementKey = map_list.get(map_list.size()-1).get("key").toString();
			LoadEvents = dbase.orderByKey().startAt(lastElementKey);
			map_list.remove(map_list.size()-1);
			isLoaded=Boolean.TRUE;
		}
		else {
			LoadEvents = dbase.orderByKey().limitToFirst(endLimit);

		}
		LoadEvents.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String, Object>> value = new GenericTypeIndicator<HashMap<String, Object>>() {};
				for (DataSnapshot data:dataSnapshot.getChildren()) {
					HashMap<String, Object> map = data.getValue(value);

					map_list.add(map);
				}


                event_list.setAdapter(new Event_listAdapter(map_list));
				isLoading=Boolean.FALSE;
                ((BaseAdapter)event_list.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
		
		_drawer = (DrawerLayout) findViewById(R.id._drawer);
		ActionBarDrawerToggle _toggle = new ActionBarDrawerToggle(HomeActivity.this, _drawer, _toolbar, R.string.app_name, R.string.app_name);
		_drawer.addDrawerListener(_toggle);
		_toggle.syncState();
		
		LinearLayout _nav_view = (LinearLayout) findViewById(R.id._nav_view);
		bottomNavigationView=findViewById(R.id.bottomNavigation);
		home_frag = (LinearLayout) findViewById(R.id.home_frag);
		search_container = (LinearLayout) findViewById(R.id.search_container);
		event_search_text = (EditText) findViewById(R.id.event_search_text);
		search_imgbtn = (ImageView) findViewById(R.id.search_imgbtn);
		event_list = (ListView) findViewById(R.id.event_list);
		_drawer_settings = (LinearLayout) _nav_view.findViewById(R.id.settings);
		_drawer_feedback = (LinearLayout) _nav_view.findViewById(R.id.feedback);
		_drawer_bug_report = (LinearLayout) _nav_view.findViewById(R.id.bug_report);
		_drawer_help = (LinearLayout) _nav_view.findViewById(R.id.help);
		_drawer_about = (LinearLayout) _nav_view.findViewById(R.id.about);
		_drawer_logout = (LinearLayout) _nav_view.findViewById(R.id.logout);
		_drawer_linear3 = (LinearLayout) _nav_view.findViewById(R.id.linear3);
		_drawer_user_ico = (CircleImageView) _nav_view.findViewById(R.id.user_ico);
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

		loadEvents();

		if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
			_drawer_imageview1.setImageDrawable(getDrawable(R.drawable.settings_icon_w));
			_drawer_imageview3.setImageDrawable(getDrawable(R.drawable.feedback_icon_w));
			_drawer_imageview4.setImageDrawable(getDrawable(R.drawable.ic_bug_report_white));
			_drawer_imageview5.setImageDrawable(getDrawable(R.drawable.help_icon_w));
			_drawer_imageview2.setImageDrawable(getDrawable(R.drawable.about_us_w));
			_drawer_imageview6.setImageDrawable(getDrawable(R.drawable.ic_launch_white));
		}


		bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				if(item.getItemId()==R.id.menu_home){
					getSelection=0;
					home_frag.setVisibility(View.VISIBLE);
					setTitle("Home");
					_fab.setVisibility(View.GONE);
				}
            	else if(item.getItemId()==R.id.menu_live){
					getSelection=2;
					home_frag.setVisibility(View.GONE);
					getSupportFragmentManager().beginTransaction().replace(R.id.frameFragment,liveFrag).commit();
					setTitle("Live");
					_fab.setVisibility(View.GONE);
				}
            	else if(item.getItemId()==R.id.menu_joined){
					getSelection=3;
					home_frag.setVisibility(View.GONE);
					setTitle("Joined Events");
					_fab.setVisibility(View.GONE);
				}
            	else{
					getSelection=4;
					home_frag.setVisibility(View.GONE);
					setTitle("My Events");
					_fab.setVisibility(View.VISIBLE);
				}
            	return true;
            }
        });

		event_list.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (view.getLastVisiblePosition() >= map_list.size()-2){
					//map_list.remove(map_list.size()-1);
					if(!isLoading){
						loadEvents();
					}
					else{
						Toast.makeText(HomeActivity.this, "LOADING EVENTS...", Toast.LENGTH_SHORT).show();
					}

				}
			}
		});


		try{
			rootref.child("users/"+FirebaseAuth.getInstance().getCurrentUser().getEmail()+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
				@Override
				public void onSuccess(Uri uri) {
					Glide.with(HomeActivity.this)
							.load(uri)
							.into(_drawer_user_ico);
				}
			});
		}catch(Exception e){
			Toast.makeText(HomeActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            _drawer_user_ico.setImageDrawable(getDrawable(R.drawable.def_user));
		}

		_drawer_user_ico.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CropImage.activity()
						.setGuidelines(CropImageView.Guidelines.ON)
						.setAspectRatio(1,1)
						.setActivityTitle("User Profile")
						.setActivityMenuIconColor(R.color.design_default_color_secondary)
						.setBackgroundColor(R.color.design_default_color_background)
						.setGuidelinesColor(R.color.design_default_color_background)
						.start(HomeActivity.this);
			}
		});

		
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
					Toast.makeText(HomeActivity.this, "Something wrong with the Event", Toast.LENGTH_SHORT).show();
				}
				itnt.setClass(getApplicationContext(), EventPageActivity.class);
				startActivity(itnt);
			}
		});





		
		/*joined_events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
		});*/
		

		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				itnt.setClass(getApplicationContext(), CreateEventActivity.class);
				startActivity(itnt);
				finish();
			}
		});
		
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

		_drawer_settings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
			    itnt.setClass(getApplicationContext(),SettingsActivity.class);
			    startActivity(itnt);
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
		//CardView(getColor(R.color.colorBackground), 1, 10, page);

		_RippleEffect("#2196f3", search_imgbtn);


		search_container.setVisibility(View.GONE);
		_fab.setVisibility(View.GONE);
	}



	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);

		if(_requestCode==203){
			CropImage.ActivityResult activityResult=CropImage.getActivityResult(_data);
			if(_resultCode==RESULT_OK){
				Uri icon;
				icon=activityResult.getUri();
				_drawer_user_ico.setImageURI(icon);

				StorageReference userdp = rootref.child("users/"+FirebaseAuth.getInstance().getCurrentUser().getEmail()+".png");
				userdp.putFile(icon).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
					@Override
					public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
						Toast.makeText(HomeActivity.this, "Profile Pic updated :)", Toast.LENGTH_SHORT).show();
					}
				});


			}
		}

	}
	
	@Override
	public void onResume() {
		super.onResume();

		if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
			_drawer_imageview1.setImageResource(R.drawable.settings_icon_w);
			_drawer_imageview3.setImageResource(R.drawable.feedback_icon_w);
			_drawer_imageview4.setImageResource(R.drawable.ic_bug_report_white);
			_drawer_imageview5.setImageResource(R.drawable.help_icon_w);
			_drawer_imageview2.setImageResource(R.drawable.about_us_w);
			_drawer_imageview6.setImageResource(R.drawable.ic_launch_white);
		}
		else{

			_drawer_imageview1.setImageResource(R.drawable.settings_icon);
			_drawer_imageview3.setImageResource(R.drawable.feedback_icon);
			_drawer_imageview4.setImageResource(R.drawable.ic_bug_report_black);
			_drawer_imageview5.setImageResource(R.drawable.help_icon);
			_drawer_imageview2.setImageResource(R.drawable.about_us_icon);
			_drawer_imageview6.setImageResource(R.drawable.ic_launch_black);
		}
		
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


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }
        else {
            initializeLogic();
        }

	}

	private void CardView (final int _color, final double _radius, final double _shadow, final View _view) {
		android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
		gd.setColor(_color);
		gd.setCornerRadius((int)_radius);
		_view.setBackground(gd);
		try {
			if(Build.VERSION.SDK_INT >= 21) {
				_view.setElevation((int)_shadow);
			}
		}
		catch (Exception e) {}
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
			View v = _view;
			if (v == null) {
				v = _inflater.inflate(R.layout.list_element, null);
			}
			
			final LinearLayout elmnt_container = (LinearLayout) v.findViewById(R.id.elmnt_container);
			final ImageView event_dp = (ImageView) v.findViewById(R.id.event_dp);
			final LinearLayout evt_title_cont = (LinearLayout) v.findViewById(R.id.evt_title_cont);
			final LinearLayout linear5 = (LinearLayout) v.findViewById(R.id.linear5);
			final TextView event_title = (TextView) v.findViewById(R.id.event_title);
			final TextView location = (TextView) v.findViewById(R.id.location);
			//final ImageView imageview2 = (ImageView) v.findViewById(R.id.imageview2);
			final TextView date = (TextView) v.findViewById(R.id.date);
			final TextView mthyr = (TextView) v.findViewById(R.id.mthyr);
			


			event_dp.setImageDrawable(getDrawable(R.drawable.bg_img_2));
			try {
				FirebaseStorage.getInstance().getReference().child("events/" + _data.get((int) _position).get("Title").toString() + "/icon.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
					@Override
					public void onSuccess(Uri uri) {
						Glide.with(HomeActivity.this)
								.load(uri)
								.placeholder(R.drawable.app_icon)
								.into(event_dp);
					}
				});
			}
			catch (Exception e){}

			//imageview2.setImageResource(R.drawable.ic_bookmark_black);
			try{
				event_title.setText(_data.get((int)_position).get("Title").toString());
				location.setText(_data.get((int)_position).get("Owner").toString());
				date.setText(_data.get((int)_position).get("Start Date").toString().substring(0,2));
				mthyr.setText(_data.get((int)_position).get("Start Date").toString().substring(3,5)+" "+_data.get((int)_position).get("Start Date").toString().substring(6,10));
			}
			catch(Exception e)
			{
				Toast.makeText(HomeActivity.this, "Unable to set data to event list item", Toast.LENGTH_SHORT).show();
			}
			
			return v;
		}
	}

	
}
