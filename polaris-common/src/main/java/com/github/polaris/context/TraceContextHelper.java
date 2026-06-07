package com.github.polaris.context;

import com.github.polaris.constants.CommonConstants;
import org.slf4j.MDC;

import java.util.Map;

public class TraceContextHelper {

    public static String currentTrace() {
        return MDC.get(CommonConstants.TRACE_ID);
    }

    public static void setTrace(String traceId) {
        MDC.put(CommonConstants.TRACE_ID, traceId);
    }

    public static void clearTrace() {
        MDC.clear();
    }

    public static Map<String, String> currentContext() {
        return MDC.getCopyOfContextMap();
    }

    public static void setContext(Map<String, String> map) {
        MDC.setContextMap(map);
    }

    public static void clearContext() {
        MDC.clear();
    }
}
