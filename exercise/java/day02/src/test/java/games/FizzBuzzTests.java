package games;

import io.vavr.collection.LinkedHashMap;
import io.vavr.collection.Seq;
import io.vavr.test.Arbitrary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static games.FizzBuzz.MAX;
import static games.FizzBuzz.MIN;
import static io.vavr.API.List;
import static io.vavr.API.Some;
import static io.vavr.test.Arbitrary.integer;
import static io.vavr.test.Property.def;
import static org.assertj.core.api.Assertions.assertThat;

class FizzBuzzTests {
    private static final Seq<String> fizzBuzzStrings = List("Fizz", "Buzz", "FizzBuzz", "Whizz", "Bang", "FizzWhizz", "FizzBang", "BuzzWhizz", "BuzzBang", "WhizzBang");
    public static final LinkedHashMap<Integer, String> ORIGINAL_RULES = LinkedHashMap.of(
            15, "FizzBuzz",
            3, "Fizz",
            5, "Buzz"
    );

    public static final LinkedHashMap<Integer, String> NEW_RULES = LinkedHashMap.of(
            15, "FizzBuzz",
            21, "FizzWhizz",
            33, "FizzBang",
            35, "BuzzWhizz",
            55, "BuzzBang",
            77, "WhizzBang",
            3, "Fizz",
            5, "Buzz",
            7, "Whizz",
            11, "Bang"
    );

    @BeforeEach
    void setUp() {
        FizzBuzz.setRules(NEW_RULES);
    }

    public static Stream<Arguments> validInputs() {
        return Stream.of(
                Arguments.of(1, "1"),
                Arguments.of(67, "67"),
                Arguments.of(82, "82"),
                Arguments.of(3, "Fizz"),
                Arguments.of(66, "FizzBang"),
                Arguments.of(99, "FizzBang"),
                Arguments.of(5, "Buzz"),
                Arguments.of(50, "Buzz"),
                Arguments.of(85, "Buzz"),
                Arguments.of(15, "FizzBuzz"),
                Arguments.of(30, "FizzBuzz"),
                Arguments.of(45, "FizzBuzz"),
                Arguments.of(7, "Whizz"),
                Arguments.of(11, "Bang"),
                Arguments.of(21, "FizzWhizz"),
                Arguments.of(35, "BuzzWhizz"),
                Arguments.of(70, "BuzzWhizz"),
                Arguments.of(55, "BuzzBang"),
                Arguments.of(77, "WhizzBang")
        );
    }

    @ParameterizedTest
    @MethodSource("validInputs")
    void parse_successfully_numbers_between_1_and_100_samples(int input, String expectedResult) {
        assertThat(FizzBuzz.convert(input))
                .isEqualTo(Some(expectedResult));
    }

    @Test
    void parse_return_valid_string_for_numbers_between_1_and_100() {
        def("Some(validString) for numbers in [1; 100]")
                .forAll(validInput())
                .suchThat(this::isConvertValid)
                .check()
                .assertIsSatisfied();
    }

    @Test
    void parse_fail_for_numbers_out_of_range() {
        def("None for numbers out of range")
                .forAll(invalidInput())
                .suchThat(x -> FizzBuzz.convert(x).isEmpty())
                .check()
                .assertIsSatisfied();
    }

    private boolean isConvertValid(Integer x) {
        return FizzBuzz.convert(x)
                .exists(s -> validStringsFor(x).contains(s));
    }

    private static Arbitrary<Integer> validInput() {
        return integer().filter(x -> x >= MIN && x <= MAX);
    }

    private static Seq<String> validStringsFor(Integer x) {
        return fizzBuzzStrings.append(x.toString());
    }

    private static Arbitrary<Integer> invalidInput() {
        return integer().filter(x -> x < MIN || x > MAX);
    }
}