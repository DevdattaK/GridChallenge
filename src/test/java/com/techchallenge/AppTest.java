package com.techchallenge;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.exception.InputException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit test for simple App.
 */
public class AppTest {

  private App obj;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() throws Exception {
    obj = App.getInstance();
  }

  @Test
  public void whenGridIsEmpty_ReturnInvalidCombinations() throws Exception {
    int[][] arr = {};
    long result = obj.getTotalCombinations(arr, 2);
    assertEquals(-1, result);
  }

  @Test
  public void whenGivenLengthAsUnityWithEmptyGrid_ReturnInvalidCombinations() throws Exception {
    int[][] arr = {};
    long result = obj.getTotalCombinations(arr, 1);
    assertEquals(-1, result);
  }


  @Test
  public void whenGivenAsymmetricGridWith6Rows1ColumnLength3_ReturnColumnCombinations() throws Exception {
    int[][] arr = {{1}, {502}, {97}, {49}, {986}, {15}};
    long result = obj.getTotalCombinations(arr, 3);
    assertEquals(4, result);
  }

  @Test
  public void whenGivenAsymmetricGridWith1Row6ColumnLength3_ReturnRowCombinations() throws Exception {
    int[][] arr = {{1, 502, 97, 49, 986, 15}};
    long result = obj.getTotalCombinations(arr, 3);
    assertEquals(4, result);
  }

  @Test
  public void whenGivenLengthAsUnityWithValidGrid_ReturnTotalCellCount() throws Exception {
    int[][] arr = {{1, 2, 3, 4}, {5, 6, 7, 8}};
    long result = obj.getTotalCombinations(arr, 1);
    assertEquals(8, result);
  }

  @Test
  public void whenGivenUnityGridWithLengthAsUnity_ReturnUnity() throws Exception {
    int[][] arr = {{1}};
    long result = obj.getTotalCombinations(arr, 1);
    assertEquals(1, result);
  }

  @Test
  public void whenGivenAsymmetricGridWithColSizeLessThenLength_ReturnCombinationsAlongRow() throws Exception {
    int[][] arr = {{1, 2, 3, 4}, {2, 3, 4, 5}};
    long result = obj.getTotalCombinations(arr, 3);
    assertEquals(4, result);
  }


  @Test
  public void whenGivenAsymmetricGridWithColSize5RowSize4Length3_ReturnCombinationAsComputed() throws Exception {
    int[][] arr = {{1, 2, 3, 4, 5}, {2, 3, 4, 5, 6}, {1, 2, 3, 4, 7}, {2, 3, 4, 5, 8}};
    long result = obj.getTotalCombinations(arr, 3);
    assertEquals(34, result);
  }


  @Test
  public void whenGivenAsymmetricGridWithColSize6RowSize4Length3_ReturnCombinationAsComputed() throws Exception {
    int[][] arr = {{1, 2, 3, 4, 5, 6}, {2, 3, 4, 5, 6, 7}, {1, 2, 3, 4, 7, 8}, {2, 3, 4, 5, 8, 9}};
    long result = obj.getTotalCombinations(arr, 3);
    assertEquals(44, result);
  }


  @Test
  public void whenGivenGridWithSizeLessThanLength_ReturnCombinationsAlongRow() throws Exception {
    int[][] arr = {{1, 2}, {2, 3}};
    long result = obj.getTotalCombinations(arr, 3);
    assertEquals(-1, result);
  }

