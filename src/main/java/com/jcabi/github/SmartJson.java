/**
 * Copyright (c) 2013-2025 Yegor Bugayenko
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the jcabi.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jcabi.github;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import java.io.IOException;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Smart JSON (supplementary help class).
 *
 * @since 0.5
 */
@Immutable
@ToString
@Loggable(Loggable.DEBUG)
@EqualsAndHashCode(of = "object")
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
final class SmartJson {

    /**
     * Encapsulated JSON object.
     */
    private final transient JsonReadable object;

    /**
     * Public ctor.
     * @param obj Readable object
     */
    SmartJson(final JsonReadable obj) {
        this.object = obj;
    }

    /**
     * Get its property as string.
     * @param name Name of the property
     * @return Value
     * @throws IOException If there is any I/O problem
     */
    public String text(
        final String name
    ) throws IOException {
        return this.value(name, JsonString.class).getString();
    }

    /**
     * Get its property as number.
     * @param name Name of the property
     * @return Value
     * @throws IOException If there is any I/O problem
     */
    public int number(
        final String name
    ) throws IOException {
        return this.value(name, JsonNumber.class).intValue();
    }

    /**
     * Get JSON.
     * @return JSON
     * @throws IOException If there is any I/O problem
     * @since 0.14
     */
    public JsonObject json() throws IOException {
        return this.object.json();
    }

    /**
     * Get its property as custom type.
     * @param name Name of the property
     * @param type Type of result expected
     * @return Value
     * @throws IOException If there is any I/O problem
     * @param <T> Type expected
     */
    public <T> T value(
        final String name,
        final Class<T> type
    ) throws IOException {
        final JsonObject json = this.json();
        if (!json.containsKey(name)) {
            throw new IllegalStateException(
                String.format(
                    "'%s' is absent in JSON: %s", name, json
                )
            );
        }
        final JsonValue value = json.get(name);
        if (value == null) {
            throw new IllegalStateException(
                String.format(
                    "'%s' is NULL in %s", name, json
                )
            );
        }
        if (value.getClass().isAssignableFrom(type)) {
            throw new IllegalStateException(
                String.format(
                    "'%s' is not of type %s", name, type
                )
            );
        }
        return type.cast(value);
    }

    /**
     * Checks if a certain key is present
     *  AND its ValueType isn't ValueType.NULL.
     * @param name Name of the key which ValueType should be checked.
     * @return Returns <code>true</code> if key <code>name</code> is present
     *  and its ValueType isn't ValueType.NULL, <code>false</code> otherwise.
     * @throws IOException If there is any I/O problem
     */
    public boolean hasNotNull(
        final String name
    ) throws IOException {
        final JsonValue value = this.object.json().get(name);
        return value != null
            && !ValueType.NULL.equals(value.getValueType());
    }
}
