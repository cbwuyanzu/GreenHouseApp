package cddddzzzz.greenhousetongji;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class IndoorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static final int IN_UPDATE_FAIL = 0;
    private TextView inTemp;
    private TextView inHum;
    private TextView inRad;
    private TextView inCO2;
    private TextView inUpdate;
    private Handler handler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case IN_UPDATE_FAIL:
                    Toast.makeText(IndoorActivity.this,"Update Failed",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    public OnClickListener updateIndoorListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Snackbar.make(v, "Updating indoor weather", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            new Thread() {
                public void run() {
                    updateIndoor();
                }
            }.start();
        }
    };

    public void updateIndoor() {
        try {
            String spec = "http://121.43.106.119:8020/indoor";
            URL url = new URL(spec);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            if (urlConnection.getResponseCode() == 200) {
                InputStream is = urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int len = 0;
                byte buffer[] = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                is.close();
                baos.close();
                String result = new String(baos.toByteArray());
                JSONObject jsonObject = new JSONObject(result);
                final JSONObject inJson = jsonObject.getJSONObject("1");
                IndoorActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            inTemp.setText(inJson.getString("temperature"));
                            inHum.setText(inJson.getString("humidity"));
                            inRad.setText(inJson.getString("radiation"));
                            inCO2.setText(inJson.getString("co2"));
                            inUpdate.setText(inJson.getString("update_time"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                Log.i("Connection", "connect failed.........");
            }
        } catch (Exception e) {
            e.printStackTrace();
            handler.obtainMessage(this.IN_UPDATE_FAIL).sendToTarget();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(updateIndoorListener);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        inTemp = (TextView) findViewById(R.id.in_temp);
        inHum = (TextView) findViewById(R.id.in_hum);
        inRad = (TextView) findViewById(R.id.in_rad);
        inCO2 = (TextView) findViewById(R.id.in_co2);
        inUpdate = (TextView) findViewById(R.id.in_update);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_out) {
            // Handle the camera action
            Intent intent=new Intent(IndoorActivity.this,MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_indoor) {

        } else if (id == R.id.nav_control) {
            Intent intent=new Intent(IndoorActivity.this,ControlActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
