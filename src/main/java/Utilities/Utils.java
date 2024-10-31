package Utilities;

import PodcastData.Opportunity;
import com.alibaba.fastjson2.JSON;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import PodcastData.Podcast;

public class Utils {

    public static List<String> loadJsonsFromFile(String filepath) {
        List<String> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null)
                result.add(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Podcast[] getPodcasts(String filepath) {
        List<String> jsons = loadJsonsFromFile(filepath);
        Podcast[] result = new Podcast[jsons.size()];
        for (int i = 0; i < jsons.size(); i++) {
            result[i] = JSON.parseObject(jsons.get(i), Podcast.class);
        }
        return result;
    }

    public static HashMap<String, Integer> getNumbersForDownloads(Podcast[] podcasts, String city) {
        HashMap<String, Integer> map = new HashMap<>();
        for (Podcast p : podcasts) {
            if (p.getCity().equals(city)) {
                String showId = p.getDownloadIdentifier().getShowId();
                map.put(showId, map.getOrDefault(showId, 0) + 1);
            }
        }
        return map;
    }

    public static HashMap<String, Integer> getNumbersForDevices(Podcast[] podcasts) {
        HashMap<String, Integer> map = new HashMap<>();
        for (Podcast p : podcasts) {
            String deviceType = p.getDeviceType();
            map.put(deviceType, map.getOrDefault(deviceType, 0) + 1);
        }
        return map;
    }

    public static HashMap<String, Integer> getPrerolls(Podcast[] podcasts) {
        HashMap<String, Integer> map = new HashMap<>();
        for (Podcast p : podcasts) {
            String showId = p.getDownloadIdentifier().getShowId();
            List<Opportunity> opp = p.getOpportunities();
            int count = 0;
            for (Opportunity o : opp) {
                if (o.getPositionUrlSegments().get("aw_0_ais.adBreakIndex").contains("preroll")) count++;
            }
            map.put(showId, map.getOrDefault(showId, 0) + count);
        }
        return map;
    }

    public static LinkedHashMap<String, Integer> sortMap(HashMap<String, Integer> unsortedMap) {
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        ArrayList<Integer> counts = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : unsortedMap.entrySet())
            counts.add(entry.getValue());
        Collections.sort(counts);
        for (int i = counts.size() - 1; i >= 0; i--) {
            for (Map.Entry<String, Integer> entry : unsortedMap.entrySet()) {
                if (entry.getValue().equals(counts.get(i))) sortedMap.put(entry.getKey(), counts.get(i));
            }
        }
        return sortedMap;
    }

    public static HashMap<String, List<String>> getShowTimestamps(Podcast[] podcasts) {
        HashMap<String, List<String>> map = new HashMap<>();
        Set<String> showIdList = getPodcasts(podcasts);
        for (String showId : showIdList) {
            List<String> originalEventTimes = new ArrayList<>();
            for (Podcast p : podcasts) {
                String show = p.getDownloadIdentifier().getShowId();
                if (show.equals(showId)) {
                    List<Opportunity> opportunities = p.getOpportunities();
                    for (Opportunity op : opportunities) {
                        String originalEventTime = getDayOfWeekFromTimestamp(op.getOriginalEventTime());
                        originalEventTimes.add(originalEventTime);
                    }
                }
            }
            map.put(showId, originalEventTimes);
        }
        return map;
    }

    private static Set<String> getPodcasts(Podcast[] podcasts) {
        Set<String> showIdList = new HashSet<>();
        for (Podcast p : podcasts)
            showIdList.add(p.getDownloadIdentifier().getShowId());
        return showIdList;
    }

    private static String getReadableTime(long timestamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE dd.MM.yyyy HH:mm");
        return dateTime.format(formatter);
    }

    public static boolean isWeeklyShow(List<String> timestamps) {
        for (int i = 0; i < timestamps.size(); i++) {
            Set<String> unique = new HashSet<>(timestamps);
            if (unique.size() > 1) return false;
        }
        return true;
    }

    public static String getDayOfWeekFromTimestamp(long timestamp) {
        String date = getReadableTime(timestamp);
        date = date.split(" ")[0] + " " + date.split(" ")[2];
        return date;
    }
}