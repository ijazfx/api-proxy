package com.github.ijazfx.testapi.vaadin;

import com.vaadin.flow.router.Route;

import io.graphenee.vaadin.flow.base.GxSecuredView;
import io.graphenee.vaadin.flow.base.GxVerticalLayoutView;

@GxSecuredView
@Route(value = "", layout = MainLayout.class)
public class HomeView extends GxVerticalLayoutView {

    private static final long serialVersionUID = 1L;

}
