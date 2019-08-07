package com.guru.takeaway.ui.utils;

import com.guru.takeaway.model.Restaurant;
import com.guru.takeaway.ui.BaseViewState;

import java.util.ArrayList;
import java.util.List;

public class LoadingState extends BaseViewState<List<Restaurant>> {
    private LoadingState(List<Restaurant> data, int currentState, Throwable error) {
        this.data = data;
        this.error = error;
        this.currentState = currentState;
    }

    public static LoadingState ERROR_STATE = new LoadingState(null, BaseViewState.State.FAILED.value, new Throwable());
    public static LoadingState SUCCESS_STATE = new LoadingState(new ArrayList<>(), State.SUCCESS.value, null);
}
