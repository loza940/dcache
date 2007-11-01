package diskCacheV111.cells ;

import java.util.* ;
import dmg.util.* ;
import dmg.cells.nucleus.* ;

import diskCacheV111.vehicles.* ;
import diskCacheV111.util.* ;

public class DebugCommander extends CellAdapter {
    private CellNucleus _nucleus ;
    private Args        _args ;
    public DebugCommander( String name , String  args ){
       super( name , args , true ) ;
       _nucleus = getNucleus() ;
       _args    = getArgs() ;
    
    }
    public void messageArrived( CellMessage msg ){
       Object obj = msg.getMessageObject() ;
       say( "DBC : From     : "+msg.getSourcePath() ) ;
       say( "DBC : Class    : "+obj.getClass().getName() ) ;
       say( "DBC : toString : "+obj.toString() ) ;
    
    }
    public String hh_send_fetch = "<hsm> <pool> <pnfsId> [-path=<poolPath>]" ;
    public String ac_send_fetch_$_3( Args args )throws Exception {
        CellPath path  = null ; 
        String hsmName  = args.argv(0);
        String poolName = args.argv(1) ;
        String poolPath = args.getOpt("path") ;
        if( poolPath == null ){
           path = new CellPath( poolName ) ;
        }else{
           path = new CellPath( poolPath ) ;
        }
        String   pnfsid = PnfsId.toCompleteId( args.argv(1) ) ;
        
        
        _nucleus.sendMessage( 
           new CellMessage( 
                path , 
                 new PoolFetchFileMessage( poolName , 
                                           new GenericStorageInfo(hsmName,"any") ,
                                           pnfsid )
                          ) ) ;
        return "Stay tuned" ;
    }
    public String hh_send_getpool = 
       "[-dest=<destCell>] read|write <storageGroup> <pnfsId>" ;
    public String ac_send_getpool_$_3( Args args )throws Exception{
        String dest = args.getOpt("dest") ;
        dest = dest == null ? "PoolManager" : dest ;
        
        String dir = args.argv(0) ;
        
        PoolManagerMessage pm = null ; 
        if( dir.equals("read") ){
           pm = new PoolManagerGetReadPoolMessage( 
                          args.argv(1) , 
                          args.argv(2) ) ;
        }else if( dir.equals("write") ){
           pm = new PoolManagerGetWritePoolMessage( 
                          args.argv(1) , 
                          args.argv(2) ) ;
        }else
           throw new 
           CommandSyntaxException("read or write" ) ;
           
        
        _nucleus.sendMessage( new CellMessage( 
                                new CellPath( dest ) ,
                                pm ) 
                            );
                                
        return "Done" ;
    
    }

}
