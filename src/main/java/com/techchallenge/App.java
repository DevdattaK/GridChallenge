package com.techchallenge;


import com.exception.InputException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import javafx.util.Pair;

/**
 * #Problem Statement : #1. Count different combinations of 'x' consecutive cells in K x K grid #2. Calculate max product of 'x' consecutive cells in
 * K x K grid
 */
public class App {

  private static volatile App obj;

  /**
   * enum to indicate the direction of adjacency. We don't need to store diagonals' product, as they are not common for any subGrids.
   */
  private enum direction {
    VERTICAL, HORIZONTAL
  }

  private App() {
  }

  /**
   * @return singleton instance of the class.
   */
  public static App getInstance() {
    if (obj == null) {
      obj = new App();
    }

    return obj;
  }

  /**
   * @param grid
   * @param length
   * @return
   */
  public long getTotalCombinations(int[][] grid, int length) {
    /**
     * if grid is not K x K dimension, then return -1.
     * if length is greater than grid length, return -1.
     */
    if(grid.length == 0)
      return -1;

    if (grid.length == grid[0].length) {
      if (grid.length < length) {
        return -1L;
      } else if (grid.length == length) {
        /**
         * There are total (2K + 2) combinations in any given grid of size K x K. K rows + K columns + 2 diagonals.
         */
        return (2 * length) + 2;
      } else {
        /**
         * for every row, there would be (gridSize - length + 1) combination. Same for column.
         * there would be (gridSize - length + 1) combinations for each longest diagonal.
         * On each side of this longest diagonal, there will be smaller diagonals of size >= length && < longestDiagonalSize. Same for other longest diagonal
         */
        long horizontalCombinations = grid.length * (grid.length - length + 1);
        long verticalCombinations = horizontalCombinations;
        long longestDiagonalCombination = (grid.length - length + 1);
        /**
         * arithmatic progression. Grid is 5x5 and length is 3.
         * Longest diagonal size is 5, forming total 3 combinations. On upper side, next smaller diagonal size would be 4, forming 2 combinations.
         * Next diagonal will be of size 3, forming the only combination. i.e. 3 + 2 + 1. Arithmatic progression. Since longest diagonal (from topRight to bottomLeft direction) is only one,
         * we start the progression from n = longestDiagonalCombinations-1 for calculating total diagonal combinations in partition above the diagonal.
         * Some for the other side of this longest diagonal (i.e. portion below diagonal). Repeat this for other diagonal.
         */
        long totalCombinationsOnEitherSidesOfFirstLongestDiagonal = (longestDiagonalCombination - 1) * (longestDiagonalCombination) * 2;

        return (horizontalCombinations + verticalCombinations + (longestDiagonalCombination * 2) + totalCombinationsOnEitherSidesOfFirstLongestDiagonal);
      }
    } else {
      //imbalanced grid.
      return -1L;
    }

  }


  private long populateEntityAndGetMaxProductForEntityList(int row, int col, int length, int[][] grid, Map<direction, Queue<Long>> curMap,
      direction curDirection) {
    long tempProd = 1L;
    Queue<Long> curList = null;
    long maxProductForSubGrid = Long.MIN_VALUE;

    curList = new LinkedList();
    for (int i = row; i < row + length; i++) {
      tempProd = 1L;
      for (int j = col; j < col + length; j++) {
        if (curDirection == direction.HORIZONTAL) {
          //tempProd *= grid[i][j];
          tempProd = Math.multiplyExact(tempProd, grid[i][j]);
        } else {
          //tempProd *= grid[j][i];
          tempProd = Math.multiplyExact(tempProd, grid[j][i]);
        }
      }
      curList.add(tempProd);
      maxProductForSubGrid = tempProd > maxProductForSubGrid ? tempProd : maxProductForSubGrid;
    }
    curMap.put(curDirection, curList);

    return maxProductForSubGrid;
  }

