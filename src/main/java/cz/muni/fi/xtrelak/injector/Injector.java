package cz.muni.fi.xtrelak.injector;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;
import java.util.Arrays;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;

public class Injector {
        public static void premain(String agentArgs, Instrumentation inst) {
            var agent = new AgentBuilder.Default();
            var packages = agentArgs == null ? null : agentArgs.split(",");
            if (packages == null || packages.length == 0) {
                System.out.println("No packages to target provided");
                agent.type(ElementMatchers.any())
                        .transform((builder, _, _, _, _) ->
                                builder.visit(Advice.to(InjectAdvice.class).on(any())))
                        .installOn(inst);
                return;
            }

            var targetedPackages = agent.type(nameStartsWith(packages[0]));
            for (int i = 1; i < packages.length; i++) {
                targetedPackages = targetedPackages.or(nameStartsWith(packages[i]));
            }

            targetedPackages
                .transform((builder, _, _, _, _) ->
                        builder.visit(Advice.to(InjectAdvice.class).on(any())))
                .installOn(inst);
        }
}
