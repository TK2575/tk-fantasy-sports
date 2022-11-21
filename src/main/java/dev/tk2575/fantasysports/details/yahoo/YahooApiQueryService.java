package dev.tk2575.fantasysports.details.yahoo;

import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Getter(AccessLevel.PROTECTED)
public abstract class YahooApiQueryService<T extends YahooFantasyObject> {

    private final YahooFantasyApiInteractionManager apiManager;
    private final Gson gson;

    YahooApiQueryService() {
        this.apiManager = YahooFantasyApiInteractionManager.getInstance();
        this.gson = YahooUtils.getGson();
    }

    abstract Class<T> clazz();

    abstract String URL();

    abstract String getQueryCode();

    T getResults() throws YahooFantasyApiInteractionManager.YahooFantasyServiceException, IOException, ExecutionException, InterruptedException {
        String url = apiManager.generateUrl(String.format(URL(), getQueryCode()));
        var json = apiManager.request(url);
        return gson.fromJson(json, clazz());
    }
}
