package org.wayne.main;

import java.io.Console;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommandProcessor {
    final int ID;
    Map<String, Map<String, MethodDescription>> mapMethods = new HashMap<>();
    Map<String, MyBasic> mapClassInstances = new HashMap<>();
    Map<Integer, IndexMethods> mapEnum = new HashMap<>();
    Set<String> setPrimitives = new HashSet<>(
        Arrays.asList("int","Integer","String","char","Character","double","Double","boolean","Boolean"));

    public CommandProcessor(List<Class> listClass) {
        ID = 1234;
        List<Class> listMyBasic = listClass.stream()
            .filter(c -> MyBasic.class.isAssignableFrom(c))
            .collect(Collectors.toList());
        populateAndInstance(listMyBasic);
    }

    public int getID() {
        return ID;
    }

    void populateAndInstance(List<Class> list) {
        Function<Class,String> getClassSuffix = x -> {
            if(x == null) return null;
            String [] hierarchy = x.getName().split("\\.");
            return hierarchy[hierarchy.length-1];
        };
        AtomicInteger atomicIntClass = new AtomicInteger(0);
        list.stream().forEach(c -> {
            try {
                String className = c.getName();
                MyBasic myBasic = MyBasic.class.cast(c.newInstance());
                mapClassInstances.put(className, myBasic);
                AtomicInteger atomicIntMethods = new AtomicInteger(0);
                Map<String,MethodDescription> mapMethodNames = new TreeMap<>();
                mapMethods.put(c.getName(), mapMethodNames);
                Map<Integer,MethodDescription> mapIndexMethod = new TreeMap<>();
                IndexMethods indexMethods = new IndexMethods(className, myBasic, mapIndexMethod);
                mapEnum.put(atomicIntClass.getAndIncrement(), indexMethods);
                Arrays.asList(c.getDeclaredMethods()).stream()
                    .filter(m -> (m.getName().startsWith("test") || m.getName().startsWith("cmd"))&& !m.getName().contains("$"))
                    .forEach(m -> {
                        Class<?> [] params = m.getParameterTypes();
                        List<String> listParams = null;
                        StringBuilder sb = new StringBuilder();
                        sb.append(String.format("%-50s ", c.getName() + "." + m.getName()));
                        String returnType = getClassSuffix.apply(m.getReturnType());
                        sb.append(String.format("return: %-10s ", returnType));
                        if(params != null && params.length != 0) {
                            listParams = new ArrayList<>();
                            sb.append("args: ");
                            for(Class<?> clazz: params) {
                                String classSuffix = getClassSuffix.apply(clazz);
                                listParams.add(classSuffix);
                                sb.append(String.format("%s ", classSuffix));
                            }
                        }
                        String description = sb.toString();
                        MethodDescription methodDescription = new
                            MethodDescription(m.getName(), m, listParams, returnType, description);
                        mapMethodNames.put(m.getName(), methodDescription);
                        mapIndexMethod.put(atomicIntMethods.getAndIncrement(), methodDescription);
                    });
            } catch (Exception e) { e.printStackTrace(); }
        });
    }


    static void p(String f, Object ...o) { System.out.printf(f, o); }

    void shutdown() {
        mapClassInstances.values().stream().forEach(x -> x.shutdown());
    }

    public void start(String [] args) {
        long timebegin = System.nanoTime();
        try {
            if(args.length > 0) {
                if("console".equals(args[0])) {
                    consoleLoop();
                    shutdown();
                }
                else if("server".equals(args[0])) {
                    if(args.length == 2) {
                        int portnum = Integer.valueOf(args[1]);
                        //MyNetwork.class.cast(mapClassInstances.get("MyNetwork")).cmdStartListenThreadPool();
                    }
                }
                else if("serverkv".equals(args[0])) {
                    if(args.length == 2) {
                        int portnum = Integer.valueOf(args[1]);
                        //MyNetwork.class.cast(mapClassInstances.get("MyNetworkHashMap")).cmdStartListenThreadPool();
                    }
                }
            } else {
                test(args);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        long timeend = System.nanoTime();
        long timediff = timeend - timebegin;
        long timediffmilli = timediff / 1000000;
        timeend = System.currentTimeMillis();
    }

    void consoleLoop() throws InvocationTargetException, IllegalAccessException {
        Console console = System.console();
        if(console == null) return;
        boolean quit = false;
        while(!quit) {
            ConsoleCommandBasic commandBasic = null;
            try {
                String cmd = console.readLine("Enter cmd: ");
                commandBasic = parseConsoleCommand(cmd);
                if(commandBasic == null) { continue; }
                if(commandBasic.consoleCommands == ConsoleCommands.quit) {
                    quit = true; break;
                }
            } catch(Exception e) {

            }
            try {
                if(commandBasic != null) processConsoleCommands(commandBasic);
            } catch(Exception e) {

            }
        }
        //p("quit\n");
    }

    void processConsoleCommands(ConsoleCommandBasic cmd) throws InvocationTargetException, IllegalAccessException {
        if(cmd.consoleCommands == ConsoleCommands.invalid) {
            p("command is invalid: %s\n", cmd.cmd);
        }
        else if(cmd.consoleCommands == ConsoleCommands.unknown) {
            p("command is unknown: %s\n", cmd.cmd);
        }
        else if(cmd.consoleCommands == ConsoleCommands.help) {
            ConsoleCommandHelp cmdHelp = ConsoleCommandHelp.class.cast(cmd);
            if(cmdHelp.helpArg == null) {
                printHelpCommands();
            }
            else {
                printMethods(cmdHelp.helpArg);
            }
        }
        else if(cmd.consoleCommands == ConsoleCommands.run) {
            ConsoleCommandRun cmdRun = ConsoleCommandRun.class.cast(cmd);
            runCommand(cmdRun);
        }
        else {
            p("command is invalid: %s\n", cmd.cmd);
        }
    }

    ConsoleCommandBasic parseConsoleCommand(String cmdString) {
        if(cmdString == null || cmdString.length() == 0) return null;
        if("q".equals(cmdString) || "quit".equals(cmdString))
            return new ConsoleCommandBasic(ConsoleCommands.quit, cmdString,null);
        LinkedList<String> args = new LinkedList<>(Arrays.asList(cmdString.split("\\s+")));
        String cmd = args.poll();

        if("help".equals(cmd) || "h".equals(cmd)) {
            String helpArg = args.size() == 0 ? null : args.poll();
            return new ConsoleCommandHelp(ConsoleCommands.help, cmdString, args, helpArg);
        }
        if("run".equals(cmd) || "r".equals(cmd)) {
            if(args.size() == 0) {
                return new ConsoleCommandBasic(ConsoleCommands.unknown, cmdString, args);
            }
            LinkedList<String> funcPath = new LinkedList<>(Arrays.asList(args.poll().split("\\.")));
            if(funcPath.size() == 2) {
                String className = funcPath.poll();
                String methodName = funcPath.poll();
                return genRunConsoleCommand(cmdString, args, null, className, methodName);
            }
            if(funcPath.size() == 3) {
                String pathName = funcPath.poll();
                String className = funcPath.poll();
                String methodName = funcPath.poll();
                return genRunConsoleCommand(cmdString, args, pathName, className, methodName);
            }
            return new ConsoleCommandBasic(ConsoleCommands.unknown, cmdString, args);
        }
        if("exe".equals(cmd) || "e".equals(cmd)) {
            return parseArgs2ClassMethod(args, cmdString);
        }
        return new ConsoleCommandBasic(ConsoleCommands.unknown, cmdString, args);
    }

    ConsoleCommandBasic parseArgs2ClassMethod(LinkedList<String> args, String cmdString) {
        if(args.size() < 2) {
            return new ConsoleCommandBasic(ConsoleCommands.unknown, cmdString, args);
        }
        Integer intClassName = Integer.parseInt(args.poll());
        Integer intMethodName = Integer.parseInt(args.poll());
        IndexMethods indexMethods = mapEnum.get(intClassName);
        if(indexMethods == null) {
            return new ConsoleCommandBasic(ConsoleCommands.unknown, cmdString, args);
        }
        String className = indexMethods.getClassName();
        MethodDescription methodDescription = indexMethods.getMap().get(intMethodName);
        if(methodDescription == null) {
            return new ConsoleCommandBasic(ConsoleCommands.unknown, cmdString, args);
        }
        String methodName = methodDescription.name;
        return genRunConsoleCommand(cmdString, args, null, className, methodName);
    }
    ConsoleCommandRun genRunConsoleCommand(
        String cmdStr,
        List<String> args,
        String pathName,
        String className,
        String methodName) {
        Map<String,MethodDescription> mapMethod = mapMethods.get(className);
        if(mapMethod == null) {
            p("invalid run cmd: %s\n", className);
            return null;
        }
        MethodDescription methodDescription = mapMethod.get(methodName);
        if(methodDescription == null) {
            p("invalid run cmd: %s %s\n", className, methodName);
            return null;
        }
        Deque<Object> resultMethodArgs = parseMethodArgs(methodDescription, args);
        if(resultMethodArgs == null || resultMethodArgs.size() < 1) return null;
        boolean isValidResult = (resultMethodArgs.peek() instanceof Boolean) ?
            Boolean.class.cast(resultMethodArgs.poll()).booleanValue() : false;
        if(!isValidResult) return null;
        Object [] params = resultMethodArgs.toArray();
        return new ConsoleCommandRun(
            ConsoleCommands.run, cmdStr, args, params, pathName, className, methodName);
    }

    Deque<Object> parseMethodArgs(MethodDescription desc, List<String> args) {
        Deque<Object> q = new ArrayDeque<>();
        List<String> referenceParams = desc.listParams;
        if(referenceParams == null) {
            q.push(Boolean.TRUE);
        }
        else if(referenceParams.size() != args.size()){
            q.push(Boolean.FALSE);
        }
        else {
            // does not support array parsing, only primitives
            Boolean isValid = true;
            try {
                for(int i = 0 ; i < args.size(); i++) {
                    String s = args.get(i);
                    if(!s.matches("([A-Za-z0-9-_]+)")) {
                        isValid = false; break;
                    }
                    String strCastType = referenceParams.get(i);
                    if(!setPrimitives.contains(strCastType)) {
                        isValid = false; break;
                    }
                    switch(strCastType) {
                        case "int": case "Integer":
                            q.add(Integer.parseInt(s)); break;
                        case "char": case "Character": case "String":
                            q.add(s); break;
                        case "boolean": case "Boolean":
                            q.add(Boolean.parseBoolean(s)); break;
                        case "double": case "Double":
                            q.add(Double.parseDouble(s)); break;
                        default: isValid = false; break;
                    }
                }
            } catch(Exception e) {
                isValid = false;
                e.printStackTrace();
            }
            q.push(isValid);
        }
        return q;
    }

    void runCommand(ConsoleCommandRun cmd) throws InvocationTargetException, IllegalAccessException {
        Map<String,MethodDescription> mapMethod = mapMethods.get(cmd.className);
        MethodDescription methodDescription = mapMethod.get(cmd.methodName);
        p("Running %s.%s\n", cmd.className, cmd.methodName);
        Object instance = mapClassInstances.get(cmd.className);
        long timebeg = System.currentTimeMillis();
        methodDescription.m.invoke(instance, cmd.params);
        long timeend = System.currentTimeMillis();
        long diff = timeend - timebeg;
        p("time to complete: %d ms\n", diff);
    }

    void printMethods(String arg) {
        boolean validClassName = arg != null;
        boolean isNumericArg = false;
        int idx = 0;
        if(validClassName) {
            if(!mapClassInstances.containsKey(arg)) {
                try {
                    idx = Integer.parseInt(arg);
                    if(mapEnum.containsKey(idx)) { isNumericArg = true; }
                    else { validClassName = false; }
                } catch(Exception e) {}
            }
        }
        if(validClassName) {
            if(isNumericArg) {
                mapEnum.get(idx).getMap().entrySet().stream().forEach(m ->
                    p("(%2d) %s\n", m.getKey(), m.getValue().getDescription()));
            } else {
                mapMethods.get(arg).entrySet().stream().forEach(m -> p("%s\n", m.getValue().getDescription()));
            }
        }
        else {
            mapMethods.entrySet().stream().forEach(
                c -> c.getValue().entrySet().stream().forEach(
                    m -> p("%s\n", m.getValue().getDescription())));
        }
    }

    void printHelpCommands() {
        p("(r)un path.class.method || (e)xe classNum methodNum || (h)elp classNum\n");
        p("help cmd\n");
        for(Map.Entry<Integer,IndexMethods> kv: mapEnum.entrySet()) {
            Integer i = kv.getKey();
            IndexMethods indexMethods = kv.getValue();
            p("help (%d) %s\n", i, indexMethods.className);
        }
        /*
        for(String className: mapClassInstances.keySet()) {
            p("help %s\n", className);
        }
        */
    }

    void test(String [] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    }

}

class ConsoleCommands {
    static int ctr = 0;
    public static final Integer quit = ctr++;
    public static final Integer invalid = ctr++;
    public static final Integer unknown = ctr++;
    public static final Integer help = ctr++;
    public static final Integer commands = ctr++;
    public static final Integer run = ctr++;
}

class ConsoleCommandBasic {
    final public Integer consoleCommands;
    final public String cmd;
    final public List<String> args;
    public ConsoleCommandBasic(Integer consoleCommands, String cmd, List<String> args) {
        this.consoleCommands = consoleCommands;
        this.cmd = cmd;
        this.args = args;
    }
}

class ConsoleCommandHelp extends ConsoleCommandBasic {
    String helpArg = null;
    public ConsoleCommandHelp(
            Integer consoleCommands,
            String cmd,
            List<String> args,
            String helpArg) {
        super(consoleCommands, cmd, args);
        this.helpArg = helpArg;
    }
}

class ConsoleCommandRun extends ConsoleCommandBasic {
    final public String pathName;
    final public String className;
    final public String methodName;
    final public Object [] params;
    public ConsoleCommandRun(
            Integer consoleCommands,
            String cmd,
            List<String> args,
            Object [] params,
            String pathName,
            String className,
            String methodName) {
        super(consoleCommands, cmd, args);
        this.pathName = pathName;
        this.methodName = methodName;
        this.className = className;
        this.params = params;
    }
    public Object [] getParams() { return params; }
}

class MethodDescription {
    final Method m;
    final String returnType;
    final List<String> listParams;
    final String description;
    final String name;
    public MethodDescription(String name, Method m, List<String> listParams, String returnType, String description) {
        this.name = name;
        this.m = m;
        this.listParams = (listParams == null || listParams.size() == 0) ? null : listParams;
        this.returnType = returnType;
        this.description = description;
    }
    public Method getM() { return m; }
    public String getReturnType() { return returnType; }
    public List<String> getListParams() { return listParams; }
    public String getDescription() { return description; }
    public String getName() { return name; }
}

class IndexMethods {
    final MyBasic myBasic;
    final String className;
    final Map<Integer, MethodDescription> map;
    public IndexMethods(String className, MyBasic myBasic, Map<Integer, MethodDescription> map) {
        this.className = className;
        this.myBasic = myBasic;
        this.map = map;
    }
    public String getClassName() { return className; }
    public MyBasic getInstance() { return myBasic; }
    public Map<Integer, MethodDescription> getMap() { return map; }
}
