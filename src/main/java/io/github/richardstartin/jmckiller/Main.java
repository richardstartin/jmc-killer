package io.github.richardstartin.jmckiller;

import jdk.jfr.Recording;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String... args) throws IOException {
        try (Recording recording = new Recording()) {
            String mode = args[0].toUpperCase();
            recording.setName(mode);
            recording.setDumpOnExit(true);
            recording.setDestination(getRecordingDestination(args[1], mode));
            recording.start();
            switch (mode) {
                case "ASCENDING":
                    generateDisjointAscendingEvents();
                    break;
                case "DESCENDING":
                    generateDisjointDescendingEvents();
                    break;
                case "SHUFFLED":
                    generateDisjointShuffledEvents();
                    break;
                case "STACK":
                    generateStackEvents();
                    break;
                default:
            }
        }
    }

    private static Path getRecordingDestination(String recordingDir, String mode) throws IOException {
        Path destination = Paths.get(recordingDir);
        if (!Files.exists(destination)) {
            Files.createDirectory(destination);
        }
        return destination.resolve(mode + ".jfr");
    }

    private static void generateDisjointAscendingEvents() {
        for (int i = 0; i < 1000 * 100; i++) {
            MyEvent myEvent = new MyEvent();
            myEvent.begin();
            myEvent.end();
            myEvent.commit();
        }
    }

    private static void generateDisjointDescendingEvents() {
        List<MyEvent> myEvents = new ArrayList<>(1000 * 100);
        for (int i = 0; i < 1000 * 100; i++) {
            MyEvent myEvent = new MyEvent();
            myEvent.begin();
            myEvents.add(myEvent);
        }
        Collections.reverse(myEvents);
        for (MyEvent myEvent : myEvents) {
            myEvent.end();
            myEvent.commit();
        }
    }

    private static void generateDisjointShuffledEvents() {
        List<MyEvent> myEvents = new ArrayList<>(1000 * 100);
        for (int i = 0; i < 1000 * 100; i++) {
            MyEvent myEvent = new MyEvent();
            myEvent.begin();
            myEvents.add(myEvent);
        }
        Collections.shuffle(myEvents);
        for (MyEvent myEvent : myEvents) {
            myEvent.end();
            myEvent.commit();
        }
    }

    private static void generateStackEvents() {
        for (int i = 0; i < 1000; i++) {
            stack(100, 0);
        }
    }

    private static void stack(int targetDepth, int depth) {
        MyEvent myEvent = new MyEvent();
        myEvent.begin();
        if (targetDepth == depth) {
            sleep(10);
        } else {
            stack(targetDepth, depth + 1);
        }
        myEvent.end();
        myEvent.commit();
    }

    private static void sleep(int millis) {
//        try {
//            Thread.sleep(millis);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
    }
}
