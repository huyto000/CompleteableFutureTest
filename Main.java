package com.example.DemoJenkins.model;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("Run a task specified by a Runnable Object asynchronously.");
        long start1 = System.currentTimeMillis();
        CompletableFuture<String> futureString = getStringCompletablefuture();
        CompletableFuture<Integer> futureInteger = getIntegerCompletablefuture();

//        CompletableFuture<String> completeablefutureCombined = futureString.thenCombine(
//                futureInteger,
//                (string, integer) -> "This is String " + string + " and Integer " + integer
//        );

//        CompletableFuture<String> completeablefutureCombined = CompletableFuture.allOf(futureInteger, futureString)
//                        .thenApply(x -> {
//                         Integer integer = futureInteger.join();
//                         String string = futureString.join();
//                         return "This is String " + string + " and Integer " + integer;
//                        });
        List<CompletableFuture<String>> completableFutures = toListCompleteAbleFutureString();
        CompletableFuture<Void> completeablefutureCombined = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));

        CompletableFuture<List<String>> allCompletableFuture = completeablefutureCombined.thenApply(future -> {
            return completableFutures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
        });
        System.out.println("It is also running... ");
        // Block and wait for the future to complete
//        System.out.println("Result: " + completeablefutureCombined.get());
       System.out.println("Result: " + allCompletableFuture.get());
        long end1 = System.currentTimeMillis();
        System.out.println("Elapsed Time in milli seconds: "+ (end1-start1));
        System.out.println("Done!!!");
    }

    private static CompletableFuture<String> getStringCompletablefuture() {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("String completablefuture");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Hello";
        });
    }

    private static CompletableFuture<Integer> getIntegerCompletablefuture() {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Integer completablefuture");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return 100;
        });
    }

    private static List<CompletableFuture<String>> toListCompleteAbleFutureString(){
        List<String> strings = List.of("1", "2", "3");
        return strings.stream()
                .map(s -> CompletableFuture.supplyAsync(() -> concatString(s)))
                .collect(Collectors.toList());
     }

     private static String concatString(String s){
         try {
             TimeUnit.SECONDS.sleep(3);
         } catch (InterruptedException e) {
             throw new IllegalStateException(e);
         }
         return s.concat("string");
     }


}
