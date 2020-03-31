//Main class



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestBarrier {
  //We will spawn 5 threads
  public static final int THREAD_COUNT = 5;
  
  /**
  * @param args
  * @throws IOException
  */
  public static void main(String[] args) throws IOException {
     // TODO Auto-generated method stub
     System.out.println("Proper output is that we should see is that all threads");
       System.out.println("output an 'A' before reaching the barrier and then a 'B'");
       System.out.println("after proceeding through the barrier. Therefore, output");
       System.out.println("should appear as a series of 'A's followed by an equal count");
       System.out.println("of 'B's. (There should not be an intermingling of 'A's and 'B's.");
       System.out.println("\n\nPress Enter to begin the test:");
       (new BufferedReader(new InputStreamReader(System.in))).read();
       if(args.length < 1){
         throw new IOException("not enterd barrier");
       }
       int barrier = 0 ;
       try{
         barrier = Integer.parseInt(args[0]);  
       }
       catch(Exception e){
         
       }
       BarrierImpl barrierImp = new BarrierImpl(THREAD_COUNT);
       barrierImp.initBarrier(barrier);
       // initialize the barrier to the number of threads

       Thread[] workers = new Thread[THREAD_COUNT];
       for (int i = 0; i < workers.length; i++) {
             workers[i] = new Thread(new Worker(barrierImp,i));
             workers[i].start();
       }
       try {
             for (int i = 0; i < workers.length; i++)
                  workers[i].join();
       }
       catch (InterruptedException ie) { }
  }
}


//Barrier Class



/**
*
* 
* This class is used to implement Barrier Logic
* Initialize barrier
* Check Barrier limit reached
* Move Thread to sleep if it reaches Barrier limit and others not yet
* Wkae up all threads when all threads reach barrier point
*/
  class BarrierImpl {
  //Variable to store Barrier
  private int barrier;
  //Variable to check how many number of Threads reached Barrier point
  private int countBarrierCrossedTheads =0;
  //Variable to strore number of threads
  private int numThreads = 0;
  /**
    * This is Object on which threads will be synchronized
    * Thread will go to Wait state on this object
    * Once all threads reach Barrier point, All Threads will be notified
    */
  private Object waitObject;
  /*
  * Constructor will take 2 arguements  
  * Arg-2 ==> Barrier limit
  */
   public BarrierImpl(int numThreads){
     this.numThreads = numThreads;
   }
   //Function to initialize Barrier
   public void initBarrier(int barrier){
       this.barrier = barrier;  
      
       waitObject = new Object();
   }
   /**
    * Function to check whether barrier is reached.
    * This will be called by each thread.
    * Thread will go to wait state if Barrier is reached and others not
    */
   public boolean checkBarrierReached(int threadId,int curValue){
       //check barrier reached
     if(curValue == barrier){
        //Current Thread reached barrier so increment count
        countBarrierCrossedTheads++;
        /**
        * Current Thread reached barrier so check whether all others readched or not
        */
        if(countBarrierCrossedTheads != numThreads){
           System.out.println("Thread "+threadId+" Reached Barrrie and all others not sso wait ");
           //All threds not treached barrier, so wait  
           try {
              synchronized(waitObject){
                  waitObject.wait();
              }
           } catch (InterruptedException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
           }
        }
        else if(countBarrierCrossedTheads == numThreads){
           System.out.println("All Threads reached barrier, notify all");
           synchronized(waitObject){
               waitObject.notifyAll();
           }
        }
        return true;
     }
     //Thread already reached barrie
     else if(curValue > barrier){
         return true;  
     }
     else{
        return false;
     }
   }
}

//Explanation:
//﻿Worker Thread Class





  class Worker implements Runnable{
   //Threads current barrier
  private int currentBarrier;
  //Object of BarrierImpl
  BarrierImpl barrier;
  //Id for Thread , this is just for identification only
  int id;
   public Worker(BarrierImpl barrier,int id){
       this.barrier =  barrier;
       this.id = id;
   }
  @Override
  public void run() {
     // TODO Auto-generated method stub
     //Run infinetly
     while(true){
        //increment barrier
        currentBarrier++;  
        try {
           Thread.sleep(id*100);
        } catch (InterruptedException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
        }
        //Check Barrier reached
        /**
        * IF Barrier reached Print "B"
        * Else Print "A"
        * IF currentBarrier < Barrier limit, false will be returned
        * IF currentBarrier = Barrier limit, if other threds not reached then excution will be sleep here
        * IF currentBarrier > Barrier limit "true" will be retuned
        */
        if(barrier.checkBarrierReached(id, currentBarrier)){
           System.out.println("B  ==> "+id);
        }else
        {
           System.out.println("A ==> "+id);  
        }
     }
  }
  
}