import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Parkolo{
    private final int N = 10;
    ArrayList<Integer> cars;
    Lock lock = new ReentrantLock();
    Condition ures = lock.newCondition();
    public Parkolo(){
        cars = new ArrayList<>(N);
    }
    public void enter(int carnum){
        lock.lock();
        while (cars.size() == N){
            try{
                //System.out.println(carnum + " is waiting...");
                ures.await();
            }
            catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }
        cars.add(carnum);
        System.out.println(cars);
        lock.unlock();
    }

    public void leave(int carnum){
        lock.lock();
        System.out.println(cars);
        cars.removeFirst();
        ures.signalAll();
        lock.unlock();
    }
    public void parking(int carnum){
        //System.out.println(carnum + " is parking...");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
class Auto implements Callable<Void>{
    Parkolo parkolo;
    int carnum;
    public Auto(Parkolo parkolo, int carnum){
        this.parkolo = parkolo;
        this.carnum = carnum;
    }

    @Override
    public Void call(){
        parkolo.enter(carnum);
        parkolo.parking(carnum);
        parkolo.leave(carnum);

        return null;
    }
}

class Main {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        Parkolo parkolo = new Parkolo();

        for(int i = 0; i < 100; ++i){
            executor.submit(new Auto(parkolo, i+1));
        }
        executor.shutdown();
    }
}
