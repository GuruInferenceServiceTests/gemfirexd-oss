/*
 * Copyright (c) 2010-2015 Pivotal Software, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License. See accompanying
 * LICENSE file.
 */
package com.gemstone.gemfire.cache.query.internal.parse;


import antlr.*;
import com.gemstone.gemfire.cache.query.internal.QCompiler;

/**
 *
 * @author Yogesh Mahajan
 */
public class ASTSortCriterion extends GemFireAST {
  private static final long serialVersionUID = -3654854374157753771L;
  public ASTSortCriterion() { }

  /** Creates a new instance of ASTSortCriterion */
  public ASTSortCriterion(Token t) {
    super(t);
  }
    
  @Override
  public void compile(QCompiler compiler) {
    super.compile(compiler);
    compiler.compileSortCriteria(this.getText());
  }

  
  
}