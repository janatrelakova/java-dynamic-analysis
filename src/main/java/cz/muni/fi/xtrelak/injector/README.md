# injector
Use the injector as a Java Agent. The analysis statements will be injected to the bytecode of the given jar file
and written to `invoked-calls.txt` file. The standard output should not be affected.

## Usage
1. Run with targeted packages:
    ```
    java -javaagent=injector.jar=<package1>,<package2> -jar <path-to-analyzed-jar>
    ```
    The statement will be executed only if current method belongs to the set of specified packages.
    
    The origin of the call of the method does not have an effect whether the Advice will be applied or not
    (the Advice is called whether the method is called by other method specified in the package and will be applied
    if called from other package, like system package for example, too).
2. Run without target packages:
    ```
    java -javaagent=injector.jar -jar <path-to-analyzed-jar>
    ```
    The statement will be executed for all methods.
    Be aware this could invoke a load of calls that are being written to the `invoked-calls.txt` file.
   

## Logs
Logs of injector (not the analyzed )are collected to the `dynamic-analysis.log` file.
