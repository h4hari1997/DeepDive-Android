/*
 * Copyright (c) 2018 Nuvolect LLC.
 * This software is offered for free under conditions of the GPLv3 open source software license.
 * Contact Nuvolect LLC for a less restrictive commercial license if you would like to use the software
 * without the GPLv3 restrictions.
 */

package com.nuvolect.deepdive.util;


public class Safe {

    private static final boolean DEBUG = false;

    //FUTURE reduce four single quotes to two single quotes
    /**
     * Make a safe string by making pairs of single quotes, removing non-printing characters
     * and replacing the null string with an empty string.
     * @param unsafeString
     * @return safeString
     */
    public static String safeString(String unsafeString){

        if(unsafeString == null){
            return "";
        }

        char[] oldChars = new char[unsafeString.length()];
        int extraSpace = Math.max( unsafeString.length() / 10, 20);
        unsafeString.getChars(0, unsafeString.length(), oldChars, 0);
        int bufferSize = unsafeString.length()+extraSpace;// to handle extra quote characters
        char[] newChars = new char[ bufferSize];
        int newLen = 0;
        for (int j = 0; j < unsafeString.length(); j++) {
            char ch = oldChars[j];
            if( ch == 39){
                // Found a single quote, put in two
                newChars[newLen] = ch;
                newLen++;
                newChars[newLen] = ch;
                newLen++;
            }else
                if( newLen == bufferSize -1){
                    LogUtil.log("Input note string too long");
                    break;
                }
                if (ch == 9 || ch == 10 || ch >= ' ') { // accept newline, tab and all printing chars
                    newChars[newLen] = ch;
                    newLen++;
                }
        }
        String safeString = new String(newChars, 0, newLen);

        if(DEBUG){
            if( unsafeString.contains("'")){
                LogUtil.log("found quote: "+unsafeString );
                LogUtil.log("quoted: "+safeString);
            }
        }
        return safeString.trim();
    }

    /**
     * Remove all whitespace and non-visible characters.
     * @param hasWhitespace
     * @return
     */
    public static String removeWhitespace(String hasWhitespace){

        return hasWhitespace.replaceAll("\\s","");
    }
}
