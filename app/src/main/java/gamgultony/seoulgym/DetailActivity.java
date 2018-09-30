package gamgultony.seoulgym;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class DetailActivity extends AppCompatActivity {

    SharedPreferences.Editor attrEd;
    SharedPreferences attr;
    String[] items= {"없음","축구장","야구장","농구장","배구장","배드민턴장","족구장","테니스장","풋살경기장","다목적경기장","체육관","운동장",};//spinner
    String service;
    String attr1;
    String attr2;
    int attr3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        //설정 불러오기
        attr=getSharedPreferences("SETTINGS",Activity.MODE_PRIVATE);
        attrEd = attr.edit();
        attr1=attr.getString("payMent","적용안함");
        attr2=attr.getString("reservation","적용안함");
        attr3=attr.getInt("athletic",0);
        service=items[attr3];
        //ui
        Spinner spinner=(Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(attr3);
        //아이템 선택 이벤트
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //선택시 호출
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("aaaaaaaaaaaa","spinner selected");
                Log.d("aaaaaaaaaaaa",items[position]);
                attr3=position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("aaaaaaaaaaaa","spinner unselected");
            }
        });

        final Button attrBtn1=(Button) findViewById(R.id.attr1);
        attrBtn1.setText(attr1);
        attrBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attr1.equals("적용안함"))
                {
                    attr1="무료";
                }else if(attr1.equals("무료"))
                {
                    attr1="유료";
                }else if (attr1.equals("유료"))
                {
                    attr1="적용안함";
                }
                attrBtn1.setText(attr1);
            }
        });
        final Button attrBtn2=(Button) findViewById(R.id.attr2);
        attrBtn2.setText(attr2);
        attrBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attr2.equals("적용안함"))
                {
                    attr2="접수가능";
                }else if(attr2.equals("접수가능"))
                {
                    attr2="접수불가";
                }else if (attr2.equals("접수불가"))
                {
                    attr2="적용안함";
                }
                attrBtn2.setText(attr2);
            }
        });
        final Button saveBtn=(Button) findViewById(R.id.save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attrEd.putString("payMent",attr1);
                attrEd.putString("reservation",attr2);
                attrEd.putInt("athletic",attr3);
                attrEd.commit();
                Intent intent=new Intent(DetailActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
