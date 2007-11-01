// $Id: PoolCostInfo.java,v 1.6 2005-06-01 06:03:53 patrick Exp $

package diskCacheV111.pools ;
import java.util.* ;

public class PoolCostInfo implements java.io.Serializable {

    static final long serialVersionUID = 5181562551679185500L;
    
    private PoolQueueInfo _store = null  , _restore = null  , 
                          _mover = null  , _p2p     = null ,
                          _p2pClient = null ;
    private Map           _extendedMoverHash = null ; 
    private String        _defaultQueueName  = null ;                  
    private PoolSpaceInfo _space  = null ;
    private String        _poolName ;
    public PoolCostInfo( String poolName ){ _poolName = poolName ; }
    public String getPoolName(){ return _poolName ; }
    public class NamedPoolQueueInfo extends PoolQueueInfo {
        private String _name = null ;
        private NamedPoolQueueInfo( String name , int active , int maxActive , int queued ){
           super( active , maxActive , queued ) ;
           _name = name ;
         
        }
        public String getName(){ return _name ; }
        public String toString(){
           return _name+"={"+super.toString()+"}" ;
        }
    }
    public class PoolQueueInfo implements java.io.Serializable {
        private int _active = 0 , _maxActive = 0 , _queued = 0 ;
        private PoolQueueInfo( int active , int maxActive , int queued ){
           _active    = active ;
           _maxActive = maxActive ;
           _queued    = queued ;

        }
        public String toString(){ return "a="+_active+";m="+_maxActive+";q="+_queued ; }
        public int getActive(){ return _active ; }
        public int getMaxActive(){ return _maxActive ; }
        public int getQueued(){ return _queued ; }
        public void modifyQueue( int diff ){ 
  
           int total = _active + _queued + diff ; 

           _active = Math.min( total , _maxActive ) ;
           
           _queued = Math.max( 0 , total - _maxActive ) ;
        }
    }
    public PoolQueueInfo getStoreQueue(){ return _store ; }
    public PoolQueueInfo getRestoreQueue(){ return _restore ; }
    public PoolQueueInfo getMoverQueue(){ return _mover ; }
    public PoolQueueInfo getP2pQueue(){ return _p2p ; }
    public PoolQueueInfo getP2pClientQueue(){ return _p2pClient ; }
    public PoolSpaceInfo getSpaceInfo(){ return _space ; }
    
    public class PoolSpaceInfo implements java.io.Serializable {
        private long _total = 0 , _free = 0 , _precious = 0 , _removable = 0 , _lru = 0 ;
        private long _gap   = 0 ;
        private double _breakEven = 0;
        private PoolSpaceInfo( long total , long free , long precious , long removable ){
           _total     = total ; 
           _free      = free ;
           _precious  = precious ;
           _removable = removable ;
           
        }
        private PoolSpaceInfo( long total , long free , long precious , long removable , long lru ){
           _total     = total ; 
           _free      = free ;
           _precious  = precious ;
           _removable = removable ;
           _lru       = lru ;
        }
        public void setParameter( double breakEven , long gap ){
           _breakEven = breakEven ;
           _gap       = gap ;
        }
        public String toString(){
           return "t="+_total+
                  ";f="+_free+
                  ";p="+_precious+
                  ";r="+_removable+
                  ";lru="+_lru+
                  ";{g="+_gap+";b="+_breakEven+"}" ;
        }
        public long getFreeSpace(){ return _free ; }
        public long getTotalSpace(){ return _total ; }
        public long getPreciousSpace(){ return _precious ; }
        public long getRemovableSpace(){ return _removable ; }
        public long getGap(){ return _gap ; }
        public double getBreakEven(){ return _breakEven ; }
        public long getLRUSeconds(){ return _lru ; }
    }

    //
    /// the setters
    //
    public void setSpaceUsage( long total , long free , long precious , long removable ){
        _space = new PoolSpaceInfo( total , free , precious , removable ) ;
    }
    public void setSpaceUsage( long total , long free , long precious , long removable , long lru ){
        _space = new PoolSpaceInfo( total , free , precious , removable , lru ) ;
    }
    public void setQueueSizes( int moverActive   , int moverMaxActive   , int moverQueued ,
                               int restoreActive , int restoreMaxActive , int restoreQueued ,
                               int storeActive   , int storeMaxActive   , int storeQueued        ){

       _mover   = new PoolQueueInfo( moverActive , moverMaxActive , moverQueued ) ;
       _restore = new PoolQueueInfo( restoreActive , restoreMaxActive , restoreQueued ) ;
       _store   = new PoolQueueInfo( storeActive , storeMaxActive , storeQueued ) ;
      
    }
    public void addExtendedMoverQueueSizes( String name , int moverActive   , int moverMaxActive   , int moverQueued ){
       if( _extendedMoverHash == null ){
           _defaultQueueName  = name ;
           _extendedMoverHash = new HashMap() ;
       }
       _extendedMoverHash.put( name, new NamedPoolQueueInfo( name, moverActive, moverMaxActive, moverQueued ));
    }
    public Map getExtendedMoverHash(){ return _extendedMoverHash ; }
    public String getDefaultQueueName(){ return _defaultQueueName ; }
    public void setP2pServerQueueSizes( int p2pActive     , int p2pMaxActive     , int p2pQueued  ){
       _p2p = new PoolQueueInfo( p2pActive   , p2pMaxActive   , p2pQueued ) ;
    }
    public void setP2pClientQueueSizes( int p2pClientActive     , int p2pClientMaxActive     , int p2pClientQueued ){
       _p2pClient = new PoolQueueInfo( p2pClientActive   , p2pClientMaxActive   , p2pClientQueued ) ;
    }
    public String toString() {
       StringBuffer sb = new StringBuffer() ;
       
       sb.append(_poolName).append("={R={").append(_restore.toString()).
          append("};S={").append(_store.toString()).
          append("};M={").append(_mover.toString()) ;
       if( _p2p != null )sb.append("};PS={").append(_p2p.toString()) ;
       if( _p2pClient != null )sb.append("};PC={").append(_p2pClient.toString()) ;
       sb.append("};SP={").append(_space.toString()).append("};");
       if( _extendedMoverHash != null ){
           sb.append("XM={");
           for( Iterator it = _extendedMoverHash.values().iterator() ; it.hasNext() ; ){
               sb.append( it.next().toString() ).append(";");
           }
           sb.append("};");
       }
       sb.append("}");
          
       return sb.toString();
    }



}
