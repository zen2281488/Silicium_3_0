package util;

import org.openqa.selenium.WebElement;

import java.util.List;

// этот класс содержит методы для выполнения 7 пункта первой части задания
public class TestUtils {

    public static String longestToolName(List<WebElement> tools) {
        return tools.stream()
                .map(e -> e.getText().trim())
                .max(java.util.Comparator.comparingInt(String::length))
                .orElse("");
    }

    public static int toolsCount(List<WebElement> tools) {
        return (int) tools.stream()
                .map(e -> e.getText().trim())
                .filter(s -> !s.isEmpty())
                .count();
    }

}
