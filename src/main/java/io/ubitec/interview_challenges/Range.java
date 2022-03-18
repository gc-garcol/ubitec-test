package io.ubitec.interview_challenges;

import io.ubitec.interview_challenges.util.ReflectUtil;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Range<T extends Comparable> {

  private final T lowerbound;

  private final T upperbound;

  private final RangeType rangeType;

  enum RangeType {
    OPEN(RangeContains.OPEN, RangeString.OPEN, RangeParser.OPEN),
    CLOSE(RangeContains.CLOSE, RangeString.CLOSE, RangeParser.CLOSE),
    OPEN_CLOSED(RangeContains.OPEN_CLOSED, RangeString.OPEN_CLOSED, RangeParser.OPEN_CLOSED),
    CLOSED_OPEN(RangeContains.CLOSED_OPEN, RangeString.CLOSED_OPEN, RangeParser.CLOSED_OPEN),
    LESS_THAN(RangeContains.LESS_THAN, RangeString.LESS_THAN, RangeParser.LESS_THAN),
    AT_LEAST(RangeContains.AT_LEAST, RangeString.AT_LEAST, RangeParser.AT_LEAST),
    AT_MOST(RangeContains.AT_MOST, RangeString.AT_MOST, RangeParser.AT_MOST),
    GREATER_THAN(RangeContains.GREATER_THAN, RangeString.GREATER_THAN, RangeParser.GREATER_THAN),
    ALL(RangeContains.ALL, RangeString.ALL, RangeParser.ALL);

    RangeContains rangeContains;
    RangeString rangeString;
    RangeParser rangeParser;

    RangeType(RangeContains rangeContains, RangeString rangeString, RangeParser parser) {
      this.rangeContains = rangeContains;
      this.rangeString = rangeString;
      this.rangeParser = parser;
    }

  }

  enum RangeParser {
    OPEN((parser) ->
            Range.open(
                    ReflectUtil.parseComparableInstance(parser.lowerbound, parser.type),
                    ReflectUtil.parseComparableInstance(parser.upperbound, parser.type))),
    CLOSE((parser) ->
            Range.closed(
                    ReflectUtil.parseComparableInstance(parser.lowerbound, parser.type),
                    ReflectUtil.parseComparableInstance(parser.upperbound, parser.type))),
    OPEN_CLOSED((parser) ->
            Range.openClosed(
                    ReflectUtil.parseComparableInstance(parser.lowerbound, parser.type),
                    ReflectUtil.parseComparableInstance(parser.upperbound, parser.type))),
    CLOSED_OPEN((parser) ->
            Range.closedOpen(
                    ReflectUtil.parseComparableInstance(parser.lowerbound, parser.type),
                    ReflectUtil.parseComparableInstance(parser.upperbound, parser.type))),
    LESS_THAN((parser) ->
            Range.lessThan(ReflectUtil.parseComparableInstance(parser.upperbound, parser.type))),
    AT_LEAST((parser) ->
            Range.atLeast(
                    ReflectUtil.parseComparableInstance(parser.lowerbound, parser.type))),
    AT_MOST((parser) ->
            Range.atMost(ReflectUtil.parseComparableInstance(parser.upperbound, parser.type))),
    GREATER_THAN((parser) ->
            Range.greaterThan(ReflectUtil.parseComparableInstance(parser.lowerbound, parser.type))),
    ALL((parser) -> Range.all());

    Function<Parser, Range> parser;

    RangeParser(Function<Parser, Range> parser) {
      this.parser = parser;
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
    OPEN(
            range -> String.format("(%s, %s)", range.lowerbound, range.upperbound),
            Pattern.compile("^\\(\\w+, \\w+\\)$")),
    CLOSE(
            range -> String.format("[%s, %s]", range.lowerbound, range.upperbound),
            Pattern.compile("^\\[\\w+, \\w+\\]$")),
    OPEN_CLOSED(
            range -> String.format("(%s, %s]", range.lowerbound, range.upperbound),
            Pattern.compile("^\\(\\w+, \\w+\\]$")),
    CLOSED_OPEN(
            range -> String.format("[%s, %s)", range.lowerbound, range.upperbound),
            Pattern.compile("^\\[\\w+, \\w+\\)$")),
    LESS_THAN(
            range -> String.format("[%s, %s)", RangeString.INFINITY, range.upperbound),
            Pattern.compile("^\\[Infinitive, \\w+\\)$", Pattern.CASE_INSENSITIVE)),
    AT_LEAST(
            range -> String.format("[%s, %s]", range.lowerbound, RangeString.INFINITY),
            Pattern.compile("^\\[\\w+, Infinitive\\]$", Pattern.CASE_INSENSITIVE)),
    AT_MOST(
            range -> String.format("[%s, %s]", RangeString.INFINITY, range.upperbound),
            Pattern.compile("^\\[Infinitive, \\w+\\]$", Pattern.CASE_INSENSITIVE)),
    GREATER_THAN(
            range -> String.format("(%s, %s]", range.lowerbound, RangeString.INFINITY),
            Pattern.compile("^\\(\\w+, Infinitive\\]$", Pattern.CASE_INSENSITIVE)),
    ALL(
            range -> String.format("[%s, %s]", RangeString.INFINITY, RangeString.INFINITY),
            Pattern.compile("^\\[Infinitive, Infinitive\\]$", Pattern.CASE_INSENSITIVE));

    static final String INFINITY = "Infinitive";

    Function<Range, String> tostring;
    Pattern pattern;

    RangeString(Function<Range, String> tostring, Pattern pattern) {
      this.tostring = tostring;
      this.pattern = pattern;
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

  public static <T extends Comparable> Range<T> parse(String format, Class<T> type) {
    RangeType rangeType = ParserHelper.findRangeType(format);
    String[] parsedBounds = format.substring(1, format.length() - 1).split(", ");
    Parser<T> parser = new Parser<>(parsedBounds[0], parsedBounds[1], type, rangeType);
    return rangeType.rangeParser.parser.apply(parser);
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

class Parser<T> {
  String lowerbound;
  String upperbound;
  Class<T> type;
  Range.RangeType rangeType;

  public Parser(String lowerbound, String upperbound, Class<T> type, Range.RangeType rangeType) {
    this.lowerbound = lowerbound;
    this.upperbound = upperbound;
    this.type = type;
    this.rangeType = rangeType;
  }
}

class ParserHelper {

  public static Range.RangeType findRangeType(String format) {
    if (Range.RangeString.ALL.pattern.matcher(format).find()) {
      return Range.RangeType.ALL;
    }

    if (Range.RangeString.GREATER_THAN.pattern.matcher(format).find()) {
      return Range.RangeType.GREATER_THAN;
    }

    if (Range.RangeString.AT_MOST.pattern.matcher(format).find()) {
      return Range.RangeType.AT_MOST;
    }

    if (Range.RangeString.AT_LEAST.pattern.matcher(format).find()) {
      return Range.RangeType.AT_LEAST;
    }

    if (Range.RangeString.LESS_THAN.pattern.matcher(format).find()) {
      return Range.RangeType.LESS_THAN;
    }

    if (Range.RangeString.CLOSED_OPEN.pattern.matcher(format).find()) {
      return Range.RangeType.CLOSED_OPEN;
    }

    if (Range.RangeString.OPEN_CLOSED.pattern.matcher(format).find()) {
      return Range.RangeType.OPEN_CLOSED;
    }

    if (Range.RangeString.CLOSE.pattern.matcher(format).find()) {
      return Range.RangeType.CLOSE;
    }

    if (Range.RangeString.OPEN.pattern.matcher(format).find()) {
      return Range.RangeType.OPEN;
    }

    throw new RuntimeException("Format not found");

  }
}
