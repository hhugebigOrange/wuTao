package com.xunwei.som.base.model;

@SuppressWarnings("serial")
public class ValueResult<T> extends Result{

	private T result;

    //region getter and setter

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
