package batch;

public class PrintTaskHandler implements Task {

    public PrintTaskHandler()
    {

    }

    public void process(String str)
    {
        System.out.println(Thread.currentThread().getName() + "  " + this + "  " + str);
    }
}
