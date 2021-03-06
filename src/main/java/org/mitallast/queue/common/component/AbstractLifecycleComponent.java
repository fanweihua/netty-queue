package org.mitallast.queue.common.component;

import org.mitallast.queue.QueueException;
import org.mitallast.queue.common.settings.Settings;

public abstract class AbstractLifecycleComponent extends AbstractComponent implements LifecycleComponent {

    protected final Lifecycle lifecycle = new Lifecycle();

    protected AbstractLifecycleComponent(Settings settings) {
        super(settings);
    }

    protected AbstractLifecycleComponent(Settings settings, Class customClass) {
        super(settings, customClass);
    }

    protected AbstractLifecycleComponent(Settings settings, Class loggerClass, Class componentClass) {
        super(settings, loggerClass, componentClass);
    }

    protected AbstractLifecycleComponent(Settings settings, String prefixSettings) {
        super(settings, prefixSettings);
    }

    protected AbstractLifecycleComponent(Settings settings, String prefixSettings, Class customClass) {
        super(settings, prefixSettings, customClass);
    }

    protected AbstractLifecycleComponent(Settings settings, String prefixSettings, Class loggerClass, Class componentClass) {
        super(settings, prefixSettings, loggerClass, componentClass);
    }

    @Override
    public Lifecycle.State lifecycleState() {
        return this.lifecycle.state();
    }

    @Override
    public void start() throws QueueException {
        if (!lifecycle.canMoveToStarted()) {
            logger.warn("Can't move to started, " + lifecycleState());
            return;
        }
        if (!lifecycle.moveToStarted()) {
            logger.warn("Don't moved to started, " + lifecycleState());
        }
        doStart();
        logger.info("started");
    }

    protected abstract void doStart() throws QueueException;

    @Override
    public void stop() throws QueueException {
        logger.debug("stopping");
        if (!lifecycle.canMoveToStopped()) {
            logger.warn("Can't move to stopped, it's a " + lifecycleState());
            return;
        }
        lifecycle.moveToStopped();
        doStop();
        logger.debug("stopped");
    }

    protected abstract void doStop() throws QueueException;

    @Override
    public void close() throws QueueException {
        if (lifecycle.started()) {
            stop();
        }
        if (!lifecycle.canMoveToClosed()) {
            logger.warn("Can't move to closed, it's a " + lifecycleState().name());
            return;
        }
        lifecycle.moveToClosed();
        doClose();
        logger.info("closed");
    }

    protected abstract void doClose() throws QueueException;
}

