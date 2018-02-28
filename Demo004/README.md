Demo004 ReentrantLock的使用
========
* **app:** ReentrantLock同步
* **app1:** ReentrantLock的等待通知 signal、await
* **app2:** 共享量变的线程不安全
* **app3:** 异常锁释放 (抛出异常线程结束)
* **app4:** synchronized 代码块同步，一些异步，一些同步
* **app5:** synchronized static  class  object  死锁一个
* **app6:** 静态内部类
* **app7:** volatile 使变量在多个线程间可见


* **NOTE:** 
> 方法内的变量是线程安全的  
> 实例变量是非线程安全的  
> synchronized不具有继承性  
> 提高效率尽量缩小synchronized的范围 
> String常量池,指向同一对象

