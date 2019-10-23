Runnable vs Thread
Callable vs Runnable
Daemon
Serialization methods:
ReentrantLock(), Lock.lock(), Lock.unlock(), Object.wait(), and Object.notify()
ExecutorPool
ExecutorService
    submit(Runnable/Thread)
    shutdown()
    execute(Runnable)
    Future<X> submit(Callable)
ExecutorService = Executors.newFixedThreadPool(size)
Condition = ReentrantLock.newCondition()
    await(time, timeunit)
    signalAll()
Semaphore(num)
    acquire(1)
    acquire()
    release()
    try { semaphore.acquire(); } finally { semaphore.release(); }
Lock = new ReentrantLock()
    lock()
    finally
        unlock()
    newCondition()
Object
    notifyAll()  // must be in synchronized block
    notify()     // must be in synchronized block
    wait()       // must be in synchronized block
    catch(InterruptedException)
    var = new Object
    synchronized(var)
Future, FutureTask
    get()
Callable<T>
    T call()
        return T
Runnable
    run()
AtomicVariable
    compareAndSet(old, new)
        
    