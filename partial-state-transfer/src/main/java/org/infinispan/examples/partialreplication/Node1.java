/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012 Red Hat Inc. and/or its affiliates and other
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

import java.io.Console;

import javax.transaction.TransactionManager;

import org.infinispan.Cache;
import org.infinispan.CacheImpl;

/**
 * 
 * Node1.
 * 
 * @author Martin Gencur
 */
public class Node1 extends AbstractNode {

    public static final String BIKE1 = "bike1";
    private static final String initialPrompt = "Choose action:\n" + "============= \n"
            + "u   -  update some of bike components\n" + "p   -  print current bike components\n" + "q   -  quit\n";

    public static void main(String[] args) throws Exception {
        new Node1().run();
    }

    public void run() {
        Cache<String, Bicycle> cache = getCacheManager().getCache("Demo");
        TransactionManager tm = ((CacheImpl) cache).getAdvancedCache().getTransactionManager();

        waitForClusterToForm();

        // put some information in the cache that we can display on the other node
        Bicycle bike = new Bicycle();
        bike.initializeWithDefaults();
        try {
            tm.begin();
            cache.put(BIKE1, bike);
            tm.commit();
        } catch (Exception e) {
            try {
                if (tm != null) {
                    tm.rollback();
                }
            } catch (Exception ex) {
            }
        }

        Console con = System.console();
        con.printf(initialPrompt);
        
        while (true) {

            String action = con.readLine(">");

            if ("p".equals(action)) {

                Bicycle ob = cache.get(BIKE1);
                System.out.println(ob);

            } else if ("u".equals(action)) {

                try {
                    tm.begin();
                    
                    //retrieve the bicycle from the cache
                    Bicycle toChange = cache.get(BIKE1);
                    
                    //apply some changes, only these changes will be replicated
                    System.out.println("Updating components: frame, fork");
                    toChange.setFrame("New Frame");
                    toChange.setFork("New Fork");
                    
                    //store the bicycle back to the cache
                    cache.put(BIKE1, toChange);
                    
                    tm.commit();
                } catch (Exception e) {
                    try {
                        if (tm != null) {
                            tm.rollback();
                        }
                    } catch (Exception ex) {
                    }
                }

            } else if ("q".equals(action)) {
                System.exit(0);
            }
        }
    }

    @Override
    protected int getNodeId() {
        return 1;
    }
}
