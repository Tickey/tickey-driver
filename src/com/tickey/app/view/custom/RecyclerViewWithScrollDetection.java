package com.tickey.app.view.custom;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class RecyclerViewWithScrollDetection extends RecyclerView {

	private OnScrollListener onScrollListener;
	private OnDetectScrollListener onDetectScrollListener;

	public interface OnDetectScrollListener {

		void onUpScrolling();

		void onDownScrolling();
		
		//void onScrolled(RecyclerView recyclerView, int dx, int dy);
	}

	public RecyclerViewWithScrollDetection(Context context) {
		super(context);
		onCreate(context, null, null);
	}

	public RecyclerViewWithScrollDetection(Context context, AttributeSet attrs) {
		super(context, attrs);
		onCreate(context, attrs, null);
	}

	public RecyclerViewWithScrollDetection(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		onCreate(context, attrs, defStyle);
	}

	private void onCreate(Context context, AttributeSet attrs, Integer defStyle) {
		setListeners();
	}

	private void setListeners() {
		super.setOnScrollListener(new OnScrollListener() {

			private int oldTop;
			private int oldFirstVisibleItem;

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView,
					int newState) {
				if (onScrollListener != null) {
					onScrollListener.onScrollStateChanged(recyclerView,
							newState);
				}
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView
						.getLayoutManager());
				int firstVisiblePosition = layoutManager
						.findFirstVisibleItemPosition();

				if (onScrollListener != null) {
					onScrollListener.onScrolled(recyclerView, dx, dy);
				}

				if (onDetectScrollListener != null) {
					onDetectedListScroll(recyclerView, firstVisiblePosition);
				}
			}

			private void onDetectedListScroll(RecyclerView absListView,
					int firstVisibleItem) {
				View view = absListView.getChildAt(0);
				int top = (view == null) ? 0 : view.getTop();

				if (firstVisibleItem == oldFirstVisibleItem) {
					if (top > oldTop) {
						onDetectScrollListener.onUpScrolling();
					} else if (top < oldTop) {
						onDetectScrollListener.onDownScrolling();
					}
				} else {
					if (firstVisibleItem < oldFirstVisibleItem) {
						onDetectScrollListener.onUpScrolling();
					} else {
						onDetectScrollListener.onDownScrolling();
					}
				}

				oldTop = top;
				oldFirstVisibleItem = firstVisibleItem;
			}
		});
	}

	@Override
	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}

	public void setOnDetectScrollListener(
			OnDetectScrollListener onDetectScrollListener) {
		this.onDetectScrollListener = onDetectScrollListener;
	}

}
