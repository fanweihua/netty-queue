package org.mitallast.queue.rest.action.queue.delete;

import com.google.inject.Inject;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.mitallast.queue.action.ActionListener;
import org.mitallast.queue.action.queue.delete.DeleteRequest;
import org.mitallast.queue.action.queue.delete.DeleteResponse;
import org.mitallast.queue.client.Client;
import org.mitallast.queue.common.settings.Settings;
import org.mitallast.queue.rest.BaseRestHandler;
import org.mitallast.queue.rest.RestController;
import org.mitallast.queue.rest.RestRequest;
import org.mitallast.queue.rest.RestSession;
import org.mitallast.queue.rest.response.StatusRestResponse;

import java.util.UUID;

public class RestDeleteAction extends BaseRestHandler {

    @Inject
    public RestDeleteAction(Settings settings, Client client, RestController controller) {
        super(settings, client);
        controller.registerHandler(HttpMethod.DELETE, "/{queue}/message", this);
        controller.registerHandler(HttpMethod.DELETE, "/{queue}/message/{uuid}", this);
    }

    @Override
    public void handleRequest(final RestRequest request, final RestSession session) {
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.setQueue(request.param("queue"));
        String uuid = request.param("uuid");
        if (uuid != null) {
            deleteRequest.setUuid(UUID.fromString(uuid));
        }

        client.queue().deleteRequest(deleteRequest, new ActionListener<DeleteResponse>() {
            @Override
            public void onResponse(DeleteResponse deleteResponse) {
                session.sendResponse(new StatusRestResponse(HttpResponseStatus.ACCEPTED));
            }

            @Override
            public void onFailure(Throwable e) {
                session.sendResponse(e);
            }
        });
    }
}