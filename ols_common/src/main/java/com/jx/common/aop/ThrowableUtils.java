package com.jx.common.aop;

public class ThrowableUtils {
    private static final String EMPTY = "";
    private static final String NEW_LINE = "\n";

    public ThrowableUtils() {
    }

    private static boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    public static String getString(Throwable throwable) {
        if (throwable == null) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder();
            String clazzName = throwable.getClass().getName();
            builder.append(clazzName).append(": ");
            String message = throwable.getLocalizedMessage();
            if (isEmpty(message)) {
                builder.append("引发类型为“").append(clazzName).append("”的异常。");
            } else {
                builder.append(message);
            }

            Throwable innerThrowable = throwable.getCause();
            if (innerThrowable != null) {
                builder.append(" --->").append(getString(innerThrowable)).append(NEW_LINE).append(" ---内部异常堆栈跟踪的结尾 ---");
            }

            StackTraceElement[] traces = throwable.getStackTrace();
            if (traces != null) {
                StackTraceElement[] var6 = traces;
                int var7 = traces.length;

                for (int var8 = 0; var8 < var7; ++var8) {
                    StackTraceElement trace = var6[var8];
                    builder.append(NEW_LINE).append(" 在").append(trace);
                }
            }
            return builder.toString();
        }
    }

    public static String getMessage(Throwable throwable) {
        if (throwable == null) {
            return "";
        } else {
            String message = throwable.getLocalizedMessage();
            return isEmpty(message) ? "引发类型为“" + throwable.getClass().getName() + "”的异常。" : message;
        }
    }
}
