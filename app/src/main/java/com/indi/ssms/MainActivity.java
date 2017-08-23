package com.indi.ssms;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.indi.adapters.DividerItemDecoration;
import com.indi.adapters.ListContactsAdapter;
import com.indi.mundo.UserBase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private TextView tvNoContacts, tvPressPlus, tvLoadingContacts;
    private ListContactsAdapter listContactsAdapter;
    private RecyclerView recyclerViewContacts;
    private ProgressBar progressBarLoadContacts;
    private SwipeRefreshLayout swipeRefreshLayoutMainActivity;
    private FirebaseAuth auth;
    private FirebaseDatabase fbDatabase;
    private ArrayList<UserBase> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fabShare = (FloatingActionButton) findViewById(R.id.fabShare);
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), ShareTextActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //El nav_header_main layout esta en la posicion 0 del headerView del navigationBar... cool
        View headerLayout =  navigationView.getHeaderView(0);

        auth = FirebaseAuth.getInstance();

        TextView tvUsername = (TextView) headerLayout.findViewById(R.id.tv_username_nav);
        tvUsername.setText(auth.getCurrentUser().getDisplayName());

        TextView tvEmail = (TextView) headerLayout.findViewById(R.id.tv_email_nav);
        tvEmail.setText(auth.getCurrentUser().getEmail());

        //Los textviews que indican que no tiene amiguis
        tvNoContacts = (TextView) findViewById(R.id.tv_no_contacts);
        tvPressPlus = (TextView) findViewById(R.id.tv_press_plus);
        tvLoadingContacts= (TextView) findViewById(R.id.tv_loading_contacts);

        recyclerViewContacts = (RecyclerView) findViewById(R.id.recyclerViewContacts);
        recyclerViewContacts.addItemDecoration(new DividerItemDecoration(this, null));
        recyclerViewContacts.setHasFixedSize(true);
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));

        progressBarLoadContacts = (ProgressBar) findViewById(R.id.progressBar_main_activity);

        swipeRefreshLayoutMainActivity = (SwipeRefreshLayout) findViewById(R.id.swiperefreshMainActivity);
        swipeRefreshLayoutMainActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                contacts = new ArrayList<>();
                cargarContactos();
            }
        });

        contacts = new ArrayList<>();

        cargarContactos();
        //Verificacion de que el usuario tenga al menos una clave privada.

    }

    private void cargarContactos() {


        //Por ahora a traer todos los registrados en la base firebase y agregarlos como contactos.
        //Primero saca una lista de contactos de FB
        sacarContactosFB();


        listContactsAdapter = new ListContactsAdapter(contacts, this);
        recyclerViewContacts.setAdapter(listContactsAdapter);
    }

    private void sacarContactosFB() {
        fbDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = fbDatabase.getReference("users");
        //Query query = ref.orderByChild("users").equalTo("users", auth.getCurrentUser().getUid());
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                UserBase newPost = dataSnapshot.getValue(UserBase.class);
                if(!newPost.email.equals(auth.getCurrentUser().getEmail())){
                    Log.i(TAG, "sacarContactosFB-->agrego contacto: " + newPost.username);
                    contacts.add(newPost);
                    listContactsAdapter.notifyDataSetChanged();
                }

                if(swipeRefreshLayoutMainActivity.isRefreshing())
                    swipeRefreshLayoutMainActivity.setRefreshing(false);

                if(contacts.size() == 0){
                    tvLoadingContacts.setVisibility(View.GONE);
                    tvNoContacts.setVisibility(View.VISIBLE);
                    tvPressPlus.setVisibility(View.VISIBLE);
                }
                else{
                    tvLoadingContacts.setVisibility(View.GONE);
                    tvNoContacts.setVisibility(View.GONE);
                    tvPressPlus.setVisibility(View.GONE);
                }


                progressBarLoadContacts.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            auth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void iniciarChat(UserBase contact) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("contact", contact);
        startActivity(intent);
    }
}
