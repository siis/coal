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
package edu.psu.cse.siis.coal.field.transformers.set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import edu.psu.cse.siis.coal.field.transformers.FieldTransformer;
import edu.psu.cse.siis.coal.field.values.FieldValue;
import edu.psu.cse.siis.coal.field.values.SetFieldValue;

public class SetFieldTransformerTest {

  // private Value value1 = new JimpleLocal("testLocal", null);
  // private Value value2 = new JimpleLocal("testLocal2", null);

  // private Stmt stmt = new JAssignStmt(this.value1, this.value2);

  // private TransformerSequence transformerSequence = Utils.makeTransformerSequence(value1, stmt);

  @Test
  public void testApplyWithClear() {
    Set<Object> expectedValueSet = new HashSet<>();
    expectedValueSet.add("addTest1");
    expectedValueSet.add("addTest2");
    SetFieldValue expectedFieldValue = new SetFieldValue();
    expectedFieldValue.addAll(expectedValueSet);

    testApplyHelper(true, expectedFieldValue);
  }

  @Test
  public void testApplyWithoutClear() {
    Set<Object> expectedValueSet = new HashSet<>();
    expectedValueSet.add("testString1");
    expectedValueSet.add("testString3");
    expectedValueSet.add("testString4");
    expectedValueSet.add("addTest1");
    expectedValueSet.add("addTest2");
    SetFieldValue expectedFieldValue = new SetFieldValue();
    expectedFieldValue.addAll(expectedValueSet);

    testApplyHelper(false, expectedFieldValue);
  }

  // @Test
  // public void testApplyWithTransformerSequenceInTransformer() {
  // FieldTransformer fieldTransformer = makeSecondFieldTransformer(false);
  // fieldTransformer.transformerSequence = transformerSequence;
  //
  // FieldValue fieldValue = makeInitialFieldValue(false);
  // FieldValue resultFieldValue = fieldTransformer.apply(fieldValue);
  //
  // Set<Object> expectedValueSet = new HashSet<>();
  // expectedValueSet.add("testString1");
  // expectedValueSet.add("testString3");
  // expectedValueSet.add("testString4");
  // expectedValueSet.add("addTest1");
  // expectedValueSet.add("addTest2");
  // FieldValue expectedFieldValue = new IntermediateFieldValue();
  // expectedFieldValue.addAll(expectedValueSet);
  // ((IntermediateFieldValue) expectedFieldValue)
  // .addTransformerSequence(fieldTransformer.transformerSequence);
  //
  // assertEquals(expectedFieldValue, resultFieldValue);
  // }

  // @Test
  // public void testApplyWithTransformerSequenceInValue() {
  // FieldTransformer fieldTransformer = makeSecondFieldTransformer(false);
  // // fieldTransformer.transformerSequence = makeTransformerSequence();
  //
  // FieldValue fieldValue = makeInitialFieldValue(true);
  // ((IntermediateFieldValue) fieldValue).addTransformerSequence(transformerSequence);
  // FieldValue resultFieldValue = fieldTransformer.apply(fieldValue);
  //
  // Set<Object> expectedValueSet = new HashSet<>();
  // expectedValueSet.add("testString1");
  // expectedValueSet.add("testString2");
  // expectedValueSet.add("testString3");
  // expectedValueSet.add("testString4");
  // expectedValueSet.add("testString5");
  // FieldValue expectedFieldValue = new IntermediateFieldValue();
  // expectedFieldValue.addAll(expectedValueSet);
  // List<SequenceElement> sequenceElements = new ArrayList<>();
  // sequenceElements.add(new SequenceElement(value1, stmt, Constants.DefaultActions.ADD));
  // TransformerSequence expectedTransformerSequence = new TransformerSequence(sequenceElements);
  // expectedTransformerSequence.addTransformerToSequence(new Add("testComposedAddValue"));
  // expectedTransformerSequence.addTransformerToSequence(new Remove("testComposedRemoveValue"));
  // expectedTransformerSequence.addTransformerToSequence(fieldTransformer);
  // ((IntermediateFieldValue) expectedFieldValue)
  // .addTransformerSequence(expectedTransformerSequence);
  //
  // assertEquals(expectedFieldValue.getValues(), resultFieldValue.getValues());
  // assertEquals(expectedTransformerSequence,
  // ((IntermediateFieldValue) resultFieldValue).getTransformerSequence());
  // }

