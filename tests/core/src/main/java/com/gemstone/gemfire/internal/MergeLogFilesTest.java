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
package com.gemstone.gemfire.internal;

import com.gemstone.gemfire.LogWriter;
import com.gemstone.gemfire.SystemFailure;

import dunit.DistributedTestCase;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import junit.framework.*;

/**
 * This class tests the functionality of the (new multi-threaded)
 * {@link MergeLogFiles} utility.
 *
 * @author David Whitlock
 *
 */
public class MergeLogFilesTest extends TestCase {

  public MergeLogFilesTest(String name) {
    super(name);
  }

  ////////  Test Methods

  /** The next integer to be written to the log */
  protected int next = 0;

  /**
   * A bunch of threads write a strictly increasing integer to log
   * "files" stored in byte arrays.  The files are merged and the
   * order is verified.
   */
  public void testMultipleThreads()
    throws InterruptedException, IOException {

    // Spawn a bunch of threads that write to a log 
    WorkerGroup group = new WorkerGroup("Workers");
    List workers = new ArrayList();
    for (int i = 0; i < 10; i++) {
      Worker worker = new Worker("Worker " + i, group);
      workers.add(worker);
      worker.start();
    }

    for (Iterator iter = workers.iterator(); iter.hasNext(); ) {
      Worker worker = (Worker) iter.next();
      DistributedTestCase.join(worker, 120 * 1000, null);
    }

    if (group.exceptionOccurred()) {
      fail(group.getExceptionString());
    }

    // Merge the log files together
    InputStream[] streams = new InputStream[workers.size()];
    String[] names = new String[workers.size()];
    for (int i = 0; i < workers.size(); i++) {
      Worker worker = (Worker) workers.get(i);
      streams[i] = worker.getInputStream();
      names[i] = worker.getName();
    }
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw, true);
    MergeLogFiles.mergeLogFiles(streams, names, pw);

    System.out.println(sw.toString());

    // Verfiy that the entries are sorted
    BufferedReader br =
      new BufferedReader(new StringReader(sw.toString()));
    Pattern pattern =
      Pattern.compile("^Worker \\d+: .* VALUE: (\\d+)");
    int lastValue = -1;
    while (br.ready()) {
      String line = br.readLine();
      if (line == null) {
        break;
      }

      Matcher matcher = pattern.matcher(line);
      if (matcher.matches()) {
        int value = Integer.parseInt(matcher.group(1));
        assertTrue(lastValue + " <= " + value, value > lastValue);
        lastValue = value;
      }
    }

    assertEquals(999, lastValue);
  }

  /**
   * A <code>ThreadGroup</code> for workers
   */
  static class WorkerGroup extends ThreadGroup {
    /** Did an uncaught exception occur in one of this group's
     * threads? */
    private boolean exceptionOccurred;

    /** A <code>StringBuffer</code> containing a description of the
     * uncaught exceptions thrown by the worker threads. */
    private StringBuffer sb;

    /**
     * Creates a new <code>WorkerGroup</code> with the given name
     */
    public WorkerGroup(String name) {
      super(name);
      sb = new StringBuffer();
    }

    public void uncaughtException(Thread t, Throwable e) {
      if (e instanceof VirtualMachineError) {
        SystemFailure.setFailure((VirtualMachineError)e); // don't throw
      }
      sb.append("Uncaught exception in thread ");
      sb.append(t.getName());
      sb.append("\n");
      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw, true));
      sb.append(sw.toString());
      sb.append("\n");

      exceptionOccurred = true;
    }

    /**
     * Returns whether or not an uncaught exception occurred in one of
     * the worker threads.
     */
    public boolean exceptionOccurred() {
      return this.exceptionOccurred;
    }

    /**
     * Returns a string describing the uncaught exception(s) that
     * occurred in the worker threads.
     */
    public String getExceptionString() {
      return this.sb.toString();
    }
  }

  /**
   * Writes a strictly increasing number to a log "file" stored in a
   * byte array.  Waits a random amount of time between writing
   * entries. 
   */
  class Worker extends Thread {
    /** The input stream for reading from the log file */
    private InputStream in;

    /** A random number generator */
    private Random random;

    /**
     * Creates a new <code>Worker</code> with the given name
     */
    public Worker(String name, ThreadGroup group) {
      super(group, name);
      this.random = new Random();
    }

    public void run() {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      LogWriter logger = new LocalLogWriter(LogWriterImpl.ALL_LEVEL,
                                            new PrintStream(baos, true));
      for (int i = 0; i < 100; i++) {
        int n;
        synchronized (MergeLogFilesTest.this) {
          n = MergeLogFilesTest.this.next++;

          // Have to log with the lock to guarantee ordering
          logger.info("VALUE: " + n);

          try {
            // Make sure that no two entries are at the same
            // millisecond.  Since Windows doesn't have millisecond
            // granularity, we have to wait at least ten.  Sigh.
            // DJM - even at 10, some entries have the same timestamp
            // on some PCs.
            Thread.sleep(15);
            
          } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            break; // TODO
          }
        }

        try {
          Thread.sleep(random.nextInt(250));

        } catch (InterruptedException ex) {
          Thread.currentThread().interrupt();
          break; // TODO
        }
      }

      System.out.println(baos.toString());
      in = new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * Returns an <code>InputStream</code> for reading from the log
     * that this worker wrote.
     */
    public InputStream getInputStream() {
      return this.in;
    }

  }

}
