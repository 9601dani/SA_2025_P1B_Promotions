package com.danimo.promotion.sale.infrastructure.outputadapters.rest;

import com.danimo.promotion.sale.application.outputadapters.rest.ExistItemOutputPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.UUID;
@Component
public class ItemRestApiOutputAdapter implements ExistItemOutputPort {

    private final RestClient dishRestClient;
    private final RestClient roomRestClient;

    public ItemRestApiOutputAdapter(@Qualifier("DishRestApi") RestClient dishRestClient, @Qualifier("RoomRestApi") RestClient roomRestClient) {
        this.dishRestClient = dishRestClient;
        this.roomRestClient = roomRestClient;
    }

    @Override
    public boolean existItem(UUID itemId) {
        try {
            dishRestClient.head()
                    .uri(itemId.toString())
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (RestClientResponseException e) {
            if (!e.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            roomRestClient.head()
                    .uri(itemId.toString())
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (RestClientResponseException e) {
            if (!e.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
