package jp.livlog.numexp.share;

import lombok.ToString;

@ToString (callSuper = false)
public final class RefObject <T> {

    public T argValue;

    public RefObject(T refArg) {

        this.argValue = refArg;
    }
}
