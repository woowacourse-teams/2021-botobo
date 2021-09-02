package botobo.core.util;

import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.TagName;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// 문자열 간의 유사도 측정 알고리즘인 편집거리 알고리즘
// ref: https://madplay.github.io/post/levenshtein-distance-edit-distance
public class SimilarityChecker {
    private SimilarityChecker() {
    }

    public static List<Tag> orderBySimilarity(String origin, List<Tag> target, int sizeLimit) {
        List<Pair> tempList = new ArrayList<>();
        for (Tag tag : target) {
            double score = similarity(origin, tag.getTagName());
            tempList.add(new Pair(tag, score));
        }
        return tempList.stream()
                .sorted(Comparator.comparingDouble(p -> -p.score))
                .map(pair -> pair.tag)
                .limit(sizeLimit)
                .collect(Collectors.toList());
    }

    private static double similarity(String origin, TagName tagName) {
        final String target = tagName.getValue();
        String longStr = origin;
        String shortStr = target;

        if (origin.length() < target.length()) {
            longStr = target;
            shortStr = origin;
        }

        int lengthOfLong = longStr.length();
        if (lengthOfLong == 0) return 1.0;
        return (lengthOfLong - editDistance(longStr, shortStr)) / (double) lengthOfLong;
    }

    private static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();
        int[] scores = new int[s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            lastValue = updateLastValue(s1, s2, scores, i, lastValue);
            if (i > 0) scores[s2.length()] = lastValue;
        }

        return scores[s2.length()];
    }

    private static int updateLastValue(String s1, String s2, int[] scores, int i, int lastValue) {
        for (int j = 0; j <= s2.length(); j++) {
            if (i == 0) {
                scores[j] = j;
                continue;
            }
            if (j > 0) {
                int newValue = scores[j - 1];
                if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                    newValue = Math.min(Math.min(newValue, lastValue), scores[j]) + 1;
                }
                scores[j - 1] = lastValue;
                lastValue = newValue;
            }
        }
        return lastValue;
    }

    private static class Pair {
        Tag tag;
        double score;

        public Pair(Tag tag, double score) {
            this.tag = tag;
            this.score = score;
        }
    }
}
