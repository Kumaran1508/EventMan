package com.ask.eventman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.widget.LinearLayout;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.util.*;

import java.util.*;

import android.widget.ListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.content.Intent;
import android.view.View;

public class MyEventsActivity extends AppCompatActivity {
	
	
	private Toolbar _toolbar;
	private FloatingActionButton _fab;
	private DrawerLayout _drawer;
	
	private LinearLayout container_home;
	private LinearLayout page;
	private LinearLayout bottom_nav;
	private LinearLayout my_events_frag;
	private ListView listview1;
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
	
	private Intent itnt = new Intent();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.my_events);
		com.google.firebase.FirebaseApp.initializeApp(this);
		initialize(_savedInstanceState);
		initializeLogic();
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
		
		_drawer = (DrawerLayout) findViewById(R.id._drawer);ActionBarDrawerToggle _toggle = new ActionBarDrawerToggle(MyEventsActivity.this, _drawer, _toolbar, R.string.app_name, R.string.app_name);
		_drawer.addDrawerListener(_toggle);
		_toggle.syncState();
		
		LinearLayout _nav_view = (LinearLayout) findViewById(R.id._nav_view);
		
		container_home = (LinearLayout) findViewById(R.id.container_home);
		page = (LinearLayout) findViewById(R.id.page);
		bottom_nav = (LinearLayout) findViewById(R.id.bottom_nav);
		my_events_frag = (LinearLayout) findViewById(R.id.my_events_frag);
		listview1 = (ListView) findViewById(R.id.listview1);
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
		
		home_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				home_img.setImageResource(R.drawable.home_icon);
				trending_img.setImageResource(R.drawable.trending_3);
				live_img.setImageResource(R.drawable.live_icon);
				joined_img.setImageResource(R.drawable.joined_icon);
				created_img.setImageResource(R.drawable.myevents_icon);
				itnt.setClass(getApplicationContext(), HomeActivity.class);
				startActivity(itnt);
			}
		});
		
		trending_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				home_img.setImageResource(R.drawable.home_icon_2);
				trending_img.setImageResource(R.drawable.trending_2);
				live_img.setImageResource(R.drawable.live_icon);
				joined_img.setImageResource(R.drawable.joined_icon);
				created_img.setImageResource(R.drawable.myevents_icon);
			}
		});
		
		live_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				home_img.setImageResource(R.drawable.home_icon_2);
				trending_img.setImageResource(R.drawable.trending_3);
				live_img.setImageResource(R.drawable.icon_1);
				joined_img.setImageResource(R.drawable.joined_icon);
				created_img.setImageResource(R.drawable.myevents_icon);
			}
		});
		
		joined_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				home_img.setImageResource(R.drawable.home_icon_2);
				trending_img.setImageResource(R.drawable.trending_3);
				live_img.setImageResource(R.drawable.live_icon);
				joined_img.setImageResource(R.drawable.icon_6);
				created_img.setImageResource(R.drawable.myevents_icon);
			}
		});
		
		created_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				
			}
		});
		
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				itnt.setClass(getApplicationContext(), CreateEventActivity.class);
				startActivity(itnt);
			}
		});
	}
	private void initializeLogic() {
		home_img.setImageResource(R.drawable.home_icon_2);
		trending_img.setImageResource(R.drawable.trending_3);
		live_img.setImageResource(R.drawable.live_icon);
		joined_img.setImageResource(R.drawable.joined_icon);
		created_img.setImageResource(R.drawable.icon_3);
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
	public void onBackPressed() {
		if (_drawer.isDrawerOpen(GravityCompat.START)) {
			_drawer.closeDrawer(GravityCompat.START);
		}
		else {
			super.onBackPressed();
		}
	}
	public class Listview1Adapter extends BaseAdapter {
		ArrayList<HashMap<String, Object>> _data;
		public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
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
