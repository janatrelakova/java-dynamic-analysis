package cz.muni.fi.xtrelak.injector;

import javassist.*;
import java.io.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.jar.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Injector {

    public static void modifyJar(File jarFile) throws Exception {

        String includePackage = "com.project.example";
        try (JarFile jar = new JarFile(jarFile)) {
            try (JarOutputStream newJar = new JarOutputStream(new FileOutputStream("my-jar.jar"))) {
                var entries = enumerationAsStream(jar.entries());

                entries.forEach(entry -> {
                    try {
                        if (entry.getName().contains(includePackage) && entry.getName().endsWith(".class")) {
                           modifyEntry(jar, newJar, entry);
                        } else {
                            copyEntry(jar, newJar, entry);
                        }
                    } catch (IOException | CannotCompileException e) {
                        throw new RuntimeException(e);
                    }
                });

            }

        }
        System.out.println("JAR file modified successfully!");
    }

    private static void modifyEntry(JarFile jar, JarOutputStream destJar, JarEntry entry) throws IOException, CannotCompileException {
        destJar.putNextEntry(entry);
        ClassPool pool = ClassPool.getDefault();
        CtClass c = null;
        try {
            c = pool.makeClass(new ByteArrayInputStream(jar.getInputStream(entry).readAllBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] result;
        try {
            assert c != null;
            result = c.toBytecode();
        } catch (IOException | CannotCompileException e) {
            e.printStackTrace();
            return;
        } finally {
            if (c != null) {
                c.detach();
            }
        }
        destJar.write(result);
        destJar.closeEntry();
    }

    private static void copyEntry(JarFile srcJar, JarOutputStream destJar, JarEntry entry) throws IOException {
        destJar.putNextEntry(entry);
        try (InputStream in = srcJar.getInputStream(entry)) {
            byte[] buffer = new byte[1024];
            while (true) {
                int count = in.read(buffer);
                if (count == -1)
                    break;
                destJar.write(buffer, 0, count);
            }
            destJar.closeEntry();
        }
    }

    /**
     * Creates stream from Enumeration.
     * @author Georgios Gousios <gousiosg@gmail.com>
     */
    public static <T> Stream<T> enumerationAsStream(Enumeration<T> e) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        new Iterator<T>() {
                            public T next() {
                                return e.nextElement();
                            }

                            public boolean hasNext() {
                                return e.hasMoreElements();
                            }
                        },
                        Spliterator.ORDERED), false);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java injector.jar <path-to-jar>");
            return;
        }

        File jarFile = new File(args[0]);

        try {
            modifyJar(jarFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
