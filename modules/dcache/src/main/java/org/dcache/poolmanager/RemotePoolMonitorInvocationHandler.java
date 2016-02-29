package org.dcache.poolmanager;

import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.aop.target.dynamic.Refreshable;
import org.springframework.remoting.RemoteInvocationFailureException;
import org.springframework.remoting.RemoteProxyFailureException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

import diskCacheV111.util.CacheException;
import diskCacheV111.util.TimeoutCacheException;
import diskCacheV111.vehicles.PoolManagerGetPoolMonitor;

import org.dcache.cells.CellStub;

import static java.util.concurrent.TimeUnit.*;

/**
 * InvocationHandler to access a cached PoolMonitor that is periodically imported from pool manager.
 */
public class RemotePoolMonitorInvocationHandler implements InvocationHandler, Refreshable
{
    /* Fake key to register the pool monitor in a cache. */
    private static final String KEY = "key";

    private long _refreshDelay = SECONDS.toMillis(20);
    private volatile long _lastRefreshTime;
    private CellStub _poolManagerStub;

    private LoadingCache<String,PoolMonitor> _poolMonitor;

    public void setPoolManagerStub(CellStub stub)
    {
        _poolManagerStub = stub;
    }

    public long getRefreshDelay()
    {
        return _refreshDelay;
    }

    /**
     * Sets the delay in milliseconds between two consecutive refreshes
     * of the cached PoolMonitor.
     */
    public void setRefreshDelay(long refreshDelay)
    {
        _refreshDelay = refreshDelay;
    }

    private PoolMonitor getPoolMonitor() throws InterruptedException, CacheException
    {
        try {
            return _poolMonitor.get(KEY);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            Throwables.propagateIfInstanceOf(cause, InterruptedException.class);
            Throwables.propagateIfInstanceOf(cause, CacheException.class);
            throw Throwables.propagate(cause);
        }
    }

    public CacheStats getStats()
    {
        return _poolMonitor.stats();
    }

    @Override
    public void refresh()
    {
        try {
            _poolMonitor.put(KEY, _poolManagerStub.sendAndWait(new PoolManagerGetPoolMonitor()).getPoolMonitor());
            _lastRefreshTime = System.currentTimeMillis();
        } catch (CacheException e) {
            _poolMonitor.refresh(KEY);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public long getRefreshCount()
    {
        return _poolMonitor.stats().loadCount();
    }

    @Override
    public long getLastRefreshTime()
    {
        return _lastRefreshTime;
    }

    public void init()
    {
        _poolMonitor =
                CacheBuilder.newBuilder()
                        .maximumSize(1)
                        .expireAfterWrite(3, MINUTES)
                        .refreshAfterWrite(_refreshDelay, MILLISECONDS)
                        .build(new PoolMonitorCacheLoader());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
    {
        try {
            if (method.getDeclaringClass().isAssignableFrom(Refreshable.class)) {
                return method.invoke(this, args);
            }
            return method.invoke(getPoolMonitor(), args);
        } catch (IllegalAccessException | InterruptedException e) {
            throw new RemoteProxyFailureException("Failed to fetch pool monitor: " + e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new RemoteInvocationFailureException("Failed to fetch pool monitor: " + e.getCause().getMessage(), e.getCause());
        } catch (TimeoutCacheException e) {
            throw new RemoteProxyFailureException("Failed to fetch pool monitor: " + e.getMessage(), e);
        } catch (CacheException e) {
            throw new RemoteInvocationFailureException("Failed to fetch pool monitor: " + e.getMessage(), e);
        }
    }

    private class PoolMonitorCacheLoader extends CacheLoader<String, PoolMonitor>
    {
        @Override
        public PoolMonitor load(String ignored)
                throws InterruptedException, CacheException
        {
            PoolManagerGetPoolMonitor reply =
                    _poolManagerStub.sendAndWait(new PoolManagerGetPoolMonitor());
            _lastRefreshTime = System.currentTimeMillis();
            return reply.getPoolMonitor();
        }

        @Override
        public ListenableFuture<PoolMonitor> reload(String ignored, PoolMonitor oldValue)
        {
            return Futures.transform(
                    _poolManagerStub.send(new PoolManagerGetPoolMonitor()),
                    (PoolManagerGetPoolMonitor reply) -> {
                        _lastRefreshTime = System.currentTimeMillis();
                        return reply.getPoolMonitor();
                    }
            );
        }
    }
}
