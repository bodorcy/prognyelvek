import java.util.ArrayList;

import static java.lang.Thread.sleep;

class ParkingLot{
    private final int size;
    public ArrayList<Integer> autok;

    public ParkingLot(int size){
        this.size = size;
        this.autok = new ArrayList<Integer>(size);
    }

    synchronized void enter(int carnum){
        while(autok.size() == size){
            try{
                wait();
            }
            catch (InterruptedException e){
                throw new RuntimeException();
            }
        }

        autok.add(carnum);
        System.out.println(autok);
        notifyAll();
    }
    synchronized void leave(){
        autok.removeFirst();
        notifyAll();
    }
}

class Car extends Thread{
    private final int carnum;
    private final ParkingLot parkingLot;

    public Car(ParkingLot parkingLot, int carnum) {
        this.parkingLot = parkingLot;
        this.carnum = carnum;
    }

    @Override
    public void run(){
        parkingLot.enter(carnum);
        try {
            sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        parkingLot.leave();
    }

}
public class Main {
    public static void main(String[] args) throws InterruptedException {
        ParkingLot parkingLot = new ParkingLot(10);
        ArrayList<Car> cars = new ArrayList<>(100);

        for(int i = 0; i < 100; ++i){
            Car car = new Car(parkingLot, i);
            car.start();
            cars.add(car);
        }

        for(Car c : cars){
            c.join();
        }
    }
}