  @Test
  public void testComposeWithClear() {
    FieldTransformer firstFieldTransformer = makeFirstFieldTransformer();
    FieldTransformer secondFieldTransformer = makeSecondFieldTransformer(true);

    FieldTransformer resultFieldTransformer = firstFieldTransformer.compose(secondFieldTransformer);

    assertEquals(secondFieldTransformer, resultFieldTransformer);
  }

  @Test
  public void testComposeWithoutClear() {
    FieldTransformer firstFieldTransformer = makeFirstFieldTransformer();
    FieldTransformer secondFieldTransformer = makeSecondFieldTransformer(false);

    FieldTransformer resultFieldTransformer = firstFieldTransformer.compose(secondFieldTransformer);

    SetFieldTransformer expectedFieldTransformer = new SetFieldTransformer();
    expectedFieldTransformer.clear = false;
    expectedFieldTransformer.add = new HashSet<>();
    expectedFieldTransformer.add.add("addTestFirst1");
    expectedFieldTransformer.add.add("addTestFirst2");
    expectedFieldTransformer.add.add("addTestFirst3");
    expectedFieldTransformer.add.add("addTest1");
    expectedFieldTransformer.add.add("addTest2");
    expectedFieldTransformer.remove = new HashSet<>();
    expectedFieldTransformer.remove.add("removeTest1");
    expectedFieldTransformer.remove.add("removeTest2");
    expectedFieldTransformer.remove.add("testString2");
    expectedFieldTransformer.remove.add("testString5");

    assertEquals(expectedFieldTransformer, resultFieldTransformer);
    assertEquals(expectedFieldTransformer.toString(), resultFieldTransformer.toString());
    assertEquals(expectedFieldTransformer.hashCode(), resultFieldTransformer.hashCode());
    assertFalse(firstFieldTransformer.equals(secondFieldTransformer));
    assertFalse(firstFieldTransformer.equals(this));
  }

  // @Test
  // public void testComposeWithTransformerSequenceInFirstTransformer() {
  // FieldTransformer firstFieldTransformer = makeFirstFieldTransformer();
  // firstFieldTransformer.transformerSequence = transformerSequence;
  // FieldTransformer secondFieldTransformer = makeSecondFieldTransformer(false);
  //
  // FieldTransformer resultFieldTransformer =
  // firstFieldTransformer.compose(secondFieldTransformer);
  //
  // FieldTransformer expectedFieldTransformer = new FieldTransformer();
  // expectedFieldTransformer.clear = false;
  // expectedFieldTransformer.add = new HashSet<>();
  // expectedFieldTransformer.add.add("addTestFirst1");
  // expectedFieldTransformer.add.add("addTestFirst2");
  // expectedFieldTransformer.add.add("addTestFirst3");
  // expectedFieldTransformer.add.add("testString5");
  // expectedFieldTransformer.remove = new HashSet<>();
  // expectedFieldTransformer.remove.add("removeTest1");
  // expectedFieldTransformer.remove.add("removeTest2");
  // expectedFieldTransformer.remove.add("addTest1");
  //
  // List<SequenceElement> sequenceElements = new ArrayList<>();
  // sequenceElements.add(new SequenceElement(value1, stmt, Constants.DefaultActions.ADD));
  // TransformerSequence expectedTransformerSequence = new TransformerSequence(sequenceElements);
  // expectedTransformerSequence.addTransformerToSequence(new Add("testComposedAddValue"));
  // expectedTransformerSequence.addTransformerToSequence(new Remove("testComposedRemoveValue"));
  // expectedTransformerSequence.addTransformerToSequence(secondFieldTransformer);
  // expectedFieldTransformer.transformerSequence = expectedTransformerSequence;
  //
  // assertEquals(expectedFieldTransformer, resultFieldTransformer);
  // assertEquals(expectedTransformerSequence.hashCode(),
  // resultFieldTransformer.transformerSequence.hashCode());
  // assertEquals(expectedTransformerSequence.toString(),
  // resultFieldTransformer.transformerSequence.toString());
  // assertFalse(expectedTransformerSequence.equals(this));
  // }

