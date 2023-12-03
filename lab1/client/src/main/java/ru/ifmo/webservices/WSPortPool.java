package ru.ifmo.webservices;

import ru.ifmo.webservices.generated.MovieService;
import ru.ifmo.webservices.generated.MovieWebService;

import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WSPortPool {
    private static final int MAX_POOL_SIZE = 10;

    private static WSPortPool instance;
    private final URL url;
    private final BlockingQueue<MovieService> pool;

    private WSPortPool(URL url) {
        this.url = url;
        this.pool = new LinkedBlockingQueue<>(MAX_POOL_SIZE);
        for (int i = 0; i < MAX_POOL_SIZE; i++) {
            MovieWebService service = new MovieWebService(url);
            MovieService port = service.getMovieWebServicePort();
            this.pool.add(port);
        }
    }

    public static synchronized WSPortPool getInstance(URL url) {
        if (instance == null) {
            instance = new WSPortPool(url);
        }
        return instance;
    }

    public MovieService getPort() throws InterruptedException {
        return pool.take();
    }

    public void releasePort(MovieService port) {
        pool.offer(port);
    }

    public URL getUrl() {
        return url;
    }
}
