package com.caju_authorization.mapper;

import com.caju_authorization.constant.MccEnum;
import com.caju_authorization.model.Account;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BalanceMapper {
    private static final Map<MccEnum, Function<Account, BigDecimal>> balanceGetters = new HashMap<>();
    private static final Map<MccEnum, BiConsumer<Account, BigDecimal>> balanceSetters = new HashMap<>();

    static {
        // Mapping getters to each MCC
        balanceGetters.put(MccEnum.FOOD, Account::getFoodBalance);
        balanceGetters.put(MccEnum.MEAL, Account::getMealBalance);
        balanceGetters.put(MccEnum.DEFAULT, Account::getCashBalance);

        // Mapping setters to each MCC
        balanceSetters.put(MccEnum.FOOD, Account::setFoodBalance);
        balanceSetters.put(MccEnum.MEAL, Account::setMealBalance);
        balanceSetters.put(MccEnum.DEFAULT, Account::setCashBalance);
    }

    public static Function<Account, BigDecimal> getGetter(MccEnum mcc) {
        return balanceGetters.get(mcc);
    }

    public static BiConsumer<Account, BigDecimal> getSetter(MccEnum mcc) {
        return balanceSetters.get(mcc);
    }
}
