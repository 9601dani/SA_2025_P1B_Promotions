package com.danimo.promotion.sale.infrastructure.outputadapters.rest;


import com.danimo.promotion.sale.application.rest.ExistLocationOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LocationRestApiOutputAdapterTest {

    private RestClient locationRestClient;
    private ExistLocationOutputPort locationAdapter;

    @BeforeEach
    void setUp() {
        locationRestClient = mock(RestClient.class);
        locationAdapter = new LocationRestApiOutputAdapter(locationRestClient);
    }

    @Test
    void testExistLocation_whenExists_returnsTrue() {
        UUID locationId = UUID.randomUUID();

        var requestSpec = mock(RestClient.RequestHeadersUriSpec.class);
        var responseSpec = mock(RestClient.ResponseSpec.class);

        when(locationRestClient.head()).thenReturn(requestSpec);
        when(requestSpec.uri(locationId.toString())).thenReturn(requestSpec);
        when(requestSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(null); // simulamos éxito

        boolean result = locationAdapter.existLocation(locationId);

        assertTrue(result);
    }

    @Test
    void testExistLocation_whenNotFound_returnsFalse() {
        UUID locationId = UUID.randomUUID();

        var requestSpec = mock(RestClient.RequestHeadersUriSpec.class);
        var responseSpec = mock(RestClient.ResponseSpec.class);

        when(locationRestClient.head()).thenReturn(requestSpec);
        when(requestSpec.uri(locationId.toString())).thenReturn(requestSpec);
        when(requestSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenThrow(
                new RestClientResponseException("Not found", 404, "NOT_FOUND", null, null, null)
        );

        boolean result = locationAdapter.existLocation(locationId);

        assertFalse(result);
    }

    @Test
    void testExistLocation_whenOtherError_returnsFalse() {
        UUID locationId = UUID.randomUUID();

        var requestSpec = mock(RestClient.RequestHeadersUriSpec.class);
        var responseSpec = mock(RestClient.ResponseSpec.class);

        when(locationRestClient.head()).thenReturn(requestSpec);
        when(requestSpec.uri(locationId.toString())).thenReturn(requestSpec);
        when(requestSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenThrow(
                new RestClientResponseException("Server error", 500, "INTERNAL_SERVER_ERROR", null, null, null)
        );

        boolean result = locationAdapter.existLocation(locationId);

        assertFalse(result);
    }
}
