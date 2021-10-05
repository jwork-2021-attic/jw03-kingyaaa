package example;

public class SelectSorter implements Sorter{
    private int[] a;

    @Override
    public void load(int[] a) {
        this.a = a;
    }    

    private String plan = "";

    private void swap(int i, int j) {
        int temp;
        temp = a[i];
        a[i] = a[j];
        a[j] = temp;
        plan += "" + a[i] + "<->" + a[j] + "\n";
        //System.out.println(plan);
    }

    @Override
    public void sort() {
        for(int i = 0;i < a.length;i++){
            int min = i;
            for(int j = i + 1;j < a.length;j++){
                if(a[j] < a[min]){
                    min = j;
                }
            }
            if(i != min){
                swap(i, min);
            }
        }        
    }

    @Override
    public String getPlan() {
        return this.plan;
    }
}
