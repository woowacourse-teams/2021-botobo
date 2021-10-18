package botobo.core.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YamlLoader {

    private YamlLoader() {
    }

    public static Object extractValue(String resourceFileName, String fullKey) {
        Map<String, Object> map = loadYaml(resourceFileName);
        String[] arr = fullKey.split("\\.");
        int len = arr.length;
        for (int i = 0; i < len - 1; i++) {
            Object obj = map.get(arr[i]);
            if (!(obj instanceof Map)) {
                return null;
            }
            map = (Map<String, Object>) obj;
        }
        return map.get(arr[len - 1]);
    }

    private static Map<String, Object> loadYaml(String resourceFileName) {
        Yaml yaml = new Yaml();
        InputStream inputStream = YamlLoader.class
                .getClassLoader()
                .getResourceAsStream(resourceFileName);
        return yaml.load(inputStream);
    }
}
