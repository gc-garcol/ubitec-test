package io.ubitec.interview_challenges;

import java.util.function.BiFunction;

public class Range<T extends Comparable> {

  private final T lowerbound;

  private final T upperbound;

  private final RangeType rangeType;

  enum RangeType {
    OPEN((range, comparedValue) -> range.lowerbound.compareTo(comparedValue) < 0 && range.upperbound.compareTo(comparedValue) > 0),
    CLOSE((range, comparedValue) -> range.lowerbound.compareTo(comparedValue) <= 0 && range.upperbound.compareTo(comparedValue) >= 0),
    OPEN_CLOSED((range, comparedValue) -> range.lowerbound.compareTo(comparedValue) < 0 && range.upperbound.compareTo(comparedValue) >= 0),
    CLOSED_OPEN((range, comparedValue) -> range.lowerbound.compareTo(comparedValue) <= 0 && range.upperbound.compareTo(comparedValue) > 0);

    BiFunction<Range, Comparable, Boolean> contains;

    RangeType(BiFunction<Range, Comparable, Boolean> contains) {
      this.contains = contains;
    }

    <T extends Comparable> boolean contains(Range range, T comparedValue) {
      return contains.apply(range, comparedValue);
    }
  }

  /**
   * Constructor is private BY DESIGN.
   *
   */
  private Range(T lowerbound, T upperbound, RangeType rangeType) {
    if (lowerbound.compareTo(upperbound) == 1) {
      throw new IllegalArgumentException("It is not allowed to create a Range with lowerbound > upperbound");
    }
    this.lowerbound = lowerbound;
    this.upperbound = upperbound;
    this.rangeType = rangeType;
  }

  public static <T extends Comparable> Range of(T lowerbound, T upperbound) {
    return new Range(lowerbound, upperbound, RangeType.CLOSE);
  }

  public static <T extends Comparable> Range open(T lowerbound, T upperbound) {
    return new Range(lowerbound, upperbound,RangeType.OPEN);
  }

  public static <T extends Comparable> Range closed(T lowerbound, T upperbound) {
    return of(lowerbound, upperbound);
  }

  public static <T extends Comparable> Range openClosed(T lowerbound, T upperbound) {
    return new Range(lowerbound, upperbound, RangeType.OPEN_CLOSED);
  }

  public static <T extends Comparable> Range closedOpen(T lowerbound, T upperbound) {
    return new Range(lowerbound, upperbound, RangeType.CLOSED_OPEN);
  }

  /**
   * Returns {@code true} on if the given {@code value} is contained in this
   * {@code Range}.
   */
  public boolean contains(T value) {
    return rangeType.contains(this, value);
  }

  /**
   * Returns the {@code lowerbound} of this {@code Range}.
   */
  public T lowerbound() {
    return lowerbound;
  }

  /**
   * Returns the {@code upperbound} of this {@code Range}.
   */
  public T upperbound() {
    return upperbound;
  }

}
