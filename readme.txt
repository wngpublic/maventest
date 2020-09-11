javac -d bin -cp src src/org/dir/main/Main.java 
java -cp bin org.wayne.main.Main
jar -cfm SizeOf.jar src/main/java/org/wayne/java/SizeOf.class src/main/resources/META-INF/MANIFEST.MF
jar tvf jarfile      // read contents of jar

mvn compile
mvn clean install
mvn test
mvn test -Dtest=AppTest
mvn test -Dtest=Testcases
mvn clean package -DskipTests
mvn package -DskipTests

-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005

java -cp target/MavenTest-1.0-SNAPSHOT.jar org.wayne.main.Main <testcasename>

in eclipse/intellij, vm args (not program args) should have -ea flag for assertions

to include files, add into resources directory

jar tf jarfile

git init
git status
git add dir1 dir2 file3 file4
git add -A   // stage all
git add -u   // stage all modified and deleted without new
git commit -m "message 1"
git status
curl https://api.github.com/user/repos -d '{"name":"maventest"}' \
    -u '<username>' -H "Authorization: token <2fa>"
git remote add origin https://github.com/<username>/maventest
git push https://<username>@github.com/<username>/maventest.git

- jdwp
-- jdwp: java debug wire protocol
    transport
    dt_socket
-- jvmdi java virtual machine debugging interface
-- jpda java platform debugger architecture
-- jdb java debugger
-- basic
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000 App
-- use local address whenever possible and do ssh tunnel
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=127.0.0.1:8000 App
-- use jdb --attach 127.0.0.1:8000

- jdb 
-- the program should be started with:
    -agentlib:jdwp=transport=dt_shmem,server=y,suspend=n
    -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=127.0.0.1:8000 App
-- then on jdb, do attach
    jdb --attach <addres>
    jdb --attach 127.0.0.1:8000   // App is not specified because jdb connecting to existing VM
    jdb -attach <port>
    jdb -attach <address>:<port>  // jdb -attach 127.0.0.1:8000
    jdb MyClass                   // start new process MyClass with jdb
    jdb -listen <address>:<port>  // wait for running VM to connect at this addr
    jdb -sourcepath <path_end_slash/> -attach ip:port  // make sure path/ else not recursive!
    jdb -sourcepath path1/:path2/path3/ -attach ip:port  // make sure path/ else not recursive!
    jdb -sourcepath path/ -classpath target -attach ip:port // what is classpath?
-- commands
    jdb -help
    stop in ClassName.<init>      // breakpoint at constructor
    stop in ClassName.method(fullyQualifiedArgs)  // breakpoint at method name
      stop in CM.mN(java.lang.String,java.lang.String)
    stop in a.b.c.ClassName.methodName  // stop at that method
    stop at ClassName:<line>      // stop at is for linenum
      stop at ClassName:7
    catch java.lang.Exception     // catch all exception
    clear ClassName:<line>        // clear breakpoint
    clear                         // list of existing breakpoints
    run <classname>
    run <classname> <args>
    list                          // list 4 lines before and 5 lines after current line
    list <line-num>               // list 10 lines surrounding line-num
    list <method-name>            // list first 10 lines of method
      if Source file not found:
        
    step                          // step into method
    step into                     // step into method
    step up                       // step out of method
    step out                      // step out of method
    step over                     // same as step, step over a function
    pop                           // pop stack, when to use? 
    next                          // step over method
    cont
    wherei                        // print current stack frame
    where                         // dump stack of current thread
    System.out.println(objectName.instancevar);
    eval objectName.instancevar   // print out the var, same as print
    print x                       // print out the var, same as eval
    dump x                        // print out the var, same as print, maybe more complex
    watch classname.fieldname
    unwatch classname.fieldname
    locals                        // print values of all local vars
    set variable = value          // set value
    threads                       // list the threads running
    thread X command
    thread <num>                  // go to thread num
    exit
    quit
    up <n frames>                 // move up stack
    down <n frames>               // move down stack
    where <opt thread id>         // where in stack
    wherei <opt thread id>        // where in stack with pc
    javac -g *.java               // all code must be compiled with g flag for jdb
    !!                            // repeat last command


jmx       -Dcom.sun.management.jmxremote
jcmd      jcmd ManagementAgent.start jdp.port=<port>
attach
jinfo
jstatd
java flight recorder
jvisualvm
jmap
jstack
jps
jstat
jmc       java mission control
jmap

jcmd -> attach -> jvm attach <-> dcmd <-> monitored jvm <-> jmx client
                  jvmstat     -> jstat -> jstat client rmi



