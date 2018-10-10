package talitha_koum.milipade.com.app.afdis.fragments;

import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import talitha_koum.milipade.com.app.afdis.App;
import talitha_koum.milipade.com.app.afdis.R;
import talitha_koum.milipade.com.app.afdis.activities.MainActivity;
import talitha_koum.milipade.com.app.afdis.data.AfdisController;
import talitha_koum.milipade.com.app.afdis.models.Location;
import talitha_koum.milipade.com.app.afdis.models.Shop;
import talitha_koum.milipade.com.app.afdis.network.ApiClient;
import talitha_koum.milipade.com.app.afdis.network.ApiInterface;
import talitha_koum.milipade.com.app.afdis.responses.LoginResponse;
import talitha_koum.milipade.com.app.afdis.utils.CustomToast;
import talitha_koum.milipade.com.app.afdis.utils.Utils;




public class Login_Fragment extends Fragment implements OnClickListener {
	private static View view;

	private static EditText emailid, password;
	private static Button loginButton;
	private static TextView forgotPassword, signUp;
	private static CheckBox show_hide_password;
	private static LinearLayout loginLayout;
	private static Animation shakeAnimation;
	private static FragmentManager fragmentManager;
	private String accountType;
	private AccountManager accountManager;
    private AfdisController DBcontroller;
	// Creating progress dialog.
	ProgressDialog progressDialog;

	// Creating FirebaseAuth object.



	public Login_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.login_layout, container, false);

		progressDialog =  new ProgressDialog(getActivity());


        DBcontroller = new AfdisController(getActivity());

		initViews();
		setListeners();
		return view;
	}

	// Initiate Views
	private void initViews() {
		fragmentManager = getActivity().getSupportFragmentManager();
		emailid = (EditText) view.findViewById(R.id.login_emailid);
		password = (EditText) view.findViewById(R.id.login_password);
		loginButton = (Button) view.findViewById(R.id.loginBtn);
		forgotPassword = (TextView) view.findViewById(R.id.forgot_password);
		signUp = (TextView) view.findViewById(R.id.createAccount);
		show_hide_password = (CheckBox) view.findViewById(R.id.show_hide_password);
		loginLayout = (LinearLayout) view.findViewById(R.id.login_layout);
		// Load ShakeAnimation
		shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

		// Setting text selector over textviews
		XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
		try {
			ColorStateList csl = ColorStateList.createFromXml(getResources(), xrp);
			forgotPassword.setTextColor(csl);
			show_hide_password.setTextColor(csl);
			signUp.setTextColor(csl);
		} catch (Exception e) {
		}
	}

	// Set Listeners
	private void setListeners() {
		loginButton.setOnClickListener(this);
		forgotPassword.setOnClickListener(this);
		signUp.setOnClickListener(this);
		// Set check listener over checkbox for showing and hiding password
		show_hide_password
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton button,
							boolean isChecked) {

						// If it is checkec then show password else hide
						// password
						if (isChecked) {

							show_hide_password.setText(R.string.hide_pwd);
							password.setInputType(InputType.TYPE_CLASS_TEXT);
							password.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());// show password
						} else {
							show_hide_password.setText(R.string.show_pwd);
							password.setInputType(InputType.TYPE_CLASS_TEXT
									| InputType.TYPE_TEXT_VARIATION_PASSWORD);
							password.setTransformationMethod(PasswordTransformationMethod
									.getInstance());// hide password

						}

					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginBtn:
			checkValidation();
			break;

		case R.id.forgot_password:

			// Replace forgot password fragment with animation
			fragmentManager
					.beginTransaction()
					.setCustomAnimations(R.anim.right_enter, R.anim.left_out)
					.replace(R.id.frameContainer, new ForgotPassword_Fragment(), Utils.ForgotPassword_Fragment).commit();
			break;
		case R.id.createAccount:

			// Replace signup frgament with animation
			//fragmentManager
			//		.beginTransaction()
			//		.setCustomAnimations(R.anim.right_enter, R.anim.left_out)
			//		.replace(R.id.frameContainer, new SignUp_Fragment(), SignUp_Fragment).commit();
			break;
		}

	}

	// Check Validation before login
	private void checkValidation() {
		// Get email id and password
		String getEmailId = emailid.getText().toString();
		String getPassword = password.getText().toString();

		// Check patter for email id
		Pattern p = Pattern.compile(Utils.regEx);

		Matcher m = p.matcher(getEmailId);

		// Check for both field is empty or not
		if (getEmailId.equals("") || getEmailId.length() == 0
				|| getPassword.equals("") || getPassword.length() == 0) {
			loginLayout.startAnimation(shakeAnimation);
			new CustomToast().Show_Toast(getActivity(), view,
					"Enter both credentials.");

		}
		// Check if email id is valid or not
		else if (!m.find())
			new CustomToast().Show_Toast(getActivity(), view,
					"Your Email Id is Invalid.");
		// Else do login and do your stuff
		else
			login();

	}



	void login(){
		ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

		final String username = ((EditText) view.findViewById(R.id.login_emailid)).getText().toString();
		final String password = ((EditText) view.findViewById(R.id.login_password)).getText().toString();


		final ProgressDialog progressDialog = new ProgressDialog(getActivity());
		progressDialog.setIndeterminate(true);
		progressDialog.setMessage("Authenticating...");
		progressDialog.show();

        //MySharedPreferences.setLogin(getActivity().getBaseContext(),true);
        //progressDialog.dismiss();
        //getActivity().finish();

		Call<LoginResponse> call = apiService.login(username,  password);
		call.enqueue(new Callback<LoginResponse>() {
			@Override
			public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
				//Toast.makeText(getActivity().getBaseContext(), "response", Toast.LENGTH_LONG).show();
				progressDialog.dismiss();
				LoginResponse loginResponse = response.body();
				if(loginResponse.getStatus_code()!=201) {
					new CustomToast().Show_Toast(getActivity(), view, "something went wront");

				}
				if(loginResponse.getData().getStatus()){
                    App.getPrefManager(getContext()).storeUser(loginResponse.getData().getUser());
                    DBcontroller.open();
                    //if(loginResponse.getData().getUser().getLocation()!=null) {
                        for (Location location : loginResponse.getData().getUser().getLocation()) {
                            //if (location.getShops().size() < 0) {
                                for (Shop shop : location.getShops()) {
                                    DBcontroller.insertData(shop, location.getLocation_name());
                                }
                           // }
                        }
                        DBcontroller.close();
                        App.getPrefManager(getContext()).setLogin(true);
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    }else{
                        new CustomToast().Show_Toast(getActivity(), view, "error 2: something went wront");
                    }
				//}else{
				   // new CustomToast().Show_Toast(getActivity(), view,loginResponse.getData().getMessage() );
				//}

			}

			@Override
			public void onFailure(Call<LoginResponse> call, Throwable t) {
				//Toast.makeText(getActivity().getBaseContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                new CustomToast().Show_Toast(getActivity(), view,"Network Error Please try again" );
				progressDialog.dismiss();

			}
		});

	}
}
