package com.caju_authorization.mapper;

import com.caju_authorization.constant.MccEnum;

import java.util.HashMap;
import java.util.Map;

public class MerchantMapper {
    private static final Map<String, MccEnum> merchantMap = new HashMap<>();

    static {
        merchantMap.put("UBER TRIP                   SAO PAULO BR", MccEnum.DEFAULT);
        merchantMap.put("UBER EATS                   SAO PAULO BR", MccEnum.MEAL);
        merchantMap.put("PAG*JoseDaSilva          RIO DE JANEI BR", MccEnum.FOOD);
        merchantMap.put("PICPAY*BILHETEUNICO           GOIANIA BR", MccEnum.DEFAULT);
    }

    public static MccEnum getBalanceType(String merchant) {
        return merchantMap.getOrDefault(merchant, MccEnum.DEFAULT);
    }
}
