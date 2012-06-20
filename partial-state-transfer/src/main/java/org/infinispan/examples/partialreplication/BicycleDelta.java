package org.infinispan.examples.partialreplication;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import org.infinispan.atomic.Delta;
import org.infinispan.atomic.DeltaAware;
import org.infinispan.marshall.AdvancedExternalizer;
import org.infinispan.util.Util;

/**
 *
 * BicycleDelta holds only information about changed bicycle components. These changes are 
 * then replicated.
 * 
 * @author Martin Gencur
 */
public class BicycleDelta implements Delta {

    //list of bike components that have changed (<componentName, newComponentDescription>)
    private HashMap<String, String> changeLog = new HashMap<String, String>();
    
    /**
     * This method must be implemented so that Infinispan knows how to merge an existing object
     * with incoming changes. 
     * 
     * @param d Represents an object that is already stored in a cache, 
     *          incoming changes will be applied to this object.
     * 
     */
    @Override
    public DeltaAware merge(DeltaAware d) {
        Bicycle other;
        if (d != null && (d instanceof Bicycle)) {
            other = (Bicycle) d;
        }
        else {
            other = new Bicycle();
        }
        Class<?> hugeClass = other.getClass();
        StringBuilder updatedComponents = new StringBuilder();
        try {
            if (changeLog != null) {
                for (Entry<String, String> o : changeLog.entrySet()) {
                    updatedComponents.append(o.getKey() + ", ");
                    Field x = hugeClass.getDeclaredField(o.getKey());
                    if (!x.isAccessible()) x.setAccessible(true);
                    x.set(other, o.getValue());
                }
                //print only components that were changed = actual payload
                printUpdatedComponents(updatedComponents);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return other;
    }
    
    /**
     * The name of this method is arbitrary. When we apply changes to an object that 
     * is already stored in the cache, those changes are recorded by this method during 
     * an ongoing transaction. This "changelog" will be then replicated to other nodes 
     * in the cluster. 
     * 
     * @param compName
     * @param compDescription
     */
    public void registerComponentChange(String compName, String compDescription) {
        changeLog.put(compName, compDescription);
    }
    
    private void printUpdatedComponents(StringBuilder comp) {
        comp.setLength(comp.length() - 2);
        comp.append("\n>");
        System.out.print("\nUpdated components: " + comp.toString());
    }
    
    /**
     * An externalizer that is used to marshall BicycleDelta objects
     */
    public static class Externalizer implements AdvancedExternalizer<BicycleDelta> {

        @Override
        public Set<Class<? extends BicycleDelta>> getTypeClasses() {
            return Util.<Class<? extends BicycleDelta>>asSet(BicycleDelta.class);
        }

        @Override
        public void writeObject(ObjectOutput output, BicycleDelta object) throws IOException {
            output.writeObject(object.changeLog);
        }

        @Override
        public BicycleDelta readObject(ObjectInput input) throws IOException, ClassNotFoundException {
            BicycleDelta delta = new BicycleDelta();
            delta.changeLog = (HashMap<String, String>) input.readObject();
            return delta;
        }
        
        @Override
        public Integer getId() {
           return 33; //put some random value here to identify the externalizer (must not be in reserved value ranges)
        }
        
    }
}
