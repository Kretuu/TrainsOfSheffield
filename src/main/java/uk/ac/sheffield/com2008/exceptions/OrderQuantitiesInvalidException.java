package uk.ac.sheffield.com2008.exceptions;

import uk.ac.sheffield.com2008.model.domain.data.OrderLine;

import java.util.List;

public class OrderQuantitiesInvalidException extends Exception {
    private final List<OrderLine> orderLines;
    private String message;

    public OrderQuantitiesInvalidException(List<OrderLine> invalidOrderLines) {
        super();

        this.orderLines = invalidOrderLines;
    }


    public void addMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        StringBuilder messageBuilder = new StringBuilder()
                .append("Products quantities for following products are not in stock: ");
        orderLines.forEach(orderLine -> messageBuilder.append(orderLine.getProduct().getName())
                .append(", "));
        messageBuilder.setLength(messageBuilder.length() - 2);
        messageBuilder.append(".");
        if (message != null && !message.isEmpty()) messageBuilder.append(" ").append(message);
        return messageBuilder.toString();
    }
}
