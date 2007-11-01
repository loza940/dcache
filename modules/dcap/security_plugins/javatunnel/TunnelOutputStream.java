/*
 * $Id: TunnelOutputStream.java,v 1.1.42.1 2006-10-11 07:56:26 tigran Exp $
 */

package javatunnel;
 
import java.io.IOException;
import java.io.OutputStream;

class TunnelOutputStream extends OutputStream {

    private static final int ARRAYMAXLEN = 4096;
    
	
	private OutputStream _out = null;
	private Convertable _converter = null;
    private byte[] _buffer = new byte[ARRAYMAXLEN];
    private int _pos = 0;
    
	public TunnelOutputStream(OutputStream out, Convertable converter) {
		_out = out;
        _converter = converter;
	}	
	
	public void write(int b) throws java.io.IOException {
        
        
        _buffer[_pos] = (byte)b;
        ++_pos;
        
        if( ((char)b == '\n') || ( (char)b == '\r' ) || ( _pos >= ARRAYMAXLEN )) {
            _converter.encode( _buffer, _pos, _out);
            _pos = 0;
        }
        
	}
	
	public void close() throws IOException {
		_out.close();
	}	
}
