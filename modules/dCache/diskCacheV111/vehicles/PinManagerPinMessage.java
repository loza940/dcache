/*
 * PinManagerPinMessage.java
 *
 * Created on April 28, 2004, 1:51 PM
 */

package diskCacheV111.vehicles;
import diskCacheV111.util.PnfsId;

/**
 *
 * @author  timur
 */
public class PinManagerPinMessage extends PinManagerMessage {
    private long lifetime;
    private String pinId;
    private String readPoolName;
    
    private static final long serialVersionUID = 5292501618388506009L;
    
    /** Creates a new instance of PinManagerPinMessage */
    public PinManagerPinMessage(String pnfsId,long lifetime) {
        super(pnfsId);
        this.lifetime = lifetime;
    }
    
    public PinManagerPinMessage(PnfsId pnfsId,long lifetime) {
        super(pnfsId);
        this.lifetime = lifetime;
    }
    
    /** Getter for property lifetime.
     * @return Value of property lifetime.
     *
     */
    public long getLifetime() {
        return lifetime;
    }    
    
    /** Setter for property lifetime.
     * @param lifetime New value of property lifetime.
     *
     */
    public void setLifetime(long lifetime) {
        this.lifetime = lifetime;
    }
    
    /** Getter for property pinId.
     * @return Value of property pinId.
     *
     */
    public java.lang.String getPinId() {
        return pinId;
    }
    
    /** Setter for property pinId.
     * @param pinId New value of property pinId.
     *
     */
    public void setPinId(java.lang.String pinId) {
        this.pinId = pinId;
    }
    
    /** Getter for property readPoolName.
     * @return Value of property readPoolName.
     *
     */
    public java.lang.String getReadPoolName() {
        return readPoolName;
    }
    
    /** Setter for property readPoolName.
     * @param readPoolName New value of property readPoolName.
     *
     */
    public void setReadPoolName(java.lang.String readPoolName) {
        this.readPoolName = readPoolName;
    }
    
    public String toString() {
        return "PinManagerPinMessage["+getPnfsId()+
                (pinId==null?"":(","+pinId))+
                "]";
    }
    
}
