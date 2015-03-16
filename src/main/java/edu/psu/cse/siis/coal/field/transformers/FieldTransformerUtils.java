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
package edu.psu.cse.siis.coal.field.transformers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import soot.Unit;
import soot.Value;
import edu.psu.cse.siis.coal.PropagationSolver;
import edu.psu.cse.siis.coal.arguments.ArgumentValueManager;
import edu.psu.cse.siis.coal.field.values.FieldValue;
import edu.psu.cse.siis.coal.values.BasePropagationValue;
import edu.psu.cse.siis.coal.values.PropagationValue;
import edu.psu.cse.siis.coal.values.TopPropagationValue;

/**
 * Utility functions for field transformers.
 */
public class FieldTransformerUtils {
  private static Logger logger = LoggerFactory.getLogger(FieldTransformerUtils.class);

  /**
   * Returns field transformers that model the influence of a referenced COAL value.
   * 
   * @param stmt The statement where the COAL value composition occurs.
   * @param symbol A COAL symbol that is referenced by the statement.
   * @param field The name of the field that is referenced by the statement.
   * @param type The type of the field that is referenced by the statement.
   * @param solver A propagation solver.
   * @param operation A field operation to be performed with the reference value.
   * @param returnedTop An array whose first element indicates whether a {@link TopFieldTransformer}
   *          was returned.
   * @return The resulting field transformers.
   */
  public static Set<FieldTransformer> makeTransformersFromReferencedValue(Unit stmt, Value symbol,
      String field, String type, PropagationSolver solver, String operation, Boolean[] returnedTop) {
    BasePropagationValue referencedBaseValue = solver.resultAt(stmt, symbol);
    if (referencedBaseValue == null) {
      if (logger.isDebugEnabled()) {
        logger.debug("Returning top0");
      }
      returnedTop[0] = true;
      return Collections.singleton(ArgumentValueManager.v().getTopFieldTransformer(type));
    } else if (referencedBaseValue instanceof TopPropagationValue) {
      if (logger.isDebugEnabled()) {
        logger.debug("Returning top1");
      }
      returnedTop[0] = true;
      return Collections.singleton(ArgumentValueManager.v().getTopFieldTransformer(type));
    } else {

      PropagationValue referencedValue = (PropagationValue) referencedBaseValue;
      referencedValue.makeFinalValue(solver);
      Set<FieldValue> fieldValues = referencedValue.getValuesForField(field);
      Set<FieldTransformer> result = new HashSet<>();
      for (FieldValue fieldValue : fieldValues) {
        if (fieldValue != null) {
          FieldTransformer fieldTransformer = IdentityFieldTransformer.v();
          Set<Object> values = fieldValue.getValues();
          if (values != null) {
            for (Object value : values) {
              fieldTransformer =
                  fieldTransformer.compose(FieldTransformerManager.v().makeFieldTransformer(
                      operation, value));
            }
          }
          result.add(fieldTransformer.intern());
        } else {
          result.add(null);
        }
      }
      if (logger.isDebugEnabled()) {
        logger.debug("Returning " + result.toString());
      }
      returnedTop[0] = false;
      return result;
    }
  }
}
