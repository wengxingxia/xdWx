package com.xander.wx.common.utils;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * Description: 字符串格式化工具
 *
 * @author Xander
 * datetime: 2019/8/19 10:21
 */
public class StringFormatUtil {

    public static String format(String format, Object... args) {
        //example "this is an {} for use." example,{} 是占位符
        FormattingTuple ft = MessageFormatter.arrayFormat(format, args);
        return ft.getMessage();
    }


}
