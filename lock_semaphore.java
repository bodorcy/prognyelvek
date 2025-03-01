import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class SharedInt{
    int value;
    private final Lock lock;

    public SharedInt(){
        this.value = 0;
        this.lock = new ReentrantLock();
    }

    public void setValue(int newValue){
        lock.lock();
        System.out.print("Setting new value: " + this.value);
        this.value = newValue;
        System.out.println(" --> " + newValue);
        lock.unlock();
    }
    public void decrement(){
        lock.lock();
        System.out.print("Decrementing: " + this.value);
        this.value--;
        System.out.println(" --> " + this.value);
        lock.unlock();
    }
}

class SharedIntSemaphore extends SharedInt{
    private final Semaphore semaphore = new Semaphore(1);
    public SharedIntSemaphore(){
        super();
        this.value = 0;
    }
    @Override
    public void setValue(int newValue) {
        try{
            semaphore.acquire();
            System.out.print("Setting new value: " + this.value);
            this.value = newValue;
            System.out.println(" --> " + newValue);
            semaphore.release();
        }
        catch (InterruptedException e){}
    }
    @Override
    public void decrement() {
        try{
            semaphore.acquire();
            System.out.print("Decrementing: " + this.value);
            this.value--;
            System.out.println(" --> " + this.value);
            semaphore.release();
        }
        catch (InterruptedException e){}
    }
}
class Modifier implements Runnable{
    private final SharedInt sharedInt;

    public Modifier(SharedInt sharedInt){
        this.sharedInt = sharedInt;
    }
    @Override
    public void run(){
        Random r = new Random();
        int choice = r.nextInt();

        if(choice % 2 == 1)
            sharedInt.setValue(choice % 10);
        else
            sharedInt.decrement();
    }
}

class ParkingLot{
    protected final int max_capacity = 10;
    ArrayList<Integer> cars;
    Lock lock;
    Condition notFull;
    public ParkingLot(){
        this.cars = new ArrayList<Integer>(max_capacity);
        lock = new ReentrantLock();
        notFull = lock.newCondition();
    }
    public void enter(int carnum){
        lock.lock();
        while(cars.size() == max_capacity){
            try{
                notFull.await();
            }
            catch (InterruptedException e){}
        }
        cars.add(carnum);
        lock.unlock();
    }
    public void leave(int carnum){
        lock.lock();
        cars.removeFirst();
        notFull.signal();
        lock.unlock();
    }
}
class ParkingLotSemaphore extends ParkingLot{
    Semaphore semaphore = new Semaphore(max_capacity);

    @Override
    public void enter(int carnum){
        try{
            semaphore.acquire();
        }
        catch (InterruptedException e){}
        cars.add(carnum);
    }
    public void leave(int carnum){
        cars.removeFirst();
        semaphore.release();
    }
}
class Car extends Thread{
    private final ParkingLot parkingLot;
    private final int carnum;
    public Car(ParkingLot parkingLot, int carnum){
        this.parkingLot = parkingLot;
        this.carnum = carnum;
    }
    @Override
    public void run(){
        parkingLot.enter(this.carnum);
        System.out.println(parkingLot.cars);
        try{
            Thread.sleep(50 + carnum * 10);
        }
        catch (InterruptedException e){}
        parkingLot.leave(this.carnum);
    }
}

public class Main{
    public static void main(String[] args) throws InterruptedException {
        SharedIntSemaphore sharedInt = new SharedIntSemaphore();

        for(int i = 0; i < 20; ++i){
            Thread t = new Thread(new Modifier(sharedInt));
            t.start();
        }
        Thread.sleep(100);

        ParkingLotSemaphore parkingLot = new ParkingLotSemaphore();

        for(int i = 0; i < 100; ++i){
            Car c = new Car(parkingLot, i);
            c.start();
        }

    }
}
