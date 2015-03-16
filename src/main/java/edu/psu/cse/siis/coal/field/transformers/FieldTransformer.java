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

package edu.psu.cse.siis.coal.field.transformers;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import edu.psu.cse.siis.coal.Internable;
import edu.psu.cse.siis.coal.Pool;
import edu.psu.cse.siis.coal.field.TransformerSequence;
import edu.psu.cse.siis.coal.field.values.FieldValue;
import edu.psu.cse.siis.coal.field.values.IntermediateFieldValue;

/**
 * A field transformer, which models the influence of a statement of a single field.
 */
public class FieldTransformer implements Internable<FieldTransformer> {
  private static final Pool<FieldTransformer> POOL = new Pool<>();

  protected Set<Object> add;
  protected Set<Object> remove;
  protected boolean clear;
  protected TransformerSequence transformerSequence;

  /**
   * Applies this field transformer to a {@link FieldValue}.
   * 
   * @param fieldValue A field value.
   * @return A field value.
   */
  public FieldValue apply(FieldValue fieldValue) {
    FieldValue result;

    if (this.transformerSequence != null || (fieldValue.hasTransformerSequence() && !this.clear)) {
      result = new IntermediateFieldValue();
    } else {
      result = new FieldValue();
    }

    if (!this.clear) {
      result.addAll(fieldValue.getValues());
      if (fieldValue.hasTransformerSequence()) {
        ((IntermediateFieldValue) result)
            .addTransformerSequence(((IntermediateFieldValue) fieldValue).getTransformerSequence());
      }
    }

    if (fieldValue.hasTransformerSequence() && !this.clear) {
      // If the field value has a non-empty transformer sequence, then we should start by
      // adding the add/remove operations to the sequence.
      FieldTransformer intermediateFieldTransformer = this.makeNonComposedFieldTransformer();
      ((IntermediateFieldValue) result).addTransformerToSequence(intermediateFieldTransformer);
    } else {
      if (this.remove != null) {
        result.removeAll(this.remove);
      }
      if (this.add != null) {
        result.addAll(this.add);
      }
    }

    if (this.transformerSequence != null) {
      ((IntermediateFieldValue) result).addTransformerSequence(this.transformerSequence);
    }

    return result.intern();
  }

  /**
   * Composes this field transformer with another one.
   * 
   * @param secondFieldOperation A field transformer.
   * @return The result of composing this field transformer with the argument transformer.
   */
  public FieldTransformer compose(FieldTransformer secondFieldOperation) {
    if (secondFieldOperation.clear) {
      return secondFieldOperation;
    }

    FieldTransformer result = new FieldTransformer();
    result.clear = this.clear;

    // Add the values from the first operation.
    if (this.add != null) {
      result.add = new HashSet<>(this.add);
    }
    if (this.remove != null) {
      result.remove = new HashSet<>(this.remove);
    }

    if (this.transformerSequence != null) {
      result.transformerSequence = new TransformerSequence(this.transformerSequence);
      if (secondFieldOperation.add != null || secondFieldOperation.remove != null) {
        // The second field transformer has some add or remove
        // operation. Start by applying these.
        FieldTransformer intermediateFieldTransformer =
            secondFieldOperation.makeNonComposedFieldTransformer();

        result.transformerSequence.addTransformerToSequence(intermediateFieldTransformer);
      }
    } else {
      // Add the values from the second operation.
      if (secondFieldOperation.add != null) {
        if (result.remove != null) {
          result.remove.removeAll(secondFieldOperation.add);
        }
        if (result.add == null) {
          result.add = new HashSet<>(secondFieldOperation.add);
        } else {
          result.add.addAll(secondFieldOperation.add);
        }
      }

      if (secondFieldOperation.remove != null) {
        if (result.add != null) {
          result.add.removeAll(secondFieldOperation.remove);
        }
        if (result.remove == null) {
          result.remove = new HashSet<>(secondFieldOperation.remove);
        } else {
          result.remove.addAll(secondFieldOperation.remove);
        }
      }
    }

    if (secondFieldOperation.transformerSequence != null) {
      // If a transformer sequence is found in the second field transformer, it means that
      // the latter contains a field composition as well. We add it last.
      if (result.transformerSequence == null) {
        result.transformerSequence = new TransformerSequence();
      }
      result.transformerSequence.addElementsToSequence(secondFieldOperation.transformerSequence);
    }

    return result.intern();
  }

  /**
   * Generates a field transformer that is identical to this instance, except that it does not
   * contain a transformer sequence.
   * 
   * @return A field transformer without transformer sequence.
   */
  public FieldTransformer makeNonComposedFieldTransformer() {
    FieldTransformer intermediateFieldTransformer = new FieldTransformer();
    intermediateFieldTransformer.add = this.add;
    intermediateFieldTransformer.remove = this.remove;

    return intermediateFieldTransformer.intern();
  }

  @Override
  public String toString() {
    return "Add " + this.add + ", remove " + this.remove + ", clear " + this.clear
        + ", transformer sequence " + this.transformerSequence;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.clear, this.add, this.remove, this.transformerSequence);
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof FieldTransformer)) {
      return false;
    }
    FieldTransformer secondFieldTransformer = (FieldTransformer) other;
    return Objects.equals(this.add, secondFieldTransformer.add)
        && Objects.equals(this.remove, secondFieldTransformer.remove)
        && this.clear == secondFieldTransformer.clear
        && Objects.equals(this.transformerSequence, secondFieldTransformer.transformerSequence);
  }

  @Override
  public FieldTransformer intern() {
    return POOL.intern(this);
  }
}
