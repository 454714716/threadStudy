package sync;

import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Created by 13 on 2017/5/4.
 */
public class ThreadGroupName implements Runnable {
    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        String groupAndName = Thread.currentThread().getThreadGroup().getName() + "-" + Thread.currentThread().getName();
        while (true) {
            System.out.println("I am " + groupAndName);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

 /*   public static void main(String args[]) {
        ThreadGroup tg = new ThreadGroup("PrintGroup");
        Thread t1 = new Thread(tg, new ThreadGroupName(), "T1");
        Thread t2 = new Thread(tg, new ThreadGroupName(), "T2");
        t1.start();
        t2.start();
        System.out.println(tg.activeCount());
        Thread t3 = new Thread(tg, new ThreadGroupName(), "T3");
        t3.start();
        System.out.println(tg.activeCount());
        tg.list();
    }
*/


        public static void main(String[] args) {
            //对在AtomicReference上面，通过计数的方式，解决了ABA的问题
            B b = new B();
            b.setStr("old b");
            AtomicStampedReference<B> aAtomicStampedReference = new AtomicStampedReference<B>(b,0);
            System.out.println(aAtomicStampedReference.getReference().getStr()+"-----" +aAtomicStampedReference.getStamp());

            //更改里面的b的值
            b.setStr("new b");
            //设置预期的类和现在的类，预期的版本号与现在的版本号，如果预期与当前的符合就更改。
            aAtomicStampedReference.compareAndSet(b,b,0,1);

            System.out.println(aAtomicStampedReference.getReference().getStr()+"-----" +aAtomicStampedReference.getStamp());

            //===========================================================
            B b1 = new B();
            b1.setStr("old b1");
            AtomicMarkableReference<B> atomicMarkableReference = new AtomicMarkableReference<>(b1,false);
            System.out.println(atomicMarkableReference.getReference().getStr()+"-----"+atomicMarkableReference.isMarked());
            b1.setStr("new b1");
            atomicMarkableReference.compareAndSet(b1,b1,false,true);
            System.out.println(atomicMarkableReference.getReference().getStr()+"-----"+atomicMarkableReference.isMarked());


        }
        public static class B{
        public String str;
        public void setStr(String str){
            this.str = str;
            }
            public String getStr(){
            return str;
            }

        }
}
