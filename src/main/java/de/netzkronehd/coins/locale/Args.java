package de.netzkronehd.coins.locale;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public interface Args {

    @FunctionalInterface
    interface Args0 {
        Component build();

        default void send(Audience audience) {
            audience.sendMessage(build());
        }
    }

    @FunctionalInterface
    interface Args1<A0> {
        Component build(A0 arg0);

        default void send(Audience audience, A0 arg0) {
            audience.sendMessage(build(arg0));
        }
    }

    @FunctionalInterface
    interface Args2<A0, A1> {
        Component build(A0 arg0, A1 arg1);

        default void send(Audience audience, A0 arg0, A1 arg1) {
            audience.sendMessage(build(arg0, arg1));
        }
    }

    @FunctionalInterface
    interface Args3<A0, A1, A2> {
        Component build(A0 arg0, A1 arg1, A2 arg2);

        default void send(Audience audience, A0 arg0, A1 arg1, A2 arg2) {
            audience.sendMessage(build(arg0, arg1, arg2));
        }
    }

    @FunctionalInterface
    interface Args4<A0, A1, A2, A3> {
        Component build(A0 arg0, A1 arg1, A2 arg2, A3 arg3);

        default void send(Audience audience, A0 arg0, A1 arg1, A2 arg2, A3 arg3) {
            audience.sendMessage(build(arg0, arg1, arg2, arg3));
        }
    }

    @FunctionalInterface
    interface Args5<A0, A1, A2, A3, A4> {
        Component build(A0 arg0, A1 arg1, A2 arg2, A3 arg3, A4 arg4);

        default void send(Audience audience, A0 arg0, A1 arg1, A2 arg2, A3 arg3, A4 arg4) {
            audience.sendMessage(build(arg0, arg1, arg2, arg3, arg4));
        }
    }

    @FunctionalInterface
    interface Args6<A0, A1, A2, A3, A4, A5> {
        Component build(A0 arg0, A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5);

        default void send(Audience audience, A0 arg0, A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5) {
            audience.sendMessage(build(arg0, arg1, arg2, arg3, arg4, arg5));
        }
    }

}
