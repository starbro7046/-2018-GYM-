package gamgultony.seoulgym;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.LocaleDisplayNames;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String Url = "http://openAPI.seoul.go.kr:8088/4b654f74617374613734776346734a/json/ListPublicReservationSport/1/308/";
    String[] items = {"없음", "축구장", "야구장", "농구장", "배구장", "배드민턴장", "족구장", "테니스장", "풋살경기장", "다목적경기장", "체육관", "운동장",};//spinner
    String service = null;
    boolean isRunning = false;
    String dataA;
    int value;
    StringBuilder output;
    private List<HashMap<String,String>> infoList = null;
    private List<HashMap<String,String>> infoList2 = null;;
    private SimpleAdapter adapter = null;
    public static final int LOAD_SUCCESS = 101;
    private ProgressDialog progressDialog = null;
    private List<String> list;
    JSONObject listData;
    JSONObject listData2;
    JSONArray jsonArray;
    HashMap<String, String> infoMap;
    String name2;
    String url2;
    String athletic2;
    String  attr2;
    boolean end=false;
    int[] to;
    String[] from;
    String name;
    String url;
    String athletic;
    String  attr;
    Handler handler = new Handler();
    ListView listView;
    SharedPreferences.Editor attrEd3;
    SharedPreferences attr3;
    String mAttr1;
    String mAttr2;
    int mAttr3;
    String can=null;
    String can2=null;
    String mAttr222=null;
    EditText editTextFilter=null;
    int ab=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextFilter = (EditText)findViewById(R.id.editText) ;
        attr3=getSharedPreferences("SETTINGS",Activity.MODE_PRIVATE);
        mAttr1=attr3.getString("payMent","적용안함");
        mAttr2=attr3.getString("reservation","적용안함");
        mAttr3=attr3.getInt("athletic",0);

        TextView abc1=(TextView)findViewById(R.id.abc1);
        TextView abc2=(TextView)findViewById(R.id.abc2);
        TextView abc3=(TextView)findViewById(R.id.abc3);
        abc1.setText(mAttr1);
        abc2.setText(mAttr2);
        abc3.setText(items[mAttr3]);
        if (mAttr2.equals("접수가능"))
        {
            ab=1;
        }else
        {
            ab=2;
        }
        /*if(mAttr2.equals("접수가능"))
        {
            Log.d("aaaaaaaa","돼");
            mAttr2="접수중";
            mAttr222="안내중";
        }else if(mAttr2.equals("접수불가")) {
            Log.d("aaaaaaaa","안돼");
            mAttr2 = "접수종료";
            mAttr222="예약일시중지";
        }
        */
        listView = (ListView)findViewById(R.id.list);
        infoList = new ArrayList<HashMap<String,String>>();
        from = new String[]{"name","attr","url"};
        to = new int[] {R.id.name, R.id.attr, R.id.url, };
        adapter = new SimpleAdapter(this, infoList, R.layout.item, from, to);
        listView.setAdapter(adapter);
        final HashMap<String, String> infoMap = null;

        list = new ArrayList<String>();


        ConnectThread thread = new ConnectThread(Url);
        thread.start();
        //ui
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ImforActivity.class);

                try {
                    JSONObject listData3=jsonArray.getJSONObject(position);
                    intent.putExtra("title",listData3.getString("SVCNM"));//서비스명
                    intent.putExtra("loc",listData3.getString("PLACENM"));//위치
                    intent.putExtra("pay",listData3.getString("PAYATNM"));//결제방법
                    intent.putExtra("ath",listData3.getString("MINCLASSNM"));//종목
                    intent.putExtra("can",listData3.getString("SVCSTATNM"));//접수가능
                    intent.putExtra("tel",listData3.getString("TELNO"));//전화번호
                    intent.putExtra("st",listData3.getString("V_MIN"));//시작시간
                    intent.putExtra("et",listData3.getString("V_MAX"));//끝나는시간
                    intent.putExtra("url",listData3.getString("SVCURL"));
                    Log.d("aaaaaa",listData3.getString("SVCSTATNM"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        Button btn1 = (Button) findViewById(R.id.search);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog( MainActivity.this );
                progressDialog.setMessage("로딩중...");
                progressDialog.show();
                ConnectThread thread = new ConnectThread(Url);
                thread.start();
            }
        });
        Button btn2 = (Button) findViewById(R.id.filter);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });
        editTextFilter = (EditText)findViewById(R.id.editText) ;
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String text = editTextFilter.getText().toString();
                infoList.clear();
                if(jsonArray!=null) {
                    if(text.length()==0)
                    {
                        Log.d("aaaaaaa","전부 보여줌");
                        showAll();
                    }else {
                        Log.d("aaaaaaa", "서칭");
                        search(text);
                    }
                }
            }
        });

    }
    public void a()
    {

    }
    public boolean check(String payMent22,String can22,String athletic22 )
    {
        if((payMent22.contains(mAttr1)) || (mAttr1.equals("적용안함")))
        {
         //if((can22.contains(mAttr2)||(attr3.getString("reservation","적용안함").equals("적용안함"))||(can22.contains(mAttr222))))
         if (((ab==1)&&((can22.equals("접수중"))||(can22.equals("안내중"))))||(ab==2)&&((can22.equals("접수종료"))||(can22.equals("예약일시중지")))  )
         {
             Log.d("aaaaa",Integer.toString(ab));
             Log.d("aaaaa",can22);
             if((athletic22.contains(items[mAttr3])) ||(items[mAttr3].equals("없음")))
             {
                 return true;
             }
         }
        }
        return false;
    }
    public void showAll()
    {
        for(int i=0;i<jsonArray.length();i++)
        {
            try
            {
                listData2=jsonArray.getJSONObject(i);
                infoMap = new HashMap<String, String>();

                name2=listData2.getString("SVCNM");
                url2=listData2.getString("SVCURL");
                athletic2=listData2.getString("MINCLASSNM");
                attr2=listData2.getString("PAYATNM");
                can2=listData2.getString("SVCSTATNM");
                if(check(attr2,can2,athletic2)) {
                    infoMap.put("name", name2);
                    infoMap.put("attr", attr2);
                    infoMap.put("url", url2);
                    infoList.add(infoMap);
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }
    public void search(String text)
    {
        for(int i=0;i<jsonArray.length();i++)
        {
            try
            {
                listData2=jsonArray.getJSONObject(i);
                if(listData2.getString("SVCNM").contains(text))
                {
                    infoMap = new HashMap<String, String>();

                    name2=listData2.getString("SVCNM");
                    url2=listData2.getString("SVCURL");
                    athletic2=listData2.getString("MINCLASSNM");
                    attr2=listData2.getString("PAYATNM");
                    can2=listData2.getString("SVCSTATNM");
                    if(check(attr2,can2,athletic2)) {
                        infoMap.put("name", name2);
                        infoMap.put("attr", attr2);
                        infoMap.put("url", url2);
                        infoList.add(infoMap);
                    }
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            //Log.d("aaaaaaaa",name + url + athletic+ attr);

        }
        adapter.notifyDataSetChanged();
    }
    private final MyHandler mHandler = new MyHandler(this);
    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> weakReference;


        public MyHandler(MainActivity mainactivity) {
            weakReference = new WeakReference<MainActivity>(mainactivity);
        }
        @Override
        public void handleMessage(Message msg) {

            MainActivity mainactivity = weakReference.get();

            if (mainactivity != null) {
                switch (msg.what) {

                    case LOAD_SUCCESS:
                        mainactivity.progressDialog.dismiss();
                        mainactivity.adapter.notifyDataSetChanged();
                        break;
                }
            }
        }
    }
    class ConnectThread extends Thread {
        String urlStr;

        public ConnectThread(String inStr) {
            urlStr = inStr;
        }

        public void run() {

            try {
                final String output = request(urlStr);
                //final TextView textView = (TextView) findViewById(R.id.textView);
                JSONObject jsonObject = new JSONObject(output);
                //jsonObject=jsonObject.getJSONObject("ListPublicReservationSport");
                jsonArray=jsonObject.getJSONObject("ListPublicReservationSport").getJSONArray("row");
                Log.d("aaaaaaaaaa","전");
                for(int i=0;i<jsonArray.length();i++)
                {
                    listData=jsonArray.getJSONObject(i);
                    name=listData.getString("SVCNM");
                    url=listData.getString("SVCURL");
                    athletic=listData.getString("MINCLASSNM");
                    attr=listData.getString("PAYATNM");
                    can=listData.getString("SVCSTATNM");
                    //Log.d("aaaaaaaa",name + url + athletic+ attr);
                    if(check(attr,can,athletic)) {
                        infoMap = new HashMap<String, String>();
                        infoMap.put("name", name);
                        infoMap.put("attr", attr);
                        infoMap.put("url", url);
                        infoList.add(infoMap);
                    }
                }
                Log.d("aaaaaaaa","후");

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            handler.post(new Runnable() {
                public void run() {
                    editTextFilter.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                }
            });

        }

        private String request(String urlStr) {
            output = new StringBuilder();
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    int resCode = conn.getResponseCode();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        output.append(line + "\n");
                    }

                    reader.close();
                    conn.disconnect();
                }
            } catch (Exception ex) {
                Log.e("aaaaaaaaaaaa", "Exception in processing response.", ex);
                ex.printStackTrace();
            }

            return output.toString();
        }

    }
}
