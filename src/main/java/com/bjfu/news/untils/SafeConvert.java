package com.bjfu.news.untils;

import org.apache.commons.lang.StringUtils;

import java.time.Instant;
import java.util.Date;

abstract public class SafeConvert {

    public static int toInt(Object obj) {
        return toInt(obj, 0);
    }

    public static int toInt(Object obj, int defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        if (obj instanceof Long) {
            // 这里可能会丢失精度
            return ((Long) obj).intValue();
        }
        String str = obj.toString();
        str = str == null ? null : str.trim();
        if (str == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }


    public static long toLong(Object obj) {
        return toLong(obj, 0);
    }

    public static long toLong(Object obj, long defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        if (obj instanceof Long) {
            return (Long) obj;
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).longValue();
        }
        if (obj instanceof Date) {
            return ((Date) obj).getTime();
        }
        if (obj instanceof Instant) {
            return ((Instant) obj).toEpochMilli();
        }
        String str = obj.toString();
        str = str == null ? null : str.trim();
        if (str == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static double toDouble(Object obj) {
        return toDouble(obj, 0);
    }

    public static double toDouble(Object obj, double defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        if (obj instanceof Double) {
            return (Double) obj;
        }
        String str = obj.toString();
        str = str == null ? null : str.trim();
        if (str == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(str);
        } catch (final NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public static boolean toBoolean(Object obj) {
        return toBoolean(obj, false);
    }

    public static boolean toBoolean(Object obj, boolean defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        String str = obj.toString();
        str = str == null ? null : str.trim();
        return StringUtils.equals(str, "true");
    }
}
