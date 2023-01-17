package extra;

public class Writing
{
    private int number;

    /**
     * @param number
     * @return
     * precondition: number has to be equal to number
     * postcondition: the value of number + 100 will be returned
     */
    private static int add100(int number)
    {
        while(number == number)
        {
            for(int i = 0; i < 10; i++)
            {
                number += 10;
            }
            return number;
        }
        return 0;
    }
    public String toString()
    {
        return "number + 100 is " + add100(number);
    }
}
