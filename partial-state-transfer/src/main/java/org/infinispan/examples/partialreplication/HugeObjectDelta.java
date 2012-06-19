/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import org.infinispan.atomic.AtomicHashMap;
import org.infinispan.atomic.Delta;
import org.infinispan.atomic.DeltaAware;
import org.infinispan.marshall.AbstractExternalizer;

public class HugeObjectDelta implements Delta {

    //list of bike components that have changed (<componentName, newComponentDescription>)
    private HashMap<String, String> changeLog;
    
    @Override
    public DeltaAware merge(DeltaAware d) {
        HugeObject other;
        if (d != null && (d instanceof AtomicHashMap))
           other = (HugeObject) d;
        else
           other = new HugeObject();
        Class<?> hugeClass = other.getClass();
        try {
            if (changeLog != null) {
               for (Entry<String, String> o : changeLog.entrySet()) {
                   Field x = hugeClass.getDeclaredField(o.getKey());
                   if (!x.isAccessible()) x.setAccessible(true);
                   x.set(other, o.getValue());
               }
            }  
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return other;
    }
    
    public void registerComponentChange(String compName, String compDescription) {
        changeLog.put(compName, compDescription);
    }
    
    public static class Externalizer extends AbstractExternalizer<HugeObjectDelta> {

        @Override
        public Set<Class<? extends HugeObjectDelta>> getTypeClasses() {
            return null;
        }

        @Override
        public void writeObject(ObjectOutput output, HugeObjectDelta object) throws IOException {
            
        }

        @Override
        public HugeObjectDelta readObject(ObjectInput input) throws IOException, ClassNotFoundException {
            return null;
        }
        
    }
}
