package org.infinispan.examples.partialreplication;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Set;

import org.infinispan.atomic.Delta;
import org.infinispan.atomic.DeltaAware;
import org.infinispan.marshall.AdvancedExternalizer;
import org.infinispan.util.Util;

/**
 * 
 * Bicycle consists of many components. In order to avoid replicating information about
 * all components every time the bicycle object is changed, we implement DeltaAware interface
 * which helps us replicate only changes that were made to some specific components.
 * 
 * @author Martin Gencur
 */
public class Bicycle implements DeltaAware, Cloneable {

    //bicycle attributes
    String frame, fork, rearShock, crank, bottomBracket, shifters, cogSet, chain, 
           frontDerailleur, rearDerailleur, rims, hubs, tires, pedals, brake, handlebar, stem;

    private BicycleDelta delta;

    /**
     * When a transaction is committed, the delta is nulled and changes to that object are discarded.
     */
    @Override
    public void commit() {
        delta = null;
    }

    /**
     * Return data that should be replicated. Infinispan won't replicate the whole object but rather only the delta. 
     */
    @Override
    public Delta delta() {
        Delta toReturn = getDelta();
        delta = null; // reset
        return toReturn;
    }

    BicycleDelta getDelta() {
        if (delta == null) delta = new BicycleDelta();
        return delta;
    }

    public void initializeWithDefaults() {
        setFrame("All-New Mongoose Freedrive DH Aluminum 210mm travel");
        setFork("RockShox Boxxer RC w/200mm Travel, Maxle Lite DH 20mm thru-axle, Rebound & Low Speed Compression Adjust");
        setRearShock("Fox Van R w/210mm Travel, Rebound Adjust");
        setCrank("Truvativ Hussefelt 1.0 w/ E13 LG1 chainguide, 36t");
        setBottomBracket("Truvativ Howitzer Team");
        setShifters("SRAM X7 Trigger");
        setCogSet("SRAM PG-950, 11-26t, 9-speed");
        setChain("KMC X9 w/ Ti-Gold finish, 9-speed");
        setFrontDerailleur("SRAM X9");
        setRearDerailleur("SRAM X9");
        setRims("Alex FR30 32 Hole");
        setHubs("KK Alloy Disc for 20mm thru-axle 32 Hole (F), 150 x 12mm thru-axle 32 Hole (R)");
        setTires("Kenda Excavator DH, 26 x 2.5");
        setPedals("Funn Platform");
        setBrake("Avid Code R Hydraulic Disc, 200mm/180mm rotor (F/R)");
        setHandlebar("Funn Fatboy, 31.8, 15mm rise");
        setStem("Funn RSX MKII Direct Mount, 31.8");
    }

    /* ========= getters/setters =============================== */

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        getDelta().registerComponentChange("frame", frame);
        this.frame = frame;
    }

    public String getFork() {
        return fork;
    }

    public void setFork(String fork) {
        getDelta().registerComponentChange("fork", fork);
        this.fork = fork;
    }

    public String getRearShock() {
        return rearShock;
    }

    public void setRearShock(String rearShock) {
        getDelta().registerComponentChange("rearShock", rearShock);
        this.rearShock = rearShock;
    }

    public String getCrank() {
        return crank;
    }

    public void setCrank(String crank) {
        getDelta().registerComponentChange("crank", crank);
        this.crank = crank;
    }

    public String getBottomBracket() {
        return bottomBracket;
    }

    public void setBottomBracket(String bottomBracket) {
        getDelta().registerComponentChange("bottomBracket", bottomBracket);
        this.bottomBracket = bottomBracket;
    }

    public String getShifters() {
        return shifters;
    }

    public void setShifters(String shifters) {
        getDelta().registerComponentChange("shifters", shifters);
        this.shifters = shifters;
    }

    public String getCogSet() {
        return cogSet;
    }

    public void setCogSet(String cogSet) {
        getDelta().registerComponentChange("cogSet", cogSet);
        this.cogSet = cogSet;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        getDelta().registerComponentChange("chain", chain);
        this.chain = chain;
    }

    public String getFrontDerailleur() {
        return frontDerailleur;
    }

    public void setFrontDerailleur(String frontDerailleur) {
        getDelta().registerComponentChange("frontDerailleur", frontDerailleur);
        this.frontDerailleur = frontDerailleur;
    }

    public String getRearDerailleur() {
        return rearDerailleur;
    }

    public void setRearDerailleur(String rearDerailleur) {
        getDelta().registerComponentChange("rearDerailleur", rearDerailleur);
        this.rearDerailleur = rearDerailleur;
    }

    public String getRims() {
        return rims;
    }

    public void setRims(String rims) {
        getDelta().registerComponentChange("rims", rims);
        this.rims = rims;
    }

    public String getHubs() {
        return hubs;
    }

    public void setHubs(String hubs) {
        getDelta().registerComponentChange("hubs", hubs);
        this.hubs = hubs;
    }

    public String getTires() {
        return tires;
    }

    public void setTires(String tires) {
        getDelta().registerComponentChange("tires", tires);
        this.tires = tires;
    }

    public String getPedals() {
        return pedals;
    }

    public void setPedals(String pedals) {
        getDelta().registerComponentChange("pedals", pedals);
        this.pedals = pedals;
    }

    public String getBrake() {
        return brake;
    }

    public void setBrake(String brake) {
        getDelta().registerComponentChange("brake", brake);
        this.brake = brake;
    }

    public String getHandlebar() {
        return handlebar;
    }

    public void setHandlebar(String handlebar) {
        getDelta().registerComponentChange("handlebar", handlebar);
        this.handlebar = handlebar;
    }

    public String getStem() {
        return stem;
    }

    public void setStem(String stem) {
        getDelta().registerComponentChange("stem", stem);
        this.stem = stem;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("*** Bike components ***\n");
        sb.append("=======================");
        sb.append("\nframe: " + frame);
        sb.append("\nfork: " + fork);
        sb.append("\nrearShock: " + rearShock);
        sb.append("\ncrank: " + crank);
        sb.append("\nbottomBracket: " + bottomBracket);
        sb.append("\nshifters: " + shifters);
        sb.append("\ncogSet: " + cogSet);
        sb.append("\nchain: " + chain);
        sb.append("\nfrontDerailleur: " + frontDerailleur);
        sb.append("\nrearDerailleur: " + rearDerailleur);
        sb.append("\nrims: " + rims);
        sb.append("\nhubs: " + hubs);
        sb.append("\ntires: " + tires);
        sb.append("\npedals: " + pedals);
        sb.append("\nbrake: " + brake);
        sb.append("\nhandlebar: " + handlebar);
        sb.append("\nstem: " + stem);
        return sb.toString();
    }

    /**
     * An externalizer that is used to marshall Bicycle objects
     */
    public static class Externalizer implements AdvancedExternalizer<Bicycle> {
        @Override
        public Set<Class<? extends Bicycle>> getTypeClasses() {
            return Util.<Class<? extends Bicycle>> asSet(Bicycle.class);
        }

        @Override
        public void writeObject(ObjectOutput output, Bicycle object) throws IOException {
            output.writeObject(object);
        }
    
        @Override
        public Bicycle readObject(ObjectInput input) throws IOException, ClassNotFoundException {
            return (Bicycle) input.readObject();
        }

        @Override
        public Integer getId() {
            return 22; //put some random value here to identify the externalizer (must not be in reserved value ranges)
        }
    }
    
}
