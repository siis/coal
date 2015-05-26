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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.psu.cse.siis.coal.field.transformers.FieldTransformerManager;
import edu.psu.cse.siis.coal.field.values.FieldValue;
import edu.psu.cse.siis.coal.field.values.SetFieldValue;

public class PropagationValueTest {

  // private static Value value1 = new JimpleLocal("testLocal", null);
  // private static Value value2 = new JimpleLocal("testLocal2", null);
  // private static Stmt stmt = new JAssignStmt(value1, value2);

  private static String[] fields1 = { "field1", "field2", "field3", "field4" };
  private static String[] fields2 = { "field1", "field3", "field4", "field2" };
  private static String[] fields3 = { "field4", "field5", "field1" };
  private static FieldValue[] fieldValues1 = makeFieldValues("1", 3);
  private static FieldValue[] fieldValues2 = makeFieldValues("2", 3);
  private static FieldValue[] fieldValues3 = makeFieldValues("3", 2);
  private static PropagationValue simplePropagationValue;

  // private class MockPropagationSolver extends PropagationSolver {
  //
  // public MockPropagationSolver() {
  // super(new PropagationProblem(null));
  // }
  //
  // @Override
  // public BasePropagationValue resultAt(Unit unit, Value value) {
  // if (unit.equals(stmt) && value.equals(value1)) {
  // return simplePropagationValue;
  // } else {
  // return null;
  // }
  // }
  // }

  @BeforeClass
  public static void setUpClass() {
    FieldTransformerManager.v().registerDefaultFieldTransformerFactories();

    simplePropagationValue = new PropagationValue();

    simplePropagationValue.addPathValue(makeBranchValue(fields1, fieldValues1));
    simplePropagationValue.addPathValue(makeBranchValue(fields2, fieldValues2));
    simplePropagationValue.addPathValue(makeBranchValue(fields3, fieldValues3));
  }

  @Test
  public void testGetValuesForField() {
    Set<FieldValue> expectedFieldValues = new HashSet<>();
    expectedFieldValues.add(fieldValues1[0]);
    expectedFieldValues.add(fieldValues2[0]);
    expectedFieldValues.add(fieldValues3[2]);
    assertEquals(expectedFieldValues, simplePropagationValue.getValuesForField(fields1[0]));

    expectedFieldValues.clear();
    expectedFieldValues.add(fieldValues1[1]);
    expectedFieldValues.add(fieldValues2[3]);
    expectedFieldValues.add(null);
    assertEquals(expectedFieldValues, simplePropagationValue.getValuesForField(fields1[1]));

    expectedFieldValues.clear();
    expectedFieldValues.add(fieldValues1[2]);
    expectedFieldValues.add(fieldValues2[1]);
    expectedFieldValues.add(null);
    assertEquals(expectedFieldValues, simplePropagationValue.getValuesForField(fields1[2]));

    expectedFieldValues.clear();
    expectedFieldValues.add(fieldValues1[3]);
    expectedFieldValues.add(fieldValues2[2]);
    expectedFieldValues.add(fieldValues3[0]);
    assertEquals(expectedFieldValues, simplePropagationValue.getValuesForField(fields1[3]));

    expectedFieldValues.clear();
    expectedFieldValues.add(null);
    expectedFieldValues.add(fieldValues3[1]);
    assertEquals(expectedFieldValues, simplePropagationValue.getValuesForField(fields3[1]));

    assertFalse(expectedFieldValues.equals(simplePropagationValue.getValuesForField(fields1[0])));
  }

