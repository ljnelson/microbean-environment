/* -*- mode: Java; c-basic-offset: 2; indent-tabs-mode: nil; coding: utf-8-unix -*-
 *
 * Copyright © 2021–2022 microBean™.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.microbean.environment.api;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import java.util.Objects;

/**
 * A holder of a {@link Type} that embodies <a
 * href="http://gafter.blogspot.com/2006/12/super-type-tokens.html"
 * target="_parent">Gafter's gadget</a>.
 *
 * <p>To use this class, create a new instance of an anonymous
 * subclass of it, and then call {@link #type() type()} on it.  For
 * example:</p>
 *
 * <blockquote><pre>
 * // type will be a {@link ParameterizedType} whose {@link ParameterizedType#getRawType() rawType} is {@link java.util.List List.class} and
 * // whose {@linkplain ParameterizedType#getActualTypeArguments() sole type argument} is {@link String String.class}
 * {@link Type} type = new {@link TypeToken}&lt;{@link java.util.List List}&lt;{@link String}&gt;&gt;() {}.{@link #type() type()};</pre></blockquote>
 *
 * @param <T> the modeled type; often parameterized
 *
 * @author <a href="https://about.me/lairdnelson"
 * target="_parent">Laird Nelson</a>
 *
 * @see #type()
 */
public abstract class TypeToken<T> {


  /*
   * Instance fields.
   */


  private final Type type;


  /*
   * Constructors.
   */


  /**
   * Creates a new {@link TypeToken}.
   */
  protected TypeToken() {
    super();
    this.type = mostSpecializedParameterizedSuperclass(this.getClass()).getActualTypeArguments()[0];
  }


  /*
   * Instance methods.
   */


  /**
   * Returns the {@link Type} modeled by this {@link TypeToken}.
   *
   * @return the {@link Type} modeled by this {@link TypeToken}; never
   * {@code null}
   *
   * @nullability This method never returns {@code null}.
   *
   * @threadsafety This method is safe for concurrent use by multiple
   * threads.
   *
   * @idempotency This method is idempotent and deterministic.
   */
  public final Type type() {
    return this.type;
  }

  /**
   * Returns a hashcode for this {@link TypeToken} computed from the
   * {@link Type} it {@linkplain #type() models}.
   *
   * @return a hashcode for this {@link TypeToken}
   *
   * @threadsafety This method is, and its overrides must be, safe for
   * concurrent use by multiple threads.
   *
   * @idempotency This method is, and its overrides must be,
   * idempotent and deterministic.
   *
   * @see #equals(Object)
   */
  @Override // Object
  public int hashCode() {
    final Type type = this.type();
    return type == null ? 0 : type.hashCode();
  }

  /**
   * Returns {@code true} if the supplied {@link Object} is equal to
   * this {@link TypeToken}.
   *
   * <p>This method returns {@code true} if the supplied {@link
   * Object}'s {@linkplain Object#getClass() class} is this {@link
   * TypeToken}'s class and if its {@linkplain #type() modeled
   * <code>Type</code>} is equal to this {@link TypeToken}'s
   * {@linkplain #type() modeled <code>Type</code>}.</p>
   *
   * @param other the {@link Object} to test; may be {@code null} in
   * which case {@code false} will be returned
   *
   * @return {@code true} if the supplied {@link Object} is equal to
   * this {@link TypeToken}; {@code false} otherwise
   *
   * @threadsafety This method is, and its overrides must be, safe for
   * concurrent use by multiple threads.
   *
   * @idempotency This method is, and its overrides must be,
   * idempotent and deterministic.
   *
   * @see #hashCode()
   */
  @Override // Object
  public boolean equals(final Object other) {
    if (other == this) {
      return true;
    } else if (other instanceof TypeToken<?> tt) {
      return Objects.equals(this.type(), tt.type());
    } else {
      return false;
    }
  }

  /**
   * Returns a {@link String} representation of this {@link
   * TypeToken}.
   *
   * <p>This method returns a value equal to that returned by {@link
   * Type#getTypeName() this.type().getTypeName()}.</p>
   *
   * @return a {@link String} representation of this {@link
   * TypeToken}; never {@code null}
   *
   * @nullability This method does not, and its overrides must not,
   * return {@code null}.
   *
   * @threadsafety This method is, and its overrides must be, safe for
   * concurrent use by multiple threads.
   *
   * @idempotency This method is, and its overrides must be,
   * idempotent and deterministic.
   */
  @Override // Object
  public String toString() {
    final Type type = this.type();
    return type == null ? "null" : type.getTypeName();
  }


  /*
   * Static methods.
   */

  
  private static final ParameterizedType mostSpecializedParameterizedSuperclass(final Type type) {
    return mostSpecializedParameterizedSuperclass(TypeToken.class, type);
  }

  private static final ParameterizedType mostSpecializedParameterizedSuperclass(final Class<?> stopClass, final Type type) {
    if (type == null || type == Object.class || type == stopClass) {
      return null;
    } else {
      final Class<?> erasure;
      if (type instanceof Class<?> c) {
        erasure = c;
      } else if (type instanceof ParameterizedType p) {
        erasure = (Class<?>)p.getRawType();
      } else {
        erasure = null;
      }
      if (erasure == null || erasure == Object.class || !(stopClass.isAssignableFrom(erasure))) {
        return null;
      } else {
        return type instanceof ParameterizedType p ? p : mostSpecializedParameterizedSuperclass(stopClass, erasure.getGenericSuperclass());
      }
    }
  }


}
