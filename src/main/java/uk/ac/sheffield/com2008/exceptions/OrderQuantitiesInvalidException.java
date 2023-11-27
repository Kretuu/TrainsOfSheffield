package uk.ac.sheffield.com2008.exceptions;

import uk.ac.sheffield.com2008.model.domain.data.OrderLine;

import java.util.List;

public class OrderQuantitiesInvalidException extends Exception {
    private final List<OrderLine> orderLines;

    public OrderQuantitiesInvalidException(List<OrderLine> invalidOrderLines) {
        super();
        this.orderLines = invalidOrderLines;
    }

    @Override
    public String getMessage() {
        StringBuilder messageBuilder = new StringBuilder()
                .append("Products quantities for following products are not in stock: ");
        orderLines.forEach(orderLine -> messageBuilder.append(orderLine.getProduct().getName())
                .append(", "));
        messageBuilder.setLength(messageBuilder.length() - 2);
        messageBuilder.append(". Quantities have been updated to stock value");
        return messageBuilder.toString();
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }
}
