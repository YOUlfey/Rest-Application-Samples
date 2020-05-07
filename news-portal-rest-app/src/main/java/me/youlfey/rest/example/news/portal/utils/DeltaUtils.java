package me.youlfey.rest.example.news.portal.utils;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeltaUtils {
    public static Set<String> ignoreFields = Stream.of("id", "modifiedDate").collect(Collectors.toSet());
    public static <T> void update(T resObj, T newObj, Set<String> ignoreFieldNames)  {
        try {
            Field[] fields = resObj.getClass().getDeclaredFields();
            for (Field field: fields) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                if (!ignoreFieldNames.contains(field.getName())) {
                    if (Objects.nonNull(field.get(newObj))) {
                        field.set(resObj, field.get(newObj));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
