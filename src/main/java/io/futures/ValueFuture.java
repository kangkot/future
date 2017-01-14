package io.futures;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

final class ValueFuture<T> extends SatisfiedFuture<T> {

  final T value;

  ValueFuture(final T value) {
    this.value = value;
  }

  @Override
  final <R> Future<R> map(final Function<T, R> f) {
    try {
      return new ValueFuture<>(f.apply(value));
    } catch (final RuntimeException ex) {
      return new ExceptionFuture<>(ex);
    }
  }

  @Override
  final <R> Future<R> flatMap(final Function<T, Future<R>> f) {
    try {
      return f.apply(value);
    } catch (final RuntimeException ex) {
      return new ExceptionFuture<>(ex);
    }
  }

  @Override
  final Future<T> onSuccess(final Consumer<T> c) {
    try {
      c.accept(value);
    } catch (final RuntimeException ex) {
      // TODO logging
    }
    return this;
  }

  @Override
  final Future<T> onFailure(final Consumer<RuntimeException> c) {
    return this;
  }
  
  @Override
  Future<T> rescue(Function<RuntimeException, Future<T>> f) {
    return this;
  }

  @Override
  Future<T> handle(Function<RuntimeException, T> f) {
    return this;
  }

  @Override
  protected final T get(final long timeout, final TimeUnit unit) {
    return value;
  }

  @Override
  public final int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  @Override
  public final boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final ValueFuture<?> other = (ValueFuture<?>) obj;
    if (value == null) {
      if (other.value != null)
        return false;
    } else if (!value.equals(other.value))
      return false;
    return true;
  }
}
