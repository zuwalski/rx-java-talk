package app.util;

import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.schedulers.Schedulers;

public class ObservableRestClient {
    private final RestTemplate rest = new RestTemplate();

    public <T> Observable<T> post(String url, Class<T> t) {
        return Observable.fromCallable(() ->
                rest.postForObject("http://localhost:8080/" + url, null, t)
        ).subscribeOn(Schedulers.io());
    }

    public <T> Observable<T> get(String url, Class<T> t) {
        return Observable.fromCallable(() ->
                rest.getForObject("http://localhost:8080/" + url, t)
        ).subscribeOn(Schedulers.io());
    }
}
