package io.ubitec.interview_challenges.util;

import java.util.concurrent.Callable;

public class UncheckUtil {

    public interface CustomRunnable {
        void run() throws Exception;
    }

    public static <T> T call(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}


