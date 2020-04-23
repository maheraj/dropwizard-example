package com.example.bookingwallet.util;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class CurrencyExchangeUtil {
    public static double convert(Money sourceMoney, CurrencyUnit targetCurrency) {
        if (sourceMoney.getCurrencyUnit().getCode().equals(targetCurrency.getCode())) {
            return sourceMoney.getAmount().doubleValue();
        }

        CurrencyUnit euroCurrency = CurrencyUnit.EUR;
        Money euroMoney = sourceMoney.convertedTo(euroCurrency, EuroCurrencyRate.valueOf(sourceMoney.getCurrencyUnit().getCode()).rate, RoundingMode.HALF_EVEN);

        if (euroCurrency.getCode().equals(targetCurrency.getCode())) {
            return euroMoney.getAmount().doubleValue();
        }

        Money targetMoney = euroMoney.convertedTo(targetCurrency, new BigDecimal(1 / EuroCurrencyRate.valueOf(targetCurrency.getCode()).rate.doubleValue(), new MathContext(5)), RoundingMode.HALF_EVEN);
        return targetMoney.getAmount().doubleValue();
}
}
