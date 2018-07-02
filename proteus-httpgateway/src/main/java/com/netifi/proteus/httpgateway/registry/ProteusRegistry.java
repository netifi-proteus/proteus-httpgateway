/**
 * Copyright 2018 Netifi Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netifi.proteus.httpgateway.registry;

import java.util.Objects;

public interface ProteusRegistry {

    boolean isRegisteredService(String service, String method);

    boolean isRegisteredService(Key key);

    /**
     *
     */
    class Key {
        public final String service;
        public final String method;

        public Key(final String service, final String method) {
            this.service = service;
            this.method = method;
        }

        public String getService() {
            return service;
        }

        public String getMethod() {
            return method;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return Objects.equals(service, key.service) &&
                    Objects.equals(method, key.method);
        }

        @Override
        public int hashCode() {
            return Objects.hash(service, method);
        }
    }
}
