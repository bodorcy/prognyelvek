import java.util.ArrayList;
import java.util.List;

class Resource{
     int[] array;
     public Resource(int szalak){
         array = new int[szalak * 100];
         for(int i = 0; i < array.length; ++i){
             array[i] = Math.floorDiv(i, 100);
         }
     }
}
class Calculator extends Thread{
    private final int a, b;
    Resource resource;
    int sum = 0;

    public Calculator(int a, int b, Resource resource){
        this.a = a;
        this.b = b;
        this.resource = resource;
    }

    @Override
    public void run() {
        try{
            for(int i = a; i < b; ++i){
                sum += resource.array[i];
                sleep(2);
            }
        }
        catch (InterruptedException e){}
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int szalak = 10;
        int sum = 0;

        Resource resource = new Resource(szalak);
        ArrayList<Calculator> szalArray = new ArrayList<>();

        for(int i = 0; i < szalak; ++i){
            Calculator calculator = new Calculator(100 * i, 100 * i + 100, resource);
            calculator.start();
            szalArray.add(calculator);
        }
        for (Calculator c : szalArray) {
            c.join();
            sum += c.sum;
        }

        System.out.println("Öszeg: " + sum);
    }
}
