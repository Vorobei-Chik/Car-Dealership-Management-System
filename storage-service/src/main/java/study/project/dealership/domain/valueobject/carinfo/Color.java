package study.project.dealership.domain.valueobject.carinfo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Embeddable
@Getter
@NoArgsConstructor
public class Color {
    private static final Pattern RGB_PATTERN = Pattern.compile("^#[0-9A-Fa-f]{6}$");

    @Column(name = "color", nullable = false)
    private String value;

    public Color(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }

        String trimmed = value.trim();
        if (!RGB_PATTERN.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("Invalid value format. Expected #RRGGBB (e.g., #FF00A1)");
        }

        this.value = trimmed.toUpperCase();
    }
}