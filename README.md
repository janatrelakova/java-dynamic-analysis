# Java Dynamic Analysis
This initiative consist of three parts:
1. **scraper**
   1. Targeted on Spring projects.
   2. Go through the source code of the Java program and extract the information about exposed endpoints.
   3. Uses spring annotations like `@RequestMapping`, `@GetMapping`, `PathVariable`.
   4. Result is a list of endpoints with their HTTP methods and parameters (format accepted by the invoker).
   5. see the [scraper](src/main/java/cz/muni/fi/xtrelak/scraper/README.md) for more information.
2. **injector**
   1. Works on java bytecode level.
   2. Injects printing statement to the beginning of each method providing information about how many times and who called the application.
   3. Running as a Java agent.
   4. see the [injector](src/main/java/cz/muni/fi/xtrelak/injector/README.md) for more information.
3. **invoker**
  - The invoker is a tool that sends requests to the exposed endpoints of the target application.
  - The implementation is written in Python.
  - see the invoker in this [GitHub repository](https://github.com/janatrelakova/invoker).

## Proposed flow
1. Run the scraper on the target project to easily identify endpoint to call to be able to collect dynamic data in the next steps.
   1. Run `java -jar scraper.jar <path-to-analyzed-project>` to get the list of endpoints.
2. Run your project _jar_ with the injector as a java agent.
   1. Run `java -javaagent=injector.jar -jar <path-to-analyzed-jar>` to inject the printing statements to the bytecode of the given jar file.
3. There are two approaches how to collect the dynamic calls:
   1. Use the injected _jar_ and use it as you usually do. This approach is the best possible as it will collect the calls based on the real usage of the application.
   2. Use the invoker to send requests to the exposed endpoints. The invoker will create an artificial load for your application.

