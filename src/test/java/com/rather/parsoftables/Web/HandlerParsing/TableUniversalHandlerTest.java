package com.rather.parsoftables.Web.HandlerParsing;

import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TableUniversalHandlerTest {

    private TableUniversalHandler tableUniversalHandler;

    @BeforeEach
    public void setupTableUniversalHandler(){
        tableUniversalHandler = new TableUniversalHandler();
    }

    @Test
    @DisplayName("Should not use handle when parameter Elements is null")
    public void shouldThrowRuntimeExceptionWhenElementsIsNul(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            tableUniversalHandler.handle(null, 3, 4);
        });
    }

    @Test
    @DisplayName("Should not use handle when parameter Rows is null")
    public void shouldThrowRuntimeExceptionWhenRowsIsNul(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            tableUniversalHandler.handle(null, 0, 4);
        });
    }

    @Test
    @DisplayName("Should not use handle when parameter Columns is null")
    public void shouldThrowRuntimeExceptionWhenColumnsIsNul(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            tableUniversalHandler.handle(null, 3, 0);
        });
    }
}
