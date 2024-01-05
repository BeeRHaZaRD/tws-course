package ru.ifmo.webservices;

import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;

import javax.xml.namespace.QName;
import java.util.Collections;
import java.util.Set;

public class ThrottlingHandler implements SOAPHandler<SOAPMessageContext> {
    private static int availableRequests = 5;

    public Set<QName> getHeaders() {
        return Collections.emptySet();
    }

    public boolean handleMessage(SOAPMessageContext messageContext) {
        Boolean outboundProperty = (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outboundProperty) {
            availableRequests += 1;
        } else {
            if (availableRequests == 0) {
                throw new RuntimeException("Throttling");
            }
            availableRequests -= 1;
        }
        return true;
    }

    public boolean handleFault(SOAPMessageContext messageContext) {
        return true;
    }

    public void close(MessageContext messageContext) {}
}
