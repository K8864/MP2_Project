package extra;

public class Array
{
    public static void main(String[] args)
    {
        int[] a = {1, 1, 2, 3, 4, 5};
        String[] b = {"a", "b", "c", "d", "e"};
        int[] c = new int[1];
//        System.out.println(sum(a));
//        System.out.println(average(a));
//        System.out.println(mode(a));
    }
    public static int sum(int[] a)
    {
        int b = 0;
        for(int i = 0; i < a.length; i++)
        {
            b += a[i];
        }
        return b;
    }
    public static double average(int[] a)
    {
        double b = 0;
        for(int i = 0; i < a.length; i++)
        {
            b += a[i];
        }
        return b/a.length;
    }
    public static int mode(int[] a)
    {
        int track = 0, repeat = 0, mostRepeat = 0;
        for(int b : a)
        {
            for(int c : a)
            {
                if(b == c)
                {
                    repeat++;
                }
            }
            repeat--;
            if(repeat > track)
            {
                track = repeat;
                mostRepeat = b;
                repeat = 0;
            }
        }
        return mostRepeat;
    }
}
