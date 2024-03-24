import java.util.Arrays;

public class Solution {
    public long[] mostFrequentIDs(int[] nums, int[] freq) {
        int n = freq.length;
        long[] res = new long[n];
        long[] arr = new long[100001];
        SegmentTree segmentTree = new SegmentTree(arr);
        for (int i = 0; i < freq.length; i++) {
            long val = segmentTree.nums[nums[i]];
            segmentTree.update(nums[i], val + freq[i]);

            long max = segmentTree.getMaxValue();
            res[i] = max;
        }

        return res;
    }
}
class SegmentTree {
    int length;
    long[] segmentTree;
    long[] nums;
    int n;

    public SegmentTree(long[] nums) {
        this.nums = nums;
        n = nums.length;
        if ((n != 1) && ((n & (n - 1)) == 0)) {
            length = n * 2 - 1;
        } else {
            int power = 1;
            while (power < n) {
                power *= 2;
            }
            length = power * 2 - 1;
        }
        segmentTree = new long[length];
        Arrays.fill(segmentTree, Integer.MIN_VALUE);
        buildTree(0, n - 1, 0);
    }

    public void buildTree(int low, int high, int position) {
        if (low == high) {
            segmentTree[position] = nums[low];
            return;
        }
        int mid = low + (high - low) / 2;

        buildTree(low, mid, 2 * position + 1);
        buildTree(mid + 1, high, 2 * position + 2);
        segmentTree[position] = Math.max(segmentTree[2 * position + 1], segmentTree[2 * position + 2]);
    }

    public void update(int index, long val) {
        updateTree(0, n - 1, 0, index, val);
    }

    public void updateTree(int low, int high, int position, int index, long val) {
        if (index < low || index > high) {
            return;
        }

        if (low == high) {
            nums[index] = val;
            segmentTree[position] = val;
            return;
        }

        int mid = low + (high - low) / 2;
        updateTree(low, mid, 2 * position + 1, index, val);
        updateTree(mid + 1, high, 2 * position + 2, index, val);

        segmentTree[position] = Math.max(segmentTree[2 * position + 1], segmentTree[2 * position + 2]);
    }

    public long getMaxValue() {
        return segmentTree[0];
    }

    public long getMaxInRange(int queryLow, int queryHigh) {
        return getMaxInRangeHelper(0, n - 1, 0, queryLow, queryHigh);
    }

    private long getMaxInRangeHelper(int low, int high, int position, int queryLow, int queryHigh) {
        if (low > queryHigh || high < queryLow) {
            return Integer.MIN_VALUE;
        }

        if (low >= queryLow && high <= queryHigh) {
            return segmentTree[position];
        }

        int mid = low + (high - low) / 2;
        long leftMax = getMaxInRangeHelper(low, mid, 2 * position + 1, queryLow, queryHigh);
        long rightMax = getMaxInRangeHelper(mid + 1, high, 2 * position + 2, queryLow, queryHigh);

        return Math.max(leftMax, rightMax);
    }
}

