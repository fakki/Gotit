package com.mycompany.gotit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

import java.util.ArrayList;

import static com.mycompany.gotit.MainActivity.COLUMNS_KEY;
import static com.mycompany.gotit.MainActivity.CURRENT_ID_KEY;

public class ChangeColumnActivity extends AppCompatActivity {

    private String[] mBookColumnsList = new String[]{};

    private static final String TAG = "ChangeColumnActivity";

    private ArrayList<String> mResult = new ArrayList<>();
    //private String[] mMovieColumnsList;
    //private String[] mMusicColumnsList;

    private int columns_num = 3;

    private void initColumnsList(){
        mBookColumnsList = new String[]{"小说", "外国文学","中国文学","日本文学", "散文", "诗歌", "童话", "当代文学",
                "诗词", "漫画", "推理", "绘本", "科幻", "奇幻",
                "历史", "心理学", "哲学", "社会学", "政治", "经济学", "管理", "金融", "科普", "互联网", "编程", "科学"};
    }

    @Override
    protected void onCreate(Bundle onSaveInstanceState){
        super.onCreate(onSaveInstanceState);
        setContentView(R.layout.activity_change_columns);

        int current_id = getIntent().getIntExtra(CURRENT_ID_KEY, 0);
        if(current_id == 1 || current_id == 2){
            Toast.makeText(getApplicationContext(), R.string.not_finished, Toast.LENGTH_SHORT).show();
            finish();
        }

        Toolbar toolbar = findViewById(R.id.change_columns_ToolBar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.label_select);

        initColumnsList();

        GridLayout label_GridLayout = findViewById(R.id.label_GridLayout);
        for(int i = 0; i < mBookColumnsList.length; i++){
            CheckBox checkBox = new CheckBox(getApplicationContext());
            checkBox.setBackgroundResource(R.drawable.check_box_select_selector);
            checkBox.setText(mBookColumnsList[i]);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        mResult.add((String) buttonView.getText());
                    }
                    else {
                        mResult.remove(buttonView.getText());
                    }
                }
            });

            GridLayout.Spec rowSpec = GridLayout.spec(i/columns_num, 1.0f);
            GridLayout.Spec columnSpec = GridLayout.spec(i%columns_num, 1.0f);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
            //checkBox.setButtonDrawable(R.drawable.check_box_txt_select_selector);
            switch (i % columns_num){
                case 0:
                    params.setGravity(Gravity.LEFT);
                    break;
                case 1:
                    params.setGravity(Gravity.CENTER);
                    break;
                case 2:
                    params.setGravity(Gravity.RIGHT);
                    break;
                default:
                    params.setGravity(Gravity.CENTER);
                    break;
            }
            params.topMargin = 50;
            params.height = 100;
            params.width = 300;
            label_GridLayout.addView(checkBox, params);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_change_column_activity, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.change_columns_confirmButton:
                Intent data = new Intent();
                Bundle args = new Bundle();
                args.putStringArrayList(COLUMNS_KEY, mResult);
                data.putExtras(args);
                setResult(Activity.RESULT_OK, data);
                finish();
                return true;
            default:
                return false;
        }
    }


}
