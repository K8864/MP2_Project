package extra;

public class Iteration
{
    public static void main(String[] args)
    {
//        System.out.println(add100(25));
//        System.out.println(count());
    }
    public static int add100(int a)
    {
        while(a == a)
        {
            for(int i = 0; i < 10; i++)
            {
                a += 10;
            }
            return a;
        }
        return 0;
    }
    public static int count()
    {
        int count = 0;
        for(int i = 0; i < 10; i++)
        {
            count++;
        }
        return count;
    }
}