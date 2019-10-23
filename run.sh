java -ea -cp target/MavenTest-1.0-SNAPSHOT.jar -Dlog4j.configurationFile=src/main/resources/log4j2.xml -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 org.wayne.main.Main console
