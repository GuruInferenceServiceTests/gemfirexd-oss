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
package com.gemstone.gemfire.internal.cache.ha;

import java.io.IOException;
import com.gemstone.gemfire.cache.CacheException;
import com.gemstone.gemfire.internal.cache.Conflatable;
import com.gemstone.gemfire.internal.cache.EventID;

/**
 * Test runs all tests of HARegionQueueJUnitTest using BlockingHARegionQueue
 * instead of HARegionQueue.
 * 
 * @author Suyog Bhokare
 *  
 */
public class BlockingHARegionQueueJUnitTest extends HARegionQueueJUnitTest
{

  public BlockingHARegionQueueJUnitTest(String arg0) {
    super(arg0);
  }

  /**
   * Creates Blocking HA region-queue object
   * 
   * @return Blocking HA region-queue object
   * @throws IOException
   * @throws ClassNotFoundException
   * @throws CacheException
   * @throws InterruptedException
   */
  protected HARegionQueue createHARegionQueue(String name)
      throws IOException, ClassNotFoundException, CacheException, InterruptedException
  {
    HARegionQueue regionqueue = HARegionQueue.getHARegionQueueInstance(name,
        cache, HARegionQueue.BLOCKING_HA_QUEUE, false);
    return regionqueue;
  }

  /**
   * Creates Blocking HA region-queue object
   * 
   * @return Blocking HA region-queue object
   * @throws IOException
   * @throws ClassNotFoundException
   * @throws CacheException
   * @throws InterruptedException
   */
  protected HARegionQueue createHARegionQueue(String name,
      HARegionQueueAttributes attrs) throws IOException, ClassNotFoundException, CacheException, InterruptedException
  {
    HARegionQueue regionqueue = HARegionQueue.getHARegionQueueInstance(name,
        cache, attrs, HARegionQueue.BLOCKING_HA_QUEUE, false);
    return regionqueue;
  }

  /**
   * Tests the effect of a put which is blocked because of capacity constraint &
   * subsequent passage because of take operation
   * 
   * @author ashahid
   */
  public void testBlockingPutAndTake()
  {
    try {
      HARegionQueueAttributes hrqa = new HARegionQueueAttributes();
      hrqa.setBlockingQueueCapacity(1);
      final HARegionQueue hrq = this.createHARegionQueue("testBlockingPutAndTake",
          hrqa);
      hrq.setPrimary(true);//fix for 40314 - capacity constraint is checked for primary only.
      EventID id1 = new EventID(new byte[] { 1 }, 1, 1);
      hrq.put(new ConflatableObject("key1", "val1", id1, false, "testing"));
      Thread t1 = new Thread(new Runnable() {
        public void run() {
          try{
          EventID id2 = new EventID(new byte[] { 1 }, 1, 2);
          hrq.put(new ConflatableObject("key1", "val2", id2, false, "testing"));
          }catch(Exception e) {
            encounteredException=true;
          }
        }
      });
      t1.start();
      Thread.sleep(4000);
      assertTrue(t1.isAlive());
      Conflatable conf = (Conflatable)hrq.take();
      assertNotNull(conf);
      Thread.sleep(2000);
      assertTrue(!t1.isAlive());      

    }
    catch (Exception e) {
      e.printStackTrace();
      fail("Test failed because of exception " + e);
    }
  }

  /**
   * Test Scenario : BlockingQueue capacity is 1. The first put should be
   * successful. The second put should block till a peek/remove happens.
   * 
   */
  public void testBlockingPutAndPeekRemove()
  {
    try {
      HARegionQueueAttributes hrqa = new HARegionQueueAttributes();
      hrqa.setBlockingQueueCapacity(1);
      final HARegionQueue hrq = this.createHARegionQueue(
          "testBlockingPutAndPeekRemove", hrqa);
      hrq.setPrimary(true);//fix for 40314 - capacity constraint is checked for primary only.
      EventID id1 = new EventID(new byte[] { 1 }, 1, 1);
      hrq.put(new ConflatableObject("key1", "val1", id1, false, "testing"));
      Thread t1 = new Thread(new Runnable() {
        public void run()
        {
          try {
            EventID id2 = new EventID(new byte[] { 1 }, 1, 2);
            hrq
                .put(new ConflatableObject("key1", "val2", id2, false,
                    "testing"));
          }
          catch (Exception e) {
            encounteredException = true;
          }
        }
      });
      t1.start();
      Thread.sleep(4000);
      assertTrue("put-thread expected to blocked, but was not ", t1.isAlive());
      Conflatable conf = (Conflatable)hrq.peek();
      assertNotNull(conf);
      hrq.remove();
      Thread.sleep(2000);
      assertFalse("Put-thread blocked unexpectedly", t1.isAlive());
      assertFalse("Exception occured in put-thread", encounteredException);

    }
    catch (Exception e) {
      e.printStackTrace();
      fail("Test failed because of exception " + e);
    }
  }

  /**
   * Test Scenario :Blocking Queue capacity is 1. The first put should be
   * successful.The second put should block till the first put expires.
   * 
   */
  //fix for 40314 - capacity constraint is checked for primary only and
  //expiry is not applicable on primary so marking this test as invalid.
  public void _testBlockingPutAndExpiry()
  {
    try {
      HARegionQueueAttributes hrqa = new HARegionQueueAttributes();
      hrqa.setBlockingQueueCapacity(1);
      hrqa.setExpiryTime(4);
      final HARegionQueue hrq = this.createHARegionQueue(
          "testBlockingPutAndExpiry", hrqa);
      
      EventID id1 = new EventID(new byte[] { 1 }, 1, 1);
      hrq.put(new ConflatableObject("key1", "val1", id1, false, "testing"));
      Thread t1 = new Thread(new Runnable() {
        public void run()
        {
          try {
            EventID id2 = new EventID(new byte[] { 1 }, 1, 2);
            hrq
                .put(new ConflatableObject("key1", "val2", id2, false,
                    "testing"));
          }
          catch (Exception e) {
            encounteredException = true;
          }
        }
      });
      t1.start();
      Thread.sleep(2000);
      assertTrue("put-thread expected to blocked, but was not ", t1.isAlive());
      Thread.sleep(2500);
      assertFalse("Put-thread blocked unexpectedly", t1.isAlive());
      assertFalse("Exception occured in put-thread", encounteredException);

    }
    catch (Exception e) {
      e.printStackTrace();
      fail("Test failed because of exception " + e);
    }
  }
}