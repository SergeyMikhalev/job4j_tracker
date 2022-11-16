package ru.job4j.tracker.output;

public class StubOutput implements Output {

    private StringBuilder data = new StringBuilder();

    @Override
    public void println(Object obj) {
        data.append(obj);
        data.append(System.lineSeparator());
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
