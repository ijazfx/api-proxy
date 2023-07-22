package com.github.ijazfx.urlproxy.vaadin.component;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.Route;

import io.graphenee.vaadin.flow.base.GxSecuredView;
import io.graphenee.vaadin.flow.base.GxVerticalLayoutView;

@GxSecuredView
@Route("url")
public class UrlView extends GxVerticalLayoutView {

	private static final long serialVersionUID = 1L;

	@Autowired
	private UrlList list;

	@Override
	protected void decorateLayout(HasComponents rootLayout) {
		rootLayout.add(list);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		list.refresh();
	}

	@Override
	protected String getCaption() {
		return "Manage URLs";
	}

}