  private long fillMapWithProductsOfSubgridElementsAndGetMaxFromCurSubgrid(int row, int col, int length, int[][] grid,
      Map<Pair<Integer, Integer>, Map<direction, Queue<Long>>> tabularProductMap) {
    Queue<Long> curList = null;
    Map<direction, Queue<Long>> curMap = null;
    long tempProd = 1L;
    long maxProductForSubGrid = Long.MIN_VALUE;
    long maxProductForRows = Long.MIN_VALUE, maxProductForColumns = Long.MIN_VALUE;

    /**
     * populate rows
     */
    curMap = new HashMap<>();
    if (col == 0 && row == 0) {
      maxProductForRows = this.populateEntityAndGetMaxProductForEntityList(row, col, length, grid, curMap, direction.HORIZONTAL);
      tabularProductMap.put(new Pair<>(row, col), curMap);
    } else {
      maxProductForRows = this.populateEntityFromPrebuiltEntitiesAndGetMaxProductForEntity(row, col, length, grid, tabularProductMap,
          direction.HORIZONTAL);
    }

    /**
     * populate columns
     */
    if (col == 0 && row == 0) {
      maxProductForColumns = this.populateEntityAndGetMaxProductForEntityList(col, row, length, grid, curMap, direction.VERTICAL);
    } else {
      maxProductForColumns = this.populateEntityFromPrebuiltEntitiesAndGetMaxProductForEntity(row, col, length, grid, tabularProductMap,
          direction.VERTICAL);
    }

    maxProductForSubGrid = maxProductForRows > maxProductForColumns ? maxProductForRows : maxProductForColumns;

    /**
     * No need to store diagonal values
     * calculate diagonals product and use them to find maxProduct (D1 : top-left to bottom-right AND D2 : bottom-left to top-right).
     */
    tempProd = 1L;
    for (int i = row, j = col; i < row + length && j < col + length; i++, j++) {
      //tempProd *= grid[i][j];
      tempProd = Math.multiplyExact(tempProd, grid[i][j]);
    }
    maxProductForSubGrid = tempProd > maxProductForSubGrid ? tempProd : maxProductForSubGrid;

    tempProd = 1L;
    for (int i = row + length - 1, j = col; i >= 0 && j < col + length; i--, j++) {
      //tempProd *= grid[i][j];
      tempProd = Math.multiplyExact(tempProd, grid[i][j]);
    }
    maxProductForSubGrid = tempProd > maxProductForSubGrid ? tempProd : maxProductForSubGrid;

    return maxProductForSubGrid;
  }

  private void updateTabularMapForPair(Pair<Integer, Integer> key, Map<Pair<Integer, Integer>, Map<direction, Queue<Long>>> tabularProductMap,
      direction curDirection, Queue<Long> curProducts) {
    Map<direction, Queue<Long>> curMap = null;
    if (tabularProductMap.containsKey(key)) {
      curMap = tabularProductMap.get(key);
      curMap.put(curDirection, curProducts);
    } else {
      curMap = new HashMap<>();
      curMap.put(curDirection, curProducts);
      tabularProductMap.put(key, curMap);
    }
  }

  private long getMaxProductUsingPrevMap(int row, int col, int length, int[][] grid, Map<direction, Queue<Long>> prevMap,
      Map<Pair<Integer, Integer>, Map<direction, Queue<Long>>> tabularProductMap, direction curDirection) {
    Queue<Long> prevProducts = null;
    Queue<Long> curProducts = new LinkedList();
    long remainingEntityTypeProduct = 1L;
    long tempMaxProd = 1L;

    /**
     * copy the 'length - 1' products on given dimension (horizontal/vertical) as is. They have been precalculated during prev subgrid.
     * Strategy : Copy all, then remove the head of the queue, calculate the product of the only new dimention for cur subgrid and add to queue's tail
     */
    prevProducts = prevMap.get(curDirection);
    curProducts.addAll(prevProducts);
    curProducts.remove();

    /**
     * calculate product of remaining elements on given dimension.
     */
    if (curDirection == direction.HORIZONTAL) {
      for (int j = col; j < col + length; j++) {
        //remainingEntityTypeProduct *= grid[row + length - 1][j];
        remainingEntityTypeProduct = Math.multiplyExact(remainingEntityTypeProduct, grid[row + length - 1][j]);
      }
    } else {
      for (int i = row; i < row + length; i++) {
        //remainingEntityTypeProduct *= grid[i][col + length - 1];
        remainingEntityTypeProduct = Math.multiplyExact(remainingEntityTypeProduct, grid[i][col + length - 1]);
      }
    }
    curProducts.add(remainingEntityTypeProduct);

    Pair<Integer, Integer> key = new Pair<>(row, col);
    this.updateTabularMapForPair(key, tabularProductMap, curDirection, curProducts);

    tempMaxProd = curProducts.stream()
                             .mapToLong(Long::longValue)
                             .max()
                             .orElse(Long.MIN_VALUE);

    return tempMaxProd;
  }


