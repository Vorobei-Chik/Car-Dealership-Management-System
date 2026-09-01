package study.project.dealership.presentation.api;

import java.util.List;

public record ValidationErrorResponse(String message, List<FieldViolation> violations) {
}
