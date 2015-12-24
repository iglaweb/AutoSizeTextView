package ru.igla.autosizetextview;

/**
 * Created by Lashkov Igor on 16/06/15.
 * Copyright (c) 2015. All rights reserved.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;


/**
 * a textView that is able to self-adjust its font size depending on the min and max size of the font, and its own size.<br/>
 * code is heavily based on this StackOverflow thread:
 * http://stackoverflow.com/questions/16017165/auto-fit-textview-for-android/21851239#21851239 <br/>
 * It should work fine with most Android versions, but might have some issues on Android 3.1 - 4.04, as setTextSize will only work for the first time. <br/>
 * More info here: https://code.google.com/p/android/issues/detail?id=22493 and here in case you wish to fix it: http://stackoverflow.com/a/21851239/878126
 */
public class AutoSizeTextView extends TextView {

	final String ZERO_WIDTH_SPACE = "\u200B";

	// Minimum size of the text in pixels
	private static final float DEFAULT_MIN_TEXT_SIZE = 8.0f; //sp


	private static final int NO_LINE_LIMIT = -1;
	private final RectF _availableSpaceRect = new RectF();

	private float mMaxTextSize;
	private float mMinTextSize;
	private int mMaxLines;
	private boolean mIsInitialized = false;

	private TextPaint mPaint;
	private FontSizeUtils mFontSizeUtils;

	public AutoSizeTextView(final Context context) {
		this(context,null,0);
	}

	public AutoSizeTextView(final Context context, final AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AutoSizeTextView(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		Init();
	}

	private void Init(){
		//due to problem of big font size, http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}

		// using the minimal recommended font size
		mMinTextSize = TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP,
				DEFAULT_MIN_TEXT_SIZE,
				getResources().getDisplayMetrics()
		);

		mMaxTextSize = getTextSize();

		if(mMaxLines == 0) {
			// no value was assigned during construction
			mMaxLines = NO_LINE_LIMIT;
		}

		mIsInitialized = true;


		mFontSizeUtils = new FontSizeUtils();
		mFontSizeUtils.setSizeTester(
				new FontSizeUtils.SizeTester(mMinTextSize, mMaxTextSize));

		mPaint = new TextPaint();
		mPaint.setTextAlign(Paint.Align.CENTER);
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLACK);
		mPaint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.NORMAL));
		mPaint.setSubpixelText(true);
	}

	@Override
	public void setTypeface(final Typeface tf) {

		if(mPaint == null) {
			mPaint = new TextPaint(getPaint());
		}

		mPaint.setTypeface(tf);
		adjustTextSize();
		super.setTypeface(tf);
	}

	@Override
	public void setTextSize(final float size) {
		mMaxTextSize = size;
		adjustTextSize();
	}

	@Override
	public void setMaxLines(final int maxlines) {
		super.setMaxLines(maxlines);
		mMaxLines = maxlines;
		reAdjust();
	}

	@Override
	public int getMaxLines() {
		return mMaxLines;
	}

	@Override
	public void setSingleLine() {
		super.setSingleLine();
		mMaxLines = 1;
		reAdjust();
	}

	@Override
	public void setSingleLine(final boolean singleLine) {
		super.setSingleLine(singleLine);
		mMaxLines = singleLine ? 1 : NO_LINE_LIMIT;
		reAdjust();
	}

	@Override
	public void setLines(final int lines) {
		super.setLines(lines);
		mMaxLines = lines;
		reAdjust();
	}

	@Override
	public void setTextSize(final int unit,final float size) {
		final Context c = getContext();

		Resources r = c == null ? Resources.getSystem() : c.getResources();

		mMaxTextSize = TypedValue.applyDimension(unit,size,r.getDisplayMetrics());
		if(mFontSizeUtils.getSizeTester() != null){
			mFontSizeUtils.getSizeTester().setMaxTextSize(mMaxTextSize);
		}

		adjustTextSize();
	}

	/**
	 * Set the lower text size limit and invalidate the view
	 *
	 * @param minTextSize
	 */
	public void setMinTextSize(final float minTextSize) {
		mMinTextSize = minTextSize;

		if(mFontSizeUtils.getSizeTester() != null){
			mFontSizeUtils.getSizeTester().setMinTextSize(minTextSize);
		}

		reAdjust();
	}

	private void reAdjust() {
		adjustTextSize();
	}

	private void adjustTextSize() {
		if(!mIsInitialized) {
			return;
		}

		final int heightLimit = getMeasuredHeight() - getCompoundPaddingBottom() - getCompoundPaddingTop();
		final int _widthLimit = getMeasuredWidth() - getCompoundPaddingLeft() - getCompoundPaddingRight();
		if(_widthLimit <= 0) {
			return;
		}

		_availableSpaceRect.right = _widthLimit;
		_availableSpaceRect.bottom = heightLimit;
		superSetTextSize();
	}

	private void superSetTextSize() {

		float size = efficientTextSizeSearch(_availableSpaceRect);

		super.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

		String newText = getText().toString().trim();
		if(size - 1 <= mMinTextSize){
			newText = StringUtils.ellipsizeText(newText, _availableSpaceRect.width(), mPaint);
		}

		//workaround http://code.google.com/p/android/issues/detail?id=17343#c9
		//http://stackoverflow.com/a/13377239/1461625
		String fixString = "";
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1
				&& Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
			fixString = ZERO_WIDTH_SPACE;
		}

		super.setText(newText + fixString);
	}

	private float efficientTextSizeSearch(final RectF availableSpace) {

		String text = getText().toString().trim();

		boolean isSingleLine = StringUtils.isSingleLine(text);

		float newSize = mFontSizeUtils.getFontSize(text, isSingleLine, availableSpace, mPaint);
		System.out.println(newSize);

		return newSize;
	}

	@Override
	protected void onSizeChanged(final int width,final int height,final int oldwidth,final int oldheight) {
		super.onSizeChanged(width, height, oldwidth, oldheight);
		if(width!=oldwidth||height!=oldheight)
			reAdjust();
	}

	/**
	 * Resize text after measuring
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

		/*int l = getCompoundPaddingLeft();
		int r = getCompoundPaddingRight();
		int t = getCompoundPaddingTop();
		int b = getCompoundPaddingBottom();

		int widthLimit = (right - left) - getCompoundPaddingLeft() - getCompoundPaddingRight();
		int heightLimit = (bottom - top) - getCompoundPaddingBottom() - getCompoundPaddingTop();*/

		super.onLayout(changed, left, top, right, bottom);
	}

	Paint.FontMetricsInt fontMetricsInt;

	@Override
	protected void onDraw(Canvas canvas) {
		if (fontMetricsInt == null){
			fontMetricsInt = new Paint.FontMetricsInt();
			getPaint().getFontMetricsInt(fontMetricsInt);
		}
		canvas.translate(0, fontMetricsInt.top - fontMetricsInt.ascent);
		super.onDraw(canvas);
	}
}