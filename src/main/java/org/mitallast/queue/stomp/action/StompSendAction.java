package org.mitallast.queue.stomp.action;

import com.google.inject.Inject;
import io.netty.handler.codec.stomp.StompCommand;
import io.netty.handler.codec.stomp.StompFrame;
import io.netty.handler.codec.stomp.StompHeaders;
import org.mitallast.queue.action.ActionListener;
import org.mitallast.queue.action.queue.enqueue.EnQueueRequest;
import org.mitallast.queue.action.queue.enqueue.EnQueueResponse;
import org.mitallast.queue.client.Client;
import org.mitallast.queue.common.settings.Settings;
import org.mitallast.queue.queue.QueueMessage;
import org.mitallast.queue.queue.QueueMessageType;
import org.mitallast.queue.stomp.BaseStompHandler;
import org.mitallast.queue.stomp.StompController;
import org.mitallast.queue.stomp.transport.StompSession;

import java.util.UUID;

public class StompSendAction extends BaseStompHandler {

    @Inject
    public StompSendAction(Settings settings, Client client, StompController controller) {
        super(settings, client);
        controller.registerHandler(StompCommand.SEND, this);
    }

    @Override
    public void handleRequest(final StompSession session, final StompFrame request) {
        QueueMessage queueMessage = new QueueMessage();

        switch (request.headers().get(StompHeaders.CONTENT_TYPE, "text/plain")) {
            case "text":
            case "text/plain":
                queueMessage.setSource(QueueMessageType.STRING, request.content());
                break;
            case "json":
            case "application/json":
                queueMessage.setSource(QueueMessageType.JSON, request.content());
                break;
            default:
                session.sendError("Unsupported content type");
                return;
        }

        String messageId = request.headers().get(StompHeaders.MESSAGE_ID);
        if (messageId != null && !messageId.isEmpty()) {
            try {
                queueMessage.setUuid(UUID.fromString(messageId));
            } catch (IllegalArgumentException e) {
                session.sendError(e);
                return;
            }
        }

        EnQueueRequest enQueueRequest = new EnQueueRequest();
        enQueueRequest.setQueue(request.headers().get(StompHeaders.DESTINATION));
        enQueueRequest.setMessage(queueMessage);

        client.queue().enqueueRequest(enQueueRequest, new ActionListener<EnQueueResponse>() {
            @Override
            public void onResponse(EnQueueResponse response) {
                session.sendResponse(StompCommand.RECEIPT);
            }

            @Override
            public void onFailure(Throwable e) {
                session.sendError(e);
            }
        });
    }
}
