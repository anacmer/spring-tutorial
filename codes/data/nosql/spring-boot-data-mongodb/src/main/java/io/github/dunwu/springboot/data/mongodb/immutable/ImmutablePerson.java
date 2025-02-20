/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.dunwu.springboot.data.mongodb.immutable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;
import org.bson.types.ObjectId;

/**
 * Immutable object.
 * @author Mark Paluch
 */
@With
@Getter
@RequiredArgsConstructor
public class ImmutablePerson {

    private final ObjectId id;
    private final int randomNumber;

    public ImmutablePerson() {
        this(null, 0);
    }

}
