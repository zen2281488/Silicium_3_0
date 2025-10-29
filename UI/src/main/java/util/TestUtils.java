package util;

import model.xyz.CustomerRow;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

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

    public static List<CustomerRow> parseCustomerRows(WebElement customersTable) {

        List<WebElement> headers = customersTable.findElements(By.cssSelector("thead tr td"));
        int fnIdx = TestUtils.indexOf(headers, "First Name");
        int lnIdx = TestUtils.indexOf(headers, "Last Name");
        int pcIdx = TestUtils.indexOf(headers, "Post Code");
        int accIdx = TestUtils.indexOf(headers, "Account Number");
        int delIdx = TestUtils.indexOf(headers, "Delete Customer");

        return customersTable.findElements(By.cssSelector("tbody tr"))
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
}
