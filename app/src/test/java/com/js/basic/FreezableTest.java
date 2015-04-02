package com.js.basic;

import com.js.testUtils.MyTestCase;

import java.util.ArrayList;
import java.util.Random;

import static com.js.basic.Tools.*;

public class FreezableTest extends MyTestCase {

  // Set this true for diagnostic information printed during tests
  private static final boolean VERBOSE = false;

  public void testBuiltObjectInitiallyMutable() {
    Alpha a = alpha();
    assertTrue(a.isMutable());
  }

  public void testMutableObjectIsActuallyMutable() {
    Alpha a = alpha();
    int value = a.value();
    a.setValue(value + 5);
    assertEquals(value + 5, a.value());
  }

  public void testFreezingReportsFrozen() {
    Alpha a = alpha();
    assertTrue(a.isMutable());
    assertFalse(a.isFrozen());
    a.freeze();
    assertFalse(a.isMutable());
    assertTrue(a.isFrozen());
  }

  public void testFrozenObjectNotMutable() {
    Alpha a = alpha();
    int value = a.value();
    a.freeze();
    try {
      a.setValue(value + 5);
      fail();
    } catch (Freezable.IllegalMutationException e) {
      doNothing();
    }
  }

  public void testCopyOfFrozenReturnsSameObject() {
    Freezable a = alpha();
    a.freeze();
    Freezable b = a.getFrozenCopy();
    assertTrue(a == b);
  }

  public void testMutableCopyOfMutableReturnsDifferentObject() {
    Freezable a = alpha();
    Freezable b = a.getMutableCopy();
    assertFalse(a == b);
  }

  public void testMutableCopyOfFrozenReturnsDifferentObject() {
    Freezable a = alpha();
    a.freeze();
    Freezable b = a.getMutableCopy();
    assertFalse(a == b);
  }

  public void testMutatingCopyOfFrozenAllowed() {
    Alpha a = alpha();
    a.freeze();
    Alpha b = mutableCopyOf(a);
    b.setValue(b.value() + 100);
  }

  public void testMutatingCopyLeavesOriginalUnchanged() {
    Alpha a = alpha();
    int aValue = a.value();
    Alpha b = mutableCopyOf(a);

    b.setValue(b.value() + 100);
    assertEquals(aValue, a.value());
  }

  public synchronized int adjustLiveThreadCount(int amount) {
    mLiveThreadCount += amount;
    return mLiveThreadCount;
  }

  private enum EvalType {SYNCHRONIZED, NONSYNCHRONIZED}

  private class AlphaEvaluator implements Runnable {

    public AlphaEvaluator(Alpha alpha, EvalType type) {
      mAlpha = alpha;
      mType = type;
    }

    @Override
    public void run() {
      adjustLiveThreadCount(1);
      while (System.currentTimeMillis() < mStartTime)
        Thread.yield();

      sleep();
      switch (mType) {
        case SYNCHRONIZED:
          mAlpha.getFirstName(this);
          break;
        case NONSYNCHRONIZED:
          mAlpha.getSecondName(this);
          break;
      }

      adjustLiveThreadCount(-1);
    }

    private Alpha mAlpha;
    private EvalType mType;
  }

  /**
   * Tests our implementation of a thread-safe, lazy-initialized field of an immutable object.
   * <p/>
   * There are two variants:
   * <p/>
   * The first guarantees that a particular field is evaluated only once, at the cost of
   * synchronization during that evaluation.
   * <p/>
   * The second permits a field to be evaluated more than once (and earlier values to be
   * overwritten), but avoids the synchronization.  Note that if the evaluation is not
   * deterministic, this may violate the object's immutability (since different threads
   * may end up with different values for this field!).  For this reason, maybe the second
   * method variant should be avoided.
   * <p/>
   * The field holding the lazy-evaluated value must be marked volatile.  This prevents the
   * possibility that its value is cached by one thread, and hence fails to see its value being
   * changed by some other thread.  It also ensures that reads and writes to the value are
   * atomic (this is always the case for reference types, or primitive types other than
   * long or double).
   */

