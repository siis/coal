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

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class DefaultCommandLineParser extends CommandLineParser<DefaultCommandLineArguments> {
  private static final String COPYRIGHT = "Copyright (C) 2015 The Pennsylvania State University\n"
      + "Systems and Internet Infrastructure Security Laboratory";

  @Override
  protected void parseAnalysisSpecificArguments(Options options) {
  }

  @Override
  protected void printHelp(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    System.out.println(COPYRIGHT);
    formatter.printHelp("coal -input <input directory> -classpath <classpath> "
        + "-output <output directory> [-model <model directory>] [-cmodel <compiled model path>] "
        + "[-appName <app name>] [-skipnonmodeledtypes]", options);
  }

}