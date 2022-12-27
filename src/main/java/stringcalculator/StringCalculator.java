package stringcalculator;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {
    private static final String DEFAULT_DELIMITER = ",|:";
    private static final String REGEX_PATTERN = "//(.)\n(.*)";

    public String[] split(String s) {
        if (s == null || s.isEmpty()) {
            return new String[]{};
        }
        return splitByRegex(s).orElseGet(() -> s.split(DEFAULT_DELIMITER));
    }

    public Optional<String[]> splitByRegex(String s) {
        Matcher m = Pattern.compile(REGEX_PATTERN).matcher(s);
        if (m.find()) {
            String delimiter = m.group(1);
            return Optional.of(m.group(2).split(Pattern.quote(delimiter)));
        }
        return Optional.empty();
    }

    public int sumOf(String[] input) {
        int[] numbers = cast(input);
        return Arrays.stream(numbers).sum();
    }

    private int[] cast(String[] input) {
        int[] result = Arrays.stream(input).mapToInt(Integer::parseInt).filter(i -> i >= 0).toArray();
        if (input.length != result.length) {
            throw new RuntimeException();
        }
        return result;
    }

    public int calculate(String s) {
        String[] splitString = split(s);
        return sumOf(splitString);
    }
}
