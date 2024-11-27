package cz.muni.fi.xtrelak.injector;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;

public class Injector {
    private static final Logger logger = LogManager.getLogger(Injector.class);

    public static void premain(String agentArgs, Instrumentation inst) {
        Writer.setupFileWriter();

        var agent = new AgentBuilder.Default();
        var packages = agentArgs == null ? null : agentArgs.split(",");
        if (packages == null || packages.length == 0) {
            logger.warn("No packages to target provided");
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

        logger.info("Targeting packages: {}", (Object) packages);
        logger.info("Starting dynamic analysis...");
        targetedPackages
            .transform((builder, _, _, _, _) ->
                    builder.visit(Advice.to(InjectAdvice.class).on(any())))
            .installOn(inst);
    }
}
