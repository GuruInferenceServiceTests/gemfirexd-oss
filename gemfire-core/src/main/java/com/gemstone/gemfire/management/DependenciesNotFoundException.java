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
package com.gemstone.gemfire.management;

import com.gemstone.gemfire.GemFireException;

/**
 * Indicates that required dependencies were not found in the ClassPath.
 * 
 * @author Abhishek Chaudhari
 * @since 7.0
 */
public class DependenciesNotFoundException extends GemFireException {
  private static final long serialVersionUID = 9082304929238159814L;

  /**
   * Constructs a new DependenciesNotFoundException with the specified detail 
   * message and cause.
   *
   * Note that the detail message associated with <code>cause</code> is
   * <i>not</i> automatically incorporated in this runtime exception's detail
   * message.
   * 
   * @param message
   *          The detail message.
   * @param cause
   *          The cause of this exception or <code>null</code> if the cause is
   *          unknown.
   */
  public DependenciesNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new DependenciesNotFoundException with the specified detail 
   * message.
   * 
   * @param message
   *          The detail message.
   */
  public DependenciesNotFoundException(String message) {
    super(message);
  }
}
