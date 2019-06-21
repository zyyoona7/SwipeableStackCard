package com.zyyoona7.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.zyyoona7.swipeablestack.OnItemSwipedListener;
import com.zyyoona7.swipeablestack.StackItemTouchHelper;
import com.zyyoona7.swipeablestack.StackLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private boolean mIsLoadingMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RecyclerView recyclerView = findViewById(R.id.rv_main);
        recyclerView.setLayoutManager(new StackLayoutManager());
        recyclerView.setHasFixedSize(true);
        final MainAdapter adapter = MainAdapter.newInstance(15);
        final StackItemTouchHelper<String,MainAdapter> itemTouchHelper = new StackItemTouchHelper<>(adapter,
                new OnItemSwipedListener<String>() {
                    @Override
                    public void onSwipeStart(@Nullable RecyclerView.ViewHolder holder, int position) {
                        Log.d(TAG, "onSwipeStart: ");
                    }

                    @Override
                    public void onSwipeRelease(@Nullable RecyclerView.ViewHolder holder, int position) {
                        Log.d(TAG, "onSwipeRelease: ");
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder holder, int direction,
                                         @Nullable String item, int position) {
                        Log.d(TAG, "onSwiped: direction:"+direction);
                        if (adapter.getItemCount()>0) {
                            Log.d(TAG, "onSwiped: first position data:"+adapter.getItem(0));
                        }
                        if (adapter.getItemCount() < 4 && !mIsLoadingMore) {
                            mIsLoadingMore = true;
                            adapter.addData(getDataList());
                            mIsLoadingMore = false;
                        }
                    }

                    @Override
                    public void onSwiping(@NonNull RecyclerView.ViewHolder holder, int direction,
                                          float thresholdRatio, float threshold, float ratio, int total) {
//                        Log.d(TAG, "onSwiping: thresholdRatio="+thresholdRatio+",ratio="+ratio);
                    }

                    @Override
                    public void onRecover() {
                        Log.d(TAG, "onRecover: ");
                    }
                });
        itemTouchHelper.bindToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);

        Button recoverBtn = findViewById(R.id.btn_recover);
        recoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemTouchHelper.recover();
            }
        });
    }

    private List<String> getDataList() {
        List<String> list = new ArrayList<>(15);
        for (int i = 0; i < 15; i++) {
            list.add("item stack " + i);
        }
        return list;
    }
}
