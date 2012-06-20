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
import org.infinispan.Cache;
import static org.infinispan.examples.partialreplication.Node1.BIKE1;

/**
 * 
 * Node0.
 * 
 * @author Martin Gencur
 */
public class Node0 extends AbstractNode {

    private static final String initialPrompt = "Choose action:\n" + "============= \n"
            + "p   -  print current bike components\n" + "q   -  quit\n";

    public static void main(String[] args) throws Exception {
        new Node0().run();
    }

    public void run() {
        Cache<String, Bicycle> cache = getCacheManager().getCache("Demo");

        waitForClusterToForm();

        Console con = System.console();
        con.printf(initialPrompt);
        while (true) {

            String action = con.readLine(">");

            if ("p".equals(action)) {

                System.out.println(cache.get(BIKE1));

            } else if ("q".equals(action)) {
                System.exit(0);
            }
        }
    }

    @Override
    protected int getNodeId() {
        return 0;
    }

}
