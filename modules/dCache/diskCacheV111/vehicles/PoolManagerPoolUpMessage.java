// $Id: PoolManagerPoolUpMessage.java,v 1.7 2004-11-05 12:07:19 tigran Exp $

package diskCacheV111.vehicles;

import java.util.*; 
import diskCacheV111.pools.PoolCostInfo ;

public class PoolManagerPoolUpMessage extends PoolManagerMessage {
   
    private String    _poolName                = null;
    private long      _serialId                = 0L ;
    private PoolCostInfo _poolCostInfo         = null ;
    private Map       _tagMap                  = null ;

    private static final long serialVersionUID = -8421133630068493665L;
    
    public PoolManagerPoolUpMessage(String poolName, long serialId ){
        _poolName = poolName;
	_serialId = serialId ;
	setReplyRequired(false);
    }
    public PoolManagerPoolUpMessage( String poolName , long serialId , PoolCostInfo costInfo ){
        this( poolName , serialId ) ;
        _poolCostInfo = costInfo ;
    }    
    public PoolCostInfo getPoolCostInfo(){ return _poolCostInfo ; }
    public String getPoolName(){
        return _poolName;
    }
    public long getSerialId(){ return _serialId ; }
    public void setTagMap( Map map ){ _tagMap = map ; }
    public Map  getTagMap(){ return _tagMap ; }
}
