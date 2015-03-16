/*
 * Copyright 2015 Systems and Internet Infrastructure Security Laboratory at the Pennsylvania
 * State University
 * Copyright 2015 The University of Wisconsin
 * 
 * Author: Damien Octeau
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.psu.cse.siis.coal.field.values;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import edu.psu.cse.siis.coal.Internable;
import edu.psu.cse.siis.coal.Pool;

/**
 * A COAL field value. It is modeled as a set of objects that could be anything, depending on the
 * problem.
 */
public class FieldValue implements Internable<FieldValue> {
  private static final Pool<FieldValue> POOL = new Pool<>();

  private Set<Object> values = null;

  public FieldValue() {
  }

  public FieldValue(FieldValue other) {
    this.values = other.values;
  }

  /**
   * Returns the values represented by this field value.
   * 
   * @return The values represented by this field value.
   */
  public Set<Object> getValues() {
    return this.values;
  }

  /**
   * Adds a set of values to this field value.
   * 
   * @param add A set of values.
   */
  public void addAll(Set<Object> add) {
    if (add == null) {
      return;
    }
    if (this.values == null) {
      this.values = new HashSet<>(add.size());
    }
    this.values.addAll(add);
  }

  /**
   * Removes a set of values from this field value.
   * 
   * @param remove A set of values.
   */
  public void removeAll(Set<Object> remove) {
    // No need to do anything if there is no value.
    if (this.values != null) {
      this.values.removeAll(remove);
    }
  }

  /**
   * Determines if the field value makes a reference to another COAL value. In other words, this
   * determines if the field value is not completely resolved.
   * 
   * @return True if the field value makes a reference to another COAL value.
   */
  public boolean hasTransformerSequence() {
    return false;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder("[");
    if (values != null) {
      List<String> parts = new ArrayList<>(values.size());
      for (Object object : values) {
        parts.add(object + ", ");
      }
      Collections.sort(parts);
      for (String part : parts) {
        result.append(part);
      }
    }
    result.append("]");
    return result.toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(values);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof FieldValue && Objects.equals(this.values, ((FieldValue) other).values);
  }

  @Override
  public FieldValue intern() {
    return POOL.intern(this);
  }
}
