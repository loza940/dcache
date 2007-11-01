package diskCacheV111.vehicles.transferManager;
import diskCacheV111.vehicles.Message;
/**
 * @author Patrick F.
 * @author Timur Perelmutov. timur@fnal.gov
 * @version 0.0, 28 Jun 2002
 */

public abstract class TransferManagerMessage extends Message {
    public static final int TOO_MANY_TRANSFERS=1;
    public static final int FILE_NOT_FOUND=2;
    public static final int NO_ACCESS=2;
    public static final int POOL_FAILURE=3;
    
    static final long serialVersionUID = -5532348977012216312L;
    private String user;
    private int    uid = 0;
    private int    gid = 0;
    //in case of store space might be reserved and
    // size of filw might be know
    private String spaceReservationId;
    private Long size;
    //true if transfer is from remote server to dcache
    // false if otherwise
    private boolean store;
    private String pnfsPath;
    private String remoteUrl;
    private boolean spaceReservationStrict;
    private Long credentialId;
    
    public TransferManagerMessage(
            String user,
            int uid,
            int gid,
            String pnfsPath,
            String remoteUrl,
            boolean store,
            Long credentialId) {
        this(user,uid,gid, pnfsPath, remoteUrl, store, credentialId,null,false,null);
    }
    
    public TransferManagerMessage(String user,
            int uid, int gid, String pnfsPath,
            String remoteUrl,
            boolean store,
            Long credentialId,
            String spaceReservationId,boolean spaceReservationStrict,
            Long size
            ) {
        super();
        this.user = user;
        this.uid = uid;
        this.gid = gid;
        this.pnfsPath = pnfsPath;
        this.remoteUrl = remoteUrl;
        this.store = store;
        this.spaceReservationId =  spaceReservationId;
        this.spaceReservationStrict = spaceReservationStrict;
        this.size = size;
        this.credentialId = credentialId;
        
    }
    
    public TransferManagerMessage(TransferManagerMessage original) {
        super();
        setId(original.getId());
        this.user = original.user;
        this.uid = original.uid;
        this.gid = original.gid;
        this.pnfsPath = original.pnfsPath;
        this.remoteUrl = remoteUrl;
        this.store = original.store;
        this.spaceReservationId = original.spaceReservationId;
        this.size = original.size;
        this.credentialId = original.credentialId;
        
        
    }
    
    public TransferManagerMessage(long id, long callerUniqueId) {
        super();
        setId(id);
        
    }
    /** Getter for property user.
     * @return Value of property user.
     */
    public String getUser() {
        return user;
    }
    /** Getter for property uid.
     * @return Value of property uid.
     */
    public int getUid() {
        return uid;
    }
    
    /** Getter for property gid.
     * @return Value of property gid.
     */
    public int getGid() {
        return gid;
    }
    
    /** Getter for property store.
     * @return Value of property store.
     */
    public boolean isStore() {
        return store;
    }
    
    
    /** Getter for property pnfsPath.
     * @return Value of property pnfsPath.
     */
    public java.lang.String getPnfsPath() {
        return pnfsPath;
    }
    
    
    public String getRemoteURL() {
        return remoteUrl;
    }
    
    /**
     * Getter for property spaceReservationId.
     * @return Value of property spaceReservationId.
     */
    public java.lang.String getSpaceReservationId() {
        return spaceReservationId;
    }
    
    /**
     * Setter for property spaceReservationId.
     * @param spaceReservationId New value of property spaceReservationId.
     */
    public void setSpaceReservationId(java.lang.String spaceReservationId) {
        this.spaceReservationId = spaceReservationId;
    }
    
    /**
     * Getter for property size.
     * @return Value of property size.
     */
    public java.lang.Long getSize() {
        return size;
    }
    
    /**
     * Setter for property size.
     * @param size New value of property size.
     */
    public void setSize(java.lang.Long size) {
        this.size = size;
    }
    
    /**
     * Getter for property spaceReservationStrict.
     * @return Value of property spaceReservationStrict.
     */
    public boolean isSpaceReservationStrict() {
        return spaceReservationStrict;
    }
    
    /**
     * Setter for property spaceReservationStrict.
     * @param spaceReservationStrict New value of property spaceReservationStrict.
     */
    public void setSpaceReservationStrict(boolean spaceReservationStrict) {
        this.spaceReservationStrict = spaceReservationStrict;
    }
    
    public Long getCredentialId() {
        return credentialId;
    }
    
    public void setCredentialId(Long credentialId) {
        this.credentialId = credentialId;
    }
    
}



