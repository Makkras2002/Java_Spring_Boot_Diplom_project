package com.makkras.shop.repo.projection_interface;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface MoneyByDateStatistics {
    LocalDate getDate();
    BigDecimal getAmount();
}
