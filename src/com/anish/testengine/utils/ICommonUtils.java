package com.anish.testengine.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public interface ICommonUtils {
    static String getStackTraceString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
