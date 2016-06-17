package net.iquesoft.project.seed.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import net.iquesoft.project.seed.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirebaseMessageActivity extends AppCompatActivity {

    @BindView(R.id.tvMessage)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_message);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String s = intent.getStringExtra("message");
        textView.setText(s);
    }
}

