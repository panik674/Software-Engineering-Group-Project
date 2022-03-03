package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Test;
import uk.comp2211.group13.data.Data;
import uk.comp2211.group13.data.log.Impression;
import uk.comp2211.group13.enums.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class DataTest {
    private Data data = new Data();
    @Test
    public void ingestTest(){
        HashMap<Path, String> pathsTest = new HashMap<>();
        Assert.assertSame("test ingest",true,data.ingest(pathsTest));
        Assert.assertNotNull(data.request());
    }
    @Test
    public void validatePathTest(){

    }
    @Test
    public void validateLogFormatTest(){

    }
    @Test
    public void resetLogsTest(){

    }
    @Test
    public void requestTest(){
        Assert.assertNotNull(data.request());
        Assert.assertNotNull(data.request().getClickLogs());
        Assert.assertNotNull(data.request().getImpressionLogs());
        Assert.assertNotNull(data.request().getServerLogs());
    }
}

