package tests.providers;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

public class HotelsFilterProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return List.of(
                Arguments.of("Radisson", "Kazan", "Kazan", "Russia"),
                Arguments.of(null, null, null, "Russia"),
                Arguments.of(null, null, "Kazan", "Russia"),
                Arguments.of("Radisson", null, null, null)
        ).stream();
    }
}
