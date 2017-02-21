package com.zakmouf.pms.domain;

@SuppressWarnings("serial")
public abstract class BaseEntity extends BaseObject {

    protected Long id;

    public BaseEntity() {

    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

}
