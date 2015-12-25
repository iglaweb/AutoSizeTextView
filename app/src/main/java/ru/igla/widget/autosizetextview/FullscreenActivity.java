package ru.igla.widget.autosizetextview;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import ru.igla.widget.AutoSizeTextView;

public class FullscreenActivity extends Activity {

	private RelativeLayout activityRoot;

    public static final String FULLSCREEN_TEXT = "fullscreen_text";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        initiateActivity();
		setContentView(activityRoot);
	}

    void initiateActivity(){
        LayoutInflater inflater = (LayoutInflater)getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        activityRoot = (RelativeLayout)inflater.inflate(R.layout.fullscreen, null);

        Bundle extras = getIntent().getExtras();
        if(extras == null){
            return;
        }

        AutoSizeTextView textView = (AutoSizeTextView)activityRoot.findViewById(R.id.tvFullscreen);
        //FontUtils.setRobotoFont(textView);

        String text = extras.getString(FULLSCREEN_TEXT);
        if(TextUtils.isEmpty(text)){
            return;
        }

        text = text.trim();
        String adjusted = text.replaceAll("(?m)^[ \t]*\r?\n", "");
        //general hack http://code.google.com/p/android/issues/detail?id=13895
        //we should not use single line mode ever due to issue on Samsung Galaxy S5 4.4.2
        textView.setText(adjusted);


        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
