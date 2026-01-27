package com.example.mindStreamApplication.AI.MCP.Rules;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.mindStreamApplication.AI.MCP.Processors.QwenOutputParser;


@SpringBootTest
public class QwenOutputParserTest {


    @Autowired
    private QwenOutputParser qwenOutputParser;

    @Test
    public void testExtractTextFromCode_WhenPrintStatement_ReturnsText(){
        String code= "print(\"Hello, World!\")";
        String result= qwenOutputParser.extractTextFromCode(code);
        assertEquals("Hello, World!", result);
    }

    @Test
    void testExtractTextFromCode_ReturnStatement() {
        String code = """
            def generate_summary():
                return "A comedy film about friendship"
            """;
        String result = qwenOutputParser.extractTextFromCode(code);
        assertEquals("A comedy film about friendship", result);
    }

    @Test
    void testExtractJSONFromCode() {
        String code = """
            def parse_query():
                result = {"genre": "action", "mood": "exciting"}
                print(json.dumps(result))
            """;
        String result = qwenOutputParser.extractJSONFromCode(code);
        assertEquals("{\"genre\": \"action\", \"mood\": \"exciting\"}", result);
    }
}
