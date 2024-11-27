package cz.muni.fi.xtrelak.injector;

import net.bytebuddy.asm.Advice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InjectAdvice {
    private static final Logger logger = LogManager.getLogger(InjectAdvice.class);

    @Advice.OnMethodEnter
    public static void onEnter(@Advice.Origin("#m") String methodName, @Advice.Origin("#t") String className) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 3) {
            // Caller should be on the third position, but we have altered the stack
            // by calling `Thread.currentThread().getStackTrace();`
            StackTraceElement caller = stackTrace[2];
            Writer.writeToFile(caller.getClassName() + "." + caller.getMethodName() + " -> " + className + "." + methodName);
        } else {
            logger.info("Method: {} has no caller information available.", methodName);
        }
    }
}
