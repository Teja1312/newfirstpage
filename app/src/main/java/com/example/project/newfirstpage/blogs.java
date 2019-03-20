package com.example.project.newfirstpage;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class blogs extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogs);
        findViewById(R.id.blog1).setOnClickListener(this);
        findViewById(R.id.blog2).setOnClickListener(this);
        findViewById(R.id.blog3).setOnClickListener(this);
        findViewById(R.id.blog4).setOnClickListener(this);
        findViewById(R.id.blog5).setOnClickListener(this);
        findViewById(R.id.blog6).setOnClickListener(this);
        findViewById(R.id.blog7).setOnClickListener(this);
        findViewById(R.id.blog8).setOnClickListener(this);
        findViewById(R.id.blog9).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.blog1 : startActivity(new Intent(Intent.ACTION_VIEW,  Uri.parse("https://www.seed-blog.com/10-tips-empowering-women-self-defence/"))); break ;
            case R.id.blog2 : startActivity(new Intent(Intent.ACTION_VIEW,  Uri.parse("https://www.centennialcollege.ca/about-centennial/safety-and-security/campus-safety-watch/womens-safety-awareness/"))); break ;
            case R.id.blog3 : startActivity(new Intent(Intent.ACTION_VIEW,  Uri.parse("https://currentaffairs.gktoday.in/mha-creates-women-safety-division-address-issues-women-security-05201855641.html"))); break ;
            case R.id.blog4 : startActivity(new Intent(Intent.ACTION_VIEW,  Uri.parse("https://www.unilever.com/sustainable-living/enhancing-livelihoods/opportunities-for-women/promoting-safety-for-women/"))); break ;
            case R.id.blog5 : startActivity(new Intent(Intent.ACTION_VIEW,  Uri.parse("http://www.womensselfdefense-seps.com/womensselfdefense-blog.php"))); break ;
            case R.id.blog6 : startActivity(new Intent(Intent.ACTION_VIEW,  Uri.parse("https://www.lessthanlethalselfdefenseblog.com/blog/womens-self-defense-abcs"))); break ;
            case R.id.blog7 : startActivity(new Intent(Intent.ACTION_VIEW,  Uri.parse("https://yourstory.com/2013/01/a-kick-to-safety-truly-justice-for-women-with-self-defense-classes"))); break ;
            case R.id.blog8 : startActivity(new Intent(Intent.ACTION_VIEW,  Uri.parse("https://www.thehindubusinessline.com/news/self-defence-workshops-for-women-and-girls/article23092661.ece"))); break ;
            case R.id.blog9 : startActivity(new Intent(Intent.ACTION_VIEW,  Uri.parse("http://www.uwindsor.ca/campuspolice/306/rad-womens-self-defence-course"))); break ;

        }
    }
}
