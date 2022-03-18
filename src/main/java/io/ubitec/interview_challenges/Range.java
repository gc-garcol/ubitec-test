package io.ubitec.interview_challenges;

public class Range {

  private final int lowerbound;

  private final int upperbound;

  /**
   * Constructor is private BY DESIGN.
   *
   */
  private Range(int lowerbound, int upperbound) {
    if (lowerbound > upperbound) {
      throw new IllegalArgumentException("It is not allowed to create a Range with lowerbound > upperbound");
    }
    this.lowerbound = lowerbound;
    this.upperbound = upperbound;
  }

  /**
   * Creates a new <b>closed</b> {@code Range} that includes both bounds.
   */
  public static Range of(int lowerbound, int upperbound) {
    return new Range(lowerbound, upperbound);
  }

  /**
   * Returns {@code true} on if the given {@code value} is contained in this
   * {@code Range}.
   */
  public boolean contains(int value) {
    return lowerbound <= value && value <= upperbound;
  }

  /**
   * Returns the {@code lowerbound} of this {@code Range}.
   */
  public int lowerbound() {
    return lowerbound;
  }

  /**
   * Returns the {@code upperbound} of this {@code Range}.
   */
  public int upperbound() {
    return upperbound;
  }

}
