package dmg.util ;

/**
  *  
  *
  * @author Patrick Fuhrmann
  * @version 0.1, 15 Feb 1998
  *
  * CommandException is the basic Exception thrown by
  * the CommandInterpreter. 
  */
public class CommandException extends Exception {
   static final long serialVersionUID = -598409398076727725L;
   private int _errorCode = 0 ;
   public CommandException( String str ){ super(str) ; }
   public CommandException( int rc , String str ){ 
       super(str) ;
       _errorCode = rc ;
   }
   public int getErrorCode(){ return _errorCode ; }
   public String getErrorMessage(){ return super.getMessage() ; }
   public String getMessage(){ 
      return "("+_errorCode+") "+super.getMessage() ; 
   }
   
}
