package com.example.hometest;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecycleView;
    List<String> dataReturn = new ArrayList();
    int countCharacterStart = 0;
    int countCharacterEnd = 0;
    String splitStart = "";
    String splitEnd = "";
    ListAdapter adapter;
    LinearLayoutManager manager;
    final int duration = 5000;
    final int pixelsToMove = 300;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecycleView = findViewById(R.id.list_data);
        mRecycleView.setHasFixedSize(true);
        processData(ListAdapter.getValue());
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);

        adapter = new ListAdapter(dataReturn, this);
        mRecycleView.setLayoutManager(manager);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setAdapter(adapter);
        autoScroll();

    }

    private final Runnable SCROLLING_RUNNABLE = new Runnable() {
        @Override
        public void run() {
            mRecycleView.smoothScrollBy(pixelsToMove, 0);
            mHandler.postDelayed(this, duration);
        }
    };

    private void autoScroll(){
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = manager.findLastCompletelyVisibleItemPosition();
                if(lastItem == manager.getItemCount()-1){
                    mHandler.removeCallbacks(SCROLLING_RUNNABLE);
                    Handler postHandler = new Handler();
                    postHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mRecycleView.setAdapter(null);
                            mRecycleView.setAdapter(adapter);
                            mHandler.postDelayed(SCROLLING_RUNNABLE, 2000);
                        }
                    }, 2000);
                }
            }
        });
        mHandler.postDelayed(SCROLLING_RUNNABLE, 2000);
    }

    private void loadData() {
        for (String value : dataReturn) {
            System.out.println(value);
        }
    }

    private void processData(List<String> data) {
        for (String value : data) {
            checkValue(value.trim());
        }
    }

    /**
     * count space
     * @param value
     * @return
     */
    private int countSpace(String value) {
        int count = 0;
        char kyTu;
        for (int i = 0; i < value.length(); i++) {
            kyTu = value.charAt(i);
            if (Character.isWhitespace(kyTu)) {
                count++;
            }
        }
        return count;
    }

    /**
     * check value have contains space
     * @param value
     */
    private void checkValue(String value) {
        if (value.contains(" ")) {
            if (countSpace(value) == 1) {
                dataReturn.add(replaceSpace(value));
            } else {
                dataReturn.add(processValue(value));
            }
        } else {
            dataReturn.add(value);
        }
    }

    /**
     * replace \n to space
     * @param value
     * @return
     */
    private String replaceSpace(String value) {
        String val = "";
        char[] val1 = value.toCharArray();
        for (int i = 0; i < value.length(); i++) {
            if (Character.toString(val1[i]).equals(" ")) {
                val = new StringBuffer(value).replace(i, i+1, "\n").toString();
            }
        }
        return val;
    }

    /**
     * replace \n to space
     * @param value
     * @return
     */
    private String processValue(String value) {
        int countEnd = 0;
        int countStart = 0;
        String val = "";
        char[] val1 = value.toCharArray();
        int length = value.length() / 2;

        if (Character.toString(val1[length]).equals(" ")) {
            val = new StringBuffer(value).replace(length, length, "\n").toString();
        } else {
            countEnd = countCharacter(subStringEnd(val1, length, value));
            countStart = countCharacter(subStringStart(val1, length - 1, value));
            System.out.println("value: " + value
                    + "\n length" + value.length()
                    + "\n countEnd: " + countEnd + "\n countStart: " + countStart
                    + "\n countCharacterEnd: " + countCharacterEnd + "\n countCharacterStart: " + countCharacterStart
                    + "\n ------------------------------------------------------------");
            if (countEnd == countStart) {
                val = new StringBuffer(value).replace(countCharacterEnd, countCharacterEnd, "\n").toString();
            } else if (countEnd > countStart) {
                val = new StringBuffer(value).replace(countCharacterEnd, countCharacterEnd, "\n").toString();
            } else {
                val = new StringBuffer(value).replace(countCharacterStart, countCharacterStart + 1, "\n").toString();
            }
        }
        return val;
    }

    /**
     * count length String
     * @param val
     * @return
     */
    private int countCharacter(String val) {
        return val.length();
    }

    /**
     * sub string from middle to end
     * @param value
     * @param length
     * @param val
     * @return
     */
    private String subStringEnd(char[] value, int length, String val) {
        for (int i = length; i < val.length(); i++) {
            if (Character.toString(value[i]).equals(" ")) {
                splitEnd = val.substring(i + 1, value.length);
                countCharacterEnd = ++i;
                break;
            }
        }
        return splitEnd;
    }

    /**
     * sub String from start to middle
     * @param value
     * @param length
     * @param val
     * @return
     */
    private String subStringStart(char[] value, int length, String val) {
        for (int i = length; i > 0; i--) {
            if (Character.toString(value[i]).equals(" ")) {
                splitStart = val.substring(0, i);
                countCharacterStart = i;
                break;
            }
        }
        return splitStart;
    }


}
