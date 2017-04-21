package org.aakimov.glue.test;

import org.aakimov.glue.annotations.ImmutableImplementation;

import java.util.List;
import java.util.Map;

/**
 * Test interface to check the annotation processor.
 *
 * @author aakimov
 */
@ImmutableImplementation
public interface Hello {
    List<String> getList();
    String getString();
    Map<Integer, List<Long>> getComplexMap();
}
