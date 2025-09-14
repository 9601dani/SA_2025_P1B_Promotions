package com.danimo.promotion.sale.infrastructure.outputadapters.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemRestApiOutputAdapterTest {

    private static final UUID ITEM_ID = UUID.randomUUID();

    private RestClient dishRestClient;
    private RestClient roomRestClient;
    private ItemRestApiOutputAdapter adapter;

    @BeforeEach
    void setUp() {
        dishRestClient = mock(RestClient.class, RETURNS_DEEP_STUBS);
        roomRestClient = mock(RestClient.class, RETURNS_DEEP_STUBS);
        adapter = new ItemRestApiOutputAdapter(dishRestClient, roomRestClient);
    }

    @Test
    void existItem_itemExistsInDish_returnsTrue() {
        // Arrange
        when(dishRestClient.head().uri(ITEM_ID.toString()).retrieve().toBodilessEntity()).thenReturn(null);

        // Act
        boolean result = adapter.existItem(ITEM_ID);

        // Assert
        assertTrue(result);
        verify(dishRestClient.head().uri(ITEM_ID.toString()).retrieve()).toBodilessEntity();
        verifyNoInteractions(roomRestClient);
    }

    @Test
    void existItem_itemExistsInRoom_returnsTrue() {
        // Arrange
        // dishRestClient throws 404
        RestClientResponseException notFound = new RestClientResponseException(
                "Not Found", 404, "Not Found", null, null, null);
        when(dishRestClient.head().uri(ITEM_ID.toString()).retrieve().toBodilessEntity())
                .thenThrow(notFound);

        when(roomRestClient.head().uri(ITEM_ID.toString()).retrieve().toBodilessEntity()).thenReturn(null);

        // Act
        boolean result = adapter.existItem(ITEM_ID);

        // Assert
        assertTrue(result);
        verify(dishRestClient.head().uri(ITEM_ID.toString()).retrieve()).toBodilessEntity();
        verify(roomRestClient.head().uri(ITEM_ID.toString()).retrieve()).toBodilessEntity();
    }

    @Test
    void existItem_itemDoesNotExist_returnsFalse() {
        // Arrange
        RestClientResponseException notFound = new RestClientResponseException(
                "Not Found", 404, "Not Found", null, null, null);
        when(dishRestClient.head().uri(ITEM_ID.toString()).retrieve().toBodilessEntity())
                .thenThrow(notFound);
        when(roomRestClient.head().uri(ITEM_ID.toString()).retrieve().toBodilessEntity())
                .thenThrow(notFound);

        // Act
        boolean result = adapter.existItem(ITEM_ID);

        // Assert
        assertFalse(result);
        verify(dishRestClient.head().uri(ITEM_ID.toString()).retrieve()).toBodilessEntity();
        verify(roomRestClient.head().uri(ITEM_ID.toString()).retrieve()).toBodilessEntity();
    }

    @Test
    void existItem_dishThrowsUnexpectedException_printsStackTraceAndChecksRoom() {
        // Arrange
        RuntimeException ex = new RuntimeException("Error");
        when(dishRestClient.head().uri(ITEM_ID.toString()).retrieve().toBodilessEntity())
                .thenThrow(ex);
        when(roomRestClient.head().uri(ITEM_ID.toString()).retrieve().toBodilessEntity()).thenReturn(null);

        // Act
        boolean result = adapter.existItem(ITEM_ID);

        // Assert
        assertTrue(result);
    }

    @Test
    void existItem_bothThrowUnexpectedException_returnsFalse() {
        // Arrange
        RuntimeException ex = new RuntimeException("Error");
        when(dishRestClient.head().uri(ITEM_ID.toString()).retrieve().toBodilessEntity())
                .thenThrow(ex);
        when(roomRestClient.head().uri(ITEM_ID.toString()).retrieve().toBodilessEntity())
                .thenThrow(ex);

        // Act
        boolean result = adapter.existItem(ITEM_ID);

        // Assert
        assertFalse(result);
    }
}
