package com.zyyoona7.demo;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public static MainAdapter newInstance(int count) {
        List<String> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add("stack item " + i);
        }
        return new MainAdapter(list);
    }

    public MainAdapter(@Nullable List<String> data) {
        super(R.layout.item_stack, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_item_text, item);
    }
}
