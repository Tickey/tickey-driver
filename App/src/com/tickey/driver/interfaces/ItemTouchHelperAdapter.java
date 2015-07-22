package com.tickey.driver.interfaces;

import android.support.v7.widget.RecyclerView;

/**
 * Created by SineMetu on 7/20/15.
 */
public interface ItemTouchHelperAdapter {

//    void onItemDismiss(int position);
    void onItemDismiss(RecyclerView.ViewHolder holder);
}
