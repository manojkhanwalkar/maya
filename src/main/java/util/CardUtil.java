package util;

import com.idealista.fpe.algorithm.ff1.FPE;

import java.util.concurrent.ThreadLocalRandom;

public class CardUtil {

    static ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    public static String createVCard() {

        return createCard(4);

    }


    public static String createMCard() {

        return createCard(5);

    }

    private static String createCard(int first)
    {
        // starts with 4.
        // 16 digits
        // last is check sum

        int[] number = new int[16];
        number[0] = first;

        for (int i=1;i<15;i++)
        {
            number[i] = threadLocalRandom.nextInt(10);
        }

        int sum = 0;
        // generate last digit
        for (int i=14;i>=0;i--,i--)
        {
            int digit = compute(number[i] ,number[i]);

            sum = sum + digit;

            if (i-1>=0)
                sum = sum + number[i-1];
        }

        sum = sum*9;
        sum = sum % 10;

        number[15] = sum;

        StringBuilder builder = new StringBuilder();

        for (int i=0;i<=15;i++)
        {
            builder.append(number[i]);
        }

        return builder.toString();

    }

    public static boolean validateCard(String str)
    {


        int[] number = new int[str.length()];

        for (int i=0;i<number.length;i++)
        {
            String s = str.substring(i,i+1);
           number[i] =  Integer.parseInt(s);

        }



        int sum = 0;
        // generate last digit
        for (int i=number.length-2;i>=0;i--,i--)
        {
            int digit = compute(number[i] ,number[i]);

          //  System.out.println(digit);

            sum = sum + digit;

            if (i-1>=0) {
                sum = sum + number[i - 1];

//                System.out.println(number[i-1]);
            }
        }

        sum = sum*9;
        sum = sum % 10;



        return sum==number[number.length-1];

    }

    private static int compute(int x, int y)
    {
        int sum = x+y;

        String sumStr = String.valueOf(sum);
        if (sumStr.length()==1)
            return sum;

        sum = Integer.valueOf(sumStr.substring(0,1)) + Integer.valueOf(sumStr.substring(1,2));
        return sum;

    }


    public static String FPEencrypt(String pan, byte[] key , byte[] tweak)
    {
        return FPE.encrypt(pan,key,tweak);
    }


    public static String FPEdecrypt(String fpeToken, byte[] key, byte[] tweak)
    {
        return FPE.decrypt(fpeToken,key,tweak);
    }



}
