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

package edu.psu.cse.siis.coal.arguments;

import java.util.Set;

import soot.Unit;

/**
 * An argument value analysis. Subclasses indicate how to analyze a certain argument type. They
 * should override:
 * <ul>
 * <li>{@link #computeArgumentValues} to compute the argument value at a given statement for a given
 * {@link Argument}.</li>
 * <li>{@link #computeInlineArgumentValues(String[])} to compute an inline argument value from a
 * string representation.</li>
 * <li>{@link #getTopValue()} to indicate how an unknown argument value should be represented.</li>
 * </ul>
 */
public interface ArgumentValueAnalysis {

  /**
   * Computes the possible argument value for a given statement and a given argument.
   * 
   * @param argument An {@link Argument}.
   * @param callSite A call statement.
   * @return The set of possible values for the argument.
   */
  public abstract Set<Object> computeArgumentValues(Argument argument, Unit callSite);

  /**
   * Computes a set of inline values from a string representation.
   * 
   * @param inlineValues An array of strings.
   * @return The set of inline values.
   */
  public abstract Set<Object> computeInlineArgumentValues(String[] inlineValues);

  /**
   * Returns the representation of an unknown argument value.
   * 
   * @return The representation of an unknown argument value.
   */
  public abstract Object getTopValue();

}
