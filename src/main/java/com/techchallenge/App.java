package com.techchallenge;


import com.exception.InputException;
import java.util.HashMap;
import java.util.LinkedList;
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
    int rows = grid.length;
    int columns = rows > 0 ? grid[0].length : 0;

    /**
     * if grid is not K x K dimension, then return -1.
     * if length is greater than grid length, return -1.
     */
    if (rows == 0 || columns == 0) {
      return -1L;
    }
    if (length == 1) {
      return rows * columns;
    }

    if (rows < length && columns < length) {
      return -1L;
    } else if (rows == length && columns == length) {
      /**
       * There are total (2K + 2) combinations in any given grid of size K x K. K rows + K columns + 2 diagonals.
       */
      return (2 * length) + 2;
    }
    /**
     * on any given dimension (row, col, diagonal), total combination = dimensionLength - length + 1.
     */
    else if (length > columns && length <= rows) {
      //consider only rows. There will also be no diagonals.
      return columns * (rows - length + 1);
    } else if (length > rows && length <= columns) {
      //consider only columns. There will also be no diagonals.
      return rows * (columns - length + 1);
    } else {
      //asymmetric grid.
      int max = rows > columns ? rows : columns;
      int min = rows < columns ? rows : columns;

      int totalNumberOfLongestDiagonals = max - min + 1;
      int longestDiagonalLength = min;

      long totalCombinationsOnLongestDiagonal = longestDiagonalLength - length + 1;
      long combinationsFromLongestDiagonals = 2 * (totalCombinationsOnLongestDiagonal * totalNumberOfLongestDiagonals);
      /**
       * subOptimalLength diagonals are those whose length is equal to or greater than 'length', but less than longest diagonal.
       * They too form unique combinations.
       */
      long combinationsFromSubOptimalLengthDiagonals = 2 * (totalCombinationsOnLongestDiagonal - 1) * totalCombinationsOnLongestDiagonal;

      long totalRowCombinations = rows * (columns - length + 1);
      long totalColumnCombinations = columns * (rows - length + 1);

      return combinationsFromLongestDiagonals + totalRowCombinations + totalColumnCombinations + combinationsFromSubOptimalLengthDiagonals;
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
      }
    } else {
      if (tabularProductMap.containsKey(keyForVertical)) {
        prevMap = tabularProductMap.get(keyForVertical);
        curMaxProd = this.getMaxProductUsingPrevMap(row, col, length, grid, prevMap, tabularProductMap, curDirection);
      }
    }

    return curMaxProd;
  }

  private long getMaxProductFromTheDimension(final int[][] grid, final int length, final int minDimension, final int maxLimit,
      final direction curDirection) {
    long prevProduct = 1L;
    long curProduct = 1L;
    int counter;        //counter for unique combinations
    long result = Long.MIN_VALUE;

    for (int j = 0; j < minDimension; j++) {
      for (int i = 0; i <= maxLimit; i++) {
        counter = 0;
        while (counter < length) {
          if (curDirection == direction.VERTICAL) {
            if (i > 0) {
              curProduct = Math.multiplyExact(curProduct, prevProduct / grid[i - 1][j] * grid[i + length - 1][j]);
              break;
            } else {
              curProduct = Math.multiplyExact(curProduct, grid[i + counter][j]);
            }
          } else {
            if (i > 0) {
              curProduct = Math.multiplyExact(curProduct, prevProduct / grid[j][i - 1] * grid[j][i + length - 1]);
              break;
            } else {
              curProduct = Math.multiplyExact(curProduct, grid[j][i + counter]);
            }
          }
          counter++;
        }
        prevProduct = curProduct;
        result = curProduct > result ? curProduct : result;
        curProduct = 1L;
      }
    }

    return result;
  }


  /**
   * @return maxProduct
   */
  public long getProduct(int[][] grid, int length) throws InputException, ArithmeticException {
    int rows = grid.length;
    int columns = rows > 0 ? grid[0].length : 0;
    long result = Long.MIN_VALUE;
    long maxProductForCurSubGrid;
    Map<Pair<Integer, Integer>, Map<direction, Queue<Long>>> tabularProductMap = new HashMap<>();


    if (rows == 0) {
      throw new InputException("Invalid input => Grid is empty.");
    }
    if (length > rows && length > columns) {
      throw new InputException("Invalid input => Length is greater than both dimensions of grid.");
    }

    int row = 0, col = 0, rowLimit = rows - length, colLimit = columns - length;


    if (rows == columns && columns == length) {
      /**
       * No additional SubGrids possible. Grid == the only SubGrid.
       */
      maxProductForCurSubGrid = this.fillMapWithProductsOfSubgridElementsAndGetMaxFromCurSubgrid(row, col, length, grid, tabularProductMap);

      return maxProductForCurSubGrid > result ? maxProductForCurSubGrid : result;
    } else if ((length > columns && length < rows) || (length > rows && length < columns)) {
      //consider only possible dimension (row ExOR col). No diagonals possible.
      int minDimension = rows < columns ? rows : columns;
      int maxLimit = (rows < columns ? columns : rows) - length;
      direction curDirection = length > columns ? direction.VERTICAL : direction.HORIZONTAL;
      return this.getMaxProductFromTheDimension(grid, length, minDimension, maxLimit, curDirection);
    } else {
      while (row <= rowLimit && col <= colLimit) {
        if (row > rowLimit && col > colLimit) {
          break;
        } else {
          maxProductForCurSubGrid = this.fillMapWithProductsOfSubgridElementsAndGetMaxFromCurSubgrid(row, col, length, grid, tabularProductMap);

          result = maxProductForCurSubGrid > result ? maxProductForCurSubGrid : result;

          col++;

          if (col > colLimit && row < rowLimit) {
            row++;
            col = 0;
          }
        }
      }
      return result;
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
