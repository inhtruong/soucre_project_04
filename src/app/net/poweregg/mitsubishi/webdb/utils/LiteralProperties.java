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

import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * @author : lochg
 * @PG_ID :
 * @createDate : 2012/07/17
 */
public class LiteralProperties {

    private static LiteralProperties literalProperties = null;
    protected HashMap<String, ResourceBundle> properties;

    private LiteralProperties() {
        properties = new HashMap<String, ResourceBundle>();
    }

    public static final LiteralProperties getInstance() {
        if (literalProperties == null)
            literalProperties = new LiteralProperties();
        return literalProperties;
    }

    public void reload(String baseName) {
        ResourceBundle resourceBundle = ResourceBundle
                .getBundle((new StringBuilder()).append(baseName)
                        .append("-resource").toString());
        properties.put(baseName, resourceBundle);
    }

    public String getLiteral(String baseName, String key) {
        try {
            ResourceBundle bundle = findBundle(baseName);
            if (bundle != null) {
                return bundle.getString(key);
            } else {
                return null;
            }

        } catch (Exception e) {
            return null;
        }
    }

    public String[] getLiterals(String baseName, String key) {
        String ret[] = new String[0];
        String value = getLiteral(baseName, key);
        if (value != null) {
            CSVTokenizer csvTokenizer = new CSVTokenizer(value);
            ret = new String[csvTokenizer.countTokes()];
            for (int i = 0; csvTokenizer.hasMoreElements(); i++)
                ret[i] = csvTokenizer.nextToken();

        }
        return ret;
    }

    public Boolean getLiteralAsBoolean(String baseName, String key) {
        return ConvertUtils.toBoolean(getLiteral(baseName, key));
    }

    public Integer getLiteralAsInteger(String baseName, String key) {
        return ConvertUtils.toInteger(getLiteral(baseName, key));
    }

    public Short getLiteralAsShort(String baseName, String key) {
        return ConvertUtils.toShort(getLiteral(baseName, key));
    }

    public Float getLiteralAsFloat(String baseName, String key) {
        return ConvertUtils.toFloat(getLiteral(baseName, key));
    }

    public Long getLiteralAsLong(String baseName, String key) {
        return ConvertUtils.toLong(getLiteral(baseName, key));
    }
    
    public int getLiteralAsIntValue(String baseName, String key) {
        return ConvertUtils.toIntValue(getLiteral(baseName, key));
    }

    public boolean isResourceExisted(String subSystem) {
        boolean checkFlg = true;
        ResourceBundle resourceBundle = findBundle(subSystem);
        if (resourceBundle == null) {
            checkFlg = false;
        }
        return checkFlg;
    }

    protected ResourceBundle findBundle(String subSystem) {
        try {
            ResourceBundle resourceBundle = (ResourceBundle) properties
                    .get(subSystem);
            if (resourceBundle == null) {
                resourceBundle = ResourceBundle.getBundle((new StringBuilder())
                        .append(subSystem).append("-resource").toString());
                properties.put(subSystem, resourceBundle);
            }
            return resourceBundle;
        } catch (Exception e) {
            return null;
        }

    }
}
