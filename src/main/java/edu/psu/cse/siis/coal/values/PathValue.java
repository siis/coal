/*
 * Copyright (C) 2015 The Pennsylvania State University and the University of Wisconsin
 * Systems and Internet Infrastructure Security Laboratory
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
package edu.psu.cse.siis.coal.values;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import edu.psu.cse.siis.coal.PropagationSolver;
import edu.psu.cse.siis.coal.field.values.FieldValue;
import edu.psu.cse.siis.coal.field.values.IntermediateFieldValue;

/**
 * A COAL value for a single execution path. This is essentially a collection of field values.
 * 
 * @see FieldValue
 */
public class PathValue {
  private Map<String, FieldValue> fieldMap = new HashMap<>();

  /**
   * Adds a field value.
   * 
   * @param field The name of the field that should be added.
   * @param fieldValue The {@link FieldValue} to be added.
   */
  public void addFieldEntry(String field, FieldValue fieldValue) {
    this.fieldMap.put(field, fieldValue);
  }

  /**
   * Returns the mapping between field names and field values.
   * 
   * @return The mapping between field names and field values.
   */
  public Map<String, FieldValue> getFieldMap() {
    return this.fieldMap;
  }

  /**
   * Returns the value of a given field.
   * 
   * @param field The name of a field.
   * @return The value of the field if it was found, null otherwise.
   */
  public FieldValue getFieldValue(String field) {
    return this.fieldMap.get(field);
  }

  /**
   * Returns the value of a field converted to a specific type.
   * 
   * @param field The name of the field whose value should be returned.
   * @param type The type to which the field value should be converted.
   * @return The field value.
   */
  @SuppressWarnings("unchecked")
  public <T> Set<T> getFieldValue(String field, Class<T> type) {
    FieldValue fieldValue = this.fieldMap.get(field);
    if (fieldValue == null || fieldValue.getValues() == null) {
      return null;
    }
    Set<T> result = new HashSet<>();
    for (Object value : fieldValue.getValues()) {
      result.add((T) value);
    }
    return result;
  }

  /**
   * Returned the string value of a field.
   * 
   * @param field The name of the field whose value should be returned.
   * @return The field value.
   */
  public Set<String> getStringFieldValue(String field) {
    return getFieldValue(field, String.class);
  }

  /**
   * Determines whether this value contains a field value that has a given name and that is not
   * null.
   * 
   * @param field A field name.
   * @return True if there is a field by the name indicated that is not null.
   */
  public boolean containsNonNullFieldValue(String field) {
    if (!fieldMap.containsKey(field)) {
      return false;
    }
    FieldValue fieldValue = fieldMap.get(field);
    if (fieldValue == null) {
      return false;
    }
    Set<Object> values = fieldValue.getValues();
    return values != null && values.size() != 0;
  }

  /**
   * Get a single string value for a given field. Only use this method if you are sure that a field
   * cannot have a set of more than one value.
   * 
   * @param field A field name.
   * @return The single string value, or null if the field has no value.
   * @throws RuntimeException if the field has more than one value.
   */
  public String getSingleStringFieldValue(String field) {
    FieldValue fieldValue = this.fieldMap.get(field);
    if (fieldValue == null) {
      return null;
    }

    Set<Object> values = fieldValue.getValues();
    if (values == null) {
      return null;
    }

    if (values.size() != 1) {
      throw new RuntimeException("More than one string value for field " + field);
    }

    return (String) values.iterator().next();
  }

  /**
   * Determines if this object contains an {@link IntermediateFieldValue}.
   * 
   * @return True if this object contains an IntermediateFieldValue.
   */
  public boolean containsIntermediateField() {
    for (Map.Entry<String, FieldValue> entry : fieldMap.entrySet()) {
      if (entry.getValue() instanceof IntermediateFieldValue) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns the {@link PathValue} elements for this object without {@link IntermediateFieldValue} .
   * 
   * @param solver A propagation solver.
   * @return The objects without IntermediateFieldValue.
   */
  public Set<PathValue> makeFinalBranchValues(PropagationSolver solver) {
    List<String> partialValueFields = new ArrayList<>();
    Map<String, FieldValue> partialFieldMap = new HashMap<>();

    for (Map.Entry<String, FieldValue> entry : fieldMap.entrySet()) {
      FieldValue fieldValue = entry.getValue();
      if (fieldValue instanceof IntermediateFieldValue) {
        partialValueFields.add(entry.getKey());
      } else {
        partialFieldMap.put(entry.getKey(), fieldValue);
      }
    }

    Set<Map<String, FieldValue>> fieldMaps = Collections.singleton(partialFieldMap);

    for (String partialValueField : partialValueFields) {
      fieldMaps =
          addFieldValuesToFieldMaps(fieldMaps, partialValueField,
              ((IntermediateFieldValue) fieldMap.get(partialValueField)).makeFinalFieldValues(
                  partialValueField, solver));
    }

    Set<PathValue> result = new HashSet<>();
    for (Map<String, FieldValue> newFieldMap : fieldMaps) {
      PathValue newBranchValue = new PathValue();
      newBranchValue.fieldMap = newFieldMap;
      result.add(newBranchValue);
    }

    return result;
  }

  /**
   * Adds field values for a given field to a set of field maps.
   * 
   * @param fieldMaps A set of field maps.
   * @param field A field name.
   * @param fieldValues A set of field values associated with the field whose name is provided as an
   *          argument.
   * @return The resulting set of field maps.
   */
  private Set<Map<String, FieldValue>> addFieldValuesToFieldMaps(
      Set<Map<String, FieldValue>> fieldMaps, String field, Set<FieldValue> fieldValues) {
    Set<Map<String, FieldValue>> result = new HashSet<>();

    for (Map<String, FieldValue> fieldMap : fieldMaps) {
      result.addAll(addFieldValuesToFieldMap(fieldMap, field, fieldValues));
    }

    return result;
  }

  /**
   * Adds field values for a given field to a field map.
   * 
   * @param fieldMap A field map.
   * @param field A field name.
   * @param fieldValues A set of field values associated with the field whose name is provided as an
   *          argument.
   * @return The resulting set of field maps.
   */
  private Set<Map<String, FieldValue>> addFieldValuesToFieldMap(Map<String, FieldValue> fieldMap,
      String field, Set<FieldValue> fieldValues) {
    Set<Map<String, FieldValue>> result = new HashSet<>();

    for (FieldValue fieldValue : fieldValues) {
      Map<String, FieldValue> newFieldMap = new HashMap<>(fieldMap);
      if (fieldValue != null) {
        newFieldMap.put(field, fieldValue);
      }
      result.add(newFieldMap);
    }

    return result;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    List<String> parts = new ArrayList<>(this.fieldMap.size());

    for (Map.Entry<String, FieldValue> entry : this.fieldMap.entrySet()) {
      FieldValue value = entry.getValue();
      String valueString = value == null ? "null" : value.toString();
      parts.add(entry.getKey() + "=" + valueString + ",");
    }
    Collections.sort(parts);

    for (String part : parts) {
      result.append(part);
    }
    return result.toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.fieldMap);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof PathValue
        && Objects.equals(this.fieldMap, ((PathValue) other).fieldMap);
  }
}
