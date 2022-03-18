package io.ubitec.interview_challenges;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.*;

public class RangeTest {

  @Test
  public void should_create_range() {
    Range range = Range.of(5, 50);
    assertThat(range.lowerbound()).isEqualTo(5);
    assertThat(range.upperbound()).isEqualTo(50);
  }

  @Test
  public void should_throw_error__when_create_with_lowerbound_bigger_than_upperbound() {
    try {
      Range.of(500, 1);
      fail("Should not allow creating a invalid Range");
    } catch(IllegalArgumentException e) {
      // pass
    }
  }

  @Test
  public void closed_range_should_contain_both_bounds_and_all_elements_in_between() {
    Range closedRange = Range.of(5, 50);

    assertThat(closedRange.contains(Integer.MIN_VALUE)).isEqualTo( false);
    assertThat(closedRange.contains(4)).isEqualTo( false);

    assertThat(closedRange.contains(5)).isEqualTo( true);

    assertThat(closedRange.contains(42)).isEqualTo( true);

    assertThat(closedRange.contains(50)).isEqualTo( true);

    assertThat(closedRange.contains(10000)).isEqualTo( false);
    assertThat(closedRange.contains(Integer.MAX_VALUE)).isEqualTo( false);
  }

  @Test
  public void range_should_be_state_independent() {
    Range range1 = Range.of(5, 10);
    Range range2 = Range.of(11, 20);

    assertThat(range1.contains(10)).isEqualTo( true);
    assertThat(range2.contains(10)).isEqualTo( false);
  }

  @Test
  public void close_opened_openClosed_closedOpen_should_be_valid() {
    Range open = Range.open(5, 7);
    assertThat(open.contains(5)).isEqualTo(false);

    Range closed = Range.closed(5, 7);
    assertThat(closed.contains(5)).isEqualTo(true);

    Range openClosed = Range.openClosed(5, 7);
    assertThat(openClosed.contains(5)).isEqualTo(false);
    assertThat(openClosed.contains(7)).isEqualTo(true);

    Range closedOpen = Range.closedOpen(5, 7);
    assertThat(closedOpen.contains(5)).isEqualTo(true);
    assertThat(closedOpen.contains(7)).isEqualTo(false);
  }

  @Test
  public void should_create_generic_range() {
    Range<String> text = Range.open("abc", "xyz");

    Range<BigDecimal> decimals = Range.open(BigDecimal.valueOf(1.32432), BigDecimal.valueOf(1.324323423423423423423));

    Range<LocalDate> dates = Range.closed(LocalDate.of(2016, Month.SEPTEMBER, 11), LocalDate.of(2017, Month.JUNE, 30));
  }

  @Test
  public void should_infinitive_be_valid() {
    Range<Integer> lessThanFive = Range.lessThan(5); // [Infinitive, 5)
    assertThat(lessThanFive.contains(5)).isEqualTo(false);
    assertThat(lessThanFive.contains(-9000)).isEqualTo(true);

    Range<Integer> atLeastFive = Range.atLeast(5); // [5, Infinitive]
    assertThat(atLeastFive.contains(5)).isEqualTo(true);
    assertThat(atLeastFive.contains(4)).isEqualTo(false);

    Range<Integer> atMostFive = Range.atMost(5); // [Infinitive, 5]
    assertThat(atMostFive.contains(5)).isEqualTo(true);
    assertThat(atMostFive.contains(-234234)).isEqualTo(true);
    assertThat(atMostFive.contains(6)).isEqualTo(false);


    Range<LocalDate> afterEpoch = Range.greaterThan(LocalDate.of(1900, Month.JANUARY, 1)); // (1900-01-01, Infinitive]
    assertThat(afterEpoch.contains(LocalDate.of(2016, Month.JULY, 28))).isEqualTo(true);
    assertThat(afterEpoch.contains(LocalDate.of(1750, Month.JANUARY, 1))).isEqualTo(false);


    Range<String> all = Range.all(); // [Infinitive, Infinitive]
    assertThat(all.contains("anything")).isEqualTo(true);
    assertThat(all.contains("")).isEqualTo(true);
    assertThat(all.contains(null)).isEqualTo(true);
  }

}
