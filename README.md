# GridChallenge


## Assumptions

* Method signatures provided were used as guidelines for implementation. For this reason, kept the return type as ``` long ``` and grid element type as ``` int ```, as provided in the problem statement. 
* The term combination refers to mathematical combination i.e. order of elements do not matter and resulting collection is equivalent to Set<T>. This is also captured in 'Clarifications' section of the problem statement



## Solution Approach

**1. Return total number of combinations**
 - divide a grid of KxK into sub-grid of length x length, if possible.
 - if elements of this sub-grid have never been taken into account during prior processing, then there are total (2*length + 2) unique combinations i.e. ```length```# rows + ```length```# columns + 2 diagonals.
 - if elements of this sub-grid have been taken into account during prior processing, then there will be ```length - 1``` combinations less than a fresh grid combinations. 
      e.g. consider 4x4 grid with ```length=3```. First sub-grid starts at (0,0) and ends at (2,2) and will have 8 unique combinations. 
      Second Subgrid starts at (0, 1) and ends at (2,3) and has 8 combinations (not-unique), out of which 2 combinations have been taken into account while processing sub-grid that starts at (0,0) i.e. column#1 and column#2.
      Hence the formula.
      
**2. Return maximum product of adjucent elements**
 - this solution uses the same approach of sliding-window as that of problem#1. The solution uses memoization to avoid calculating values when they are already calculated during prior sub-grid processing.
 - this solution processes a sub-grid of size ```length x length``` starting from ```grid[0][0]```. Once all products are calculated, the values are stored in hashMap, with key being <row,col> of sub-grid, and then processing moves to next window (sliding window). Once we hit the end of a given horizantal traversal, if possible, we move back to ```row + 1``` and ```col = 0``` and restart the sliding window processing.
 - e.g. for a grid of 4x4 and length = 3, first sub-grid/window starts from (0,0). The hashmap then stores a map<traversalDirection, ListOfProductsAlongThisTraversalDirection> with key (0,0). When this processing is done, we have one entry in hashMap with key (0,0) and value is second map. This second map has two keys - Horizontal and Vertical, representing traversal direction. When window is slid to next position i.e. (0, 1), for this sub-grid processing, we use the products which were computed while prior processing for given traversal direction.

## Performance Considerations
- In order to avoid overflow, we could have used BigInteger / BigDecimal types. However, that has performance impact of tranforming the values back and forth (BigInteger to/from long) into required types.
- Memoization helps when the grid size is very large.

## Alernate Implementation
- We can use fork/join framework for each sub-grid processing i.e. parent thread's responsibility is to create a sub-grid and hand it over to worker thread (ExecutorService). Worker thread then computes the max product and returns a CompletableFuture<Grid> asynchronously. We shall create only those many worker threads simultaneously as we have processors available on machine (actually workerThreadCount = NumberOfProcessors-1). With this, memoization may be bit challenging (or if done with synchronization, it will reduce the overall speed), but we may gain speed through parallalizing the sub-grid processing. Benchmark needs to be built to validate this assumption.
