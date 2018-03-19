package side;

/**
 * Created by Maxim Tarasov on 06.10.2017.
 */
class SomeThing            //Нечто, реализующее интерфейс Runnable
        implements Runnable        //(содержащее метод run())
{
    public void run()        //Этот метод будет выполняться в побочном потоке
    {
        System.out.println("Привет из побочного потока!");
    }
}