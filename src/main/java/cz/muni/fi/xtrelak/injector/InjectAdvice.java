package cz.muni.fi.xtrelak.injector;

import net.bytebuddy.asm.Advice;

import java.util.Arrays;


public class InjectAdvice {
    @Advice.OnMethodEnter
    public static void onEnter(@Advice.Origin("#m") String methodName, @Advice.Origin("#t") String className) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 3) {
            // Caller should be on the third position, but we have altered the stack
            // by calling `Thread.currentThread().getStackTrace();`
            StackTraceElement caller = stackTrace[2];
            System.out.println(caller.getClassName() + "." + caller.getMethodName() + " -> " + className + "." + methodName);
        } else {
            System.out.println("Method: " + methodName + " has no caller information available.");
        }
    }
}
