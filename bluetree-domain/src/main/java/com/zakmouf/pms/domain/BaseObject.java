package com.zakmouf.pms.domain;

import java.io.Serializable;
import java.text.MessageFormat;

@SuppressWarnings("serial")
public abstract class BaseObject implements Serializable {

    public BaseObject() {

    }

    protected String msg(String pattern, Object... arguments) {
	return MessageFormat.format(pattern, arguments);
    }

}
