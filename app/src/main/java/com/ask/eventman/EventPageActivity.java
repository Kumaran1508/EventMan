package com.ask.eventman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.*;
import android.database.Cursor;
import android.os.*;
import android.provider.OpenableColumns;
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

import java.io.File;
import java.util.*;
import java.text.*;
import java.util.HashMap;
import java.util.ArrayList;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.content.Intent;
import android.net.Uri;
import android.content.ClipData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;
import android.view.View;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;

public class EventPageActivity extends AppCompatActivity {
	
	public final int REQ_CD_IMG_PICKER = 101;

	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
	private FirebaseAuth dbauth;

	private OnCompleteListener<AuthResult> _dbauth_create_user_listener;
	private OnCompleteListener<AuthResult> _dbauth_sign_in_listener;
	private OnCompleteListener<Void> _dbauth_reset_password_listener;
	private ChildEventListener _dbase_child_listener;
	private ChildEventListener _dbusers_child_listener;
	private ChildEventListener _joined_child_listener;

	private DatabaseReference dbase = _firebase.getReference("/events");
	private DatabaseReference dbusers = _firebase.getReference("/users");
	private DatabaseReference joined = _firebase.getReference("/joined");
	
	private Toolbar _toolbar;

	private double i = 0;

	private HashMap<String, Object> row_images = new HashMap<>();
	private HashMap<String, Object> mapJoined = new HashMap<>();
	
	private ArrayList<HashMap<String, Object>> Images = new ArrayList<>();
	private ArrayList<String> Image_urls = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> maplist = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> joined_list = new ArrayList<>();
	private ArrayList<Uri> _filePath = new ArrayList<>();
	private ArrayList<Uri> imgs = new ArrayList<>();
	
	private LinearLayout eventpage_container;
	private LinearLayout top_section;
	private LinearLayout middle_section;
	private LinearLayout bottom_section;
	private LinearLayout linear6;
	private LinearLayout start_content;
	private LinearLayout end_content;
	private LinearLayout linear9;
	private LinearLayout linear5;
	private LinearLayout linear13;
	private LinearLayout linear15;
	private LinearLayout linear_inside_midscr;
	private LinearLayout linear11;
	private LinearLayout linear12;
	private LinearLayout linear16;
	private LinearLayout linear17;

	private ListView listview1;

	private RecyclerView recyclerView;
	RecyclerView.LayoutManager layoutManager;
	private RecyclerViewAdapter2 recyclerViewAdapter;

	private ImageView evt_logo;

	private Button join_btn;
	private Button nav_btn;
	private Button add_btn;

	private ScrollView desc_scroll;

	private TextView textview3;
	private TextView desc;
	private TextView textview8;
	private TextView st_dt;
	private TextView textview9;
	private TextView st_t;
	private TextView title;
	private TextView evttype;
	private TextView textview10;
	private TextView end_dt;
	private TextView textview11;
	private TextView end_t;
	private TextView textview5;

