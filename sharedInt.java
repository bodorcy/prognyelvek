import java.util.ArrayList;
import java.util.Random;

class SharedInt {
    private int n = 0;

    public int getN() {
        return n;
    }

    synchronized void setValue(int newValue){
        System.out.println("Setting new value: " + newValue);
        n = newValue;
    }

    void decrament(){
        synchronized (this){
            System.out.print("Decramenting... Old value: " + n);
            --n;
            System.out.println(" New value: " + n);
        }
    }
}
class Modifier implements Runnable{
    SharedInt sharedInt;

    public Modifier(SharedInt sharedInt){
        this.sharedInt = sharedInt;
    }

    @Override
    public void run(){
        Random random = new Random();
        int choice = random.nextInt();

        if (choice % 2 == 1)
            sharedInt.setValue(choice % 10);
        else
            sharedInt.decrament();

    }
}
public class Main {
    public static void main(String[] args) throws InterruptedException {
        ArrayList<Thread> threads = new ArrayList<>(20);

        SharedInt sharedInt = new SharedInt();

        for(int i = 0; i < 20; ++i){
            Thread thread = new Thread(new Modifier(sharedInt));
            thread.start();
            threads.add(thread);
        }

        for(Thread t : threads){
            t.join();
        }

        System.out.println(sharedInt.getN());
    }
}
