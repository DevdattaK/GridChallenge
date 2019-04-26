package com.techchallenge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.exception.InputException;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

  private App obj;

  @Before
  public void setUp() throws Exception {
    obj = App.getInstance();
  }

  /**
   * -ve test.
   */

  @Test
  public void whenGivenInvalidLength_ThrowException() throws Exception {
    int[][] arr = {{1}, {2}, {3}};
    long result = obj.getTotalCombinations(arr, 2);
    assertEquals(-1, result);
  }

  @Test
  public void whenGivenGridLengthSameAsLength_ReturnProductAsEight() throws Exception {
    int[][] arr = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
    long result = obj.getTotalCombinations(arr, 3);
    assertEquals(8, result);
  }

  @Test
  public void whenGivenGridSizeFourAndLength3_ComputeAndReturnTheProductAsTwentyEight() throws Exception {
    int[][] arr = {{1, 2, 3, 4}, {2, 3, 4, 5}, {3, 4, 5, 6}, {6, 7, 8, 9}};
    long result = obj.getTotalCombinations(arr, 3);
    assertEquals(28, result);
  }

  @Test
  public void whenGivenGridSizeSixAndLength4_ComputeAndReturnTheProductAsFiftyFour() throws Exception {
    int[][] arr = {{1, 2, 3, 4, 5, 6}, {2, 3, 4, 5, 6, 7}, {3, 4, 5, 6, 7, 8}, {6, 7, 8, 9, 10, 11}, {12, 13, 14, 15, 16, 17},
        {14, 15, 16, 17, 18, 19}};
    long result = obj.getTotalCombinations(arr, 4);
    assertEquals(54, result);
  }

  @Test
  public void whenGivenGridSizeTenAndLength3_ComputeAndReturnTheProductAsFourHundred() throws Exception {
    int[][] arr = {{1, 2, 3, 4, 5, 6, 2, 3, 4, 5}, {2, 3, 4, 5, 6, 7, 3, 4, 5, 6}, {3, 4, 5, 6, 7, 8, 4, 5, 6, 7}, {6, 7, 8, 9, 10, 11, 7, 8, 9, 10},
        {12, 13, 14, 15, 16, 17, 13, 14, 15, 16},
        {14, 15, 16, 17, 18, 19, 20, 21, 22, 23}, {14, 15, 16, 17, 18, 19, 20, 21, 22, 23}, {14, 15, 16, 17, 18, 19, 20, 21, 22, 23},
        {14, 15, 16, 17, 18, 19, 20, 21, 22, 23},
        {14, 15, 16, 17, 18, 19, 20, 21, 22, 23}};
    long result = obj.getTotalCombinations(arr, 3);
    assertEquals(400, result);
  }

  @Test
  public void whenGivenGridWithSizeLessThanLength_ThrowInputValidationException() throws Exception {
    int[][] arr = {{1, 2, 0, 3}, {2, 3, 4, 0}, {3, 4, 5, 0}, {0, 7, 8, 9}};
    Exception inputException = null;
    try {
      long result = obj.getProduct(arr, 5);
    } catch (Exception e) {
      inputException = e;
    }

    assertEquals(InputException.class, inputException.getClass());
  }

  @Test
  public void whenGivenAsymmetricGrid_ThrowInputValidationException() throws Exception {
    int[][] arr = {{1, 2, 0}, {2, 3, 4}, {3, 4, 5}, {0, 7, 8}};
    Exception inputException = null;
    try {
      long result = obj.getProduct(arr, 5);
    } catch (Exception e) {
      inputException = e;
    }

    assertEquals(InputException.class, inputException.getClass());
  }

  @Test
  public void whenGivenGridWithSizeEqualToLength_TreatWholeGridAsOnlySubgrid() throws Exception {
    int[][] arr = {{1, 2, 0, 3}, {2, 3, 4, 0}, {3, 4, 5, 0}, {0, 7, 8, 9}};
    long result = obj.getProduct(arr, 4);
    assertEquals(168, result);
  }

  @Test
  public void whenGivenGridWithOneZeroPerGroupingType_ReturnZero() throws Exception {
    int[][] arr = {{1, 2, 0, 3}, {2, 0, 4, 3}, {3, 4, 5, 0}, {0, 7, 8, 9}};
    long result = obj.getProduct(arr, 4);
    assertEquals(0, result);
  }

  @Test
  public void whenGivenGridWithNonZeroValuesPerGroupingType_ReturnComputedValue() throws Exception {
    int[][] arr = {{1, 2, 3, 4}, {5, 6, 7, 8}, {8, 7, 6, 5}, {4, 3, 2, 1}};
    long result = obj.getProduct(arr, 4);
    assertEquals(1680, result);
  }

  @Test
  public void whenGivenGridWithSizeGreaterThanLength_ReturnComputedValue() throws Exception {
    int[][] arr = {{1, 2, 3, 4}, {5, 6, 7, 8}, {8, 7, 6, 5}, {4, 3, 2, 1}};
    long result = obj.getProduct(arr, 3);
    assertEquals(336, result);
  }

  @Test
  public void whenGivenAllMinIntsToCauseOverflow_ExceptionIsThrown() throws Exception {
    final int minInt = Integer.MIN_VALUE;
    int[][] arr = {{minInt, minInt, minInt, minInt}, {minInt, minInt, minInt, minInt}, {minInt, minInt, minInt, minInt},
        {minInt, minInt, minInt, minInt}};
    long result = 1L;
    Exception arithException = null;

    try {
      result = obj.getProduct(arr, 4);
    } catch (ArithmeticException e) {
      arithException = e;
    } finally {
      if (arithException == null) {
        assertTrue(false);
      }
    }

    assertEquals(arithException.getClass(), ArithmeticException.class);
  }

  @Test
  public void whenGivenTestDataProvidedInQuestionWithTenByTenGrid_ReturnComputedValue() throws Exception {
    int[][] arr = {{8, 2, 22, 97, 38, 15, 0, 40, 0, 75},
        {49, 49, 99, 40, 17, 81, 18, 57, 60, 87},
        {81, 49, 31, 73, 55, 79, 14, 29, 93, 71},
        {52, 70, 95, 23, 4, 60, 11, 42, 69, 24},
        {22, 31, 16, 71, 51, 67, 63, 89, 41, 92},
        {24, 47, 32, 60, 99, 3, 45, 2, 44, 75},
        {32, 98, 81, 28, 64, 23, 67, 10, 26, 38},
        {67, 26, 20, 68, 2, 62, 12, 20, 95, 63},
        {24, 55, 58, 5, 66, 73, 99, 26, 97, 17},
        {21, 36, 23, 9, 75, 0, 76, 44, 20, 45}};
    long result = obj.getProduct(arr, 3);
    assertEquals(667755, result);
  }

}
