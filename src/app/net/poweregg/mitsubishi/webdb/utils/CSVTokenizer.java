/*
 * Copyright(C) 2012 D-CIRCLE, INC. All rights reserved.
 * 
 * (1)このソフトウェアは、ディサークル株式会社に帰属する機密情報 であり開示を固く禁じます。
 * (2)この情報を使用するには、ディサークル株式会社とのライセンス 契約が必要となります。
 * 
 * This software is the confidential and proprietary information of
 * D-CIRCLE, INC. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license
 * agreement you entered into with D-CIRCLE.
 */

package net.poweregg.mitsubishi.webdb.utils;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * @author : lochg
 * @PG_ID :
 * @createDate : 2012/07/17
 */
public class CSVTokenizer implements Enumeration<Object> {
    
    private String source;
    private int currentPos;
    private int maxPos;
    private char delim;

    public CSVTokenizer(String line) {
        source = null;
        currentPos = 0;
        maxPos = 0;
        delim = ',';
        source = line;
        currentPos = 0;
        maxPos = line.length();
    }

    public CSVTokenizer(String line, char delim) {
        this(line);
        this.delim = delim;
    }

    public boolean hasMoreElements() {
        return hasMoreTokens();
    }

    public Object nextElement() {
        return nextToken();
    }

    public boolean hasMoreTokens() {
        return nextComma(currentPos) <= maxPos;
    }

    public int countTokes() {
        int i = 0;
        int ret;
        for (ret = 1; (i = nextComma(i)) < maxPos; ret++)
            i++;

        return ret;
    }

    public String nextToken() {
        if (currentPos > maxPos)
            throw new NoSuchElementException((new StringBuilder())
                    .append(toString()).append("#nextToken").toString());
        int st = currentPos;
        currentPos = nextComma(currentPos);
        StringBuffer sb = new StringBuffer();
        do {
            if (st >= currentPos)
                break;
            char ch = source.charAt(st++);
            if (ch == '"') {
                if (st + 1 != currentPos && st < currentPos
                        && source.charAt(st) == '"') {
                    sb.append(ch);
                    st++;
                }
            } else {
                sb.append(ch);
            }
        } while (true);
        currentPos++;
        return new String(sb);
    }

    public String toString() {
        return source;
    }

    private int nextComma(int index) {
        boolean inquote = false;
        for (; index < maxPos; index++) {
            char ch = source.charAt(index);
            if (!inquote && ch == delim)
                break;
            if ('"' == ch)
                inquote = !inquote;
        }

        return index;
    }

 
}
