package com.danimo.promotion.sale.application.outputadapters.rest;

import java.util.UUID;

public interface ExistItemOutputPort {
    boolean existItem(UUID itemId);
}