  /**
   * ------------- NO MORE USED. ------------. ArithmaticException is also not handled for the same reason.
   */
  private long getMaxProductUsingPrevMapAndNewElements_DEPRECATED(int row, int col, int length, int[][] grid, Map<direction, Queue<Long>> prevMap,
      Map<Pair<Integer, Integer>, Map<direction, Queue<Long>>> tabularProductMap,
      direction curDirection, int prevCol, int prevRow) {

    Queue<Long> prevProducts = null;
    List<Long> prevProductList = new ArrayList<>(length);
    Queue<Long> curProducts = new LinkedList();
    long curProd = 1L, prevProd = 1L, curMaxProd = 1L;

    prevProducts = prevMap.get(curDirection);
    prevProductList.addAll(prevProducts);

    if (curDirection == direction.HORIZONTAL) {
      for (int i = row; i < row + length; i++) {
        prevProd = prevProductList.remove(0);
        curProd = prevProd / grid[i][prevCol] * grid[i][col + length - 1];
        curProducts.add(curProd);
      }
    } else {
      for (int j = col; j < col + length; j++) {
        prevProd = prevProductList.remove(0);
        curProd = prevProd / grid[prevRow][j] * grid[row + length - 1][j];
        curProducts.add(curProd);
      }
    }

    Pair<Integer, Integer> key = new Pair<>(row, col);
    this.updateTabularMapForPair(key, tabularProductMap, curDirection, curProducts);

    curMaxProd = curProducts.stream()
                            .mapToLong(Long::longValue)
                            .max()
                            .orElse(Long.MIN_VALUE);

    return curMaxProd;
  }

  private long populateEntityFromPrebuiltEntitiesAndGetMaxProductForEntity(int row, int col, int length, int[][] grid,
      Map<Pair<Integer, Integer>, Map<direction, Queue<Long>>> tabularProductMap, direction curDirection) {

    int prevRow = row > 0 ? row - 1 : row;
    int prevCol = col > 0 ? col - 1 : col;
    Pair<Integer, Integer> keyForHorizontal = row == 0 ? new Pair<>(row, prevCol) : new Pair<>(prevRow, col);
    Pair<Integer, Integer> keyForVertical = col == 0 ? new Pair<>(prevRow, col) : new Pair<>(row, prevCol);
    Map<direction, Queue<Long>> prevMap = null;
    long curMaxProd = 1L;

    if (curDirection == direction.HORIZONTAL) {
      if (tabularProductMap.containsKey(keyForHorizontal)) {
        prevMap = tabularProductMap.get(keyForHorizontal);
        curMaxProd = this.getMaxProductUsingPrevMap(row, col, length, grid, prevMap, tabularProductMap, curDirection);
      } else {
        /*prevMap = tabularProductMap.get(keyForVertical);
        curMaxProd = this.getMaxProductUsingPrevMapAndNewElements_DEPRECATED(row, col, length, grid, prevMap, tabularProductMap, curDirection, prevCol, prevRow);*/
      }
    } else {
      if (tabularProductMap.containsKey(keyForVertical)) {
        prevMap = tabularProductMap.get(keyForVertical);
        curMaxProd = this.getMaxProductUsingPrevMap(row, col, length, grid, prevMap, tabularProductMap, curDirection);
      } else {
        /*prevMap = tabularProductMap.get(keyForHorizontal);
        curMaxProd = this.getMaxProductUsingPrevMapAndNewElements_DEPRECATED(row, col, length, grid, prevMap, tabularProductMap, curDirection, prevCol, prevRow);*/
      }
    }

    return curMaxProd;
  }


  /**
   * @return maxProduct
   */
  public long getProduct(int[][] grid, int length) throws InputException, ArithmeticException {
    int row = 0, col = 0, limit = grid.length - length;
    long result = Long.MIN_VALUE;
    long maxProductForCurSubGrid;
    Map<Pair<Integer, Integer>, Map<direction, Queue<Long>>> tabularProductMap = new HashMap<>();

    if(grid.length == 0)
      throw new InputException("Invalid input => Grid is empty.");

    if (grid.length == grid[0].length) {
      if (grid.length < length) {
        //return -1L;
        throw new InputException("Invalid input => length provided is greater than grid dimension.");
      } else if (grid.length == length) {
        /**
         * No true SubGrids possible. Grid == the only SubGrid.
         */

        maxProductForCurSubGrid = this.fillMapWithProductsOfSubgridElementsAndGetMaxFromCurSubgrid(row, col, length, grid, tabularProductMap);

        return maxProductForCurSubGrid > result ? maxProductForCurSubGrid : result;
      } else {
        while (row <= limit && col <= limit) {
          if (row > limit && col > limit) {
            break;
          } else {
            maxProductForCurSubGrid = this.fillMapWithProductsOfSubgridElementsAndGetMaxFromCurSubgrid(row, col, length, grid, tabularProductMap);

            result = maxProductForCurSubGrid > result ? maxProductForCurSubGrid : result;

            col++;

            if (col > limit && row < limit) {
              row++;
              col = 0;
            }
          }
        }
        return result;
      }
    } else {
      //imbalanced grid. Replace -1L with exception. -1 could be a valid max product.
      throw new InputException("Invalid input => grid is not balanced or symmetric");
    }
  }

  /**
   *
   * @param args
   */
  public static void main(String[] args) {
    System.out.println("Please call methods explictly with inputs of your choice.");
  }
}
