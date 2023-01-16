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

import java.io.Serializable;
import java.math.BigDecimal;

import net.poweregg.faces.util.StringUtils;

/**
 *
 * @author : phapnv
 * @PG_ID :
 * @createDate : Jul 23, 2012
 */
public class ConvertUtils implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public static final String EMPTY_STRING = "";
    public static final String BLANK_STRING = " ";
    public static final String WINDOWS_31J = "Windows-31J";
    public static final String ZEZO = "0";
    public static final String L = "L";
    public static final String R = "R";
    public static final String NULL_STRING = "null";
    public static final String BASE_NAME_PWA2 = "pwa2";
    public static String subString(String source, int srcBegin, int srcEnd) {
        if (null == source) {
            return null;
        }
        if (srcBegin > source.length() - 1) {
            srcBegin = source.length();
        }
        if (srcBegin < 0) {
            srcBegin = 0;
        }
        if (srcEnd > source.length() - 1) {
            srcEnd = source.length();
        }
        if (srcEnd < 0) {
            srcEnd = 0;
        }
        return source.substring(srcBegin, srcEnd);
    }

    /**
     * Return a new byte array containing a sub-portion of the source
     * array
     *
     * @param srcBegin
     *        The beginning index (inclusive)
     * @return The new, populated byte array
     *
     *         author phapnv
     */
    public static byte[] subBytes(byte[] source, int srcBegin) {
        return subBytes(source, srcBegin, source.length);
    }

    /**
     * Return a new byte array containing a sub-portion of the source
     * array
     *
     * @param srcBegin
     *        The beginning index (inclusive)
     * @param srcEnd
     *        The ending index (exclusive)
     * @return The new, populated byte array author phapnv
     */
    public static byte[] subBytes(byte[] source, int srcBegin, int srcEnd) {
        byte destination[];

        destination = new byte[srcEnd - srcBegin];
        getBytes(source, srcBegin, srcEnd, destination, 0);

        return destination;
    }

    /**
     * author phapnv
     */
    public static String subStringBytes(String source, String charsetName,
            int srcBegin, int srcEnd) {
        if (null == source) {
            return null;
        }
        String subString = null;
        try {
            byte subByte[] = null;
            if (null == charsetName) {
                subByte = source.getBytes();
            } else {
                subByte = source.getBytes(charsetName);
            }
            if (null != subByte) {
                if (srcBegin > subByte.length) {
                    srcBegin = subByte.length;
                }
                if (srcBegin < 0) {
                    srcBegin = 0;
                }
                if (srcEnd > subByte.length) {
                    srcEnd = subByte.length;
                }
                if (srcEnd < 0) {
                    srcEnd = 0;
                }
                subByte = subBytes(subByte, srcBegin, srcEnd);
                if (null == charsetName) {
                    subString = new String(subByte);
                } else {
                    subString = new String(subByte, charsetName);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return subString;
    }

    /**
     * Copies bytes from the source byte array to the destination array
     *
     * @param source
     *        The source array
     * @param srcBegin
     *        Index of the first source byte to copy
     * @param srcEnd
     *        Index after the last source byte to copy
     * @param destination
     *        The destination array
     * @param dstBegin
     *        The starting offset in the destination array author phapnv
     */
    public static void getBytes(byte[] source, int srcBegin, int srcEnd,
            byte[] destination, int dstBegin) {
        System.arraycopy(source, srcBegin, destination, dstBegin, srcEnd
                - srcBegin);
    }

    public static boolean isLongNumber(String number) {
        try {
            Long.valueOf(number);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String lpad(String original, int length, String addStr) {
        return fillString(original, L, length, addStr);
    }

    public static String rpad(String original, int length, String addStr) {
        return fillString(original, R, length, addStr);
    }

    public static String fillString(String original, String position,
            int length, String addStr) {
        if (original == null) {
            original = EMPTY_STRING;
        }
        original = original.replaceAll("\r", "").replaceAll("\n", ""); //#26734 改行コードを除去する
        original = original.replaceAll("\t", ""); //#26734 タブ文字を除去する
        StringBuffer buffer = new StringBuffer(original);
        while (length > buffer.length()) {
            if (position.equalsIgnoreCase(L)) {
                int sum = buffer.length() + addStr.length();
                if (sum > length) {
                    addStr = addStr.substring(0, addStr.length()
                            - (sum - length));
                    buffer.insert(0, addStr);
                } else {
                    buffer.insert(0, addStr);
                }
            } else {
                buffer.append(addStr);
            }
        }
        if (buffer.length() == length) {
            return buffer.toString();
        }
        return buffer.toString().substring(0, length);
    }

    public static String zeroFormat(String value, int length) {
        StringBuffer buffer = null;
        if (null != value) {
            int zezoLength = length - value.length();
            buffer = new StringBuffer();
            if (zezoLength >= 0) {
                for (int i = 0; i < zezoLength; i++) {
                    buffer.append(ZEZO);
                }
                buffer.append(value);
                return buffer.toString();
            } else {
                return buffer.substring(value.length() - length);
            }
        }
        return null;
    }

    public static String getEncoding() {
        LiteralProperties properties = LiteralProperties.getInstance();
        String encode = properties.getLiteral(BASE_NAME_PWA2,
                "PWA2.SYSTEM.FILE.ENCODING");

        if (encode == null) {
            encode = WINDOWS_31J;
        }
        return encode;
    }

    /**
     *
     * @param value
     * @return
     */
    public static boolean isNullOrEmpty(String value) {
        if (null == value) {
            return true;
        }
        if (value.length() == 0) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param value
     * @return
     */
    public static boolean isNullOrEmptyOrBlank(String value) {
        if (null == value) {
            return true;
        }
        if (value.length() == 0) {
            return true;
        }
        if (value.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Object aObject) {
        if (aObject == null)
            return true;
        String str = aObject.toString();
        return str.length() == 0;
    }

    public static Boolean toBoolean(Object aObject) {
        if (aObject == null)
            return null;
        String target = toString(aObject);
        if (target.equals("1"))
            return new Boolean(true);
        if (target.equals("0"))
            return new Boolean(false);
        else
            return Boolean.valueOf(target);
    }
    
    public static boolean toBoolean(String aObject) {
        String target = StringUtils.toEmpty(aObject);
        if ("1".equals(target))
            return true;
        else
            return false;
    }

    public static Short toShort(Object aObject) {
        try {
            return new Short(aObject.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Short toShortZero(Object aObject) {
        try {
            if (aObject == null)
                return new Short("0");
        } catch (Exception e) {
            return null;
        }
        return new Short(aObject.toString());
    }

    public static Integer toInteger(Object aObject) {
        try {
            return new Integer(aObject.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer toIntegerZero(Object aObject) {
        try {
            if (aObject == null)
                return new Integer("0");
        } catch (Exception e) {
            return null;
        }
        return new Integer(aObject.toString());
    }

    public static int toIntValue(Object value) {
        try {
            return Integer.valueOf(value.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public static long toLongValue(Object value) {
        try {
            return Long.valueOf(value.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public static Long toLong(Object aObject) {
        try {
            return new Long(aObject.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Long toLongZero(Object aObject) {
        try {
            if (aObject == null)
                return new Long("0");
        } catch (Exception e) {
            return null;
        }
        return new Long(aObject.toString());
    }

    public static Float toFloat(Object aObject) {
        try {
            return new Float(aObject.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Float toFloatZero(Object aObject) {
        try {
            if (aObject == null)
                return new Float("0");
        } catch (Exception e) {
            return null;
        }
        return new Float(aObject.toString());
    }

    public static Double toDouble(Object aObject) {
        try {
            return new Double(aObject.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Double toDoubleZero(Object aObject) {
        try {
            if (aObject == null)
                return new Double("0");
        } catch (Exception e) {
            return null;
        }
        return new Double(aObject.toString());
    }

    public static String toString(Object anObject) {
        if (anObject == null)
            return null;
        String str = anObject.toString();
        if (str.compareTo("") == 0)
            return null;
        else
            return str;
    }

    public static String toStringBlank(Object anObject) {
        String str = toString(anObject);
        if (str == null) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * trim char blank japanese/english
     *
     * @param obj
     * @return
     * @author cuongbd
     */
    public static String trimJ(String obj) {
        if (isNullOrEmpty(obj)) {
            return null;
        }
        String objResult = obj;
        objResult = objResult.replaceAll("(^\\p{Z}+|\\p{Z}+$)", "");
        objResult = objResult.trim();
        return objResult;
    }

    /*******************************************************************************
     * Object1・Object2の値が等しいがどうか判断します<BR>
     *
     * @param anObj1
     *        オブジェクト
     * @param anObj2
     *        オブジェクト
     * @return boolean(true:＝ false:≠)
     * @author cuongbd
     *******************************************************************************/
    public static boolean isEqual(Object anObj1, Object anObj2) {
        String str1 = toString(anObj1);
        String str2 = toString(anObj2);
        if (str1 == null && str2 == null) {
            return true;
        } else {
            if (str1 == null) {
                return false;
            }
            if (str2 == null) {
                return false;
            }
            // -- ＝の場合
            if (str1.equals(str2)) {
                return true;
                // -- ≠の場合
            } else {
                return false;
            }
        }
    }

    /**
	 *
	 */
    public static String replaceSpecialLiteral(String str) {
        String result = str;
        result = result.replaceAll("_", "__");
        result = result.replaceAll("%", "_%");
        result = result.replaceAll("％", "_％");
        return result;
    } 

    /**
     * オブジェクトの値がNullの場合、ゼロに変換する。
     *
     * @param val
     * @return　文字列タイプ
     */
    public static String nullToZero(Object val) {
        if (StringUtils.nullOrBlank(val)) {
            return "0";
        }
        return val.toString();
    }

    public static BigDecimal toBigDecimal(Object aObject) {
        try {
            return new BigDecimal(aObject.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static String escapseQuote(String aStr) {
    	String result = StringUtils.toEmpty(aStr);
    	result = result.replaceAll("'", "''");
    	return result;
    }
    
    /**
     * Cast number object (Long, Integer, Double, Float...) to BigDecimal
     * 
     * @param obj The object to convert
     * @return The BigDecimal value of obj
     */
	public static BigDecimal castNumericToBigDecimal(Object obj) {
		BigDecimal result = null;

		if (obj instanceof BigDecimal) {
			result = BigDecimal.class.cast(obj);
		} else if (obj instanceof Long) {
			result = new BigDecimal(Long.class.cast(obj));
		} else if (obj instanceof Integer) {
			result = new BigDecimal(Integer.class.cast(obj));
		} else if (obj instanceof Double) {
			result = new BigDecimal(Double.class.cast(obj));
		} else if (obj instanceof Float) {
			result = new BigDecimal(Float.class.cast(obj));
		} else {
			result = toBigDecimal(obj);
		}

		return result;
	}
	
    /**
     * 先頭にOを追加する
     * @param value
     * @param num
     * @return
     */
    public static String addZeroRight(Object value, int num) {
    	if (net.poweregg.util.StringUtils.nullOrBlank(value)) {
    		return "";
    	}
    	String ret = value.toString();
    	for (int i=ret.length(); i<num;i++) {
    		ret = ret + "0";
    	}
    	return ret;
    }
    /**
     * true: return 1
     * false: return 0;
     * @param value
     * @return
     */
    public static int booleanTotoInt(boolean value) {
       if(value){
    	   return 1;
       }else{
    	   return 0;
       }
    }

}
