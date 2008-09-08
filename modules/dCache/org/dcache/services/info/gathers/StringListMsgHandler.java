package org.dcache.services.info.gathers;

import org.dcache.services.info.base.StateComposite;
import org.dcache.services.info.base.StatePath;
import org.dcache.services.info.base.StateUpdate;

/**
 * A generic routine for processing an incoming CellMessage.  The message is expected to
 * be an array of Strings that should be inserted into dCache state at a specific
 * point in the state. 
 * @author Paul Millar <paul.millar@desy.de>
 */
public class StringListMsgHandler extends CellMessageHandlerSkel {
	
	final private StatePath _path;
	
	/**
	 * Create a new generic String-list message handler.
	 * @param path a String representation of the path under which incoming elements
	 * will be added
	 */
	public StringListMsgHandler( String path) {
		_path = new StatePath(path);
	}
	
	public void process( Object msgPayload, long metricLifetime) {		
		
		Object array[] = (Object []) msgPayload;
		
		if( array.length == 0)
			return;

		StateUpdate update = new StateUpdate();

		for( int i = 0; i < array.length; i++) {
			String listItem = (String) array[i];			
			update.appendUpdate( _path.newChild(listItem), new StateComposite(metricLifetime));
		}

		applyUpdates( update);
	}

}
