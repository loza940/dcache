/*
 * SrmLs.java
 *
 * Created on October 4, 2005, 3:40 PM
 */

package org.dcache.srm.handler;

import org.dcache.srm.v2_2.TReturnStatus;
import org.dcache.srm.v2_2.TStatusCode;
import org.dcache.srm.v2_2.SrmReleaseFilesRequest;
import org.dcache.srm.v2_2.SrmReleaseFilesResponse;
import org.dcache.srm.request.RequestUser;
import org.dcache.srm.request.RequestCredential;
import org.dcache.srm.AbstractStorageElement;
import org.dcache.srm.SRMException;
import org.dcache.srm.scheduler.Scheduler;
import org.dcache.srm.request.Request;
import org.dcache.srm.request.FileRequest;
import org.dcache.srm.request.GetRequest;
import org.dcache.srm.request.sql.GetRequestStorage;
import org.dcache.srm.request.sql.GetFileRequestStorage;
import org.dcache.srm.util.Configuration;
import org.dcache.srm.scheduler.State;
import org.dcache.srm.scheduler.IllegalStateTransition;
import org.dcache.srm.v2_2.ArrayOfTSURLReturnStatus;

/**
 *
 * @author  timur
 */
public class SrmReleaseFiles {
    
    
    private final static String SFN_STRING="?SFN=";
    AbstractStorageElement storage;
    SrmReleaseFilesRequest srmReleaseFilesRequest;
    SrmReleaseFilesResponse response;
    Scheduler getScheduler;
    RequestUser user;
    RequestCredential credential;
    GetRequestStorage getStorage;
    GetFileRequestStorage getFileRequestStorage;
    Configuration configuration;
    private int results_num;
    private int max_results_num;
    int numOfLevels =0;
    /** Creates a new instance of SrmLs */
    public SrmReleaseFiles(RequestUser user,
            RequestCredential credential,
            SrmReleaseFilesRequest srmReleaseFilesRequest,
            AbstractStorageElement storage,
            org.dcache.srm.SRM srm,
            String client_host) {
        this.srmReleaseFilesRequest = srmReleaseFilesRequest;
        this.user = user;
        this.credential = credential;
        this.storage = storage;
        this.getScheduler = srm.getGetRequestScheduler();
        this.configuration = srm.getConfiguration();
    }
    
    private void say(String words_of_wisdom) {
        if(storage!=null) {
            storage.log("SrmReleaseFiles "+words_of_wisdom);
        }
    }
    
    private void esay(String words_of_despare) {
        if(storage!=null) {
            storage.elog("SrmReleaseFiles "+words_of_despare);
        }
    }
    private void esay(Throwable t) {
        if(storage!=null) {
            storage.elog(" SrmReleaseFiles exception : ");
            storage.elog(t);
        }
    }
    boolean longFormat =false;
    String servicePathAndSFNPart = "";
    int port;
    String host;
    public SrmReleaseFilesResponse getResponse() {
        if(response != null ) return response;
        try {
            response = srmReleaseFiles();
        } catch(Exception e) {
            storage.elog(e);
            response = getFailedResponse(e.toString());
        }
        
        return response;
    }
    
    public static final SrmReleaseFilesResponse getFailedResponse(String error) {
        return getFailedResponse(error,null);
    }
    
    public static final SrmReleaseFilesResponse getFailedResponse(String error,TStatusCode statusCode) {
        if(statusCode == null) {
            statusCode =TStatusCode.SRM_FAILURE;
        }
        TReturnStatus status = new TReturnStatus();
        status.setStatusCode(statusCode);
        status.setExplanation(error);
        SrmReleaseFilesResponse srmReleaseFilesResponse = new SrmReleaseFilesResponse();
        srmReleaseFilesResponse.setReturnStatus(status);
        return srmReleaseFilesResponse;
    }
    /**
     * implementation of srm ls
     */
    public SrmReleaseFilesResponse srmReleaseFiles()
    throws SRMException,org.apache.axis.types.URI.MalformedURIException,
            java.sql.SQLException, IllegalStateTransition {
        
        
        say("Entering srmReleaseFiles.");
        String requestToken = srmReleaseFilesRequest.getRequestToken();
        if( requestToken == null ) {
            return getFailedResponse("request contains no request token");
        }
        Long requestId;
        try {
            requestId = new Long( requestToken);
        } catch (NumberFormatException nfe){
            return getFailedResponse(" requestToken \""+
                    requestToken+"\"is not valid",
                    TStatusCode.SRM_INVALID_REQUEST);
        }
        
        Request request = Request.getRequest(requestId);
        if(request == null) {
            return getFailedResponse("request for requestToken \""+
                    requestToken+"\"is not found",
                    TStatusCode.SRM_INVALID_REQUEST);
            
        }
        if ( !(request instanceof GetRequest) ){
            return getFailedResponse("request for requestToken \""+
                    requestToken+"\"is not srmPrepareToGet request",
                    TStatusCode.SRM_INVALID_REQUEST);
            
        }
        GetRequest getRequest = (GetRequest) request;
        org.apache.axis.types.URI [] surls ;
        if(  srmReleaseFilesRequest.getArrayOfSURLs() == null ){
            surls = null;
        }  else {
            surls = srmReleaseFilesRequest.getArrayOfSURLs().getUrlArray();
        }
        String surl_stings[] = null;
        if( surls == null ){
            synchronized(getRequest) {
                State state = getRequest.getState();
                if(!State.isFinalState(state)) {
                    getRequest.setState(State.DONE,"SrmReleaseFiles called");
                }
            }
        } else {
            if(surls.length == 0) {
                return getFailedResponse("0 lenght SiteURLs array");
            }
            surl_stings = new String[surls.length];
            for(int i = 0; i< surls.length; ++i) {
                if(surls[i] == null) {
                    return getFailedResponse("surls["+i+"]=null");
                }
                surl_stings[i] = surls[i].toString();
                FileRequest fileRequest = getRequest.getFileRequestBySurl(surl_stings[i]);
                synchronized(fileRequest) {
                    State state = fileRequest.getState();
                    if(!State.isFinalState(state)) {
                        fileRequest.setState(State.DONE,"SrmReleaseFiles called");
                    }
                }
            }
        }
        
        TReturnStatus status = new TReturnStatus();
        status.setStatusCode(TStatusCode.SRM_SUCCESS);
        SrmReleaseFilesResponse srmReleaseFilesResponse = new SrmReleaseFilesResponse();
        srmReleaseFilesResponse.setReturnStatus(status);
        if( surls != null) {
            srmReleaseFilesResponse.setArrayOfFileStatuses(new ArrayOfTSURLReturnStatus(getRequest.getArrayOfTSURLReturnStatus(surl_stings)));
        }
        // we do this to make the srm update the status of the request if it changed
        request.getTReturnStatus();
        return srmReleaseFilesResponse;
        
    }
    
    
}
