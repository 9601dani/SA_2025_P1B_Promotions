package com.danimo.promotion.sale.infrastructure.outputadapters.rest;

import com.danimo.promotion.sale.application.outputadapters.rest.ExistLocationOutputPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.UUID;
@Component
public class LocationRestApiOutputAdapter implements ExistLocationOutputPort {
    private final RestClient locationRestClient;

    public LocationRestApiOutputAdapter(@Qualifier("LocationRestApi") RestClient locationRestClient) {
        this.locationRestClient = locationRestClient;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existLocation(UUID locationId) {
        try {
            locationRestClient.head()
                    .uri(locationId.toString())
                    .retrieve()
                    .toBodilessEntity();

            return true;
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                return false;
            } else {
                e.printStackTrace();
            }
        }

        return false;
    }
}
