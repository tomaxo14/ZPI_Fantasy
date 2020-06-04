package com.example.ZPI.Utils;

import java.util.*;

public class MapUtil {
  public static <K,Integer> Map<K, Integer> sortByValue(Map<K, Integer> map) {
    List<Map.Entry<K, Integer>> list = new ArrayList<>(map.entrySet());
    list.sort(Map.Entry.comparingByValue((Comparator<? super Integer>) Comparator.reverseOrder()));

    Map<K, Integer> result = new LinkedHashMap<>();
    for (Map.Entry<K, Integer> entry : list) {
      result.put(entry.getKey(), entry.getValue());
    }

    return result;
  }
}
