package batch;

import java.util.concurrent.*;
import java.util.function.Supplier;

public class BatchProcessor {

    int tps;

    int threads;

    Supplier<Task>  taskSupplier;

    String dir;

    public BatchProcessor(int tps, int threads, Supplier<Task> taskSupplier, String dir) {
        this.tps = tps;
        this.threads = threads;
        this.taskSupplier = taskSupplier;
        this.dir = dir;

        init();
    }


    ExecutorService service ;

    TaskHandler[] handlers;

    FileContentsIterator fileContentsIterator ;
    private void init()
    {

        service = Executors.newFixedThreadPool(threads, Thread::new);

        handlers = new TaskHandler[threads];

        for (int i=0;i<threads;i++)
        {
            handlers[i] = new TaskHandler(taskSupplier);
            service.submit(handlers[i]);
        }

        dirContentsIterator = new DirContentsIterator(dir);

        fileContentsIterator = new FileContentsIterator(dirContentsIterator.next());
    }

    public void complete()
    {

        service.shutdown();
        for (int i=0;i<threads;i++)
        {
            handlers[i].terminate();

        }


        return;
    }


    public void batch()
    {
        int counter=0;
        String s;
        while(true)  // replace by dir processing
        {
            long start = System.currentTimeMillis();  long end = start+1000;

            for (int i=0;i<tps;i++)
            {
                if ((s=moreData())==null) return;
                handlers[counter++%threads].handOff(s);
            }

            long curr = System.currentTimeMillis();

            if (curr<end)
            {
                try {
                    Thread.sleep(end-curr);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    DirContentsIterator dirContentsIterator ;

    int count=10;
    private String moreData()
    {
        String res = moreFileData();

        if (res!=null)
            return res;

        if (dirContentsIterator.hasNext())
        {
            fileContentsIterator = new FileContentsIterator(dirContentsIterator.next());

        }

        res = moreFileData();

        return res;

    }

    private String moreFileData()
    {

        if (fileContentsIterator.hasNext())
            return fileContentsIterator.next();
        else
            return null;
    }


    public static void main(String[] args) {

        Supplier<Task> taskSupplier = () ->  {return new PrintTaskHandler();  };

        BatchProcessor batchProcessor = new BatchProcessor(2,2,taskSupplier,"/home/manoj/data/vfs/data" );

        batchProcessor.batch();

        batchProcessor.complete();

        System.out.println("Batch processing completed");

    }


    static class TaskHandler implements Runnable
    {

        volatile boolean process = true;
        Supplier<Task>  taskSupplier;

        SynchronousQueue<String> queue = new SynchronousQueue<>();

        public TaskHandler(Supplier<Task> taskSupplier) {
            this.taskSupplier = taskSupplier;
        }


        // not perfect as thread can get scheduled out . before process is set to false .
        public void terminate()
        {

          //  System.out.println("Terminating ");
            if (queue.isEmpty())
            {
               //System.out.println("Last message - dummy");

                handOff("");
                process = false;
            }
            else
            {
             //   System.out.println("Queue has message ");
                process = false;
                // do nothing .
            }
        }

        public void handOff(String str)
        {

            try {
                queue.put(str);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }


        public void run()
        {
            while(process)
            {

                String str = null;
                try {
                    str = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (str.equals(""))
                    continue;
                Task task = taskSupplier.get();

                task.process(str);


            }
        }


    }
}
