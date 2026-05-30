import java.util.Stack;

class Solution {

    /*
     * Updates the histogram heights for the current row.
     *
     * If the current cell contains '1', increase the height by 1.
     * If the current cell contains '0', reset the height to 0.
     */
    private void resetHeight(char[][] matrix, int[] height, int row) {
        for (int col = 0; col < matrix[0].length; col++) {
            if (matrix[row][col] == '1') {
                height[col]++;
            } else {
                height[col] = 0;
            }
        }
    }

    /*
     * Finds the largest rectangle area in a histogram.
     *
     * Uses a monotonic increasing stack:
     * - Stack stores indices of histogram bars.
     * - When a smaller height is encountered, calculate areas
     *   for bars taller than the current one.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    private int largestInLine(int[] height) {

        if (height == null || height.length == 0) {
            return 0;
        }

        int len = height.length;
        int maxArea = 0;

        Stack<Integer> stack = new Stack<>();

        // Iterate one extra time with height = 0
        // to flush remaining elements from the stack.
        for (int i = 0; i <= len; i++) {

            int currentHeight = (i == len) ? 0 : height[i];

            /*
             * If current bar is smaller than the bar at stack top,
             * calculate area for the taller bars.
             */
            while (!stack.isEmpty() &&
                   currentHeight < height[stack.peek()]) {

                int topIndex = stack.pop();
                int barHeight = height[topIndex];

                // Calculate width
                int width;

                if (stack.isEmpty()) {
                    width = i;
                } else {
                    width = i - stack.peek() - 1;
                }

                maxArea = Math.max(maxArea, barHeight * width);
            }

            stack.push(i);
        }

        return maxArea;
    }

    /*
     * Main function:
     * Treat each row as the base of a histogram.
     * For every row:
     *   1. Update histogram heights.
     *   2. Find largest rectangle in that histogram.
     *   3. Keep track of the global maximum area.
     *
     * Time Complexity:
     * O(rows * cols)
     *
     * Space Complexity:
     * O(cols)
     */
    public int maximalRectangle(char[][] matrix) {

        // Edge case: empty matrix
        if (matrix == null ||
            matrix.length == 0 ||
            matrix[0].length == 0) {
            return 0;
        }

        int rows = matrix.length;
        int cols = matrix[0].length;

        // Histogram heights
        int[] height = new int[cols];

        // Initialize histogram using first row
        for (int col = 0; col < cols; col++) {
            if (matrix[0][col] == '1') {
                height[col] = 1;
            }
        }

        // Largest rectangle from first row histogram
        int result = largestInLine(height);

        // Process remaining rows
        for (int row = 1; row < rows; row++) {

            // Update histogram heights
            resetHeight(matrix, height, row);

            // Find largest rectangle for current histogram
            result = Math.max(result, largestInLine(height));
        }

        return result;
    }
}
