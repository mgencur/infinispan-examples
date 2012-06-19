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
import java.util.Set;
import org.infinispan.atomic.Delta;
import org.infinispan.atomic.DeltaAware;
import org.infinispan.atomic.NullDelta;
import org.infinispan.marshall.AbstractExternalizer;

public class HugeObject implements DeltaAware, Cloneable {

    String frame, fork, rearShock, crank, bottomBracket, shifters, cogSet, chain, frontDerailleur,
        rearDerailleur, rims, hubs, tires, pedals, brake, handlebar, stem; 
    
    private HugeObjectDelta delta = null;
    
    @Override
    public void commit() {
        delta = null;
    }

    @Override
    public Delta delta() {
        Delta toReturn = delta == null ? NullDelta.INSTANCE : delta;
        delta = null; // reset
        return toReturn;
    }
    
    HugeObjectDelta getDelta() {
        if (delta == null) delta = new HugeObjectDelta();
        return delta;
    }
    
    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        getDelta().registerComponentChange(getCurrentComponentName(), frame);
        this.frame = frame;
    }
    
    public static String getCurrentComponentName()
    {
      String toReturn = new Object(){}.getClass().getEnclosingMethod().getName().substring(3);
      System.out.println("Component: " + toReturn);  
      return toReturn;
    }


    public String getFork() {
        return fork;
    }

    public void setFork(String fork) {
        getDelta().registerComponentChange(getCurrentComponentName(), fork);
        this.fork = fork;
    }

    public String getRearShock() {
        return rearShock;
    }

    public void setRearShock(String rearShock) {
        getDelta().registerComponentChange(getCurrentComponentName(), rearShock);
        this.rearShock = rearShock;
    }

    public String getCrank() {
        return crank;
    }

    public void setCrank(String crank) {
        getDelta().registerComponentChange(getCurrentComponentName(), crank);
        this.crank = crank;
    }

    public String getBottomBracket() {
        return bottomBracket;
    }

    public void setBottomBracket(String bottomBracket) {
        getDelta().registerComponentChange(getCurrentComponentName(), bottomBracket);
        this.bottomBracket = bottomBracket;
    }

    public String getShifters() {
        return shifters;
    }

    public void setShifters(String shifters) {
        getDelta().registerComponentChange(getCurrentComponentName(), shifters);
        this.shifters = shifters;
    }

    public String getCogSet() {
        return cogSet;
    }

    public void setCogSet(String cogSet) {
        getDelta().registerComponentChange(getCurrentComponentName(), cogSet);
        this.cogSet = cogSet;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        getDelta().registerComponentChange(getCurrentComponentName(), chain);
        this.chain = chain;
    }

    public String getFrontDerailleur() {
        return frontDerailleur;
    }

    public void setFrontDerailleur(String frontDerailleur) {
        getDelta().registerComponentChange(getCurrentComponentName(), frontDerailleur);
        this.frontDerailleur = frontDerailleur;
    }

    public String getRearDerailleur() {
        return rearDerailleur;
    }

    public void setRearDerailleur(String rearDerailleur) {
        getDelta().registerComponentChange(getCurrentComponentName(), rearDerailleur);
        this.rearDerailleur = rearDerailleur;
    }

    public String getRims() {
        return rims;
    }

    public void setRims(String rims) {
        getDelta().registerComponentChange(getCurrentComponentName(), rims);
        this.rims = rims;
    }

    public String getHubs() {
        return hubs;
    }

    public void setHubs(String hubs) {
        getDelta().registerComponentChange(getCurrentComponentName(), hubs);
        this.hubs = hubs;
    }

    public String getTires() {
        return tires;
    }

    public void setTires(String tires) {
        getDelta().registerComponentChange(getCurrentComponentName(), tires);
        this.tires = tires;
    }

    public String getPedals() {
        return pedals;
    }

    public void setPedals(String pedals) {
        getDelta().registerComponentChange(getCurrentComponentName(), pedals);
        this.pedals = pedals;
    }

    public String getBrake() {
        return brake;
    }

    public void setBrake(String brake) {
        getDelta().registerComponentChange(getCurrentComponentName(), brake);
        this.brake = brake;
    }

    public String getHandlebar() {
        return handlebar;
    }

    public void setHandlebar(String handlebar) {
        getDelta().registerComponentChange(getCurrentComponentName(), handlebar);
        this.handlebar = handlebar;
    }

    public String getStem() {
        return stem;
    }

    public void setStem(String stem) {
        getDelta().registerComponentChange(getCurrentComponentName(), stem);
        this.stem = stem;
    }

    public static class Externalizer extends AbstractExternalizer<HugeObject> {

        @Override
        public Set<Class<? extends HugeObject>> getTypeClasses() {
            return null;
        }

        @Override
        public void writeObject(ObjectOutput output, HugeObject object) throws IOException {
            
        }

        @Override
        public HugeObject readObject(ObjectInput input) throws IOException, ClassNotFoundException {
            return null;
        }
        
    }
}
