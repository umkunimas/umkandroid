package com.example.rog.umk.Profile;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.rog.umk.Adapter.SectionPageAdapter;
import com.example.rog.umk.Admin.solve;
import com.example.rog.umk.Admin.unsolve;
import com.example.rog.umk.Helper.RequestHandler;
import com.example.rog.umk.MainActivity;
import com.example.rog.umk.Order.generateQr;
import com.example.rog.umk.Product.addNewProduct;
import com.example.rog.umk.R;
import com.example.rog.umk.User.earning;
import com.example.rog.umk.User.expenses;
import com.example.rog.umk.User.likeList;
import com.example.rog.umk.User.userHelp;
import com.example.rog.umk.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import static android.view.View.GONE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class userProfile extends Fragment implements View.OnClickListener {
    private ViewPager viewPager;
    private SectionPageAdapter mSectionPageAdapter;
    private TextView location, tel, balance, join;
    private TextView name, titleBox, titleBox2;
    Spinner spn;
    static String email, img1;
    private ImageView img;
    SharedPreferences prefs;
    String result;
    CardView card;
    LinearLayout card2;
    TextView card3, card4;
    Button add;
    public boolean isLogin;
    String UPLOAD_URL = "https://umk-jkms.com/mobile/manualAdd.php",tag;
    String price, tag2;
    int M_STATE = 0;
    public static userProfile newInstance(){
        userProfile fragment = new userProfile();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.topnav, menu);
        super.onCreateOptionsMenu(menu, inflater);
        if (M_STATE == 1){
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setVisible(false);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        isLogin = prefs.getBoolean("isLogin", false);
        View rv = null;
        String usrType = prefs.getString("type","none");
        if (isLogin) {
            rv = inflater.inflate(R.layout.user_profile, container, false);
            Toolbar myToolbar = (Toolbar) rv.findViewById(R.id.my_toolbar);
            ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(myToolbar);
            name = rv.findViewById(R.id.name);
            location = rv.findViewById(R.id.division);
            tel = rv.findViewById(R.id.tel);
            card = rv.findViewById(R.id.card);
            card2 = rv.findViewById(R.id.card2);
            card3 = rv.findViewById(R.id.card3);
            card4 = rv.findViewById(R.id.card4);
            balance = rv.findViewById(R.id.balance);
            join = rv.findViewById(R.id.join);
            img = rv.findViewById(R.id.imageView);
            add = rv.findViewById(R.id.add);
            add.setOnClickListener(this);
            mSectionPageAdapter = new SectionPageAdapter(getChildFragmentManager());
            email = prefs. getString("username","none");
            viewPager = rv.findViewById(R.id.viewpager);
            setupViewPager(viewPager);
            TabLayout tabLayout = rv.findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            System.out.println("profile");
            if (usrType.equals("buyer")){
                card.setVisibility(GONE);
                card2.setVisibility(GONE);
                card3.setVisibility(GONE);
                card4.setVisibility(GONE);
                add.setVisibility(GONE);
                balance.setVisibility(GONE);
                location.setVisibility(GONE);
                M_STATE = 1;
            }
            loadUser();
        }
        else{
            Intent intent = new Intent(getActivity(), MainActivity.class);// This intent will be initiated
            startActivity(intent);
        }
        return rv;
    }
    public void loadUser(){
        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        String URL_PRODUCTS = "https://umk-jkms.com/mobile/loadUser.php?id=" + email;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                //getting product object from json array
                                JSONObject response1 = array.getJSONObject(i);
                                name.setText(response1.getString("name"));
                                tel.setText(response1.getString("tel"));
                                location.setText(response1.getString("location"));
                                balance.setText(response1.getString("bal"));
                                join.setText(response1.getString("joined"));
                                img1 = response1.getString("img");
                                setUser();
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }
    public void setUser(){
            Glide.with(this)
                    .load(img1)
                    .transition(withCrossFade())
                    .into(img);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.navigation_help:
                intent = new Intent(getContext(), askForHelp.class);
                startActivity(intent);
                System.out.println("help");
                return true;
            case R.id.review:
                intent = new Intent (getContext(), userHelp.class);
                startActivity(intent);
                return true;
            case R.id.navigation_add:
                intent = new Intent(getContext(), addNewProduct.class);
                startActivity(intent);
                System.out.println("add");
                return true;
            case R.id.navigation_logout:
                prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.commit();
                getActivity().finish();
                Toast.makeText(getContext(), "Successfully logged out", Toast.LENGTH_SHORT).show();
                intent = new Intent(getContext(), MainActivity.class);// This intent will be initiated
                startActivity(intent);
                return true;
            case R.id.navigation_edit:
                intent = new Intent(getContext(), profile.class);
                intent.putExtra("sellerEmail", email);
                startActivity(intent);
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    private void setupViewPager(ViewPager vp){
        SectionPageAdapter adapter = new SectionPageAdapter(getChildFragmentManager());
        String userType = prefs.getString("type","none");
        if (userType.equals("seller") || userType.equals("koperasi")) {
            adapter.addFragment(new earning(), "Earning");
            adapter.addFragment(new expenses(), "Expense");
            adapter.addFragment(new solve(), "Solve");
            adapter.addFragment(new unsolve(), "Unsolve");
        }
        else if (userType.equals("buyer")){
            adapter.addFragment(new likeList(), "Like");
        }
        vp.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == add){
            String titleName="", titleName2="";

            LayoutInflater li = LayoutInflater.from(getContext());
            View promptsView = li.inflate(R.layout.custombox2, null);
            titleName = "Enter Total Price";
            titleName2 = "Enter Activity";
            titleBox = promptsView.findViewById(R.id.title);
            titleBox2 = promptsView.findViewById(R.id.title2);
            spn = promptsView.findViewById(R.id.spinner1);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);
            titleBox.setText(titleName);
            titleBox2.setText(titleName2);
            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.input);
            final EditText userInput2 = (EditText) promptsView.findViewById(R.id.input2);
            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // get user input and set it to result
                                    // edit text
                                    price = userInput.getText().toString();
                                    tag = userInput2.getText().toString();
                                    tag2 = spn.getSelectedItem().toString();
                                    goUpload();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }
    private void goUpload() {
        class UploadImage extends AsyncTask<String, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getContext(), "Updating profile", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {//This part involves the php codes
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("success")) {// IF in php, the data was found AND IF the echo produced is "Correct", then...
                    Toast.makeText(getContext(), "Balance Updated", Toast.LENGTH_LONG).show();
                    loadUser();
                } else {
                    Toast.makeText(getContext(), "Cannot Update", Toast.LENGTH_LONG).show();
                    System.out.println("s" + s);
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<>();

                data.put("price", price);
                data.put("tag", tag);
                data.put("tag2", tag2);
                data.put("email" , email);
                String result = rh.sendPostRequest(UPLOAD_URL, data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute();
    }
}