	private Intent itnt = new Intent();
	private Intent img_picker = new Intent(Intent.ACTION_GET_CONTENT);

	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.event_page);
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
		eventpage_container = (LinearLayout) findViewById(R.id.eventpage_container);
		top_section = (LinearLayout) findViewById(R.id.top_section);
		middle_section = (LinearLayout) findViewById(R.id.middle_section);
		//bottom_section = (LinearLayout) findViewById(R.id.bottom_section);
		evt_logo = (ImageView) findViewById(R.id.evt_logo);
		title = (TextView) findViewById(R.id.title);
		linear5 = (LinearLayout) findViewById(R.id.linear5);
		linear13 = (LinearLayout) findViewById(R.id.linear13);
		linear15 = (LinearLayout) findViewById(R.id.linear15);
		evttype = (TextView) findViewById(R.id.evttype);
		join_btn = (Button) findViewById(R.id.join_btn);
		desc_scroll = (ScrollView) findViewById(R.id.desc_scroll);
		linear_inside_midscr = (LinearLayout) findViewById(R.id.linear_inside_midscr);
		textview3 = (TextView) findViewById(R.id.textview3);
		desc = (TextView) findViewById(R.id.desc);
		linear6 = (LinearLayout) findViewById(R.id.linear6);
		start_content = (LinearLayout) findViewById(R.id.start_content);
		end_content = (LinearLayout) findViewById(R.id.end_content);
		linear9 = (LinearLayout) findViewById(R.id.linear9);
		textview8 = (TextView) findViewById(R.id.textview8);
		st_dt = (TextView) findViewById(R.id.st_dt);
		textview9 = (TextView) findViewById(R.id.textview9);
		st_t = (TextView) findViewById(R.id.st_t);
		linear11 = (LinearLayout) findViewById(R.id.linear11);
		linear12 = (LinearLayout) findViewById(R.id.linear12);
		textview10 = (TextView) findViewById(R.id.textview10);
		end_dt = (TextView) findViewById(R.id.end_dt);
		textview11 = (TextView) findViewById(R.id.textview11);
		end_t = (TextView) findViewById(R.id.end_t);
		linear16 = (LinearLayout) findViewById(R.id.linear16);
		listview1 = (ListView) findViewById(R.id.listview1);
		textview5 = (TextView) findViewById(R.id.textview5);
		linear17 = (LinearLayout) findViewById(R.id.linear17);
		add_btn = (Button) findViewById(R.id.add_btn);
		img_picker.setType("image/*");
		nav_btn= (Button) findViewById(R.id.nav_btn);
		img_picker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		dbauth = FirebaseAuth.getInstance();

		recyclerView=(RecyclerView) findViewById(R.id.imgRecyclerView);
		layoutManager = new GridLayoutManager(this,3);
		recyclerView.setLayoutManager(layoutManager);
		recyclerViewAdapter = new RecyclerViewAdapter2(imgs);
		recyclerView.setAdapter(recyclerViewAdapter);
		recyclerView.setHasFixedSize(true);
		
		join_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (join_btn.getText().toString().equals("JOIN")) {
					mapJoined.put("user", FirebaseAuth.getInstance().getCurrentUser().getEmail());
					mapJoined.put("eventkey", getIntent().getStringExtra("eventkey"));
					mapJoined.put("key", joined.push().getKey());
					joined.child(mapJoined.get("key").toString()).updateChildren(mapJoined);
					join_btn.setText("JOINED");
					join_btn.setTextColor(0xFF00C853);
				}
				else {
					if (true) {
						
					}
				}
			}
		});

		nav_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				float lat=Float.parseFloat(getIntent().getStringExtra("Latitude"));
				float lng=Float.parseFloat(getIntent().getStringExtra("Longitude"));
				String url=String.format(Locale.ENGLISH,"http://www.google.com/maps/dir/?api=1&destination=%f,%f",lat,lng);
				Intent itnt=new Intent(Intent.ACTION_VIEW,Uri.parse(url));
				itnt.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
				startActivity(itnt);
			}
		});

		add_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				startActivityForResult(img_picker, REQ_CD_IMG_PICKER);

				for(int i=0;i<_filePath.size();i++){
                }

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
		
		_joined_child_listener = new ChildEventListener() {
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
		joined.addChildEventListener(_joined_child_listener);
		
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

		storageRef.child("events/").listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
			@Override
			public void onSuccess(ListResult listResult) {
				for (StorageReference item : listResult.getItems()) {
					// All the items under listRef.
					item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
						@Override
						public void onSuccess(Uri uri) {
							Toast.makeText(EventPageActivity.this, "loading "+uri.toString(), Toast.LENGTH_SHORT).show();
							imgs.add(uri);
						}
					});
				}
				recyclerViewAdapter = new RecyclerViewAdapter2(imgs);
				recyclerView.setAdapter(recyclerViewAdapter);
			}
		});
	}





	private void initializeLogic() {
		try{
			title.setText(getIntent().getStringExtra("Title"));
			evttype.setText(getIntent().getStringExtra("Type"));
			desc.setText(getIntent().getStringExtra("Description"));
			st_dt.setText(getIntent().getStringExtra("Start Date"));
			st_t.setText(getIntent().getStringExtra("Start Time"));
			end_dt.setText(getIntent().getStringExtra("End Date"));
			end_t.setText(getIntent().getStringExtra("End Time"));
			_CardView("#ffffff", 1, 5, join_btn);
			_CardView("#2196f3", 10, 5, add_btn);
			_CardView("#2196f3", 10, 5, nav_btn);
		}
		catch(Exception e)
		{
			SketchwareUtil.showMessage(getApplicationContext(), "Something_Error");
		}


		//listview1.setAdapter(new Listview1Adapter(Images));
		add_btn.setVisibility(View.GONE);
		try{
			if (getIntent().getStringExtra("Owner").equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
				add_btn.setVisibility(View.VISIBLE);
			}
		}
		catch(Exception e){
			SketchwareUtil.showMessage(getApplicationContext(), "");
		}
	}



	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_IMG_PICKER:
			if (_resultCode == Activity.RESULT_OK) {
				if (_data != null) {
					if (_data.getClipData() != null) {
						for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
						    //_data.getClipData().getItemAt(_index).
							ClipData.Item _item = _data.getClipData().getItemAt(_index);
							_filePath.add(_item.getUri());
							Toast.makeText(EventPageActivity.this, "for loop part executes : "+_item.getUri().toString(), Toast.LENGTH_LONG).show();
						}
					}
					else {
						_filePath.add(_data.getData());
                        //Toast.makeText(EventPageActivity.this, _data.getData().toString() , Toast.LENGTH_SHORT).show();

					}



				}
				else{
					Toast.makeText(EventPageActivity.this, "No File Picked", Toast.LENGTH_SHORT).show();
				}
			}
			break;
			default:
			break;
		}
		for (Uri f:_filePath) {
			//Uri file = Uri.fromFile(new File(f));
            String filename=f.getLastPathSegment();
			StorageReference riversRef = storageRef.child("events/"+getFileName(f) );
			UploadTask uploadTask = riversRef.putFile(f);

            Toast.makeText(this, "uploading"+getFileName(f), Toast.LENGTH_SHORT).show();

			// Register observers to listen for when the download is done or if it fails
			uploadTask.addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception exception) {
					// Handle unsuccessful uploads
                    //Toast.makeText(EventPageActivity.this, "Upload Failed :(\n"+exception.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
				@Override
				public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
					// taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
					// ...
                    Toast.makeText(EventPageActivity.this, "Upload Success :)", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	public String getFileName(Uri uri) {
		String result = null;
		if (uri.getScheme().equals("content")) {
			Cursor cursor = getContentResolver().query(uri, null, null, null, null);
			try {
				if (cursor != null && cursor.moveToFirst()) {
					result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
				}
			} finally {
				cursor.close();
			}
		}
		if (result == null) {
			result = uri.getPath();
			int cut = result.lastIndexOf('/');
			if (cut != -1) {
				result = result.substring(cut + 1);
			}
		}
		return result;
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	private void _CardView (final String _color, final double _radius, final double _shadow, final View _view) {
		android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable(); gd.setColor(Color.parseColor(_color)); gd.setCornerRadius((int)_radius); _view.setBackground(gd); try { if(Build.VERSION.SDK_INT >= 21) { _view.setElevation((int)_shadow); } } catch (Exception e) {}
	}

	
	
	/*public class Listview1Adapter extends BaseAdapter {
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
				_v = _inflater.inflate(R.layout.gallery_row, null);
			}
			
			final LinearLayout gallery_row = (LinearLayout) _v.findViewById(R.id.gallery_row);
			final LinearLayout linear2 = (LinearLayout) _v.findViewById(R.id.linear2);
			final LinearLayout linear3 = (LinearLayout) _v.findViewById(R.id.linear3);
			final LinearLayout linear4 = (LinearLayout) _v.findViewById(R.id.linear4);
			final ImageView img1 = (ImageView) _v.findViewById(R.id.img1);
			final ImageView img2 = (ImageView) _v.findViewById(R.id.img2);
			final ImageView img3 = (ImageView) _v.findViewById(R.id.img3);
			
			Glide.with(getApplicationContext()).load(Uri.parse(Images.get((int)_position).get("image1").toString())).into(img1);
			Glide.with(getApplicationContext()).load(Uri.parse(Images.get((int)_position).get("image2").toString())).into(img2);
			Glide.with(getApplicationContext()).load(Uri.parse(Images.get((int)_position).get("image3").toString())).into(img3);
			img1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					itnt.putExtra("img", Images.get((int)_position).get("image1").toString());
					itnt.setClass(getApplicationContext(), GalleryImageActivity.class);
					startActivity(itnt);
				}
			});
			img2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					itnt.putExtra("img", Images.get((int)_position).get("image2").toString());
					itnt.setClass(getApplicationContext(), GalleryImageActivity.class);
					startActivity(itnt);
				}
			});
			img3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					itnt.putExtra("img", Images.get((int)_position).get("image3").toString());
					itnt.setClass(getApplicationContext(), GalleryImageActivity.class);
					startActivity(itnt);
				}
			});
			
			return _v;
		}
	}*/
	
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
