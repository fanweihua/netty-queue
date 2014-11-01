package org.mitallast.queue.client;

import com.google.inject.Inject;
import org.mitallast.queue.action.ActionListener;
import org.mitallast.queue.action.queues.create.CreateQueueAction;
import org.mitallast.queue.action.queues.create.CreateQueueRequest;
import org.mitallast.queue.action.queues.create.CreateQueueResponse;
import org.mitallast.queue.action.queues.delete.DeleteQueueAction;
import org.mitallast.queue.action.queues.delete.DeleteQueueRequest;
import org.mitallast.queue.action.queues.delete.DeleteQueueResponse;
import org.mitallast.queue.action.queues.stats.QueuesStatsAction;
import org.mitallast.queue.action.queues.stats.QueuesStatsRequest;
import org.mitallast.queue.action.queues.stats.QueuesStatsResponse;

public class QueuesClient {

    private final QueuesStatsAction queuesStatsAction;
    private final CreateQueueAction createQueueAction;
    private final DeleteQueueAction deleteQueueAction;

    @Inject
    public QueuesClient(QueuesStatsAction queuesStatsAction, CreateQueueAction createQueueAction, DeleteQueueAction deleteQueueAction) {
        this.queuesStatsAction = queuesStatsAction;
        this.createQueueAction = createQueueAction;
        this.deleteQueueAction = deleteQueueAction;
    }

    public void queuesStatsRequest(QueuesStatsRequest request, ActionListener<QueuesStatsResponse> listener) {
        queuesStatsAction.execute(request, listener);
    }

    public void createQueue(CreateQueueRequest request, ActionListener<CreateQueueResponse> listener) {
        createQueueAction.execute(request, listener);
    }

    public void deleteQueue(DeleteQueueRequest request, ActionListener<DeleteQueueResponse> listener) {
        deleteQueueAction.execute(request, listener);
    }
}