  // @Test
  // public void testMakeFinalValue() {
  // FieldValue[] fieldValues1 = makeFieldValues("11", 3);
  // FieldValue[] fieldValues2 = makeFieldValues("22", 3);
  //
  // IntermediateFieldValue intermediateFieldValue = new IntermediateFieldValue();
  // intermediateFieldValue.addAll(fieldValues2[1].getValues());
  // intermediateFieldValue.addTransformerSequence(Utils.makeTransformerSequence(value1, stmt));
  // fieldValues2[1] = intermediateFieldValue;
  //
  // PathValue initialBranchValue1 = makeBranchValue(fields1, fieldValues1);
  // PathValue initialBranchValue2 = makeBranchValue(fields2, fieldValues2);
  //
  // PropagationValue initialCollectingValue = new PropagationValue();
  // initialCollectingValue.addPathValue(initialBranchValue1);
  // initialCollectingValue.addPathValue(initialBranchValue2);
  //
  // PathValue finalBranchValue1 = makeBranchValue(fields1, fieldValues1);
  //
  // Set<Object> finalValuesBase = new HashSet<>();
  // finalValuesBase.add("value2211");
  // finalValuesBase.add("value2212");
  // finalValuesBase.add("value2213");
  //
  // Set<Object> finalValues1 = new HashSet<>(finalValuesBase);
  // finalValues1.add("value121");
  // finalValues1.add("value122");
  // finalValues1.add("value123");
  // finalValues1.add("testComposedAddValue");
  // FieldValue finalFieldValue1 = new FieldValue();
  // finalFieldValue1.addAll(finalValues1);
  // FieldValue[] finalFieldValues2 = Arrays.copyOf(fieldValues2, 4);
  // finalFieldValues2[1] = finalFieldValue1;
  // PathValue finalBranchValue2 = makeBranchValue(fields2, finalFieldValues2);
  //
  // Set<Object> finalValues2 = new HashSet<>(finalValuesBase);
  // finalValues2.add("value211");
  // finalValues2.add("value212");
  // finalValues2.add("value213");
  // finalValues2.add("testComposedAddValue");
  // FieldValue finalFieldValue2 = new FieldValue();
  // finalFieldValue2.addAll(finalValues2);
  // FieldValue[] finalFieldValues3 = Arrays.copyOf(fieldValues2, 4);
  // finalFieldValues3[1] = finalFieldValue2;
  // PathValue finalBranchValue3 = makeBranchValue(fields2, finalFieldValues3);
  //
  // Set<Object> finalValues3 = new HashSet<>(finalValuesBase);
  // finalValues2.add("testComposedAddValue");
  // FieldValue finalFieldValue3 = new FieldValue();
  // finalFieldValue3.addAll(finalValues3);
  // FieldValue[] finalFieldValues4 = Arrays.copyOf(fieldValues2, 4);
  // finalFieldValues4[1] = finalFieldValue3;
  // PathValue finalBranchValue4 = makeBranchValue(fields2, finalFieldValues4);
  //
  // PropagationValue expectedCollectingValue = new PropagationValue();
  // expectedCollectingValue.addPathValue(finalBranchValue1);
  // expectedCollectingValue.addPathValue(finalBranchValue2);
  // expectedCollectingValue.addPathValue(finalBranchValue3);
  // expectedCollectingValue.addPathValue(finalBranchValue4);
  //
  // MockPropagationSolver solver = new MockPropagationSolver();
  // initialCollectingValue.makeFinalValue(solver);
  //
  // assertEquals(expectedCollectingValue, initialCollectingValue);
  // assertEquals(expectedCollectingValue.toString(), initialCollectingValue.toString());
  // }

  public static PathValue makeBranchValue(String[] fields, FieldValue[] fieldValues) {
    if (fields.length != fieldValues.length) {
      fail("Problem with array lengths when making path value");
    }

    PathValue result = new PathValue();
    for (int i = 0; i < fields.length; ++i) {
      result.addFieldEntry(fields[i], fieldValues[i]);
    }
    return result;
  }

  private static FieldValue[] makeFieldValues(String suffix, int count) {
    FieldValue[] result = new FieldValue[count + 1];

    for (int i = 0; i < count; ++i) {
      SetFieldValue fieldValue = new SetFieldValue();
      Set<Object> values = new HashSet<>();
      values.add("value" + suffix + i + "1");
      values.add("value" + suffix + i + "2");
      values.add("value" + suffix + i + "3");
      fieldValue.addAll(values);
      result[i] = fieldValue;
    }

    result[count] = null;

    return result;
  }
}
