package de.jannikarndt.datamover.monitor

trait Monitoring {
    protected val monitor: Monitor = Monitor.getMonitor(getClass.getName)
}
