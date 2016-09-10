package cddddzzzz.greenhousetongji;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ControlActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static final int CONTROL_UPDATE_FAIL = 0;
    private ListView triAct;
    private ListView biAct;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CONTROL_UPDATE_FAIL:
                    Toast.makeText(ControlActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    //    public OnClickListener updateIndoorListener = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Snackbar.make(v, "Updating indoor weather", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
//            new Thread() {
//                public void run() {
//                    updateIndoor();
//                }
//            }.start();
//        }
//    };
//
//    public void updateIndoor() {
//        try {
//            String spec = "http://121.43.106.119:8020/indoor";
//            URL url = new URL(spec);
//            HttpURLConnection urlConnection = (HttpURLConnection) url
//                    .openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.setReadTimeout(5000);
//            urlConnection.setConnectTimeout(5000);
//            if (urlConnection.getResponseCode() == 200) {
//                InputStream is = urlConnection.getInputStream();
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                int len = 0;
//                byte buffer[] = new byte[1024];
//                while ((len = is.read(buffer)) != -1) {
//                    baos.write(buffer, 0, len);
//                }
//                is.close();
//                baos.close();
//                String result = new String(baos.toByteArray());
//                JSONObject jsonObject = new JSONObject(result);
//                final JSONObject inJson = jsonObject.getJSONObject("1");
//                ControlActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            inTemp.setText(inJson.getString("temperature"));
//                            inHum.setText(inJson.getString("humidity"));
//                            inRad.setText(inJson.getString("radiation"));
//                            inCO2.setText(inJson.getString("co2"));
//                            inUpdate.setText(inJson.getString("update_time"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            } else {
//                Log.i("Connection", "connect failed.........");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            handler.obtainMessage(this.IN_UPDATE_FAIL).sendToTarget();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(updateIndoorListener);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        initActuator();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        triAct = (ListView) findViewById(R.id.list_tri);
        TriAdapter triAdapter = new TriAdapter(this);
        triAct.setAdapter(triAdapter);
        biAct = (ListView) findViewById(R.id.list_bi);
        BiAdapter biAdapter = new BiAdapter(this);
        biAct.setAdapter(biAdapter);

    }

    private void initActuator() {
        Actuator roofSouth = new Actuator("roof_vent_south", "roof(south)");
        Actuator roofNorth = new Actuator("roof_vent_north", "roof(north)");
        Actuator sideVent = new Actuator("side_vent", "side vent");
        Actuator shadeNorth = new Actuator("shade_screen_north", "screen(north)");
        Actuator shadeSouth = new Actuator("shade_screen_south", "screen(south)");
        Actuator thermalInside = new Actuator("thermal_screen", "screen(inside)");
        triList.add(roofSouth);
        triList.add(roofNorth);
        triList.add(sideVent);
        triList.add(shadeNorth);
        triList.add(shadeSouth);
        triList.add(thermalInside);
        Actuator coolPump = new Actuator("cooling_pump", "cooling pad");
        Actuator coolFan = new Actuator("cooling_fan", "force vent");
        Actuator fan = new Actuator("fan", "circulating");
        Actuator fog = new Actuator("fogging", "fogging");
        Actuator heating = new Actuator("heating", "heating");
        Actuator co2 = new Actuator("co2", "CO2 enrichment");
        Actuator lighting_1 = new Actuator("lighting_1", "lighting(1)");
        Actuator lighting_2 = new Actuator("lighting_2", "lighting(2)");
        biList.add(coolPump);
        biList.add(coolFan);
        biList.add(fan);
        biList.add(fog);
        biList.add(heating);
        biList.add(co2);
        biList.add(lighting_1);
        biList.add(lighting_2);
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
            Intent intent = new Intent(ControlActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_indoor) {
            Intent intent = new Intent(ControlActivity.this, IndoorActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_control) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private ArrayList<Actuator> triList = new ArrayList<Actuator>();
    private ArrayList<Actuator> biList = new ArrayList<Actuator>();


    private class TriAdapter extends BaseAdapter {
        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局 /*构造函数*/

        public TriAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {

            return triList.size();//返回数组的长度
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item,
                        null);
                holder = new ViewHolder();
                    /*得到各个控件的对象*/
                holder.title = (TextView) convertView.findViewById(R.id.ItemTitle);
                holder.bt1 = (Button) convertView.findViewById(R.id.ItemButton_on);
                holder.bt2 = (Button) convertView.findViewById(R.id.ItemButton_off);
                holder.bt3 = (Button) convertView.findViewById(R.id.ItemButton_stop);
                convertView.setTag(holder);//绑定ViewHolder对象
            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.title.setText(triList.get(position).getLabel());
            holder.bt1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    triList.get(position).setCommand("on");
                    Log.i("Json", triList.get(position).toJString());
                    new Thread() {
                        public void run() {
                            postControl(triList.get(position).toJString());
                        }
                    }.start();
                }
            });
            holder.bt2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    triList.get(position).setCommand("off");
                    Log.i("Json", triList.get(position).toJString());
                    new Thread() {
                        public void run() {
                            postControl(triList.get(position).toJString());
                        }
                    }.start();

                }
            });
            holder.bt3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    triList.get(position).setCommand("stop");
                    Log.i("Json", triList.get(position).toJString());
                    new Thread() {
                        public void run() {
                            postControl(triList.get(position).toJString());
                        }
                    }.start();
                }
            });
            return convertView;
        }
    }/*存放控件*/

    public final class ViewHolder {
        public TextView title;
        public Button bt1;
        public Button bt2;
        public Button bt3;
    }

    private class BiAdapter extends BaseAdapter {
        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局 /*构造函数*/

        public BiAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {

            return biList.size();//返回数组的长度
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder2 holder;
            //观察convertView随ListView滚动情况
            Log.v("MyListViewBase", "getView " + position + " " + convertView);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item2,
                        null);
                holder = new ViewHolder2();
                    /*得到各个控件的对象*/
                holder.title = (TextView) convertView.findViewById(R.id.Item2Title);
                holder.bt1 = (Button) convertView.findViewById(R.id.Item2Button_on);
                holder.bt2 = (Button) convertView.findViewById(R.id.Item2Button_off);
                convertView.setTag(holder);//绑定ViewHolder对象
            } else {
                holder = (ViewHolder2) convertView.getTag();//取出ViewHolder对象
            }
            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.title.setText(biList.get(position).getLabel());
            holder.bt1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    biList.get(position).setCommand("on");
                    Log.i("Json", biList.get(position).toJString());
                    new Thread() {
                        public void run() {
                            postControl(biList.get(position).toJString());
                        }
                    }.start();
                }
            });
            holder.bt2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    biList.get(position).setCommand("off");
                    Log.i("Json", biList.get(position).toJString());
                    new Thread() {
                        public void run() {
                            postControl(biList.get(position).toJString());
                        }
                    }.start();
                }
            });
            return convertView;
        }
    }/*存放控件*/

    public final class ViewHolder2 {
        public TextView title;
        public Button bt1;
        public Button bt2;
    }


    public void postControl(String s) {

        try {
//            String encodeData = URLEncoder.encode(s,"UTF-8");
            String spec = "http://121.43.106.119:8020/control";
            URL url = new URL(spec);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            OutputStream os = urlConnection.getOutputStream();
            Log.i("POST", "RQST" + s);
            os.write(s.getBytes());
            os.flush();
            os.close();
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
                Log.i("POST RSP", result);
//                JSONObject jsonObject = new JSONObject(result);
//                final JSONObject inJson = jsonObject.getJSONObject("1");
//                IndoorActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            inTemp.setText(inJson.getString("temperature"));
//                            inHum.setText(inJson.getString("humidity"));
//                            inRad.setText(inJson.getString("radiation"));
//                            inCO2.setText(inJson.getString("co2"));
//                            inUpdate.setText(inJson.getString("update_time"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
            } else {
                Log.i("Connection", "connect failed.........");
            }
        } catch (Exception e) {
            e.printStackTrace();
            handler.obtainMessage(this.CONTROL_UPDATE_FAIL).sendToTarget();
        }
    }
}


