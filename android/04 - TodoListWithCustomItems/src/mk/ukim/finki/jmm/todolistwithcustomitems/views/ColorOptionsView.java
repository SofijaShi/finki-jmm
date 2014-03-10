package mk.ukim.finki.jmm.todolistwithcustomitems.views;

import mk.ukim.finki.jmm.todolistwithcustomitems.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ColorOptionsView extends LinearLayout {
	private View mValue;
	int valueColor;

	public ColorOptionsView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ColorOptionsView, 0, 0);
		String titleText = a.getString(R.styleable.ColorOptionsView_titleText);
		valueColor = a.getColor(R.styleable.ColorOptionsView_valueColor,
				R.color.blue);
		a.recycle();

		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_color_options, this, true);

		TextView title = (TextView) getChildAt(0);
		title.setText(titleText);

		mValue = getChildAt(1);
		mValue.setBackgroundColor(valueColor);

	}

	public ColorOptionsView(Context context) {
		this(context, null);
	}

	public void setValueColor(int color) {
		mValue.setBackgroundColor(color);
	}

	public int getValueColor() {
		return valueColor;
	}

}
