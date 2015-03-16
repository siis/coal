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
package edu.psu.cse.siis.coal;

import java.util.ArrayList;
import java.util.List;

import soot.Value;
import soot.jimple.Stmt;
import edu.psu.cse.siis.coal.field.SequenceElement;
import edu.psu.cse.siis.coal.field.TransformerSequence;
import edu.psu.cse.siis.coal.field.transformers.Add;
import edu.psu.cse.siis.coal.field.transformers.Remove;

public class Utils {
  public static TransformerSequence makeTransformerSequence(Value value, Stmt stmt) {
    List<SequenceElement> sequenceElements = new ArrayList<>();
    sequenceElements.add(new SequenceElement(value, stmt, Constants.DefaultActions.ADD));
    TransformerSequence transformerSequence = new TransformerSequence(sequenceElements);
    transformerSequence.addTransformerToSequence(new Add("testComposedAddValue"));
    transformerSequence.addTransformerToSequence(new Remove("testComposedRemoveValue"));

    return transformerSequence;
  }
}
