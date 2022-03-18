package io.ubitec.interview_challenges;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Range<T extends Comparable> {

  private final T lowerbound;

  private final T upperbound;

  private final RangeType rangeType;

  enum RangeType {
    OPEN(RangeContains.OPEN, RangeString.OPEN),
    CLOSE(RangeContains.CLOSE, RangeString.CLOSE),
    OPEN_CLOSED(RangeContains.OPEN_CLOSED, RangeString.OPEN_CLOSED),
    CLOSED_OPEN(RangeContains.CLOSED_OPEN, RangeString.CLOSED_OPEN),
    LESS_THAN(RangeContains.LESS_THAN, RangeString.LESS_THAN),
    AT_LEAST(RangeContains.AT_LEAST, RangeString.AT_LEAST),
    AT_MOST(RangeContains.AT_MOST, RangeString.AT_MOST),
    GREATER_THAN(RangeContains.GREATER_THAN, RangeString.GREATER_THAN),
    ALL(RangeContains.ALL, RangeString.ALL);

    RangeContains rangeContains;
    RangeString rangeString;

    RangeType(RangeContains rangeContains, RangeString rangeString) {
      this.rangeContains = rangeContains;
      this.rangeString = rangeString;
    }

  }

  enum RangeContains {
    OPEN((range, comparedValue) -> range.lowerbound.compareTo(comparedValue) < 0 && range.upperbound.compareTo(comparedValue) > 0),
    CLOSE((range, comparedValue) -> range.lowerbound.compareTo(comparedValue) <= 0 && range.upperbound.compareTo(comparedValue) >= 0),
    OPEN_CLOSED((range, comparedValue) -> range.lowerbound.compareTo(comparedValue) < 0 && range.upperbound.compareTo(comparedValue) >= 0),
    CLOSED_OPEN((range, comparedValue) -> range.lowerbound.compareTo(comparedValue) <= 0 && range.upperbound.compareTo(comparedValue) > 0),
    LESS_THAN((range, comparedValue) -> range.upperbound.compareTo(comparedValue) > 0),
    AT_LEAST((range, comparedValue) -> range.lowerbound.compareTo(comparedValue) <= 0),
    AT_MOST((range, comparedValue) -> range.upperbound.compareTo(comparedValue) >= 0),
    GREATER_THAN((range, comparedValue) -> range.lowerbound.compareTo(comparedValue) < 0),
    ALL((range, comparedValue) -> true);

    BiFunction<Range, Comparable, Boolean> contains;

    RangeContains(BiFunction<Range, Comparable, Boolean> contains) {
      this.contains = contains;
    }

    <T extends Comparable> boolean contains(Range range, T comparedValue) {
      return contains.apply(range, comparedValue);
    }
  }

  enum RangeString {
    OPEN(range -> String.format("(%s, %s)", range.lowerbound, range.upperbound)),
    CLOSE(range -> String.format("[%s, %s]", range.lowerbound, range.upperbound)),
    OPEN_CLOSED(range -> String.format("[%s, %s)", range.lowerbound, range.upperbound)),
    CLOSED_OPEN(range -> String.format("[%s, %s)", range.lowerbound, range.upperbound)),
    LESS_THAN(range -> String.format("[%s, %s)", RangeString.INFINITY, range.upperbound)),
    AT_LEAST(range -> String.format("[%s, %s]", range.lowerbound, RangeString.INFINITY)),
    AT_MOST(range -> String.format("[%s, %s]", RangeString.INFINITY, range.upperbound)),
    GREATER_THAN(range -> String.format("(%s, %s]", range.lowerbound, RangeString.INFINITY)),
    ALL(range -> String.format("[%s, %s]", RangeString.INFINITY, RangeString.INFINITY));

    static final String INFINITY = "Infinitive";

    Function<Range, String> tostring;

    RangeString(Function<Range, String> tostring) {
      this.tostring = tostring;
    }

    String toString(Range range) {
      return tostring.apply(range);
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

  private Range() {
    this.rangeType = RangeType.ALL;
    this.lowerbound = null;
    this.upperbound = null;
  }

  public static <T extends Comparable> Range of(T lowerbound, T upperbound) {
    return new Range(lowerbound, upperbound, RangeType.CLOSE);
  }

  public static <T extends Comparable> Range open(T lowerbound, T upperbound) {
    return new Range(lowerbound, upperbound, RangeType.OPEN);
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

  public static <T extends Comparable> Range lessThan(T value) {
    return new Range(value, value, RangeType.LESS_THAN);
  }

  public static <T extends Comparable> Range atLeast(T value) {
    return new Range(value, value, RangeType.AT_LEAST);
  }

  public static <T extends Comparable> Range atMost(T value) {
    return new Range(value, value, RangeType.AT_MOST);
  }

  public static <T extends Comparable> Range greaterThan(T value) {
    return new Range(value, value, RangeType.GREATER_THAN);
  }

  public static <T extends Comparable> Range all() {
    return new Range();
  }

  /**
   * Returns {@code true} on if the given {@code value} is contained in this
   * {@code Range}.
   */
  public boolean contains(T value) {
    return rangeType.rangeContains.contains(this, value);
  }

  @Override
  public String toString() {
    return rangeType.rangeString.toString(this);
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
