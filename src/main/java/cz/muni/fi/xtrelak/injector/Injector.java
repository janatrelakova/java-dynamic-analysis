package cz.muni.fi.xtrelak.injector;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;

public class Injector {
        public static void premain(String agentArgs, Instrumentation inst) {
            new AgentBuilder.Default()
                    .type(nameStartsWith("com.example.project"))  // Target your package or class here
                    .transform((builder, _, _, _, _) ->
                            builder.visit(Advice.to(InjectAdvice.class).on(any())))
                    .installOn(inst);
        }
}
