package com.example.mindStreamApplication.AI.MCP.Rules;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

public class SummaryRulesTest {
    @Autowired
    private SummaryRules summaryRules;

    @Test
    public void testContainsSpoilers_WhenSpoilerPresent_ResturnsTrue(){
        String textWithSpoiler="The main character dies in the ending scene";
        assertTrue(summaryRules.containsSpoilers(textWithSpoiler));
    }

    @Test
    public void testContainsSpoilers_WhenSpoilerPresent_ResturnsFalse(){
        String safeText= "A thrilling adventure about exploration";
        assertFalse(summaryRules.containsSpoilers(safeText));

    }

    @Test
    public void enforceMaxLength_WhenTooLong_TruncatesProperly(){
        String longText= "A".repeat(200);
        String result= summaryRules.enforceMaxLength(longText, 150);
        assertEquals(147, result.length());
        assertTrue(result.endsWith("..."));
    }

    @Test
    public void testAdjustTone_Nuetral_Tone_RemovesExclamation(){
        String excitedText ="This movie is amazing !";
        String nuetral= summaryRules.adjustTone(excitedText, "nuetral");
        assertEquals("This movie is amazing.", nuetral);
    }
    
}
