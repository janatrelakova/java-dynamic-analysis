package cz.muni.fi.xtrelak.scraper.iterator;

import com.github.javaparser.ast.body.MethodDeclaration;
import cz.muni.fi.xtrelak.scraper.Endpoint;

import java.util.*;

public interface Annotation {
    List<Endpoint> extractHttpConfiguration(MethodDeclaration method) throws IllegalAccessException;

}
