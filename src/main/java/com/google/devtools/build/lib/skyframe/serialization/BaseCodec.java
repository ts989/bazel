// Copyright 2017 The Bazel Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.devtools.build.lib.skyframe.serialization;

import com.google.common.collect.ImmutableList;
import com.google.devtools.build.skyframe.SkyKey;
import com.google.devtools.build.skyframe.SkyValue;
import java.util.List;

/** An opaque interface for codecs that just reveals the {@link Class} of its objects. */
interface BaseCodec<T> {
  /**
   * Returns the class of the objects serialized/deserialized by this codec.
   *
   * <p>This is useful for automatically dispatching to the correct codec, e.g. in {@link
   * ObjectCodecs} and {@link BaseCodecMap}. It may also be useful for automatically registering
   * codecs for {@link SkyKey}s and {@link SkyValue}s instead of using the current manual mapping
   * (b/26186886).
   */
  Class<T> getEncodedClass();

  /**
   * Returns additional subtypes of {@code T} that may be serialized/deserialized using this codec
   * without loss of information.
   *
   * <p>This method is intended for when {@code T} has multiple concrete implementations whose
   * details are known to the codec but not to the codec dispatching mechanism. It signals that the
   * dispatcher may choose to use this codec for the subtype, rather than raise {@link
   * SerializationException.NoCodecException}.
   *
   * <p>This method should not be used if the codec's serialization and deserialization methods
   * perform their own dispatching to other codecs for subtypes of {@code T}.
   *
   * <p>{@code T} itself should not be included in the returned list.
   */
  default List<Class<? extends T>> additionalEncodedSubclasses() {
    return ImmutableList.of();
  }
}
