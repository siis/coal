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

/**
 * A factory for remove field transformers.
 */
public class FieldRemoveTransformerFactory extends FieldTransformerFactory {

  @Override
  FieldTransformer makeFieldTransformer(Object value) {
    return new Remove(value);
  }

}
