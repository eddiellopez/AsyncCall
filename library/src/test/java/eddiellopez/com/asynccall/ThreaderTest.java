package eddiellopez.com.asynccall;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ThreaderTest {

    @Mock
    private ExecutorService executor;

    @Mock
    private OnExceptionHandler onExceptionHandler;

    @Mock
    private LifecycleOwner lifecycleOwner;

    @Mock
    private Lifecycle lifecycle;

    @Mock
    private Callable<String> callable;

    @Mock
    private DeliveryProcedure<String> deliveryProcedure;


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(lifecycleOwner.getLifecycle()).thenReturn(lifecycle);
    }

    @Test
    public void name() {
        // Any threader, with lifecycle owner
        final TestThreader threader = new TestThreader(
                executor,
                onExceptionHandler,
                lifecycleOwner,
                deliveryProcedure
        );

        // Is expected to monitor the lifecycle
        verify(lifecycleOwner).getLifecycle();
    }

    @Test
    public void submit() {
        // Any threader
        final TestThreader threader = new TestThreader(
                executor,
                onExceptionHandler,
                lifecycleOwner,
                deliveryProcedure
        );

        // When a submit is issued...
        threader.submit(callable);

        // We expect is executed in the executor
        verify(executor).execute(Mockito.any());
    }
}