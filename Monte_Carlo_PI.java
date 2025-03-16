import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

class Point{
    double x;
    double y;
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }
    public boolean isInCirlce(){
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) <= 1; // egységsugarú, (0;0) középpontú körön belül található a pont
    }
}

class MyCallable implements Callable<Point> {
    Point point = new Point(-1 + Math.random() * 2, -1 + Math.random() * 2);

    @Override
    public Point call(){
        return point;
    }
}

class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int N = 200_000;
        int K = 0;

        ExecutorService executorService = Executors.newCachedThreadPool();
        List<MyCallable> callables = new ArrayList<>();
        List<Future<Point>> futures;



        for(int i = 0; i < N; ++i){
            MyCallable myCallable = new MyCallable();
            callables.add(myCallable);
        }
        futures = executorService.invokeAll(callables);

        for(Future<Point> p : futures){
            if (p.get().isInCirlce())
                K += 1;
        }
        executorService.shutdown();

        System.out.println("A pi közelítése: " + 4.0 * K/N);
    }
}
