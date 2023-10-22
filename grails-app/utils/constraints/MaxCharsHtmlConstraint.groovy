package constraints

import org.codehaus.groovy.grails.validation.ConstrainedProperty
import org.springframework.validation.Errors

/**
 * Constraint validator for counting characters in a HTML element
 */
class MaxCharsHtmlConstraint extends org.codehaus.groovy.grails.validation.AbstractConstraint {
    public static final String MAX_CHARS_HTML_CONSTRAINT = "maxCharsHtml";
    private static final String DEFAULT_EXCEDED_MAX_CHARS_HTML_MESSAGE_CODE = "default.not.maxCharsHtml.message";

    private int maxSize

    int getMaxSize() {
        return maxSize
    }

    @Override
    protected void processValidate(Object target, Object propertyValue, Errors errors) {
        int maxSize = this.constraintParameter
        String cleanText = clearText(propertyValue)
        if (cleanText.size() > maxSize) {
            super.rejectValue(target, errors, DEFAULT_EXCEDED_MAX_CHARS_HTML_MESSAGE_CODE, MAX_CHARS_HTML_CONSTRAINT,
                    this.constraintPropertyName, target.getClass(), propertyValue, maxSize);
        }
    }

    private String clearText(String rawText) {
        String cleanText = rawText
                .replaceAll("<(.|\n)*?>", '') // REMOVE ALL HTML CODES
                .replaceAll("&nbsp;", ' ') // REMOVE ALL HTML CODES
                .replaceAll("\u00A0", "") // Special break line or space
                .replaceAll("\\s+", " ") // Replace multiple spaces with only one
        return cleanText;

    }

    @Override
    boolean supports(Class type) {
        return type != null && String.class.isAssignableFrom(type);
    }

    @Override
    String getName() {
        return MAX_CHARS_HTML_CONSTRAINT
    }

    @Override
    void setParameter(Object constraintParameter) {
        if (!(constraintParameter instanceof Integer)) {
            throw new IllegalArgumentException("Parameter for constraint [" +
                    ConstrainedProperty.MAX_SIZE_CONSTRAINT + "] of property [" +
                    constraintPropertyName + "] of class [" + constraintOwningClass +
                    "] must be a of type [java.lang.Integer]");
        }

        maxSize = ((Integer) constraintParameter).intValue();
        super.setParameter(constraintParameter);
    }
}
