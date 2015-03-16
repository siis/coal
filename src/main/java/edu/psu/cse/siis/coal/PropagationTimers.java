/* Soot - a J*va Optimization Framework
 * Copyright (C) 2003 Ondrej Lhotak
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.    See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

/*
 * Modified by the Sable Research Group and others 1997-1999.    
 * See the 'credits' file distributed with Soot for the complete list of
 * contributors.    (Soot is distributed at http://www.sable.mcgill.ca/soot)
 */

package edu.psu.cse.siis.coal;

import soot.Timer;

/**
 * Timers and counters for the execution of the COAL solver.
 */
public class PropagationTimers {
  private static PropagationTimers instance = new PropagationTimers();

  private PropagationTimers() {
  }

  public static PropagationTimers v() {
    synchronized (instance) {
      return instance;
    }
  }

  public static void clear() {
    instance = new PropagationTimers();
  }

  public Timer modelParsing = new Timer("modelParsing");

  public Timer problemGeneration = new Timer("IDE problem generation");

  public Timer ideSolution = new Timer("ideSolution");

  public Timer constantAnalysis = new Timer("Constant analysis");

  public Timer misc = new Timer("Misc");

  public Timer valueComposition = new Timer("Value composition");

  public Timer resultGeneration = new Timer("Result generation");

  public Timer totalTimer = new Timer("totalTimer");

  public Timer soot = new Timer("soot");

  public int entryPoints = 0;

  public int reachableMethods = 0;

  public long reachableStatements = 0;

  public Integer argumentValueTime = 0;

  public int classes = 0;

  public int pathValues = 0;
}
