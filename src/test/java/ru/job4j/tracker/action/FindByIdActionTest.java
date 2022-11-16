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

public class FindByIdActionTest {

    @Test
    public void whenOk() {
        Output out = new StubOutput();
        Store tracker = new MemTracker();
        tracker.add(new Item("Super unique item"));
        UserAction find = new FindByIdAction(out);

        Input input = mock(Input.class);

        when(input.askInt(any(String.class))).thenReturn(0);

        find.execute(input, tracker);

        assertThat(out.toString().contains("Super unique item"), is(true));
    }

    @Test
    public void whenNotOk() {
        Output out = new StubOutput();
        Store tracker = new MemTracker();
        UserAction find = new FindByIdAction(out);

        Input input = mock(Input.class);

        when(input.askInt(any(String.class))).thenReturn(0);

        find.execute(input, tracker);
        String ls = System.lineSeparator();

        assertThat(out.toString(), is("Wrong id! Not found" + ls));
    }
}