  private void performLazyEvaluationOfType(EvalType type) {
    // Construct a single frozen object
    Alpha a = alpha();
    a.setValue(76);
    a.freeze();

    // Construct a number of threads that will reference this frozen object,
    // and each attempt to evaluate one of its (lazy-evaluated) fields
    mStartTime = System.currentTimeMillis() + 200;
    ArrayList<Thread> threads = new ArrayList();
    for (int i = 0; i < 50; i++)
      threads.add(new Thread(new AlphaEvaluator(a, type)));

    // Start all the threads
    for (Thread thread : threads)
      thread.start();

    // Sleep for a small amount to ensure at least some threads have had a chance to start
    sleepFor(100);

    // Wait until all threads are done
    while (adjustLiveThreadCount(0) != 0)
      Thread.yield();
  }

  public void testLazyEvaluationWithSynchronization() {
    performLazyEvaluationOfType(EvalType.SYNCHRONIZED);
    // Verify that we didn't perform more than a single evaluation
    assertEquals(1, mLazyEvaluations);
  }

  public void testLazyEvaluationWithoutSynchronization() {
    performLazyEvaluationOfType(EvalType.NONSYNCHRONIZED);
    // Verify that we performed more than a single evaluation
    // (note: this may fail, since we're assuming threads will cause a race condition which
    // is not actually guaranteed, we've just encouraged them to do so by adding sleep calls)
    assertTrue(mLazyEvaluations > 1);
  }

  /**
   * Construct another Alpha object, with value one more than the previously constructed one
   */
  private Alpha alpha() {
    Alpha alpha = new Alpha(mNextAlphaValue);
    mNextAlphaValue++;
    return alpha;
  }

  private void sleep() {
    int delay;
    synchronized (this) {
      delay = sRandom.nextInt(10);
    }
    sleepFor(delay);
  }

  private void incrementEvaluationsCount(Object caller) {
    synchronized (this) {
      mLazyEvaluations++;
      if (VERBOSE)
        pr("Object " + nameOf(caller, false) + " evaluated, count " + mLazyEvaluations);
    }
  }

  private static Random sRandom = new Random();

  private int mNextAlphaValue = 50;
  private int mLazyEvaluations;
  private int mLiveThreadCount;
  private long mStartTime;

  /**
   * Class that implements the Freezable interface
   */
  private class Alpha extends Freezable.Mutable {

    public Alpha(int value) {
      mValue = value;
    }

    public int value() {
      return mValue;
    }

    public void setValue(int value) {
      mutate();
      mValue = value;
    }

    @Override
    public Freezable getMutableCopy() {
      return new Alpha(this.value());
    }

    /**
     * Lazy-initialized property.
     * <p/>
     * Designed to be only valid for frozen instances; uses synchronization to ensure
     * it is only evaluated once
     */
    public String getFirstName(Object caller) {
      assertFrozen();
      if (mFirstName == null) {
        synchronized (this) {
          if (mFirstName == null) {
            //
            sleep();
            sleep();
            sleep();
            //
            mFirstName = "FirstName(" + value() + ")";
            incrementEvaluationsCount(caller);
            //
            sleep();
            //
          }
        }
      }
      return mFirstName;
    }

    /**
     * Lazy-initialized property.
     * <p/>
     * Designed to be only valid for frozen instances; does not use synchronization, which means
     * it may be evaluated by different threads simultaneously
     */
    public String getSecondName(Object caller) {
      assertFrozen();
      if (mSecondName == null) {
        //
        sleep();
        sleep();
        sleep();
        //
        mSecondName = "SecondName(" + value() + ")";
        incrementEvaluationsCount(caller);
        //
        sleep();
        //
      }
      return mSecondName;
    }

    private int mValue;
    private volatile String mFirstName;
    private volatile String mSecondName;
  }

}
