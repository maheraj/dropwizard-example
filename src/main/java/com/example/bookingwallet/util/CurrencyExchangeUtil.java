package com.example.bookingwallet.util;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class CurrencyExchangeUtil {
    public static double convert(String sourceCurrencyCode, double sourceAmount, String targetCurrencyCode) {
        if (sourceCurrencyCode.equalsIgnoreCase(targetCurrencyCode)) {
            return Money.of(CurrencyUnit.of(targetCurrencyCode), sourceAmount, RoundingMode.UNNECESSARY).getAmount().doubleValue();
        }

        CurrencyUnit sourceCurrency = CurrencyUnit.of(sourceCurrencyCode);
        Money sourceMoney = Money.of(sourceCurrency, sourceAmount, RoundingMode.UNNECESSARY);

        CurrencyUnit euroCurrency = CurrencyUnit.EUR;
        Money euroMoney = sourceMoney.convertedTo(euroCurrency, EuroCurrencyRate.valueOf(sourceCurrency.getCode()).rate, RoundingMode.HALF_EVEN);

        CurrencyUnit targetCurrency = CurrencyUnit.of(targetCurrencyCode);
        if (euroCurrency.getCode().equals(targetCurrency.getCode())) {
            return euroMoney.getAmount().doubleValue();
        }

        Money targetMoney = euroMoney.convertedTo(targetCurrency, new BigDecimal(1 / EuroCurrencyRate.valueOf(targetCurrencyCode).rate.doubleValue(), new MathContext(5)), RoundingMode.HALF_EVEN);
        return targetMoney.getAmount().doubleValue();
}
}
