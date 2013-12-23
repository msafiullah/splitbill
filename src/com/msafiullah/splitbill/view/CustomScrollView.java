package com.msafiullah.splitbill.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.msafiullah.splitbill.R;
import com.msafiullah.splitbill.listeners.OnProgressViewReachedListener;

public class CustomScrollView extends ScrollView {

	private final String tag = "CustomScrollView";

	private OnProgressViewReachedListener _onProgressViewReachedListener = null;

	public CustomScrollView(Context context) {
		super(context);
	}

	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// Grab the last child placed in the ScrollView, we need it to
		// determinate the bottom position.

		LinearLayout layoutBill = (LinearLayout) getChildAt(0);
		View view = layoutBill.getChildAt(layoutBill.getChildCount() - 1);

		if (view != null && view.getId() == R.id.circularProgressLayout) {
			// Calculate the scroll diff
			int diff = (view.getTop() - (getHeight() + getScrollY()));
			// if diff is zero, then the bottom has been reached
			if (diff <= 0) {
				// notify that we have reached the bottom
				if (_onProgressViewReachedListener != null) {
					Log.v(tag, "onScrollChanged, onProgressViewReached()");
					_onProgressViewReachedListener.onProgressViewReached();
				}
			}
		}
		super.onScrollChanged(l, t, oldl, oldt);
	}
	
	public void setOnProgressViewReachedListener(OnProgressViewReachedListener onProgressViewReachedListener) {
		_onProgressViewReachedListener = onProgressViewReachedListener;
	}
}
