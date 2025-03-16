import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicReference;

class Resource{
    ArrayList<Integer> array;

    public Resource(int szalak_szama){
        Random r = new Random();
        array = new ArrayList<Integer>(szalak_szama * 100);

        for(int i = 0; i < szalak_szama * 100; ++i)
            array.add(i / 100);
    }
}

class Calculator extends Thread{
    Resource resource;
    int start;
    int end;
    CyclicBarrier cyclicBarrier;
    int sum = 0;
    public Calculator(Resource resource, int start, int end, CyclicBarrier cyclicBarrier){
        this.resource = resource;
        this.start = start;
        this.end = end;
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run(){
        System.out.println(Thread.currentThread().threadId() + " is counting...");
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for(int i = start; i < end; ++i){
            sum += resource.array.get(i);
        }
        System.out.println(sum);
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}

class Osszegzo implements Runnable{
    ArrayList<Calculator> calculators;
    int osszeg = 0;
    public Osszegzo(ArrayList<Calculator> calculators){
        this.calculators = calculators;
    }
    @Override
    public void run(){
        System.out.println("Summarizing...");
        for(Calculator c : calculators)
            osszeg += c.sum;
;
        System.out.println("Sum: " + osszeg);
    }
}

public class Main{
    public static void main(String[] args) {
        
        int szalak_szama = 10;

        Resource resource = new Resource(szalak_szama);
        ArrayList<Calculator> calculators = new ArrayList<>(szalak_szama);
        Osszegzo o = new Osszegzo(calculators);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(szalak_szama, o);

        for(int i = 0; i < szalak_szama; ++i){
            Calculator c = new Calculator(resource, 100 * i, 100 * i + 100, cyclicBarrier);
            c.start();
            calculators.add(c);
        }


        AtomicReference<String> str = new AtomicReference<>("Idle");

        for(int i = 0; i < 10; ++i) {
            new Thread(() -> {
                while (!str.compareAndSet("Idle", "InProgress")) {
                    continue;
                }
                try {
                    System.out.println(Thread.currentThread().threadId() + " is in progress...");
                    System.out.println(str.get());
                    Thread.sleep(500);
                    str.set("Idle");
                    System.out.println(str.get());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }

    }
}
