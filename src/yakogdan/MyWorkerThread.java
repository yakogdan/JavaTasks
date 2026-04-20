package yakogdan;

import java.util.ArrayList;

public class MyWorkerThread {

    static class BlockingQueue {
        ArrayList<Runnable> tasks = new ArrayList<>();

        public synchronized Runnable get() throws InterruptedException {
            while (tasks.isEmpty()) {
                wait();
            }

            Runnable task = tasks.getFirst();
            tasks.remove(task);
            return task;
        }

        public synchronized void put(Runnable task) {
            tasks.add(task);
            notify();
        }
    }

    public static Runnable getTask() {
        return new Runnable() {

            @Override
            public void run() {
                System.out.println("Task started: " + this);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                System.out.println("Task finished: " + this);
            }
        };
    }

    static void main() {
        BlockingQueue queue = new BlockingQueue();

        Thread worker = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                Runnable task;

                try {
                    task = queue.get();
                    task.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        worker.start();

        for (int i = 0; i < 5; i++) {
            queue.put(getTask());
        }
    }
}