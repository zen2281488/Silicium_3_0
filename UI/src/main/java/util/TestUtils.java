package util;

import model.xyz.CustomerRow;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

// этот класс содержит методы для выполнения 7 пункта первой части тестового задания
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

    public static long getRandomPostCodeNumber() {
        return ThreadLocalRandom.current()
                .nextLong(1_000_000_000L, 10_000_000_000L);
    }

    public static String nameFromPostCode(long postCode) {
        char[] out = new char[5];
        long div = 100_000_000L;

        for (int i = 0; i < 5; i++, div /= 100) {
            int twoDigits = (int) ((postCode / div) % 100);
            out[i] = (char) ('a' + (twoDigits % 26));
        }
        return new String(out);
    }

    public static int indexOf(List<WebElement> headers, String title) {
        for (int i = 0; i < headers.size(); i++) {
            if (title.equals(headers.get(i).getText().trim())) return i;
        }
        return -1;
    }

    public static String text(List<WebElement> tds, int idx) {
        return (idx >= 0 && idx < tds.size()) ? tds.get(idx).getText().trim() : "";
    }

    public static CustomerRow parseCustomerRow(List<CustomerRow> rows, String firstName, String lastName) {
        return rows.stream()
                .filter(r -> firstName.equals(r.getFirstName()) && lastName.equals(r.getLastName()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Не найден клиент: " + firstName + " " + lastName));
    }

    public static List<CustomerRow> parseCustomerRows(List<WebElement> tableHeaders,List<WebElement> tableRows) {

        int fnIdx = TestUtils.indexOf(tableHeaders, "First Name");
        int lnIdx = TestUtils.indexOf(tableHeaders, "Last Name");
        int pcIdx = TestUtils.indexOf(tableHeaders, "Post Code");
        int accIdx = TestUtils.indexOf(tableHeaders, "Account Number");
        int delIdx = TestUtils.indexOf(tableHeaders, "Delete Customer");

        return tableRows
                .stream()
                .map(tr -> {
                    List<WebElement> tds = tr.findElements(By.cssSelector("td"));
                    String f = TestUtils.text(tds, fnIdx);
                    String l = TestUtils.text(tds, lnIdx);
                    String p = TestUtils.text(tds, pcIdx);
                    List<String> acc = (accIdx >= 0 && accIdx < tds.size())
                            ? tds.get(accIdx).findElements(By.cssSelector("span"))
                            .stream().map(e -> e.getText().trim())
                            .filter(s -> !s.isEmpty()).toList()
                            : List.of();
                    WebElement delBtn = (delIdx >= 0 && delIdx < tds.size())
                            ? tds.get(delIdx).findElement(By.cssSelector("button"))
                            : null;

                    return CustomerRow.builder()
                            .firstName(f)
                            .lastName(l)
                            .postCode(p)
                            .accountNumbers(acc)
                            .deleteButton(delBtn)
                            .build();
                }).toList();
    }

    public static double averageFirstNameLen(List<CustomerRow> rows) {
        return rows.stream()
                .map(CustomerRow::getFirstName)
                .map(String::trim)
                .mapToInt(String::length)
                .average()
                .orElse(0.0);
    }

    public static Optional<CustomerRow> closestByFirstNameLenToMean(List<CustomerRow> rows) {
        if (rows == null || rows.isEmpty()) return Optional.empty();

        double avg = averageFirstNameLen(rows);
        int idx = IntStream.range(0, rows.size())
                .boxed()
                .min(Comparator.comparingDouble(i ->
                        Math.abs(rows.get(i).getFirstName().trim().length() - avg)))
                .orElse(0);

        return Optional.of(rows.get(idx));
    }
}
