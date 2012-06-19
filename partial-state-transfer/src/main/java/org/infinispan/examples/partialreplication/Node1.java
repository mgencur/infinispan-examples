/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other
 * contributors as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.infinispan.examples.partialreplication;

import javax.transaction.TransactionManager;

import org.infinispan.Cache;
import org.infinispan.CacheImpl;

public class Node1 extends AbstractNode {

   public static void main(String[] args) throws Exception {
      new Node1().run();
   }

   public void run() {
      Cache<, String> cache = getCacheManager().getCache("Demo");
      TransactionManager tm = ((CacheImpl) cache).getAdvancedCache().getTransactionManager();
      System.out.println("Transaction: " + tm.getClass().getName());
      
      waitForClusterToForm();
      
      // Put some information in the cache that we can display on the other node
      try {
          tm.begin();
              System.out.println(tm.getTransaction().toString());
              cache.put("key", "value");
          tm.commit();
      } catch (Exception e) {
          try {
              if (tm != null) {
                  tm.rollback();
              }
          } catch (Exception ex) {}
      }
   }
   
   @Override
   protected int getNodeId() {
      return 1;
   }

}
