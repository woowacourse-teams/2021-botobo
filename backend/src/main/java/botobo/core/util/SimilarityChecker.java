package botobo.core.util;

import botobo.core.domain.tag.Tag;
import botobo.core.domain.tag.dto.TagDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// 문자열 간의 유사도 측정 알고리즘인 편집거리 알고리즘
// ref: https://madplay.github.io/post/levenshtein-distance-edit-distance
public class SimilarityChecker {
    private SimilarityChecker() {
    }

    public static List<TagDto> orderBySimilarity(String origin, List<TagDto> target, int sizeLimit) {
        List<Pair> tempList = new ArrayList<>();
        for (TagDto tag : target) {
            int score = similarity(origin, tag.getName());
            tempList.add(new Pair(tag, score));
        }
        return tempList.stream()
                .sorted((previous, next) -> comparePriority(previous, next, origin))
                .map(pair -> pair.tag)
                .limit(sizeLimit)
                .collect(Collectors.toList());
    }

    private static int comparePriority(Pair previous, Pair next, String origin) {
        if (tagNameStartsWith(previous, origin) && !tagNameStartsWith(next, origin)) {
            return -1;
        }
        if (tagNameStartsWith(next, origin) && !tagNameStartsWith(previous, origin)) {
            return 1;
        }
        if (previous.score == next.score) {
            return previous.getName().compareToIgnoreCase(next.getName());
        }
        return previous.score > next.score ? 1 : -1;
    }

    private static boolean tagNameStartsWith(Pair previous, String origin) {
        return previous.getName().startsWith(origin);
    }

    private static int similarity(String s1, String name) {
        String s2 = name;
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
        TagDto tag;
        int score;

        public Pair(TagDto tag, int score) {
            this.tag = tag;
            this.score = score;
        }

        public String getName() {
            return tag.getName();
        }
    }
}
