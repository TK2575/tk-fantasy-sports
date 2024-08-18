package dev.tk2575.fantasysports;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Utils {

  public static BigDecimal median(List<BigDecimal> numbers) {
    List<BigDecimal> sortedNumbers = new ArrayList<>(numbers);

    // Sort the copied list
    Collections.sort(sortedNumbers);

    int size = sortedNumbers.size();

    // If the size is odd, return the middle element
    if (size % 2 == 1) {
      return sortedNumbers.get(size / 2);
    }
    // If the size is even, return the average of the two middle elements
    BigDecimal middle1 = sortedNumbers.get(size / 2 - 1);
    BigDecimal middle2 = sortedNumbers.get(size / 2);
    return middle1.add(middle2).divide(new BigDecimal(2), RoundingMode.HALF_UP);
  }
}
