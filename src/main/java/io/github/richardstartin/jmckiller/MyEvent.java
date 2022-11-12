package io.github.richardstartin.jmckiller;

import jdk.jfr.Category;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Category("My Events")
@Label("My Event")
public class MyEvent extends Event {
}
