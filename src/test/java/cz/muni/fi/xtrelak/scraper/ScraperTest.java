package cz.muni.fi.xtrelak.scraper;

import org.junit.jupiter.api.Test;

public class ScraperTest {
    @Test
    public void testMain() throws Exception {
        Scraper.main(new String[]{"src/test/resources/test-project"});
    }
}
