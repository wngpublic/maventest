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

Topics:
- distributed tracing
- service discovery
- service registration
- pub/sub
- WAL
- distributed KV mem
- consistent hashing
- reverse proxy
- load distributor
- json parser
- sharding
- replication
- distributed queueing
- priority scheduler
- distributed scheduling
- streaming data
- windowing
- graph
- push and pull model
- serialization/deserialization
- serial collector
- adaptive rate streaming tcp
