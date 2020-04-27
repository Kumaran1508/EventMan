package com.ask.eventman;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ScrollView;
import android.content.Intent;
import android.net.Uri;
import java.util.Timer;
import java.util.TimerTask;
import android.animation.ObjectAnimator;
import android.view.animation.LinearInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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

public class LoginActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
	
	private boolean isLogin = false;
	private HashMap<String, Object> user_signup = new HashMap<>();
	
	private LinearLayout container_login;
	private LinearLayout linear1;
	private LinearLayout linear3;
	private LinearLayout title_section;
	private LinearLayout login_section;
	private ImageView imageview7;
	private LinearLayout login_title;
	private LinearLayout username_container;
	private LinearLayout password_container;
	private LinearLayout login_btn_container;
	private LinearLayout login_mode_container;
	private TextView textview1;
	private EditText uname;
	private EditText pwd;
	private LinearLayout sigupcon_login_btn;
	private LinearLayout signup_flds_con_h;
	private Button login_btn;
	private ScrollView vscroll5;
	private LinearLayout linear12;
	private EditText name;
	private EditText user;
	private EditText password;
	private EditText password_repeat;
	private EditText email_id;
	private EditText date_of_birth;
	private TextView login_mode_text;
	private TextView sign_up;
	
	private Intent intnt = new Intent();
	private TimerTask timer;
	private ObjectAnimator anim = new ObjectAnimator();
	private ObjectAnimator anim2 = new ObjectAnimator();
	private ObjectAnimator anim3 = new ObjectAnimator();
	private ObjectAnimator anim4 = new ObjectAnimator();
	private ObjectAnimator anim5 = new ObjectAnimator();
	private ObjectAnimator anim6 = new ObjectAnimator();
	private FirebaseAuth dbauth;
	private OnCompleteListener<AuthResult> _dbauth_create_user_listener;
	private OnCompleteListener<AuthResult> _dbauth_sign_in_listener;
	private OnCompleteListener<Void> _dbauth_reset_password_listener;
	private DatabaseReference dbase = _firebase.getReference("/users");
	private ChildEventListener _dbase_child_listener;
	private AlertDialog.Builder dialog;

	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.login);
		com.google.firebase.FirebaseApp.initializeApp(this);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		container_login = (LinearLayout) findViewById(R.id.container_login);
		linear1 = (LinearLayout) findViewById(R.id.linear1);
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		title_section = (LinearLayout) findViewById(R.id.title_section);
		login_section = (LinearLayout) findViewById(R.id.login_section);
		imageview7 = (ImageView) findViewById(R.id.imageview7);
		login_title = (LinearLayout) findViewById(R.id.login_title);
		username_container = (LinearLayout) findViewById(R.id.username_container);
		password_container = (LinearLayout) findViewById(R.id.password_container);
		login_btn_container = (LinearLayout) findViewById(R.id.login_btn_container);
		login_mode_container = (LinearLayout) findViewById(R.id.login_mode_container);
		textview1 = (TextView) findViewById(R.id.textview1);
		uname = (EditText) findViewById(R.id.uname);
		pwd = (EditText) findViewById(R.id.pwd);
		sigupcon_login_btn = (LinearLayout) findViewById(R.id.sigupcon_login_btn);
		signup_flds_con_h = (LinearLayout) findViewById(R.id.signup_flds_con_h);
		login_btn = (Button) findViewById(R.id.login_btn);
		vscroll5 = (ScrollView) findViewById(R.id.vscroll5);
		linear12 = (LinearLayout) findViewById(R.id.linear12);
		name = (EditText) findViewById(R.id.name);
		user = (EditText) findViewById(R.id.user);
		password = (EditText) findViewById(R.id.password);
		password_repeat = (EditText) findViewById(R.id.password_repeat);
		email_id = (EditText) findViewById(R.id.email_id);
		date_of_birth = (EditText) findViewById(R.id.date_of_birth);
		login_mode_text = (TextView) findViewById(R.id.login_mode_text);
		sign_up = (TextView) findViewById(R.id.sign_up);
		dbauth = FirebaseAuth.getInstance();
		dialog = new AlertDialog.Builder(this);
		
		login_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				anim.setTarget(login_btn);
				anim.setDuration((int)(150));
				anim.setPropertyName("scaleX");
				anim.setFloatValues((float)(1), (float)(0.85d));
				anim.setRepeatMode(ValueAnimator.REVERSE);
				anim.setRepeatCount((int)(1));
				anim2.setTarget(login_btn);
				anim2.setPropertyName("scaleY");
				anim2.setDuration((int)(150));
				anim2.setFloatValues((float)(1), (float)(0.85d));
				anim2.setRepeatMode(ValueAnimator.REVERSE);
				anim2.setRepeatCount((int)(1));
				anim.start();
				anim2.start();
				if (isLogin) {
					SketchwareUtil.showMessage(getApplicationContext(), "Signing In....  please wait....");
					dbauth.signInWithEmailAndPassword(uname.getText().toString(), pwd.getText().toString()).addOnCompleteListener(LoginActivity.this, _dbauth_sign_in_listener);
				}
				else {
					if (!password.getText().toString().equals(password_repeat.getText().toString())) {
						SketchwareUtil.showMessage(getApplicationContext(), "Passwords doesn't match");
						return;
					}
					user_signup.put("key", dbase.push().getKey());
					user_signup.put("name", name.getText().toString());
					user_signup.put("username", user.getText().toString());
					user_signup.put("password", password.getText().toString());
					user_signup.put("repeat_password", password_repeat.getText().toString());
					user_signup.put("mail", email_id.getText().toString());
					user_signup.put("dob", date_of_birth.getText().toString());
					dbauth.createUserWithEmailAndPassword(email_id.getText().toString(), password.getText().toString()).addOnCompleteListener(LoginActivity.this, _dbauth_create_user_listener);
					dbase.child(user_signup.get("key").toString()).updateChildren(user_signup);
					SketchwareUtil.showMessage(getApplicationContext(), "Sign Up Successful!");
				}
			}
		});
		
		sign_up.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				login_mode_text.setVisibility(View.GONE);
				sign_up.setVisibility(View.GONE);
				if (isLogin) {
					anim.setTarget(textview1);
					anim.setPropertyName("translationX");
					anim.setFloatValues((float)(0), (float)(-100));
					anim.setInterpolator(new DecelerateInterpolator());
					anim.setDuration((int)(500));
					anim.start();
					anim2.setTarget(textview1);
					anim2.setPropertyName("alpha");
					anim2.setFloatValues((float)(1), (float)(0));
					anim2.setInterpolator(new LinearInterpolator());
					anim2.setDuration((int)(500));
					anim2.start();
					timer = new TimerTask() {
						@Override
						public void run() {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									textview1.setText("Sign Up");
									login_btn.setText("Sign Up");
									anim.setTarget(textview1);
									anim.setPropertyName("translationX");
									anim.setFloatValues((float)(100), (float)(0));
									anim.setInterpolator(new DecelerateInterpolator());
									anim.setDuration((int)(500));
									anim.start();
									anim2.setTarget(textview1);
									anim2.setPropertyName("alpha");
									anim2.setFloatValues((float)(0), (float)(1));
									anim2.setInterpolator(new LinearInterpolator());
									anim2.setDuration((int)(500));
									anim2.start();
									textview1.setVisibility(View.VISIBLE);
									username_container.setVisibility(View.GONE);
									password_container.setVisibility(View.GONE);
									signup_flds_con_h.setVisibility(View.INVISIBLE);
									anim3.setTarget(signup_flds_con_h);
									anim3.setPropertyName("translationX");
									anim3.setFloatValues((float)(100), (float)(0));
									anim3.setInterpolator(new DecelerateInterpolator());
									anim3.setDuration((int)(500));
									anim3.start();
									anim4.setTarget(textview1);
									anim4.setPropertyName("alpha");
									anim4.setFloatValues((float)(0), (float)(1));
									anim4.setInterpolator(new LinearInterpolator());
									anim4.setDuration((int)(500));
									anim4.start();
									signup_flds_con_h.setVisibility(View.VISIBLE);
									isLogin = false;
									login_mode_text.setText("Already have account?");
									sign_up.setText("Login");
								}
							});
						}
					};
					_timer.schedule(timer, (int)(1000));
					login_mode_text.setVisibility(View.VISIBLE);
					sign_up.setVisibility(View.VISIBLE);
				}
				else {
					anim.setTarget(textview1);
					anim.setPropertyName("translationX");
					anim.setFloatValues((float)(0), (float)(-100));
					anim.setInterpolator(new DecelerateInterpolator());
					anim.setDuration((int)(500));
					anim.start();
					anim2.setTarget(textview1);
					anim2.setPropertyName("alpha");
					anim2.setFloatValues((float)(1), (float)(0));
					anim2.setInterpolator(new LinearInterpolator());
					anim2.setDuration((int)(500));
					anim2.start();
					timer = new TimerTask() {
						@Override
						public void run() {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									textview1.setText("Login");
									login_btn.setText("Login");
									anim.setTarget(textview1);
									anim.setPropertyName("translationX");
									anim.setFloatValues((float)(100), (float)(0));
									anim.setInterpolator(new DecelerateInterpolator());
									anim.setDuration((int)(500));
									anim.start();
									anim2.setTarget(textview1);
									anim2.setPropertyName("alpha");
									anim2.setFloatValues((float)(0), (float)(1));
									anim2.setInterpolator(new LinearInterpolator());
									anim2.setDuration((int)(500));
									anim2.start();
									textview1.setVisibility(View.VISIBLE);
									signup_flds_con_h.setVisibility(View.GONE);
									username_container.setVisibility(View.INVISIBLE);
									password_container.setVisibility(View.INVISIBLE);
									anim5.setTarget(username_container);
									anim5.setPropertyName("translationX");
									anim5.setFloatValues((float)(100), (float)(0));
									anim5.setInterpolator(new DecelerateInterpolator());
									anim5.setDuration((int)(500));
									anim5.start();
									anim6.setTarget(username_container);
									anim6.setPropertyName("alpha");
									anim6.setFloatValues((float)(0), (float)(1));
									anim6.setInterpolator(new LinearInterpolator());
									anim6.setDuration((int)(500));
									anim6.start();
									anim3.setTarget(password_container);
									anim3.setPropertyName("translationX");
									anim3.setFloatValues((float)(100), (float)(0));
									anim3.setInterpolator(new DecelerateInterpolator());
									anim3.setDuration((int)(500));
									anim3.start();
									anim4.setTarget(password_container);
									anim4.setPropertyName("alpha");
									anim4.setFloatValues((float)(0), (float)(1));
									anim4.setInterpolator(new LinearInterpolator());
									anim4.setDuration((int)(500));
									anim4.start();
									username_container.setVisibility(View.VISIBLE);
									password_container.setVisibility(View.VISIBLE);
									isLogin = true;
									login_mode_text.setText("Not an user?");
									sign_up.setText("Sign Up");
								}
							});
						}
					};
					_timer.schedule(timer, (int)(1000));
					login_mode_text.setVisibility(View.VISIBLE);
					sign_up.setVisibility(View.VISIBLE);
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

				if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
					intnt.putExtra("userid", FirebaseAuth.getInstance().getCurrentUser().getEmail());
					intnt.setClass(getApplicationContext(), HomeActivity.class);
					startActivity(intnt);
					finish();
				}
				else {
					SketchwareUtil.showMessage(getApplicationContext(), "Username or password is Invalid");
				}

				
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
		signup_flds_con_h.setVisibility(View.GONE);
		setTitle("Login");
		try{
			if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
				intnt.putExtra("userid", FirebaseAuth.getInstance().getCurrentUser().getEmail());
				intnt.setClass(getApplicationContext(), HomeActivity.class);
				startActivity(intnt);
				finish();
			}
			isLogin = true;
		}
		catch(Exception e)
		{
			SketchwareUtil.showMessage(getApplicationContext(), "Not logged in already");
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
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
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
