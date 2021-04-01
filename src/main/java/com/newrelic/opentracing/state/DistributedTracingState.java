/*
 * Copyright 2020 New Relic Corporation. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.newrelic.opentracing.state;

import com.newrelic.opentracing.LambdaPayloadContext;
import com.newrelic.opentracing.LambdaSpan;
import com.newrelic.opentracing.dt.DistributedTracePayload;
import com.newrelic.opentracing.dt.DistributedTracePayloadImpl;
import com.newrelic.opentracing.dt.DistributedTracing;
import com.newrelic.opentracing.util.DistributedTraceUtil;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class DistributedTracingState {

    private final DistributedTracePayloadImpl inboundPayload;
    private final long transportTimeMillis;
    private final String traceId;
    private volatile Map<String, String> baggage;

    public DistributedTracingState(LambdaPayloadContext context) {
        this.inboundPayload = context.getPayload();
        this.transportTimeMillis = context.getTransportDurationInMillis();
        this.setBaggage(context.getBaggage());
        this.traceId = inboundPayload.getTraceId();
    }

    public DistributedTracingState() {
        inboundPayload = null;
        transportTimeMillis = Long.MIN_VALUE;
        setBaggage(Collections.emptyMap());
        traceId = DistributedTraceUtil.generateGuid();
    }

    public DistributedTracePayloadImpl getInboundPayload() {
        return inboundPayload;
    }

    public String getTraceId() {
        return traceId;
    }

    public DistributedTracePayload createDistributedTracingPayload(LambdaSpan span) {
        return DistributedTracing.getInstance().createDistributedTracePayload(span);
    }

    public void setBaggage(Map<String, String> baggage) {
        this.baggage = baggage;
    }

    public Map<String, String> getBaggage() {
        return baggage;
    }

    /**
     * @return transport duration or Long.MIN_VALUE if not set
     */
    public long getTransportTimeMillis() {
        return transportTimeMillis;
    }

}
