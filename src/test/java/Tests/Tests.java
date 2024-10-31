package Tests;

import PodcastData.Podcast;
import TestUtilities.TestUtils;
import Utilities.Utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Tests {
    public static void main(String[] args) {
        Podcast[] podcasts = Utils.getPodcasts("src/test/resources/downloads.txt");

//        Question 3
        Question3(podcasts, "san francisco");
//        Question 4
        Question4(podcasts);
//        Question 5
        Question5(podcasts);

//        Bonus
        BonusRequest(podcasts);
    }

    private static void Question3(Podcast[] podcasts, String city) {
        HashMap<String, Integer> downloadsMap = Utils.getNumbersForDownloads(podcasts, city);
        String show = "";
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : downloadsMap.entrySet()) {
            int value = entry.getValue();
            if (value > maxCount) {
                show = entry.getKey();
                maxCount = value;
            }
        }
        TestUtils.customAssert("Who Trolled Amber", show, "The show should be \"Who Trolled Amber\", but the actual show is \"" + show + "\"");
        TestUtils.customAssert(24, maxCount, "The number of downloads should be 25, but the actual number is " + maxCount);
        System.out.println("Most popular show is: " + show);
        System.out.println("Number of downloads is: " + maxCount);
    }

    private static void Question4(Podcast[] podcasts) {
        HashMap<String, Integer> deviceMap = Utils.getNumbersForDevices(podcasts);
        String device = "";
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : deviceMap.entrySet()) {
            int value = entry.getValue();
            if (value > maxCount) {
                device = entry.getKey();
                maxCount = value;
            }
        }
        TestUtils.customAssert("mobiles & tablets", device, "The most popular device shoule be \"mobiles & tablets\", but the actual device is \"" + device + "\"");
        TestUtils.customAssert(60, maxCount,"The number of downloads should be 25, but the actual number is " + maxCount);
        System.out.println("Most popular device is: " + device);
        System.out.println("Number of downloads: " + maxCount);
    }

    private static void Question5(Podcast[] podcasts) {
        HashMap<String, Integer> rollMap = Utils.getPrerolls(podcasts);
        LinkedHashMap<String, Integer> sortedRollMap = Utils.sortMap(rollMap);
        TestUtils.mapAssert(sortedRollMap);
        for (Map.Entry<String, Integer> entry : sortedRollMap.entrySet())
            System.out.println("Show Id: " + entry.getKey() + ", Preroll Opportunity Number: " + entry.getValue());
    }

    private static void BonusRequest(Podcast[] podcasts) {
        HashMap<String, List<String>> map = Utils.getShowTimestamps(podcasts);
        System.out.println("Weekly shows are:");
        String weeklies = "";
        for (Map.Entry<String, List<String>> entry : map.entrySet())
            if (Utils.isWeeklyShow(entry.getValue()))
                weeklies += "\n"+ entry.getKey() + " - " + entry.getValue().get(0);
        TestUtils.weeklyAsserts(weeklies);
        System.out.println(weeklies);
    }
}