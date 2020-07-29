package com.idealista.fpe.algorithm.ff1;


import com.idealista.fpe.algorithm.ff1.FF1Algorithm;
import com.idealista.fpe.component.functions.prf.DefaultPseudoRandomFunction;

import java.security.SecureRandom;


public class FPE {



    public static String encrypt(String pan, byte[] key , byte[] tweak)
    {

        Integer radix = 10;

       int[] input = StringToIntArray(pan);

        int[] cipherText = FF1Algorithm.encrypt(input, radix, tweak, new DefaultPseudoRandomFunction(key));

        return IntArrayToString(cipherText);

    }

    private static int[] StringToIntArray(String str)
    {
        int[] input = new int[str.length()];

        for (int i=0;i<input.length;i++)
        {
            String s = str.substring(i,i+1);
            input[i] =  Integer.parseInt(s);

        }

        return input;
    }
    private static String IntArrayToString(int[] arr)
    {
        StringBuilder builder = new StringBuilder();

        for (int i=0;i<arr.length;i++)
        {
            builder.append(arr[i]);
        }

        return builder.toString();

    }

    public static String decrypt(String fpeToken, byte[] key, byte[] tweak)
    {
        int[] input = StringToIntArray(fpeToken);

        Integer radix = 10;


        int[] decText = FF1Algorithm.decrypt(input, radix, tweak, new DefaultPseudoRandomFunction(key));

        return IntArrayToString(decText);

    }

    public static void main(String[] args) {


        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32];


        byte[] tweak = new byte[10];


        secureRandom.nextBytes(key);


        secureRandom.nextBytes(tweak);


        String cipherText = encrypt("4111222233334444",key,tweak);

        String newText = decrypt(cipherText,key,tweak);



        System.out.println(newText);



    }



}