  @Test
  public void whenGivenGridLengthSameAsLength_ValidateCombinationsOfGridComponents() throws Exception {
    int[][] arr = {{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
    long result = obj.getTotalCombinations(arr, 3);
    assertEquals(8, result);
  }

  @Test
  public void whenGivenGridSizeFourLengthIsFour_ValidateCombinationsOfGridComponents() throws Exception {
    int[][] arr = {{1, 2, 3, 4}, {2, 3, 4, 5}, {3, 4, 5, 6}, {6, 7, 8, 9}};
    long result = obj.getTotalCombinations(arr, 4);
    assertEquals(10, result);
  }

  @Test
  public void whenGivenGridSizeIsFourAndLengthIsThree_ValidateCombinationsUsingSubgrids() throws Exception {
    int[][] arr = {{1, 2, 3, 4}, {2, 3, 4, 5}, {3, 4, 5, 6}, {6, 7, 8, 9}};
    long result = obj.getTotalCombinations(arr, 3);
    assertEquals(24, result);
  }

  @Test
  public void whenGivenGridSizeIsSixAndLengthIsFour_ValidateCombinationsUsingSubgrids() throws Exception {
    int[][] arr = {{1, 2, 3, 4, 5, 6}, {2, 3, 4, 5, 6, 7}, {3, 4, 5, 6, 7, 8}, {6, 7, 8, 9, 10, 11}, {12, 13, 14, 15, 16, 17},
        {14, 15, 16, 17, 18, 19}};
    long result = obj.getTotalCombinations(arr, 4);
    assertEquals(54, result);
  }

  @Test
  public void whenGivenGridSizeIsTenAndLengthIsThree_ValidateCombinationsUsingSubgrids() throws Exception {
    int[][] arr = {{1, 2, 3, 4, 5, 6, 2, 3, 4, 5}, {2, 3, 4, 5, 6, 7, 3, 4, 5, 6}, {3, 4, 5, 6, 7, 8, 4, 5, 6, 7}, {6, 7, 8, 9, 10, 11, 7, 8, 9, 10},
        {12, 13, 14, 15, 16, 17, 13, 14, 15, 16},
        {14, 15, 16, 17, 18, 19, 20, 21, 22, 23}, {14, 15, 16, 17, 18, 19, 20, 21, 22, 23}, {14, 15, 16, 17, 18, 19, 20, 21, 22, 23},
        {14, 15, 16, 17, 18, 19, 20, 21, 22, 23},
        {14, 15, 16, 17, 18, 19, 20, 21, 22, 23}};
    long result = obj.getTotalCombinations(arr, 3);
    assertEquals(288, result);
  }


  /**
   * Unit tests for problem#2.
   */

  @Test
  public void whenGivenEmptyGrid_ThrowInputValidationException() throws Exception {
    int[][] arr = {};

    thrown.expect(InputException.class);
    thrown.expectMessage(startsWith("Invalid input"));
    thrown.expectMessage("Grid is empty");

    long result = obj.getProduct(arr, 5);
  }


  @Test
  public void whenGivenAsymmetricGridLengthGreaterThanBothDimensions_ThrowInputValidationException() throws Exception {
    int[][] arr = {{1, 2}, {2, 3}, {3, 4}, {0, 7}};

    thrown.expect(InputException.class);
    thrown.expectMessage(startsWith("Invalid input"));
    thrown.expectMessage("Length is greater than both dimensions");

    long result = obj.getProduct(arr, 5);
  }

  @Test
  public void whenGivenAsymmetricGridWithColumnsLessThanLength_ProvideMaxProductOnColumns() throws Exception {
    int[][] arr = {{1, 2}, {2, 3}, {3, 4}, {0, 7}};
    long result = obj.getProduct(arr, 3);
    assertEquals(84, result);
  }

  @Test
  public void whenGivenAsymmetricGridWithRowsLessThanLength_ProvideMaxProductOnRows() throws Exception {
    int[][] arr = {{1, 2, 3, 4, -1}, {5, 6, 7, 8, 0}};
    long result = obj.getProduct(arr, 3);
    assertEquals(336, result);
  }



  @Test
  public void whenGivenGridWithSizeEqualToLength_TreatWholeGridAsTheOnlySubgrid() throws Exception {
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

    thrown.expect(ArithmeticException.class);
    thrown.expectMessage("overflow");

    long result = obj.getProduct(arr, 4);
  }

  @Test
  public void whenGivenAllMaxIntsToCauseOverflow_ExceptionIsThrown() throws Exception {
    final int maxInt = Integer.MAX_VALUE;
    int[][] arr = {{maxInt, maxInt, maxInt, maxInt}, {maxInt, maxInt, maxInt, maxInt}, {maxInt, maxInt, maxInt, maxInt},
        {maxInt, maxInt, maxInt, maxInt}};

    thrown.expect(ArithmeticException.class);
    thrown.expectMessage("overflow");

    long result = obj.getProduct(arr, 4);
  }

  @Test
  public void whenGivenTestDataProvidedInQuestionWithTenByTenGridLength3_ReturnComputedValue() throws Exception {
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


  @Test
  public void whenGivenTestDataSufficentlyLargeWithTenByTenGridLength3_ReturnComputedValue() throws Exception {
    int[][] arr = {{558, 552, 5522, 5597, 5538, 5515, 0, 5540, 0, 5575},
        {5549, 5549, 5599, 5540, 5517, 5581, 5518, 5557, 5560, 5587},
        {5581, 5549, 5531, 5573, 5555, 5579, 5514, 5529, 5593, 5571},
        {5552, 5570, 5595, 5523, 554, 5560, 5511, 5542, 5569, 5524},
        {5522, 5531, 5516, 5571, 5551, 5567, 5563, 5589, 5541, 5592},
        {5524, 5547, 5532, 5560, 5599, 553, 5545, 552, 5544, 5575},
        {5532, 5598, 5581, 5528, 5564, 5523, 5567, 5510, 5526, 5538},
        {5567, 5526, 5520, 5568, 552, 5562, 5512, 5520, 5595, 5563},
        {5524, 5555, 5558, 555, 5566, 5573, 5599, 5526, 5597, 5517},
        {5521, 5536, 5523, 559, 5575, 550, 5576, 5544, 5520, 5545}};
    long result = obj.getProduct(arr, 3);
    assertEquals(174519402255L, result);
  }

  @Test
  public void whenGivenTestDataWithAsymmetricGridRows4Columns6Length3_ReturnComputedValue() throws Exception {
    int[][] arr = {{8, 2, 22, 97, 38, 15},
        {49, 49, 99, 40, 17, 81},
        {81, 49, 31, 73, 55, 79},
        {52, 70, 95, 23, 4, 60}};
    long result = obj.getProduct(arr, 3);
    assertEquals(470547, result);
  }


  @Test
  public void whenGivenTestDataWithAsymmetricGridRows5Columns3Length2_ReturnComputedValue() throws Exception {
    int[][] arr = {{8, 2, 22},
                  {49, 49, 99},
                  {81, 49, 31},
                  {52, 70, 95},
                  {22, 31, 16}};
    long result = obj.getProduct(arr, 2);
    assertEquals(6650, result);
  }

  @Test
  public void whenGivenTestDataWith1Row6ColumnsLength2_ReturnComputedValue() throws Exception {
    int[][] arr = {{1, 502, 97, 49, 986, 15}};
    long result = obj.getProduct(arr, 2);
    assertEquals(48694, result);
  }

  @Test
  public void whenGivenTestDataWith6Rows1ColumnsLength2_ReturnComputedValue() throws Exception {
    int[][] arr = {{1}, {502}, {97}, {49}, {986}, {15}};
    long result = obj.getProduct(arr, 2);
    assertEquals(48694, result);
  }

}
