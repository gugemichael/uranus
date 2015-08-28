package org.uranus.utils.metric;

import java.io.PrintStream;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Counter;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.Histogram;
import com.yammer.metrics.core.Metered;
import com.yammer.metrics.core.Metric;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricPredicate;
import com.yammer.metrics.core.MetricProcessor;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.reporting.AbstractReporter;
import com.yammer.metrics.stats.Snapshot;

/**
 * A simple reporters which prints out application metrics to a
 * {@link PrintStream} periodically.
 */
public class YammerUranusReporter extends AbstractReporter implements MetricProcessor<YammerUranusReporter.Context>
{
	class Context
	{
		public String result;
	}

	private final String separator;

	public YammerUranusReporter() {
		this(" = ");
	}

	public YammerUranusReporter(String separator) {
		super(Metrics.defaultRegistry());
		this.separator = separator;
	}

	public String extract() {
		try {
			StringBuilder buffer = new StringBuilder(4096);

			for (Entry<String, SortedMap<MetricName, Metric>> entry : getMetricsRegistry().groupedMetrics(
					MetricPredicate.ALL).entrySet()) {
				for (Entry<MetricName, Metric> subEntry : entry.getValue().entrySet()) {
					Context ctx = new Context();
					buffer.append(subEntry.getKey().getName()).append(separator);
					subEntry.getValue().processWith(this, subEntry.getKey(), ctx);
					buffer.append(ctx.result).append(String.format("%n"));
				}
			}

			return buffer.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	@Override
	public void processGauge(MetricName name, Gauge<?> gauge, Context ctx) {
		ctx.result = gauge.value().toString();
	}

	@Override
	public void processCounter(MetricName name, Counter counter, Context ctx) {
		ctx.result = String.format("%,d", counter.count());
	}

	@Override
	public void processMeter(MetricName name, Metered meter, Context ctx) {
		final String unit = abbrev(meter.rateUnit());
		ctx.result = String.format("%n    count = %,d%n    mean rate = %,2.0f %s/%s%n    1-minute rate = %,2.0f %s/%s%n    5-minute rate = %,2.0f %s/%s%n    15-minute rate = %,2.0f %s/%s",
				meter.count(), meter.meanRate(), meter.eventType(), unit, meter.oneMinuteRate(),
				meter.eventType(), unit, meter.fiveMinuteRate(), meter.eventType(), unit,
				meter.fifteenMinuteRate(), meter.eventType(), unit);
	}

	@Override
	public void processHistogram(MetricName name, Histogram histogram, Context ctx) {
		final Snapshot snapshot = histogram.getSnapshot();
		ctx.result = String.format("%n    min = %,2.0f%n    max = %,2.0f%n	mean = %,2.0f%n    stddev = %,2.0f%n    median = %,2.0f%n    75%% <= %,2.0f%n    99%% <= %,2.0f",
				histogram.min(), histogram.max(), histogram.mean(), histogram.stdDev(), snapshot.getMedian(),
				snapshot.get75thPercentile(), snapshot.get99thPercentile());
	}

	@Override
	public void processTimer(MetricName name, Timer timer, Context ctx) {
		processMeter(name, timer, ctx);
		final String durationUnit = abbrev(timer.durationUnit());
		final Snapshot snapshot = timer.getSnapshot();
		ctx.result += String.format("%n    median = %,2.0f%s%n    75%% <= %,2.0f%s%n    99%% <= %,2.0f%s",
				snapshot.getMedian(), durationUnit, snapshot.get75thPercentile(), durationUnit, snapshot.get99thPercentile(), durationUnit);
	}

	private String abbrev(TimeUnit unit) {
		switch (unit) {
			case NANOSECONDS:
				return "ns";
			case MICROSECONDS:
				return "us";
			case MILLISECONDS:
				return "ms";
			case SECONDS:
				return "s";
			case MINUTES:
				return "m";
			case HOURS:
				return "h";
			case DAYS:
				return "d";
			default:
				throw new IllegalArgumentException("Unrecognized TimeUnit: " + unit);
		}
	}
}
