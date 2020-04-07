package itba.edu.ar.model;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class Either<T, S> {
    private final T value;
    private final S alternative;

    private Either(T value, S alternative) {
        this.value = value;
        this.alternative = alternative;
    }

    public static <T, S> Either<T, S> value(final T value) {
        return new Either<T, S>(value, null);
    }

    public static <T, S> Either<T, S> alternative(final S alternative) {
        return new Either<T, S>(null, alternative);
    }

    public T getValue() {
        if (!isValuePresent())
            throw new NoSuchElementException("No value present");
        return value;
    }

    public S getAlternative() {
        if (isValuePresent())
            throw new NoSuchElementException("No alternative present");
        return alternative;
    }

    public boolean isValuePresent() {
        return value != null;
    }

    public void ifPresent(Consumer<? super T> consumer) {
        if (value != null)
            consumer.accept(value);
    }
}

