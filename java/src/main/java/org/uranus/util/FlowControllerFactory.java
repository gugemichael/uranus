package org.uranus.util;

import org.uranus.util.FlowController.FlowControllerType;

public class FlowControllerFactory {

	/**
	 * Generate specify flow controller with type of {@link FlowControllerType}
	 * 
	 * @param type
	 * @return
	 */
	public static final FlowController create(FlowControllerType type) {
		switch (type) {
		case QPS:
			return new QPSFlowController();
		case FLOW_FUNNEL:
			throw new IllegalArgumentException("FUNNEL_QPS not support yet");
		default:
			throw new IllegalArgumentException(String.format("unknown type %s", type.name()));
		}
	}

}
