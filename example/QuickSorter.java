package example;

import java.util.Stack;

public class QuickSorter implements Sorter{
    private int[] a;

    @Override
    public void load(int[] a) {
        this.a = a;
    }
    
    private String plan = "";

    @Override
    public void sort() {
        int low = 0, high = a.length - 1;
        int pivot;
        if(low >= high){
            return;
        }
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(low);
        stack.push(high);
        while(!stack.empty()){
            high = stack.pop();
            low = stack.pop();
            pivot = partition(a,low,high);
            if(low < pivot - 1){
                stack.push(low);
                stack.push(pivot - 1);
            }
            if(pivot + 1 < high){
                stack.push(pivot + 1);
                stack.push(high);
            }
        }
    }

    private int partition(int[] a,int low,int high){
        int pivotKey = a[low];
        while(low < high){
            while(low < high && a[high] >= pivotKey){
                high--;
            }
            //a[low] = a[high];
            swap(low,high);
            while(low < high && a[low] <= pivotKey){
                low++;
            }
            swap(low,high);
            //a[high] = a[low];
        }
        a[low] = pivotKey;
        return low;
    }

    private void swap(int i, int j) {
        int temp;
        temp = a[i];
        a[i] = a[j];
        a[j] = temp;
        plan += "" + a[i] + "<->" + a[j] + "\n";
        //System.out.println(plan);
    }

    @Override
    public String getPlan() {
        return this.plan;
    }
}
