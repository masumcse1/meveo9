package org.everit.json.schema;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import com.google.re2j.Pattern;
import java.util.Optional;

public class StringSchemaValidatingVisitor extends Visitor {

    private final Object subject;

    private String stringSubject;

    private int stringLength;

    private final ValidatingVisitor owner;

    public StringSchemaValidatingVisitor(Object subject, ValidatingVisitor owner) {
        this.subject = subject;
        this.owner = requireNonNull(owner, "failureReporter cannot be null");
    }

    @Override void visitStringSchema(StringSchema stringSchema) {
        if (owner.passesTypeCheck(String.class, stringSchema.requireString(), stringSchema.isNullable())) {
            stringSubject = (String) subject;
            stringLength = stringSubject.codePointCount(0, stringSubject.length());
            super.visitStringSchema(stringSchema);
        }
    }

    @Override void visitMinLength(Integer minLength) {
        if (minLength != null && stringLength < minLength.intValue()) {
            owner.failure("expected minLength: " + minLength + ", actual: "
                    + stringLength, "minLength");
        }
    }

    @Override void visitMaxLength(Integer maxLength) {
        if (maxLength != null && stringLength > maxLength.intValue()) {
            owner.failure("expected maxLength: " + maxLength + ", actual: "
                    + stringLength, "maxLength");
        }
    }

    @Override void visitPattern(Pattern pattern) {
        if (pattern != null && !pattern.matcher(stringSubject).find()) {
            String message = format("string [%s] does not match pattern %s",
                    subject, pattern.pattern());
            owner.failure(message, "pattern");
        }
    }

    @Override void visitFormat(FormatValidator formatValidator) {
        Optional<String> failure = formatValidator.validate(stringSubject);
        if (failure.isPresent()) {
            owner.failure(failure.get(), "format");
        }
    }

}
