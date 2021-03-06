package org.uranus.net.metric;

import org.uranus.util.ClassMemberReflector;

public class MetricClient implements MetricProcessor {
    private enum COMMAND {
        STATS, PING, PONG, HELP
    }

    // remember the last command
    private String lastCommand;
    // show status count with target class
    private final Class<?> target;
    // class static member reflector
    private final ClassMemberReflector reflector = new ClassMemberReflector();

    public MetricClient(Class<?> target) {
        this.target = target;
    }

    @Override
    public void acceptNewClient(MetricClientConnection client) {

    }

    @Override
    public void destoryClient(MetricClientConnection client) {

    }

    protected String response(COMMAND cmd) {
        switch (cmd) {
        case STATS:
            return reflector.toString(target);
        case PONG:
            return COMMAND.PING.name();
        case PING:
            return COMMAND.PONG.name();
        default:
            StringBuilder buffer = new StringBuilder();
            for (COMMAND c : COMMAND.values())
                buffer.append(c).append(",");
            return buffer.toString();
        }

    }

    @Override
    public void process(MetricClientConnection client) {
        String command = new String(client.getRecvBuffer().array(), 0, client.getRecvBuffer().limit()).trim();

        /**
         * if we get \r\n or \n we use previos command
         */
        if (lastCommand != null && command.isEmpty())
            command = lastCommand;

        do {

            COMMAND cmd;
            try {
                cmd = COMMAND.valueOf(command.toUpperCase());
            } catch (IllegalArgumentException e) {
                cmd = COMMAND.HELP;
            }

            client.write(response(cmd).getBytes());
            client.write(String.format("%n").getBytes());

        } while (false);

        lastCommand = command;
    }

}