  // @Test
  // public void testComposeWithTransformerSequenceInSecondTransformer() {
  // FieldTransformer firstFieldTransformer = makeFirstFieldTransformer();
  // FieldTransformer secondFieldTransformer = makeSecondFieldTransformer(false);
  // secondFieldTransformer.transformerSequence = transformerSequence;
  //
  // FieldTransformer resultFieldTransformer =
  // firstFieldTransformer.compose(secondFieldTransformer);
  //
  // FieldTransformer expectedFieldTransformer = new FieldTransformer();
  // expectedFieldTransformer.clear = false;
  // expectedFieldTransformer.add = new HashSet<>();
  // expectedFieldTransformer.add.add("addTestFirst1");
  // expectedFieldTransformer.add.add("addTestFirst2");
  // expectedFieldTransformer.add.add("addTestFirst3");
  // expectedFieldTransformer.add.add("addTest1");
  // expectedFieldTransformer.add.add("addTest2");
  // expectedFieldTransformer.remove = new HashSet<>();
  // expectedFieldTransformer.remove.add("removeTest1");
  // expectedFieldTransformer.remove.add("removeTest2");
  // expectedFieldTransformer.remove.add("testString2");
  // expectedFieldTransformer.remove.add("testString5");
  //
  // List<SequenceElement> sequenceElements = new ArrayList<>();
  // sequenceElements.add(new SequenceElement(value1, stmt, Constants.DefaultActions.ADD));
  // TransformerSequence expectedTransformerSequence = new TransformerSequence(sequenceElements);
  // expectedTransformerSequence.addTransformerToSequence(new Add("testComposedAddValue"));
  // expectedTransformerSequence.addTransformerToSequence(new Remove("testComposedRemoveValue"));
  // expectedTransformerSequence.addTransformerToSequence(secondFieldTransformer);
  // expectedFieldTransformer.transformerSequence = transformerSequence;
  //
  // assertEquals(expectedFieldTransformer, resultFieldTransformer);
  // }

  private void testApplyHelper(boolean clear, FieldValue expectedFieldValue) {
    SetFieldTransformer fieldTransformer = makeSecondFieldTransformer(clear);

    FieldValue fieldValue = makeInitialFieldValue(false);

    FieldValue resultFieldValue = fieldTransformer.apply(fieldValue);

    assertEquals("Field values are not equal", expectedFieldValue, resultFieldValue);
  }

  private SetFieldTransformer makeFirstFieldTransformer() {
    SetFieldTransformer fieldTransformer = new SetFieldTransformer();
    fieldTransformer.clear = false;
    fieldTransformer.add = new HashSet<>();
    fieldTransformer.add.add("addTestFirst1");
    fieldTransformer.add.add("addTestFirst2");
    fieldTransformer.add.add("addTestFirst3");
    fieldTransformer.add.add("testString5");
    fieldTransformer.remove = new HashSet<>();
    fieldTransformer.remove.add("removeTest1");
    fieldTransformer.remove.add("removeTest2");
    fieldTransformer.remove.add("addTest1");

    return fieldTransformer;
  }

  private SetFieldTransformer makeSecondFieldTransformer(boolean clear) {
    SetFieldTransformer fieldTransformer = new SetFieldTransformer();
    fieldTransformer.clear = clear;
    fieldTransformer.add = new HashSet<>();
    fieldTransformer.add.add("addTest1");
    fieldTransformer.add.add("addTest2");
    fieldTransformer.remove = new HashSet<>();
    fieldTransformer.remove.add("testString2");
    fieldTransformer.remove.add("testString5");

    return fieldTransformer;
  }

  private SetFieldValue makeInitialFieldValue(boolean intermediate) {
    // FieldValue fieldValue = intermediate ? new IntermediateFieldValue() : new FieldValue();
    SetFieldValue setFieldValue = new SetFieldValue();

    Set<Object> testFieldValueSet = new HashSet<>();

    testFieldValueSet.add("testString1");
    testFieldValueSet.add("testString2");
    testFieldValueSet.add("testString3");
    testFieldValueSet.add("testString4");
    testFieldValueSet.add("testString5");

    setFieldValue.addAll(testFieldValueSet);

    return setFieldValue;
  }
}
