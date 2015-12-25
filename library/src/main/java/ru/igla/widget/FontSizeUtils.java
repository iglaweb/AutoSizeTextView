package ru.igla.widget;

import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;

/**
 * Created by Lashkov Igor on 22/06/15.
 * Copyright (c) 2015. All rights reserved.
 */
public class FontSizeUtils {

    private static final float MAX_TEXT_SIZE = 100.0f;
    private static final float MIN_TEXT_SIZE = 8.0f;


    private SizeTester sizeTester;

    public SizeTester getSizeTester() {
        return sizeTester;
    }

    public void setSizeTester(SizeTester sizeTester) {
        this.sizeTester = sizeTester;
    }


    public static class SizeTester {
        private float minTextSize;
        private float maxTextSize;

        private float padding = 0f;

        public SizeTester(float minTextSize, float maxTextSize){
            this.minTextSize = minTextSize;
            this.maxTextSize = maxTextSize;
        }

        public float getMinTextSize() {
            return minTextSize;
        }

        public void setMinTextSize(float minTextSize) {
            this.minTextSize = minTextSize;
        }

        public float getMaxTextSize() {
            return maxTextSize;
        }

        public void setMaxTextSize(float maxTextSize) {
            this.maxTextSize = maxTextSize;
        }

        public float getPadding() {
            return padding;
        }

        public void setPadding(float padding) {
            this.padding = padding;
        }
    }




    private static boolean testFontSize(String text, RectF availableSpace, TextPaint tp, boolean singleLine){
        return /*singleLine ?
                testFontSizeSingleline(text, availableSpace, tp) :*/
                testMultilineSize(text, availableSpace, tp);
    }

    private static boolean testMultilineSize(String text, RectF availableSpace, TextPaint tp){

        StaticLayout sl = new StaticLayout(
                text,
                tp,
                (int)availableSpace.width(),
                Layout.Alignment.ALIGN_CENTER,
                1.0f,
                0.0f,
                true
        );

		float lineHeight = tp.getFontSpacing();

        final RectF textRect = new RectF();

        int maxHeight = sl.getHeight();
        textRect.bottom = maxHeight;
        int maxWidth = -1;
        for(int i = 0; i < sl.getLineCount(); i++)
            if(maxWidth < sl.getLineRight(i) - sl.getLineLeft(i))
                maxWidth = (int)sl.getLineRight(i) - (int)sl.getLineLeft(i);

		maxWidth += 5; //hack for Nexus 7 https://st.yandex-team.ru/MT-2386

        textRect.right = maxWidth;

        textRect.offsetTo(0,0);


        //bug; we need to know the longest word, as it should not have any breaks
        String word = StringUtils.getLongestWord(text) + text.charAt(0); //huck
        float measureWidth = tp.measureText(word);





        Rect boundsLWord = new Rect();
        tp.getTextBounds(word, 0, word.length(), boundsLWord);






        final float avHeight = availableSpace.height();
        final float avWidth = availableSpace.width();


        //check if longest word fits width
        CharSequence t = TextUtils.ellipsize(word, tp, avWidth, TextUtils.TruncateAt.END);
        if(!TextUtils.equals(word, t)){ //if we have a truncated text
            return false; // too big
        }


        if(maxHeight >= avHeight ||
                maxWidth >= avWidth ||
                measureWidth >= avWidth ||
                boundsLWord.height() >= avHeight ||
                boundsLWord.width() >= avWidth){
            return false; //too big
        } else {
            //Log.e("TOO SMALL rect: " + textRect.toShortString() + " available: " + availableSpace.toShortString());
            return true; //too small
        }
    }


    private static boolean testFontSizeSingleline(String text, RectF availableSpace, TextPaint tp){

        Rect bounds = new Rect();

        float lineHeight = tp.getFontSpacing();
        float measureWidth = tp.measureText(text);

        tp.getTextBounds(text, 0, text.length(), bounds);

        if(measureWidth >= availableSpace.width() ||
                lineHeight >= availableSpace.height() ||
                bounds.height() >= availableSpace.height()) {
            return false; // too big
        }
        return true; // too small
    }



    public int getFontSize(String text, boolean isSingleLine, RectF availableSpace, TextPaint tp){

        float max = sizeTester == null ? MAX_TEXT_SIZE : sizeTester.getMaxTextSize();
        float min = sizeTester == null ? MIN_TEXT_SIZE : sizeTester.getMinTextSize();

        int start = (int)min;

        int lastBest = start;

        int hi = (int)max;
        int lo = start;

        int mid;
        while(hi - lo > 1) {

            mid = lo + hi >>> 1;

            tp.setTextSize(mid);
            boolean flag = testFontSize(text, availableSpace, tp, isSingleLine);

            if(flag) {
                lastBest = lo;
                lo = mid + 1;
            } else {
                hi = mid - 1;
                lastBest = hi;
            }
        }
        return lastBest;
    }
}
