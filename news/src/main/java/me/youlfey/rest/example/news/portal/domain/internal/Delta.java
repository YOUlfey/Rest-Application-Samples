package me.youlfey.rest.example.news.portal.domain.internal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ImmutableSet;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

public interface Delta<T> {
    boolean isChanged();

    T getOldValue();

    T getNewValue();

    @JsonIgnore
    Class getType();

    static <E> Delta<E> produce(E oldVal, E newVal) {
        if (isSimpleClass(oldVal.getClass())) {
            return new SimpleDelta<>(oldVal, newVal);
        }
        return new ComplexDelta<>(oldVal, newVal);
    }

    Set<Class> simpleClasses = ImmutableSet.of(
            UUID.class,
            String.class,
            Long.class,
            Integer.class,
            Double.class,
            BigInteger.class,
            LocalDateTime.class
    );

    static boolean isSimpleClass(Class<?> type) {
        return simpleClasses.contains(type);
    }

    @Data
    @ToString
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    class ComplexDelta<T> extends SimpleDelta<T> {
        private Map<String, Delta> deltaMap;


        private ComplexDelta(T oldValue, T newValue) {
            super(oldValue, newValue);
            deltaMap = new HashMap<>();
            generateDeltaMap(oldValue, newValue);
            super.setChanged(deltaMap.values().stream().map(Delta::isChanged).reduce((a, b) -> a | b).orElse(false));
        }

        private void generateDeltaMap(Object oldVal, Object newVal) {
            try {
                Field[] fields = oldVal.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    Object fieldNewVal = field.get(newVal);
                    Object fieldOldVal = field.get(oldVal);
                    if (!Objects.equals(fieldNewVal, fieldOldVal) && Objects.nonNull(fieldNewVal)) {
                        deltaMap.put(field.getName(), Delta.produce(fieldOldVal, fieldNewVal));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode
    class SimpleDelta<T> implements Delta<T> {
        private T oldValue;
        private T newValue;
        private boolean isChanged;
        @JsonIgnore
        private Class type;

        private SimpleDelta(T oldValue, T newValue) {
            this.oldValue = oldValue;
            this.newValue = newValue;
            type = oldValue.getClass();
            isChanged = !Objects.equals(oldValue, newValue);
        }
    }
}