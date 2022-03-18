package io.ubitec.interview_challenges.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class ReflectUtil {

    public static <T extends Comparable> T parseComparableInstance(String value, Class<T> type) {
        try {
            Method[] methods = type.getMethods();
            Optional<Method> methodOptional = Arrays.stream(methods)
                    .filter(method -> {
                        method.setAccessible(true);
                        return method.getName().startsWith("parse") && method.getParameterCount() == 1;
                    })
                    .findFirst();
            Method method = methodOptional.orElseThrow(() -> new RuntimeException("Method notfound"));
            return (T) method.invoke(null, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
