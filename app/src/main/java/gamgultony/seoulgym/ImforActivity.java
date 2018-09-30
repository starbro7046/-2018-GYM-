package gamgultony.seoulgym;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ImforActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imfor);
        TextView textView1=(TextView) findViewById(R.id.title);
        TextView textView2=(TextView) findViewById(R.id.pay);
        TextView textView3=(TextView) findViewById(R.id.tel);
        TextView textView4=(TextView) findViewById(R.id.loc);
        TextView textView5=(TextView) findViewById(R.id.sub);
        TextView textView6=(TextView) findViewById(R.id.can);
        TextView textView10=(TextView) findViewById(R.id.url);
        TextView textView7=(TextView) findViewById(R.id.st);
        TextView textView8=(TextView) findViewById(R.id.et);

        textView1.setText(getIntent().getStringExtra("title"));
        textView2.setText(getIntent().getStringExtra("pay"));
        textView3.setText(getIntent().getStringExtra("tel"));
        textView4.setText(getIntent().getStringExtra("loc"));
        textView6.setText(getIntent().getStringExtra("can"));
        textView5.setText(getIntent().getStringExtra("ath"));
        textView7.setText(getIntent().getStringExtra("st"));
        textView8.setText(getIntent().getStringExtra("et"));
        textView10.setText(getIntent().getStringExtra("url"));

        ImageView ima=(ImageView) findViewById(R.id.a);
        ima.setColorFilter(ContextCompat.getColor(ImforActivity.this, R.color.deepBlue));
        ImageView imb=(ImageView) findViewById(R.id.b);
        imb.setColorFilter(ContextCompat.getColor(ImforActivity.this, R.color.deepBlue));
        ImageView imc=(ImageView) findViewById(R.id.c);
        imc.setColorFilter(ContextCompat.getColor(ImforActivity.this, R.color.deepBlue));
        ImageView ime=(ImageView) findViewById(R.id.e);
        ime.setColorFilter(ContextCompat.getColor(ImforActivity.this, R.color.deepBlue));

        ImageView imd=(ImageView) findViewById(R.id.d);
        if(getIntent().getStringExtra("can").equals("접수중")||(getIntent().getStringExtra("can").equals("안내중")))
        {
            imd.setColorFilter(ContextCompat.getColor(ImforActivity.this, R.color.green));
        }else
        {
            imd.setColorFilter(ContextCompat.getColor(ImforActivity.this, R.color.red));
        }
    }
}
