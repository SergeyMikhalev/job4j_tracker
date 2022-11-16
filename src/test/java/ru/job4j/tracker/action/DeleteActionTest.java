package ru.job4j.tracker.action;

import org.junit.Test;
import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.output.Output;
import ru.job4j.tracker.output.StubOutput;
import ru.job4j.tracker.store.MemTracker;
import ru.job4j.tracker.store.Store;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeleteActionTest {

    @Test
    public void whenOk() {
        Output out = new StubOutput();
        Store tracker = new MemTracker();
        tracker.add(new Item("Delete item"));
        UserAction del = new DeleteAction(out);

        Input input = mock(Input.class);

        when(input.askInt(any(String.class))).thenReturn(0);

        del.execute(input, tracker);

        String ln = System.lineSeparator();
        assertThat(out.toString(), is("Item is successfully deleted!" + ln));
        assertThat(tracker.findAll().size(), is(0));
    }

    @Test
    public void whenNotOk() {
        Output out = new StubOutput();
        Store tracker = new MemTracker();
        UserAction del = new DeleteAction(out);

        Input input = mock(Input.class);

        when(input.askInt(any(String.class))).thenReturn(0);

        del.execute(input, tracker);

        String ln = System.lineSeparator();
        assertThat(out.toString(), is("Wrong id!" + ln));
        assertThat(tracker.findAll().size(), is(0));
    }
}

