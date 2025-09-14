package com.danimo.promotion.sale.application.rest;

import java.util.UUID;

public interface ExistItemOutputPort {
    boolean existItem(UUID itemId);
}
