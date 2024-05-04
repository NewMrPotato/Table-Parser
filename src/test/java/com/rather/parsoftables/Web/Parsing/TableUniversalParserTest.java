package com.rather.parsoftables.Web.Parsing;

import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TableUniversalParserTest {

    private TableUniversalParser tableUniversalParser;

    @BeforeEach
    public void setupTableUniversalParser(){
        tableUniversalParser = new TableUniversalParser();
    }

    @Test
    @DisplayName("Should not use parser when parameter Reference is null")
    public void shouldRuntimeExceptionWhenReferenceIsNull(){
        Assertions.assertThrows(RuntimeException.class, () -> {
           tableUniversalParser.parse(null, "someStr", 4, 4, 0);
        });
    }

    @Test
    @DisplayName("Should not use parser when parameter xPath is null")
    public void shouldRuntimeExceptionWhenXPathIsNull(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            tableUniversalParser.parse("someStr", null, 4, 4, 0);
        });
    }

    @Test
    @DisplayName("Should not use parser when parameter Rows is null")
    public void shouldRuntimeExceptionWhenRowsIsNull(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            tableUniversalParser.parse("someStr", "someStr", 0, 4, 0);
        });
    }

    @Test
    @DisplayName("Should not use parser when parameter Columns is null")
    public void shouldRuntimeExceptionWhenColumnsIsNull(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            tableUniversalParser.parse("someStr", "someStr", 4, 0, 0);
        });
    }
}
