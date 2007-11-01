//package diskCacheV111.services.authorization.gplazmalite.storageauthzdbService;
package gplazma.gplazmalite.storageauthzdbService;

import java.util.*;

public abstract class StorageAuthorizationBase extends Object
{
    public String Username = null;
    public int UID = -1;
    public int GID = -1;
    public String Home = null;
    public String Root = null;
    public String FsRoot = null;
    public boolean ReadOnly = false;

    public StorageAuthorizationBase(String user, boolean readOnly, int uid, int gid, String home, 
			String root, String fsroot)
    {
	Username = user;
	ReadOnly = readOnly;
	UID = uid;
	GID = gid;
	Home = home;
	Root = root;
	FsRoot = fsroot;
    }

    public boolean isReadOnly() {
	return ReadOnly;
    }

    public String readOnlyStr() {
	if(ReadOnly) {
	    return "read-only";
	} else {
	    return "read-write";
	}
    }

    abstract public boolean isAnonymous();
    abstract public boolean isWeak();
	
	
}
