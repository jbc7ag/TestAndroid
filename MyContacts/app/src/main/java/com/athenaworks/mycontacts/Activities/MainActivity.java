package com.athenaworks.mycontacts.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.athenaworks.mycontacts.Adapters.ContactsAdapter;
import com.athenaworks.mycontacts.Models.Contacts;
import com.athenaworks.mycontacts.Network.Client;
import com.athenaworks.mycontacts.Network.Service;
import com.athenaworks.mycontacts.Utils.Utils;
import com.athenaworks.mycontacts.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    ContactsAdapter adapter;
    ListView lv;
    ProgressBar progressBar;
    ProgressBar pbLoading;
    RelativeLayout rl;
    int Page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       initData();

    }

    @Override
    protected void onStart(){
        super.onStart();

        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)myActionMenuItem.getActionView();
        searchView.setQueryHint(getString(R.string.main_search));


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)){
                      adapter.filter("");
                      lv.clearTextFilter();
                }
                else {
                      adapter.filter(s);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }



    //new definitions
    public void initData(){

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.main_title);
        toolbar.inflateMenu(R.menu.menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        progressBar = findViewById(R.id.progressBar);
        pbLoading = findViewById(R.id.progressBarLoading);

        //init relative layout in case of error
        rl= findViewById(R.id.main_rel_error);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getData();
            }
        });

    }

    // get data for first time or in case of error is called again
    public void getData (){

        progressBar.setVisibility(View.VISIBLE );
        Context context= getApplication().getApplicationContext();
        Retrofit client = Client.Companion.getClient();

        Service service = client.create(Service.class);

        Call<Contacts> call = service.getContacts(1);


        call.enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, Response<Contacts> response) {
                int statusCode = response.code();

                if( statusCode == 200) {

                    Contacts contacts = response.body();
                    fillListView(contacts);
                    rl.setVisibility(View.GONE);

                }else{

                    setErrorPage();



                }
                progressBar.setVisibility(View.GONE );

            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {
                setErrorPage();
                progressBar.setVisibility(View.GONE );

            }
        });

    }

    public void fillListView(Contacts contacts){

        lv = findViewById(R.id.main_listView);
        adapter = new ContactsAdapter(this, contacts.getResults());
        lv.setAdapter(adapter);

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {

                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;

            }

            //Help us to check if the scroll is not moving in order to no overload the connection
            private void isScrollCompleted() {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount && this.currentScrollState == SCROLL_STATE_IDLE) {

                    if(Utils.Companion.isConnected(getApplicationContext())) {
                        getnewData();
                    }
                }
            }

        });


    }

    //let us show an error page in case of we dont have internet connection or something is wrong with the api
    public void setErrorPage(){

        rl.setVisibility(View.VISIBLE);
    }

    // Manage the new data from the Scroll update
    public void getnewData (){

        pbLoading.setVisibility(View.VISIBLE );
        Context context= getApplication().getApplicationContext();
        Retrofit client = Client.Companion.getClient();

        Service service = client.create(Service.class);

        Call<Contacts> call = service.getContacts(Page);


        call.enqueue(new Callback<Contacts>() {
            @Override
            public void onResponse(Call<Contacts> call, Response<Contacts> response) {
                int statusCode = response.code();

                if( statusCode == 200) {
                    Contacts contacts = response.body();
                    adapter.addNewData(contacts.getResults());
                }

                pbLoading.setVisibility(View.GONE );
            }

            @Override
            public void onFailure(Call<Contacts> call, Throwable t) {
                pbLoading.setVisibility(View.GONE );

            }
        });

        Page++;

    }




}
