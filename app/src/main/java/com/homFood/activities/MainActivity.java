package com.homFood.activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.homFood.R;
import com.homFood.fragments.Categories;
import com.homFood.fragments.MainFragment;
import com.homFood.networking.Urls;
import com.karan.churi.PermissionManager.PermissionManager;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String customer_id = "";
    public static String email = "";
    public static String Name = "";
    public static String password = "";
    public static String phone = "";
    public static String user_image = "";
    public static String Address = "";
//    public static int available;
//    public static int type = 0;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Dialog myDialog;
    public static MenuItem filter_icon, cart_icon;
    public static String selected_item;
    PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionManager = new PermissionManager() {
        };
        permissionManager.checkAndRequestPermissions(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        myDialog = new Dialog(this);
//        -----------------------------------------------------------
        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            customer_id = intent.getExtras().getString("customer_id", "");
            email = intent.getExtras().getString("email", "");
            Name = intent.getExtras().getString("Name", "");
            password = intent.getExtras().getString("password", "");
            phone = intent.getExtras().getString("Phone", "");
            user_image = intent.getExtras().getString("img", "");
            Address = intent.getExtras().getString("Address", "");
//            available = intent.getExtras().getInt("available", 0);
//            type = intent.getExtras().getInt("type", -1);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header_view = navigationView.getHeaderView(0);
        LinearLayout header_nav_logo = header_view.findViewById(R.id.nav_logo);
        ImageView header_img = header_view.findViewById(R.id.main_profile_image);
        TextView header_name = header_view.findViewById(R.id.nav_name_txt_id);

        // check type to know that is login or not ....
        Menu nav_Menu = navigationView.getMenu();
        header_nav_logo.setVisibility(View.VISIBLE);
        nav_Menu.findItem(R.id.nav_my_orders).setVisible(true);
        nav_Menu.findItem(R.id.settings).setVisible(true);
        header_name.setText(Name);
        Picasso.with(MainActivity.this)
                .load(Urls.base_Images_Url + user_image)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(header_img);
        nav_Menu.findItem(R.id.logout).setVisible(true);

        //Select Home by default
        MainFragment fragment = new MainFragment();
        displaySelectedFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("هل تريد الخروج من التطبيق ؟")
                    .setCancelable(false)
                    .setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //super.onBackPressed();
                    pref = getSharedPreferences(LogIn.MY_PREFS_NAME, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.clear();
                    editor.apply();

                    // -------------------------------------------------------

                    startActivity(new Intent(MainActivity.this, LogIn.class));
                    finish();

                }
            }).create().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        filter_icon = menu.findItem(R.id.filter_icon);
        cart_icon = menu.findItem(R.id.card);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.card) {
            if (!customer_id.equals("0")) {
                startActivity(new Intent(MainActivity.this, Cart.class));
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("يجب بتسجيل الدخول اولا")
                        .setCancelable(false)
                        .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).create().show();
            }
        } else if (id == R.id.filter_icon) {
            show_popup();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void show_popup() {
        final String[] data = getResources().getStringArray(R.array.popup_list);
        TextView txt_close;
        ListView filter_list;
        myDialog.setContentView(R.layout.filter_popup_window);
        txt_close = myDialog.findViewById(R.id.txt_close_id);
        filter_list = myDialog.findViewById(R.id.filter_list);
        txt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, data);
        filter_list.setAdapter(adapter);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        myDialog.show();
        filter_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected_item = data[i];
                Bundle bundle = new Bundle();
                bundle.putString("selected_item", selected_item);
                MainFragment fragobj = new MainFragment();
                fragobj.setArguments(bundle);
                displaySelectedFragment(fragobj);
                myDialog.dismiss();
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_home) {
            cart_icon.setVisible(true);
            filter_icon.setVisible(true);
            fragment = new MainFragment();
            displaySelectedFragment(fragment);

        } else if (id == R.id.nav_categories) {
            fragment = new Categories();
            displaySelectedFragment(fragment);

        } else if (id == R.id.add_product) {
            startActivity(new Intent(MainActivity.this, AddMeal.class));

        } else if (id == R.id.nav_nearBy) {
            startActivity(new Intent(MainActivity.this, NearByPlacses.class));

        } else if (id == R.id.nav_sreach) {
            startActivity(new Intent(MainActivity.this, Search.class));

        } else if (id == R.id.nav_my_orders) {
            if (customer_id.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("يجب تسجيل الدخول اولا")
                        .setCancelable(false)
                        .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else {
                startActivity(new Intent(MainActivity.this, Orders.class));
            }

        } else if (id == R.id.nav_ordered_from_me) {
            if (customer_id.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("يجب تسجيل الدخول اولا")
                        .setCancelable(false)
                        .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else {
                startActivity(new Intent(MainActivity.this, OrderedFromMe.class));
            }

        } else if (id == R.id.nav_my_products) {
            if (customer_id.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("يجب تسجيل الدخول اولا")
                        .setCancelable(false)
                        .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            } else {
                startActivity(new Intent(MainActivity.this, MyProducts.class));
            }

        } else if (id == R.id.nav_my_coupons) {
            if (customer_id.equals("")) {
                Toast.makeText(this, "يجب تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(MainActivity.this, DiscountCoupons.class));
            }
        } else if (id == R.id.nav_messages) {
            if (customer_id.equals("")) {
                Toast.makeText(this, "يجب تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(MainActivity.this, Chatters.class));
            }
        } else if (id == R.id.nav_sreach) {
            startActivity(new Intent(MainActivity.this, Search.class));

        } else if (id == R.id.about_app) {
            startActivity(new Intent(MainActivity.this, AboutApp.class));

        } else if (id == R.id.contact) {
            startActivity(new Intent(MainActivity.this, ContactUs.class));

        } else if (id == R.id.settings) {
            Intent intent = new Intent(MainActivity.this, Settings.class);
            intent.putExtra("id", customer_id);
            startActivity(intent);

        } else if (id == R.id.share_app) {

            int applicationNameId = this.getApplicationInfo().labelRes;
            final String appPackageName = this.getPackageName();
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getString(applicationNameId));
            String text = "Install this cool application: ";
            String link = "https://play.google.com/store/apps/details?id=" + appPackageName;
            i.putExtra(Intent.EXTRA_TEXT, text + " " + link);
            startActivity(Intent.createChooser(i, "Share link:"));

        } else if (id == R.id.rate_app) {

            Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
            }
        } else if (id == R.id.logout) {

            // you should clear shared preferences first, then finish the activity ...
            pref = getSharedPreferences(LogIn.MY_PREFS_NAME, Context.MODE_PRIVATE);
            editor = pref.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(MainActivity.this, LogIn.class));
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Loads the specified fragment to the frame
     *
     * @param fragment
     */
    private void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
