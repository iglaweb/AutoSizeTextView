package ru.igla.autosizetextview;

import android.text.TextPaint;
import android.text.TextUtils;

import java.util.StringTokenizer;

/**
 * Created by lashkov on 24/12/15.
 * Copyright (c) 2015. All rights reserved.
 */
public class StringUtils {

    public static boolean isSingleLine(String text){
        StringTokenizer st = new StringTokenizer(text);
        return st.countTokens() < 2;
    }

    /***
     * Get the longest word in a text by splitting it by spaces
     * @param text
     * @return
     */
    public static String getLongestWord(String text){
        StringTokenizer st = new StringTokenizer(text);
        String result = "";
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            if(s.length() > result.length()){
                result = s;
            }
        }
        return result;
    }

    /***
     * Ellipsize words if they do not fit a line
     * @param text text to be checked
     * @param width available width for the text
     * @param tp used textpaint to draw text
     * @return formatted text
     */
    public static String ellipsizeText(final String text, float width, final TextPaint tp){
        String str = text;

        StringTokenizer st = new StringTokenizer(text);
        while (st.hasMoreTokens()) {

            String s = st.nextToken();

            CharSequence t = TextUtils.ellipsize(s, tp, width, TextUtils.TruncateAt.END);
            if(!TextUtils.equals(t, s)){
                str = str.replace(s, t);
            }
        }
        return str;
    }
}
