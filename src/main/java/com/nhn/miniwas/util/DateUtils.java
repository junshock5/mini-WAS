package com.nhn.miniwas.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    /**
     * 파일 처리 시간을 yyyyMMddHHmm 형태로 반환 한다.
     *
     * @param 현재 시간
     * @return 처리 시간의 문자열 202001004
     * @author topojs8
     */
    private static final ThreadLocal<SimpleDateFormat> getNowTimeToyyyyMMddHHmm = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmm");
        }
    };

    public static String getNowTimeToyyyyMMddHHmm(Date date) throws ParseException {
        return getNowTimeToyyyyMMddHHmm.get().format(date);
    }


}
