package client;

import data.Transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class TransactionTester {

    static final String[] merchants = {"Merchant1", "Merchant2" , "Merchant3" };

    static ThreadLocalRandom random = ThreadLocalRandom.current();

    public static Transaction create()
    {
        String merchant = merchants[(int)(System.currentTimeMillis()%merchants.length)];

        String transactionTime = LocalDateTime.now().minusDays(random.nextInt(60)).toString();

        double amount = random.nextDouble(500.55);

        Transaction transaction = new Transaction(merchant,amount,transactionTime);

        return transaction;

    }


    public static List<Transaction> sort(List<Transaction> list , Comparator<Transaction> comparator)
    {
        return list.stream().sorted(comparator).collect(Collectors.toList());

    }


    public static void main(String[] args) {

        List<Transaction> list = new ArrayList<>();
        for (int i=0;i<5;i++)
        {
            list.add(create());
        }



        List<Transaction>  sortedByAmount = sort(list,Comparator.comparingDouble(Transaction::getAmount));

        List<Transaction>  sortedByAmountDesc = sort(list,Comparator.comparingDouble(Transaction::getAmount).reversed());


        Comparator<Transaction> merchantSorter = (Transaction t1,Transaction t2)->{

            String s1 = t1.getMerchantId();

            String s2 = t2.getMerchantId();

            return s1.compareTo(s2);


        };


        Comparator<Transaction> dateSorter = (Transaction t1,Transaction t2)->{

            String s1 = t1.getTransactionTime();

            String s2 = t2.getTransactionTime();

            LocalDateTime ldt1  = LocalDateTime.parse(s1);

            LocalDateTime ldt2 = LocalDateTime.parse(s2);



            return ldt1.compareTo(ldt2);


        };

        Comparator<Transaction> merchantSorterReversed = merchantSorter.reversed();

        Comparator<Transaction> merchantDateThenAmount = merchantSorter.thenComparing(dateSorter).thenComparingDouble(Transaction::getAmount);
    //    Comparator<Transaction> merchantDateThenAmount = merchantSorter.thenComparingDouble(Transaction::getAmount).thenComparing(dateSorter);


        List<Transaction>  sortedByMerchant = sort(list,merchantSorter);

        List<Transaction>  sortedByMerchantReversed = sort(list,merchantSorterReversed);

        List<Transaction>  sortedByMerchantThenAmount = sort(list,merchantDateThenAmount);


       // System.out.println(sortedByAmount);

       // System.out.println(sortedByAmountDesc);


        System.out.println(sortedByMerchantThenAmount);




    }
}
